package controller;

import model.Order;
import model.DTO.OrderWithUser;
import model.DTO.OrderItemDetail;
import repository.OrderRepository;
import view.internal.OrderInternalFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Grupo 3
 */
public class OrderController {

    private final OrderInternalFrame view;
    private final OrderRepository repo;
    private List<OrderWithUser> cache = new ArrayList<>();

    public OrderController(OrderInternalFrame view) {
        this.view = view;
        this.repo = new OrderRepository();
        registerListeners();
        loadUsers();
        loadTable();
    }

    private void registerListeners() {
        view.addNewListener(e -> view.enterCreateMode());
        view.addSaveListener(e -> onCreate());
        view.addModifyListener(e -> onEdit());
        view.addDeleteListener(e -> onDelete());
        view.addCancelListener(e -> view.resetMode());
        view.addExitListener(e -> view.dispose());

        view.getTable().getSelectionModel().addListSelectionListener(
                (ListSelectionListener) evt -> {
                    if (!evt.getValueIsAdjusting() && view.getSelectedOrderId() >= 0) {
                        onRowSelect();
                    }
                });

        view.addSearchUserListener(e -> onSearchByUsername());
        view.addFilterDatesListener(e -> onFilterByDateRange());
        view.addLoadDetailsListener(e -> onLoadDetails());
    }

    private void loadUsers() {
        try {
            List<Map<String, Object>> users = repo.getUsersForSelection();
            view.setUsersForSelection(users);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error cargando clientes: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTable() {
        new SwingWorker<List<OrderWithUser>, Void>() {
            @Override
            protected List<OrderWithUser> doInBackground() throws Exception {
                return repo.getOrdersWithUsers();
            }

            @Override
            protected void done() {
                try {
                    cache = get();
                    List<Object[]> rows = new ArrayList<>();
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (OrderWithUser o : cache) {
                        rows.add(new Object[]{
                            o.getId(),
                            fmt.format(o.getOrderDate()),
                            o.getTotalAmount(),
                            o.getCustomerName()
                        });
                    }
                    view.setTableData(rows);
                    view.resetMode();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view,
                            "Error cargando órdenes: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void onRowSelect() {
        int row = view.getTable().getSelectedRow();
        OrderWithUser o = cache.get(row);
        view.populateFields(o);
        view.enterEditMode();
    }

    private void onCreate() {
        int userId = view.getSelectedUserId();
        if (userId < 0) {
            JOptionPane.showMessageDialog(view,
                    "Selecciona un cliente.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Order o = new Order();
                o.setUserId(userId);
                o.setTotalAmount(view.getTotalAmount());
                repo.create(o);
                repo.updateOrderTotal(o.getId());
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(view,
                        "Orden creada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            }
        }.execute();
    }

    private void onEdit() {
        int id = view.getSelectedOrderId();
        if (id < 0) {
            return;
        }
        int userId = view.getSelectedUserId();
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Order o = repo.getById(id);
                o.setUserId(userId);
                o.setTotalAmount(view.getTotalAmount());
                repo.update(o);
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(view,
                        "Orden actualizada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            }
        }.execute();
    }

    private void onDelete() {
        int id = view.getSelectedOrderId();
        if (id < 0) {
            return;
        }
        if (JOptionPane.showConfirmDialog(view,
                "Eliminar orden?", "Confirmar", JOptionPane.YES_NO_OPTION)
                != JOptionPane.YES_OPTION) {
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                repo.delete(id);
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(view,
                        "Orden eliminada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            }
        }.execute();
    }

    private void onSearchByUsername() {
        String uname = view.getSearchUsername();
        new SwingWorker<List<OrderWithUser>, Void>() {
            @Override
            protected List<OrderWithUser> doInBackground() throws Exception {
                return repo.searchOrdersByUsername(uname);
            }

            @Override
            protected void done() {
                try {
                    cache = get();
                    renderCache();
                } catch (Exception ex) {
                }
            }
        }.execute();
    }

    private void onFilterByDateRange() {
        try {
            Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(view.getDateFrom());
            Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(view.getDateTo());
            new SwingWorker<List<OrderWithUser>, Void>() {
                @Override
                protected List<OrderWithUser> doInBackground() throws Exception {
                    return repo.getOrdersByDateRange(d1, d2);
                }

                @Override
                protected void done() {
                    try {
                        cache = get();
                        renderCache();
                    } catch (Exception ex) {
                    }
                }
            }.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Formato de fecha inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void renderCache() {
        List<Object[]> rows = new ArrayList<>();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (OrderWithUser o : cache) {
            rows.add(new Object[]{
                o.getId(), fmt.format(o.getOrderDate()),
                o.getTotalAmount(), o.getCustomerName()
            });
        }
        view.setTableData(rows);
    }

    private void onLoadDetails() {
        int id = view.getSelectedOrderId();
        if (id < 0) {
            return;
        }
        new SwingWorker<Map<String, Object>, Void>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return repo.getOrderDetails(id);
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> data = get();
                    Order header = (Order) data.get("order");
                    @SuppressWarnings("unchecked")
                    List<OrderItemDetail> items = (List<OrderItemDetail>) data.get("items");
                    // Construir tabla de detalles
                    DefaultTableModel m
                            = new DefaultTableModel(new Object[]{"Producto", "Cant.", "Precio", "Subt."}, 0);
                    for (OrderItemDetail it : items) {
                        m.addRow(new Object[]{it.getProductName(),
                            it.getQuantity(),
                            it.getUnitPrice(),
                            it.getSubtotal()});
                    }
                    JTable t = new JTable(m);
                    JOptionPane.showMessageDialog(view,
                            new JScrollPane(t),
                            "Detalles Orden #" + header.getId(),
                            JOptionPane.PLAIN_MESSAGE);
                } catch (Exception ex) {
                }
            }
        }.execute();
    }
}
