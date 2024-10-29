package com.contact.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class JWTSecurityClass {
        @Autowired
        private JWTDAOClass jwtdaoClass;

        @Autowired
        private AuthenticationDone authenticationDone;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        public UserDetailsService userDetailsService() {
                return jwtdaoClass;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

                return security
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/static", "/static/**", "/CSS/**", "/JS/**", "/Image/**",
                                                                "/OTHER/**", "/logo/**", "/error")
                                                .permitAll()
                                                .requestMatchers("/", "/contact", "/contact/**", "/terms",
                                                                "/hyperlink_policy", "/privacy_policy",
                                                                "/searchSuggession", "/searchItemClicked",
                                                                "/searchItemClicked/**", "/sitemap", "/sitemap.xml",
                                                                "/error", "/contactUsPage", "/addContactManually",
                                                                "/disclimer","/keep-alive","/cookie-policy", "/csrf")
                                                .permitAll()
                                                .requestMatchers("/ads.txt", "/robots.txt", "/dmca-validation.html",
                                                                "/BingSiteAuth.xml",
                                                                "/dad6d65d5c644998b38e6be0ea987c7d.txt",
                                                                "/googleaa10623011f14756.html")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                //.defaultSuccessUrl("/dashboard", true) // Redirect to /dashboard on successful login
                                                .successHandler(authenticationDone) // Set the custom handler
                                )
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login") // Redirect to the home page after logout
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"))
                                .csrf(csrf -> csrf
                                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Create session only if required
                                                .maximumSessions(1) // Limit to one session per user
                                                .maxSessionsPreventsLogin(false) // Prevents new login if session limit
                                                .expiredUrl("/login?expired=true") // Redirect to login page if session expires
                                )
                                .sessionManagement(session -> session
                                                .sessionFixation().migrateSession() // Migrate session on login to
                                                                                    // prevent session fixation
                                )
                                .build();
        }

}
