# 📋 Sistema CRUD de Usuarios con Patrón MVC

Un sistema completo de gestión de usuarios (CRUD) desarrollado en Java puro utilizando JDBC y el **patrón MVC (Model-View-Controller)**. Este proyecto demuestra cómo organizar código de forma simple y efectiva para aplicaciones con interfaz gráfica.

---

## 📚 Tabla de Contenidos

1. [Descripción General](#-descripción-general)
2. [Patrón MVC Explicado](#-patrón-mvc-explicado)
3. [Estructura del Proyecto](#-estructura-del-proyecto)
4. [Requisitos Previos](#-requisitos-previos)
5. [Instalación y Configuración](#-instalación-y-configuración)
6. [Flujo de Desarrollo Paso a Paso](#-flujo-de-desarrollo-paso-a-paso)
7. [Flujo de Ejecución](#-flujo-de-ejecución)
8. [Uso del Sistema](#-uso-del-sistema)
9. [Comparación: MVC vs Arquitectura en Capas](#-comparación-mvc-vs-arquitectura-en-capas)
10. [Conceptos Clave Implementados](#-conceptos-clave-implementados)
11. [Problemas Comunes y Soluciones](#-problemas-comunes-y-soluciones)

---

## 🎯 Descripción General

Este proyecto implementa el **patrón MVC (Model-View-Controller)** para crear un sistema CRUD de usuarios. Es ideal para:

- ✅ Aprender el patrón MVC desde cero
- ✅ Entender separación de responsabilidades
- ✅ Ver JDBC puro sin frameworks
- ✅ Desarrollar aplicaciones desktop simples
- ✅ Proyectos educativos y prototipos rápidos

---

## 🏗️ Patrón MVC Explicado

### ¿Qué es MVC?

**MVC** es un patrón de diseño que separa una aplicación en **3 componentes principales**:

```
┌─────────────────────────────────────────────────────────┐
│                   MODEL (Modelo)                        │
│                                                          │
│  - Maneja DATOS y LÓGICA DE NEGOCIO                    │
│  - Interactúa con la base de datos                     │
│  - Valida información                                  │
│  - NO sabe nada de la interfaz gráfica                 │
│                                                          │
│  UserModel.java                                         │
└──────────────────────┬──────────────────────────────────┘
                       │
                       │ Notifica cambios
                       ▼
┌─────────────────────────────────────────────────────────┐
│              CONTROLLER (Controlador)                   │
│                                                          │
│  - COORDINA Model y View                               │
│  - Recibe eventos del usuario desde View              │
│  - Actualiza Model con nuevos datos                    │
│  - Pide a View que se actualice                        │
│                                                          │
│  UserController.java                                    │
└──────────────────────┬──────────────────────────────────┘
                       │
                       │ Actualiza interfaz
                       ▼
┌─────────────────────────────────────────────────────────┐
│                    VIEW (Vista)                         │
│                                                          │
│  - Muestra INTERFAZ GRÁFICA al usuario                 │
│  - Captura eventos (clics, inputs)                     │
│  - Envía eventos al Controller                         │
│  - NO tiene lógica de negocio                          │
│                                                          │
│  UserView.java                                          │
└─────────────────────────────────────────────────────────┘
```

### Flujo de Comunicación

```
Usuario → View → Controller → Model → Controller → View → Usuario
```

**Ejemplo concreto:**
1. Usuario clica "Crear Usuario" → **View** captura el evento
2. **View** pide nombre, email, edad al usuario
3. **View** llama a `controller.crearUsuario()` con los datos
4. **Controller** crea objeto `User`
5. **Controller** llama a `model.save(user)`
6. **Model** valida datos, conecta a BD, ejecuta INSERT
7. **Model** retorna `true` o `false` al **Controller**
8. **Controller** pide a **View** mostrar mensaje de éxito/error
9. **View** muestra el mensaje al usuario

---

## 📁 Estructura del Proyecto

```
example_jdbc/
│
├── lib/
│   └── mysql-connector-j-9.4.0.jar    # Driver JDBC de MySQL
│
├── src/
│   ├── config/
│   │   ├── DatabaseConfig.java        # Gestión de conexiones
│   │   └── database.properties        # Credenciales de BD
│   │
│   ├── model/
│   │   ├── User.java                  # POJO (entidad)
│   │   └── UserModel.java             # MODEL (lógica + BD)
│   │
│   ├── view/
│   │   └── UserView.java              # VIEW (interfaz JOptionPane)
│   │
│   ├── controller/
│   │   └── UserController.java        # CONTROLLER (coordina)
│   │
│   └── Main.java                      # Punto de entrada
│
└── README.md
```

### Responsabilidades de cada componente:

| Archivo | Componente MVC | Responsabilidad |
|---------|---------------|-----------------|
| `UserModel.java` | **Model** | Validaciones, lógica de negocio, SQL, acceso a BD |
| `UserView.java` | **View** | Interfaz gráfica (JOptionPane), capturar inputs, mostrar mensajes |
| `UserController.java` | **Controller** | Coordinar Model y View, manejar flujo, procesar eventos |
| `User.java` | Entidad | POJO (datos del usuario) |
| `DatabaseConfig.java` | Configuración | Gestionar conexiones MySQL |
| `Main.java` | Inicialización | Crear Controller e iniciar app |

---

## 🔧 Requisitos Previos

- **Java JDK 8+**
  ```bash
  java -version
  javac -version
  ```

- **MySQL Server 5.7+**
  ```bash
  mysql --version
  ```

- **MySQL Connector/J** (incluido en `lib/`)

---

## 🚀 Instalación y Configuración

### Paso 1: Clonar y navegar al proyecto

```bash
git clone <URL_DEL_REPOSITORIO>
cd example_jdbc
git checkout mvc-pattern
```

### Paso 2: Crear base de datos

```sql
CREATE DATABASE latte_camilo;
USE latte_camilo;

CREATE TABLE users(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    edad INT NOT NULL
);
```

### Paso 3: Configurar credenciales

Editar `src/config/database.properties`:

```properties
DB_URL=jdbc:mysql://TU_IP:TU_PUERTO/latte_camilo
DB_USER=tu_usuario
DB_PASSWORD=tu_contraseña
```

### Paso 4: Compilar

**Linux/Mac:**
```bash
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/model/*.java
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/view/*.java
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/controller/*.java
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/Main.java
```

**Windows:**
```cmd
javac -cp ".;lib/mysql-connector-j-9.4.0.jar" src/model/*.java
javac -cp ".;lib/mysql-connector-j-9.4.0.jar" src/view/*.java
javac -cp ".;lib/mysql-connector-j-9.4.0.jar" src/controller/*.java
javac -cp ".;lib/mysql-connector-j-9.4.0.jar" src/Main.java
```

### Paso 5: Ejecutar

**Linux/Mac:**
```bash
java -cp ".:lib/mysql-connector-j-9.4.0.jar" src.Main
```

**Windows:**
```cmd
java -cp ".;lib/mysql-connector-j-9.4.0.jar" src.Main
```

---

## 🛠️ Flujo de Desarrollo Paso a Paso

### **Fase 1: Entidad (User.java)**

**Objetivo:** Definir la estructura de datos

- Crear POJO con atributos: `id`, `name`, `email`, `age`
- Implementar 3 constructores (vacío, sin ID, completo)
- Getters y setters para encapsulación

**Resultado:** Objeto Java que mapea a la tabla `users`

---

### **Fase 2: Configuración (DatabaseConfig.java)**

**Objetivo:** Gestionar conexiones MySQL

- Cargar propiedades desde `database.properties`
- Proveer `getConnection()` y `closeConnection()`
- Usar bloque estático para inicializar una sola vez

**Resultado:** Sistema centralizado de conexiones

---

### **Fase 3: MODEL (UserModel.java)**

**Objetivo:** Implementar lógica de negocio y acceso a datos

**Métodos implementados:**
- `save(User)` → INSERT
- `findById(int)` → SELECT WHERE id
- `findAll()` → SELECT *
- `update(User)` → UPDATE SET
- `delete(int)` → DELETE

**Validaciones incluidas:**
- Campos no vacíos
- Email con @
- Edad entre 1-150

**Resultado:** Model completo con CRUD y validaciones

---

### **Fase 4: VIEW (UserView.java)**

**Objetivo:** Crear interfaz gráfica con JOptionPane

**Métodos implementados:**
- `mostrarMenu()` → Menú principal
- `pedirNombre()` → Input con validación do-while
- `pedirEmail()` → Input con validación do-while
- `pedirEdad()` → Input con validación do-while
- `pedirId()` → Input numérico
- `mostrarUsuarios(List)` → Listar usuarios
- `mostrarUsuario(User)` → Mostrar un usuario
- `mostrarMensaje(String)` → Mensaje de información
- `mostrarError(String)` → Mensaje de error
- `confirmarAccion(String)` → Diálogo Sí/No

**Validaciones de formato:**
- Campos no vacíos
- Valores numéricos
- Formato básico de email

**Resultado:** Vista completa con todas las interacciones de usuario

---

### **Fase 5: CONTROLLER (UserController.java)**

**Objetivo:** Coordinar Model y View

**Métodos implementados:**
- `iniciar()` → Bucle principal del menú
- `crearUsuario()` → Coordina creación
- `listarUsuarios()` → Coordina listado
- `buscarUsuario()` → Coordina búsqueda
- `actualizarUsuario()` → Coordina actualización
- `eliminarUsuario()` → Coordina eliminación

**Flujo típico de cada método:**
1. Pedir datos a View
2. Si cancela → salir
3. Crear/obtener objeto User
4. Llamar a Model para procesar
5. Pedir a View mostrar resultado

**Resultado:** Controller que orquesta todo el flujo

---

### **Fase 6: MAIN (Main.java)**

**Objetivo:** Inicializar la aplicación

```java
public static void main(String[] args) {
    UserController controller = new UserController();
    controller.iniciar();
}
```

**Resultado:** Main limpio de solo 2 líneas

---

## 🔄 Flujo de Ejecución

### Crear Usuario (CREATE)

```
1. Usuario selecciona "1 - Crear Usuario" en menú
   ↓
2. VIEW.mostrarMenu() retorna 1 al CONTROLLER
   ↓
3. CONTROLLER.crearUsuario() se ejecuta
   ↓
4. CONTROLLER pide datos a VIEW:
   - nombre = VIEW.pedirNombre()
   - email = VIEW.pedirEmail()
   - edad = VIEW.pedirEdad()
   ↓
5. CONTROLLER crea: User user = new User(nombre, email, edad)
   ↓
6. CONTROLLER llama: boolean ok = MODEL.save(user)
   ↓
7. MODEL valida datos
   ↓
8. MODEL conecta a BD y ejecuta INSERT
   ↓
9. MODEL retorna true/false al CONTROLLER
   ↓
10. CONTROLLER pide a VIEW mostrar resultado:
    - Si true: VIEW.mostrarMensaje("Usuario creado")
    - Si false: VIEW.mostrarError("No se pudo crear")
    ↓
11. Vuelve al menú
```

### Listar Usuarios (READ)

```
1. Usuario selecciona "2 - Listar Usuarios"
   ↓
2. CONTROLLER.listarUsuarios() se ejecuta
   ↓
3. CONTROLLER llama: List<User> usuarios = MODEL.findAll()
   ↓
4. MODEL ejecuta SELECT * FROM users
   ↓
5. MODEL retorna lista de usuarios al CONTROLLER
   ↓
6. CONTROLLER llama: VIEW.mostrarUsuarios(usuarios)
   ↓
7. VIEW muestra todos los usuarios en diálogo
```

---

## 📖 Uso del Sistema

### Menú Principal

Al ejecutar la aplicación, aparece:

```
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
```

### Validaciones Implementadas

#### En la Vista (formato):
- ✅ Campos no vacíos
- ✅ Números son numéricos
- ✅ Email contiene @

#### En el Model (negocio):
- ✅ Edad entre 1-150
- ✅ Email con formato válido
- ✅ Datos consistentes

### Re-ingreso de Campos

Si un campo es inválido, **solo se repite ese campo**, no todos:

```
Ingrese nombre: Juan
Ingrese email: juan
❌ El email debe contener @

Ingrese email: juan@example.com  ← Solo repite email
Ingrese edad: 25

✓ Usuario creado exitosamente
```

---

## 🔀 Comparación: MVC vs Arquitectura en Capas

### Arquitectura en Capas (rama `layered-architecture`)

```
┌─────────────────────────────┐
│  Main.java (500+ líneas)    │  ← UI + parte del Controller
├─────────────────────────────┤
│  UserService.java           │  ← Validaciones de negocio
├─────────────────────────────┤
│  UserDAOImpl.java           │  ← SQL y acceso a datos
├─────────────────────────────┤
│  User.java                  │  ← Solo datos (POJO)
└─────────────────────────────┘
```

**Características:**
- ✅ Alta separación de responsabilidades
- ✅ Ideal para proyectos grandes
- ✅ Fácil escalar
- ❌ Más archivos y complejidad
- ❌ Main muy largo

### Patrón MVC (esta rama)

```
┌─────────────────────────────┐
│  Main.java (20 líneas)      │  ← Solo inicializa
├─────────────────────────────┤
│  UserController.java        │  ← Coordina todo
├─────────────────────────────┤
│  UserView.java              │  ← Toda la UI
├─────────────────────────────┤
│  UserModel.java             │  ← Lógica + SQL
├─────────────────────────────┤
│  User.java                  │  ← Entidad
└─────────────────────────────┘
```

**Características:**
- ✅ Más simple y directo
- ✅ Ideal para proyectos pequeños-medianos
- ✅ Main súper limpio
- ✅ Desarrollo rápido
- ❌ Model tiene muchas responsabilidades
- ❌ Menos escalable que Capas

### ¿Cuál usar?

| Criterio | MVC | Capas |
|----------|-----|-------|
| **Tamaño del proyecto** | Pequeño-Mediano | Grande-Empresarial |
| **Equipo** | 1-3 personas | 4+ personas |
| **Complejidad lógica** | Simple-Moderada | Compleja |
| **Tiempo desarrollo** | Rápido | Más tiempo |
| **Escalabilidad** | Moderada | Alta |
| **Curva aprendizaje** | Baja | Media |

**Recomendación:**
- 🎓 **Aprende primero MVC** (esta rama) → Más fácil de entender
- 📚 **Luego estudia Capas** (rama `layered-architecture`) → Más profesional

---

## 💡 Conceptos Clave Implementados

### 1. Separación de Responsabilidades

**Model:**
- ✅ Validaciones de negocio
- ✅ Conexión a BD
- ✅ Ejecución de SQL
- ❌ NO sabe de JOptionPane

**View:**
- ✅ Mostrar diálogos
- ✅ Capturar inputs
- ✅ Validaciones de formato
- ❌ NO sabe de SQL

**Controller:**
- ✅ Coordinar Model y View
- ✅ Manejar flujo
- ✅ Procesar eventos
- ❌ NO tiene SQL ni JOptionPane

### 2. JDBC Puro

**PreparedStatement:**
```java
String sql = "INSERT INTO users (name, email, edad) VALUES (?, ?, ?)";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, name);
pstmt.setString(2, email);
pstmt.setInt(3, edad);
pstmt.executeUpdate();
```

**Ventajas:**
- ✅ Previene SQL Injection
- ✅ Mejor rendimiento
- ✅ Código más limpio

### 3. Validación en Capas

**View (formato):**
```java
if (nombre.trim().isEmpty()) {
    // Error: campo vacío
}
```

**Model (negocio):**
```java
if (edad < 1 || edad > 150) {
    throw new IllegalArgumentException("Edad inválida");
}
```

### 4. Bucles do-while para Re-ingreso

```java
do {
    edad = pedirEdad();

    if (esInvalido) {
        mostrarError();
        continue; // Volver a pedir
    }

    valido = true;
} while (!valido);
```

### 5. Manejo de Excepciones

```java
try {
    model.save(user);
} catch (IllegalArgumentException e) {
    view.mostrarError("Validación: " + e.getMessage());
} catch (SQLException e) {
    view.mostrarError("BD: " + e.getMessage());
}
```

---

## 🔍 Problemas Comunes y Soluciones

### Problema 1: Error de conexión MySQL

**Error:**
```
SQLException: Access denied for user 'root'@'localhost'
```

**Solución:**
- Verificar `database.properties`
- Verificar que MySQL esté corriendo
- Verificar permisos del usuario

### Problema 2: ClassNotFoundException

**Error:**
```
ClassNotFoundException: com.mysql.cj.jdbc.Driver
```

**Solución:**
- Verificar que `mysql-connector-j-9.4.0.jar` esté en `lib/`
- Incluir en classpath: `-cp ".:lib/mysql-connector-j-9.4.0.jar"`

### Problema 3: No compila (clase no encontrada)

**Error:**
```
cannot find symbol: class UserModel
```

**Solución:**
Compilar en orden:
1. Model → 2. View → 3. Controller → 4. Main

### Problema 4: Column 'edad' not found

**Solución:**
Tu tabla debe tener columna `edad` (no `age`):
```sql
CREATE TABLE users(
    ...
    edad INT NOT NULL
);
```

---

## 👨‍💻 Autor

Proyecto educativo para demostrar el patrón MVC con JDBC puro.

---

## 📄 Licencia

Código abierto - Fines educativos

---

## 🔗 Ver También

- **Rama `layered-architecture`**: Implementación con Arquitectura en Capas
- **Rama `main`**: Documentación general del proyecto

```bash
# Ver arquitectura en capas
git checkout layered-architecture

# Volver a MVC
git checkout mvc-pattern
```

---

**¡Happy Coding!** 🚀
