package com.vaqueras.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vaqueras.model.TokenUser; 

public class JwtUtil {
    private static final String SECRET = "VAQUERAS_SUPER_SECRET_CAMBIAME_2025";
    private static final long EXP_SECONDS = 60L * 60L * 6L; // 6 horas para pruebas y demas (como procrastino jasjja)

    public static String generateToken(int idUser, String nickname, String rol) {
        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

        long now = Instant.now().getEpochSecond();
        long exp = now + EXP_SECONDS;

        // Construcción del JSON usando String.format que me es mas simple y efectivo
        String payloadJson = String.format(
                "{\"sub\":%d,\"nick\":\"%s\",\"rol\":\"%s\",\"iat\":%d,\"exp\":%d}",
                idUser,
                escapeJson(nickname),
                escapeJson(rol),
                now,
                exp
        );

        String header = base64Url(headerJson.getBytes(StandardCharsets.UTF_8));
        String payload = base64Url(payloadJson.getBytes(StandardCharsets.UTF_8));
        String signingInput = header + "." + payload;
        String signature = hmacSha256(signingInput, SECRET);

        return signingInput + "." + signature;
    }

    public static TokenUser getUserFromToken(String token) {
        try {
            if (token == null || token.isBlank()) return null;

            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;

            // Validar Firma
            String signingInput = parts[0] + "." + parts[1];
            String expectedSig = hmacSha256(signingInput, SECRET);

            // Comparación segura contra timing attacks
            if (!MessageDigest.isEqual(expectedSig.getBytes(StandardCharsets.UTF_8),
                    parts[2].getBytes(StandardCharsets.UTF_8))) {
                return null; // Firma inválida
            }

            // Decodificar Payload usando GSON
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            JsonObject obj = JsonParser.parseString(payloadJson).getAsJsonObject();

            //Validar Expiración
            long exp = obj.get("exp").getAsLong();
            long now = Instant.now().getEpochSecond();
            if (now > exp) {
                return null; // Token expirado
            }

            // Extraer datos para el usuario
            int sub = obj.get("sub").getAsInt();
            // Validamos que existan los campos para evitar NullPointer
            String nick = obj.has("nick") && !obj.get("nick").isJsonNull() ? obj.get("nick").getAsString() : "";
            String rol = obj.has("rol") && !obj.get("rol").isJsonNull() ? obj.get("rol").getAsString() : "";

            return new TokenUser(sub, nick, rol);

        } catch (Exception e) {
            // Si el JSON está mal formado o hay error de parsing
            return null;
        }
    }

    public static boolean isValid(String token) {
        // si devuelve objeto es true, si es null es false
        return getUserFromToken(token) != null;
    }

    private static String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] sig = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return base64Url(sig);
        } catch (Exception e) {
            throw new RuntimeException("Error creando firma JWT", e);
        }
    }

    private static String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    

    private static String escapeJson(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    
    public static String extractBearerToken(jakarta.servlet.http.HttpServletRequest req) {
        String auth = req.getHeader("Authorization");
        if (auth == null) return null;
        auth = auth.trim();
        if (!auth.startsWith("Bearer ")) return null;
        return auth.substring("Bearer ".length()).trim();
    }
}
