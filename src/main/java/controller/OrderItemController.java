package controller;

import java.math.BigDecimal;
import model.OrderItem;
import model.User;
import model.Product;
import repository.OrderItemRepository;
import repository.UserRepository;
import repository.ProductRepository;
import view.internal.OrderItemInternalFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Grupo 3
 */
public class OrderItemController {

    private final OrderItemInternalFrame view;
    private final OrderItemRepository repo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    private Integer selectedUserId = null;
    private Integer selectedProductId = null;
    private BigDecimal selectedProductPrice = BigDecimal.ZERO;
    private final List<OrderItem> itemsBuffer = new ArrayList<>();

    public OrderItemController(OrderItemInternalFrame view) {
        this.view = view;
        this.repo = new OrderItemRepository();
        this.userRepo = new UserRepository();
        this.productRepo = new ProductRepository();

        registerListeners();
        view.resetMode();
    }

    private void registerListeners() {
        view.addSelectUserListener(e -> onSelectUser());
        view.addSelectProductListener(e -> onSelectProduct());
        view.addAddProductListener(e -> onAddProduct());
        view.addProcessListener(e -> onProcessOrder());
    }

    private void onSelectUser() {
        try {
            List<User> users = userRepo.getAll();
            String[] options = users.stream()
                    .map(u -> u.getUsername() + " (" + u.getEmail() + ")")
                    .toArray(String[]::new);

            String sel = (String) JOptionPane.showInputDialog(
                    view,
                    "Seleccione cliente:",
                    "Clientes",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (sel == null) {
                return;
            }

            int idx = java.util.Arrays.asList(options).indexOf(sel);
            User u = users.get(idx);
            selectedUserId = u.getId();
            view.setSelectedUser(sel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error cargando usuarios:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSelectProduct() {
        try {
            List<Product> products = productRepo.getAll();
            String[] options = products.stream()
                    .map(p -> p.getName() + " - $" + p.getPrice())
                    .toArray(String[]::new);

            String sel = (String) JOptionPane.showInputDialog(
                    view,
                    "Seleccione producto:",
                    "Productos",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (sel == null) {
                return;
            }

            int idx = java.util.Arrays.asList(options).indexOf(sel);
            Product p = products.get(idx);
            selectedProductId = p.getId();
            selectedProductPrice = p.getPrice();
            view.setSelectedProduct(sel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error cargando productos:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAddProduct() {
        if (selectedProductId == null) {
            JOptionPane.showMessageDialog(view,
                    "Debe seleccionar un producto primero.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int qty = view.getQuantity();
        if (qty <= 0) {
            JOptionPane.showMessageDialog(view,
                    "La cantidad debe ser mayor que 0.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrderItem oi = new OrderItem();
        oi.setProductId(selectedProductId);
        oi.setQuantity(qty);
        oi.setUnitPrice(selectedProductPrice);
        itemsBuffer.add(oi);

        DefaultTableModel m = view.getTableModel();
        BigDecimal total = selectedProductPrice.multiply(new BigDecimal(qty));
        m.addRow(new Object[]{
            view.getSelectedProduct(),
            qty,
            selectedProductPrice,
            total
        });

        BigDecimal grand = itemsBuffer.stream()
                .map(i -> i.getUnitPrice().multiply(new BigDecimal(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        view.updateGrandTotal(grand.toPlainString());

        view.setSelectedProduct("");
    }

    private void onProcessOrder() {
        if (selectedUserId == null) {
            JOptionPane.showMessageDialog(view,
                    "Debe seleccionar un cliente.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (itemsBuffer.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "La boleta debe contener al menos un producto.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                repo.createFullOrder(selectedUserId, itemsBuffer);
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(view,
                        "Boleta procesada correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                itemsBuffer.clear();
                selectedUserId = null;
                selectedProductId = null;
                selectedProductPrice = BigDecimal.ZERO;
                view.resetMode();
            }
        }.execute();
    }
}
