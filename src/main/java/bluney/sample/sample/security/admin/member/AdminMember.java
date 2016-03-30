package bluney.sample.sample.security.admin.member;

import com.u2ware.springfield.config.Springfield;
import com.u2ware.springfield.config.Springfield.Strategy;
import com.u2ware.springfield.repository.QueryMethod;

import bluney.sample.sample.security.Users;
import lombok.Getter;
import lombok.Setter;

@Springfield(
	strategy=Strategy.JPA,
	entity=Users.class,
	methodLevelMapping={"*.html"}
)
@QueryMethod("findByUsername")
public class AdminMember {

	@Getter @Setter private String username;

}
