package com.perfumes.perfumeswebapp.Services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Configuration
        @Order(1)
        public static class App1ConfigurationAdapter {

                @Bean
                public SecurityFilterChain filterChainApp1(HttpSecurity http, HandlerMappingIntrospector introspector)
                                throws Exception {
                        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
                        http.securityMatcher("/admin/**")
                                        .authorizeHttpRequests(
                                                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                                                                        .requestMatchers(mvcMatcherBuilder
                                                                                        .pattern("/admin/**"))
                                                                        .hasRole("ADMIN"))
                                        // log in
                                        .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                                                        .loginPage("/loginAdmin")
                                                        .loginProcessingUrl("/loginAdmin")
                                                        .failureUrl("/loginAdmin?error=loginError")
                                                        .defaultSuccessUrl("/adminPage"))
                                        // logout
                                        .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                                                        .logoutUrl("/admin_logout")
                                                        .logoutSuccessUrl("/protectedLinks")
                                                        .deleteCookies("JSESSIONID"))
                                        .exceptionHandling(
                                                        httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                                                                        .accessDeniedPage("/403"))
                                        .csrf(AbstractHttpConfigurer::disable);

                        return http.build();
                }
        }

        @Configuration
        @Order(2)
        public static class App2ConfigurationAdapter {

                @Bean
                public SecurityFilterChain filterChainApp2(HttpSecurity http, HandlerMappingIntrospector introspector)
                                throws Exception {
                        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
                        http.securityMatcher("/user*")
                                        .authorizeHttpRequests(
                                                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                                                                        .requestMatchers(mvcMatcherBuilder
                                                                                        .pattern("/user*"))
                                                                        .hasRole("USER"))
                                        // log in
                                        .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                                                        .loginPage("/login")
                                                        .loginProcessingUrl("/login")
                                                        .failureUrl("/loginUser?error=loginError")
                                                        .defaultSuccessUrl("/home"))
                                        // logout
                                        .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                                                        .logoutUrl("/user_logout")
                                                        .logoutSuccessUrl("/protectedLinks")
                                                        .deleteCookies("JSESSIONID"))
                                        .exceptionHandling(
                                                        httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                                                                        .accessDeniedPage("/403"))
                                        .csrf(AbstractHttpConfigurer::disable);
                        return http.build();
                }
        }
}