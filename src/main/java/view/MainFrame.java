package view;

import controller.LoginController;
import view.internal.*;
import view.util.WindowManager;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyVetoException;
import java.net.URL;
import javax.swing.*;

public class MainFrame extends JFrame {

    private final JDesktopPane desktopPane;
    private final JToolBar toolBar;
    private JPanel infoPanel;

    public MainFrame() {
        setTitle("Sistema de Gestión");
        URL iconUrl = getClass().getResource("/resources/digital_buho_icon.png");
        if (iconUrl != null) {
            setIconImage(new ImageIcon(iconUrl).getImage());
        }

        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        desktopPane = new JDesktopPane() {
            private final Image bg = loadBackgroundImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) {
                    g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                }
            }

            private Image loadBackgroundImage() {
                URL bgUrl = getClass().getResource("/resources/Trigger_Out_Of_Sight.jpg");
                if (bgUrl != null) {
                    return new ImageIcon(bgUrl).getImage();
                }
                return null;
            }
        };
        add(desktopPane, BorderLayout.CENTER);
        WindowManager.initialize(desktopPane);

        initInfoOverlay();

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);
        addToolbarButton("Usuarios", UserInternalFrame.class);
        addToolbarButton("Roles", RoleInternalFrame.class);
        addToolbarButton("Productos", ProductInternalFrame.class);
        addToolbarButton("Órdenes", OrderInternalFrame.class);
        addToolbarButton("Emisión de Boletas", OrderItemInternalFrame.class);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuSesion = new JMenu("Sesión");
        JMenuItem mCerrar = new JMenuItem("Cerrar sesión");
        mCerrar.addActionListener(e -> onLogout());
        menuSesion.add(mCerrar);
        menuBar.add(menuSesion);

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem mSalir = new JMenuItem("Salir");
        mSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(mSalir);
        menuBar.add(menuArchivo);

        setJMenuBar(menuBar);
    }

    private void initInfoOverlay() {
        infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(true);
        infoPanel.setBackground(new Color(255, 255, 255, 200)); // blanco semitransparente
        infoPanel.setBounds(50, 50, getWidth() - 100, getHeight() - 100);

        desktopPane.add(infoPanel, JLayeredPane.PALETTE_LAYER);

        String html = """
            <html>
            <body style='font-family:sans-serif; padding:15px; color:#333;'>
              <h1>Bienvenido a DIGITAL BUHO S.A.C.</h1>
              <p><strong>RUC:</strong> 20603684291<br>
              <strong>Director:</strong> Enrique Baca Deyvis Daniel<br>
              <strong>Dirección:</strong> Urbanización Villa Los Ángeles, Altura de la 4 de Seoane 232<br>
              <strong>Tipo:</strong> MYPE<br>
              <strong>Condición:</strong> SOCIO</p>
              <h2>Misión</h2>
              <p>Ayudar a los negocios y emprendedores a despegar su marca en el mundo digital mediante un trabajo creativo y comprometido.</p>
              <h2>Visión</h2>
              <p>Ser punto de referencia en ofrecer las mejores soluciones de tecnología Digital en el Perú.</p>
              <h2>Valores</h2>
              <ul>
                <li>Integridad</li>
                <li>Servicio</li>
                <li>Compromiso</li>
                <li>Respeto</li>
                <li>Responsabilidad</li>
              </ul>
            </body>
            </html>
            """;

        JEditorPane editor = new JEditorPane("text/html", html);
        editor.setEditable(false);
        editor.setOpaque(false);

        JScrollPane scroll = new JScrollPane(editor);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        infoPanel.add(scroll, BorderLayout.CENTER);

        desktopPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                infoPanel.setBounds(50, 50, desktopPane.getWidth() - 100, desktopPane.getHeight() - 100);
            }
        });
    }

    private <T extends JInternalFrame> void addToolbarButton(String title, Class<T> cls) {
        JButton btn = new JButton(title);
        btn.addActionListener(e -> {
            WindowManager.getInstance().showFrame(cls);
            SwingUtilities.invokeLater(() -> {
                for (JInternalFrame frame : desktopPane.getAllFrames()) {
                    if (frame.getClass().equals(cls)) {
                        desktopPane.setLayer(frame, JLayeredPane.MODAL_LAYER);
                        frame.toFront();
                        try {
                            frame.setSelected(true);
                        } catch (PropertyVetoException ignore) {
                        }
                        break;
                    }
                }
            });
        });
        toolBar.add(btn);
    }

    private void onLogout() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            LoginDialog login = new LoginDialog(null);
            new LoginController(login).show();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginDialog login = new LoginDialog(null);
            new LoginController(login).show();
        });
    }
}
