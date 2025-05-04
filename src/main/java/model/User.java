package model;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Grupo 3
 */
public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private int role_id;

    public User() {
    }

    public User(int id, String username, String password, String email, int role_id) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role_id = role_id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String plainOrHash) {
        if (plainOrHash != null && plainOrHash.startsWith("$2")) {
            this.password = plainOrHash;
        } else {
            this.password = BCrypt.hashpw(plainOrHash, BCrypt.gensalt(12));
        }
    }

    public boolean verifyPassword(String plainPassword) {
        return BCrypt.checkpw(plainPassword, this.password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRoleId() {
        return role_id;
    }

    public void setRoleId(int role_id) {
        this.role_id = role_id;
    }
}
