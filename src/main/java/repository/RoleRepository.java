package repository;

import config.DatabaseConfig;
import model.Role;
import util.ResultSetMapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Grupo 3
 */
public class RoleRepository implements CrudRepository<Role, Integer> {

    @Override
    public Role getById(Integer id) throws Exception {
        String sql = "{call sp_get_role(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Role> result = ResultSetMapper.mapAll(rs, Role.class);
                return result.isEmpty() ? null : result.get(0);
            }
        }
    }

    @Override
    public List<Role> getAll() throws Exception {
        String sql = "{call sp_get_all_role()}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {

            return ResultSetMapper.mapAll(rs, Role.class);
        }
    }

    @Override
    public void create(Role r) throws Exception {
        String sql = "{call sp_create_role(?,?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, r.getName());
            stmt.setString(2, r.getDescription());
            stmt.execute();
        }
    }

    @Override
    public void update(Role r) throws Exception {
        String sql = "{call sp_update_role(?,?,?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, r.getId());
            stmt.setString(2, r.getName());
            stmt.setString(3, r.getDescription());
            stmt.execute();
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "{call sp_delete_role(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, id);
            stmt.execute();
        }
    }

    public Role getByName(String name) throws Exception {
        String sql = "{call sp_get_role_by_name(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    List<Role> result = ResultSetMapper.mapAll(rs, Role.class);
                    return result.isEmpty() ? null : result.get(0);
                }
                return null;
            }
        }
    }
}
