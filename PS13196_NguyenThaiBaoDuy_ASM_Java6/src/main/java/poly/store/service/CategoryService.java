package poly.store.service;

import java.util.List;

import poly.store.entity.Category;

public interface CategoryService {

	List<Category> findAll();
	
	Category findById(String id);
	
	Category create(Category c);

	Category update(Category c);

	void delete(String id);

	List<Category> findAllByNameLike(String string);
}
