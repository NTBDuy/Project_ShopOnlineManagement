package poly.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import poly.store.dao.AccountDAO;
import poly.store.dao.AuthorityDAO;
import poly.store.entity.Account;
import poly.store.entity.Authority;
import poly.store.entity.Role;
import poly.store.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService{
	@Autowired
	AccountDAO adao;
	@Autowired AuthorityDAO audao;
	@Autowired
	BCryptPasswordEncoder pe;
	
	@Override
	public Account findById(String username) {
		return adao.findById(username).get();
	}

	@Override
	public List<Account> getAdministrators() {
		return adao.getAdministrators();
	}

	@Override
	public List<Account> findAll() {
		
		return adao.findAll();
	}
	
	@Override
	public Account create(Account a) {
		String ps = pe.encode(a.getPassword());
		System.out.println("Mật khẩu đã mã hoá: "+ ps);
		a.setPassword(ps);
		adao.save(a);
		Authority au = new Authority();
		au.setAccount(a);
		Role ro = new Role();
		ro.setId("CUST");
		au.setRole(ro);
		adao.save(a);
		audao.save(au);
		return adao.save(a);
	}

	@Override
	public Account update(Account a) {
		String ps = pe.encode(a.getPassword());
		System.out.println("Mật khẩu đã mã hoá: "+ ps);
		a.setPassword(ps);
		return adao.save(a);
	}

	@Override
	public void delete(String username) {
		adao.deleteById(username);
	}

	@Override
	public List<Account> findAllByUsernameLike(String keyword) {
		return adao.findAllByUsernameLike(keyword);
	}
	
}
