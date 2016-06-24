package bluney.sample.sample.security;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.u2ware.springfield.config.Springfield;
import com.u2ware.springfield.config.Springfield.Strategy;

import lombok.Getter;
import lombok.Setter;



@Springfield(strategy=Strategy.HIBERNATE_REPOSITORY_ONLY)
@Entity
public class Users implements UserDetails{

	private static final long serialVersionUID = 5482145308642802692L;

	public Users(){
	}
	public Users(String username){
		this.username = username;
	}
	
	@Id 
	@Getter @Setter private @NotNull String username;
	@Getter @Setter private @NotNull String password;
	@Getter @Setter private @NotNull boolean enabled = true;
	@Getter @Setter private boolean accountNonExpired = true;
	@Getter @Setter private boolean accountNonLocked = true;
	@Getter @Setter private boolean credentialsNonExpired = true;
	@Getter @Setter private @NotNull String salt;
//	@Getter @Setter private @NotNull String description;
	@Getter @Setter private @NotNull String role;	
//	@Getter @Setter private @NotNull String part;
	@Getter @Setter private @NotNull String phone;
//	@Getter @Setter private @NotNull String task;
	@Getter @Setter private @NotNull String email;
	

	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Role.valueOf(role).getAuthorities();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Users other = (Users) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Users [username=" + username 
				+ ", password=" + password
				+ ", enabled=" + enabled 
				+ ", role=" + role 
				+ ", salt=" + salt 
				+ ", phone=" + phone
				+ ", email=" + email + "]";
	}
	
	public static enum Role {

		ANONYMOUS(new SimpleGrantedAuthority("ROLE_ANONYMOUS")),
		USER(new SimpleGrantedAuthority("ROLE_ANONYMOUS"), new SimpleGrantedAuthority("ROLE_USER")),
		ADMIN(new SimpleGrantedAuthority("ROLE_ANONYMOUS"), new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		private Collection<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		
		Role(GrantedAuthority... authorities){
			for(GrantedAuthority authority : authorities){
				grantedAuthorities.add(authority);
			}
		}
		public Collection<? extends GrantedAuthority> getAuthorities(){
			return grantedAuthorities;
		}
	}
}
