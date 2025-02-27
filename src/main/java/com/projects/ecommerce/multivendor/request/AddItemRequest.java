package com.projects.ecommerce.multivendor.request;


public class AddItemRequest {
    private String size;
    private int quantity;
    private Long productId;

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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
