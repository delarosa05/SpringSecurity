package com.example.SpringSecurity.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/v1")
public class CustomerController1 {

    @Autowired
    private SessionRegistry sessionRegistry;

    
    @GetMapping("/index")
    public String getCustomer() {
        return "Hello Customer!";
    }

    @GetMapping("/index2")
    public String index2() {
        return "Hello world from index2!";
    }

    @GetMapping("/session")
    public ResponseEntity<?> getDetailSession(){

        String sessionId = "";
        User userObject = null;

        List<Object> sessions = sessionRegistry.getAllPrincipals();

        for (Object session:sessions){
            if(session instanceof User){
                userObject = (User) session;
            }

            List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session, false);
            for(SessionInformation sessionInformation:sessionInformations){
                sessionId= sessionInformation.getSessionId();
            }
        }

        Map<String,Object> response = new HashMap<>();
        response.put("response", "Hello World");
        response.put("sessionId", sessionId);
        response.put("sessionUser", userObject);
        return (ResponseEntity<?>) ResponseEntity.ok(response);
    }


    @GetMapping("/")
    public String getSession(HttpServletRequest httpServletRequest) {
        return "Tu id de sesión: "+ httpServletRequest.getSession().getId();
    }

    //Hay que obtener el token csrf para las peticiones POST, PUT y DELETE
    //Hay que añadir este token a la cabecera con X-CSRF-TOKEN

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest httpServletRequest) {
        return (CsrfToken) httpServletRequest.getAttribute("_csrf");
    }
    
    
}
