package math;




public class RenderUtil {

    static float F0 = 0.8f;

    static float k = 0.2f;


    public static Vector3 CookTorranceSimple(Vector3 materialDiffuseColor,
                                             Vector3 materialSpecularColor,
                                             Vector3 normal,
                                             Vector3 lightDir,
                                             Vector3 viewDir,
                                             Vector3 lightColor,
                                             float materialRoughness) {
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


    public static Vector3 CookTorrance(Vector3 lightDir, Vector3 normal, Vector3 rayDir, Material mat) {

        Material Material = mat;

        float metalness = mat.getMetalness();
        float roughness = mat.getRoughness();
        float roughnessSq = (float) Math.pow(roughness, 2);
        Vector3 albedo = mat.getAlbedoColor();

        // H = (V+L)/2
        Vector3 H = new Vector3(rayDir);
        H.mult(-1);
        H.add(lightDir);
        H.mult(0.5f);
        H.normalize();

        float NdotL = Math.max(0, normal.dotProduct(lightDir));
        float NdotH = Math.max(0, normal.dotProduct(H));
        float NdotV = Math.max(0, normal.dotProduct(rayDir));
        float VdotH = Math.max(0, rayDir.dotProduct(H));//max(0, dot(lightDir, H));
        if (NdotL > 0) {
            //   System.out.println("!");
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


        Vector3 diffusLicht = new Vector3(kd.x * albedo.x, kd.y * albedo.y, kd.z * albedo.z);

        Vector3 glanzLicht = new Vector3(F);
        glanzLicht.mult(D);
        glanzLicht.mult(G);

        Vector3 finalCol = new Vector3(diffusLicht);
        finalCol.add(glanzLicht);

        finalCol.mult(NdotL);
        return  finalCol;

    }


}
