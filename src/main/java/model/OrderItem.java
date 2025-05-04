package model;

import java.math.BigDecimal;

/**
 *
 * @author Grupo 3
 */
public class OrderItem {

    private int id;
    private int order_id;
    private int product_id;
    private int quantity;
    private BigDecimal unit_price;

    public OrderItem() {
    }

    public OrderItem(int id, int order_id, int product_id, int quantity, BigDecimal unit_price) {
        this.id = id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return order_id;
    }

    public void setOrderId(int order_id) {
        this.order_id = order_id;
    }

    public int getProductId() {
        return product_id;
    }

    public void setProductId(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unit_price;
    }

    public void setUnitPrice(BigDecimal unit_price) {
        this.unit_price = unit_price;
    }
}
