package util;

import components.Scene;
import math.Vector3;
import objects.Ray;
import objects.SceneObject;

public class CookTorrance extends ShadingModel {
    @Override
    public Vector3 computeColor(Ray ray, Vector3 lightDir, Vector3 normal, SceneObject objectToShade, Scene currentScene, boolean refl, float depth) {
        depth--;
        //  System.out.println(depth);
        if (depth<  3) {
            //     System.out.println(depth);
        }
        Material Material = objectToShade.getMaterial();

        Vector3 rayDir = new Vector3(ray.getDirection());

        float roughness = Material.getRoughness();
        float roughnessSq = (float) Math.pow(roughness, 2);
        Vector3 albedo = Material.getAlbedoColor();


        float i1 = indexLuft;
        float i2 = Material.getRefractiveIndex();
        Vector3 albedoRefl = new Vector3(0,0,0);
        if (depth != 0) {

            // Veränderter albedo Wert via refl  (1 - reflectivity) * o.albedo + reflectivity * FertigeFarbe(q)
            if (Material.getReflectivity() > 0) {
                albedoRefl = new Vector3(albedo);
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
            // this is here to generste fresnel values
            Vector3 rayDirRefr = getRefractionVector(rayDir, normal, objectToShade);
            // w1 = NdotL  w2 = -NdotL2

            float w1 = refrA;
            Vector3 negN = new Vector3(normal);
            negN.mult(-1);
            float w2 = (float)Math.cos(refrB);
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

            Vector3 intersection = ray.intersection1;
            totalReflexion = false;

            if (intersection.z < 1f){
                System.out.println("!");
            }
            // w1 = NdotL  w2 = -NdotL2
            Vector3 rayDirRefr = getRefractionVector(rayDir, normal, objectToShade);
            float w1 = refrA;
            Vector3 negN = new Vector3(normal);

            float w2 = (float)Math.cos(refrB);

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

            Vector3 refracColor = (depth != 0)? getColRecursive(rayDirRefr, intersection, objectToShade, currentScene, depth, true): new Vector3(0,0,0);

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
        // if (Material.isTransparent()) ColS = new Vector3(0,0,0);
        glanzLicht.add(ColS);

        Vector3 finalCol = new Vector3(diffusLicht);
        finalCol.add(glanzLicht);

        finalCol.mult(NdotL);

        return finalCol;

    }
}
