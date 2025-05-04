package view.internal;

import model.Role;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author Grupo 3
 */
public class RoleInternalFrame extends JInternalFrame {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField txtId, txtName, txtDescription;
    private final JButton btnNew, btnSave, btnModify, btnDelete, btnCancel, btnExit;

    public RoleInternalFrame() {
        super("Gestión de Roles", true, true, true, true);
        setSize(600, 400);
        setLayout(null);

        // ID
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 20, 80, 25);
        add(lblId);
        txtId = new JTextField();
        txtId.setBounds(100, 20, 50, 25);
        txtId.setEnabled(false);
        add(txtId);

        // Nombre
        JLabel lblName = new JLabel("Nombre:");
        lblName.setBounds(20, 60, 80, 25);
        add(lblName);
        txtName = new JTextField();
        txtName.setBounds(100, 60, 200, 25);
        txtName.setEnabled(false);
        add(txtName);

        // Descripción
        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setBounds(20, 100, 80, 25);
        add(lblDesc);
        txtDescription = new JTextField();
        txtDescription.setBounds(100, 100, 300, 25);
        txtDescription.setEnabled(false);
        add(txtDescription);

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
        btnExit.setBounds(460, 20, 100, 30);
        add(btnExit);

        // Tabla
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 190, 540, 160);
        add(scroll);

        resetMode();
    }

    // Estados
    public void resetMode() {
        txtId.setText("");
        txtName.setText("");
        txtDescription.setText("");
        btnNew.setEnabled(true);
        btnSave.setEnabled(false);
        btnModify.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(false);
        txtName.setEnabled(false);
        txtDescription.setEnabled(false);
    }

    public void enterCreateMode() {
        txtId.setText("");
        txtName.setText("");
        txtDescription.setText("");
        btnNew.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        txtName.setEnabled(true);
        txtDescription.setEnabled(true);
    }

    public void enterEditMode() {
        btnNew.setEnabled(false);
        btnSave.setEnabled(false);
        btnModify.setEnabled(true);
        btnDelete.setEnabled(true);
        btnCancel.setEnabled(true);
        txtName.setEnabled(true);
        txtDescription.setEnabled(true);
    }

    // Poblado
    public void populateFields(Role r) {
        txtId.setText(String.valueOf(r.getId()));
        txtName.setText(r.getName());
        txtDescription.setText(r.getDescription());
    }

    // Getters de inputs
    public String getNameInput() {
        return txtName.getText().trim();
    }

    public String getDescriptionInput() {
        return txtDescription.getText().trim();
    }

    // Selección tabla
    public int getSelectedRoleTableId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return -1;
        }
        Object v = tableModel.getValueAt(row, 0);
        if (v instanceof Number) {
            return ((Number) v).intValue();
        }
        try {
            return Integer.parseInt(v.toString().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    // Carga datos en tabla
    public void setTableData(List<Object[]> rows) {
        tableModel.setRowCount(0);
        for (Object[] r : rows) {
            tableModel.addRow(r);
        }
    }

    // Registro de listeners
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

    public JTable getTable() {
        return table;
    }
}
