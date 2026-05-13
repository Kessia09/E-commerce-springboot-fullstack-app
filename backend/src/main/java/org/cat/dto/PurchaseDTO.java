package org.cat.irere.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PurchaseDTO {
    private Long id;
    private String productCode;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal total;
    private LocalDate date;

    // Constructors
    public PurchaseDTO() {
    }

    public PurchaseDTO(Long id, String productCode, String productName, Integer quantity, BigDecimal price,
            BigDecimal total, LocalDate date) {
        this.id = id;
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.date = date;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}