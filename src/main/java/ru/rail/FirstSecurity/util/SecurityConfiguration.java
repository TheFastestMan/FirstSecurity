package ru.rail.FirstSecurity.util;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import static ru.rail.FirstSecurity.entity.Role.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // связка с контроллере @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')") //для доступа
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(CsrfConfigurer::disable)
        http // по дефолту csrf включен
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/login", "/registration", "/v3/api-docs/", "/swagger-ui/").permitAll()
                        .requestMatchers("/admin/**").hasRole(ADMIN.getAuthority())
                        .requestMatchers(antMatcher("/users/{\\d}/delete")).hasAnyAuthority(
                                ADMIN.getAuthority())
                        .requestMatchers(antMatcher("/users/{\\d}/update")).hasAnyAuthority(
                                ADMIN.getAuthority(), OPERATOR.getAuthority(), USER.getAuthority())
                        .requestMatchers("/users").hasAnyAuthority(ADMIN.getAuthority(),
                                OPERATOR.getAuthority())

                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/user")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID"));

        return http.build();
    }



}