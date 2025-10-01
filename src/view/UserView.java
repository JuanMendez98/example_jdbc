package src.view;

// ═══════════════════════════════════════════════════════════════════════════════
// IMPORTACIONES - Librerías para crear la interfaz gráfica
// ═══════════════════════════════════════════════════════════════════════════════

import javax.swing.JOptionPane;
// JOptionPane: Clase de Swing para crear cuadros de diálogo simples
// Permite mostrar mensajes, pedir inputs y confirmar acciones SIN crear ventanas complejas

import src.model.User;
// User: Clase modelo que representa un usuario

import java.util.List;
// List: Interfaz para manejar listas de usuarios

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * UserView - VISTA en el patrón MVC (Model-View-Controller)
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * PATRÓN MVC - ROL DE LA VISTA:
 *
 *   ┌─────────────────────────────────────────────────────────┐
 *   │                     VISTA (View)                        │
 *   │                                                          │
 *   │  RESPONSABILIDADES:                                     │
 *   │  1. Mostrar información al usuario                     │
 *   │  2. Capturar entradas del usuario                      │
 *   │  3. Validar FORMATO de datos (no lógica de negocio)   │
 *   │  4. Enviar acciones al Controller                      │
 *   │                                                          │
 *   │  LO QUE NO DEBE HACER:                                 │
 *   │  ✗ Acceder directamente a la base de datos            │
 *   │  ✗ Contener lógica de negocio                         │
 *   │  ✗ Conocer detalles de persistencia                   │
 *   │                                                          │
 *   │  ESTA CLASE = UserView.java                            │
 *   └─────────────────────────────────────────────────────────┘
 *                          │
 *                          │ Eventos del usuario
 *                          ▼
 *   ┌─────────────────────────────────────────────────────────┐
 *   │                CONTROLADOR (Controller)                 │
 *   └─────────────────────────────────────────────────────────┘
 *
 * MÉTODOS DE ESTA CLASE:
 * - mostrarMenu(): Muestra el menú principal
 * - pedirNombre(): Pide el nombre con validación
 * - pedirEmail(): Pide el email con validación
 * - pedirEdad(): Pide la edad con validación
 * - pedirId(): Pide un ID
 * - mostrarUsuarios(): Muestra lista de usuarios
 * - mostrarUsuario(): Muestra un usuario
 * - mostrarMensaje(): Muestra mensaje genérico
 * - mostrarError(): Muestra mensaje de error
 * - confirmarAccion(): Pide confirmación Sí/No
 */
