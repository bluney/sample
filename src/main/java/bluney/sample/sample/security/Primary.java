package bluney.sample.sample.security;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Embeddable 
public class Primary implements Serializable {

    private static final long serialVersionUID = -6949463592639319376L;

    public Primary(){
    }
    public Primary(Users username, String authority){
        this.username = username;
        this.authority = authority;
    }
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="username")
    private @Getter @Setter Users username;
    private @Getter @Setter String authority;
    @Override
    public String toString() {
        return authority;
    }
    
}