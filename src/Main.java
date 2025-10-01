package src;

// ═══════════════════════════════════════════════════════════════════════════════
// IMPORTACIONES
// ═══════════════════════════════════════════════════════════════════════════════

import src.controller.UserController;
// UserController: Controlador del patrón MVC
// Es quien coordina Model y View

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * Main - Punto de entrada de la aplicación MVC
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * PATRÓN MVC - ROL DEL MAIN:
 *
 * En el patrón MVC, el Main tiene una ÚNICA responsabilidad:
 * - INICIALIZAR el Controller y dejarlo funcionar
 *
 * TODO el flujo de la aplicación está en el Controller:
 *
 *   ┌──────────────────────────────────────────────────────┐
 *   │                  Main.java                           │
 *   │                                                       │
 *   │  public static void main(String[] args) {           │
 *   │      UserController controller = new UserController();│
 *   │      controller.iniciar();                          │
 *   │  }                                                   │
 *   │                                                       │
 *   │  ¡Eso es todo! Solo 2 líneas.                      │
 *   └───────────────────┬──────────────────────────────────┘
 *                       │
 *                       │ Delega todo al Controller
 *                       ▼
 *   ┌──────────────────────────────────────────────────────┐
 *   │            UserController.java                       │
 *   │                                                       │
 *   │  - Maneja el menú                                   │
 *   │  - Coordina Model y View                            │
 *   │  - Procesa eventos del usuario                      │
 *   │  - Maneja excepciones                               │
 *   └──────────────────────────────────────────────────────┘
 *
 * DIFERENCIA CON ARQUITECTURA EN CAPAS:
 *
 * CAPAS (layered-architecture branch):
 * Main tiene TODO el código de:
 * - Menú
 * - Captura de datos
 * - Validaciones de interfaz
 * - Llamadas al Service
 * - Manejo de excepciones
 * - Mensajes al usuario
 * ¡Main tiene 500+ líneas!
 *
 * MVC (esta rama):
 * Main solo inicializa el Controller
 * ¡Main tiene menos de 20 líneas!
 * Todo está delegado a:
 * - Controller (coordina)
 * - View (interfaz)
 * - Model (lógica y datos)
 *
 * VENTAJAS DE ESTE ENFOQUE:
 * ✅ Main es SIMPLE y LIMPIO
 * ✅ Fácil de entender qué hace la aplicación
 * ✅ Toda la complejidad está ORGANIZADA en MVC
 * ✅ Si cambias la UI, Main no cambia
 * ✅ Si cambias la lógica, Main no cambia
 * ✅ Main solo cambia si cambias el patrón arquitectónico completo
 */
public class Main {

