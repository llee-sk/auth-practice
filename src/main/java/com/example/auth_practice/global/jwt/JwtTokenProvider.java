package com.example.auth_practice.global.jwt;

import com.example.auth_practice.global.jwt.exception.InvalidJwtTokenException;
import com.example.auth_practice.global.jwt.exception.JwtTokenExpiredException;
import com.example.auth_practice.member.enums.MemberRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

@Slf4j
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpirationTime;

    @Getter
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpirationTime;

    private SecretKey getSigningKey(){
        byte[] keyBytes = Base64.getDecoder().decode(secretKey); // Base64로 인코딩된 secret Key 텍스트를 바이트 배열로 디코딩
        return Keys.hmacShaKeyFor(keyBytes); //-> JJWT에서 제공하는 메서드로, byte 배열을 기반으로 Key 객체를 만든다.
                                            //-> 이때, 만들어진 Key 객체는 JWT를 서명(sign) 하거나, 서명을 검증할 때 사용한다.
    }

    public String createAccessToken(Long id,String email, MemberRole role){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationTime);

        return Jwts.builder()
                .subject(String.valueOf(id))
                .claim("email", email)
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String createRefreshToken(Long id){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationTime);

        return Jwts.builder()
                .subject(String.valueOf(id))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .id(UUID.randomUUID().toString())
                .compact();
    }

    private Claims parseClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getMemberIdFromToken(String token){
        String subject = parseClaims(token).getSubject();
        return Long.valueOf(subject);
    }

    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException();
        } catch (SecurityException | MalformedJwtException e) {
            throw new InvalidJwtTokenException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtTokenException();
        }
    }
}
