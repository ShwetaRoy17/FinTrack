package com.app.FinTrack.security;

import com.app.FinTrack.model.User;

public class JwtUtil {
    @Value("${jwt.secret}")
    private final String secretKey;

    public String generateToken(User user){
        return "";
    }
}
