package math;

public class RenderUtil {
    Vector3 diffuseColor = new Vector3(1, 0, 0);
    Vector3 specularColor = new Vector3(1, 1, 1);
    static float F0 = 0.8f;

    static float k = 0.2f;
    Vector3 lightColor = new Vector3(1, 1, 1);

    public static Vector3 CookTorrance(Vector3 materialDiffuseColor,
                                       Vector3 materialSpecularColor,
                                       Vector3 normal,
                                       Vector3 lightDir,
                                       Vector3 viewDir,
                                       Vector3 lightColor,
                                       float materialRoughness) {
        float NdotL = Math.max(0, normal.dotProduct(lightDir));
        float Rs = 0.0f;

        if (NdotL > 0) {
            Vector3 H =  new Vector3(lightDir);
            H.add(viewDir);
            H.normalize();
            float NdotH = Math.max(0, normal.dotProduct(H));
            float NdotV = Math.max(0, normal.dotProduct(viewDir));
            float VdotH = Math.max(0, viewDir.dotProduct(H));//max(0, dot(lightDir, H)); ???


            // Fresnel reflectance
            float F = (float)Math.pow(1.0 - VdotH, 5.0);
            F *= (1.0 - F0);
            F += F0;

            // Microfacet distribution by Beckmann
            float m_squared = materialRoughness * materialRoughness;
            float r1 = (float)(1.0 / (4.0 * m_squared * Math.pow((double)NdotH, 4.0)));
            float r2 = (float)(NdotH * NdotH - 1.0) / (m_squared * NdotH * NdotH);
            float D = (float)(r1 * Math.exp((double)r2));

            // Geometric shadowing
            float two_NdotH = 2.0f * NdotH;
            float g1 = (two_NdotH * NdotV) / VdotH;
            float g2 = (two_NdotH * NdotL) / VdotH;
            float G = (float)Math.min(1.0, Math.min(g1, g2));

            Rs = (float)((F * D * G) / (Math.PI * NdotL * NdotV));
        }

        float termK = (float)(k + Rs * (1.0 - k));
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

}
