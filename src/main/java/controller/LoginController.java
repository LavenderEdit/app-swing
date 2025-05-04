package controller;

import model.User;
import repository.UserRepository;
import repository.RoleRepository;
import model.Role;
import view.LoginDialog;
import view.MainFrame;

import javax.swing.*;

public class LoginController {

    private final LoginDialog dialog;
    private final UserRepository userRepo = new UserRepository();
    private final RoleRepository roleRepo = new RoleRepository();

    public LoginController(LoginDialog dialog) {
        this.dialog = dialog;
        initListeners();
    }

    private void initListeners() {
        dialog.addLoginListener(e -> onLogin());
        dialog.addCancelListener(e -> System.exit(0));
    }

    private void onLogin() {
        String email = dialog.getEmail();
        String pwd = dialog.getPassword();

        if (email.isEmpty() || pwd.isEmpty()) {
            JOptionPane.showMessageDialog(dialog,
                    "Email y contraseña son obligatorios",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User u = userRepo.getByEmail(email);
            if (u == null || !u.verifyPassword(pwd)) {
                JOptionPane.showMessageDialog(dialog,
                        "Credenciales incorrectas",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Role r = roleRepo.getById(u.getRoleId());
            if (r == null || !"admin".equalsIgnoreCase(r.getName())) {
                JOptionPane.showMessageDialog(dialog,
                        "Acceso denegado: solo administradores pueden ingresar",
                        "Permiso denegado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dialog.dispose();
            SwingUtilities.invokeLater(() -> {
                MainFrame mf = new MainFrame();
                mf.setVisible(true);
            });

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog,
                    "Error en autenticación:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void show() {
        dialog.setVisible(true);
    }
}
