package poly.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.store.dao.AccountDAO;
import poly.store.dao.AuthorityDAO;
import poly.store.entity.Account;
import poly.store.entity.Authority;
import poly.store.service.AuthorityService;


@Service
public class AuthorityServiceImpl implements AuthorityService {
	@Autowired
	AuthorityDAO ardao;
	@Autowired
	AccountDAO adao;
	
	@Override
	public List<Authority> findAuthoritiesOfAdministrators() {
		List<Account> accounts = adao.getAdministrators();
		return ardao.authoritiesOf(accounts);
	}

	@Override
	public List<Authority> findAll() {
		return ardao.findAll();
	}

	@Override
	public Authority create(Authority auth) {
		return ardao.save(auth);
	}

	@Override
	public void delete(Integer id) {	
		ardao.deleteById(id);
	}
	
}
