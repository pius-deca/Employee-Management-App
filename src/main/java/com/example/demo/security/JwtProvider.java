package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.example.demo.security.SecurityConstants.EXPIRATION_TIME;
import static com.example.demo.security.SecurityConstants.SECRET_KEY;

@Component
public class JwtProvider {

    public String generateToken(String username){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("username", claims.getSubject());
        Date currentDate = new Date();
        Date validityTIme = new Date(currentDate.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(validityTIme)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String getUsername(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")){
            return bearer.substring(7, bearer.length());
        }
        return null;
    }

    public boolean validateToken(String token){
        boolean isValid = false;
        try{
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            isValid = true;
        }catch (JwtException | IllegalArgumentException e){
            e.getMessage();
        }
        return isValid;
    }
}
