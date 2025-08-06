package com.example.icecreamshop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class OrderItemDto {
    @JsonProperty("flavor")
    private String flavor;
    
    @JsonProperty("size")
    private String size;
    
    @JsonProperty("quantity")
    private int quantity;
    
    @JsonProperty("pricePerUnit")
    private BigDecimal pricePerUnit;
    
    // Constructors
    public OrderItemDto() {}
    
    public OrderItemDto(String flavor, String size, int quantity, BigDecimal pricePerUnit) {
        this.flavor = flavor;
        this.size = size;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }
    
    // Getters and Setters
    public String getFlavor() {
        return flavor;
    }
    
    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }
    
    public String getSize() {
        return size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }
    
    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
    
    @Override
    public String toString() {
        return "OrderItemDto{" +
                "flavor='" + flavor + '\'' +
                ", size='" + size + '\'' +
                ", quantity=" + quantity +
                ", pricePerUnit=" + pricePerUnit +
                '}';
    }
} 