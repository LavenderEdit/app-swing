package view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoginDialog extends JDialog {

    private final JTextField txtEmail = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JButton btnLogin = new JButton("Ingresar");
    private final JButton btnCancel = new JButton("Cancelar");
    private boolean succeeded = false;

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);
        URL iconUrl = getClass().getResource("/resources/digital_buho_icon.png");
        if (iconUrl != null) {
            setIconImage(new ImageIcon(iconUrl).getImage());
        }
        setLayout(null);
        setSize(350, 200);
        setLocationRelativeTo(parent);

        add(new JLabel("Email:")).setBounds(30, 30, 80, 25);
        txtEmail.setBounds(120, 30, 180, 25);
        add(txtEmail);

        add(new JLabel("Contraseña:")).setBounds(30, 70, 80, 25);
        txtPassword.setBounds(120, 70, 180, 25);
        add(txtPassword);

        btnLogin.setBounds(70, 120, 100, 30);
        add(btnLogin);

        btnCancel.setBounds(180, 120, 100, 30);
        add(btnCancel);
    }

    public String getEmail() {
        return txtEmail.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public void addLoginListener(java.awt.event.ActionListener l) {
        btnLogin.addActionListener(l);
    }

    public void addCancelListener(java.awt.event.ActionListener l) {
        btnCancel.addActionListener(l);
    }

    public void setSucceeded(boolean s) {
        succeeded = s;
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
