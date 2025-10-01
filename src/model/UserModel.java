package src.model;

// ═══════════════════════════════════════════════════════════════════════════════
// IMPORTACIONES - Librerías necesarias para trabajar con JDBC y colecciones
// ═══════════════════════════════════════════════════════════════════════════════

import java.sql.Connection;
// Connection: Representa una conexión activa a la base de datos MySQL
// Es el "puente" entre Java y la base de datos

import java.sql.PreparedStatement;
// PreparedStatement: Consulta SQL precompilada y parametrizada
// VENTAJAS sobre Statement normal:
// 1. Previene SQL Injection (ataques de seguridad)
// 2. Mejor rendimiento (la consulta se precompila)
// 3. Más seguro y fácil de usar

import java.sql.ResultSet;
// ResultSet: Almacena los resultados de una consulta SELECT
// Funciona como un cursor que recorre las filas devueltas

import java.sql.SQLException;
// SQLException: Excepción que se lanza cuando hay errores de base de datos
// (conexión fallida, query mal formado, etc.)

import java.util.ArrayList;
// ArrayList: Implementación de lista dinámica
// Usada para almacenar múltiples objetos User

import java.util.List;
// List: Interfaz que define el contrato de una lista ordenada

import src.config.DatabaseConfig;
// DatabaseConfig: Clase que gestiona conexiones a MySQL
// Proporciona métodos para obtener y cerrar conexiones

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * UserModel - MODELO en el patrón MVC (Model-View-Controller)
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * PATRÓN MVC:
 *
 *   ┌─────────────────────────────────────────────────────────┐
 *   │                    MODELO (Model)                       │
 *   │                                                          │
 *   │  - Maneja DATOS y LÓGICA DE NEGOCIO                    │
 *   │  - Interactúa con la base de datos                     │
 *   │  - Valida información                                  │
 *   │  - NO sabe nada de la interfaz gráfica                 │
 *   │                                                          │
 *   │  ESTA CLASE = UserModel.java                           │
 *   └──────────────────────┬──────────────────────────────────┘
 *                          │
 *                          │ Notifica cambios
 *                          ▼
 *   ┌─────────────────────────────────────────────────────────┐
 *   │                CONTROLADOR (Controller)                 │
 *   │                                                          │
 *   │  - COORDINA Model y View                               │
 *   │  - Recibe eventos del usuario desde View              │
 *   │  - Actualiza Model con nuevos datos                    │
 *   │  - Pide a View que se actualice                        │
 *   │                                                          │
 *   │  UserController.java                                    │
 *   └──────────────────────┬──────────────────────────────────┘
 *                          │
 *                          │ Actualiza interfaz
 *                          ▼
 *   ┌─────────────────────────────────────────────────────────┐
 *   │                     VISTA (View)                        │
 *   │                                                          │
 *   │  - Muestra INTERFAZ GRÁFICA al usuario                 │
 *   │  - Captura eventos (clics, inputs)                     │
 *   │  - Envía eventos al Controller                         │
 *   │  - NO tiene lógica de negocio                          │
 *   │                                                          │
 *   │  UserView.java                                          │
 *   └─────────────────────────────────────────────────────────┘
 *
 * RESPONSABILIDADES DE ESTA CLASE (UserModel):
 * 1. Conectar con la base de datos
 * 2. Ejecutar operaciones CRUD (Create, Read, Update, Delete)
 * 3. Validar datos de negocio
 * 4. Manejar excepciones de base de datos
 * 5. Retornar datos al Controller
 *
 * DIFERENCIA CON ARQUITECTURA EN CAPAS:
 * - En capas: Model solo tiene datos, Service tiene lógica, DAO tiene SQL
 * - En MVC: Model tiene TODO (datos + lógica + SQL)
 *
 * ¿CUÁNDO USAR MVC?
 * - Proyectos pequeños-medianos
 * - Aplicaciones con interfaz gráfica (desktop, web)
 * - Cuando se quiere simplicidad sobre escalabilidad
 *
 * ¿CUÁNDO USAR CAPAS?
 * - Proyectos grandes/empresariales
 * - Cuando se necesita alta escalabilidad
 * - Cuando el equipo es grande
 */
