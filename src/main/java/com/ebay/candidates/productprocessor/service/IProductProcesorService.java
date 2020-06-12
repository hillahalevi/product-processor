package com.ebay.candidates.productprocessor.service;

import com.ebay.candidates.productprocessor.model.Product;

public interface IProductProcesorService {

    Product processProduct(Product product);

    Product getProduct(Long id);

    void saveProduct(Product product);


}
