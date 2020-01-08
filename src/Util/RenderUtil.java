package Util;

import Objects.*;
import math.Vector;
import math.Vector3;

import java.awt.*;
import java.util.Random;


@SuppressWarnings("ALL")
public class RenderUtil {

    static float F0 = 0.8f;

    static float k = 0.2f;

    static float indexLuft = 1.0f;
    static float indexGlas = 1.5f;
    static float indexWasser = 1.3f;
    static boolean totalReflexion = false;

    static float  refrA, refrB;


    public static Vector3 CookTorranceSimple(Vector3 materialDiffuseColor, Vector3 materialSpecularColor, Vector3 normal, Vector3 lightDir, Vector3 viewDir, Vector3 lightColor, float materialRoughness) {
        float NdotL = Math.max(0, normal.dotProduct(lightDir));
        float Rs = 0.0f;

        if (NdotL > 0) {
            Vector3 H = new Vector3(lightDir);
            H.add(viewDir);
            H.normalize();
            float NdotH = Math.max(0, normal.dotProduct(H));
            float NdotV = Math.max(0, normal.dotProduct(viewDir));
            float VdotH = Math.max(0, viewDir.dotProduct(H));//max(0, dot(lightDir, H)); ???


            // Fresnel reflectance
            float F = (float) Math.pow(1.0 - VdotH, 5.0);
            F *= (1.0 - F0);
            F += F0;

            // Microfacet distribution by Beckmann
            float m_squared = materialRoughness * materialRoughness;
            float r1 = (float) (1.0 / (4.0 * m_squared * Math.pow((double) NdotH, 4.0)));
            float r2 = (float) (NdotH * NdotH - 1.0) / (m_squared * NdotH * NdotH);
            float D = (float) (r1 * Math.exp((double) r2));

            // Geometric shadowing
            float two_NdotH = 2.0f * NdotH;
            float g1 = (two_NdotH * NdotV) / VdotH;
            float g2 = (two_NdotH * NdotL) / VdotH;
            float G = (float) Math.min(1.0, Math.min(g1, g2));

            Rs = (float) ((F * D * G) / (Math.PI * NdotL * NdotV));
        }

        float termK = (float) (k + Rs * (1.0 - k));
        Vector3 diffuseCol = new Vector3(materialDiffuseColor);
        diffuseCol.mult(NdotL);
        Vector3 specularCol = new Vector3(materialSpecularColor);
        specularCol.mult(NdotL);
        specularCol.mult(termK);

        Vector3 finalCol = new Vector3(diffuseCol);
        finalCol.add(specularCol);


        // return materialDiffuseColor * lightColor * NdotL + lightColor * materialSpecularColor * NdotL * termK;
        return finalCol;
    }

