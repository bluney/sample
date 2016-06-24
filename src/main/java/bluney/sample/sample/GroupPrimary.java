package bluney.sample.sample;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Embeddable
public class GroupPrimary implements Serializable {
    public GroupPrimary(){
    }
    public GroupPrimary(String authority){
        this.authority =authority;
    }
    @Getter @Setter private @NotNull String groupId;
    @Getter @Setter private @NotNull String authority;
}