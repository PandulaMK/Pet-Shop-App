package com.example.dognutrition;

import java.util.List;

public class Order {
    private String orderId;
    private String userId;
    private List<Cart_Item> orderItems;
    private double totalAmount;
    private String status;
    private String paymentMethod;

    // Default constructor
    public Order() {}

    // Parameterized constructor
    public Order(String orderId, String userId, List<Cart_Item> orderItems, double totalAmount, String status, String paymentMethod) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderItems = orderItems;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Cart_Item> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<Cart_Item> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
