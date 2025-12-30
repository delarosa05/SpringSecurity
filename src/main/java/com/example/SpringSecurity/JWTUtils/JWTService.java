package com.example.SpringSecurity.JWTUtils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Base64.Decoder;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    private String secret = "";

    public JWTService(){ //Genera una clave aleatoria 
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secret = Base64.getEncoder().encodeToString(sk.getEncoded());
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String createJWT(String username){
        
        Map<String,Object> claims = new HashMap<>(); //Aqui puedo meter info adicional (ROLES, PERMISOS,...)
        return Jwts.builder().claims().add(claims).subject(username).
        issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() + 60* 60 * 30))
        .and()
        .signWith(getKey())
        .compact(); 

    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret); //Pasa de BASE64 a Bytes 
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
