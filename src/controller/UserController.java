package src.controller;

// ═══════════════════════════════════════════════════════════════════════════════
// IMPORTACIONES
// ═══════════════════════════════════════════════════════════════════════════════

import src.model.User;
// User: Clase modelo que representa un usuario

import src.model.UserModel;
// UserModel: Maneja la lógica de negocio y acceso a datos

import src.view.UserView;
// UserView: Maneja la interfaz gráfica (JOptionPane)

import java.sql.SQLException;
// SQLException: Excepción de errores de base de datos

import java.util.List;
// List: Colección para listas de usuarios

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * UserController - CONTROLADOR en el patrón MVC (Model-View-Controller)
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * PATRÓN MVC - ROL DEL CONTROLADOR:
 *
 *   ┌─────────────────────────────────────────────────────────┐
 *   │               CONTROLADOR (Controller)                  │
 *   │                                                          │
 *   │  RESPONSABILIDADES:                                     │
 *   │  1. COORDINAR Model y View                             │
 *   │  2. RECIBIR eventos del usuario desde View             │
 *   │  3. ACTUALIZAR Model con nuevos datos                  │
 *   │  4. PEDIR a View que actualice la interfaz             │
 *   │  5. MANEJAR el flujo de la aplicación                  │
 *   │                                                          │
 *   │  ESTA CLASE = UserController.java                      │
 *   └──────────────────┬──────────────────────────────────────┘
 *                      │
 *        ┌─────────────┴─────────────┐
 *        │                           │
 *        ▼                           ▼
 *   ┌─────────┐                 ┌─────────┐
 *   │  Model  │                 │  View   │
 *   └─────────┘                 └─────────┘
 *
 * FLUJO DE COMUNICACIÓN EN MVC:
 *
 * 1. Usuario interactúa con la VIEW (presiona botón, ingresa datos)
 *    ↓
 * 2. VIEW captura el evento y llama al CONTROLLER
 *    ↓
 * 3. CONTROLLER procesa el evento:
 *    - Valida si es necesario
 *    - Llama al MODEL para actualizar datos
 *    ↓
 * 4. MODEL ejecuta lógica de negocio y accede a base de datos
 *    ↓
 * 5. MODEL retorna resultado al CONTROLLER
 *    ↓
 * 6. CONTROLLER pide a VIEW que actualice la interfaz
 *    ↓
 * 7. VIEW muestra los nuevos datos al usuario
 *
 * EJEMPLO CONCRETO (Crear Usuario):
 *
 * 1. Usuario elige "1 - Crear Usuario" → VIEW captura
 * 2. VIEW llama a controller.crearUsuario()
 * 3. CONTROLLER pide datos a VIEW: nombre, email, edad
 * 4. CONTROLLER crea objeto User con esos datos
 * 5. CONTROLLER llama a model.save(user)
 * 6. MODEL valida, conecta a BD, ejecuta INSERT
 * 7. MODEL retorna true/false al CONTROLLER
 * 8. CONTROLLER pide a VIEW mostrar mensaje de éxito/error
 *
 * VENTAJAS DEL PATRÓN MVC:
 * - Separación clara de responsabilidades
 * - Model y View no se conocen (bajo acoplamiento)
 * - Fácil de testear cada componente
 * - Fácil cambiar la UI sin tocar lógica de negocio
 */
public class UserController {

    // ═══════════════════════════════════════════════════════════════════════════
    // ATRIBUTOS - Referencias a Model y View
    // ═══════════════════════════════════════════════════════════════════════════

    private UserModel model;
    // model: Maneja la lógica de negocio y acceso a datos
    // El Controller DELEGA las operaciones de datos al Model

    private UserView view;
    // view: Maneja la interfaz gráfica
    // El Controller DELEGA la presentación a la View

