package com.its.crm.config;

import com.its.crm.security.CustomUserDetailsService;
import com.its.crm.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;
    private final WhitelistProperties whitelistProperties;

    @Value("${app.cors.origins:http://localhost:3000,http://localhost:5173,http://localhost:5174}")
    private String corsOriginsStr;

    @Value("${crm.auth.dev-users:false}")
    private boolean devUsers;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Spring Security 7 (Boot 4) takes the UserDetailsService in the constructor; the setter was removed.
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] allowedOrigins = Arrays.stream(corsOriginsStr.split(","))
                .map(String::trim)
                .toArray(String[]::new);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOriginPatterns(Arrays.asList(allowedOrigins));
                    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(List.of("*"));
                    configuration.setExposedHeaders(List.of("Authorization", "X-Request-Id"));
                    configuration.setAllowCredentials(true);
                    return configuration;
                }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    // Always allow auth and swagger endpoints
                    auth.requestMatchers(
                            "/auth/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**",
                            "/api-docs/**",
                            "/actuator/**"
                    ).permitAll();
                    // Allow any additional whitelist paths from properties
                    List<String> whitelist = whitelistProperties.getWhitelistPaths();
                    if (!whitelist.isEmpty()) {
                        auth.requestMatchers(whitelist.toArray(new String[0])).permitAll();
                    }
                    // PrivateInvest status probe (public, as in the PrivateInvest backend).
                    auth.requestMatchers("/system/**").permitAll();
                    // Boot's error page: lets a status already decided (e.g. 400 for a malformed id) reach the
                    // client instead of being replaced by 403 on the error dispatch.
                    auth.requestMatchers("/error").permitAll();
                    // Development only: with crm.auth.dev-users=true (off by default) the PrivateInvest endpoints
                    // accept a seeded dev user from the X-Dev-User header instead of a JWT (see
                    // com.its.crm.records.security.CurrentUser). With the flag off they require a JWT like
                    // everything else.
                    if (devUsers) {
                        auth.requestMatchers(
                                "/persons/**",
                                "/entities/**",
                                "/entity-links/**",
                                "/metadata/**",
                                "/me",
                                "/list-views/**",
                                "/audit/**"
                        ).permitAll();
                    }
                    auth.anyRequest().authenticated();
                })
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
