package util;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Grupo 3
 */
public class ResultSetMapper {

    /**
     * Mapea todas las filas del ResultSet a una lista de objetos T.
     *
     * @param rs ResultSet ya posicionado antes de la primera fila
     * @param clazz Clase destino con constructor vacío
     * @param <T> Tipo genérico
     * @return Lista de objetos mapeados
     */
    public static <T> List<T> mapAll(ResultSet rs, Class<T> clazz) throws SQLException {
        List<T> output = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        while (rs.next()) {
            try {
                T instance = clazz.getDeclaredConstructor().newInstance();

                for (int i = 1; i <= columnCount; i++) {
                    String columnLabel = meta.getColumnLabel(i);
                    Object value = rs.getObject(i);

                    if (value instanceof java.time.LocalDateTime localDateTime) {
                        value = java.sql.Timestamp.valueOf(localDateTime);
                    }

                    String setterName = "set" + toPascalCase(columnLabel);

                    boolean found = false;
                    for (Method m : clazz.getMethods()) {
                        if (m.getName().equals(setterName) && m.getParameterCount() == 1) {
                            m.invoke(instance, value);
                            found = true;
                            break;
                        }
                    }
                }

                output.add(instance);
            } catch (Exception e) {
                throw new SQLException("Error mapeando fila a "
                        + clazz.getSimpleName(), e);
            }
        }
        return output;
    }

    /**
     * Convierte nombres_snake_o_column INTO NombresSnakeOColumn
     */
    private static String toPascalCase(String text) {
        StringBuilder sb = new StringBuilder();
        boolean nextUpper = true;
        for (char c : text.toCharArray()) {
            if (c == '_' || c == ' ') {
                nextUpper = true;
            } else if (nextUpper) {
                sb.append(Character.toUpperCase(c));
                nextUpper = false;
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }
}
