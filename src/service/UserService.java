package src.service;

// IMPORTACIONES

import src.dao.UserDAO;
// UserDAO: Interfaz que define las operaciones CRUD

import src.dao.UserDAOImpl;
// UserDAOImpl: Implementación concreta del DAO con JDBC

import src.model.User;
// User: Clase modelo que representa un usuario

import java.sql.SQLException;
// SQLException: Excepción de base de datos

import java.util.List;
// List: Colección ordenada de usuarios

/**
 * UserService - Capa de Lógica de Negocio (Service Layer)
 *
 * PROPÓSITO:
 * - Actuar como intermediario entre la interfaz de usuario (UI) y la capa de datos (DAO)
 * - Contener la lógica de negocio de la aplicación
 * - Validar datos antes de enviarlos al DAO
 * - Manejar excepciones y errores de forma amigable
 *
 * PATRÓN DE ARQUITECTURA EN CAPAS:
 *
 *   ┌─────────────────────┐
 *   │   UI (Main.java)    │  ← Interfaz gráfica con JOptionPane
 *   │  Presentación       │
 *   └──────────┬──────────┘
 *              │
 *              ▼
 *   ┌─────────────────────┐
 *   │   UserService       │  ← ESTA CLASE (Lógica de negocio)
 *   │  Capa de Servicio   │
 *   └──────────┬──────────┘
 *              │
 *              ▼
 *   ┌─────────────────────┐
 *   │   UserDAO/Impl      │  ← Acceso a datos con JDBC
 *   │  Capa de Datos      │
 *   └──────────┬──────────┘
 *              │
 *              ▼
 *   ┌─────────────────────┐
 *   │   MySQL Database    │  ← Base de datos
 *   └─────────────────────┘
 *
 * VENTAJAS de esta arquitectura:
 * 1. SEPARACIÓN DE RESPONSABILIDADES:
 *    - UI solo se preocupa de mostrar datos
 *    - Service maneja lógica de negocio y validaciones
 *    - DAO solo maneja acceso a datos
 *
 * 2. FACILITA TESTING:
 *    - Podemos testear la lógica de negocio sin UI ni BD
 *
 * 3. REUTILIZACIÓN:
 *    - El Service puede ser usado por diferentes UIs (Swing, web, consola, etc.)
 *
 * 4. MANTENIBILIDAD:
 *    - Si cambia la BD, solo modificamos el DAO
 *    - Si cambia la lógica de negocio, solo modificamos el Service
 *    - Si cambia la UI, solo modificamos Main
 *
 * EJEMPLOS DE LÓGICA DE NEGOCIO:
 * - Validar que el email tenga formato correcto
 * - Validar que la edad esté en un rango válido
 * - Hashear contraseñas antes de guardarlas
 * - Enviar emails de confirmación
 * - Registrar logs de auditoría
 * - Aplicar reglas de negocio complejas
 *
 * En este proyecto básico, el Service es simple (principalmente delega al DAO),
 * pero en aplicaciones reales aquí iría toda la lógica de negocio.
 */
public class UserService {

    // ATRIBUTO: Instancia del DAO para acceder a la base de datos
    // Usamos la interfaz UserDAO (no UserDAOImpl) para mayor flexibilidad
    // Esto permite cambiar la implementación sin modificar este código
    private UserDAO userDAO;

    /**
     * Constructor - Inicializa el Service con una instancia del DAO
     *
     * Al crear un UserService, automáticamente crea un UserDAOImpl
     * para poder acceder a la base de datos.
     */
    public UserService() {
        // Creamos una instancia de la implementación concreta
        this.userDAO = new UserDAOImpl();
    }

    /**
     * createUser() - Crear un nuevo usuario
     *
     * FLUJO:
     * 1. Validar datos del usuario (lógica de negocio)
     * 2. Si es válido, delegar al DAO para guardarlo
     * 3. Retornar resultado
     *
     * @param user - Usuario a crear
     * @return boolean - true si se creó exitosamente
     * @throws SQLException - Si hay error en la BD
     */
    public boolean createUser(User user) throws SQLException {
        // PASO 1: Validaciones de negocio (opcional, pero recomendado)

        // Validar que el nombre no esté vacío
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        // Validar que el email no esté vacío
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        // Validar formato de email (simple)
        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("El email debe tener un formato válido");
        }

