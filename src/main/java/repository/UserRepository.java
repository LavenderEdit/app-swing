package repository;

import config.DatabaseConfig;
import model.User;
import util.ResultSetMapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DTO.UserWithRole;

/**
 *
 * @author Grupo 3
 */
public class UserRepository implements CrudRepository<User, Integer> {

    @Override
    public User getById(Integer id) throws Exception {
        String sql = "{call sp_get_user(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<User> result = ResultSetMapper.mapAll(rs, User.class);
                return result.isEmpty() ? null : result.get(0);
            }
        }
    }

    @Override
    public List<User> getAll() throws Exception {
        String sql = "{call sp_get_all_user()}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {

            return ResultSetMapper.mapAll(rs, User.class);
        }
    }

    @Override
    public void create(User u) throws Exception {
        String sql = "{call sp_create_user(?,?,?,?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getEmail());
            stmt.setInt(4, u.getRoleId());
            stmt.execute();
        }
    }

    @Override
    public void update(User u) throws Exception {
        String sql = "{call sp_update_user(?,?,?,?,?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, u.getId());
            stmt.setString(2, u.getUsername());
            stmt.setString(3, u.getPassword());
            stmt.setString(4, u.getEmail());
            stmt.setInt(5, u.getRoleId());
            stmt.execute();
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "{call sp_delete_user(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, id);
            stmt.execute();
        }
    }

    public List<User> getByRole(int roleId) throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "{call sp_get_users_by_role(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("username"));
                    u.setEmail(rs.getString("email"));
                    u.setRoleId(roleId);
                    list.add(u);
                }
            }
        }
        return list;
    }

    public List<UserWithRole> getAllWithRole() throws Exception {
        String sql = "{call sp_get_users_with_role()}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {

            return ResultSetMapper.mapAll(rs, UserWithRole.class);
        }
    }

    public UserWithRole getUserWithRole(int idUser) throws Exception {
        String sql = "{call sp_get_user_with_rol(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idUser);
            try (ResultSet rs = stmt.executeQuery()) {
                List<UserWithRole> result = ResultSetMapper.mapAll(rs, UserWithRole.class);
                return result.isEmpty() ? null : result.get(0);
            }
        }
    }

    public User getByEmail(String email) throws Exception {
        String sql = "{call sp_get_user_by_email(?)}";
        try (Connection conn = DatabaseConfig.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                List<User> result = ResultSetMapper.mapAll(rs, User.class);
                return result.isEmpty() ? null : result.get(0);
            }
        }
    }
}
