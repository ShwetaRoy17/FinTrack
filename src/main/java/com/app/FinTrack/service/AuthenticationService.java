package com.app.FinTrack.service;

import com.app.FinTrack.dto.LoginUserDto;
import com.app.FinTrack.dto.RegisterUserDto;
import com.app.FinTrack.model.User;
import com.app.FinTrack.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public User signup(RegisterUserDto input){
        User user = new User()
                .setName(input.getUsername())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()));
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(),input.getPassword()));
    return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }

    public static class JwtUtil {
        @Value("${jwt.secret}")
        private String secretKey;

        @Value("${security.jwt.expirationTime}")
        private String expirationTime;

        public String generateToken(User users){
            return Jwts.builder()
                    .setSubject(users.getName())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(SignatureAlgorithm.HS256,secretKey)
                    .compact();
        }

        public String extractUsername(String token){
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token).getBody()
                    .getSubject();
        }

        public boolean isTokenValid(String token,User user){
            final String userName = extractUsername(token);
            return (user.getName()==userName);
        }

        private boolean isTokenExpired(String token){
            return extractExpiration(token).before(new Date());
        }

        private Date extractExpiration(String token){
            return extractClaim(token, Claims::getExpiration);
        }

        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }

        private Claims extractAllClaims(String token) {
    //return Jwts.p
                    return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        private Key getSignInKey() {
            String secret = "your-256-bit-secret"; // Should be securely stored (e.g., in env vars)
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            return Keys.hmacShaKeyFor(keyBytes);
        }

    }
}
