package es.ucm.fdi.tusnoficias;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import es.ucm.fdi.tusnoficias.model.User;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
	private static final long serialVersionUID = 5693546765169908335L;
	private static Logger log = Logger.getLogger(UserDetails.class);
	private User user;
	ArrayList<SimpleGrantedAuthority> roles;
	
	public UserDetails (User user) {
		this.user = user;
		
		 // build UserDetails object
        roles = new ArrayList<>();
        for (String r : user.getRoles().split("[,]")) {
        	roles.add(new SimpleGrantedAuthority("ROLE_" + r));
	        log.info("Roles for " + user.getLogin() + " include " + roles.get(roles.size()-1));
        }
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		this.user = IwUserDetailsService.getInstance().attachUser(this.user);
		return this.user;
	}
	
	public boolean isAdmin() {
		return this.roles.toString().contains("ROLE_admin");
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.user.getEnabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.user.getEnabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.user.getEnabled();
	}

	@Override
	public boolean isEnabled() {
		return this.user.getEnabled();
	}

}
