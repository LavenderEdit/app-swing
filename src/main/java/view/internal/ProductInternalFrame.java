package view.internal;

import model.Product;

import java.util.List;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

/**
 *
 * @author Grupo 3
 */
public class ProductInternalFrame extends JInternalFrame {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField txtId, txtName, txtPrice, txtStock;
    private final JButton btnNew, btnSave, btnModify, btnDelete, btnCancel, btnExit;

    public ProductInternalFrame() {
        super("Gestión de Productos", true, true, true, true);
        setSize(640, 420);
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

        // Precio
        JLabel lblPrice = new JLabel("Precio:");
        lblPrice.setBounds(20, 100, 80, 25);
        add(lblPrice);
        txtPrice = new JTextField();
        txtPrice.setBounds(100, 100, 100, 25);
        txtPrice.setEnabled(false);
        add(txtPrice);

        // Stock
        JLabel lblStock = new JLabel("Stock:");
        lblStock.setBounds(220, 100, 80, 25);
        add(lblStock);
        txtStock = new JTextField();
        txtStock.setBounds(300, 100, 80, 25);
        txtStock.setEnabled(false);
        add(txtStock);

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
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 190, 600, 180);
        add(scroll);

        resetMode();
    }

    // Estados de la UI
    public void resetMode() {
        txtId.setText("");
        txtName.setText("");
        txtPrice.setText("");
        txtStock.setText("");
        btnNew.setEnabled(true);
        btnSave.setEnabled(false);
        btnModify.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(false);
        txtName.setEnabled(false);
        txtPrice.setEnabled(false);
        txtStock.setEnabled(false);
    }

    public void enterCreateMode() {
        txtId.setText("");
        txtName.setText("");
        txtPrice.setText("");
        txtStock.setText("");
        btnNew.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        txtName.setEnabled(true);
        txtPrice.setEnabled(true);
        txtStock.setEnabled(true);
    }

    public void enterEditMode() {
        btnNew.setEnabled(false);
        btnSave.setEnabled(false);
        btnModify.setEnabled(true);
        btnDelete.setEnabled(true);
        btnCancel.setEnabled(true);
        txtName.setEnabled(true);
        txtPrice.setEnabled(true);
        txtStock.setEnabled(true);
    }

    // Poblado de campos
    public void populateFields(Product p) {
        txtId.setText(String.valueOf(p.getId()));
        txtName.setText(p.getName());
        txtPrice.setText(p.getPrice().toPlainString());
        txtStock.setText(String.valueOf(p.getStock()));
    }

    // Lectura de inputs
    public String getNameInput() {
        return txtName.getText().trim();
    }

    public String getPriceInput() {
        return txtPrice.getText().trim();
    }

    public String getStockInput() {
        return txtStock.getText().trim();
    }

    // Selección tabla → devuelve ID de primera columna
    public int getSelectedProductId() {
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

    // Volcado de datos en tabla
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