public class UserModel {

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: save() - CREAR un nuevo usuario (CREATE del CRUD)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * save() - Guarda un nuevo usuario en la base de datos
     *
     * SQL EJECUTADO: INSERT INTO users (name, email, edad) VALUES (?, ?, ?)
     *
     * FLUJO COMPLETO:
     * 1. VALIDAR datos (lógica de negocio)
     * 2. CONECTAR a la base de datos
     * 3. PREPARAR consulta SQL con placeholders (?)
     * 4. ASIGNAR valores a los placeholders
     * 5. EJECUTAR la consulta
     * 6. CERRAR recursos (SIEMPRE en finally)
     *
     * @param user - Objeto User con los datos a guardar (sin ID, MySQL lo genera)
     * @return boolean - true si se guardó exitosamente, false si falló
     * @throws SQLException - Si hay error de base de datos
     * @throws IllegalArgumentException - Si los datos son inválidos
     */
    public boolean save(User user) throws SQLException {

        // ════════════════ PASO 1: VALIDACIONES DE NEGOCIO ════════════════

        // Validación 1: El nombre no puede estar vacío
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            // trim() elimina espacios al inicio y final
            // isEmpty() verifica si está vacío
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        // Validación 2: El email no puede estar vacío
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        // Validación 3: El email debe tener formato válido (contener @)
        if (!user.getEmail().contains("@")) {
            // contains() verifica si contiene el carácter @
            throw new IllegalArgumentException("El email debe contener @");
        }

        // Validación 4: La edad debe ser mayor a 0
        if (user.getAge() <= 0) {
            throw new IllegalArgumentException("La edad debe ser mayor a 0");
        }

        // Validación 5: La edad debe estar en un rango razonable
        if (user.getAge() > 150) {
            throw new IllegalArgumentException("La edad debe ser menor o igual a 150");
        }

        // ════════════════ PASO 2: ACCESO A BASE DE DATOS ════════════════

        // Declaramos variables como null (se inicializarán en el try)
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // PASO 2.1: Obtener conexión a MySQL
            conn = DatabaseConfig.getConnection();

            // PASO 2.2: Definir consulta SQL con placeholders (?)
            // NO incluimos 'id' porque es AUTO_INCREMENT (MySQL lo asigna automáticamente)
            String sql = "INSERT INTO users (name, email, edad) VALUES (?, ?, ?)";

            // PASO 2.3: Preparar el statement (MySQL precompila la consulta)
            pstmt = conn.prepareStatement(sql);

            // PASO 2.4: Asignar valores a los placeholders
            // IMPORTANTE: Los índices empiezan en 1 (NO en 0 como arrays)
            pstmt.setString(1, user.getName());  // Primer ? = name
            pstmt.setString(2, user.getEmail()); // Segundo ? = email
            pstmt.setInt(3, user.getAge());      // Tercer ? = edad

            // PASO 2.5: Ejecutar el INSERT
            // executeUpdate() se usa para INSERT/UPDATE/DELETE
            // Retorna el número de filas afectadas
            int rowsAffected = pstmt.executeUpdate();

            // Si insertó al menos 1 fila → éxito
            return rowsAffected > 0;

        } finally {
            // PASO 2.6: CERRAR RECURSOS (se ejecuta SIEMPRE)
            // Es CRÍTICO cerrar para evitar memory leaks

            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error cerrando PreparedStatement: " + e.getMessage());
                }
            }

            DatabaseConfig.closeConnection(conn);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: findById() - LEER un usuario por ID (READ del CRUD)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * findById() - Busca un usuario por su ID
     *
     * SQL EJECUTADO: SELECT id, name, email, edad FROM users WHERE id = ?
     *
     * FLUJO:
     * 1. VALIDAR que el ID sea positivo
     * 2. CONECTAR a la base de datos
     * 3. EJECUTAR consulta SELECT
     * 4. PROCESAR resultado con if (esperamos 1 resultado)
     * 5. CREAR objeto User con los datos
     * 6. CERRAR recursos
     *
     * @param id - ID del usuario a buscar
     * @return User - Usuario encontrado, o null si no existe
     * @throws SQLException - Si hay error de base de datos
     * @throws IllegalArgumentException - Si el ID es inválido
     */
    public User findById(int id) throws SQLException {

        // VALIDACIÓN: El ID debe ser positivo
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null; // Almacena las filas devueltas por SELECT

        try {
            conn = DatabaseConfig.getConnection();

            // Consulta SELECT con filtro WHERE
            String sql = "SELECT id, name, email, edad FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            // executeQuery() es EXCLUSIVO para SELECT (devuelve ResultSet)
            rs = pstmt.executeQuery();

            // ¿QUÉ ES ResultSet?
            // - Es un CURSOR que apunta a las filas devueltas
            // - Inicialmente apunta ANTES de la primera fila
            // - rs.next() mueve el cursor a la siguiente fila

            if (rs.next()) {
                // rs.next() devolvió true → HAY una fila → el ID existe

                // Extraer valores de la fila actual
                // Podemos usar nombres de columna (recomendado) o índices
                int userId = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                int age = rs.getInt("edad");

                // Crear y retornar objeto User
                return new User(userId, name, email, age);
            }

            // rs.next() devolvió false → NO HAY filas → el ID no existe
            return null;

        } finally {
            // Cerrar recursos en orden inverso a su creación
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("Error cerrando ResultSet: " + e.getMessage());
                }
            }

            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error cerrando PreparedStatement: " + e.getMessage());
                }
            }

            DatabaseConfig.closeConnection(conn);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: findAll() - LEER TODOS los usuarios (READ del CRUD)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * findAll() - Obtiene todos los usuarios de la base de datos
     *
     * SQL EJECUTADO: SELECT id, name, email, edad FROM users
     *
     * DIFERENCIA con findById():
     * - findById() usa IF porque espera 1 resultado
     * - findAll() usa WHILE porque espera múltiples resultados
     *
     * FLUJO:
     * 1. CREAR lista vacía
     * 2. CONECTAR a base de datos
     * 3. EJECUTAR SELECT sin WHERE (todos los registros)
     * 4. RECORRER todas las filas con while
     * 5. Por cada fila: crear User y agregarlo a la lista
     * 6. RETORNAR lista completa
     * 7. CERRAR recursos
     *
     * @return List<User> - Lista con todos los usuarios (puede estar vacía)
     * @throws SQLException - Si hay error de base de datos
     */
    public List<User> findAll() throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Crear lista vacía para almacenar usuarios
        List<User> users = new ArrayList<>();

        try {
            conn = DatabaseConfig.getConnection();

            // SELECT sin WHERE trae TODOS los registros
            String sql = "SELECT id, name, email, edad FROM users";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            // WHILE recorre TODAS las filas
            // Diferencia:
            // - IF: ejecuta UNA vez si hay resultado
            // - WHILE: se repite MIENTRAS haya más filas

            while (rs.next()) {
                // Este bloque se ejecuta UNA VEZ por cada fila

                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                int age = rs.getInt("edad");

                User user = new User(id, name, email, age);
                users.add(user); // Agregar a la lista

                // rs.next() se ejecuta nuevamente al inicio del while
                // Si hay más filas → true → repite
                // Si NO hay más → false → sale del bucle
            }

            // Al terminar:
            // - Si había 5 usuarios → users.size() == 5
            // - Si NO había usuarios → users.size() == 0 (lista vacía, no null)

            return users;

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("Error cerrando ResultSet: " + e.getMessage());
                }
            }

            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error cerrando PreparedStatement: " + e.getMessage());
                }
            }

            DatabaseConfig.closeConnection(conn);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: update() - ACTUALIZAR un usuario existente (UPDATE del CRUD)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * update() - Actualiza los datos de un usuario existente
     *
     * SQL EJECUTADO: UPDATE users SET name = ?, email = ?, edad = ? WHERE id = ?
     *
     * IMPORTANTE:
     * - El WHERE es CRÍTICO: sin él actualizaría TODOS los registros
     * - Solo actualiza campos modificables (name, email, edad)
     * - El ID NO se modifica (es inmutable)
     *
     * FLUJO:
     * 1. VALIDAR datos (igual que save)
     * 2. CONECTAR a base de datos
     * 3. PREPARAR UPDATE con SET y WHERE
     * 4. ASIGNAR valores (incluyendo ID para el WHERE)
     * 5. EJECUTAR
     * 6. VERIFICAR si actualizó algo
     * 7. CERRAR recursos
     *
     * @param user - Usuario con nuevos datos (debe tener ID válido)
     * @return boolean - true si se actualizó, false si el ID no existe
     * @throws SQLException - Si hay error de base de datos
     * @throws IllegalArgumentException - Si los datos son inválidos
     */
    public boolean update(User user) throws SQLException {

        // VALIDACIONES (iguales que save)
        if (user.getId() <= 0) {
            throw new IllegalArgumentException("El ID es inválido");
        }

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("El email debe contener @");
        }

        if (user.getAge() <= 0 || user.getAge() > 150) {
            throw new IllegalArgumentException("La edad debe estar entre 1 y 150");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConfig.getConnection();

            // UPDATE con SET (qué modificar) y WHERE (cuál registro)
            String sql = "UPDATE users SET name = ?, email = ?, edad = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            // ORDEN CRÍTICO: debe coincidir con los ? en el SQL
            pstmt.setString(1, user.getName());  // Primer ? = name (SET)
            pstmt.setString(2, user.getEmail()); // Segundo ? = email (SET)
            pstmt.setInt(3, user.getAge());      // Tercer ? = edad (SET)
            pstmt.setInt(4, user.getId());       // Cuarto ? = id (WHERE)

            int rowsAffected = pstmt.executeUpdate();

            // rowsAffected > 0 → Se actualizó al menos 1 registro
            // rowsAffected == 0 → El ID no existe
            return rowsAffected > 0;

        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error cerrando PreparedStatement: " + e.getMessage());
                }
            }

            DatabaseConfig.closeConnection(conn);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: delete() - ELIMINAR un usuario (DELETE del CRUD)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * delete() - Elimina un usuario de la base de datos
     *
     * SQL EJECUTADO: DELETE FROM users WHERE id = ?
     *
     * ADVERTENCIA:
     * - Esta operación es IRREVERSIBLE
     * - El registro se elimina PERMANENTEMENTE
     * - El WHERE es CRÍTICO: sin él eliminaría TODOS los registros
     *
     * FLUJO:
     * 1. VALIDAR que el ID sea positivo
     * 2. CONECTAR a base de datos
     * 3. PREPARAR DELETE con WHERE
     * 4. ASIGNAR ID al placeholder
     * 5. EJECUTAR
     * 6. VERIFICAR si eliminó algo
     * 7. CERRAR recursos
     *
     * @param id - ID del usuario a eliminar
     * @return boolean - true si se eliminó, false si el ID no existe
     * @throws SQLException - Si hay error de base de datos
     * @throws IllegalArgumentException - Si el ID es inválido
     */
    public boolean delete(int id) throws SQLException {

        // VALIDACIÓN: ID debe ser positivo
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConfig.getConnection();

            // DELETE con WHERE obligatorio
            // ⚠️ PELIGRO: DELETE sin WHERE eliminaría TODA la tabla
            String sql = "DELETE FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            // rowsAffected > 0 → Se eliminó el registro
            // rowsAffected == 0 → El ID no existe
            return rowsAffected > 0;

        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error cerrando PreparedStatement: " + e.getMessage());
                }
            }

            DatabaseConfig.closeConnection(conn);
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONCEPTOS CLAVE - RESUMEN EDUCATIVO
// ═══════════════════════════════════════════════════════════════════════════════

