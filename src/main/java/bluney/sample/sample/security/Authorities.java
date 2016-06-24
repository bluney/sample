package bluney.sample.sample.security;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import com.u2ware.springfield.config.Springfield;
import com.u2ware.springfield.config.Springfield.Strategy;

import lombok.Getter;
import lombok.Setter;

@Springfield(strategy=Strategy.JPA_REPOSITORY_ONLY)
@Entity
public class Authorities implements GrantedAuthority{

	private static final long serialVersionUID = -5573779386357203026L;

	public Authorities(){
	}
	public Authorities(String username, String authority){
		this.primary = new Primary(new Users(username), authority);
	}
	
	
	@EmbeddedId 
	@Getter @Setter private Primary primary;

	@Transient
	public String getAuthority() {
		return primary != null ? primary.getAuthority() : null;
	}

//	@Override
//	public String toString() {
//		return primary.toString();
//	}

}
