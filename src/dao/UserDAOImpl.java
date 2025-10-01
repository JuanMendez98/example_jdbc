package src.dao;

// IMPORTACIONES - Todas las clases que necesitamos para trabajar con JDBC

import java.sql.Connection;
// Connection: Representa una conexión activa a MySQL
// Es el "canal de comunicación" entre Java y la base de datos

import java.sql.PreparedStatement;
// PreparedStatement: Consulta SQL precompilada y segura
// VENTAJAS:
// 1. Previene SQL Injection (ataques de seguridad)
// 2. Más eficiente que Statement simple
// 3. Escapa automáticamente caracteres especiales

import java.sql.ResultSet;
// ResultSet: Resultado de un SELECT
// Funciona como un cursor que recorre filas

import java.sql.SQLException;
// SQLException: Excepción para errores de base de datos

import java.util.ArrayList;
// ArrayList: Lista dinámica para almacenar múltiples Users

import java.util.List;
// List: Interfaz de colección ordenada

import src.config.DatabaseConfig;
// DatabaseConfig: Nuestra clase para obtener conexiones

import src.model.User;
// User: Clase modelo (POJO) del usuario

/**
 * UserDAOImpl - Implementación JDBC del patrón DAO
 *
 * Esta clase contiene TODO el código JDBC para las operaciones CRUD. Cada método sigue el mismo
 * patrón: 1. Obtener conexión 2. Preparar SQL 3. Ejecutar 4. Procesar resultado 5. Cerrar recursos
 * (SIEMPRE en finally)
 */
public class UserDAOImpl implements UserDAO {

