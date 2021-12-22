package poly.store.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import poly.store.entity.Product;
import poly.store.entity.Report;

public interface ProductDAO extends JpaRepository<Product, Integer>{
	
	@Query("SELECT p FROM Product p WHERE p.category.id=?1")
	List<Product> findByCategoryId(String cid);

	List<Product> findAllByNameLike(String pro);
	
	@Query("SELECT new Report(o.category, sum(o.price), count(o)) " + " FROM Product o " + " GROUP BY o.category"
            + " ORDER BY sum(o.price) DESC")
    List<Report> getInventoryByCategory();
}
