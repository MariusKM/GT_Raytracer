package Util;

import Objects.*;
import math.Vector;
import math.Vector3;

import java.awt.*;


public class RenderUtil {

    static float F0 = 0.8f;

    static float k = 0.2f;


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

    // TODO CLEAN UP PARAMETERS!!
    public static Vector3 CookTorrance(Vector3 lightDir, Vector3 normal, Vector3 rayDir,Vector3 rayDirN, Vector3 intersection, SceneObject objectToShade, SceneSimple currentScene ,boolean refl) {

        Material Material = objectToShade.getMaterial();

        float metalness = Material.getMetalness();
        float roughness = Material.getRoughness();
        float roughnessSq = (float) Math.pow(roughness, 2);
        Vector3 albedo = Material.getAlbedoColor();

        // H = (V+L)/2
        Vector3 H = new Vector3(rayDirN);
        H.mult(-1);
        H.add(lightDir);
        H.mult(0.5f);
        H.normalize();
        float NdotL;
        if (!refl){
             NdotL = Math.max(0, normal.dotProduct(lightDir));
        }else{
             NdotL = 0.5f;
        }
      //  float NdotL = Math.max(0, normal.dotProduct(lightDir));
        float NdotH = Math.max(0, normal.dotProduct(H));
        float NdotV = Math.max(0, normal.dotProduct(rayDirN));
        float VdotH = Math.max(0, rayDirN.dotProduct(H));//max(0, dot(lightDir, H));
        if (NdotL > 0 && refl) {
               System.out.println("!");
        }
        // D

        // D = 𝑟^2/ 𝜋 ((𝑁∙𝐻)^2 (r^2-1)+1)^2


        Vector3 normalD = new Vector3(normal);

        float nennerD = (float) (Math.PI * Math.pow((Math.pow(NdotH, 2) * (roughnessSq - 1) + 1), 2));

        float D = roughnessSq / nennerD;

        // F

        //(1 – metalness) * 0.04f
        float termF0 = (1 - metalness) * 0.04f;
        //metalness * albedo
        Vector3 F0 = new Vector3(albedo);
        F0.mult(metalness);
        // F0 = (1 – metalness) * 0.04f + metalness * albedo
        Vector3 termF0v = new Vector3(termF0, termF0, termF0);
        F0.add(termF0v);


        Vector3 normalF = new Vector3(normal);
        //termF =(1 – N·V)^5

        float termF = (float) Math.pow(1 - (NdotV), 5);

        //F = F0+ (1 – F0)(1 – N·V)^5
        Vector3 F02 = new Vector3(1, 1, 1).sub(F0);
        F02.mult(termF);
        Vector3 F = new Vector3(F0);
        F.add(F02);


        //kd = (1 – F)(1 – metallness)
        Vector3 kd = new Vector3(1, 1, 1).sub(F);
        kd.mult(1 - metalness);


        //G
        float halfRoughness = roughness / 2;
        // termG1 = N·V / (N·V(1 – r/2) + r/2);
        Vector3 normalG = new Vector3(normal);
        float termG1 = NdotV / (NdotV * (1 - halfRoughness) + halfRoughness);
        //termG2 =  N·L / (N·L(1 – r/2) + r/2)
        float termG2 = NdotL / (NdotL * (1 - halfRoughness) + halfRoughness);

        //G = N·V / (N·V(1 – r/2) + r/2) * N·L / (N·L(1 – r/2) + r/2)

        float G = termG1 * termG2;

        // Farbe = (N·L)(kd* albedo + D * F * G)
        Vector3 normalCol = new Vector3(normal);

        // Veränderter albedo Wert via refl  (1 - reflectivity) * o.albedo + reflectivity * FertigeFarbe(q)

        Vector3 albedoRefl = new Vector3(albedo);
        if (Material.getReflectivity()> 0){
            Vector3 reflColor = Reflexion(lightDir, normal, rayDir, rayDirN,intersection, objectToShade, currentScene);
            // reflectivity * FertigeFarbe(q)
            reflColor.mult(Material.getReflectivity());

            //(1 - reflectivity) * o.albedo
            albedoRefl.mult((1 - Material.getReflectivity()));
            albedoRefl.add(reflColor);
        }



        Vector3 diffusLicht = new Vector3(kd.x * albedoRefl.x, albedoRefl.y * albedoRefl.y, kd.z * albedoRefl.z);

        Vector3 glanzLicht = new Vector3(F);
        glanzLicht.mult(D);
        glanzLicht.mult(G);

        Vector3 finalCol = new Vector3(diffusLicht);
        finalCol.add(glanzLicht);

        finalCol.mult(NdotL);
        return finalCol;

    }

    public static Vector3 Reflexion(Vector3 lightDir, Vector3 normal, Vector3 rayDir,Vector3 rayDirN, Vector3 intersection, SceneObject objectToShade, SceneSimple currentScene) {

        //Reflexionsrichtung berechnet sich als r = v – 2(n·v)n
        float NdotV = normal.dotProduct(rayDir);
        Vector3 n = new Vector3(normal);
        n.mult(2 * (NdotV));
        Vector3 reflDir = new Vector3(rayDir);
        reflDir.sub(reflDir, n);

        //Reflexionsstrahl von p starten mit Richtung r
        Ray reflRay = new Ray(intersection, reflDir);
        Vector3 offset = new Vector3(reflRay.getDirection());
        offset.mult(-1);
        offset.mult(0.00001f);
        offset.add(reflRay.getOrigin());
        reflRay.setOrigin(offset);

        for (SceneObject s : currentScene.getSceneObjects()) {

            if (!s.equals(objectToShade) && !s.isGizmo()) {
                boolean intersect = s.intersect(reflRay);
            }
        }
        Vector3 reflColor = new Vector3(0, 0, 0);
        if (reflRay.getNearest() != null) {
            SceneObject temp = reflRay.getNearest();
            SceneObject intersectObj = temp;
            Vector3 reflDirN = new Vector3(reflDir);
            reflDir.mult(-1);
            // TODO FOR SOME REASON THIS IS ALWAYS BLACK
            reflColor = intersectObj.shadeCookTorrance(reflDir,reflDirN, currentScene, reflRay.getT0(), true);
        }
        if (reflColor.x != 0 ||reflColor.y != 0 ||reflColor.z != 0) System.out.println("yaay");

        return reflColor;
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


