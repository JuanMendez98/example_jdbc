package src;

// IMPORTACIONES

import src.model.User;
// User: Clase modelo que representa un usuario

import src.service.UserService;
// UserService: Capa de lógica de negocio

import javax.swing.JOptionPane;
// JOptionPane: Clase de Swing para mostrar diálogos/ventanas emergentes
// Métodos principales:
// - showInputDialog(): Pide datos al usuario
// - showMessageDialog(): Muestra mensajes informativos
// - showConfirmDialog(): Pide confirmación (Sí/No)
// - showOptionDialog(): Menú personalizado con botones

import java.sql.SQLException;
// SQLException: Para manejar errores de base de datos

import java.util.List;
// List: Para manejar la lista de usuarios

/**
 * Main - Interfaz gráfica del CRUD con JOptionPane
 *
 * PROPÓSITO:
 * - Punto de entrada de la aplicación (método main)
 * - Presentar menú interactivo al usuario con ventanas emergentes
 * - Capturar datos mediante formularios (JOptionPane.showInputDialog)
 * - Mostrar resultados y mensajes (JOptionPane.showMessageDialog)
 * - Manejar errores de forma amigable para el usuario
 *
 * VENTAJAS DE JOptionPane:
 * ✓ Fácil de usar (no requiere diseño de ventanas complejas)
 * ✓ Portable (funciona en cualquier sistema operativo)
 * ✓ Ideal para aplicaciones pequeñas y prototipos
 * ✓ No necesita instalar librerías adicionales (viene con Java)
 *
 * FLUJO DE LA APLICACIÓN:
 * 1. Mostrar menú principal
 * 2. Usuario selecciona opción
 * 3. Ejecutar operación correspondiente
 * 4. Mostrar resultado
 * 5. Volver al menú (hasta que seleccione Salir)
 */
public class Main {

    // Instancia del servicio para realizar operaciones CRUD
    // static porque lo usamos desde el método main (que es static)
    private static UserService userService = new UserService();

