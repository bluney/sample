package bluney.sample.sample.security.member.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.u2ware.springfield.repository.EntityRepository;
import com.u2ware.springfield.service.EntityServiceImpl;

import bluney.sample.sample.security.AuthenticationContext;
import bluney.sample.sample.security.Users;


@Service
public class MemberPasswordService extends EntityServiceImpl<MemberPassword, MemberPassword>{

	@Autowired
	protected AuthenticationContext authenticationContext;

	@Autowired @Qualifier("usersRepository")
	private EntityRepository<Users, String> usersRepository;

	@Override
	public MemberPassword updateForm(MemberPassword entity) {
		String username = authenticationContext.getUsername();
		entity.setUsername(username);
		return entity;
	}	

	@Override
	@Transactional
	public MemberPassword update(MemberPassword entity) {
		
		String username = authenticationContext.getUsername();
		Users user = usersRepository.findOne(username);
		
		String password = authenticationContext.getPassword(entity.getNewPassword1(), user.getSalt());
		user.setPassword(password);
		
		authenticationContext.logoff();
		return entity;
	}
}
