package repository;

import config.DatabaseConfig;
import model.Order;
import model.DTO.OrderItemDetail;
import model.DTO.OrderWithUser;
import util.ResultSetMapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Grupo 3
 */
public class OrderRepository implements CrudRepository<Order, Integer> {

    @Override
    public Order getById(Integer id) throws Exception {
        String sql = "{call sp_get_order(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Order> list = ResultSetMapper.mapAll(rs, Order.class);
                return list.isEmpty() ? null : list.get(0);
            }
        }
    }

    @Override
    public List<Order> getAll() throws Exception {
        String sql = "{call sp_get_all_order()}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {

            return ResultSetMapper.mapAll(rs, Order.class);
        }
    }

    @Override
    public void create(Order o) throws Exception {
        String sql = "{call sp_create_order(?,?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, o.getUserId());
            stmt.setBigDecimal(2, o.getTotalAmount());
            stmt.execute();
        }
    }

    @Override
    public void update(Order o) throws Exception {
        String sql = "{call sp_update_order(?,?,?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, o.getId());
            stmt.setInt(2, o.getUserId());
            stmt.setBigDecimal(3, o.getTotalAmount());
            stmt.execute();
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "{call sp_delete_order(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, id);
            stmt.execute();
        }
    }

    public List<OrderWithUser> getOrdersWithUsers() throws Exception {
        String sql = "{call sp_get_orders_with_users()}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {

            return ResultSetMapper.mapAll(rs, OrderWithUser.class);
        }
    }

    public List<OrderWithUser> searchOrdersByUsername(String username) throws Exception {
        String sql = "{call sp_search_orders_by_username(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return ResultSetMapper.mapAll(rs, OrderWithUser.class);
            }
        }
    }

    public List<OrderWithUser> getOrdersByDateRange(Date startDate, Date endDate) throws Exception {
        String sql = "{call sp_get_orders_by_date_range(?, ?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));
            try (ResultSet rs = stmt.executeQuery()) {
                return ResultSetMapper.mapAll(rs, OrderWithUser.class);
            }
        }
    }

    public Map<String, Object> getOrderDetails(int orderId) throws Exception {
        String sql = "{call sp_get_order_details(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            Map<String, Object> result = new HashMap<>();
            stmt.setInt(1, orderId);

            boolean hasResults = stmt.execute();
            // First result set: order header
            if (hasResults) {
                try (ResultSet rs = stmt.getResultSet()) {
                    List<Order> orders = ResultSetMapper.mapAll(rs, Order.class);
                    if (!orders.isEmpty()) {
                        result.put("order", orders.get(0));
                    }
                }
            }
            // Second result set: order items
            if (stmt.getMoreResults()) {
                try (ResultSet rs = stmt.getResultSet()) {
                    List<OrderItemDetail> items = ResultSetMapper.mapAll(rs, OrderItemDetail.class);
                    result.put("items", items);
                }
            }
            return result;
        }
    }

    public void updateOrderTotal(int orderId) throws Exception {
        String sql = "{call sp_update_order_total(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, orderId);
            stmt.execute();
        }
    }

    public Map<String, Object> checkProductAvailability(int productId, int quantity) throws Exception {
        String sql = "{call sp_check_product_availability(?, ?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {

            stmt.setInt(1, productId);
            stmt.setInt(2, quantity);
            if (rs.next()) {
                Map<String, Object> result = new HashMap<>();
                result.put("available", rs.getBoolean("is_available"));
                result.put("currentStock", rs.getInt("current_stock"));
                return result;
            }
            return Map.of("available", false, "currentStock", 0);
        }
    }

    public List<Map<String, Object>> getUsersForSelection() throws Exception {
        String sql = "{call sp_get_users_for_selection()}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {

            List<Map<String, Object>> result = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", rs.getObject("user_id") != null ? rs.getInt("user_id") : -1);
                user.put("display", rs.getObject("user_display") != null
                        ? rs.getString("user_display") : "Usuario sin nombre");
                result.add(user);
            }
            return result;
        }
    }
}
