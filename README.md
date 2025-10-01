# 📋 Sistema CRUD de Usuarios con JDBC

Un sistema completo de gestión de usuarios (CRUD) desarrollado en Java puro utilizando JDBC para la conexión directa con MySQL, sin frameworks ni gestores de dependencias. Este proyecto implementa una arquitectura en capas con interfaz gráfica basada en `JOptionPane`.

---

## 📚 Tabla de Contenidos

1. [Descripción General](#-descripción-general)
2. [Características](#-características)
3. [Arquitectura del Proyecto](#-arquitectura-del-proyecto)
4. [Requisitos Previos](#-requisitos-previos)
5. [Instalación y Configuración](#-instalación-y-configuración)
6. [Estructura del Proyecto](#-estructura-del-proyecto)
7. [Flujo de Desarrollo Paso a Paso](#-flujo-de-desarrollo-paso-a-paso)
8. [Flujo de Ejecución](#-flujo-de-ejecución)
9. [Uso del Sistema](#-uso-del-sistema)
10. [Patrones de Diseño Utilizados](#-patrones-de-diseño-utilizados)
11. [Conceptos Clave Implementados](#-conceptos-clave-implementados)
12. [Problemas Comunes y Soluciones](#-problemas-comunes-y-soluciones)
13. [Próximas Mejoras](#-próximas-mejoras)

---

## 🎯 Descripción General

Este proyecto es una aplicación educativa que demuestra cómo implementar un sistema CRUD (Create, Read, Update, Delete) completo utilizando **JDBC puro** sin frameworks como Spring o Hibernate. Está diseñado para comprender los fundamentos de:

- Conexión directa a bases de datos MySQL
- Uso de `PreparedStatement` para prevenir inyección SQL
- Arquitectura en capas (Model-DAO-Service-UI)
- Manejo de recursos con bloques `try-catch-finally`
- Validaciones robustas con bucles `do-while`
- Interfaz gráfica simple con `JOptionPane`

---

## ✨ Características

✅ **CRUD Completo**
- Crear nuevos usuarios
- Listar todos los usuarios
- Buscar usuario por ID
- Actualizar datos de usuarios existentes
- Eliminar usuarios

✅ **Validaciones Robustas**
- Validación de campos vacíos
- Validación de formato de email (debe contener `@`)
- Validación de rango de edad (1-150)
- Validación de tipos de datos (edad debe ser numérica)
- Re-ingreso automático en caso de error (sin perder datos previos)

✅ **Arquitectura Limpia**
- Separación de responsabilidades en capas
- Patrón DAO (Data Access Object)
- Configuración externa mediante archivo `.properties`
- Código ampliamente comentado con fines educativos

✅ **Seguridad Básica**
- Uso de `PreparedStatement` para prevenir SQL Injection
- Contraseñas en archivo de configuración separado
- Manejo adecuado de excepciones

---

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una **arquitectura en capas** (Layered Architecture):

```
┌─────────────────────────────────────┐
│         UI Layer (Main.java)        │  ← Interfaz de usuario con JOptionPane
├─────────────────────────────────────┤
│   Service Layer (UserService.java)  │  ← Lógica de negocio y validaciones
├─────────────────────────────────────┤
│   DAO Layer (UserDAOImpl.java)      │  ← Acceso a datos (SQL)
├─────────────────────────────────────┤
│  Config Layer (DatabaseConfig.java) │  ← Configuración de BD
├─────────────────────────────────────┤
│   Model Layer (User.java)           │  ← Entidades (POJO)
└─────────────────────────────────────┘
           ↓
    ┌──────────────┐
    │ MySQL Server │
    └──────────────┘
```

### Descripción de Capas:

1. **Model (Modelo)**: Define las entidades de datos (POJOs)
2. **Config (Configuración)**: Gestiona la conexión a la base de datos
3. **DAO (Data Access Object)**: Contiene toda la lógica SQL y acceso a datos
4. **Service (Servicio)**: Implementa lógica de negocio y validaciones
5. **UI (Interfaz de Usuario)**: Presenta información al usuario y captura entrada

---

## 🔧 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Java JDK 8 o superior**
  ```bash
  java -version
  javac -version
  ```

- **MySQL Server 5.7 o superior**
  ```bash
  mysql --version
  ```

- **MySQL Connector/J** (incluido en `lib/mysql-connector-j-9.4.0.jar`)

---

## 🚀 Instalación y Configuración

### Paso 1: Clonar el Repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd example_jdbc
```

### Paso 2: Configurar la Base de Datos

Conectarse a MySQL y crear la base de datos y tabla:

```sql
-- Crear base de datos
CREATE DATABASE latte_camilo;

-- Usar la base de datos
USE latte_camilo;

-- Crear tabla de usuarios
CREATE TABLE users(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    edad INT NOT NULL
);
```

### Paso 3: Configurar Conexión

Editar el archivo `src/config/database.properties` con tus credenciales:

```properties
DB_URL=jdbc:mysql://TU_IP:TU_PUERTO/latte_camilo
DB_USER=tu_usuario
DB_PASSWORD=tu_contraseña
```

**Ejemplo:**
```properties
DB_URL=jdbc:mysql://168.119.183.3:3307/latte_camilo
DB_USER=root
DB_PASSWORD=g0tIFJEQsKHm5$34Pxu1
```

### Paso 4: Compilar el Proyecto

En Linux/Mac:
```bash
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/model/*.java
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/config/*.java
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/dao/*.java
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/service/*.java
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/Main.java
```

En Windows:
```cmd
javac -cp ".;lib/mysql-connector-j-9.4.0.jar" src/model/*.java
javac -cp ".;lib/mysql-connector-j-9.4.0.jar" src/config/*.java
javac -cp ".;lib/mysql-connector-j-9.4.0.jar" src/dao/*.java
javac -cp ".;lib/mysql-connector-j-9.4.0.jar" src/service/*.java
javac -cp ".;lib/mysql-connector-j-9.4.0.jar" src/Main.java
```

### Paso 5: Ejecutar la Aplicación

En Linux/Mac:
```bash
java -cp ".:lib/mysql-connector-j-9.4.0.jar" src.Main
```

En Windows:
```cmd
java -cp ".;lib/mysql-connector-j-9.4.0.jar" src.Main
```

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
│   │   └── User.java                  # POJO de Usuario
│   │
│   ├── dao/
│   │   ├── UserDAO.java               # Interfaz DAO
│   │   └── UserDAOImpl.java           # Implementación DAO
│   │
│   ├── service/
│   │   └── UserService.java           # Lógica de negocio
│   │
│   └── Main.java                      # Punto de entrada (UI)
│
└── README.md                          # Este archivo
```

---

## 🛠️ Flujo de Desarrollo Paso a Paso

Este proyecto se desarrolló siguiendo una metodología **bottom-up** (de abajo hacia arriba), comenzando por las capas más fundamentales y subiendo hacia la interfaz de usuario.

### **Fase 1: Definición del Modelo de Datos** 📝

**Objetivo:** Crear la estructura de datos que representará a un usuario.

#### Paso 1.1: Crear la clase `User.java`
- **Ubicación:** `src/model/User.java`
- **Propósito:** Definir un POJO (Plain Old Java Object) con los atributos del usuario
- **Atributos definidos:**
  - `id` (int): Identificador único
  - `name` (String): Nombre del usuario
  - `email` (String): Correo electrónico
  - `age` (int): Edad del usuario

#### Paso 1.2: Implementar Constructores
- Constructor vacío (para crear objetos sin datos)
- Constructor sin ID (para usuarios nuevos que aún no tienen ID en BD)
- Constructor completo (para usuarios existentes con ID)

#### Paso 1.3: Implementar Getters y Setters
- Aplicar principio de **encapsulación**
- Todos los atributos privados con acceso mediante métodos públicos

**Resultado:** Un objeto Java que mapea directamente a la tabla `users` de MySQL.

---

### **Fase 2: Configuración de Base de Datos** 🗄️

**Objetivo:** Establecer la conexión con MySQL de forma segura y configurable.

#### Paso 2.1: Crear archivo de propiedades
- **Ubicación:** `src/config/database.properties`
- **Contenido:** URL de conexión, usuario y contraseña
- **Ventaja:** Separar credenciales del código fuente

#### Paso 2.2: Crear clase `DatabaseConfig.java`
- **Ubicación:** `src/config/DatabaseConfig.java`
- **Funciones principales:**
  - Cargar propiedades desde archivo `.properties`
  - Proveer método `getConnection()` para obtener conexiones
  - Proveer método `closeConnection()` para cerrar recursos

#### Paso 2.3: Implementar bloque estático
- Cargar configuración una sola vez cuando la clase se inicializa
- Usar `try-with-resources` para manejar archivos

**Resultado:** Sistema de conexión centralizado y reutilizable.

---

### **Fase 3: Capa de Acceso a Datos (DAO)** 💾

**Objetivo:** Abstraer todas las operaciones SQL en una capa dedicada.

#### Paso 3.1: Definir interfaz `UserDAO.java`
- **Ubicación:** `src/dao/UserDAO.java`
- **Métodos definidos:**
  ```java
  void save(User user)
  User findById(int id)
  List<User> findAll()
  void update(User user)
  void delete(int id)
  ```
- **Ventaja:** Contrato claro que cualquier implementación debe cumplir

#### Paso 3.2: Implementar `UserDAOImpl.java`
- **Ubicación:** `src/dao/UserDAOImpl.java`
- **Implementaciones SQL:**

**CREATE (Insertar):**
```java
String sql = "INSERT INTO users (name, email, edad) VALUES (?, ?, ?)";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, user.getName());
pstmt.setString(2, user.getEmail());
pstmt.setInt(3, user.getAge());
pstmt.executeUpdate();
```

**READ (Leer por ID):**
```java
String sql = "SELECT id, name, email, edad FROM users WHERE id = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setInt(1, id);
ResultSet rs = pstmt.executeQuery();
```

**READ (Leer todos):**
```java
String sql = "SELECT id, name, email, edad FROM users";
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(sql);
```

**UPDATE (Actualizar):**
```java
String sql = "UPDATE users SET name = ?, email = ?, edad = ? WHERE id = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, user.getName());
pstmt.setString(2, user.getEmail());
pstmt.setInt(3, user.getAge());
pstmt.setInt(4, user.getId());
pstmt.executeUpdate();
```

**DELETE (Eliminar):**
```java
String sql = "DELETE FROM users WHERE id = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setInt(1, id);
pstmt.executeUpdate();
```

#### Paso 3.3: Gestión de Recursos
- Usar bloques `finally` para cerrar `ResultSet`, `Statement`, `Connection`
- Garantizar que los recursos se liberen incluso si hay excepciones

**Resultado:** Capa de acceso a datos completa y segura contra SQL Injection.

---

### **Fase 4: Capa de Servicio (Business Logic)** 🎯

**Objetivo:** Implementar validaciones y lógica de negocio antes de acceder a datos.

#### Paso 4.1: Crear clase `UserService.java`
- **Ubicación:** `src/service/UserService.java`
- **Propósito:** Validar datos antes de enviarlos al DAO

#### Paso 4.2: Implementar Validaciones
- **Validación de campos vacíos:**
  ```java
  if (user.getName() == null || user.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre no puede estar vacío");
  }
  ```

- **Validación de formato de email:**
  ```java
  if (!user.getEmail().contains("@")) {
      throw new IllegalArgumentException("El email debe contener @");
  }
  ```

- **Validación de rango de edad:**
  ```java
  if (user.getAge() < 1 || user.getAge() > 150) {
      throw new IllegalArgumentException("La edad debe estar entre 1 y 150");
  }
  ```

#### Paso 4.3: Delegar operaciones al DAO
- Si las validaciones pasan, llamar a los métodos del DAO
- Propagar excepciones SQL hacia arriba

**Resultado:** Separación clara entre validaciones (Service) y persistencia (DAO).

---

### **Fase 5: Interfaz de Usuario** 🖥️

**Objetivo:** Crear una interfaz gráfica simple con `JOptionPane`.

#### Paso 5.1: Crear menú principal
- **Ubicación:** `src/Main.java`
- **Opciones:**
  1. Crear Usuario
  2. Listar Usuarios
  3. Buscar Usuario por ID
  4. Actualizar Usuario
  5. Eliminar Usuario
  6. Salir

#### Paso 5.2: Implementar validaciones con `do-while`
- **Problema inicial:** Si un campo era inválido, se perdían todos los datos ingresados
- **Solución:** Cada campo tiene su propio bucle `do-while` que repite solo ese campo

**Ejemplo de validación de edad:**
```java
boolean edadValida = false;
do {
    edadStr = JOptionPane.showInputDialog("Ingrese la edad:");

    // Validar que no esté vacío
    if (edadStr.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "La edad no puede estar vacía");
        continue;
    }

    // Validar que sea numérico
    try {
        edad = Integer.parseInt(edadStr.trim());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "La edad debe ser un número");
        continue;
    }

    // Validar rango
    if (edad <= 0) {
        JOptionPane.showMessageDialog(null, "La edad debe ser mayor a 0");
        continue;
    }

    if (edad > 150) {
        JOptionPane.showMessageDialog(null, "La edad debe ser menor o igual a 150");
        continue;
    }

    edadValida = true;
} while (!edadValida);
```

#### Paso 5.3: Implementar operaciones CRUD en UI
- **Crear:** Capturar datos → Validar → Llamar a `userService.save()`
- **Listar:** Llamar a `userService.findAll()` → Mostrar en cuadro de diálogo
- **Buscar:** Pedir ID → Llamar a `userService.findById()` → Mostrar datos
- **Actualizar:** Buscar usuario → Capturar nuevos datos → Llamar a `userService.update()`
- **Eliminar:** Pedir ID → Confirmar → Llamar a `userService.delete()`

**Resultado:** Aplicación completa y funcional con interfaz gráfica.

---

### **Fase 6: Pruebas y Ajustes** 🧪

#### Paso 6.1: Compilación
- Compilar cada capa en orden (model → config → dao → service → main)
- Resolver errores de sintaxis y referencias

#### Paso 6.2: Pruebas de Conexión
- Verificar que `database.properties` tenga credenciales correctas
- Probar conexión con MySQL

#### Paso 6.3: Pruebas de CRUD
- Crear varios usuarios
- Listar y verificar que aparezcan
- Actualizar datos
- Eliminar usuarios
- Verificar consistencia en la base de datos

#### Paso 6.4: Ajuste de Nombres de Columnas
- **Problema encontrado:** El código usaba `age` pero la BD tenía `edad`
- **Solución:** Cambiar todas las referencias SQL de `age` a `edad`
- **Archivos modificados:** `UserDAOImpl.java`

**Resultado:** Sistema CRUD completamente funcional y probado.

---

## 🔄 Flujo de Ejecución

### Flujo de Creación de Usuario (CREATE)

```
┌──────────────┐
│   Usuario    │
│  (Main.java) │
└──────┬───────┘
       │ 1. Ingresa datos (nombre, email, edad)
       │    con validaciones do-while
       ▼
┌─────────────────┐
│  UserService    │
│ (Validaciones)  │
└──────┬──────────┘
       │ 2. Valida:
       │    - Campos no vacíos
       │    - Email con @
       │    - Edad entre 1-150
       ▼
┌──────────────────┐
│   UserDAOImpl    │
│  (Acceso a BD)   │
└──────┬───────────┘
       │ 3. Ejecuta:
       │    INSERT INTO users (name, email, edad) VALUES (?, ?, ?)
       ▼
┌──────────────────┐
│  MySQL Database  │
│   (users table)  │
└──────────────────┘
```

### Flujo de Lectura de Usuarios (READ)

```
┌──────────────┐
│   Usuario    │
│  (Main.java) │
└──────┬───────┘
       │ 1. Selecciona "Listar Usuarios"
       ▼
┌──────────────────┐
│  UserService     │
└──────┬───────────┘
       │ 2. Llama a findAll()
       ▼
┌──────────────────┐
│   UserDAOImpl    │
└──────┬───────────┘
       │ 3. Ejecuta:
       │    SELECT id, name, email, edad FROM users
       ▼
┌──────────────────┐
│  MySQL Database  │
└──────┬───────────┘
       │ 4. Retorna ResultSet
       ▼
┌──────────────────┐
│   UserDAOImpl    │
└──────┬───────────┘
       │ 5. Convierte ResultSet a List<User>
       ▼
┌──────────────────┐
│  UserService     │
└──────┬───────────┘
       │ 6. Retorna lista
       ▼
┌──────────────┐
│   Usuario    │
│ (Muestra en  │
│  JOptionPane)│
└──────────────┘
```

### Flujo de Actualización (UPDATE)

```
Usuario → Ingresa ID → UserService.findById() → UserDAOImpl
                                                     ↓
                                              SELECT WHERE id = ?
                                                     ↓
                                              MySQL retorna datos
                                                     ↓
Usuario ← Muestra datos actuales ← UserService ← UserDAOImpl
   ↓
Usuario ingresa nuevos datos con validaciones
   ↓
UserService valida nuevos datos
   ↓
UserDAOImpl ejecuta:
UPDATE users SET name = ?, email = ?, edad = ? WHERE id = ?
   ↓
MySQL actualiza registro
```

---

## 📖 Uso del Sistema

### Crear un Usuario

1. Ejecutar la aplicación
2. Seleccionar opción `1 - Crear Usuario`
3. Ingresar datos:
   - **Nombre:** No puede estar vacío
   - **Email:** Debe contener `@`
   - **Edad:** Debe ser un número entre 1 y 150
4. Si algún dato es inválido, el sistema pedirá reingresar **solo ese campo**
5. Una vez validado, el usuario se guarda en la BD

### Listar Usuarios

1. Seleccionar opción `2 - Listar Usuarios`
2. El sistema muestra todos los usuarios en formato:
   ```
   ID: 1
   Nombre: Juan Pérez
   Email: juan@example.com
   Edad: 25
   -------------------------
   ID: 2
   Nombre: María García
   Email: maria@example.com
   Edad: 30
   ```

### Buscar Usuario por ID

1. Seleccionar opción `3 - Buscar Usuario`
2. Ingresar ID del usuario
3. Si existe, muestra sus datos
4. Si no existe, muestra mensaje de error

### Actualizar Usuario

1. Seleccionar opción `4 - Actualizar Usuario`
2. Ingresar ID del usuario a actualizar
3. El sistema muestra datos actuales
4. Ingresar nuevos datos (con las mismas validaciones que en Crear)
5. Los datos se actualizan en la BD

### Eliminar Usuario

1. Seleccionar opción `5 - Eliminar Usuario`
2. Ingresar ID del usuario a eliminar
3. Confirmar eliminación
4. El usuario se elimina de la BD

---

## 🎨 Patrones de Diseño Utilizados

### 1. **DAO (Data Access Object)**
- **Propósito:** Abstraer y encapsular todo acceso a la fuente de datos
- **Implementación:**
  - Interfaz `UserDAO` define el contrato
  - `UserDAOImpl` implementa la lógica SQL
- **Ventaja:** Cambiar de MySQL a PostgreSQL solo requiere una nueva implementación del DAO

### 2. **Singleton (Implícito en DatabaseConfig)**
- **Propósito:** Garantizar una única configuración de BD
- **Implementación:** Métodos estáticos y bloque estático de inicialización
- **Ventaja:** Evita múltiples cargas del archivo de propiedades

### 3. **Layered Architecture (Arquitectura en Capas)**
- **Propósito:** Separar responsabilidades en capas independientes
- **Capas:**
  - Presentación (Main)
  - Lógica de Negocio (UserService)
  - Acceso a Datos (UserDAOImpl)
  - Modelo (User)
  - Configuración (DatabaseConfig)

### 4. **POJO (Plain Old Java Object)**
- **Propósito:** Objetos simples sin dependencias de frameworks
- **Implementación:** Clase `User` con solo atributos, constructores, getters y setters

---

## 💡 Conceptos Clave Implementados

### 1. **JDBC (Java Database Connectivity)**
API de Java para ejecutar operaciones en bases de datos relacionales.

**Componentes principales:**
- `DriverManager`: Gestiona drivers de bases de datos
- `Connection`: Representa una conexión a la BD
- `Statement/PreparedStatement`: Ejecuta consultas SQL
- `ResultSet`: Almacena resultados de consultas SELECT

### 2. **PreparedStatement vs Statement**

**Statement (NO recomendado):**
```java
String sql = "SELECT * FROM users WHERE id = " + userId; // ❌ Vulnerable a SQL Injection
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(sql);
```

**PreparedStatement (RECOMENDADO):**
```java
String sql = "SELECT * FROM users WHERE id = ?"; // ✅ Seguro
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setInt(1, userId);
ResultSet rs = pstmt.executeQuery();
```

**Ventajas de PreparedStatement:**
- ✅ Previene SQL Injection
- ✅ Mejor rendimiento (precompilado)
- ✅ Más legible y mantenible

### 3. **Try-Catch-Finally**

**Gestión de recursos:**
```java
Connection conn = null;
PreparedStatement pstmt = null;
try {
    conn = DatabaseConfig.getConnection();
    pstmt = conn.prepareStatement(sql);
    // ... operaciones
} catch (SQLException e) {
    // Manejo de errores
} finally {
    // SIEMPRE se ejecuta, incluso si hay excepción
    if (pstmt != null) pstmt.close();
    if (conn != null) conn.close();
}
```

**Importancia del bloque `finally`:**
- Garantiza liberación de recursos
- Previene memory leaks
- Se ejecuta incluso si hay `return` en `try` o `catch`

### 4. **ResultSet (Cursor de Resultados)**

Un `ResultSet` es como un cursor que recorre las filas retornadas por un `SELECT`:

```java
ResultSet rs = pstmt.executeQuery();
while (rs.next()) { // Mueve el cursor a la siguiente fila
    int id = rs.getInt("id");
    String name = rs.getString("name");
    String email = rs.getString("email");
    int age = rs.getInt("edad");
}
```

**Conceptos:**
- `rs.next()`: Mueve el cursor a la siguiente fila (retorna `false` si no hay más)
- `rs.getInt("columna")`: Obtiene valor de la columna como entero
- `rs.getString("columna")`: Obtiene valor como String

### 5. **Validaciones con Do-While**

**Patrón implementado:**
```java
boolean valido = false;
do {
    // 1. Capturar entrada
    String input = JOptionPane.showInputDialog("Ingrese dato:");

    // 2. Validar
    if (/* condición de validez */) {
        valido = true;
    } else {
        // 3. Mostrar error y repetir
        JOptionPane.showMessageDialog(null, "Error: ...");
    }
} while (!valido);
```

**Ventaja:** El usuario solo reingresa el campo inválido, no todos los datos.

### 6. **Properties (Archivo de Configuración)**

**Formato del archivo `.properties`:**
```properties
clave1=valor1
clave2=valor2
```

**Carga en Java:**
```java
Properties props = new Properties();
FileInputStream fis = new FileInputStream("archivo.properties");
props.load(fis);
String valor = props.getProperty("clave1");
```

**Ventajas:**
- Separar configuración del código
- Facilitar cambios sin recompilar
- Mantener credenciales fuera del control de versiones

---

## 🔍 Problemas Comunes y Soluciones

### Problema 1: Error de Conexión a MySQL

**Error:**
```
SQLException: Access denied for user 'root'@'localhost'
```

**Solución:**
- Verificar credenciales en `database.properties`
- Verificar que MySQL esté ejecutándose: `sudo systemctl status mysql`
- Verificar permisos del usuario en MySQL

### Problema 2: ClassNotFoundException

**Error:**
```
java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver
```

**Solución:**
- Verificar que `mysql-connector-j-9.4.0.jar` esté en la carpeta `lib/`
- Incluir el JAR en el classpath al compilar y ejecutar:
  ```bash
  -cp ".:lib/mysql-connector-j-9.4.0.jar"
  ```

### Problema 3: SQLSyntaxErrorException (Unknown column 'age')

**Error:**
```
SQLSyntaxErrorException: Unknown column 'age' in 'field list'
```

**Causa:**
El código usaba `age` pero la tabla tiene `edad`.

**Solución:**
Cambiar todas las referencias SQL de `age` a `edad` en `UserDAOImpl.java`.

### Problema 4: Pérdida de Datos al Validar

**Problema:**
Si la edad era inválida, el usuario tenía que reingresar nombre y email también.

**Solución:**
Implementar validación `do-while` individual para cada campo:
```java
// Validar nombre
do { /* validaciones */ } while (!nombreValido);

// Validar email
do { /* validaciones */ } while (!emailValido);

// Validar edad
do { /* validaciones */ } while (!edadValida);
```

### Problema 5: Paquetes en Java

**Problema:**
Confusión entre `package config` y `package src.config`.

**Explicación:**
- Si `DatabaseConfig.java` está en `src/config/DatabaseConfig.java`
- Y se compila desde la raíz del proyecto
- El package debe ser `package config;` (sin `src`)
- Pero si Main está en `src/` y usa `src.config`, entonces debe ser `package src.config;`

**Regla:**
El `package` refleja la estructura de carpetas **desde donde se compila**.

---

## 🚀 Próximas Mejoras

### Funcionalidades
- [ ] Paginación en listado de usuarios
- [ ] Búsqueda por nombre o email
- [ ] Ordenamiento de resultados
- [ ] Exportar datos a CSV
- [ ] Logs de operaciones

### Seguridad
- [ ] Encriptación de contraseñas en `database.properties`
- [ ] Validación de email con expresiones regulares más robustas
- [ ] Manejo de inyección SQL en campos de texto
- [ ] Auditoría de cambios (quién modificó qué y cuándo)

### Arquitectura
- [ ] Migrar a Maven/Gradle para gestión de dependencias
- [ ] Implementar pool de conexiones (HikariCP)
- [ ] Usar try-with-resources en lugar de finally
- [ ] Implementar logging con Log4j o SLF4J
- [ ] Añadir pruebas unitarias con JUnit

### Interfaz
- [ ] Migrar de JOptionPane a JavaFX o Swing completo
- [ ] Añadir interfaz web con Servlets/JSP
- [ ] Crear API REST con Spring Boot
- [ ] Implementar paginación en UI

---

## 👨‍💻 Autor

Desarrollado con fines educativos para demostrar:
- Fundamentos de JDBC
- Arquitectura en capas
- Patrones de diseño (DAO, Layered Architecture)
- Buenas prácticas de validación y seguridad

---

## 📄 Licencia

Este proyecto es de código abierto y está disponible para fines educativos.

---

## 🙏 Agradecimientos

Este proyecto fue desarrollado como material educativo para comprender:
- Los fundamentos de JDBC sin abstracciones de frameworks
- La importancia de la arquitectura en capas
- Cómo implementar validaciones robustas
- El uso correcto de recursos de bases de datos

**¡Happy Coding!** 🚀
