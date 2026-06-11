package com.cma.systemc.service;

import com.cma.systemc.dto.ProductRequest;
import com.cma.systemc.entity.Product;
import com.cma.systemc.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private static final Logger logger = LogManager.getLogger(ProductService.class);

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public List<Product> findActive() {
        return repository.findByActiveTrueOrderByProductCodeAsc();
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));
    }

    public Product findByCode(String productCode) {
        return repository.findByProductCodeIgnoreCase(productCode)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productCode));
    }

    @Transactional
    public Product create(Product product) {
        String code = normalize(product.getProductCode());
        if (repository.existsByProductCodeIgnoreCase(code)) {
            throw new IllegalArgumentException("Product code already exists: " + code);
        }
        product.setProductCode(code);
        logger.info("Creating product [{}]", code);
        return repository.save(product);
    }

    @Transactional
    public Product create(ProductRequest request) {
        Product product = new Product();
        product.setProductCode(request.getProductCode());
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setActive(request.getActive());
        return create(product);
    }

    @Transactional
    public Product update(Long id, Product product) {
        Product existing = findById(id);
        String newCode = normalize(product.getProductCode());
        repository.findByProductCodeIgnoreCase(newCode)
                .filter(found -> !found.getId().equals(id))
                .ifPresent(found -> { throw new IllegalArgumentException("Product code already exists: " + newCode); });

        existing.setProductCode(newCode);
        existing.setProductName(product.getProductName());
        existing.setDescription(product.getDescription());
        existing.setActive(product.getActive() != null ? product.getActive() : true);
        logger.info("Updating product [{}]", newCode);
        return repository.save(existing);
    }

    @Transactional
    public Product update(Long id, ProductRequest request) {
        Product product = new Product();
        product.setProductCode(request.getProductCode());
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setActive(request.getActive());
        return update(id, product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = findById(id);
        logger.info("Deleting product [{}]", product.getProductCode());
        repository.delete(product);
    }

    private String normalize(String code) {
        return code == null ? null : code.trim().toUpperCase();
    }
}