public class UserView {

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: mostrarMenu() - Muestra el menú principal
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * mostrarMenu() - Muestra el menú de opciones y retorna la opción elegida
     *
     * OPCIONES:
     * 1 - Crear Usuario
     * 2 - Listar Usuarios
     * 3 - Buscar Usuario
     * 4 - Actualizar Usuario
     * 5 - Eliminar Usuario
     * 6 - Salir
     *
     * @return int - Opción elegida (1-6), o -1 si cancela
     */
    public int mostrarMenu() {
        // Crear el mensaje del menú con todas las opciones
        String menu = """
                ╔══════════════════════════════════╗
                ║   SISTEMA DE GESTIÓN DE USUARIOS ║
                ╚══════════════════════════════════╝

                1. Crear Usuario
                2. Listar Usuarios
                3. Buscar Usuario por ID
                4. Actualizar Usuario
                5. Eliminar Usuario
                6. Salir

                Seleccione una opción:
                """;

        // showInputDialog() muestra un cuadro con el mensaje y un campo de texto
        // Retorna el texto ingresado, o null si el usuario cancela
        String opcionStr = JOptionPane.showInputDialog(null, menu,
                                                        "Menú Principal",
                                                        JOptionPane.QUESTION_MESSAGE);

        // Si el usuario cancela o cierra el diálogo
        if (opcionStr == null) {
            return -1; // Señal de cancelación
        }

        // Convertir el String a int
        try {
            return Integer.parseInt(opcionStr.trim());
        } catch (NumberFormatException e) {
            // Si no es un número válido, retornar 0 (opción inválida)
            return 0;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: pedirNombre() - Solicita el nombre del usuario con validación
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * pedirNombre() - Pide el nombre y valida que no esté vacío
     *
     * VALIDACIÓN:
     * - No puede estar vacío
     * - No puede tener solo espacios
     * - Si es inválido, vuelve a pedir (bucle do-while)
     *
     * FLUJO:
     * 1. Mostrar diálogo pidiendo nombre
     * 2. Si cancela → retornar null
     * 3. Si está vacío → mostrar error y repetir
     * 4. Si es válido → retornar nombre
     *
     * @return String - Nombre ingresado, o null si cancela
     */
    public String pedirNombre() {
        String nombre;
        boolean nombreValido = false;

        // Bucle do-while: se repite hasta que el nombre sea válido
        do {
            // Pedir nombre al usuario
            nombre = JOptionPane.showInputDialog(null,
                                                  "Ingrese el nombre del usuario:",
                                                  "Nombre",
                                                  JOptionPane.QUESTION_MESSAGE);

            // Si el usuario cancela (cierra el diálogo o presiona Cancelar)
            if (nombre == null) {
                return null; // Retornar null señala cancelación al Controller
            }

            // Validar que no esté vacío
            // trim() elimina espacios al inicio y final
            // isEmpty() verifica si quedó vacío después del trim
            if (nombre.trim().isEmpty()) {
                // Mostrar mensaje de error
                JOptionPane.showMessageDialog(null,
                                              "El nombre no puede estar vacío",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                // continue hace que el bucle se repita (vuelve al inicio del do)
                continue;
            }

            // Si llegó aquí, el nombre es válido
            nombreValido = true;

        } while (!nombreValido); // Repetir mientras nombreValido sea false

        // Retornar el nombre validado (sin espacios al inicio/final)
        return nombre.trim();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: pedirEmail() - Solicita el email con validación
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * pedirEmail() - Pide el email y valida formato básico
     *
     * VALIDACIONES:
     * 1. No puede estar vacío
     * 2. Debe contener @
     *
     * NOTA: Esta es una validación BÁSICA. En producción se usaría regex más complejo
     *
     * @return String - Email ingresado, o null si cancela
     */
    public String pedirEmail() {
        String email;
        boolean emailValido = false;

        do {
            email = JOptionPane.showInputDialog(null,
                                                 "Ingrese el email del usuario:",
                                                 "Email",
                                                 JOptionPane.QUESTION_MESSAGE);

            // Si cancela
            if (email == null) {
                return null;
            }

            // Validación 1: No puede estar vacío
            if (email.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                                              "El email no puede estar vacío",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                continue; // Volver al inicio del bucle
            }

            // Validación 2: Debe contener @
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(null,
                                              "El email debe contener el símbolo @",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                continue;
            }

            // Si pasó todas las validaciones
            emailValido = true;

        } while (!emailValido);

        return email.trim();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: pedirEdad() - Solicita la edad con validación completa
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * pedirEdad() - Pide la edad y valida que sea un número en rango válido
     *
     * VALIDACIONES (todas en un solo bucle):
     * 1. No puede estar vacío
     * 2. Debe ser un número entero
     * 3. Debe ser mayor a 0
     * 4. Debe ser menor o igual a 150
     *
     * IMPORTANTE: Si una validación falla, solo se repite este campo,
     * NO se pierden los datos anteriores (nombre, email)
     *
     * @return int - Edad ingresada, o -1 si cancela
     */
    public int pedirEdad() {
        String edadStr;
        int edad = 0;
        boolean edadValida = false;

        do {
            edadStr = JOptionPane.showInputDialog(null,
                                                   "Ingrese la edad del usuario (1-150):",
                                                   "Edad",
                                                   JOptionPane.QUESTION_MESSAGE);

            // Si cancela
            if (edadStr == null) {
                return -1; // -1 señala cancelación
            }

            // Validación 1: No puede estar vacío
            if (edadStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                                              "La edad no puede estar vacía",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                continue;
            }

            // Validación 2: Debe ser un número entero
            try {
                edad = Integer.parseInt(edadStr.trim());
            } catch (NumberFormatException e) {
                // Si no se puede convertir a int, es que no es un número
                JOptionPane.showMessageDialog(null,
                                              "La edad debe ser un número entero",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                continue; // Volver a pedir
            }

            // Validación 3: Debe ser mayor a 0
            if (edad <= 0) {
                JOptionPane.showMessageDialog(null,
                                              "La edad debe ser mayor a 0",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                continue;
            }

            // Validación 4: Debe ser menor o igual a 150
            if (edad > 150) {
                JOptionPane.showMessageDialog(null,
                                              "La edad debe ser menor o igual a 150",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                continue;
            }

            // Si pasó todas las validaciones
            edadValida = true;

        } while (!edadValida);

        return edad;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: pedirId() - Solicita un ID con validación
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * pedirId() - Pide un ID numérico
     *
     * VALIDACIONES:
     * 1. No puede estar vacío
     * 2. Debe ser un número entero
     * 3. Debe ser mayor a 0
     *
     * @param mensaje - Mensaje personalizado a mostrar
     * @return int - ID ingresado, o -1 si cancela
     */
    public int pedirId(String mensaje) {
        String idStr;
        int id = 0;
        boolean idValido = false;

        do {
            idStr = JOptionPane.showInputDialog(null,
                                                 mensaje,
                                                 "ID",
                                                 JOptionPane.QUESTION_MESSAGE);

            if (idStr == null) {
                return -1; // Cancelación
            }

            if (idStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                                              "El ID no puede estar vacío",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                continue;
            }

            try {
                id = Integer.parseInt(idStr.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                                              "El ID debe ser un número",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (id <= 0) {
                JOptionPane.showMessageDialog(null,
                                              "El ID debe ser mayor a 0",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                continue;
            }

            idValido = true;

        } while (!idValido);

        return id;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: mostrarUsuarios() - Muestra lista de usuarios
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * mostrarUsuarios() - Muestra todos los usuarios en un cuadro de diálogo
     *
     * FORMATO:
     * ID: 1
     * Nombre: Juan Pérez
     * Email: juan@example.com
     * Edad: 25
     * -------------------------
     * ID: 2
     * ...
     *
     * @param usuarios - Lista de usuarios a mostrar
     */
    public void mostrarUsuarios(List<User> usuarios) {
        // Verificar si la lista está vacía
        if (usuarios == null || usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                                          "No hay usuarios registrados",
                                          "Información",
                                          JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Construir el mensaje con todos los usuarios
        StringBuilder mensaje = new StringBuilder("═══ LISTA DE USUARIOS ═══\n\n");

        // Recorrer la lista de usuarios
        for (User user : usuarios) {
            mensaje.append("ID: ").append(user.getId()).append("\n");
            mensaje.append("Nombre: ").append(user.getName()).append("\n");
            mensaje.append("Email: ").append(user.getEmail()).append("\n");
            mensaje.append("Edad: ").append(user.getAge()).append("\n");
            mensaje.append("-------------------------\n");
        }

        // Mostrar el mensaje en un cuadro de diálogo
        JOptionPane.showMessageDialog(null,
                                      mensaje.toString(),
                                      "Usuarios",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: mostrarUsuario() - Muestra un usuario específico
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * mostrarUsuario() - Muestra los datos de un usuario
     *
     * @param user - Usuario a mostrar
     */
    public void mostrarUsuario(User user) {
        if (user == null) {
            JOptionPane.showMessageDialog(null,
                                          "Usuario no encontrado",
                                          "Información",
                                          JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String mensaje = "═══ DATOS DEL USUARIO ═══\n\n" +
                        "ID: " + user.getId() + "\n" +
                        "Nombre: " + user.getName() + "\n" +
                        "Email: " + user.getEmail() + "\n" +
                        "Edad: " + user.getAge();

        JOptionPane.showMessageDialog(null,
                                      mensaje,
                                      "Usuario",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: mostrarMensaje() - Muestra mensaje genérico de éxito
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * mostrarMensaje() - Muestra un mensaje de información
     *
     * @param mensaje - Mensaje a mostrar
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null,
                                      mensaje,
                                      "Información",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: mostrarError() - Muestra mensaje de error
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * mostrarError() - Muestra un mensaje de error
     *
     * @param mensaje - Mensaje de error a mostrar
     */
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null,
                                      mensaje,
                                      "Error",
                                      JOptionPane.ERROR_MESSAGE);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: confirmarAccion() - Pide confirmación Sí/No
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * confirmarAccion() - Muestra diálogo de confirmación Sí/No
     *
     * @param mensaje - Mensaje de confirmación
     * @return boolean - true si elige Sí, false si elige No
     */
    public boolean confirmarAccion(String mensaje) {
        // showConfirmDialog() muestra botones Sí/No
        // Retorna:
        // - JOptionPane.YES_OPTION si presiona Sí
        // - JOptionPane.NO_OPTION si presiona No
        // - JOptionPane.CLOSED_OPTION si cierra el diálogo
        int respuesta = JOptionPane.showConfirmDialog(null,
                                                      mensaje,
                                                      "Confirmación",
                                                      JOptionPane.YES_NO_OPTION,
                                                      JOptionPane.QUESTION_MESSAGE);

        return respuesta == JOptionPane.YES_OPTION;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONCEPTOS CLAVE DE LA VISTA EN MVC - RESUMEN EDUCATIVO
// ═══════════════════════════════════════════════════════════════════════════════

// 1. RESPONSABILIDADES DE LA VISTA:
//    ✅ Mostrar información al usuario (UI)
//    ✅ Capturar entradas del usuario
//    ✅ Validar FORMATO de datos (vacío, numérico, etc.)
//    ✅ Comunicarse con el Controller
//
//    ❌ NO debe:
//    - Acceder directamente a la base de datos
//    - Contener lógica de negocio
//    - Conocer detalles del Model

// 2. JOptionPane - Métodos principales:
//    - showInputDialog(): Pide un dato al usuario (retorna String o null)
//    - showMessageDialog(): Muestra un mensaje
//    - showConfirmDialog(): Pide confirmación Sí/No (retorna int)

// 3. Tipos de mensajes en JOptionPane:
//    - INFORMATION_MESSAGE: Icono de información (ℹ)
//    - ERROR_MESSAGE: Icono de error (✗)
//    - WARNING_MESSAGE: Icono de advertencia (⚠)
//    - QUESTION_MESSAGE: Icono de pregunta (?)
//    - PLAIN_MESSAGE: Sin icono

// 4. Validación en la Vista vs en el Model:
//    VISTA (UserView):
//    - Validaciones de FORMATO
//    - ¿Está vacío?
//    - ¿Es un número?
//    - ¿Tiene el formato básico correcto?
//
//    MODEL (UserModel):
//    - Validaciones de NEGOCIO
//    - ¿La edad está en rango válido? (1-150)
//    - ¿El email tiene formato completo correcto?
//    - ¿El usuario ya existe?

// 5. Patrón do-while para validación:
//    do {
//        // 1. Pedir dato
//        // 2. Si cancela → return null
//        // 3. Validar dato
//        // 4. Si es inválido → mostrar error y continue
//        // 5. Si es válido → marcar como válido
//    } while (!esValido);
//
//    VENTAJA: Solo se repite el campo inválido, no todos los datos

// 6. Retornar null como señal de cancelación:
//    - Si el usuario presiona Cancelar, retornamos null
//    - El Controller debe verificar esto y manejar la cancelación

// 7. StringBuilder para construir mensajes largos:
//    - Más eficiente que concatenar Strings con +
//    - Útil cuando se construyen mensajes en bucles

// 8. Separación de responsabilidades:
//    - La Vista NO crea objetos User
//    - La Vista NO llama al Model directamente
//    - Solo pide datos y los envía al Controller
//    - El Controller es quien crea el User y llama al Model