    public static Vector3 CookTorranceNeu(Ray ray, Vector3 lightDir, Vector3 normal, SceneObject objectToShade, SceneSimple currentScene, boolean refl, float depth) {
        depth--;

      /* if (depth == 1) {
            System.out.println("stop");
        }*/
        Material Material = objectToShade.getMaterial();

        Vector3 rayDir = new Vector3(ray.getDirection());

        float roughness = Material.getRoughness();
        float roughnessSq = (float) Math.pow(roughness, 2);
        Vector3 albedo = Material.getAlbedoColor();
        Vector3 albedoRefl = new Vector3(albedo);

        float i1 = indexLuft;
        float i2 = Material.getRefractiveIndex();

        if (depth != 0) {

            // Veränderter albedo Wert via refl  (1 - reflectivity) * o.albedo + reflectivity * FertigeFarbe(q)
            if (Material.getReflectivity() > 0) {
                Vector3 intersection = ray.intersection1;
                Vector3 reflDir = getReflexionVector(rayDir, normal, objectToShade);

                Vector3 reflColor = getColRecursive(reflDir, intersection, objectToShade, currentScene, depth, false);
                // reflectivity * FertigeFarbe(q)
                reflColor.mult(Material.getReflectivity());
                //(1 - reflectivity) * o.albedo
                albedoRefl.mult((1 - Material.getReflectivity()));
                albedoRefl.add(reflColor);
            }
        }

        // H = (V+L)/2
        Vector3 H = new Vector3(rayDir);
        H.mult(-1);
        H.add(lightDir);
        H.mult(0.5f);
        H.normalize();
        float NdotL;

        if (!refl) {
            NdotL = Math.max(0, normal.dotProduct(lightDir));
        } else {
            // hier einfach zum testen
            NdotL = Math.max(0, normal.dotProduct(lightDir));
        }

        float NdotH = Math.max(0, normal.dotProduct(H));
        float NdotV = Math.max(0, normal.dotProduct(rayDir));
        float VdotH = Math.max(0, rayDir.dotProduct(H));//max(0, dot(lightDir, H));

        // D

        // D = 𝑟^2/ 𝜋 ((𝑁∙𝐻)^2 (r^2-1)+1)^2

        Vector3 normalD = new Vector3(normal);

        float nennerD = (float) (Math.PI * Math.pow((Math.pow(NdotH, 2) * (roughnessSq - 1) + 1), 2));

        float D = roughnessSq / nennerD;

        Vector3 diffusLicht = new Vector3();
        Vector3 F = new Vector3();

        if (!Material.isTransparent()) {

            // w1 = NdotL  w2 = -NdotL2
            Vector3 lightDir2 = getRefractionVector(rayDir, normal, objectToShade);
            float w1 = refrA;//normal.dotProduct(lightDir);
            Vector3 negN = new Vector3(normal);
            negN.mult(-1);
            float w2 = (float)Math.cos(refrB); //negN.dotProduct(lightDir2);
            // Fs = (i1*cos(w1)-i2*cos(w2)/i1*cos(w1)+i2*cos(w2))^2
            float FsTerm1 = (float) (i1 * Math.cos(w1) - i2 * Math.cos(w2));
            float FsTerm2 = (float) (i1 * Math.cos(w1) + i2 * Math.cos(w2));
            float Fs = (float) Math.pow(FsTerm1 / FsTerm2, 2);
            // Fp = (i2*cos(w1)-i1*cos(w2)/i2*cos(w1)+i1*cos(w2))^2
            float FpTerm1 = (float) (i2 * Math.cos(w1) - i1 * Math.cos(w2));
            float FpTerm2 = (float) (i2 * Math.cos(w1) + i1 * Math.cos(w2));
            float Fp = (float) Math.pow(FpTerm1 / FpTerm2, 2);

            float Fr = (Fs + Fp) / 2;

            float Ft = 1 - Fr;

            F = new Vector3(Ft, Ft, Ft);
            // kd = 1-F
            Vector3 kd = new Vector3(1, 1, 1).sub(F);

            diffusLicht = new Vector3(kd.x * albedo.x, kd.y * albedo.y, kd.z * albedo.z);

        } else {

            Vector3 intersection = ray.intersection2;
            totalReflexion = false;
            // w1 = NdotL  w2 = -NdotL2
            Vector3 rayDirRefr = getRefractionVector(rayDir, normal, objectToShade);
            float w1 = refrA;//normal.dotProduct(lightDir); // ist eigentlich A
            Vector3 negN = new Vector3(normal);

            float w2 = (float)Math.cos(refrB);//(negN.dotProduct(rayDirRefr)) * -1; // cos(B)

            // Fs = (i1*cos(w1)-i2*cos(w2)/i1*cos(w1)+i2*cos(w2))^2
            float FsTerm1 = (float) (i1 * Math.cos(w1) - i2 * Math.cos(w2));
            float FsTerm2 = (float) (i1 * Math.cos(w1) + i2 * Math.cos(w2));
            float Fs = (float) Math.pow(FsTerm1 / FsTerm2, 2);


            // Fp = (i2*cos(w1)-i1*cos(w2)/i2*cos(w1)+i1*cos(w2))^2
            float FpTerm1 = (float) (i2 * Math.cos(w1) - i1 * Math.cos(w2));
            float FpTerm2 = (float) (i2 * Math.cos(w1) + i1 * Math.cos(w2));
            float Fp = (float) Math.pow(FpTerm1 / FpTerm2, 2);

            float Fr = (totalReflexion) ? 1 : (Fs + Fp) / 2;

            float Ft = 1 - Fr;
            F = new Vector3(Ft, Ft, Ft);
            Vector3 refracDir = new Vector3(rayDir);

            Vector3 refracColor = getColRecursive(refracDir, intersection, objectToShade, currentScene, depth, false);

            // kd = 1-F
            Vector3 kd = new Vector3(1, 1, 1).sub(F);

            diffusLicht = new Vector3(kd.x * refracColor.x, kd.y * refracColor.y, kd.z * refracColor.z);

        }

        //G
        float halfRoughness = roughness / 2;
        // termG1 = N·V / (N·V(1 – r/2) + r/2);

        float termG1 = NdotV / (NdotV * (1 - halfRoughness) + halfRoughness);
        //termG2 =  N·L / (N·L(1 – r/2) + r/2)
        float termG2 = NdotL / (NdotL * (1 - halfRoughness) + halfRoughness);

        //G = N·V / (N·V(1 – r/2) + r/2) * N·L / (N·L(1 – r/2) + r/2)
        float G = termG1 * termG2;

        //Ergebnis += FarbeQ * IntensitätQ * N·L * (D*F*G + F * ColS + kd * Col)

        Vector3 glanzLicht = new Vector3(F);
        glanzLicht.mult(D);
        glanzLicht.mult(G);

        Vector3 ColS = new Vector3(F.x * albedoRefl.x, F.y * albedoRefl.y, F.z * albedoRefl.z);
        glanzLicht.add(ColS);

        Vector3 finalCol = new Vector3(diffusLicht);
        finalCol.add(glanzLicht);

        finalCol.mult(NdotL);

        return finalCol;

    }


