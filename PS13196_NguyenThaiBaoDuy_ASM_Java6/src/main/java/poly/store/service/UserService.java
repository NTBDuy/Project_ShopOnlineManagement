package poly.store.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import poly.store.dao.AccountDAO;
import poly.store.dao.AuthorityDAO;
import poly.store.entity.Account;
import poly.store.entity.Authority;
import poly.store.entity.Role;

@Service
public class UserService implements UserDetailsService{
	@Autowired
	AccountDAO accountDao;
	@Autowired
	AuthorityDAO auDao;
	@Autowired
	BCryptPasswordEncoder pe;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Account account = accountDao.findById(username).get();
			String password = account.getPassword();
			String []roles = account.getAuthorities().stream()
					.map(au -> au.getRole().getId())
					.collect(Collectors.toList()).toArray(new String[0]);
			return User.withUsername(username)
					.password(password)
					.roles(roles).build();
		} catch (Exception e) {
			throw new UsernameNotFoundException(username +"not found");
		}
	}
	
	public void loginFromOAuth2(OAuth2AuthenticationToken oauth2) {
		String email = oauth2.getPrincipal().getAttribute("email");
		String pass = pe.encode("123@abcxyzacas");
		System.out.println(email);
		Boolean checkTT = accountDao.existsById(email);
		if(checkTT==false) {
			Account ac = new Account();
			ac.setEmail(email);
			ac.setUsername(email);
			ac.setFullname(oauth2.getPrincipal().getAttribute("name"));
			ac.setPassword(pass);
			ac.setPhoto("user.png");
			accountDao.save(ac);
			Authority au = new Authority();
			au.setAccount(ac);
			Role ro = new Role();
			ro.setId("CUST");
			au.setRole(ro);
			auDao.save(au);
			System.out.println("Vừa tạo tk thành công!");
			UserDetails user = User.withUsername(email)
					.password(pass).roles("GUEST").build();
			Authentication auth = new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
		} else {
			Account account = accountDao.findById(email).get();
			String[]roles = account.getAuthorities().stream()
					.map(au -> au.getRole().getId())
					.collect(Collectors.toList()).toArray(new String[0]);
			UserDetails user = User.withUsername(email)
					.password(pass).roles(roles).build();
			Authentication auth = new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
	}
}
