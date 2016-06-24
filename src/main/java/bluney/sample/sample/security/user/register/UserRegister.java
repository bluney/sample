package bluney.sample.sample.security.user.register;

import javax.validation.constraints.NotNull;

import com.u2ware.springfield.config.Springfield;
import com.u2ware.springfield.config.Springfield.Strategy;

import bluney.sample.sample.security.Users.Role;
import lombok.Getter;
import lombok.Setter;

@Springfield(
	strategy=Strategy.DTO,
	methodLevelMapping={"createForm.html","create.html"},
	identity="username"
)
public class UserRegister {

	@Getter @Setter private @NotNull String username;
	@Getter @Setter private @NotNull String password1;
	@Getter @Setter private @NotNull String password2;
	@Getter @Setter private @NotNull Role role;
	@Getter @Setter private @NotNull String phone;
	@Getter @Setter private @NotNull String email;
	
	
}
