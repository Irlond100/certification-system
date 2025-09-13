package com.aviation.certification.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.aviation.certification.model.User;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String username;
	private String password;
	private String email;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserDetailsImpl(Long id, String username, String password, String email,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.authorities = authorities;
	}
	
	public static UserDetailsImpl build(User user) {
		// Теперь user.getRoles() возвращает Set<Role>, а Role реализует GrantedAuthority
		// Поэтому мы можем просто использовать: new ArrayList<>(user.getRoles())
		// Но обратите внимание: метод getAuthorities() в UserDetails возвращает Collection<? extends GrantedAuthority>
		// и Set<Role> подходит под эту сигнатуру.
		
		return new UserDetailsImpl(
				user.getId(),
				user.getUsername(),
				user.getPassword(),
				user.getEmail(),
				new ArrayList<>(user.getRoles())); // или можно оставить как Set, но лучше преобразовать в List, если в UserDetailsImpl ожидается List
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	public Long getId() { return id; }
	public String getEmail() { return email; }
	
	@Override
	public String getPassword() { return password; }
	@Override
	public String getUsername() { return username; }
	@Override
	public boolean isAccountNonExpired() { return true; }
	@Override
	public boolean isAccountNonLocked() { return true; }
	@Override
	public boolean isCredentialsNonExpired() { return true; }
	@Override
	public boolean isEnabled() { return true; }
}
