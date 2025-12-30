package com.example.SpringSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.SpringSecurity.User.MyUserDetailsService;

@Configuration
@EnableWebSecurity //Configurar seguridad de nuestra aplicacion
public class SecurityConfiguration {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Bean
    //Interfaz de Spring Security para configurar la seguridad
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception { 
        return httpSecurity
                .csrf(csrf->csrf.disable()) //Si lo ponemos --> Inhabilitamos la seguridad que ofrece Spring frente a CSRF
                .authorizeHttpRequests(authz -> authz
                    .requestMatchers("/v1/index2").permitAll() //Endpoints que no van a necesitar ninguna autorización
                    .anyRequest().authenticated()
                )
                .formLogin(frm -> frm.permitAll()
                            .successHandler(successHandler())) //URL a donde se redirige de iniciar sesión

                .sessionManagement(sm->sm
                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) 
                                .invalidSessionUrl("/login")
                                .maximumSessions(1)
                                .expiredUrl("/login") //Redireccion cuando expira la sesión
                                .sessionRegistry(sessionRegistry())) 
                .httpBasic(hb -> {}) //Enviamos nuestro usuario y contraseña en el HEADER de la petición
                                                
                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
    
    public AuthenticationSuccessHandler successHandler(){ //Esto es lo que sucede si se inicia sesión exitosamente 
        return ((request, response, authentication)->{
            response.sendRedirect("/");
        });
    }


    @Bean public 
    PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(12); 
    }


    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder){
         
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder); //Usa el bean PasswordEncoder inyectado
        return provider;
    }

}
