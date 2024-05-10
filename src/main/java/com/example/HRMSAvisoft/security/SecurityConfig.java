package com.example.HRMSAvisoft.security;


import com.example.HRMSAvisoft.service.JWTService;
import com.example.HRMSAvisoft.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JWTService jwtService, UserService userService) {
        this.jwtAuthenticationFilter = new JWTAuthenticationFilter(new JWTAuthenticationManager(jwtService, userService));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrfSpec -> csrfSpec.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**")
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());
        http.addFilterBefore(jwtAuthenticationFilter,  UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
