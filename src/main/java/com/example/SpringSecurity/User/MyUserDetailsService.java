package com.example.SpringSecurity.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepo repo;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user1 = repo.findByUsername(username);

        if( user1 == null) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("User not found");
        }
        
        return new UserPrincipal(user1);
    } 
}
