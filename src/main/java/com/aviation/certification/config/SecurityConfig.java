package com.aviation.certification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.aviation.certification.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final UserDetailsServiceImpl userDetailsService;
	
	public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable()) // Для разработки
				.authorizeHttpRequests(authz -> authz
						// Разрешаем доступ к статическим ресурсам и главной странице без аутентификации
						.requestMatchers("/", "/home", "/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/instructor/**").hasRole("INSTRUCTOR")
						.requestMatchers("/candidate/**").hasRole("CANDIDATE")
						.anyRequest().authenticated()
				)
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/dashboard")
						.permitAll()
				)
				.logout(logout -> logout
						.logoutSuccessUrl("/home")
						.permitAll()
				);
		
		return http.build();
	}
	
}
