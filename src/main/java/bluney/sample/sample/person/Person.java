package bluney.sample.sample.person;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.u2ware.springfield.config.Springfield;

import lombok.Getter;
import lombok.Setter;

@Springfield
@Entity
public class Person {
	@Id
	private @Getter @Setter String name;
	private @Getter @Setter Integer age;
	
}
