package bluney.sample.sample.security;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import com.u2ware.springfield.config.Springfield;
import com.u2ware.springfield.config.Springfield.Strategy;

import bluney.sample.sample.GroupPrimary;
import lombok.Getter;
import lombok.Setter;

@Springfield(strategy=Strategy.JPA_REPOSITORY_ONLY)
@Entity
@SuppressWarnings("serial")
public class GroupAuthorities implements GrantedAuthority{

	public GroupAuthorities(){
	}
	public GroupAuthorities(String authority){
		this.primary = new GroupPrimary(authority);
	}

	@EmbeddedId 
	@Getter @Setter private GroupPrimary primary;

	@Transient
	public String getAuthority() {
		return primary != null ? primary.getAuthority() : null;
	}


}
