package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Grupo 3
 */
public class Order {

    private int id;
    private int user_id;
    private Date order_date;
    private BigDecimal total_amount;
    private String customer_name;

    public Order() {
    }

    public Order(int id, int user_id, Date order_date, BigDecimal total_amount) {
        this.id = id;
        this.user_id = user_id;
        this.order_date = order_date;
        this.total_amount = total_amount;
    }

    public Order(int id, int user_id, Date order_date, BigDecimal total_amount, String customer_name) {
        this.id = id;
        this.user_id = user_id;
        this.order_date = order_date;
        this.total_amount = total_amount;
        this.customer_name = customer_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int order_id) {
        this.id = order_id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public Date getOrderDate() {
        return order_date;
    }

    public void setOrderDate(Date order_date) {
        this.order_date = order_date;
    }

    public BigDecimal getTotalAmount() {
        return total_amount;
    }

    public void setTotalAmount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public String getCustomerName() {
        return customer_name;
    }

    public void setCustomerName(String customer_name) {
        this.customer_name = customer_name;
    }
}
