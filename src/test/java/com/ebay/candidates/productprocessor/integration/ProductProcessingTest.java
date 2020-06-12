package com.ebay.candidates.productprocessor.integration;

import com.ebay.candidates.productprocessor.model.Attribute;
import com.ebay.candidates.productprocessor.model.Product;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductProcessingTest extends BaseTest {

    private static final String UPSERT_PRODUCT_URL = "/product-processor/product";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void normalization_capitalize() {
        Product product = new Product(100L, ImmutableList.<Attribute>builder()
                .add(new Attribute("title", Lists.newArrayList("ralph Lauren Men's Tshirt")))
                .add(new Attribute("check", Lists.newArrayList("ralph lauren Men's Tshirt")))
                .add(new Attribute("color", Lists.newArrayList("red", "blue")))
                .add(new Attribute("size", Lists.newArrayList("xl")))
                .build());
        ResponseEntity<Product> productResponseEntity = restTemplate.postForEntity(UPSERT_PRODUCT_URL, product, Product.class);
        assertThat(productResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Attribute> attributes = productResponseEntity.getBody().getAttributes();
        Map<String, List<String>> attributesMap = toAttributesMap(attributes);
        assertThat(attributesMap).containsEntry("title", Lists.newArrayList("ralph Lauren Men's Tshirt"));
        assertThat(attributesMap).containsEntry("color", Lists.newArrayList("Red", "Blue"));
        assertThat(attributesMap).containsEntry("size", Lists.newArrayList("Xl"));
        assertThat(attributesMap).containsEntry("check", Lists.newArrayList("Ralph lauren Men's Tshirt"));

    }

    @Test
    public void attributeValuesExclusion() {
        Product product = new Product(100L, ImmutableList.<Attribute>builder()
                .add(new Attribute("title", Lists.newArrayList("ralph Lauren Men's Tshirt")))
                .add(new Attribute("color", Lists.newArrayList("red", "N/A")))
                .add(new Attribute("size", Lists.newArrayList("Not Applied")))
                .build());
        ResponseEntity<Product> productResponseEntity = restTemplate.postForEntity(UPSERT_PRODUCT_URL, product, Product.class);
        assertThat(productResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Attribute> attributes = productResponseEntity.getBody().getAttributes();
        Map<String, List<String>> attributesMap = toAttributesMap(attributes);
        assertThat(attributesMap).containsEntry("title", Lists.newArrayList("ralph Lauren Men's Tshirt"));
        assertThat(attributesMap).containsEntry("color", Lists.newArrayList("Red"));
        assertThat(attributesMap).doesNotContainKey("size");
    }

    @Test
    public void sortByAttributeNames() {
        Product product = new Product(100L, ImmutableList.<Attribute>builder()
                .add(new Attribute("title", Lists.newArrayList("ralph Lauren Men's Tshirt")))
                .add(new Attribute("color", Lists.newArrayList("red")))
                .add(new Attribute("size", Lists.newArrayList("xl")))
                .add(new Attribute("brand", Lists.newArrayList("sony")))
                .build());
        ResponseEntity<Product> productResponseEntity = restTemplate.postForEntity(UPSERT_PRODUCT_URL, product, Product.class);
        assertThat(productResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Attribute> attributes = productResponseEntity.getBody().getAttributes();
        List<String> attributeNames = attributes.stream().map(Attribute::getName).collect(Collectors.toList());
        assertThat(attributeNames).containsExactly("brand", "color", "size", "title");
    }

    @Test
    public void processEmpty() {
        Product product = new Product(100L, Collections.EMPTY_LIST);
        ResponseEntity<Product> productResponseEntity = restTemplate.postForEntity(UPSERT_PRODUCT_URL, product, Product.class);
        assertThat(productResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    private Map<String, List<String>> toAttributesMap(List<Attribute> attributes) {
        return attributes.stream()
                .collect(Collectors.toMap(Attribute::getName, Attribute::getValues));
    }
}
