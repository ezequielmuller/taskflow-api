package com.taskbaseapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

  private static final String SECRET = "chave_token";
  private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

  private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 3; // 3 horas

  public static String gerarToken(String email) {
    return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
  }

  // validar token
  public static Claims validarToken(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
  }
}