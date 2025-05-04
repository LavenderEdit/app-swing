package controller;

import model.Role;
import repository.RoleRepository;
import view.internal.RoleInternalFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Grupo 3
 */
public class RoleController {

    private final RoleInternalFrame view;
    private final RoleRepository repo;
    private List<Role> cache = new ArrayList<>();

    public RoleController(RoleInternalFrame view) {
        this.view = view;
        this.repo = new RoleRepository();
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
                    if (!evt.getValueIsAdjusting() && view.getSelectedRoleTableId() >= 0) {
                        onTableSelection();
                    }
                });
    }

    private void loadTable() {
        new SwingWorker<List<Role>, Void>() {
            @Override
            protected List<Role> doInBackground() throws Exception {
                return repo.getAll();
            }

            @Override
            protected void done() {
                try {
                    cache = get();
                    List<Object[]> rows = new ArrayList<>();
                    for (Role r : cache) {
                        rows.add(new Object[]{r.getId(), r.getName(), r.getDescription()});
                    }
                    view.setTableData(rows);
                    view.resetMode();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view,
                            "Error cargando roles: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void onTableSelection() {
        int id = view.getSelectedRoleTableId();
        try {
            Role r = repo.getById(id);
            if (r != null) {
                view.populateFields(r);
                view.enterEditMode();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error cargando rol seleccionado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCreate() {
        String name = view.getNameInput();
        String desc = view.getDescriptionInput();
        if (name.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Todos los campos son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Role r = new Role();
                r.setName(name);
                r.setDescription(desc);
                repo.create(r);
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(view,
                        "Rol creado correctamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            }
        }.execute();
    }

    private void onEdit() {
        int id = view.getSelectedRoleTableId();
        if (id < 0) {
            return;
        }
        String name = view.getNameInput();
        String desc = view.getDescriptionInput();
        if (name.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Todos los campos son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Role r = repo.getById(id);
                r.setName(name);
                r.setDescription(desc);
                repo.update(r);
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(view,
                        "Rol actualizado correctamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            }
        }.execute();
    }

    private void onDelete() {
        int id = view.getSelectedRoleTableId();
        if (id < 0) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view,
                "¿Eliminar rol?", "Confirmar",
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
                        "Rol eliminado.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            }
        }.execute();
    }
}
