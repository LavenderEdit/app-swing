package view.internal;

import java.awt.event.ActionListener;
import model.DTO.UserWithRole;
import model.Role;
import repository.RoleRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Grupo 3
 */
public final class UserInternalFrame extends JInternalFrame {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField txtId, txtUsername, txtEmail;
    private final JPasswordField txtPassword;
    private final JComboBox<String> cbRole;
    private final JButton btnNew, btnSave, btnModify, btnDelete, btnCancel, btnExit, btnChangePwd;
    private List<Role> rolesCache = new ArrayList<>();

    public UserInternalFrame() {
        super("Gestión de Usuarios", true, true, true, true);
        setSize(700, 450);
        setLayout(null);

        // ID
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 20, 80, 25);
        add(lblId);
        txtId = new JTextField();
        txtId.setBounds(100, 20, 50, 25);
        txtId.setEnabled(false);
        add(txtId);

        // Contraseña
        JLabel lblPwd = new JLabel("Contraseña:");
        lblPwd.setBounds(280, 20, 80, 25);
        add(lblPwd);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(360, 20, 150, 25);
        txtPassword.setEnabled(false);
        add(txtPassword);

        // Boton Cambiar contraseña inicialmente invisible
        btnChangePwd = new JButton("Cambiar contraseña");
        btnChangePwd.setBounds(360, 20, 150, 25);
        btnChangePwd.setVisible(false);
        add(btnChangePwd);

        // Usuario
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setBounds(20, 60, 80, 25);
        add(lblUser);
        txtUsername = new JTextField();
        txtUsername.setBounds(100, 60, 150, 25);
        txtUsername.setEnabled(false);
        add(txtUsername);

        // Rol
        JLabel lblRole = new JLabel("Rol:");
        lblRole.setBounds(280, 60, 80, 25);
        add(lblRole);
        cbRole = new JComboBox<>();
        cbRole.setBounds(360, 60, 150, 25);
        cbRole.setEnabled(false);
        add(cbRole);

        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(20, 100, 80, 25);
        add(lblEmail);
        txtEmail = new JTextField();
        txtEmail.setBounds(100, 100, 150, 25);
        txtEmail.setEnabled(false);
        add(txtEmail);

        // Botones
        btnNew = new JButton("Nuevo");
        btnNew.setBounds(20, 140, 100, 30);
        add(btnNew);
        btnSave = new JButton("Guardar");
        btnSave.setBounds(130, 140, 100, 30);
        add(btnSave);
        btnModify = new JButton("Modificar");
        btnModify.setBounds(240, 140, 100, 30);
        add(btnModify);
        btnDelete = new JButton("Eliminar");
        btnDelete.setBounds(350, 140, 100, 30);
        add(btnDelete);
        btnCancel = new JButton("Cancelar");
        btnCancel.setBounds(460, 140, 100, 30);
        add(btnCancel);
        btnExit = new JButton("Salir");
        btnExit.setBounds(570, 140, 100, 30);
        add(btnExit);

        // Tabla
        tableModel = new DefaultTableModel(new Object[]{"ID", "Usuario", "Email", "Rol"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 190, 650, 230);
        add(scroll);

        loadRoles();
        resetMode();
    }

    private void loadRoles() {
        try {
            rolesCache = new RoleRepository().getAll();
            cbRole.removeAllItems();
            for (model.Role r : rolesCache) {
                cbRole.addItem(r.getName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando roles: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void populateFields(UserWithRole u) {
        txtId.setText(String.valueOf(u.getId()));
        txtUsername.setText(u.getUsername());
        txtEmail.setText(u.getEmail());
        txtPassword.setText("");
        for (int i = 0; i < rolesCache.size(); i++) {
            if (rolesCache.get(i).getName().equals(u.getRoleName())) {
                cbRole.setSelectedIndex(i);
                break;
            }
        }
    }

    public void clearInputs() {
        txtId.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtEmail.setText("");
        cbRole.setSelectedIndex(-1);
    }

    public void resetMode() {
        btnNew.setEnabled(true);
        btnSave.setEnabled(false);
        btnModify.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(false);
        btnExit.setEnabled(true);
        txtUsername.setEnabled(false);
        txtPassword.setEnabled(false);
        txtEmail.setEnabled(false);
        cbRole.setEnabled(false);
        txtPassword.setVisible(true);
        btnChangePwd.setVisible(false);
        clearInputs();
    }

    public void enterCreateMode() {
        clearInputs();
        btnNew.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        txtUsername.setEnabled(true);
        txtPassword.setEnabled(true);
        txtEmail.setEnabled(true);
        cbRole.setEnabled(true);
        txtPassword.setVisible(true);
        btnChangePwd.setVisible(false);
    }

    public void enterEditMode() {
        btnNew.setEnabled(false);
        btnSave.setEnabled(false);
        btnModify.setEnabled(true);
        btnDelete.setEnabled(true);
        btnCancel.setEnabled(true);
        txtUsername.setEnabled(true);
        txtPassword.setEnabled(true);
        txtEmail.setEnabled(true);
        cbRole.setEnabled(true);
        txtPassword.setVisible(false);
        btnChangePwd.setVisible(true);
    }

    // Listeners
    public void addNewListener(ActionListener l) {
        btnNew.addActionListener(l);
    }

    public void addSaveListener(ActionListener l) {
        btnSave.addActionListener(l);
    }

    public void addModifyListener(ActionListener l) {
        btnModify.addActionListener(l);
    }

    public void addDeleteListener(ActionListener l) {
        btnDelete.addActionListener(l);
    }

    public void addCancelListener(ActionListener l) {
        btnCancel.addActionListener(l);
    }

    public void addExitListener(ActionListener l) {
        btnExit.addActionListener(l);
    }

    public void addChangePwdListener(ActionListener l) {
        btnChangePwd.addActionListener(l);
    }

    // Getters de datos
    public String getUsernameInput() {
        return txtUsername.getText().trim();
    }

    public String getPasswordInput() {
        return new String(txtPassword.getPassword()).trim();
    }

    public String getEmailInput() {
        return txtEmail.getText().trim();
    }

    public int getSelectedRoleId() {
        int i = cbRole.getSelectedIndex();
        return i >= 0 ? rolesCache.get(i).getId() : -1;
    }

    public int getRoleId() {
        String nomRol = cbRole.getSelectedItem().toString();
        System.out.println(nomRol);

        Role r = new Role();
        try {
            RoleRepository rr = new RoleRepository();
            r = rr.getByName(nomRol);
        } catch (Exception ex) {
            Logger.getLogger(UserInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        return r.getId();
    }

    public JTable getTable() {
        return table;
    }

    public int getSelectedUserId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return -1;
        }
        Object value = tableModel.getValueAt(row, 0);
        if (value instanceof Number) {
            int id = ((Number) value).intValue();
            return id;
        }
        try {
            int id = Integer.parseInt(value.toString().trim());
            return id;
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    public void setTableData(List<Object[]> rows) {
        tableModel.setRowCount(0);
        for (Object[] r : rows) {
            tableModel.addRow(r);
        }
    }
}
