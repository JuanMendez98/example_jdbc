package src.dao;

// IMPORTACIONES

import java.sql.SQLException;
// SQLException: Excepción que se lanza cuando hay errores en operaciones de base de datos
// (ej: credenciales incorrectas, query mal formado, constraint violations, etc.)

import java.util.List;
// List: Interfaz de Java para representar colecciones ordenadas de elementos
// La usamos para devolver múltiples usuarios en findAll()
// Es una interfaz, las implementaciones comunes son ArrayList, LinkedList, etc.

import src.model.User;
// User: La clase modelo (POJO) que representa un usuario
// Contiene los atributos: id, name, email, age

/**
 * UserDAO (Data Access Object) - Interfaz que define las operaciones CRUD para Users
 *
 * ¿QUÉ ES UN DAO?
 * - DAO = Data Access Object (Objeto de Acceso a Datos)
 * - Es un PATRÓN DE DISEÑO que separa la lógica de negocio de la lógica de acceso a datos
 * - Encapsula todas las operaciones de base de datos en un solo lugar
 *
 * ¿POR QUÉ USAR UNA INTERFAZ?
 * 1. SEPARACIÓN DE RESPONSABILIDADES:
 *    - La interfaz define QUÉ operaciones se pueden hacer (el contrato)
 *    - La implementación define CÓMO se hacen (los detalles técnicos)
 *
 * 2. FLEXIBILIDAD:
 *    - Podemos cambiar la implementación sin afectar el código que la usa
 *    - Ejemplos: UserDAOImpl (JDBC), UserDAOHibernate, UserDAOMongoDB, etc.
 *
 * 3. FACILITA TESTING:
 *    - Podemos crear implementaciones falsas (mocks) para pruebas unitarias
 *    - No necesitamos una BD real para testear la lógica de negocio
 *
 * 4. CUMPLE PRINCIPIOS SOLID:
 *    - "D" de Dependency Inversion: dependemos de abstracciones, no de implementaciones
 *
 * OPERACIONES CRUD (Create, Read, Update, Delete):
 * - CREATE: save() → INSERT INTO users
 * - READ: findById(), findAll() → SELECT FROM users
 * - UPDATE: update() → UPDATE users SET ...
 * - DELETE: delete() → DELETE FROM users
 */
public interface UserDAO {

    /**
     * save() - Guarda un nuevo usuario en la base de datos
     *
     * PROPÓSITO:
     * - Insertar un nuevo registro en la tabla 'users'
     * - El ID se genera automáticamente por la BD (AUTO_INCREMENT)
     *
     * SQL EQUIVALENTE:
     * INSERT INTO users (name, email, age) VALUES (?, ?, ?)
     *
     * FLUJO:
     * 1. Recibe objeto User (sin ID o con ID=0)
     * 2. Ejecuta INSERT con los datos del usuario
     * 3. MySQL genera el ID automáticamente
     * 4. Devuelve true si se guardó correctamente
     *
     * @param user - Objeto User con name, email y age (el ID será ignorado)
     * @return boolean - true si se guardó exitosamente, false si falló
     * @throws SQLException - Si hay error de BD (ej: email duplicado por constraint UNIQUE)
     *
     * EJEMPLO DE USO:
     * User newUser = new User("Juan", "juan@email.com", 25);
     * boolean success = userDAO.save(newUser);
     * if (success) {
     *     System.out.println("Usuario guardado exitosamente");
     * }
     */
    boolean save(User user) throws SQLException;

    /**
     * findById() - Busca y devuelve un usuario por su ID
     *
     * PROPÓSITO:
     * - Consultar un usuario específico usando su ID único
     * - Es la forma más eficiente de buscar (el ID es PRIMARY KEY)
     *
     * SQL EQUIVALENTE:
     * SELECT id, name, email, age FROM users WHERE id = ?
     *
     * FLUJO:
     * 1. Recibe el ID a buscar
     * 2. Ejecuta SELECT con WHERE id = ?
     * 3. Si encuentra el registro, crea un objeto User con los datos
     * 4. Si NO encuentra, devuelve null
     *
     * @param id - El ID del usuario a buscar (debe ser > 0)
     * @return User - El usuario encontrado, o null si no existe ese ID
     * @throws SQLException - Si hay error ejecutando la consulta
     *
     * EJEMPLO DE USO:
     * User user = userDAO.findById(1);
     * if (user != null) {
     *     System.out.println("Usuario encontrado: " + user.getName());
     * } else {
     *     System.out.println("Usuario no existe");
     * }
     */
    User findById(int id) throws SQLException;

