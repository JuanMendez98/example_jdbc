package src.config;

// IMPORTACIONES: Traemos las clases que necesitamos de Java

import java.io.FileInputStream;
// FileInputStream: Para leer archivos del disco como flujo de bytes

import java.io.IOException;
// IOException: Excepción que se lanza cuando hay errores leyendo/escribiendo archivos

import java.sql.Connection;
// Connection: Representa una conexión activa a la base de datos
// Es como un "puente" entre tu aplicación Java y MySQL

import java.sql.DriverManager;
// DriverManager: Gestor de JDBC que crea y administra conexiones a bases de datos
// Se encarga de encontrar el driver correcto (MySQL en este caso)

import java.sql.SQLException;
// SQLException: Excepción que se lanza cuando hay errores relacionados con la base de datos
// (ej: credenciales incorrectas, servidor caído, query mal formado, etc.)

import java.util.Properties;
// Properties: Clase de Java para leer archivos .properties (formato clave=valor)
// Funciona como un Map/Dictionary que lee archivos de configuración

/**
 * DatabaseConfig - Clase de configuración para la conexión a MySQL
 *
 * PROPÓSITO:
 * - Centralizar toda la lógica de conexión a la base de datos
 * - Leer credenciales desde un archivo externo (database.properties)
 * - Proveer métodos estáticos para obtener/cerrar conexiones
 *
 * VENTAJAS de usar archivo .properties:
 * - Separar código de configuración (buena práctica)
 * - Evitar subir contraseñas al repositorio Git
 * - Fácil cambiar entre ambientes (desarrollo/producción)
 */
public class DatabaseConfig {

    // ATRIBUTOS ESTÁTICOS: Se cargan una vez y se comparten en toda la aplicación

    private static String URL;
    // URL: Dirección completa de la base de datos
    // Formato: jdbc:mysql://IP:PUERTO/NOMBRE_BD
    // Ejemplo: jdbc:mysql://168.119.183.3:3307/latte_camilo

    private static String USER;
    // USER: Nombre de usuario de MySQL

    private static String PASSWORD;
    // PASSWORD: Contraseña del usuario de MySQL

    // ¿Por qué son 'static'?
    // - Se comparten entre todas las instancias de la clase
    // - Podemos acceder sin crear un objeto: DatabaseConfig.getConnection()

    // ¿Por qué NO son 'final'?
    // - Porque sus valores se asignan en el bloque static (no en la declaración)

    /**
     * BLOQUE ESTÁTICO (static initializer block)
     *
     * ¿QUÉ ES?
     * - Un bloque de código especial que se ejecuta automáticamente
     * - Se ejecuta UNA SOLA VEZ cuando Java carga la clase en memoria
     *
     * ¿CUÁNDO SE EJECUTA?
     * - La primera vez que se hace referencia a DatabaseConfig
     * - Antes de cualquier método estático o creación de objetos
     * - Ejemplo: al llamar DatabaseConfig.getConnection() por primera vez
     *
     * ¿POR QUÉ LO USAMOS?
     * - Para cargar la configuración antes de que se use la clase
     * - Si falla, la aplicación se detiene inmediatamente (fail-fast)
     */
    static {
        // 1. Creamos un objeto Properties (funciona como un diccionario clave=valor)
        Properties properties = new Properties();

        try {
            // 2. Abrimos el archivo database.properties para lectura
            // FileInputStream lee el archivo como flujo de bytes
            // La ruta es relativa desde donde ejecutas el programa
            FileInputStream file = new FileInputStream("src/config/database.properties");

            // 3. El método load() lee TODO el archivo y parsea el formato clave=valor
            // Después de esta línea, 'properties' contiene todos los pares clave=valor del archivo
            properties.load(file);

            // 4. Extraemos los valores del archivo usando las claves definidas
            // getProperty("clave") busca la clave en el archivo y devuelve su valor

            URL = properties.getProperty("DB_URL");
            // Busca "DB_URL" en el archivo → Ejemplo: jdbc:mysql://168.119.183.3:3307/latte_camilo

            USER = properties.getProperty("DB_USER");
            // Busca "DB_USER" en el archivo → Ejemplo: root

            PASSWORD = properties.getProperty("DB_PASSWORD");
            // Busca "DB_PASSWORD" en el archivo → Ejemplo: tu_contraseña

            // 5. Cerramos el archivo para liberar recursos del sistema operativo
            // Es una buena práctica cerrar archivos después de usarlos
            file.close();

        } catch (IOException e) {
            // Si algo sale mal (archivo no existe, sin permisos, ruta incorrecta, etc.)
            // Lanzamos RuntimeException para detener la aplicación
            // ¿Por qué? Porque sin configuración de BD, la app no puede funcionar
            throw new RuntimeException("Error al cargar database.properties: " + e.getMessage());
        }
    }

