package repository;

import model.OrderItem;
import util.ResultSetMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import config.DatabaseConfig;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Grupo 3
 */
public class OrderItemRepository implements CrudRepository<OrderItem, Integer> {

    @Override
    public OrderItem getById(Integer id) throws Exception {
        String sql = "{call sp_get_order_item(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<OrderItem> result = ResultSetMapper.mapAll(rs, OrderItem.class);
                return result.isEmpty() ? null : result.get(0);
            }
        }
    }

    @Override
    public List<OrderItem> getAll() throws Exception {
        String sql = "{call sp_get_all_order_item()}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {

            return ResultSetMapper.mapAll(rs, OrderItem.class);
        }
    }

    @Override
    public void create(OrderItem oi) throws Exception {
        String sql = "{call sp_create_order_item(?,?,?,?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, oi.getOrderId());
            stmt.setInt(2, oi.getProductId());
            stmt.setInt(3, oi.getQuantity());
            stmt.setBigDecimal(4, oi.getUnitPrice());
            stmt.execute();
        }
    }

    @Override
    public void update(OrderItem oi) throws Exception {
        String sql = "{call sp_update_order_item(?,?,?,?,?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, oi.getId());
            stmt.setInt(2, oi.getOrderId());
            stmt.setInt(3, oi.getProductId());
            stmt.setInt(4, oi.getQuantity());
            stmt.setBigDecimal(5, oi.getUnitPrice());
            stmt.execute();
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "{call sp_delete_order_item(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, id);
            stmt.execute();
        }
    }

    public void createFullOrder(int userId, List<OrderItem> items) throws Exception {
        String sql = "{CALL sp_create_full_order(?, ?)}";
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();

        for (OrderItem item : items) {
            ObjectNode obj = mapper.createObjectNode();
            obj.put("product_id", item.getProductId());
            obj.put("quantity", item.getQuantity());
            obj.put("unit_price",
                    item.getUnitPrice()
                            .setScale(2, RoundingMode.HALF_UP)
                            .toString()
            );
            jsonArray.add(obj);
        }

        String jsonString = mapper.writeValueAsString(jsonArray);

        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, jsonString);
            stmt.execute();
        }
    }
}
