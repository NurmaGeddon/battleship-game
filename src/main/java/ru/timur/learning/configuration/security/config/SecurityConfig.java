package ru.timur.learning.configuration.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final UserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeHttpRequests(authorize ->
                authorize.requestMatchers("/registration")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
        );

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/battleship")
                .failureUrl("/signIn?error")
                .usernameParameter("login")
                .passwordParameter("password")
                .permitAll();

        return http.build();
    }

    @Autowired
    public void bindUserDetailsService(AuthenticationManagerBuilder builder, PasswordEncoder passwordEncoder) throws Exception {
        builder.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
