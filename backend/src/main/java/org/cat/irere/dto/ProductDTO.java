package org.cat.irere.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductDTO {
    private String code;
    private String name;
    private String productType;
    private BigDecimal price;
    private LocalDate inDate;
    private String image;
    private Integer availableQuantity;

    public ProductDTO() {
    }

    public ProductDTO(String code, String name, String productType, BigDecimal price, LocalDate inDate, String image,
            Integer availableQuantity) {
        this.code = code;
        this.name = name;
        this.productType = productType;
        this.price = price;
        this.inDate = inDate;
        this.image = image;
        this.availableQuantity = availableQuantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getInDate() {
        return inDate;
    }

    public void setInDate(LocalDate inDate) {
        this.inDate = inDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}