package bluney.sample.sample.security.user.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.u2ware.springfield.repository.EntityRepository;
import com.u2ware.springfield.service.EntityServiceImpl;

import bluney.sample.sample.security.AuthenticationContext;
import bluney.sample.sample.security.Authorities;
import bluney.sample.sample.security.Users;
import bluney.sample.sample.security.Users.Role;


@Service("userRegisterService")
public class UserRegisterService extends EntityServiceImpl<UserRegister, UserRegister>{

	@Autowired
	protected AuthenticationContext authenticationContext;

	@Autowired @Qualifier("usersRepository")
	private EntityRepository<Users, String> usersRepository;
	
	@Autowired @Qualifier("authoritiesRepository")
	private EntityRepository<Authorities, Integer> authoritiesRepository;

	
	@Transactional
	public UserRegister create(UserRegister entity) {
		
		
		String username = entity.getUsername();
		String salt = authenticationContext.getPasswordSalt();
		String password = authenticationContext.getPassword(entity.getPassword1(), salt);
		Role role = entity.getRole();
		String phone = entity.getPhone();
		String email = entity.getEmail();
		
		Users user = new Users();
		user.setSalt(salt);
		user.setUsername(username);
		user.setPassword(password);
		user.setRole(role.toString());
		user.setPhone(phone);
		user.setEmail(email);
		usersRepository.save(user);
		
		
		for(GrantedAuthority authority : role.getAuthorities()){
			authoritiesRepository.save(new Authorities(username, authority.getAuthority()));
		}
		logger.warn(user.toString());
		logger.warn(user.toString());
		logger.warn(user.toString());
		logger.warn(user.toString());
		
		authenticationContext.logoff();
		
		return entity;
	}
}
