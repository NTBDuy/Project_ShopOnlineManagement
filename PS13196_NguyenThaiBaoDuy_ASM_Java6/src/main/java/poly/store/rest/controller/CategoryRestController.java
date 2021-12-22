package poly.store.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.store.entity.Account;
import poly.store.entity.Category;
import poly.store.service.CategoryService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/categories")
public class CategoryRestController {
	@Autowired
	CategoryService categoryService;
	
	@GetMapping()
	public List<Category> getAll() {
		return categoryService.findAll();
	}
	
	@GetMapping("/search/{keyword}")
	public List<Category> search(@PathVariable("keyword") String keyword) {
		return categoryService.findAllByNameLike("%"+keyword+"%");
	}
	
	
	@GetMapping("{id}")
	public Category getOne(@PathVariable("id") String id) {
		return categoryService.findById(id);
	}
	
	@PostMapping()
	public Category create(@RequestBody Category c) {
		return categoryService.create(c);
	}
	
	@PutMapping("{id}")
	public Category update(@PathVariable("id") String id,
			@RequestBody Category c) {
		return categoryService.update(c);
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") String id) {
		categoryService.delete(id);
	}
}
