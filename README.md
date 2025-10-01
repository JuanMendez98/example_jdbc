# 📋 Sistema CRUD de Usuarios - Arquitecturas Java

Este repositorio contiene dos implementaciones del mismo sistema CRUD de usuarios con JDBC y MySQL, cada una siguiendo un patrón arquitectónico diferente.

## 🌿 Ramas Disponibles

### 1. `layered-architecture` - Arquitectura en Capas
Implementación usando **Layered Architecture** con patrón DAO.

**Estructura:**
- **Model**: Entidades (POJOs)
- **Config**: Configuración de BD
- **DAO**: Acceso a datos
- **Service**: Lógica de negocio
- **UI**: Interfaz de usuario

[📖 Ver documentación completa](../../tree/layered-architecture)

```bash
git checkout layered-architecture
```

### 2. `mvc-pattern` - Patrón MVC
Implementación usando **Model-View-Controller**.

**Estructura:**
- **Model**: Lógica de negocio + acceso a datos
- **View**: Interfaz de usuario (JOptionPane)
- **Controller**: Coordinación entre Model y View

[📖 Ver documentación completa](../../tree/mvc-pattern)

```bash
git checkout mvc-pattern
```

---

## 🎯 Comparación de Arquitecturas

| Aspecto | Layered Architecture | MVC Pattern |
|---------|---------------------|-------------|
| **Capas** | 5 capas (Model, Config, DAO, Service, UI) | 3 componentes (Model, View, Controller) |
| **Separación** | Mayor separación de responsabilidades | Más simple y directo |
| **Complejidad** | Media-Alta | Baja-Media |
| **Escalabilidad** | Excelente para proyectos grandes | Ideal para proyectos pequeños-medianos |
| **Mantenibilidad** | Muy fácil cambiar BD o UI | Moderada |
| **Caso de uso** | Aplicaciones empresariales | Aplicaciones simples/educativas |

---

## ✨ Características Comunes

Ambas implementaciones incluyen:

✅ **CRUD Completo**
- Crear usuarios
- Listar usuarios
- Buscar por ID
- Actualizar usuarios
- Eliminar usuarios

✅ **Validaciones Robustas**
- Campos no vacíos
- Formato de email (@)
- Rango de edad (1-150)
- Re-ingreso individual de campos inválidos

✅ **Seguridad**
- PreparedStatement (prevención SQL Injection)
- Configuración externa (.properties)
- Manejo de excepciones

✅ **Interfaz Gráfica**
- JOptionPane para UI simple
- Menú interactivo
- Mensajes de error descriptivos

---

## 🚀 Inicio Rápido

### Requisitos
- Java JDK 8+
- MySQL Server 5.7+
- MySQL Connector/J (incluido en `lib/`)

### Configuración

1. **Clonar repositorio**
```bash
git clone <URL_REPOSITORIO>
cd example_jdbc
```

2. **Crear base de datos**
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

3. **Configurar credenciales**
Editar `src/config/database.properties`:
```properties
DB_URL=jdbc:mysql://TU_IP:TU_PUERTO/latte_camilo
DB_USER=tu_usuario
DB_PASSWORD=tu_contraseña
```

4. **Elegir una arquitectura**

**Opción A: Layered Architecture**
```bash
git checkout layered-architecture
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/model/*.java
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/config/*.java
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/dao/*.java
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/service/*.java
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/Main.java
java -cp ".:lib/mysql-connector-j-9.4.0.jar" src.Main
```

**Opción B: MVC Pattern**
```bash
git checkout mvc-pattern
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/model/*.java
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/view/*.java
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/controller/*.java
javac -cp ".:lib/mysql-connector-j-9.4.0.jar" src/Main.java
java -cp ".:lib/mysql-connector-j-9.4.0.jar" src.Main
```

---

## 📚 ¿Qué Arquitectura Elegir?

### Elige **Layered Architecture** si:
- 🏢 Estás construyendo una aplicación empresarial
- 📈 Planeas escalar el proyecto
- 🔄 Necesitas cambiar fácilmente la BD o UI
- 👥 Trabajas en equipo grande
- 📖 Quieres aprender patrones profesionales

### Elige **MVC** si:
- 🎓 Estás aprendiendo patrones básicos
- ⚡ Necesitas desarrollo rápido
- 📦 Tu proyecto es pequeño/mediano
- 🎯 Prefieres simplicidad sobre flexibilidad
- 🔰 Es tu primer proyecto con patrones

---

## 🛠️ Conceptos Aprendidos

Ambas ramas te enseñan:
- ✅ JDBC puro (sin frameworks)
- ✅ PreparedStatement vs Statement
- ✅ Manejo de recursos (try-catch-finally)
- ✅ Validaciones robustas
- ✅ Separación de responsabilidades
- ✅ Configuración externa
- ✅ Patrones de diseño

---

## 👨‍💻 Autor

Proyecto educativo para demostrar diferentes arquitecturas en Java.

---

## 📄 Licencia

Código abierto - Fines educativos

---

**¡Explora ambas ramas y aprende las diferencias!** 🚀