        // Validar que la edad sea positiva
        if (user.getAge() <= 0) {
            throw new IllegalArgumentException("La edad debe ser mayor a 0");
        }

        // Validar rango de edad razonable
        if (user.getAge() > 150) {
            throw new IllegalArgumentException("La edad no puede ser mayor a 150");
        }

        // PASO 2: Si pasó todas las validaciones, delegar al DAO
        return userDAO.save(user);
    }

    /**
     * getUserById() - Obtener un usuario por su ID
     *
     * @param id - ID del usuario a buscar
     * @return User - Usuario encontrado, o null si no existe
     * @throws SQLException - Si hay error en la BD
     */
    public User getUserById(int id) throws SQLException {
        // Validación: el ID debe ser positivo
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }

        // Delegar al DAO
        return userDAO.findById(id);
    }

    /**
     * getAllUsers() - Obtener todos los usuarios
     *
     * @return List<User> - Lista con todos los usuarios
     * @throws SQLException - Si hay error en la BD
     */
    public List<User> getAllUsers() throws SQLException {
        // En este caso no hay validaciones necesarias
        // Simplemente delegamos al DAO
        return userDAO.findAll();
    }

    /**
     * updateUser() - Actualizar un usuario existente
     *
     * @param user - Usuario con los nuevos datos (debe tener ID)
     * @return boolean - true si se actualizó exitosamente
     * @throws SQLException - Si hay error en la BD
     */
    public boolean updateUser(User user) throws SQLException {
        // Validar que el ID exista
        if (user.getId() <= 0) {
            throw new IllegalArgumentException("El ID del usuario es inválido");
        }

        // Validar los mismos campos que en createUser
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("El email debe tener un formato válido");
        }

        if (user.getAge() <= 0 || user.getAge() > 150) {
            throw new IllegalArgumentException("La edad debe estar entre 1 y 150");
        }

        // Delegar al DAO
        return userDAO.update(user);
    }

    /**
     * deleteUser() - Eliminar un usuario
     *
     * @param id - ID del usuario a eliminar
     * @return boolean - true si se eliminó exitosamente
     * @throws SQLException - Si hay error en la BD
     */
    public boolean deleteUser(int id) throws SQLException {
        // Validar que el ID sea positivo
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }

        // Aquí podríamos agregar lógica adicional, por ejemplo:
        // - Verificar que el usuario exista antes de eliminarlo
        // - Registrar en un log quién eliminó al usuario
        // - Hacer un "soft delete" en lugar de eliminar físicamente
        // - Verificar permisos del usuario que está eliminando

        // Delegar al DAO
        return userDAO.delete(id);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// NOTAS ADICIONALES SOBRE LA CAPA DE SERVICIO
// ═══════════════════════════════════════════════════════════════════════════════

// 1. ¿Por qué usar IllegalArgumentException?
//    - Es una RuntimeException (no checked)
//    - Se usa para validaciones de parámetros
//    - Indica que el llamador pasó datos inválidos
//    - No necesita declararse en throws

// 2. ¿Por qué no capturamos SQLException aquí?
//    - La dejamos propagarse hacia arriba (al Main)
//    - El Main puede decidir cómo mostrar el error al usuario
//    - Si la capturáramos aquí, el Main no sabría que hubo un error

// 3. Diferencia entre excepciones:
//    - IllegalArgumentException: Error en los DATOS (validación)
//    - SQLException: Error en la BASE DE DATOS (conexión, query, etc.)

// 4. En aplicaciones reales, el Service puede:
//    - Manejar transacciones (múltiples operaciones atómicas)
//    - Implementar caché para mejorar rendimiento
//    - Aplicar seguridad y control de acceso
//    - Registrar logs y auditoría
//    - Enviar notificaciones (emails, SMS, etc.)
//    - Integrar con otros servicios externos (APIs, etc.)

// 5. Patrón de inyección de dependencias (avanzado):
//    En lugar de crear el DAO en el constructor:
//    public UserService(UserDAO userDAO) {
//        this.userDAO = userDAO;
//    }
//    Esto facilita testing (podemos inyectar un mock)
//    Frameworks como Spring hacen esto automáticamente
