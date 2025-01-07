package com.yobrunox.gestionmarko.dto.product;

import com.yobrunox.gestionmarko.models.Product;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoGetDTO {

    public ProductoGetDTO(Product product){
        idProduct = product.getIdProduct();
        barCode = product.getBarCode();
        description = product.getDescription();
        unit = product.getUnit().getName();
        initialStock = product.getInitialStock();

        priceBuy = product.getPriceBuy();
        priceSale = product.getPriceSale();
        minStock = product.getMinStock();
        maxStock = product.getMaxStock();

        currentStock = product.getCurrentStock();
        category =product.getCategory().getName();
        state = product.getState();

    }
    private UUID idProduct;
    private String barCode;
    private String description;
    private String unit;
    private Long initialStock;

    private Double priceBuy;
    private Double priceSale;


    private Long minStock;
    private Long maxStock;

    private Long currentStock;

    private String category;
    private Boolean state;

}
