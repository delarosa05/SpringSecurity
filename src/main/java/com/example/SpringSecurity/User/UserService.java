package com.example.SpringSecurity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SpringSecurity.JWTUtils.JWTService;
import com.example.SpringSecurity.JWTUtils.JWTService;

@Service
public class UserService {
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); //Guardamos la contraseña codificada con el encoder

    public UserService(UserRepo userRepo){
        this.userRepo=userRepo;
    }
    
    public User register(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public String verify(User user) {
        org.springframework.security.core.Authentication auth = authenticationManager  
        .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(auth.isAuthenticated()){
            return jwtService.createJWT(user.getUsername()); //Metodo que crea el token
        }
        return "Fallo";
    }
}
