package com.example.icecreamshop.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String flavor;
    private String size;
    private int quantity;
    private BigDecimal pricePerUnit;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
    // Constructors
    public OrderItem() {}
    
    public OrderItem(String flavor, String size, int quantity, BigDecimal pricePerUnit, Order order) {
        this.flavor = flavor;
        this.size = size;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.order = order;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", flavor='" + flavor + '\'' +
                ", size='" + size + '\'' +
                ", quantity=" + quantity +
                ", pricePerUnit=" + pricePerUnit +
                '}';
    }
} 