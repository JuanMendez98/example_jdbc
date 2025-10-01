package src.model;

/**
 * User - Clase modelo (POJO - Plain Old Java Object)
 *
 * PROPÓSITO:
 * - Representar un usuario del sistema
 * - Mapear los datos de la tabla 'users' de MySQL a un objeto Java
 * - Encapsular los atributos con getters y setters
 *
 * ¿QUÉ ES UN POJO?
 * - Plain Old Java Object (Objeto Java Simple)
 * - Clase que solo contiene atributos, constructores, getters y setters
 * - NO contiene lógica de negocio ni acceso a datos
 * - Fácil de entender, mantener y testear
 *
 * MAPEO CON LA BASE DE DATOS:
 * Tabla 'users' en MySQL:
 * ┌────────────────────────────────────────┐
 * │ id (INT, AUTO_INCREMENT, PRIMARY KEY) │
 * │ name (VARCHAR(100), NOT NULL)         │
 * │ email (VARCHAR(100), NOT NULL, UNIQUE)│
 * │ age (INT, NOT NULL)                    │
 * └────────────────────────────────────────┘
 *         ↓ mapea a ↓
 * Clase User en Java:
 * - int id
 * - String name
 * - String email
 * - int age
 */
public class User {

    // ATRIBUTOS - Corresponden a las columnas de la tabla 'users'
    // Son private para aplicar encapsulamiento (solo accesibles mediante getters/setters)

    private int id;
    // id: Identificador único del usuario (generado automáticamente por MySQL)

    private String name;
    // name: Nombre completo del usuario

    private String email;
    // email: Correo electrónico (debe ser único en la BD)

    private int age;
    // age: Edad del usuario en años

    // ═══════════════════════════════════════════════════════════════════════════════
    // CONSTRUCTORES
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * Constructor vacío (sin parámetros)
     *
     * CUÁNDO SE USA:
     * - Cuando se necesita crear un objeto User vacío y luego llenar sus datos con setters
     * - Es requerido por algunos frameworks (Hibernate, Jackson, etc.)
     *
     * EJEMPLO:
     * User user = new User();
     * user.setName("Juan");
     * user.setEmail("juan@email.com");
     * user.setAge(25);
     */
    public User() {
        // Constructor vacío - no inicializa nada
    }

    /**
     * Constructor para CREAR un nuevo usuario (sin ID)
     *
     * CUÁNDO SE USA:
     * - Cuando vas a insertar un nuevo usuario en la BD
     * - El ID NO se incluye porque MySQL lo genera automáticamente (AUTO_INCREMENT)
     *
     * @param name - Nombre del usuario
     * @param email - Email del usuario
     * @param age - Edad del usuario
     *
     * EJEMPLO:
     * User nuevoUser = new User("Juan", "juan@email.com", 25);
     * userDAO.save(nuevoUser); // MySQL genera el ID automáticamente
     */
    public User(String name, String email, int age) {
        // Usamos setters para asignar valores
        // NOTA: También podrías usar asignación directa (this.name = name)
        setName(name);
        setEmail(email);
        setAge(age);
    }

    /**
     * Constructor completo (con ID)
     *
     * CUÁNDO SE USA:
     * - Cuando obtienes un usuario de la BD (ya tiene ID asignado)
     * - Para actualizar un usuario existente
     * - Al mapear ResultSet a objeto User
     *
     * @param id - ID del usuario (ya existente en la BD)
     * @param name - Nombre del usuario
     * @param email - Email del usuario
     * @param age - Edad del usuario
     *
     * EJEMPLO:
     * // Al hacer SELECT, creamos User con todos los datos:
     * User user = new User(1, "Juan", "juan@email.com", 25);
     */
    public User(int id, String name, String email, int age) {
        setId(id);
        setName(name);
        setEmail(email);
        setAge(age);
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // GETTERS - Métodos para LEER (obtener) el valor de los atributos
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * getId() - Obtiene el ID del usuario
     * @return int - ID del usuario
     */
    public int getId() {
        return id;
    }

    /**
     * getName() - Obtiene el nombre del usuario
     * @return String - Nombre del usuario
     */
    public String getName() {
        return name;
    }

    /**
     * getEmail() - Obtiene el email del usuario
     * @return String - Email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * getAge() - Obtiene la edad del usuario
     * @return int - Edad del usuario
     */
    public int getAge() {
        return age;
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // SETTERS - Métodos para ESCRIBIR (modificar) el valor de los atributos
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * setId() - Establece el ID del usuario
     * @param id - Nuevo ID
     */
    public void setId(int id) {
        this.id = id;
        // 'this.id' se refiere al atributo de la clase
        // 'id' se refiere al parámetro del método
    }

    /**
     * setName() - Establece el nombre del usuario
     * @param name - Nuevo nombre
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setEmail() - Establece el email del usuario
     * @param email - Nuevo email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * setAge() - Establece la edad del usuario
     * @param age - Nueva edad
     */
    public void setAge(int age) {
        this.age = age;
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // MÉTODOS ESPECIALES
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * toString() - Convierte el objeto User a una representación en String
     *
     * PROPÓSITO:
     * - Facilitar la impresión del objeto (útil para debugging)
     * - Se llama automáticamente cuando haces: System.out.println(user)
     *
     * @Override indica que estamos sobrescribiendo el método toString() de Object
     *
     * @return String - Representación textual del usuario
     *
     * EJEMPLO:
     * User user = new User(1, "Juan", "juan@email.com", 25);
     * System.out.println(user);
     * // Imprime: User{id=1, name='Juan', email='juan@email.com', age=25}
     */
    @Override
    public String toString() {
        // Las comillas simples alrededor de name y email ayudan a visualizar
        // que son Strings (diferenciándolos de números)
        return "User{id=" + id + ", name='" + name + "', email='" + email + "', age=" + age + "}";
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONCEPTOS IMPORTANTES
// ═══════════════════════════════════════════════════════════════════════════════

// 1. ENCAPSULAMIENTO:
//    - Atributos privados (private)
//    - Acceso controlado mediante getters/setters (public)
//    - Protege la integridad de los datos

// 2. ¿POR QUÉ USAR GETTERS Y SETTERS?
//    - Control: Puedes agregar validaciones en los setters
//    - Flexibilidad: Puedes cambiar la implementación interna sin afectar el código externo
//    - Estándar JavaBeans: Requerido por muchos frameworks

// 3. this.atributo vs atributo:
//    - 'this' se refiere al objeto actual
//    - Necesario cuando el parámetro tiene el mismo nombre que el atributo
//    Ejemplo:
//    public void setName(String name) {
//        this.name = name;  // this.name = atributo, name = parámetro
//    }

// 4. @Override:
//    - Anotación que indica que estamos sobrescribiendo un método de la clase padre
//    - En este caso, toString() de la clase Object
//    - El compilador verifica que realmente estés sobrescribiendo (evita errores)
