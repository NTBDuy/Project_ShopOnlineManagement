package poly.store.service;

import java.util.List;

import poly.store.entity.Product;
import poly.store.entity.Report;

public interface ProductService {

	List<Product> findAll();

	List<Report> getInventoryByCategory();

	Product findById(Integer id);

	List<Product> findByCategoryId(String cid);
	
	Product create(Product p);

	Product update(Product p);

	void delete(Integer id);

	List<Product> findAllByNameLike(String pro);
}
