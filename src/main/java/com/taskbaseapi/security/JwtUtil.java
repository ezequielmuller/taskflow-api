package com.taskbaseapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

  private static final String SECRET = "minha_chave_super_secreta_com_mais_de_32_caracteres_123";
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