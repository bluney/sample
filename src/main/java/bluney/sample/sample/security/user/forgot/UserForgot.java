package bluney.sample.sample.security.user.forgot;

import javax.validation.constraints.NotNull;

import com.u2ware.springfield.config.Springfield;
import com.u2ware.springfield.config.Springfield.Strategy;

import lombok.Getter;
import lombok.Setter;

@Springfield(
	strategy=Strategy.DTO,
	methodLevelMapping={"createForm.html","create.html"},
	identity="username"
)
public class UserForgot {

	@Getter @Setter private @NotNull String username;
	
}
