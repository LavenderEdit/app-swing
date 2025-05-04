package repository;

import config.DatabaseConfig;
import model.Product;
import util.ResultSetMapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Grupo 3
 */
public class ProductRepository implements CrudRepository<Product, Integer> {

    @Override
    public Product getById(Integer id) throws Exception {
        String sql = "{call sp_get_product(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Product> result = ResultSetMapper.mapAll(rs, Product.class);
                return result.isEmpty() ? null : result.get(0);
            }
        }
    }

    @Override
    public List<Product> getAll() throws Exception {
        String sql = "{call sp_get_all_product()}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {

            return ResultSetMapper.mapAll(rs, Product.class);
        }
    }

    @Override
    public void create(Product p) throws Exception {
        String sql = "{call sp_create_product(?,?,?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, p.getName());
            stmt.setBigDecimal(2, p.getPrice());
            stmt.setInt(3, p.getStock());
            stmt.execute();
        }
    }

    @Override
    public void update(Product p) throws Exception {
        String sql = "{call sp_update_product(?,?,?,?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, p.getId());
            stmt.setString(2, p.getName());
            stmt.setBigDecimal(3, p.getPrice());
            stmt.setInt(4, p.getStock());
            stmt.execute();
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "{call sp_delete_product(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, id);
            stmt.execute();
        }
    }
}