// 1. MVC vs ARQUITECTURA EN CAPAS:
//
//    MVC (este proyecto):
//    ├── Model (UserModel.java) → TODO: validaciones + lógica + SQL
//    ├── View (UserView.java) → Solo interfaz gráfica
//    └── Controller (UserController.java) → Coordina Model y View
//
//    Arquitectura en Capas (layered-architecture branch):
//    ├── Model (User.java) → Solo datos (POJO)
//    ├── Service (UserService.java) → Validaciones y lógica de negocio
//    ├── DAO (UserDAOImpl.java) → SQL y acceso a datos
//    └── UI (Main.java) → Interfaz gráfica
//
//    ¿Cuál usar?
//    - MVC: Proyectos pequeños, desarrollo rápido, simplicidad
//    - Capas: Proyectos grandes, equipos grandes, alta escalabilidad

// 2. JDBC - Componentes principales:
//    - Connection: Conexión a la base de datos
//    - PreparedStatement: Consulta SQL precompilada
//    - ResultSet: Resultados de un SELECT

// 3. PreparedStatement - ¿Por qué usarlo?
//    ✅ SEGURIDAD: Previene SQL Injection
//    ✅ RENDIMIENTO: MySQL precompila la consulta
//    ✅ FACILIDAD: Escapa caracteres automáticamente

