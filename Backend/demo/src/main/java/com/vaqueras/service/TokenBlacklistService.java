package com.vaqueras.service;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
public class TokenBlacklistService {
    //esta clase hace el papel de una lista negra en memoria para tokens revocados y su expiracion
    private static final ConcurrentHashMap<String, Long> REVOKED = new ConcurrentHashMap<>();

    public static void revoke(String token, long expEpochSeconds) {
        if (token == null || token.isBlank()) return;
        REVOKED.put(token, expEpochSeconds);
    }

    public static boolean isRevoked(String token) {
        if (token == null || token.isBlank()) return false;

        Long exp = REVOKED.get(token);
        if (exp == null) return false;

        long now = Instant.now().getEpochSecond();
        if (now > exp) {
            REVOKED.remove(token);
            return false;
        }
        return true;
    }
}
