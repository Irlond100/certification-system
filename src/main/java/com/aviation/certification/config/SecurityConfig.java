package com.aviation.certification.config;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {
	private final UserDetailsService userDetailsService;
	
	public SecurityConfig(UserDetailsService userDetailsService) {
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
	public AuthenticationSuccessHandler successHandler() {
		return (request, response, authentication) -> {
			if (authentication.getAuthorities().stream()
					.anyMatch(grantedAuthority ->
							grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
				response.sendRedirect("/admin/dashboard"); // Изменено обратно на /admin/dashboard
			} else if (authentication.getAuthorities().stream()
					.anyMatch(grantedAuthority ->
							grantedAuthority.getAuthority().equals("ROLE_CANDIDATE"))) {
				response.sendRedirect("/candidate/dashboard");
			} else {
				response.sendRedirect("/");
			}
		};
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authz -> authz
						// Сначала специфичные правила
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/candidate/**").hasRole("CANDIDATE")
						.requestMatchers("/instructor/**").hasRole("INSTRUCTOR")
						// Затем общие правила
						.requestMatchers("/", "/home", "/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()
						// В конце общее правило
						.anyRequest().authenticated()
				)
				.formLogin(form -> form
						.loginPage("/login")
						.successHandler(successHandler())
						.permitAll()
				)
				.logout(logout -> logout
						.logoutSuccessUrl("/login?logout")
						.permitAll()
				);
		return http.build();
	}
}