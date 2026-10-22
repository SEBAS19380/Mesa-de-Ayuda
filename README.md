# Mesa de Ayuda

Aplicación web para registrar, organizar y dar seguimiento a solicitudes de soporte mediante tickets. El sistema integra gestión de usuarios, autenticación basada en sesión, búsqueda, paginación, preferencias visuales y manejo personalizado de errores.

## Características

### Gestión de usuarios

- Registro de cuentas.
- Validación de campos obligatorios.
- Validación del formato del correo electrónico.
- Confirmación de contraseña.
- Prevención de correos duplicados.
- Inicio y cierre de sesión.
- Visualización del usuario activo.

### Gestión de tickets

- Creación de tickets.
- Consulta del listado general.
- Edición y actualización.
- Eliminación.
- Validación de formularios.
- Mensajes de confirmación después de operaciones exitosas.
- Asociación de cada ticket con el usuario que lo creó.

Cada ticket contiene:

- Identificador.
- Título.
- Descripción.
- Prioridad.
- Responsable.
- Usuario asociado.

### Prioridades

Las prioridades se almacenan como valores numéricos:

```text
1 = BAJA
2 = MEDIA
3 = ALTA
```

La interfaz transforma estos valores en etiquetas visuales para facilitar su identificación.

### Búsqueda y paginación

- Búsqueda parcial por título, descripción o responsable.
- Búsqueda sin distinción entre mayúsculas y minúsculas.
- Resultados independientes del listado principal.
- Acciones de edición y eliminación desde los resultados.
- Listado principal paginado en bloques de cinco tickets.
- Navegación mediante página anterior, página siguiente y números de página.

### Sesiones

El sistema utiliza `HttpSession` para conservar al usuario autenticado durante la navegación.

- La sesión almacena el usuario activo.
- Las rutas de tickets requieren una sesión válida.
- El cierre de sesión invalida los datos activos mediante `session.invalidate()`.
- La sesión expira después de 30 minutos de inactividad.
- La cookie de sesión se configura como `HttpOnly`.

### Preferencia visual

El usuario puede seleccionar un tema claro u oscuro. La preferencia se guarda en una cookie llamada `tema` durante siete días.

```text
tema=claro
tema=oscuro
```

La cookie contiene únicamente la preferencia visual y permanece después de cerrar sesión.

### Manejo de errores

La aplicación proporciona una página personalizada para errores HTTP:

- `404`: recurso o página no encontrada.
- `500`: error interno del servidor.
- `403`: acceso no permitido.
- Otros errores HTTP.

La página presenta mensajes comprensibles, conserva el tema seleccionado y ofrece opciones para regresar al inicio o al módulo de tickets.

### Registro de operaciones

Spring AOP registra la ejecución de los servicios mediante:

- `@Before`
- `@AfterReturning`
- `@AfterThrowing`

El registro permite observar el inicio, finalización y fallo de operaciones sin agregar lógica de logging directamente en cada servicio.

## Tecnologías

- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Validation
- Spring AOP
- Thymeleaf
- Maven
- MySQL
- HTML5
- CSS3
- Git

## Arquitectura

La aplicación utiliza una arquitectura por capas:

```text
Cliente web
    ↓
Thymeleaf / HTML
    ↓
Controller
    ↓
Service
    ↓
Repository
    ↓
MySQL
```

### Estructura principal

```text
src/
└── main/
    ├── java/sd/proyecto/mesadeayuda/
    │   ├── aspect/
    │   ├── controller/
    │   ├── DTO/
    │   ├── model/
    │   ├── repository/
    │   └── service/
    └── resources/
        ├── static/css/
        ├── templates/
        └── application.properties
```

## Modelo de datos

### Usuario

```text
id
nombre
correo
password
```

### Ticket

```text
id
titulo
descripcion
prioridad
responsable
usuario_id
```

### Relación

```text
Usuario 1 ───── N Ticket
```

Un usuario puede crear varios tickets y cada ticket pertenece a un usuario.

```java
@ManyToOne
@JoinColumn(name = "usuario_id")
private Usuario usuario;
```

```java
@OneToMany(mappedBy = "usuario")
private List<Ticket> tickets;
```

## Requisitos

Antes de ejecutar el proyecto, se necesita:

