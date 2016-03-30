package bluney.sample.sample.security.member.leave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.u2ware.springfield.repository.EntityRepository;
import com.u2ware.springfield.service.EntityServiceImpl;

import bluney.sample.sample.security.AuthenticationContext;
import bluney.sample.sample.security.Users;


@Service
public class MemberLeaveService extends EntityServiceImpl<MemberLeave, MemberLeave>{

	@Autowired
	protected AuthenticationContext authenticationContext;

	@Autowired @Qualifier("usersRepository")
	private EntityRepository<Users, String> usersRepository;

	@Override
	public MemberLeave updateForm(MemberLeave entity) {
		String username = authenticationContext.getUsername();
		entity.setUsername(username);
		return entity;
	}	
	
	@Override
	@Transactional
	public MemberLeave update(MemberLeave entity) {

		String username = authenticationContext.getUsername();
		Users user = usersRepository.findOne(username);
		user.setEnabled(false);
		
		authenticationContext.logoff();
		
		return entity;
	}
}