    /**
     * getConnection() - Crea y devuelve una conexión activa a MySQL
     *
     * PROPÓSITO:
     * - Establecer una conexión nueva con la base de datos
     * - Cargar el driver JDBC de MySQL
     * - Devolver el objeto Connection para ejecutar queries
     *
     * USO:
     * Connection conn = DatabaseConfig.getConnection();
     * // Aquí puedes ejecutar queries, inserts, updates, etc.
     * DatabaseConfig.closeConnection(conn);
     *
     * @return Connection - Objeto que representa la conexión activa a MySQL
     * @throws SQLException - Si hay error al conectar (credenciales incorrectas, BD caída, etc.)
     */
    public static Connection getConnection() throws SQLException {
        try {
            // 1. Cargamos el driver de MySQL en memoria
            // Class.forName() busca y carga la clase del driver JDBC
            // En JDBC 4.0+ esto es opcional (se carga automáticamente)
            // Lo dejamos por compatibilidad y claridad
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ¿Qué hace el driver?
            // - Traduce comandos Java a protocolo MySQL
            // - Maneja la comunicación TCP/IP con el servidor MySQL

            // 2. DriverManager.getConnection() establece la conexión
            // Parámetros: (URL, usuario, contraseña)
            // Internamente:
            //   - Abre socket TCP/IP al servidor MySQL
            //   - Autentica con usuario/contraseña
            //   - Devuelve objeto Connection si todo OK
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            // Si no encuentra la clase "com.mysql.cj.jdbc.Driver"
            // Significa que el archivo mysql-connector-j-X.X.X.jar NO está en el classpath
            // Solución: Descargar el driver y agregarlo al classpath al compilar/ejecutar
            throw new SQLException("Driver MySQL no encontrado", e);
        }
    }

    /**
     * closeConnection() - Cierra una conexión a la base de datos de forma segura
     *
     * PROPÓSITO:
     * - Liberar recursos del sistema (memoria, sockets, conexiones del pool)
     * - Evitar "connection leaks" (conexiones abiertas sin cerrar)
     *
     * ¿POR QUÉ ES IMPORTANTE?
     * - MySQL tiene un límite de conexiones simultáneas
     * - Cada conexión consume memoria en el servidor
     * - Dejar conexiones abiertas puede saturar el servidor
     *
     * BUENA PRÁCTICA:
     * Siempre cerrar conexiones en un bloque finally o try-with-resources
     *
     * @param conn - La conexión a cerrar (puede ser null)
     */
    public static void closeConnection(Connection conn) {
        // Verificamos que la conexión no sea null antes de intentar cerrarla
        // Si es null, no hay nada que cerrar
        if (conn != null) {
            try {
                // conn.close() cierra la conexión:
                // - Libera el socket TCP/IP
                // - Notifica al servidor MySQL que terminó la sesión
                // - Libera memoria asociada
                conn.close();

            } catch (SQLException e) {
                // Si falla al cerrar, mostramos el error pero NO detenemos el programa
                // System.err imprime en la consola de errores (suele mostrarse en rojo)
                // No es crítico si falla el cierre (el garbage collector eventualmente lo limpiará)
                System.err.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
    }
}
