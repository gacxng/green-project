package com.greenfinal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.greenfinal.role.UserRole;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true)
public class SecurityConfig {
	
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
        		.requestMatchers(new AntPathRequestMatcher("/resources/**"))
        		.requestMatchers(new AntPathRequestMatcher("/fonts/**"))
        		.requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .requestMatchers(new AntPathRequestMatcher( "/favicon.ico"))
                .requestMatchers(new AntPathRequestMatcher( "/css/**"))
                .requestMatchers(new AntPathRequestMatcher( "/js/**"))
                .requestMatchers(new AntPathRequestMatcher( "/img/**"))
                .requestMatchers(new AntPathRequestMatcher( "/lib/**"));
    }
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
        http.headers((headerConfig) -> headerConfig
        		.frameOptions(frameOptionsConfig -> frameOptionsConfig
        				.disable()
                ))
        .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
        		.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
        		.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
        		.requestMatchers("/admin/**").hasRole(UserRole.ADMIN.name())
                .requestMatchers("/manager/**").hasAnyRole(UserRole.ADMIN.name())
                .anyRequest().authenticated())
        .formLogin((formLogin) -> formLogin
        		.loginPage("/login/login.do").defaultSuccessUrl("/main"))
        .logout((logout) -> logout
        		.logoutRequestMatcher(new AntPathRequestMatcher("/logout.do"))
        		.logoutSuccessUrl("/").invalidateHttpSession(true));
		return http.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
