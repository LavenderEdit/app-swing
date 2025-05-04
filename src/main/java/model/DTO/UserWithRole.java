package model.DTO;

/**
 *
 * @author Grupo 3
 */
public class UserWithRole {

    private int id;
    private String username;
    private String email;
    private String password;
    private int id_role;
    private String roleName;

    public UserWithRole() {
    }

    public UserWithRole(int id, String username, String email, String roleName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roleName = roleName;
    }

    public UserWithRole(int id, String username, String email, String password, int id_role, String roleName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.id_role = id_role;
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId_role() {
        return id_role;
    }

    public void setId_role(int id_role) {
        this.id_role = id_role;
    }

    public int getIdRole() {
        return id_role;
    }

    public void setIdRole(int id_role) {
        this.id_role = id_role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
