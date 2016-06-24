package bluney.sample.sample.security.member.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.u2ware.springfield.repository.EntityRepository;
import com.u2ware.springfield.service.EntityServiceImpl;

import bluney.sample.sample.security.AuthenticationContext;
import bluney.sample.sample.security.Users;


@Service
public class MemberInfoService extends EntityServiceImpl<MemberInfo, MemberInfo>{

	@Autowired
	protected AuthenticationContext authenticationContext;
	
	@Autowired @Qualifier("usersRepository")
	private EntityRepository<Users, String> usersRepository;

	////////////////////////////
	//
	////////////////////////////
	@Override
	public MemberInfo updateForm(MemberInfo entity) {
		String username = authenticationContext.getUsername();
		Users user = usersRepository.findOne(username);
		entity.setUsername(username);
		entity.setPhone(user.getPhone());
		entity.setEmail(user.getEmail());		
		return entity;
	}	
	
	@Override
	@Transactional
	public MemberInfo update(MemberInfo entity) {

		String username = authenticationContext.getUsername();
		Users user = usersRepository.findOne(username);
		user.setPhone(entity.getPhone());
		user.setEmail(entity.getEmail());
		
		return entity;
	}	
}
