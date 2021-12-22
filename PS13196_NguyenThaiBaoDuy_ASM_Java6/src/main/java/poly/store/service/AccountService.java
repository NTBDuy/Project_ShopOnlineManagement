package poly.store.service;

import java.util.List;

import poly.store.entity.Account;

public interface AccountService {
	
	Account findById(String username);
	
	List<Account> getAdministrators();
	
	List<Account> findAll();
	
	Account create(Account a);

	Account update(Account a);

	void delete(String username);

	List<Account> findAllByUsernameLike(String keyword);
}
