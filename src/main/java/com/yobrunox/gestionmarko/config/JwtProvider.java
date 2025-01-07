package com.yobrunox.gestionmarko.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

@Service
public class JwtProvider {
    private static final Logger logger = Logger.getLogger(JwtProvider.class.getName());

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long jwtexpiration;
    @Value("${jwt.refresh-token.expiration}")
    private long refreshExpiration;


    public String generateToken(final UserDetails userDetails){
        return buildToken(new HashMap<>(),userDetails,jwtexpiration);
    }

    public String refreshToken(final UserDetails userDetails){
        return buildToken(new HashMap<>(),userDetails,refreshExpiration);
    }



    public String buildToken(Map<String,Object> extraClaims, UserDetails userDetails, long expiration) {
        //LocalDateTime
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                //.claim("roles",userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))

                .setExpiration(new Date(new Date(System.currentTimeMillis()).getTime()+ expiration))
                //.setExpiration()
                .signWith(getKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getKey() {
        byte[] secretBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token); // Verificar el token
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token)
    {
        return getExpiration(token).before(new Date());
    }
    public Date getExpiration(String token)
    {
        return getClaim(token, Claims::getExpiration);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token,Claims::getSubject);
    }





    public <T> T getClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


  //  public Date getExpirationDateFromToken(String token) {}
   // public boolean validateToken(String token) {}
}
