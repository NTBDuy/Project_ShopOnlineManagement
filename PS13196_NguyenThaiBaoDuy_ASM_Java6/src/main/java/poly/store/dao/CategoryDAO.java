package poly.store.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.store.entity.Category;



public interface CategoryDAO extends JpaRepository<Category, String> {

	List<Category> findAllByNameLike(String string);

}