    public static Vector3 getRefractionVector(Vector3 rayDir, Vector3 normal, SceneObject objectToShade) {

        Material mat = objectToShade.getMaterial();
        float i1 = indexLuft;
        float i2 = mat.getRefractiveIndex();

        // i = i1/i2;
        float i = i1 / i2;
        // kommt das hier vor dem check oder danach?
        // a = v1 * N
        Vector3 v1 = new Vector3(rayDir);
        refrA = normal.dotProduct(v1);
        // Erkennung über a = v · n möglich: a < 0? Dann a = -a | sonst a > 0? Dann n = -n
        // Frage hier: ist hier auch a = -v1 * n gemeint ode v1 *n
        if (refrA < 0) refrA = -refrA;
        else normal.mult(-1);
        objectToShade.setNormal(normal);

        // b = sqrrt(1-i^2(1-a^2))
        float b1 = (float) (1 - Math.pow(i, 2));
        float b2 = (float) (1 - Math.pow(refrA, 2));
        float b3 = b1 * b2;
        if (b3 < 0) {
            totalReflexion = true;
        }

        refrB = (float) Math.sqrt(b3);
        // v2 = i*v1 + (i*a-b)*n
        Vector3 V2 = new Vector3(rayDir);
        V2.mult(i);
        float termV2 = i * refrA - refrB;
        Vector3 normalV2 = new Vector3(normal);
        normalV2.mult(termV2);

        V2.add(normalV2);

        return V2;
    }

    public static Vector3 getReflexionVector(Vector3 rayDir, Vector3 normal, SceneObject objectToShade) {

        //Reflexionsrichtung berechnet sich als r = v – 2(n·v)n
        Vector3 n1 = new Vector3(normal);
        float NdotV = n1.dotProduct(rayDir);
        NdotV *= 2;

        n1.mult((NdotV));
        Vector3 reflDir = new Vector3(rayDir);
        reflDir.sub(reflDir, n1);


        return reflDir;
    }


    public static Vector3 getColRecursive(Vector3 rayDir, Vector3 intersection, SceneObject objectToShade, SceneSimple currentScene, float depth, boolean refraction) {

        //strahl von p starten mit Richtung r
        Ray ray = new Ray(intersection, rayDir);

        Vector3 offset = new Vector3(objectToShade.getNormal());
        // bei refraktion rein rücken
        if (refraction) offset.mult(-1);

        offset.mult(0.00001f);
        offset.add(ray.getOrigin());
        ray.setOrigin(offset);

        boolean intersect = false;
        for (SceneObject s : currentScene.getSceneObjects()) {

            if (!s.equals(objectToShade) && !s.isGizmo()) {
                intersect = s.intersect(ray);
            }
        }

        // Background Color if nothing is hit
        Vector3 Color = new Vector3(((float) currentScene.getBgCol().getRed()) / 255, ((float) currentScene.getBgCol().getGreen()) / 255, ((float) currentScene.getBgCol().getBlue()) / 255);
        if (ray.getNearest() != null) {
            SceneObject temp = ray.getNearest();
            SceneObject intersectObj = temp;
            Color = intersectObj.shadeCookTorrance(ray, currentScene, true, depth);
        }
        return Color;
    }

    public static Vector3 getPointinSphere(SphereObject sphereObject) {
        var u = sphereObject.getRadius();
        // create random object
        java.util.Random R = new Random();

        var x1 = R.nextGaussian();
        var x2 = R.nextGaussian();
        var x3 = R.nextGaussian();

        var mag = Math.sqrt(x1 * x1 + x2 * x2 + x3 * x3);
        x1 /= mag;
        x2 /= mag;
        x3 /= mag;

        // Math.cbrt is cube root
        var c = Math.cbrt(u);
        Vector3 point = new Vector3((float) (x1 * c), (float) (x2 * c), (float) (x3 * c));
        point.add(sphereObject.getCenter());
        return point;
    }

    public static Vector3 randomSpherePoint(SphereObject sphereObject ){
        float radius = sphereObject.getRadius();
        var u = Math.random();
        var v = Math.random();
        var theta = 2 * Math.PI * u;
        var phi = Math.acos(2 * v - 1);

        var x = sphereObject.getCenter().x + (radius * Math.sin(phi) * Math.cos(theta));
        var y = sphereObject.getCenter().y + (radius * Math.sin(phi) * Math.sin(theta));
        var z = sphereObject.getCenter().z + (radius * Math.cos(phi));

        return new Vector3((float) x,(float)y,(float)z);
    }


    public static boolean shadowCheck(SceneSimple myScene, Ray myRay, SceneObject castingObject) {
        for (SceneObject s : myScene.getSceneObjects()) {
            Vector3 offset = new Vector3(myRay.getDirection());
            offset.mult(-1);
            offset.mult(0.00001f);
            offset.add(myRay.getOrigin());
            myRay.setOrigin(offset);

            if (!s.equals(castingObject) && !s.isGizmo()) {
                boolean intersect = s.intersect(myRay);

                if (intersect) {
                    return true;
                }
            }
        }
        return false;
    }
}


