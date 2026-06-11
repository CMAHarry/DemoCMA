package com.cma.systemc.repository;

import com.cma.systemc.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductCodeIgnoreCase(String productCode);
    boolean existsByProductCodeIgnoreCase(String productCode);
    List<Product> findByActiveTrueOrderByProductCodeAsc();
}
