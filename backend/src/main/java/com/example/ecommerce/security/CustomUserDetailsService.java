package com.example.ecommerce.security;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository users;
    public CustomUserDetailsService(UserRepository users) { this.users = users; }
    @Override public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword()).roles(user.getRole()).build();
    }
}
