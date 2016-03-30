package bluney.sample.sample.security.user.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.u2ware.springfield.repository.EntityRepository;

import bluney.sample.sample.security.Authorities;
import bluney.sample.sample.security.Groups;
import bluney.sample.sample.security.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Service
public class LoginUserDetailsRepository implements UserDetailsService , AuthenticationFailureHandler, AuthenticationSuccessHandler{

	private static final Logger logger = LoggerFactory.getLogger(LoginUserDetailsRepository.class);

	@Autowired @Qualifier("usersRepository")
	private EntityRepository<Users, String> usersRepository;

	@Autowired @Qualifier("authoritiesRepository")
	private EntityRepository<Authorities, Integer> authoritiesRepository;

	@Autowired @Qualifier("groupsRepository")
	private EntityRepository<Groups, Integer> groupsRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws DataAccessException{
		logger.debug("loadUserByUsername : "+username);
		Users user = usersRepository.findOne(username);
		if (user == null) {
			throw new DataRetrievalFailureException("Query returned no results for user '" + username + "'");
		}

		//List<Authorities> userAuthorities = authoritiesRepository.findAll(new FindByPrimary_Username_Username(username));
		//List<Authorities> groupAuthorities = authoritiesRepository.findAll(new FindByPrimary_Username_Username(username));
		//List<GrantedAuthority> combinedAuthorities = new ArrayList<GrantedAuthority>();
		//combinedAuthorities.addAll(userAuthorities);
		//user.setAuthorities(combinedAuthorities);
		
		logger.debug("loadUserByUsername : "+user);
		return user;
	}
	
	private @AllArgsConstructor class FindByPrimary_Username_Username{
		@Getter @Setter private String primary;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		logger.debug("onAuthenticationSuccess : ");
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		logger.debug("onAuthenticationFailure : ");
	}

}