    /**
     * findAll() - Obtiene todos los usuarios de la base de datos
     *
     * PROPÓSITO:
     * - Consultar TODOS los registros de la tabla 'users'
     * - Útil para listar usuarios, generar reportes, etc.
     *
     * SQL EQUIVALENTE:
     * SELECT id, name, email, age FROM users
     *
     * FLUJO:
     * 1. Ejecuta SELECT sin WHERE (todos los registros)
     * 2. Por cada registro, crea un objeto User
     * 3. Agrega cada User a una lista
     * 4. Devuelve la lista completa (vacía si no hay usuarios)
     *
     * @return List<User> - Lista con todos los usuarios (nunca null, puede ser vacía)
     * @throws SQLException - Si hay error ejecutando la consulta
     *
     * EJEMPLO DE USO:
     * List<User> users = userDAO.findAll();
     * System.out.println("Total usuarios: " + users.size());
     * for (User user : users) {
     *     System.out.println(user);
     * }
     */
    List<User> findAll() throws SQLException;

    /**
     * update() - Actualiza los datos de un usuario existente
     *
     * PROPÓSITO:
     * - Modificar los campos (name, email, age) de un usuario ya existente en la BD
     * - El ID NO se modifica (es inmutable, identifica el registro)
     *
     * SQL EQUIVALENTE:
     * UPDATE users SET name = ?, email = ?, age = ? WHERE id = ?
     *
     * FLUJO:
     * 1. Recibe objeto User con ID existente y nuevos valores
     * 2. Ejecuta UPDATE con WHERE id = user.getId()
     * 3. Si el ID existe, actualiza los campos y devuelve true
     * 4. Si el ID NO existe, no actualiza nada y devuelve false
     *
     * @param user - Objeto User con el ID existente y los nuevos datos
     * @return boolean - true si actualizó (ID existe), false si no (ID no existe)
     * @throws SQLException - Si hay error en la actualización (ej: email duplicado)
     *
     * EJEMPLO DE USO:
     * User user = userDAO.findById(1);
     * user.setName("Juan Actualizado");
     * user.setEmail("nuevo@email.com");
     * boolean success = userDAO.update(user);
     * if (success) {
     *     System.out.println("Usuario actualizado");
     * }
     */
    boolean update(User user) throws SQLException;

    /**
     * delete() - Elimina un usuario de la base de datos
     *
     * PROPÓSITO:
     * - Borrar permanentemente un registro de la tabla 'users'
     * - ATENCIÓN: Esta operación es IRREVERSIBLE
     *
     * SQL EQUIVALENTE:
     * DELETE FROM users WHERE id = ?
     *
     * FLUJO:
     * 1. Recibe el ID del usuario a eliminar
     * 2. Ejecuta DELETE con WHERE id = ?
     * 3. Si el ID existe, lo elimina y devuelve true
     * 4. Si el ID NO existe, no hace nada y devuelve false
     *
     * @param id - El ID del usuario a eliminar
     * @return boolean - true si eliminó (ID existía), false si no (ID no existía)
     * @throws SQLException - Si hay error en la eliminación
     *
     * EJEMPLO DE USO:
     * boolean success = userDAO.delete(1);
     * if (success) {
     *     System.out.println("Usuario eliminado");
     * } else {
     *     System.out.println("Usuario no existe");
     * }
     */
    boolean delete(int id) throws SQLException;

    // NOTA IMPORTANTE:
    // Esta es solo la INTERFAZ (el contrato/definición)
    // La implementación real con código JDBC estará en UserDAOImpl.java
    // Ahí veremos cómo se ejecutan los PreparedStatements, ResultSets, etc.
}
