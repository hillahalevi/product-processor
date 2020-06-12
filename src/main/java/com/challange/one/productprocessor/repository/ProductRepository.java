package com.challange.one.productprocessor.repository;

import com.challange.one.productprocessor.entity.ProductDo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<ProductDo, Long> {

}
