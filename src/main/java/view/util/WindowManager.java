package view.util;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import view.internal.UserInternalFrame;
import view.internal.RoleInternalFrame;
import view.internal.ProductInternalFrame;
import view.internal.OrderInternalFrame;
import view.internal.OrderItemInternalFrame;
import controller.UserController;
import controller.RoleController;
import controller.ProductController;
import controller.OrderController;
import controller.OrderItemController;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Grupo 3
 */
public class WindowManager {

    private static WindowManager instance;
    private final Map<Class<? extends JInternalFrame>, JInternalFrame> openFrames = new HashMap<>();
    private JDesktopPane desktop;

    private WindowManager() {
    }

    public static void initialize(JDesktopPane desktop) {
        if (instance == null) {
            instance = new WindowManager();
        }
        instance.desktop = desktop;
    }

    public static WindowManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("WindowManager no ha sido inicializado.");
        }
        return instance;
    }

    public <T extends JInternalFrame> void showFrame(Class<T> frameClass) {
        try {
            if (openFrames.containsKey(frameClass)) {
                JInternalFrame existing = openFrames.get(frameClass);
                existing.toFront();
                existing.setSelected(true);
                return;
            }

            T frame = frameClass.getDeclaredConstructor().newInstance();
            frame.setClosable(true);
            frame.setIconifiable(true);
            frame.setMaximizable(true);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

            desktop.add(frame);
            centerFrame(frame);
            frame.setVisible(true);
            frame.setSelected(true);

            switch (frame) {
                case UserInternalFrame userInternalFrame -> {
                    UserController userController = new UserController(userInternalFrame);
                }
                case RoleInternalFrame roleInternalFrame -> {
                    RoleController roleController = new RoleController(roleInternalFrame);
                }
                case ProductInternalFrame productInternalFrame -> {
                    ProductController productController = new ProductController(productInternalFrame);
                }
                case OrderInternalFrame orderInternalFrame -> {
                    OrderController orderController = new OrderController(orderInternalFrame);
                }
                case OrderItemInternalFrame orderItemInternalFrame -> {
                    OrderItemController orderItemController = new OrderItemController(orderItemInternalFrame);
                }
                default -> {
                }
            }

            openFrames.put(frameClass, frame);
            frame.addInternalFrameListener(new InternalFrameAdapter() {
                @Override
                public void internalFrameClosed(InternalFrameEvent e) {
                    openFrames.remove(frameClass);
                }
            });

        } catch (PropertyVetoException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
            JOptionPane.showMessageDialog(desktop,
                    "Error al abrir ventana: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void centerFrame(JInternalFrame frame) {
        Dimension desktopSize = desktop.getSize();
        Dimension frameSize = frame.getSize();
        frame.setLocation(
                (desktopSize.width - frameSize.width) / 2,
                (desktopSize.height - frameSize.height) / 2
        );
    }
}