    /**
     * main() - Método de entrada de la aplicación
     *
     * RESPONSABILIDAD:
     * - Crear una instancia del Controller
     * - Llamar a iniciar() para arrancar la aplicación
     * - ¡Nada más!
     *
     * @param args - Argumentos de línea de comandos (no se usan)
     */
    public static void main(String[] args) {

        // ══════════════════════════════════════════════════════════════════════
        // PASO 1: Crear instancia del Controller
        // ══════════════════════════════════════════════════════════════════════

        // El Controller, al crearse, automáticamente:
        // 1. Crea una instancia de UserModel (lógica + BD)
        // 2. Crea una instancia de UserView (interfaz gráfica)
        UserController controller = new UserController();

        // ══════════════════════════════════════════════════════════════════════
        // PASO 2: Iniciar la aplicación
        // ══════════════════════════════════════════════════════════════════════

        // A partir de aquí, el Controller toma el control:
        // - Muestra el menú (usando View)
        // - Procesa opciones del usuario
        // - Coordina Model y View
        // - Maneja todo el flujo de la aplicación
        controller.iniciar();

        // ══════════════════════════════════════════════════════════════════════
        // FIN - Cuando el usuario elija "Salir", iniciar() termina y el programa
        // se cierra automáticamente
        // ══════════════════════════════════════════════════════════════════════
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONCEPTOS CLAVE - RESUMEN EDUCATIVO
// ═══════════════════════════════════════════════════════════════════════════════

// 1. SIMPLICIDAD DEL MAIN EN MVC:
//    - Main NO debe tener lógica de negocio
//    - Main NO debe tener código de interfaz
//    - Main solo INICIALIZA y DELEGA
//
//    Piensa en Main como el "switch" que enciende la aplicación
//    Todo lo demás pasa en el Controller

// 2. FLUJO COMPLETO DE LA APLICACIÓN MVC:
//
//    Main.main()
//      ↓
//    Crea UserController
//      ↓
//    controller.iniciar()
//      ↓
//    [BUCLE INFINITO hasta que usuario salga]
//      ↓
//    Controller muestra menú (usando View)
//      ↓
//    Usuario elige opción
//      ↓
//    Controller ejecuta método correspondiente:
//      - crearUsuario()
//      - listarUsuarios()
//      - buscarUsuario()
//      - actualizarUsuario()
//      - eliminarUsuario()
//      ↓
//    Cada método:
//      1. Pide datos a View
//      2. Llama al Model para procesar
//      3. Pide a View que muestre resultado
//      ↓
//    Vuelve al menú
//      ↓
//    [Repetir hasta que elija "Salir"]
//      ↓
//    Aplicación termina

// 3. COMPARACIÓN: MVC vs CAPAS
//
//    ┌─────────────────────────────────────────────────────┐
//    │           ARQUITECTURA EN CAPAS                      │
//    ├─────────────────────────────────────────────────────┤
//    │                                                      │
//    │  Main.java (UI + Controller mezclados)             │
//    │    ├── Muestra menú con JOptionPane                │
//    │    ├── Captura datos                               │
//    │    ├── Valida formato                              │
//    │    ├── Llama a UserService                         │
//    │    └── Muestra resultados                          │
//    │                                                      │
//    │  UserService.java (Lógica de negocio)              │
//    │    ├── Valida datos de negocio                     │
//    │    └── Llama a UserDAO                             │
//    │                                                      │
//    │  UserDAOImpl.java (Acceso a datos)                 │
//    │    ├── Ejecuta SQL                                 │
//    │    └── Maneja conexiones                           │
//    │                                                      │
//    │  User.java (Solo datos - POJO)                     │
//    │                                                      │
//    └─────────────────────────────────────────────────────┘
//
//    ┌─────────────────────────────────────────────────────┐
//    │                 PATRÓN MVC                          │
//    ├─────────────────────────────────────────────────────┤
//    │                                                      │
//    │  Main.java (Solo inicializa)                       │
//    │    └── Crea Controller y llama a iniciar()        │
//    │                                                      │
//    │  UserController.java (Coordina)                    │
//    │    ├── Maneja menú y flujo                        │
//    │    ├── Recibe eventos de View                     │
//    │    ├── Llama a Model para datos                   │
//    │    └── Pide a View que actualice                  │
//    │                                                      │
//    │  UserView.java (Interfaz)                          │
//    │    ├── Muestra menú y diálogos                    │
//    │    ├── Captura inputs                             │
//    │    ├── Valida formato                             │
//    │    └── Muestra resultados                         │
//    │                                                      │
//    │  UserModel.java (Lógica + Datos)                   │
//    │    ├── Valida negocio                             │
//    │    ├── Ejecuta SQL                                │
//    │    └── Maneja conexiones                          │
//    │                                                      │
//    │  User.java (Entidad - POJO)                        │
//    │                                                      │
//    └─────────────────────────────────────────────────────┘

// 4. ¿CUÁNDO USAR CADA PATRÓN?
//
//    USA CAPAS SI:
//    ✅ Proyecto grande/empresarial
//    ✅ Equipo de desarrollo grande
//    ✅ Lógica de negocio muy compleja
//    ✅ Necesitas alta escalabilidad
//    ✅ Quieres separación máxima de responsabilidades
//
//    USA MVC SI:
//    ✅ Proyecto pequeño-mediano
//    ✅ Desarrollo rápido
//    ✅ Aplicación con interfaz gráfica
//    ✅ Lógica de negocio simple
//    ✅ Prefieres simplicidad sobre escalabilidad extrema

// 5. VENTAJAS DE TENER UN MAIN SIMPLE:
//    ✅ Fácil de entender
//    ✅ Fácil de testear (solo crea Controller)
//    ✅ Fácil de mantener
//    ✅ Bajo acoplamiento (Main no depende de detalles)
//    ✅ Alta cohesión (Main hace una sola cosa)

// 6. SEPARACIÓN DE RESPONSABILIDADES EN MVC:
//
//    Main:      "Iniciar la aplicación"
//    Controller: "Coordinar Model y View según eventos del usuario"
//    View:       "Mostrar interfaz y capturar inputs"
//    Model:      "Manejar lógica de negocio y acceso a datos"
//
//    Cada clase tiene UNA responsabilidad clara (Single Responsibility Principle)

// 7. ¿POR QUÉ ESTE MAIN ES TAN CORTO?
//    Porque TODO el código que estaba en Main (arquitectura en capas)
//    ahora está distribuido en:
//    - Controller: Flujo y coordinación
//    - View: Interfaz y captura de datos
//    - Model: Lógica y persistencia
//
//    Esto NO es "hacer trampa", es ORGANIZAR mejor el código

// 8. PATRÓN DE INICIALIZACIÓN:
//    Main → Controller → Model + View
//
//    Main crea Controller
//    Controller crea Model y View en su constructor
//    Todo está listo para funcionar

// 9. PUNTO DE ENTRADA ÚNICO:
//    Toda aplicación Java necesita un método main()
//    Este es el ÚNICO punto de entrada
//    A partir de ahí, el flujo está controlado por el patrón MVC

// 10. CONCLUSIÓN:
//     Un Main simple y limpio es señal de un buen diseño
//     La complejidad NO desaparece, se ORGANIZA en el patrón MVC