    // ═══════════════════════════════════════════════════════════════════════════
    // CONSTRUCTOR - Inicializa Model y View
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Constructor - Crea instancias de Model y View
     *
     * PATRÓN DE COMPOSICIÓN:
     * El Controller "tiene" un Model y "tiene" una View
     * Esto se llama composición (has-a relationship)
     */
    public UserController() {
        this.model = new UserModel();
        this.view = new UserView();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: iniciar() - Punto de entrada del Controller
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * iniciar() - Inicia el bucle principal de la aplicación
     *
     * FLUJO:
     * 1. Mostrar menú (VIEW)
     * 2. Capturar opción elegida
     * 3. Ejecutar la acción correspondiente (switch)
     * 4. Repetir hasta que el usuario elija "Salir"
     */
    public void iniciar() {
        int opcion;

        // Bucle principal: se repite hasta que el usuario elija salir
        do {
            // PASO 1: Pedir a VIEW que muestre el menú
            opcion = view.mostrarMenu();

            // PASO 2: Procesar la opción elegida
            switch (opcion) {
                case 1:
                    crearUsuario();
                    break;
                case 2:
                    listarUsuarios();
                    break;
                case 3:
                    buscarUsuario();
                    break;
                case 4:
                    actualizarUsuario();
                    break;
                case 5:
                    eliminarUsuario();
                    break;
                case 6:
                    view.mostrarMensaje("¡Hasta pronto!");
                    break;
                case -1:
                    // Usuario canceló el menú (cerró diálogo)
                    view.mostrarMensaje("Aplicación cerrada");
                    opcion = 6; // Forzar salida
                    break;
                default:
                    view.mostrarError("Opción inválida. Por favor elija 1-6");
            }

        } while (opcion != 6); // Repetir mientras no sea "Salir"
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: crearUsuario() - Coordina la creación de un usuario
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * crearUsuario() - Flujo completo para crear un usuario
     *
     * FLUJO:
     * 1. VIEW pide nombre
     * 2. Si cancela → salir
     * 3. VIEW pide email
     * 4. Si cancela → salir
     * 5. VIEW pide edad
     * 6. Si cancela → salir
     * 7. CONTROLLER crea objeto User
     * 8. CONTROLLER llama a model.save()
     * 9. VIEW muestra resultado
     */
    private void crearUsuario() {
        try {
            // PASO 1: Pedir nombre a la VIEW
            String nombre = view.pedirNombre();
            if (nombre == null) return; // Usuario canceló

            // PASO 2: Pedir email a la VIEW
            String email = view.pedirEmail();
            if (email == null) return; // Usuario canceló

            // PASO 3: Pedir edad a la VIEW
            int edad = view.pedirEdad();
            if (edad == -1) return; // Usuario canceló

            // PASO 4: Crear objeto User con los datos
            // Usamos constructor sin ID (el ID lo genera MySQL)
            User nuevoUser = new User(nombre, email, edad);

            // PASO 5: Llamar al MODEL para guardar
            boolean guardado = model.save(nuevoUser);

            // PASO 6: Pedir a VIEW que muestre el resultado
            if (guardado) {
                view.mostrarMensaje("✓ Usuario creado exitosamente");
            } else {
                view.mostrarError("✗ No se pudo crear el usuario");
            }

        } catch (IllegalArgumentException e) {
            // Excepción de validación (lanzada por MODEL)
            view.mostrarError("Error de validación: " + e.getMessage());

        } catch (SQLException e) {
            // Excepción de base de datos
            view.mostrarError("Error de base de datos: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: listarUsuarios() - Coordina el listado de usuarios
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * listarUsuarios() - Muestra todos los usuarios
     *
     * FLUJO:
     * 1. CONTROLLER llama a model.findAll()
     * 2. MODEL retorna lista de usuarios
     * 3. CONTROLLER pide a VIEW que los muestre
     */
    private void listarUsuarios() {
        try {
            // PASO 1: Pedir al MODEL todos los usuarios
            List<User> usuarios = model.findAll();

            // PASO 2: Pedir a VIEW que los muestre
            view.mostrarUsuarios(usuarios);

        } catch (SQLException e) {
            view.mostrarError("Error al obtener usuarios: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: buscarUsuario() - Coordina la búsqueda por ID
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * buscarUsuario() - Busca un usuario por ID
     *
     * FLUJO:
     * 1. VIEW pide el ID
     * 2. CONTROLLER llama a model.findById()
     * 3. VIEW muestra el usuario encontrado
     */
    private void buscarUsuario() {
        try {
            // PASO 1: Pedir ID a la VIEW
            int id = view.pedirId("Ingrese el ID del usuario a buscar:");
            if (id == -1) return; // Usuario canceló

            // PASO 2: Llamar al MODEL para buscar
            User usuario = model.findById(id);

            // PASO 3: Pedir a VIEW que muestre el resultado
            if (usuario != null) {
                view.mostrarUsuario(usuario);
            } else {
                view.mostrarError("No se encontró usuario con ID: " + id);
            }

        } catch (IllegalArgumentException e) {
            view.mostrarError("Error: " + e.getMessage());

        } catch (SQLException e) {
            view.mostrarError("Error al buscar usuario: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: actualizarUsuario() - Coordina la actualización
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * actualizarUsuario() - Actualiza los datos de un usuario existente
     *
     * FLUJO:
     * 1. VIEW pide ID del usuario a actualizar
     * 2. MODEL busca el usuario
     * 3. Si existe, VIEW muestra datos actuales
     * 4. VIEW pide nuevos datos
     * 5. CONTROLLER crea User con nuevos datos
     * 6. MODEL actualiza en base de datos
     * 7. VIEW muestra resultado
     */
    private void actualizarUsuario() {
        try {
            // PASO 1: Pedir ID
            int id = view.pedirId("Ingrese el ID del usuario a actualizar:");
            if (id == -1) return; // Canceló

            // PASO 2: Buscar si existe
            User usuarioActual = model.findById(id);

            if (usuarioActual == null) {
                view.mostrarError("No existe usuario con ID: " + id);
                return;
            }

            // PASO 3: Mostrar datos actuales
            view.mostrarUsuario(usuarioActual);

            // PASO 4: Pedir nuevos datos
            String nuevoNombre = view.pedirNombre();
            if (nuevoNombre == null) return; // Canceló

            String nuevoEmail = view.pedirEmail();
            if (nuevoEmail == null) return; // Canceló

            int nuevaEdad = view.pedirEdad();
            if (nuevaEdad == -1) return; // Canceló

            // PASO 5: Crear User con nuevos datos (incluir ID)
            User usuarioActualizado = new User(id, nuevoNombre, nuevoEmail, nuevaEdad);

            // PASO 6: Llamar al MODEL para actualizar
            boolean actualizado = model.update(usuarioActualizado);

            // PASO 7: Mostrar resultado
            if (actualizado) {
                view.mostrarMensaje("✓ Usuario actualizado exitosamente");
            } else {
                view.mostrarError("✗ No se pudo actualizar el usuario");
            }

        } catch (IllegalArgumentException e) {
            view.mostrarError("Error de validación: " + e.getMessage());

        } catch (SQLException e) {
            view.mostrarError("Error al actualizar: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MÉTODO: eliminarUsuario() - Coordina la eliminación
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * eliminarUsuario() - Elimina un usuario de la base de datos
     *
     * FLUJO:
     * 1. VIEW pide ID
     * 2. MODEL busca usuario
     * 3. Si existe, VIEW pide confirmación
     * 4. Si confirma, MODEL elimina
     * 5. VIEW muestra resultado
     */
    private void eliminarUsuario() {
        try {
            // PASO 1: Pedir ID
            int id = view.pedirId("Ingrese el ID del usuario a eliminar:");
            if (id == -1) return; // Canceló

            // PASO 2: Buscar si existe
            User usuario = model.findById(id);

            if (usuario == null) {
                view.mostrarError("No existe usuario con ID: " + id);
                return;
            }

            // PASO 3: Mostrar usuario y pedir confirmación
            view.mostrarUsuario(usuario);

            boolean confirmar = view.confirmarAccion(
                "¿Está seguro que desea eliminar este usuario?\n" +
                "Esta acción NO se puede deshacer."
            );

            if (!confirmar) {
                view.mostrarMensaje("Eliminación cancelada");
                return;
            }

            // PASO 4: Llamar al MODEL para eliminar
            boolean eliminado = model.delete(id);

            // PASO 5: Mostrar resultado
            if (eliminado) {
                view.mostrarMensaje("✓ Usuario eliminado exitosamente");
            } else {
                view.mostrarError("✗ No se pudo eliminar el usuario");
            }

        } catch (IllegalArgumentException e) {
            view.mostrarError("Error: " + e.getMessage());

        } catch (SQLException e) {
            view.mostrarError("Error al eliminar: " + e.getMessage());
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONCEPTOS CLAVE DEL CONTROLADOR EN MVC - RESUMEN EDUCATIVO
// ═══════════════════════════════════════════════════════════════════════════════

// 1. RESPONSABILIDADES DEL CONTROLADOR:
//    ✅ COORDINAR Model y View (no hace el trabajo, delega)
//    ✅ RECIBIR eventos del usuario (a través de View)
//    ✅ DECIDIR qué hacer según el evento
//    ✅ ACTUALIZAR Model cuando hay cambios de datos
//    ✅ PEDIR a View que actualice la interfaz
//    ✅ MANEJAR excepciones y mostrar errores
//
//    ❌ NO debe:
//    - Tener código SQL (eso es del Model)
//    - Crear componentes gráficos (eso es de View)
//    - Acceder directamente a la BD

// 2. FLUJO DE COMUNICACIÓN:
//    Usuario → View → Controller → Model → Controller → View → Usuario
//
//    EJEMPLO:
//    Usuario clica "Crear" → View captura →
//    Controller pide datos a View →
//    Controller crea objeto User →
//    Controller llama a model.save() →
//    Model valida y guarda en BD →
//    Model retorna resultado a Controller →
//    Controller pide a View mostrar mensaje →
//    View muestra "Usuario creado" al Usuario

// 3. MANEJO DE EXCEPCIONES EN EL CONTROLADOR:
//    try {
//        // Operación que puede fallar
//    } catch (IllegalArgumentException e) {
//        // Error de validación (del Model)
//        view.mostrarError("Validación: " + e.getMessage());
//    } catch (SQLException e) {
//        // Error de base de datos
//        view.mostrarError("BD: " + e.getMessage());
//    }
//
//    El Controller es quien decide CÓMO mostrar los errores
//    (usando la View, claro)

// 4. PATRÓN DE DELEGACIÓN:
//    El Controller NO hace el trabajo pesado, DELEGA:
//    - Delega validaciones al Model
//    - Delega presentación a la View
//    - Solo COORDINA entre ambos
//
//    Es como un director de orquesta:
//    - No toca instrumentos (Model/View lo hacen)
//    - Solo coordina cuándo y cómo tocar

// 5. RETORNO null COMO SEÑAL DE CANCELACIÓN:
//    La View retorna null cuando el usuario cancela
//    El Controller verifica esto y aborta la operación:
//
//    String nombre = view.pedirNombre();
//    if (nombre == null) return; // Salir del método
//
//    Esto evita crear objetos parciales o incompletos

// 6. MÉTODOS PRIVADOS vs PÚBLICOS:
//    - iniciar(): PÚBLICO (llamado desde Main)
//    - crearUsuario(), listarUsuarios(), etc.: PRIVADOS
//      · Son métodos internos del Controller
//      · Solo iniciar() debe ser público

// 7. VENTAJAS DE SEPARAR EN MÉTODOS:
//    - Cada método hace UNA cosa (Single Responsibility)
//    - Código más legible
//    - Fácil de mantener y debuggear
//    - Cada método es una "historia" completa

// 8. DIFERENCIA CON ARQUITECTURA EN CAPAS:
//    CAPAS (layered-architecture):
//    Main → Service → DAO → DB
//    (Main es UI + Controller mezclados)
//
//    MVC:
//    Main solo inicializa → Controller coordina → Model (lógica+BD) / View (UI)
//    (Separación más clara de presentación)

// 9. ¿CUÁNDO USAR MVC?
//    ✅ Aplicaciones con interfaz gráfica (desktop, web)
//    ✅ Proyectos pequeños-medianos
//    ✅ Cuando la lógica de negocio es simple
//    ✅ Cuando se prioriza rapidez de desarrollo
//
//    ¿CUÁNDO USAR CAPAS?
//    ✅ Proyectos empresariales grandes
//    ✅ Lógica de negocio compleja
//    ✅ Necesidad de alta escalabilidad
//    ✅ Equipos grandes

// 10. PATRÓN DE COMPOSICIÓN:
//     Controller "tiene-un" Model (has-a)
//     Controller "tiene-una" View (has-a)
//
//     Esto es composición, no herencia
//     Es más flexible que heredar
