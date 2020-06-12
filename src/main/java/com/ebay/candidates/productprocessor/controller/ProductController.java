package com.ebay.candidates.productprocessor.controller;

import com.ebay.candidates.productprocessor.model.Product;
import com.ebay.candidates.productprocessor.service.IProductProcesorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("/product-processor")
public class ProductController {

    @Inject
    private IProductProcesorService productProcesorService;

    @RequestMapping(path = "/product", method = RequestMethod.POST)
    public ResponseEntity<Product> upsert(@RequestBody Product product) {
        productProcesorService.processProduct(product);
        productProcesorService.saveProduct(product);
        return ResponseEntity.ok(product);
    }


    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productProcesorService.getProduct(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }


}
