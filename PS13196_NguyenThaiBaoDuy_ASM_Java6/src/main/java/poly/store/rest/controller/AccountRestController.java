package poly.store.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import poly.store.entity.Account;
import poly.store.service.AccountService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/accounts")
public class AccountRestController {
	
	@Autowired
	AccountService accountService;
	
	@GetMapping
	public List<Account> getAccount(@RequestParam("admin")Optional<Boolean> admin){
		if(admin.orElse(false)) {
			return accountService.getAdministrators();
		}
		
		return accountService.findAll();
	}
	
	@GetMapping("/getall")
	public List<Account> getAll() {
		return accountService.findAll();
	}
	

	@GetMapping("/search/{keyword}")
	public List<Account> search(@PathVariable("keyword") String keyword) {
		return accountService.findAllByUsernameLike("%"+keyword+"%");
	}
	
	@GetMapping("{id}")
	public Account getOne(@PathVariable("id") String username) {
		return accountService.findById(username);
	}
	
	@PostMapping()
	public Account create(@RequestBody Account a) {
		return accountService.create(a);
	}
	
	@PutMapping("{id}")
	public Account update(Model model,@PathVariable("id") String username,
			@RequestBody Account a) {
		return accountService.update(a);
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") String username) {
		accountService.delete(username);
	}
}