    /**
     * save() - CREAR un nuevo usuario (CREATE del CRUD)
     *
     * SQL: INSERT INTO users (name, email, age) VALUES (?, ?, ?)
     *
     * FLUJO: 1. Obtener conexión 2. Preparar INSERT con placeholders (?) 3. Asignar valores a los
     * placeholders 4. Ejecutar → MySQL genera ID automáticamente 5. Cerrar recursos
     */
    @Override
    public boolean save(User user) throws SQLException {

        // Declaramos variables como null (se inicializan en el try)
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // PASO 1: Obtener una conexión activa a MySQL
            conn = DatabaseConfig.getConnection();

            // PASO 2: Definir la consulta SQL con placeholders (?)
            // NO incluimos 'id' porque es AUTO_INCREMENT (MySQL lo genera)
            // Los ? serán reemplazados de forma SEGURA (sin SQL injection)
            String sql = "INSERT INTO users (name, email, edad) VALUES (?, ?, ?)";

            // Preparar el statement (MySQL precompila la consulta)
            pstmt = conn.prepareStatement(sql);

            // PASO 3: Asignar valores a los placeholders
            // IMPORTANTE: Los índices empiezan en 1 (no en 0)
            pstmt.setString(1, user.getName()); // Primer ? = name
            pstmt.setString(2, user.getEmail()); // Segundo ? = email
            pstmt.setInt(3, user.getAge()); // Tercer ? = age

            // Ahora la consulta sería algo como:
            // INSERT INTO users (name, email, age) VALUES ('Juan', 'juan@email.com', 25)

            // PASO 4: Ejecutar el INSERT
            // executeUpdate() se usa para INSERT/UPDATE/DELETE
            // Devuelve el número de filas afectadas
            int rowsAffected = pstmt.executeUpdate();

            // Si insertó al menos 1 fila → éxito (true)
            // Si no insertó nada → falló (false)
            return rowsAffected > 0;

        } finally {
            // PASO 5: CERRAR RECURSOS (se ejecuta SIEMPRE, haya o no excepción)
            // Es CRÍTICO cerrar para evitar memory leaks

            // Cerrar PreparedStatement
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error cerrando PreparedStatement: " + e.getMessage());
                }
            }

            // Cerrar Connection
            DatabaseConfig.closeConnection(conn);
        }
    }

    /**
     * findById() - LEER un usuario por ID (READ del CRUD)
     *
     * SQL: SELECT id, name, email, age FROM users WHERE id = ?
     *
     * FLUJO:
     * 1. Obtener conexión
     * 2. Preparar SELECT con WHERE
     * 3. Ejecutar y obtener ResultSet
     * 4. Procesar ResultSet con if
     * 5. Cerrar recursos
     */
    @Override
    public User findById(int id) throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;  // Aquí se guardan las filas devueltas por SELECT

        try {
            // PASO 1: Obtener conexión
            conn = DatabaseConfig.getConnection();

            // PASO 2: Preparar SELECT con WHERE
            // WHERE id = ? filtra para traer solo el usuario con ese ID
            String sql = "SELECT id, name, email, edad FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            // Asignar el ID al placeholder
            pstmt.setInt(1, id);

            // PASO 3: Ejecutar el SELECT
            // executeQuery() es EXCLUSIVO para SELECT (devuelve ResultSet)
            // executeUpdate() es para INSERT/UPDATE/DELETE (devuelve int)
            rs = pstmt.executeQuery();

            // PASO 4: Procesar el ResultSet
            // ¿Qué es ResultSet?
            // - Es como un CURSOR que apunta a las filas devueltas
            // - Inicialmente apunta ANTES de la primera fila
            // - rs.next() mueve el cursor a la siguiente fila

            if (rs.next()) {
                // rs.next() devolvió true → HAY una fila → el ID existe

                // Extraer valores de la fila actual usando rs.getXxx()
                // Podemos usar:
                // - Índice de columna: rs.getInt(1), rs.getString(2)...
                // - Nombre de columna: rs.getInt("id"), rs.getString("name")...
                // RECOMENDADO: usar nombres (más legible)

                int userId = rs.getInt("id");           // Columna 'id' → int
                String name = rs.getString("name");     // Columna 'name' → String
                String email = rs.getString("email");   // Columna 'email' → String
                int age = rs.getInt("edad");            // Columna 'edad' → int

                // Crear objeto User con los datos extraídos y devolverlo
                return new User(userId, name, email, age);
            }

            // rs.next() devolvió false → NO HAY filas → el ID no existe
            return null;

        } finally {
            // PASO 5: Cerrar recursos
            // ORDEN IMPORTANTE: cerrar en orden inverso a su creación
            // ResultSet → PreparedStatement → Connection

            if (rs != null) {
                try {
                    rs.close();  // Liberar recursos del ResultSet
                } catch (SQLException e) {
                    System.err.println("Error cerrando ResultSet: " + e.getMessage());
                }
            }

            if (pstmt != null) {
                try {
                    pstmt.close();  // Liberar recursos del PreparedStatement
                } catch (SQLException e) {
                    System.err.println("Error cerrando PreparedStatement: " + e.getMessage());
                }
            }

            DatabaseConfig.closeConnection(conn);  // Liberar conexión
        }
    }

    /**
     * findAll() - LEER TODOS los usuarios (READ del CRUD)
     *
     * SQL: SELECT id, name, email, age FROM users
     *
     * DIFERENCIA con findById():
     * - findById() usa IF porque espera 1 resultado
     * - findAll() usa WHILE porque espera múltiples resultados
     *
     * FLUJO:
     * 1. Crear lista vacía
     * 2. Obtener conexión
     * 3. Preparar SELECT sin WHERE (todos los registros)
     * 4. Ejecutar y obtener ResultSet
     * 5. Recorrer TODAS las filas con while
     * 6. Por cada fila: crear User y agregarlo a la lista
     * 7. Devolver lista completa
     * 8. Cerrar recursos
     */
    @Override
    public List<User> findAll() throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // PASO 1: Crear lista vacía para almacenar los usuarios
        // ArrayList es la implementación más usada de List
        List<User> users = new ArrayList<>();

        try {
            // PASO 2: Obtener conexión
            conn = DatabaseConfig.getConnection();

            // PASO 3: Preparar SELECT sin WHERE
            // Sin WHERE trae TODOS los registros de la tabla
            String sql = "SELECT id, name, email, edad FROM users";
            pstmt = conn.prepareStatement(sql);

            // PASO 4: Ejecutar SELECT
            rs = pstmt.executeQuery();

            // PASO 5: Recorrer TODAS las filas con WHILE
            // Diferencia entre IF y WHILE:
            // - IF: ejecuta una sola vez si hay resultado (findById)
            // - WHILE: se repite mientras haya más filas (findAll)

            while (rs.next()) {
                // Este bloque se ejecuta UNA VEZ por cada fila devuelta

                // Extraer valores de la fila actual
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                int age = rs.getInt("edad");

                // Crear objeto User con esos valores
                User user = new User(id, name, email, age);

                // Agregar el usuario a la lista
                users.add(user);

                // rs.next() se ejecuta nuevamente al inicio del while
                // Si hay más filas → true → repite el bucle
                // Si NO hay más filas → false → sale del bucle
            }

            // Al terminar el while:
            // - Si había 5 usuarios en la BD → users.size() == 5
            // - Si NO había usuarios → users.size() == 0 (lista vacía)

            // PASO 7: Devolver la lista completa
            return users;

        } finally {
            // PASO 8: Cerrar recursos en orden inverso

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

    /**
     * update() - ACTUALIZAR un usuario existente (UPDATE del CRUD)
     *
     * SQL: UPDATE users SET name = ?, email = ?, age = ? WHERE id = ?
     *
     * IMPORTANTE:
     * - El WHERE es CRÍTICO: sin él actualizaría TODOS los registros
     * - Solo actualiza campos modificables (name, email, age)
     * - El ID NO se modifica (es inmutable, identifica el registro)
     *
     * FLUJO:
     * 1. Obtener conexión
     * 2. Preparar UPDATE con SET y WHERE
     * 3. Asignar valores a TODOS los placeholders (incluyendo el ID del WHERE)
     * 4. Ejecutar
     * 5. Verificar si actualizó algo
     * 6. Cerrar recursos
     */
    @Override
    public boolean update(User user) throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // PASO 1: Obtener conexión
            conn = DatabaseConfig.getConnection();

            // PASO 2: Preparar UPDATE
            // Estructura: UPDATE tabla SET campo1 = ?, campo2 = ? WHERE condicion = ?
            // SET: especifica QUÉ campos modificar
            // WHERE: especifica CUÁL registro modificar (por ID)
            String sql = "UPDATE users SET name = ?, email = ?, edad = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            // PASO 3: Asignar valores a los placeholders
            // ORDEN CRÍTICO: debe coincidir exactamente con el orden de los ? en el SQL

            pstmt.setString(1, user.getName());    // Primer ? → name (del SET)
            pstmt.setString(2, user.getEmail());   // Segundo ? → email (del SET)
            pstmt.setInt(3, user.getAge());        // Tercer ? → age (del SET)
            pstmt.setInt(4, user.getId());         // Cuarto ? → id (del WHERE)

            // Después de esto, la consulta quedaría como:
            // UPDATE users SET name = 'Juan Updated', email = 'nuevo@email.com', age = 26 WHERE id = 1

            // PASO 4: Ejecutar el UPDATE
            // executeUpdate() devuelve el número de filas modificadas
            int rowsAffected = pstmt.executeUpdate();

            // PASO 5: Verificar resultado
            // rowsAffected > 0 → Se actualizó al menos 1 registro → true
            // rowsAffected == 0 → El ID no existe, no se actualizó nada → false
            return rowsAffected > 0;

        } finally {
            // PASO 6: Cerrar recursos

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

    /**
     * delete() - ELIMINAR un usuario (DELETE del CRUD)
     *
     * SQL: DELETE FROM users WHERE id = ?
     *
     * ADVERTENCIA:
     * - Esta operación es IRREVERSIBLE
     * - El registro se elimina PERMANENTEMENTE de la base de datos
     * - El WHERE es CRÍTICO: sin él eliminaría TODOS los registros
     *
     * FLUJO:
     * 1. Obtener conexión
     * 2. Preparar DELETE con WHERE
     * 3. Asignar ID al placeholder
     * 4. Ejecutar
     * 5. Verificar si eliminó algo
     * 6. Cerrar recursos
     */
    @Override
    public boolean delete(int id) throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // PASO 1: Obtener conexión
            conn = DatabaseConfig.getConnection();

            // PASO 2: Preparar DELETE
            // WHERE es OBLIGATORIO: especifica CUÁL registro eliminar
            // ⚠️ PELIGRO: DELETE sin WHERE eliminaría TODA la tabla
            String sql = "DELETE FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            // PASO 3: Asignar el ID al placeholder
            pstmt.setInt(1, id);

            // La consulta quedaría como: DELETE FROM users WHERE id = 5

            // PASO 4: Ejecutar el DELETE
            // executeUpdate() devuelve el número de filas eliminadas
            int rowsAffected = pstmt.executeUpdate();

            // PASO 5: Verificar resultado
            // rowsAffected > 0 → Se eliminó el registro → true
            // rowsAffected == 0 → El ID no existe, no se eliminó nada → false
            return rowsAffected > 0;

        } finally {
            // PASO 6: Cerrar recursos

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
// CONCEPTOS CLAVE DE JDBC - RESUMEN EDUCATIVO
// ═══════════════════════════════════════════════════════════════════════════════

// 1. COMPONENTES PRINCIPALES:
//    - Connection: Canal de comunicación con la BD
//    - PreparedStatement: Consulta SQL precompilada y parametrizada
//    - ResultSet: Cursor sobre los resultados de un SELECT

// 2. PreparedStatement vs Statement:
//    ✓ PreparedStatement (RECOMENDADO):
//      - SEGURO: Previene SQL Injection
//      - EFICIENTE: Precompilado por el servidor
//      - CÓMODO: Escapa caracteres automáticamente
//    ✗ Statement:
//      - INSEGURO: Vulnerable a SQL Injection
//      - MENOS EFICIENTE: No precompila
//      - Solo para casos MUY específicos

// 3. executeQuery() vs executeUpdate():
//    - executeQuery(): SOLO para SELECT → devuelve ResultSet
//    - executeUpdate(): Para INSERT/UPDATE/DELETE → devuelve int (filas afectadas)

// 4. ResultSet - Cómo funciona:
//    - Es un CURSOR que apunta a las filas devueltas
//    - Inicialmente apunta ANTES de la primera fila
//    - next(): Avanza a la siguiente fila
//      · Devuelve true si hay fila
//      · Devuelve false si no hay más filas
//    - getXxx(): Extrae valores de la fila actual
//      · getInt("columna") → obtiene un entero
//      · getString("columna") → obtiene un String
//      · Puedes usar índice (1, 2, 3...) o nombre

// 5. Patrón de uso de ResultSet:
//    if (rs.next()) { ... }        // Para 1 resultado esperado (findById)
//    while (rs.next()) { ... }     // Para múltiples resultados (findAll)

// 6. Bloque FINALLY - ¿Qué es y por qué es importante?
//
//    ESTRUCTURA TRY-CATCH-FINALLY:
//    try {
//        // Código que puede lanzar excepciones
//    } catch (Exception e) {
//        // Manejo de la excepción (opcional)
//    } finally {
//        // Código que SIEMPRE se ejecuta
//    }
//
//    ¿CUÁNDO SE EJECUTA FINALLY?
//    - SIEMPRE, sin importar qué pase en el try
//    - Se ejecuta incluso si hay return en el try
//    - Se ejecuta incluso si hay una excepción no capturada
//    - Solo NO se ejecuta si el programa se cierra abruptamente (System.exit(), apagón, etc.)
//
//    EJEMPLOS:
//
//    Caso 1: TODO sale bien
//    try {
//        conn = getConnection();        // ✓ Funciona
//        pstmt = ...                    // ✓ Funciona
//        return true;                   // Se ejecuta el return
//    } finally {
//        cerrar recursos                // ✓ Se ejecuta DESPUÉS del return
//    }
//
//    Caso 2: Hay una excepción
//    try {
//        conn = getConnection();        // ✓ Funciona
//        pstmt = ...                    // ✗ Lanza SQLException
//        return true;                   // NO se ejecuta
//    } finally {
//        cerrar recursos                // ✓ Se ejecuta ANTES de propagar la excepción
//    }
//
//    ¿POR QUÉ USAMOS FINALLY PARA CERRAR RECURSOS?
//    - Garantiza que los recursos se liberen SIEMPRE
//    - Evita memory leaks (pérdida de memoria)
//    - Evita quedarse sin conexiones disponibles
//    - MySQL tiene un límite de conexiones simultáneas (ej: 151 por defecto)
//
//    ALTERNATIVA MODERNA: try-with-resources (Java 7+)
//    try (Connection conn = getConnection();
//         PreparedStatement pstmt = conn.prepareStatement(sql)) {
//        // Código...
//    } // Se cierran automáticamente, no necesita finally
//
//    En este proyecto usamos finally explícito con fines EDUCATIVOS
//    para entender bien el concepto.
//
// 7. Cerrar recursos - ORDEN CORRECTO:
//    - SIEMPRE cerrar en bloque finally
//    - ORDEN: ResultSet → PreparedStatement → Connection (inverso a su creación)
//    - Si no se cierran → memory leaks (pérdida de memoria)

// 8. SQL Injection - Ejemplo de ataque que PreparedStatement previene:
//    // VULNERABLE (Statement):
//    String sql = "SELECT * FROM users WHERE name = '" + name + "'";
//    // Si name = "'; DROP TABLE users; --" → ¡DESASTRE!
//
//    // SEGURO (PreparedStatement):
//    String sql = "SELECT * FROM users WHERE name = ?";
//    pstmt.setString(1, name);
//    // El valor se escapa automáticamente, no puede inyectar código SQL

// 9. Buenas prácticas implementadas en esta clase:
//    ✓ Usar PreparedStatement siempre
//    ✓ Cerrar recursos en finally
//    ✓ Usar nombres de columnas en rs.getXxx() (más legible que índices)
//    ✓ Validar rowsAffected para saber si la operación tuvo éxito
//    ✓ Separar responsabilidades (DAO solo maneja datos, no lógica de negocio)
//    ✓ Código bien documentado y explicado
