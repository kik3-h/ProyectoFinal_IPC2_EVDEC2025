package com.vaqueras.util;

public record JwtClaims ( //clase inmutable para representar los claims del JWT
        int userId,
        String nick,
        String rol,
        long iat,
        long exp) {
}
