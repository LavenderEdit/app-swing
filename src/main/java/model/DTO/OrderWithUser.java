package model.DTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Grupo 3
 */
public class OrderWithUser {

    private int id;
    private int userId;
    private Date orderDate;
    private BigDecimal totalAmount;
    private String customerName;
    private String customerEmail;

    public OrderWithUser() {
    }

    public OrderWithUser(int id, int userId, Date orderDate, BigDecimal totalAmount, String customerName, String customerEmail) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
