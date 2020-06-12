package com.ebay.candidates.productprocessor;

import com.ebay.candidates.productprocessor.entity.ProductDo;
import com.ebay.candidates.productprocessor.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "jsr330", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IProductMapper {

    @Mappings({
            @Mapping(source = "product.id", target = "id"),
            @Mapping(source = "product.attributes", target = "attributes")

    })
    ProductDo toProductDo(Product product);



    @Mappings({
            @Mapping(source = "productDo.id", target = "id"),
            @Mapping(source = "productDo.attributes", target = "attributes")

    })
    Product toProduct(ProductDo productDo);

}