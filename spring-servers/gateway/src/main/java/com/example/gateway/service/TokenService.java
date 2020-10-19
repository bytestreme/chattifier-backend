package com.example.gateway.service;


import com.example.gateway.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bson.internal.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;


@Component
public class TokenService implements Serializable {

  @Value("${jwt.key}")
  private String SIGN_KEY;

  @Value("${jwt.roles-claim}")
  private String ROLES_CLAIM;

  @Value("${jwt.token-version-claim}")
  private String TOKEN_VERSION_CLAIM;


  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(Base64.encode(SIGN_KEY.getBytes()))
        .parseClaimsJws(token)
        .getBody();
  }

  public String generateToken(User user, String version) {
    final List<String> authorities = new ArrayList<>(user.getRoles());
    Map<String, Object> claims = new HashMap<>();
    claims.put(ROLES_CLAIM, authorities);
    claims.put(TOKEN_VERSION_CLAIM, version);

    return Jwts.builder()
        .setSubject(user.getId())
        .addClaims(claims)
        .signWith(SignatureAlgorithm.HS256, Base64.encode(SIGN_KEY.getBytes()))
        .compact();
  }

}