- JDK 21.
- Maven 3.9 o superior.
- MySQL 8 o una versión compatible.
- Puerto `8080` disponible para la aplicación.
- Puerto `3307` disponible para MySQL, o ajustar la URL de conexión.

## Configuración

### 1. Crear la base de datos

Ejecutar en MySQL:

```sql
CREATE DATABASE mesadeayuda;
```

Hibernate crea y actualiza las tablas mediante:

```properties
spring.jpa.hibernate.ddl-auto=update
```

### 2. Configurar variables de entorno

La configuración recomendada para `application.properties` es:

```properties
spring.application.name=mesadeayuda
server.port=8080

spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3307/mesadeayuda}
spring.datasource.username=
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.servlet.session.timeout=30m
server.servlet.session.cookie.http-only=true
```

### 3. Iniciar la aplicación

```bash
mvn spring-boot:run
```

También se puede limpiar, compilar e iniciar en un solo comando:

```bash
mvn clean spring-boot:run
```

La aplicación estará disponible en:

```text
http://localhost:8080
```

## Rutas principales

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/` | Página principal |
| `GET` | `/registro` | Formulario de registro |
| `POST` | `/registro` | Creación de usuario |
| `GET` | `/login` | Formulario de inicio de sesión |
| `POST` | `/login` | Validación de credenciales |
| `POST` | `/logout` | Cierre de sesión |
| `GET` | `/tickets` | Listado, búsqueda y paginación |
| `POST` | `/tickets/agregar` | Creación de ticket |
| `GET` | `/tickets/editar/{id}` | Carga del formulario de edición |
| `POST` | `/tickets/actualizar/{id}` | Actualización de ticket |
| `POST` | `/tickets/eliminar/{id}` | Eliminación de ticket |
| `POST` | `/preferencia/tema` | Actualización del tema visual |
| `GET` | `/api/tickets` | Consulta REST de tickets |

## Validaciones

La aplicación utiliza DTO y Jakarta Validation:

```java
@NotBlank
@NotNull
@Email
@Valid
```

Los controladores procesan los resultados con `BindingResult`. Cuando existen errores:

- La operación se cancela.
- Se conservan los datos diligenciados.
- Se muestran mensajes junto al campo correspondiente.
- El formulario permanece en modo creación o edición según corresponda.

## Interfaz

La vista principal de tickets utiliza un diseño de tres paneles:

```text
┌─────────────────┬──────────────────────────┬─────────────────┐
│ Crear / editar  │ Listado paginado        │ Buscar tickets │
│ ticket          │ de tickets              │ y resultados   │
└─────────────────┴──────────────────────────┴─────────────────┘
```

Las vistas se dividen en fragmentos Thymeleaf para separar responsabilidades:

```text
tickets.html
formulario-ticket.html
listado-tickets.html
busqueda-tickets.html
```

## Pruebas funcionales


### Usuarios y sesiones

- Registrar una cuenta válida.
- Rechazar correos inválidos o duplicados.
- Rechazar contraseñas que no coincidan.
- Iniciar sesión con credenciales válidas.
- Rechazar credenciales incorrectas.
- Mantener la sesión durante la navegación.
- Cerrar sesión e impedir el acceso posterior a `/tickets`.

### Tickets

- Crear un ticket válido.
- Rechazar formularios incompletos.
- Editar y actualizar un ticket.
- Mantener los valores cuando falla una validación.
- Eliminar un ticket.
- Confirmar los mensajes de éxito.

### Búsqueda y paginación

- Buscar por título, descripción y responsable.
- Buscar un valor inexistente.
- Verificar que la búsqueda no cambie la tabla principal.
- Mostrar cinco registros por página.
- Navegar entre páginas.
- Verificar el comportamiento sin registros.

### Sesiones, cookies y errores

- Verificar el usuario activo.
- Seleccionar tema claro y oscuro.
- Confirmar la cookie `tema` en las herramientas del navegador.
- Cerrar sesión y comprobar que la preferencia visual permanezca.
- Acceder a una ruta inexistente y comprobar el error 404.
- Comprobar la página personalizada para errores 500.

## Seguridad

La aplicación evita guardar preferencias o contraseñas dentro de cookies personalizadas. La cookie `tema` contiene solamente la preferencia visual.
