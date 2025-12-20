package com.vaqueras.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JwtUtil {
    private static final String SECRET = "VAQUERAS_SUPER_SECRET_CAMBIAME_2025";
    private static final long EXP_SECONDS = 60L * 60L * 6L; // 6 horas

    public static String generateToken(int idUser, String nickname, String rol) {
        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

        long now = Instant.now().getEpochSecond();
        long exp = now + EXP_SECONDS;

        // payload mínimo útil para Angular: sub(id), nick, rol, iat, exp
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

    public static boolean isValid(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;

            String signingInput = parts[0] + "." + parts[1];
            String expectedSig = hmacSha256(signingInput, SECRET);

            // comparación constante para evitar timing attacks (buenas prácticas)
            if (!MessageDigest.isEqual(expectedSig.getBytes(StandardCharsets.UTF_8),
                    parts[2].getBytes(StandardCharsets.UTF_8))) {
                return false;
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            long exp = readLongClaim(payloadJson, "exp");
            long now = Instant.now().getEpochSecond();

            return now <= exp;
        } catch (Exception e) {
            return false;
        }
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

    private static long readLongClaim(String json, String key) {
        // parser mínimo (sin libs) buscando: "key":123
        String pattern = "\"" + key + "\":";
        int idx = json.indexOf(pattern);
        if (idx < 0) return -1;
        idx += pattern.length();
        int end = idx;
        while (end < json.length() && Character.isDigit(json.charAt(end))) end++;
        return Long.parseLong(json.substring(idx, end));
    }

    private static String escapeJson(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
