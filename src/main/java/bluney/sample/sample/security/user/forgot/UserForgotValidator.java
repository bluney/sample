package bluney.sample.sample.security.user.forgot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import com.u2ware.springfield.repository.EntityRepository;
import com.u2ware.springfield.validation.EntityValidatorImpl;

import bluney.sample.sample.security.Users;


@Service
public class UserForgotValidator extends EntityValidatorImpl<UserForgot, UserForgot>{

	@Autowired @Qualifier("usersRepository")
	private EntityRepository<Users, String> usersRepository;
	
	
	@Transactional
	public void create(UserForgot target, Errors errors) {
		super.create(target, errors);
		
		if(errors.hasErrors()) return;
		
		if(! usersRepository.exists(target.getUsername())){
			errors.rejectValue("username", "errorCode" , "사용중인 아이디가 아닙니다.");
		}
	}
	
}
