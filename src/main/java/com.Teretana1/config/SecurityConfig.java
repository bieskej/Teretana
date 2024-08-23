package com.Teretana1.config;

import com.Teretana1.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests()
                .requestMatchers("/auth/register/**", "/auth/register").permitAll()
                .requestMatchers("/users/**").hasAuthority("ADMIN")
                .requestMatchers("/trener/**").hasAuthority("TRENER")
                .requestMatchers("/korisnik/**").hasAnyAuthority("KORISNIK")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(myAuthenticationSuccessHandler)
                .loginPage("/auth/login")
                .permitAll()
                .usernameParameter("email")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/").permitAll();

        http.authenticationProvider(authenticationProvider());
        http.headers().frameOptions().sameOrigin();  // If using H2 console or similar

        return http.build();
    }
}
