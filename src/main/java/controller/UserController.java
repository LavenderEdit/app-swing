package controller;

import model.User;
import model.DTO.UserWithRole;
import repository.UserRepository;
import view.internal.UserInternalFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Grupo 3
 */
public class UserController {

    private final UserInternalFrame view;
    private final UserRepository repo;
    private List<UserWithRole> cache = new ArrayList<>();

    public UserController(UserInternalFrame view) {
        this.view = view;
        this.repo = new UserRepository();
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
        view.addChangePwdListener(e -> onChangePassword());

        view.getTable()
                .getSelectionModel()
                .addListSelectionListener((ListSelectionListener) evt -> {
                    if (!evt.getValueIsAdjusting() && view.getSelectedUserId() >= 0) {
                        onTableSelection();
                    }
                });
    }

    private void onTableSelection() {
        int id = view.getSelectedUserId();
        if (id < 0) {
            return;
        }
        try {
            UserWithRole u = repo.getUserWithRole(id);
            if (u != null) {
                view.populateFields(u);
                view.enterEditMode();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error cargando usuario seleccionado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTable() {
        new SwingWorker<List<UserWithRole>, Void>() {
            @Override
            protected List<UserWithRole> doInBackground() throws Exception {
                return repo.getAllWithRole();
            }

            @Override
            protected void done() {
                try {
                    cache = get();
                    List<Object[]> rows = new ArrayList<>();
                    for (UserWithRole u : cache) {
                        rows.add(new Object[]{u.getId(), u.getUsername(), u.getEmail(), u.getRoleName()});
                    }
                    view.setTableData(rows);
                    view.resetMode();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view,
                            "Error cargando usuarios: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void onCreate() {
        String user = view.getUsernameInput();
        String password = view.getPasswordInput();
        String email = view.getEmailInput();
        int roleId = view.getSelectedRoleId();

        if (user.isEmpty() || password.isEmpty() || email.isEmpty() || roleId < 0) {
            JOptionPane.showMessageDialog(view,
                    "Todos los campos son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                User u = new User();
                u.setUsername(user);
                u.setPassword(password);
                u.setEmail(email);
                u.setRoleId(roleId);
                repo.create(u);
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(view,
                        "Usuario creado correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            }
        }.execute();
    }

    private void onEdit() {
        int id = view.getSelectedUserId();
        if (id < 0) {
            return;
        }

        String user = view.getUsernameInput();
        String email = view.getEmailInput();
        int roleId = view.getSelectedRoleId();

        if (user.isEmpty() || email.isEmpty() || roleId < 0) {
            JOptionPane.showMessageDialog(view,
                    "Usuario, email y rol son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                User u = repo.getById(id);
                u.setUsername(user);
                u.setEmail(email);
                u.setRoleId(roleId);
                repo.update(u);
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(view,
                        "Usuario actualizado correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            }
        }.execute();
    }

    private void onDelete() {
        int id = view.getSelectedUserId();
        if (id < 0) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "¿Eliminar usuario?", "Confirmar",
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
                        "Usuario eliminado.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            }
        }.execute();
    }

    private void onChangePassword() {
        JPasswordField oldPwd = new JPasswordField();
        JPasswordField newPwd = new JPasswordField();
        JPasswordField rptPwd = new JPasswordField();
        Object[] message = {
            "Contraseña actual:", oldPwd,
            "Nueva contraseña:", newPwd,
            "Repite nueva:", rptPwd
        };

        int option = JOptionPane.showConfirmDialog(
                view, message, "Cambiar contraseña",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        String oldPass = new String(oldPwd.getPassword()).trim();
        String newPass = new String(newPwd.getPassword()).trim();
        String rpt = new String(rptPwd.getPassword()).trim();

        if (oldPass.isEmpty() || newPass.isEmpty() || rpt.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Todos los campos son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!newPass.equals(rpt)) {
            JOptionPane.showMessageDialog(view,
                    "La nueva contraseña y su repetición no coinciden.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = view.getSelectedUserId();
        try {
            User u = repo.getById(id);
            if (!u.verifyPassword(oldPass)) {
                JOptionPane.showMessageDialog(view,
                        "Contraseña actual incorrecta.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            u.setPassword(newPass);
            repo.update(u);
            JOptionPane.showMessageDialog(view,
                    "Contraseña actualizada correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error actualizando contraseña:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
