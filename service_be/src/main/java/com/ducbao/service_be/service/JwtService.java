package com.ducbao.service_be.service;

import com.ducbao.common.util.Util;
import com.ducbao.service_be.model.constant.RedisConstant;
import com.ducbao.service_be.model.entity.UserModel;
import com.ducbao.service_be.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    @Value("${spring.jwt.secret.token}")
    private String secret;

    @Value("${spring.jwt.expire.refresh}")
    private long expireRefresh;

    @Value("${spring.jwt.expire.token}")
    private long expireToken;

    private final RedissonClient redisson;
    private final UserRepository userRepository;

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserModel userModel = userRepository.findByUsername(userDetails.getUsername()).get();
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireToken);

        Map<String, Object> claims = new HashMap<>();
        UserModel user = userRepository.findById(userModel.getId()).orElse(null);
        claims.put("avatar", user.getAvatar());
        claims.put("enabled", user.isEnabled());
        List<String> role = user.getRole();
        claims.put("role", role);

        String tokenGenerate = Jwts.builder()
                .setIssuedAt(now)
                .setClaims(claims)
                .setSubject(userModel.getId())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, getSignKey())
                .compact();

        RBucket<String> token = redisson.getBucket(tokenGenerate);
        log.info("Retrieved userId from Redis: {}", userModel.getId());
        token.set(userModel.getId(), expireToken, TimeUnit.MILLISECONDS);
        log.info("ID after saving to Redis: {}", token.get());
        return tokenGenerate;
    }

    // GenerateTokenById
    public String generateTokenByIdUser(String idUser) {
        Date now = new Date();
        Date expDate = new Date(now.getTime() + expireToken);

        Map<String, Object> claims = new HashMap<>();
        UserModel user = userRepository.findById(idUser).orElse(null);
        claims.put("avatar", user.getAvatar());
        claims.put("enabled", user.isEnabled());
        List<String> role = user.getRole();
        claims.put("role", role);

        String tokenGenerate = Jwts.builder()
                .subject(idUser)
                .issuedAt(now)
                .expiration(expDate)
                .signWith(SignatureAlgorithm.HS512, getSignKey())
                .compact();

        try {
            RBucket<String> token = redisson.getBucket(tokenGenerate);
            token.set(idUser, expireToken, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("Save token to redis failed: ", e);
        }

        return tokenGenerate;
    }

    // Ma hoa key
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Trich xuat tat ca thong tin
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public boolean validateToken(String authToken) {
        try {
            String userId = null;

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(getSignKey())
                        .build()
                        .parseClaimsJws(authToken)
                        .getBody();

                userId = claims.getSubject();
            } catch (Exception e) {
                log.error("jwt token not verify with jwt secret");
                return false;
            }

            // Get user access token from redis and validate
            RBucket<String> bucket = redisson.getBucket(authToken);
            String jwtsFromRedis = bucket.get();

            if (!Util.isNullOrEmpty(jwtsFromRedis) && !Util.isNullOrEmpty(userId) && jwtsFromRedis.equals(userId)) {
                return true;
            }
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: ", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: ", ex);
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty. ", ex);
        }
        return false;
    }

    /***
     * Get user id from JWT token
     * @param token
     * @return
     */

    public String getUserIdFromJWT(String token) {

        try {
            RBucket<String> bucket = redisson.getBucket(token);
            String userId = bucket.get();

            return userId;
        } catch (Exception e) {
            log.error("Get user id by jwt token failed: ", e);

            return null;
        }
    }

}
