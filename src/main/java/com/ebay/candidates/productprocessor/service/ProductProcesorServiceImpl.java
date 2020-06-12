package com.ebay.candidates.productprocessor.service;

import com.ebay.candidates.productprocessor.IProductMapper;
import com.ebay.candidates.productprocessor.entity.ProductDo;
import com.ebay.candidates.productprocessor.model.Attribute;
import com.ebay.candidates.productprocessor.model.Product;
import com.ebay.candidates.productprocessor.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductProcesorServiceImpl implements IProductProcesorService {

    private static final String TITLE = "title";

    @Inject
    private ProductRepository productRepository;


    @Inject
    private IProductMapper productMapper;


    @Value("${attribute-values.exclusion-list}")
    private String ilegalVaues;


    @Override
    public Product processProduct(Product product) {
        List<Attribute> updatedAttributes = new ArrayList<>();
        for (Attribute attribute : product.getAttributes()) {
            normalizeAttribute(attribute);
            filterAttribute(attribute);
            if (!attribute.getValues().isEmpty()) {
                updatedAttributes.add(attribute);
            }
        }
        updatedAttributes.sort(Comparator.comparing(Attribute::getName));
        product.setAttributes(updatedAttributes);
        return product;
    }

    @Override
    public Product getProduct(Long id) {
        Optional<ProductDo> productDetails = productRepository.findById(id);
        return productDetails.map(productDo -> productMapper.toProduct(productDetails.get())).orElse(null);

    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(productMapper.toProductDo(product));
    }


    private void normalizeAttribute(Attribute attribute) {
        if (!attribute.getName().equals(TITLE)) {
            List<String> normalizedValues = attribute.getValues().stream().map(this::changeToCanonicalForm).collect(Collectors.toList());
            attribute.setValues(normalizedValues);
        }
    }


    private void filterAttribute(Attribute attribute) {
        List<String> filteredValues = filterValues(attribute.getValues());
        attribute.setValues(filteredValues);
    }


    private String changeToCanonicalForm(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }


    private List<String> filterValues(List<String> values) {
        return values.stream().filter(s -> !ilegalVaues.contains(s)).collect(Collectors.toList());
    }


}
