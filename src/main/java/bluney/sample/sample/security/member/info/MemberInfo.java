package bluney.sample.sample.security.member.info;

import javax.validation.constraints.NotNull;

import com.u2ware.springfield.config.Springfield;
import com.u2ware.springfield.config.Springfield.Strategy;

import lombok.Getter;
import lombok.Setter;

@Springfield(
	strategy=Strategy.DTO,
	methodLevelMapping={"updateForm.html","update.html"},
	identity="username"
)
public class MemberInfo {

	@Getter @Setter private @NotNull String username;	
	@Getter @Setter private @NotNull String description;	
	
	@Getter @Setter private @NotNull String part;
	@Getter @Setter private @NotNull String phone;
	@Getter @Setter private @NotNull String task;
	@Getter @Setter private @NotNull String email;
	
}
