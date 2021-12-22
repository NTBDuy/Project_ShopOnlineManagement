package poly.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.store.dao.ProductDAO;
import poly.store.entity.Product;
import poly.store.entity.Report;
import poly.store.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	@Autowired
	ProductDAO proDao;

	@Override
	public List<Product> findAll() {
		return proDao.findAll();
	}

	@Override
	public Product findById(Integer id) {
		return proDao.findById(id).get();
	}

	@Override
	public List<Product> findByCategoryId(String cid) {
		return proDao.findByCategoryId(cid);
	}

	@Override
	public Product create(Product p) {
		return proDao.save(p);
	}

	@Override
	public Product update(Product p) {
		return proDao.save(p);
	}

	@Override
	public void delete(Integer id) {
		proDao.deleteById(id);
	}

	@Override
	public List<Product> findAllByNameLike(String pro) {
		return proDao.findAllByNameLike(pro);
	}

	@Override
	public List<Report> getInventoryByCategory() {
		return proDao.getInventoryByCategory();
	}
}
