package view.internal;

import model.DTO.OrderWithUser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Grupo 3
 */
public final class OrderInternalFrame extends JInternalFrame {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField txtId, txtDate, txtTotal;
    private final JComboBox<String> cbUser;
    private final List<Integer> userIds = new ArrayList<>();

    private final JButton btnNew, btnSave, btnModify, btnDelete, btnCancel, btnExit;
    private final JTextField txtSearchUser, txtDateFrom, txtDateTo;
    private final JButton btnSearchUser, btnFilterDates, btnLoadDetails;

    public OrderInternalFrame() {
        super("Gestión de Órdenes", true, true, true, true);
        setSize(820, 580);
        setLayout(null);

        // --- Encabezado: campos básicos ---
        int y = 20;
        add(new JLabel("ID:")).setBounds(20, y, 80, 25);
        txtId = new JTextField();
        txtId.setBounds(100, y, 80, 25);
        txtId.setEnabled(false);
        add(txtId);

        add(new JLabel("Cliente:")).setBounds(200, y, 80, 25);
        cbUser = new JComboBox<>();
        cbUser.setBounds(280, y, 180, 25);
        cbUser.setEnabled(false);
        add(cbUser);

        add(new JLabel("Fecha:")).setBounds(480, y, 80, 25);
        txtDate = new JTextField();
        txtDate.setBounds(540, y, 180, 25);
        txtDate.setEnabled(false);
        add(txtDate);

        add(new JLabel("Total:")).setBounds(20, y + 40, 80, 25);
        txtTotal = new JTextField();
        txtTotal.setBounds(100, y + 40, 100, 25);
        txtTotal.setEnabled(false);
        add(txtTotal);

        // --- Botón Salir en la esquina superior derecha ---
        btnExit = new JButton("Salir");
        btnExit.setBounds(720, 20, 80, 25);
        add(btnExit);

        // --- Fila de acciones CRUD ---
        int btnY = y + 40;
        btnNew = new JButton("Nuevo");
        btnNew.setBounds(220, btnY, 100, 25);
        add(btnNew);
        btnSave = new JButton("Guardar");
        btnSave.setBounds(330, btnY, 100, 25);
        add(btnSave);
        btnModify = new JButton("Modificar");
        btnModify.setBounds(440, btnY, 100, 25);
        add(btnModify);
        btnDelete = new JButton("Eliminar");
        btnDelete.setBounds(550, btnY, 100, 25);
        add(btnDelete);
        btnCancel = new JButton("Cancelar");
        btnCancel.setBounds(660, btnY, 100, 25);
        add(btnCancel);

        // --- Búsqueda y filtros ---
        int fy = y + 80;
        add(new JLabel("Buscar cliente:")).setBounds(20, fy, 100, 25);
        txtSearchUser = new JTextField();
        txtSearchUser.setBounds(120, fy, 180, 25);
        add(txtSearchUser);
        btnSearchUser = new JButton("Buscar");
        btnSearchUser.setBounds(310, fy, 80, 25);
        add(btnSearchUser);

        add(new JLabel("Desde (YYYY-MM-DD):")).setBounds(410, fy, 140, 25);
        txtDateFrom = new JTextField();
        txtDateFrom.setBounds(560, fy, 100, 25);
        add(txtDateFrom);

        add(new JLabel("Hasta:")).setBounds(20, fy + 40, 80, 25);
        txtDateTo = new JTextField();
        txtDateTo.setBounds(100, fy + 40, 100, 25);
        add(txtDateTo);
        btnFilterDates = new JButton("Filtrar fechas");
        btnFilterDates.setBounds(210, fy + 40, 140, 25);
        add(btnFilterDates);

        btnLoadDetails = new JButton("Ver detalles");
        btnLoadDetails.setBounds(380, fy + 40, 120, 25);
        add(btnLoadDetails);

        // --- Tabla de resultados ---
        tableModel = new DefaultTableModel(new Object[]{"ID", "Fecha", "Total", "Cliente"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, fy + 80, 780, 360);
        add(scroll);

        resetMode();
    }

    public void resetMode() {
        txtId.setText("");
        txtDate.setText("");
        txtTotal.setText("");

        txtSearchUser.setText("");
        txtDateFrom.setText("");
        txtDateTo.setText("");

        btnNew.setEnabled(true);
        btnSave.setEnabled(false);
        btnModify.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(false);

        txtTotal.setEnabled(false);
        cbUser.setEnabled(false);
    }

    public void enterCreateMode() {
        txtId.setText("");
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        txtTotal.setText("0.00");

        btnNew.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);

        txtTotal.setEnabled(true);
        cbUser.setEnabled(true);
    }

    public void enterEditMode() {
        btnNew.setEnabled(false);
        btnSave.setEnabled(false);
        btnModify.setEnabled(true);
        btnDelete.setEnabled(true);
        btnCancel.setEnabled(true);

        txtTotal.setEnabled(true);
        cbUser.setEnabled(true);
    }

    public void populateFields(OrderWithUser o) {
        txtId.setText(String.valueOf(o.getId()));
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(o.getOrderDate()));
        txtTotal.setText(o.getTotalAmount().toPlainString());

        String display = o.getCustomerName() + " (" + o.getCustomerEmail() + ")";
        for (int i = 0; i < userIds.size(); i++) {
            if (cbUser.getItemAt(i).equals(display)) {
                cbUser.setSelectedIndex(i);
                break;
            }
        }
    }

    public void setUsersForSelection(List<Map<String, Object>> users) {
        cbUser.removeAllItems();
        userIds.clear();

        cbUser.addItem("Seleccione cliente");
        userIds.add(-1);

        for (Map<String, Object> u : users) {
            userIds.add((Integer) u.get("id"));
            cbUser.addItem((String) u.get("display"));
        }
    }

    public void setTableData(List<Object[]> rows) {
        tableModel.setRowCount(0);
        for (Object[] r : rows) {
            tableModel.addRow(r);
        }
    }

    public int getSelectedOrderId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return -1;
        }
        Object v = tableModel.getValueAt(row, 0);
        if (v instanceof Number number) {
            return number.intValue();
        }
        try {
            return Integer.parseInt(v.toString());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public int getSelectedUserId() {
        int idx = cbUser.getSelectedIndex();
        return (idx > 0) ? userIds.get(idx) : -1;
    }

    public String getSearchUsername() {
        return txtSearchUser.getText().trim();
    }

    public String getDateFrom() {
        return txtDateFrom.getText().trim();
    }

    public String getDateTo() {
        return txtDateTo.getText().trim();
    }

    public BigDecimal getTotalAmount() {
        return new BigDecimal(txtTotal.getText().trim());
    }

    public JTable getTable() {
        return table;
    }

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

    public void addSearchUserListener(ActionListener l) {
        btnSearchUser.addActionListener(l);
    }

    public void addFilterDatesListener(ActionListener l) {
        btnFilterDates.addActionListener(l);
    }

    public void addLoadDetailsListener(ActionListener l) {
        btnLoadDetails.addActionListener(l);
    }
}