    /**
     * main() - Punto de entrada de la aplicación
     *
     * Este método se ejecuta cuando inicias el programa.
     * Contiene el bucle principal que muestra el menú repetidamente.
     */
    public static void main(String[] args) {
        // Mostrar mensaje de bienvenida
        JOptionPane.showMessageDialog(
                null,                           // parent (null = centrado en pantalla)
                "Bienvenido al Sistema de Gestión de Usuarios\n\nCRUD con JDBC + MySQL",
                "Sistema de Usuarios",          // título de la ventana
                JOptionPane.INFORMATION_MESSAGE // tipo de mensaje (ícono de información)
        );

        // Variable para controlar el bucle del menú
        boolean continuar = true;

        // BUCLE PRINCIPAL: se repite hasta que el usuario seleccione "Salir"
        while (continuar) {
            // Mostrar el menú y obtener la opción seleccionada
            String opcion = mostrarMenu();

            // Si el usuario cerró la ventana o canceló → salir
            if (opcion == null) {
                continuar = false;
                continue;
            }

            // Ejecutar la acción según la opción seleccionada
            switch (opcion) {
                case "1":
                    crearUsuario();
                    break;
                case "2":
                    listarUsuarios();
                    break;
                case "3":
                    buscarUsuario();
                    break;
                case "4":
                    actualizarUsuario();
                    break;
                case "5":
                    eliminarUsuario();
                    break;
                case "6":
                    continuar = false; // Salir del bucle
                    break;
                default:
                    // Opción inválida
                    JOptionPane.showMessageDialog(
                            null,
                            "Opción inválida. Por favor, selecciona una opción del 1 al 6.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
            }
        }

        // Mensaje de despedida
        JOptionPane.showMessageDialog(
                null,
                "¡Gracias por usar el sistema!\n\nHasta pronto.",
                "Adiós",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * mostrarMenu() - Muestra el menú principal y retorna la opción seleccionada
     *
     * Usa showInputDialog() para mostrar un campo de texto donde el usuario
     * puede escribir el número de la opción deseada.
     *
     * @return String - Opción seleccionada (1-6) o null si cancela
     */
    private static String mostrarMenu() {
        // Crear el texto del menú
        String menu = """
                ═══════════════════════════════════
                    MENÚ PRINCIPAL - CRUD USUARIOS
                ═══════════════════════════════════

                1. Crear Usuario
                2. Listar Usuarios
                3. Buscar Usuario por ID
                4. Actualizar Usuario
                5. Eliminar Usuario
                6. Salir

                ═══════════════════════════════════
                Selecciona una opción (1-6):
                """;

        // showInputDialog muestra una ventana con un campo de texto
        // Retorna el texto ingresado por el usuario, o null si cancela
        return JOptionPane.showInputDialog(
                null,
                menu,
                "Menú Principal",
                JOptionPane.QUESTION_MESSAGE
        );
    }

    /**
     * crearUsuario() - Formulario para crear un nuevo usuario
     *
     * FLUJO:
     * 1. Pedir nombre, email y edad mediante ventanas
     * 2. Validar que no se haya cancelado
     * 3. Crear objeto User
     * 4. Llamar al servicio para guardarlo
     * 5. Mostrar mensaje de éxito o error
     */
    private static void crearUsuario() {
        try {
            // PASO 1: Pedir y validar nombre
            String nombre;
            do {
                nombre = JOptionPane.showInputDialog(
                        null,
                        "Ingresa el nombre del usuario:",
                        "Crear Usuario - Nombre",
                        JOptionPane.QUESTION_MESSAGE
                );

                // Si el usuario canceló, salir del método
                if (nombre == null) return;

                // Validar que no esté vacío
                if (nombre.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "El nombre no puede estar vacío.\nPor favor, ingresa un nombre válido.",
                            "Error de Validación",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } while (nombre.trim().isEmpty());

            // PASO 2: Pedir y validar email
            String email;
            boolean emailValido = false;
            do {
                email = JOptionPane.showInputDialog(
                        null,
                        "Ingresa el email del usuario:",
                        "Crear Usuario - Email",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (email == null) return;

                // Validar que no esté vacío
                if (email.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "El email no puede estar vacío.\nPor favor, ingresa un email válido.",
                            "Error de Validación",
                            JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }

                // Validar formato de email (debe contener @)
                if (!email.contains("@")) {
                    JOptionPane.showMessageDialog(
                            null,
                            "El email debe tener un formato válido (debe contener @).\nPor favor, ingresa un email válido.",
                            "Error de Validación",
                            JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }

                emailValido = true;

            } while (!emailValido);

            // PASO 3: Pedir y validar edad (con TODAS las validaciones)
            String edadStr;
            int edad = 0;
            boolean edadValida = false;

            do {
                edadStr = JOptionPane.showInputDialog(
                        null,
                        "Ingresa la edad del usuario (1-150):",
                        "Crear Usuario - Edad",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (edadStr == null) return;

                // Validación 1: No vacío
                if (edadStr.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "La edad no puede estar vacía.\nPor favor, ingresa un número.",
                            "Error de Validación",
                            JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }

                // Validación 2: Debe ser un número
                try {
                    edad = Integer.parseInt(edadStr.trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "La edad debe ser un número válido.\nPor favor, ingresa solo números.",
                            "Error de Validación",
                            JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }

                // Validación 3: Rango válido (1-150) - misma validación que el Service
                if (edad <= 0) {
                    JOptionPane.showMessageDialog(
                            null,
                            "La edad debe ser mayor a 0.\nPor favor, ingresa una edad válida.",
                            "Error de Validación",
                            JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }

                if (edad > 150) {
                    JOptionPane.showMessageDialog(
                            null,
                            "La edad no puede ser mayor a 150.\nPor favor, ingresa una edad válida.",
                            "Error de Validación",
                            JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }

                // Si llegó aquí, la edad es válida
                edadValida = true;

            } while (!edadValida);

            // PASO 4: Crear objeto User (todos los datos ya están validados)
            User nuevoUsuario = new User(nombre, email, edad);

            // PASO 5: Llamar al servicio para guardar
            boolean exito = userService.createUser(nuevoUsuario);

            // PASO 6: Mostrar resultado
            if (exito) {
                JOptionPane.showMessageDialog(
                        null,
                        "✓ Usuario creado exitosamente!\n\n" +
                                "Nombre: " + nombre + "\n" +
                                "Email: " + email + "\n" +
                                "Edad: " + edad,
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "No se pudo crear el usuario.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (IllegalArgumentException e) {
            // Este catch ya no debería ejecutarse porque validamos todo antes
            JOptionPane.showMessageDialog(
                    null,
                    "Error de validación:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (SQLException e) {
            // Error de base de datos
            JOptionPane.showMessageDialog(
                    null,
                    "Error de base de datos:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * listarUsuarios() - Muestra todos los usuarios en una ventana
     *
     * FLUJO:
     * 1. Obtener lista de usuarios del servicio
     * 2. Verificar si hay usuarios
     * 3. Formatear la lista en un String
     * 4. Mostrar en ventana
     */
    private static void listarUsuarios() {
        try {
            // PASO 1: Obtener lista de usuarios
            List<User> usuarios = userService.getAllUsers();

            // PASO 2: Verificar si hay usuarios
            if (usuarios.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "No hay usuarios registrados en el sistema.",
                        "Lista Vacía",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // PASO 3: Formatear la lista
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("═══════════════════════════════════\n");
            mensaje.append("   LISTA DE USUARIOS (Total: ").append(usuarios.size()).append(")\n");
            mensaje.append("═══════════════════════════════════\n\n");

            for (User user : usuarios) {
                mensaje.append("ID: ").append(user.getId()).append("\n");
                mensaje.append("Nombre: ").append(user.getName()).append("\n");
                mensaje.append("Email: ").append(user.getEmail()).append("\n");
                mensaje.append("Edad: ").append(user.getAge()).append("\n");
                mensaje.append("───────────────────────────────────\n");
            }

            // PASO 4: Mostrar en ventana
            JOptionPane.showMessageDialog(
                    null,
                    mensaje.toString(),
                    "Lista de Usuarios",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error al obtener la lista de usuarios:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * buscarUsuario() - Busca un usuario por su ID
     *
     * FLUJO:
     * 1. Pedir ID
     * 2. Buscar en el servicio
     * 3. Mostrar datos si existe, o mensaje si no existe
     */
    private static void buscarUsuario() {
        try {
            // PASO 1: Pedir ID
            String idStr = JOptionPane.showInputDialog(
                    null,
                    "Ingresa el ID del usuario a buscar:",
                    "Buscar Usuario",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (idStr == null) return; // Canceló

            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "El ID debe ser un número válido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // PASO 2: Buscar usuario
            User user = userService.getUserById(id);

            // PASO 3: Mostrar resultado
            if (user != null) {
                JOptionPane.showMessageDialog(
                        null,
                        "Usuario encontrado:\n\n" +
                                "ID: " + user.getId() + "\n" +
                                "Nombre: " + user.getName() + "\n" +
                                "Email: " + user.getEmail() + "\n" +
                                "Edad: " + user.getAge(),
                        "Usuario Encontrado",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "No se encontró un usuario con el ID: " + id,
                        "No Encontrado",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error de base de datos:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * actualizarUsuario() - Actualiza los datos de un usuario existente
     *
     * FLUJO:
     * 1. Pedir ID del usuario a actualizar
     * 2. Buscar el usuario (para mostrar datos actuales)
     * 3. Pedir nuevos datos
     * 4. Actualizar en el servicio
     * 5. Mostrar resultado
     */
    private static void actualizarUsuario() {
        try {
            // PASO 1: Pedir ID
            String idStr = JOptionPane.showInputDialog(
                    null,
                    "Ingresa el ID del usuario a actualizar:",
                    "Actualizar Usuario",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (idStr == null) return;

            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "El ID debe ser un número válido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // PASO 2: Buscar usuario actual
            User userActual = userService.getUserById(id);

            if (userActual == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "No se encontró un usuario con el ID: " + id,
                        "No Encontrado",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Mostrar datos actuales
            JOptionPane.showMessageDialog(
                    null,
                    "Datos actuales del usuario:\n\n" +
                            "ID: " + userActual.getId() + "\n" +
                            "Nombre: " + userActual.getName() + "\n" +
                            "Email: " + userActual.getEmail() + "\n" +
                            "Edad: " + userActual.getAge() + "\n\n" +
                            "Ingresa los nuevos datos...",
                    "Actualizar Usuario",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // PASO 3: Pedir nuevos datos con validación DO-WHILE
            String nuevoNombre;
            do {
                nuevoNombre = JOptionPane.showInputDialog(
                        null,
                        "Nuevo nombre (actual: " + userActual.getName() + "):",
                        "Actualizar - Nombre",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (nuevoNombre == null) return;

                if (nuevoNombre.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "El nombre no puede estar vacío.\nPor favor, ingresa un nombre válido.",
                            "Error de Validación",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } while (nuevoNombre.trim().isEmpty());

            String nuevoEmail;
            do {
                nuevoEmail = JOptionPane.showInputDialog(
                        null,
                        "Nuevo email (actual: " + userActual.getEmail() + "):",
                        "Actualizar - Email",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (nuevoEmail == null) return;

                if (nuevoEmail.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "El email no puede estar vacío.\nPor favor, ingresa un email válido.",
                            "Error de Validación",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } while (nuevoEmail.trim().isEmpty());

            String nuevaEdadStr;
            int nuevaEdad = 0;
            boolean edadValida = false;

            do {
                nuevaEdadStr = JOptionPane.showInputDialog(
                        null,
                        "Nueva edad (actual: " + userActual.getAge() + "):",
                        "Actualizar - Edad",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (nuevaEdadStr == null) return;

                if (nuevaEdadStr.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "La edad no puede estar vacía.\nPor favor, ingresa un número.",
                            "Error de Validación",
                            JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }

                try {
                    nuevaEdad = Integer.parseInt(nuevaEdadStr.trim());
                    edadValida = true;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "La edad debe ser un número válido.\nPor favor, ingresa solo números.",
                            "Error de Validación",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } while (!edadValida);

            // PASO 4: Crear objeto User actualizado
            User userActualizado = new User(id, nuevoNombre, nuevoEmail, nuevaEdad);

            // Actualizar en el servicio
            boolean exito = userService.updateUser(userActualizado);

            // PASO 5: Mostrar resultado
            if (exito) {
                JOptionPane.showMessageDialog(
                        null,
                        "✓ Usuario actualizado exitosamente!\n\n" +
                                "ID: " + id + "\n" +
                                "Nombre: " + nuevoNombre + "\n" +
                                "Email: " + nuevoEmail + "\n" +
                                "Edad: " + nuevaEdad,
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "No se pudo actualizar el usuario.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error de validación:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error de base de datos:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * eliminarUsuario() - Elimina un usuario del sistema
     *
     * FLUJO:
     * 1. Pedir ID
     * 2. Buscar usuario (para mostrar qué se va a eliminar)
     * 3. Pedir confirmación
     * 4. Eliminar del servicio
     * 5. Mostrar resultado
     */
    private static void eliminarUsuario() {
        try {
            // PASO 1: Pedir ID
            String idStr = JOptionPane.showInputDialog(
                    null,
                    "Ingresa el ID del usuario a eliminar:",
                    "Eliminar Usuario",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (idStr == null) return;

            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "El ID debe ser un número válido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // PASO 2: Buscar usuario
            User user = userService.getUserById(id);

            if (user == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "No se encontró un usuario con el ID: " + id,
                        "No Encontrado",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // PASO 3: Pedir confirmación
            int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Estás seguro que deseas eliminar este usuario?\n\n" +
                            "ID: " + user.getId() + "\n" +
                            "Nombre: " + user.getName() + "\n" +
                            "Email: " + user.getEmail() + "\n" +
                            "Edad: " + user.getAge() + "\n\n" +
                            "⚠️ Esta acción NO se puede deshacer.",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            // Si el usuario NO confirmó, salir
            if (confirmacion != JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(
                        null,
                        "Operación cancelada.",
                        "Cancelado",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // PASO 4: Eliminar
            boolean exito = userService.deleteUser(id);

            // PASO 5: Mostrar resultado
            if (exito) {
                JOptionPane.showMessageDialog(
                        null,
                        "✓ Usuario eliminado exitosamente.\n\n" +
                                "ID: " + id + "\n" +
                                "Nombre: " + user.getName(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "No se pudo eliminar el usuario.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error de base de datos:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONCEPTOS DE JOptionPane
// ═══════════════════════════════════════════════════════════════════════════════

// 1. TIPOS DE DIÁLOGOS:
//
//    showMessageDialog(): Muestra un mensaje
//    - JOptionPane.INFORMATION_MESSAGE → ícono de info (i)
//    - JOptionPane.WARNING_MESSAGE → ícono de advertencia (⚠)
//    - JOptionPane.ERROR_MESSAGE → ícono de error (X)
//
//    showInputDialog(): Pide entrada de texto
//    - Retorna String con el texto ingresado
//    - Retorna null si el usuario cancela
//
//    showConfirmDialog(): Pide confirmación
//    - YES_NO_OPTION → botones Sí/No
//    - Retorna JOptionPane.YES_OPTION o JOptionPane.NO_OPTION
//
// 2. MANEJO DE CANCELACIÓN:
//    - Cuando el usuario presiona "Cancelar" o cierra la ventana
//    - showInputDialog() retorna null
//    - Siempre verificar if (input == null) para evitar errores
//
// 3. CONVERSIÓN DE TIPOS:
//    - JOptionPane siempre retorna String
//    - Usar Integer.parseInt() para convertir a int
//    - Envolver en try-catch para manejar NumberFormatException
//
// 4. BUENAS PRÁCTICAS:
//    ✓ Validar siempre que el usuario no haya cancelado
//    ✓ Capturar excepciones y mostrar mensajes amigables
//    ✓ Usar títulos descriptivos en las ventanas
//    ✓ Pedir confirmación para operaciones destructivas (eliminar)
