# Sistema de Gestión Comercial - Digital Búho S.A.C.

![Logo Digital Búho](https://cdn.worldvectorlogo.com/logos/digital-buho-1.svg)

Sistema de gestión integral para negocios desarrollado en Java Swing con arquitectura MVC.

## 🚀 Características Principales

- **Módulos Completos**:
  - ✅ Gestión de Usuarios y Roles
  - ✅ Administración de Productos
  - ✅ Procesamiento de Órdenes y Boletas
  - ✅ Reportes y consultas avanzadas

- **Tecnologías Clave**:
  - Java 21 + Swing (Interfaz gráfica)
  - MySQL + HikariCP (Base de datos y connection pooling)
  - JBCrypt (Seguridad de contraseñas)
  - Jackson (Manejo de JSON)

## 🛠️ Configuración del Entorno

1. **Requisitos**:
   - JDK 21
   - Maven 3.9+
   - MySQL 8.0+

2. **Instalación**:
   ```bash
   git clone [repo-url]
   cd app-swing
   mvn clean install -Pdist
   ```

3. **Configuración BD**:
   - Crear base de datos `app_swing`
   - Configurar credenciales en `src/main/java/config/DatabaseConfig.java`

## 🏗️ Estructura del Proyecto

```
app-swing/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── config/        # Configuración DB
│   │   │   ├── controller/    # Lógica de negocio
│   │   │   ├── model/         # Entidades y DTOs
│   │   │   ├── repository/    # Acceso a datos
│   │   │   ├── util/          # Utilidades
│   │   │   ├── view/          # Interfaces gráficas
│   │   ├── resources/         # Assets e imágenes
│   ├── test/                  # Pruebas (pendiente)
├── target/                    # Build outputs
├── pom.xml                    # Config Maven
└── README.md                  # Este archivo
```

## 🖥️ Uso del Sistema

1. **Iniciar Aplicación**:
   ```bash
   java -jar target/app-swing-dist.jar
   ```

2. **Flujo Principal**:
   - Login → Pantalla Principal → Gestión de módulos

3. **Credenciales**:
   - Usuario: admin@digitalbuho.com
   - Contraseña: [configurar durante instalación]

## 🛡️ Arquitectura y Patrones

- **MVC** (Modelo-Vista-Controlador)
- **Repository Pattern** (Acceso a datos)
- **Singleton** (Conexiones DB)
- **DTOs** (Transferencia de datos optimizada)

## 📦 Dependencias Principales

| Librería | Versión | Propósito |
|----------|---------|-----------|
| MySQL Connector | 9.3.0 | Conexión MySQL |
| HikariCP | 5.1.0 | Connection Pool |
| JBCrypt | 0.4 | Hashing contraseñas |
| Jackson | 2.15.0 | Serialización JSON |

## 📌 Roadmap

- [ ] Implementar módulo de reportes
- [ ] Añadir gráficos estadísticos
- [ ] Sistema de backup automático
- [ ] Internacionalización (i18n)

## 📄 Licencia

Proyecto desarrollado por **Joan Lavender**  
© 2025 - Todos los derechos reservados
