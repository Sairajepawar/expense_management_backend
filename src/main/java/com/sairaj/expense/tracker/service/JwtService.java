package com.sairaj.expense.tracker.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtService {

    @Value("${security.jwt.secret}")
    private String SECRET;

    private final long EXPIRATION_TIME = 1000*60*60; //1 hour

    public String generateToken(String Username){
        return Jwts.builder()
                .setSubject(Username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
