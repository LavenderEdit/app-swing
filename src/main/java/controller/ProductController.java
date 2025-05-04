package controller;

import model.Product;
import repository.ProductRepository;
import view.internal.ProductInternalFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Grupo 3
 */
public class ProductController {

    private final ProductInternalFrame view;
    private final ProductRepository repo;
    private List<Product> cache = new ArrayList<>();

    public ProductController(ProductInternalFrame view) {
        this.view = view;
        this.repo = new ProductRepository();
        registerListeners();
        loadTable();
    }

    private void registerListeners() {
        view.addNewListener(e -> view.enterCreateMode());
        view.addSaveListener(e -> onCreate());
        view.addModifyListener(e -> onEdit());
        view.addDeleteListener(e -> onDelete());
        view.addCancelListener(e -> view.resetMode());
        view.addExitListener(e -> view.dispose());

        view.getTable()
                .getSelectionModel()
                .addListSelectionListener((ListSelectionListener) evt -> {
                    if (!evt.getValueIsAdjusting() && view.getSelectedProductId() >= 0) {
                        onTableSelection();
                    }
                });
    }

    private void loadTable() {
        new SwingWorker<List<Product>, Void>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                return repo.getAll();
            }

            @Override
            protected void done() {
                try {
                    cache = get();
                    List<Object[]> rows = new ArrayList<>();
                    for (Product p : cache) {
                        rows.add(new Object[]{
                            p.getId(),
                            p.getName(),
                            p.getPrice(),
                            p.getStock()
                        });
                    }
                    view.setTableData(rows);
                    view.resetMode();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view,
                            "Error cargando productos: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void onTableSelection() {
        int id = view.getSelectedProductId();
        try {
            Product p = repo.getById(id);
            if (p != null) {
                view.populateFields(p);
                view.enterEditMode();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error cargando producto seleccionado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCreate() {
        String name = view.getNameInput();
        String price = view.getPriceInput();
        String stock = view.getStockInput();
        if (name.isEmpty() || price.isEmpty() || stock.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Todos los campos son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        BigDecimal bd;
        int st;
        try {
            bd = new BigDecimal(price);
            st = Integer.parseInt(stock);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Precio o stock no válidos.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Product p = new Product();
                p.setName(name);
                p.setPrice(bd);
                p.setStock(st);
                repo.create(p);
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(view,
                        "Producto creado correctamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            }
        }.execute();
    }

    private void onEdit() {
        int id = view.getSelectedProductId();
        if (id < 0) {
            return;
        }
        String name = view.getNameInput();
        String price = view.getPriceInput();
        String stock = view.getStockInput();
        if (name.isEmpty() || price.isEmpty() || stock.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Todos los campos son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        BigDecimal bd;
        int st;
        try {
            bd = new BigDecimal(price);
            st = Integer.parseInt(stock);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Precio o stock no válidos.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Product p = repo.getById(id);
                p.setName(name);
                p.setPrice(bd);
                p.setStock(st);
                repo.update(p);
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(view,
                        "Producto actualizado correctamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            }
        }.execute();
    }

    private void onDelete() {
        int id = view.getSelectedProductId();
        if (id < 0) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view,
                "¿Eliminar producto?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
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
                        "Producto eliminado.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            }
        }.execute();
    }
}