// 4. SQL Injection - Ejemplo de ataque que PreparedStatement previene:
//    VULNERABLE:
//    String sql = "SELECT * FROM users WHERE name = '" + name + "'";
//    Si name = "'; DROP TABLE users; --" → ¡DESASTRE!
//
//    SEGURO:
//    String sql = "SELECT * FROM users WHERE name = ?";
//    pstmt.setString(1, name);
//    El valor se escapa, no puede inyectar código SQL

// 5. executeQuery() vs executeUpdate():
//    - executeQuery(): Solo para SELECT → devuelve ResultSet
//    - executeUpdate(): Para INSERT/UPDATE/DELETE → devuelve int (filas afectadas)

// 6. ResultSet - Cómo funciona:
//    - Es un CURSOR sobre las filas devueltas
//    - Inicialmente apunta ANTES de la primera fila
//    - next(): Avanza a la siguiente fila
//      · Devuelve true si hay fila
//      · Devuelve false si no hay más
//    - getXxx(): Extrae valores de la fila actual

// 7. Patrón de uso de ResultSet:
//    if (rs.next()) { ... }     // Para 1 resultado (findById)
//    while (rs.next()) { ... }  // Para múltiples resultados (findAll)

// 8. Bloque FINALLY - ¿Por qué es importante?
//    - Se ejecuta SIEMPRE (incluso si hay return o excepción)
//    - Garantiza que los recursos se liberen
//    - Evita memory leaks
//    - MySQL tiene límite de conexiones (ej: 151)

// 9. Cerrar recursos - Orden correcto:
//    ResultSet → PreparedStatement → Connection
//    (Inverso al orden de creación)

// 10. Validaciones de negocio:
//     - IllegalArgumentException: Para datos inválidos
//     - SQLException: Para errores de base de datos
//     - Validar ANTES de acceder a la BD (eficiencia)