package bluney.sample.sample.security.member.leave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import com.u2ware.springfield.repository.EntityRepository;
import com.u2ware.springfield.validation.EntityValidatorImpl;

import bluney.sample.sample.security.Users;


@Component
public class MemberLeaveValidator extends EntityValidatorImpl<MemberLeave, MemberLeave>{

	@Autowired @Qualifier("usersRepository")
	private EntityRepository<Users, String> usersRepository;

	@Override
	@Transactional
	public void create(MemberLeave target, Errors errors) {
		super.create(target, errors);
		if(errors.hasErrors()) return;
		
		if(! usersRepository.exists(target.getUsername())){
			errors.rejectValue("username", "errorCode" , "등록된 사용자가 아닙니다.");
		}
	}	
}
