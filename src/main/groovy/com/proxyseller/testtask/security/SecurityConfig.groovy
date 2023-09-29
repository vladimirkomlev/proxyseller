package com.proxyseller.testtask.security

import com.proxyseller.testtask.data.SwitterUser
import com.proxyseller.testtask.data.SwitterUserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

@Configuration
class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder()
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http
                .authorizeHttpRequests {
                    it
                            .requestMatchers(mvcMatcherBuilder.pattern("/page")).hasRole("USER")
                            .requestMatchers(mvcMatcherBuilder.pattern("/publication")).hasRole("USER")
                            .requestMatchers(mvcMatcherBuilder.pattern("/publication/*")).hasRole("USER")
                            .requestMatchers(mvcMatcherBuilder.pattern("/subscription")).hasRole("USER")
                            .requestMatchers(mvcMatcherBuilder.pattern("/subscription/*")).hasRole("USER")
                            .requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
                            .requestMatchers(mvcMatcherBuilder.pattern("/**")).permitAll()
                }.formLogin {
                    it
                            .loginPage("/login")
                            .defaultSuccessUrl("/page")
                }.logout {
                    it
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/")
                }
        return http.build()
    }

    @Bean
    UserDetailsService userDetailsService(SwitterUserRepository userRepository) {
        return (username) -> {
            SwitterUser user = userRepository.findByUsername(username)
            if (user != null) return user

            throw new UsernameNotFoundException("User '" + username + "' not found")
        }
    }
}