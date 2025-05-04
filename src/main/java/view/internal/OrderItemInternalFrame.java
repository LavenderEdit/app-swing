package view.internal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Grupo 3
 */
public final class OrderItemInternalFrame extends JInternalFrame {

    private final JTextField txtUser, txtDate, txtProduct, txtQuantity, txtGrandTotal;
    private final JButton btnSelectUser, btnSelectProduct, btnAddProduct, btnProcess;
    private final JTable tblItems;
    private final DefaultTableModel tblModel;

    public OrderItemInternalFrame() {
        super("Crear Boleta", true, true, true, true);
        setSize(820, 580);
        setLayout(null);

        int y = 20;
        // --- Selección de Usuario ---
        add(new JLabel("Cliente:")).setBounds(20, y, 80, 25);
        txtUser = new JTextField();
        txtUser.setBounds(100, y, 200, 25);
        txtUser.setEnabled(false);
        add(txtUser);
        btnSelectUser = new JButton("Seleccionar");
        btnSelectUser.setBounds(310, y, 120, 25);
        add(btnSelectUser);

        // --- Fecha automática ---
        add(new JLabel("Fecha:")).setBounds(460, y, 80, 25);
        txtDate = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtDate.setBounds(540, y, 120, 25);
        txtDate.setEnabled(false);
        add(txtDate);

        // --- Selección de Producto y Cantidad ---
        y += 50;
        add(new JLabel("Producto:")).setBounds(20, y, 80, 25);
        txtProduct = new JTextField();
        txtProduct.setBounds(100, y, 200, 25);
        txtProduct.setEnabled(false);
        add(txtProduct);
        btnSelectProduct = new JButton("Buscar");
        btnSelectProduct.setBounds(310, y, 80, 25);
        add(btnSelectProduct);

        add(new JLabel("Cantidad:")).setBounds(410, y, 80, 25);
        txtQuantity = new JTextField();
        txtQuantity.setBounds(490, y, 60, 25);
        add(txtQuantity);
        btnAddProduct = new JButton("Agregar");
        btnAddProduct.setBounds(560, y, 100, 25);
        add(btnAddProduct);

        // --- Tabla de Items ---
        y += 50;
        String[] cols = {"Producto", "Cantidad", "Precio Unit.", "Precio Total"};
        tblModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblItems = new JTable(tblModel);
        JScrollPane scroll = new JScrollPane(tblItems);
        scroll.setBounds(20, y, 780, 350);
        add(scroll);

        // --- Total General y Procesar ---
        y += 360;
        add(new JLabel("Total General:")).setBounds(20, y, 100, 25);
        txtGrandTotal = new JTextField("0.00");
        txtGrandTotal.setBounds(130, y, 100, 25);
        txtGrandTotal.setEnabled(false);
        add(txtGrandTotal);

        btnProcess = new JButton("Procesar Boleta");
        btnProcess.setBounds(600, y, 200, 30);
        add(btnProcess);

        // Estado inicial
        resetMode();
    }

    public void resetMode() {
        txtUser.setText("");
        txtProduct.setText("");
        txtQuantity.setText("");
        tblModel.setRowCount(0);
        txtGrandTotal.setText("0.00");
    }

    // Métodos para asociar listeners si los necesitas
    public void addSelectUserListener(java.awt.event.ActionListener l) {
        btnSelectUser.addActionListener(l);
    }

    public void addSelectProductListener(java.awt.event.ActionListener l) {
        btnSelectProduct.addActionListener(l);
    }

    public void addAddProductListener(java.awt.event.ActionListener l) {
        btnAddProduct.addActionListener(l);
    }

    public void addProcessListener(java.awt.event.ActionListener l) {
        btnProcess.addActionListener(l);
    }

    // Getters para campos y tabla
    public String getSelectedUser() {
        return txtUser.getText();
    }

    public void setSelectedUser(String u) {
        txtUser.setText(u);
    }

    public String getSelectedProduct() {
        return txtProduct.getText();
    }

    public void setSelectedProduct(String p) {
        txtProduct.setText(p);
    }

    public int getQuantity() {
        try {
            return Integer.parseInt(txtQuantity.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public DefaultTableModel getTableModel() {
        return tblModel;
    }

    public void updateGrandTotal(String total) {
        txtGrandTotal.setText(total);
    }
}
