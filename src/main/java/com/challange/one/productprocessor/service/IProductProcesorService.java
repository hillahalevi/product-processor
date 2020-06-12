package com.challange.one.productprocessor.service;

import com.challange.one.productprocessor.model.Product;

public interface IProductProcesorService {

    Product processProduct(Product product);

    Product getProduct(Long id);

    void saveProduct(Product product);


}
