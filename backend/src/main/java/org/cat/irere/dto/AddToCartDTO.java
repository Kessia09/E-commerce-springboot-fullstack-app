package org.cat.irere.dto;

public class AddToCartDTO {
    private String productCode;
    private Integer quantity;

    public AddToCartDTO() {
    }

    public AddToCartDTO(String productCode, Integer quantity) {
        this.productCode = productCode;
        this.quantity = quantity;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}