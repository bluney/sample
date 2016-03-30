package bluney.sample.sample.security.user.forgot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.u2ware.springfield.repository.EntityRepository;
import com.u2ware.springfield.service.EntityServiceImpl;

import bluney.sample.sample.security.AuthenticationContext;
import bluney.sample.sample.security.Users;


@Service
public class UserForgotService extends EntityServiceImpl<UserForgot, UserForgot>{

	@Autowired
	protected AuthenticationContext authenticationContext;

	@Autowired @Qualifier("usersRepository")
	private EntityRepository<Users, String> usersRepository;
	
	@Transactional
	public UserForgot create(UserForgot entity) {
		
		String username = entity.getUsername();
		Users user = usersRepository.findOne(username);
		// send mail......

		entity.setUsername(user.getUsername());
		authenticationContext.logoff();
		return entity;
	}	
}
