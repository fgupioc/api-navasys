package com.fgupioc.api_novasys.security.service;

import com.fgupioc.api_novasys.models.User;
import com.fgupioc.api_novasys.security.models.UserView;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService implements IJwtService {
    private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 días

    @Override
    public String generateToken(User user) throws Exception {
        Map<String, Object> claims = getStringObjectMap(user);

        return getToken(claims, user);
    }

    private static Map<String, Object> getStringObjectMap(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("code", user.getId().toString());
        claims.put("firstName", user.getName());
        claims.put("lastName", user.getLastname());
        claims.put("type", user.getRol().getName());
        return claims;
    }

    @Override
    public String getTokenFormRequest(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        User user = (User) userDetails;
        return (username.equals(user.getDocument()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private String getToken(Map<String, Object> claims, User user) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(user.getDocument())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() * 1000 * 60 * 24))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] encodedKey = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(encodedKey);
    }

    private Date getExpiration(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    @Override
    public String generateRefreshToken(User user) {
        String refreshToken = buildToken(user, REFRESH_TOKEN_EXPIRATION);

        // Guardar refresh token en la base de datos
        /*
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION));
        userRepository.save(user);
        */

        return refreshToken;
    }

    private String buildToken(User user, long expiration) {
        Map<String, Object> claims = getStringObjectMap(user);

        return Jwts.builder()
                .claims(claims)
                .subject(user.getDocument())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }


    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(refreshToken);

            Date expiration = getExpiration(refreshToken);
            return expiration.after(new Date());
            // Verificar también en la base de datos
            /*
            Optional<User> user = userRepository.findByRefreshToken(refreshToken);
            return user.isPresent() && user.get().getRefreshTokenExpiry().after(new Date());
            */

        } catch (Exception e) {
            log.error("Refresh token inválido: {}", e.getMessage());
            return false;
        }
    }

    public String getDocumentFromRefreshToken(String refreshToken) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload()
                .getSubject();
    }

    public UserView decodeToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        String document = getClaimFromToken(token, Claims::getSubject);
        String code = claims.get("code", String.class);
        String firstName = claims.get("firstName", String.class);
        String lastName = claims.get("lastName", String.class);
        return UserView.builder()
                .id(Long.valueOf(code))
                .document(document)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}
