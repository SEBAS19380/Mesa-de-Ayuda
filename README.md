# Mesa de Ayuda

Aplicación web para registrar, organizar y dar seguimiento a solicitudes de soporte mediante tickets.

El sistema incluye gestión de usuarios, autenticación basada en sesión, protección centralizada de rutas, búsqueda, paginación, preferencias visuales, manejo personalizado de errores y una API REST CRUD para administrar tickets mediante solicitudes HTTP y respuestas JSON.

## Características

### Gestión de usuarios

- Registro de cuentas.
- Validación de campos obligatorios.
- Validación del formato del correo electrónico.
- Confirmación de contraseña.
- Prevención de correos duplicados.
- Inicio y cierre de sesión.
- Visualización del usuario activo.

### Centro de atención

Después de iniciar sesión, el usuario accede a un centro de atención con dos opciones:

- Abrir Microsoft Copilot para obtener orientación inicial.
- Ingresar al sistema de tickets para registrar y gestionar solicitudes.

La opción de Microsoft Copilot utiliza un enlace externo y no requiere una API, un SDK ni credenciales adicionales.

### Gestión de tickets

- Creación de tickets.
- Consulta del listado general.
- Edición y actualización.
- Eliminación.
- Validación de formularios.
- Mensajes después de operaciones exitosas.
- Asociación de cada ticket con un usuario.
- Búsqueda por título, descripción o responsable.
- Paginación del listado principal.
- API REST para administrar tickets mediante JSON.

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
- Las rutas privadas requieren una sesión válida.
- El cierre de sesión invalida los datos activos mediante `session.invalidate()`.
- La sesión expira después de 30 minutos de inactividad.
- La cookie de sesión se configura como `HttpOnly`.

### Protección de rutas

La aplicación utiliza un `HandlerInterceptor` para centralizar el control de acceso.

Las siguientes rutas requieren una sesión válida:

```text
/menu
/tickets
/tickets/**
/preferencia/**
```

La protección se registra mediante `WebMvcConfigurer`:

```java
registry.addInterceptor(loginInterceptor)
        .addPathPatterns(
                "/menu",
                "/tickets",
                "/tickets/**",
                "/preferencia/**");
```

Las rutas de la API REST no utilizan este interceptor porque deben ser accesibles directamente desde Postman y responder con JSON, no con una redirección al formulario de inicio de sesión.

### Preferencia visual

El usuario puede seleccionar un tema claro u oscuro desde el centro de atención.

La preferencia se guarda en una cookie llamada `tema`:

```text
tema=claro
tema=oscuro
```

Características de la cookie:

- Duración de siete días.
- Disponible en toda la aplicación mediante la ruta `/`.
- Configurada como `HttpOnly`.
- Validación de los valores permitidos.
- Permanece después de cerrar sesión.
- No almacena contraseñas ni información sensible.

### Manejo de errores

La aplicación proporciona una página personalizada para errores HTTP del sitio web:

- `404`: recurso o página no encontrada.
- `500`: error interno del servidor.
- `403`: acceso no permitido.
- Otros errores HTTP.

La página presenta mensajes comprensibles, conserva el tema seleccionado y ofrece opciones para regresar al inicio o al sistema de tickets.

La API REST utiliza un manejador de excepciones independiente basado en `@RestControllerAdvice`, que devuelve errores en formato JSON.

### Registro de operaciones

Spring AOP registra la ejecución de los servicios mediante:

- `@Before`
- `@AfterReturning`
- `@AfterThrowing`

El registro permite observar el inicio, finalización y fallo de operaciones sin agregar lógica de logging directamente en cada servicio.

## Tecnologías

- Java 21
- Spring Boot 4.1.1
- Spring MVC
- Spring Data JPA
- Spring Validation
- Spring AOP
- Thymeleaf
- Bootstrap 5
- Maven
- MySQL
- HTML5
- CSS3
- Postman
- Git

## Arquitectura

La aplicación utiliza una arquitectura por capas:

```text
Cliente web o Postman
          ↓
Controller MVC o REST Controller
          ↓
Service
          ↓
Repository
          ↓
MySQL
```

### Interfaz web

```text
Navegador
    ↓
Thymeleaf + Bootstrap
    ↓
@Controller
    ↓
Service
    ↓
Repository
    ↓
MySQL
```

### API REST

```text
Postman
    ↓
Solicitud HTTP + JSON
    ↓
@RestController
    ↓
Service
    ↓
Repository
    ↓
MySQL
    ↓
ResponseEntity + JSON
```

## Estructura principal

```text
src/
└── main/
    ├── java/sd/proyecto/mesadeayuda/
    │   ├── aspect/
    │   ├── config/
    │   ├── controller/
    │   ├── DTO/
    │   ├── exception/
    │   ├── interceptor/
    │   ├── model/
    │   ├── repository/
    │   └── service/
    └── resources/
        ├── static/css/
        ├── templates/
        └── application.properties
```

### Componentes de la API

```text
controller/
└── TicketRestController.java

DTO/
├── TicketApiRequestDTO.java
└── TicketApiResponseDTO.java

exception/
├── ApiExceptionHandler.java
├── TicketNoEncontradoException.java
└── UsuarioNoEncontradoException.java
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

En `Ticket`:

```java
@ManyToOne
@JoinColumn(name = "usuario_id")
private Usuario usuario;
```

En `Usuario`:

```java
@OneToMany(mappedBy = "usuario")
private List<Ticket> tickets;
```

## Diferencia entre sesión, cookie y base de datos

### Sesión

La sesión conserva temporalmente al usuario activo en el servidor.

Finaliza cuando:

- Se ejecuta `session.invalidate()`.
- Se supera el tiempo de inactividad configurado.
- La sesión deja de ser válida en el servidor.

### Cookie

La cookie se almacena en el navegador y contiene una preferencia pequeña.

En este proyecto guarda solamente:

```text
tema=claro
```

o:

```text
tema=oscuro
```

### Base de datos

MySQL almacena información persistente:

- Usuarios.
- Tickets.
- Relación entre usuarios y tickets.

Cerrar sesión o eliminar la cookie no elimina los registros de MySQL.

## Requisitos

Antes de ejecutar el proyecto se necesita:

- JDK 21.
- Maven 3.9 o superior.
- MySQL 8 o una versión compatible.
- Postman para probar la API.
- Puerto `8080` disponible para Spring Boot.
- Puerto `3307` disponible para MySQL, o modificar la URL de conexión.

## Configuración

### Crear la base de datos

Ejecutar en MySQL:

```sql
CREATE DATABASE mesadeayuda;
```

Hibernate crea y actualiza las tablas mediante:

```properties
spring.jpa.hibernate.ddl-auto=update
```

### Variables de entorno

La configuración recomendada para `application.properties` es:

```properties
spring.application.name=mesadeayuda

server.port=8080

logging.level.sd.proyecto.mesadeayuda=DEBUG

spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3307/mesadeayuda}
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.servlet.session.timeout=30m
server.servlet.session.cookie.http-only=true
```

La contraseña de MySQL no debe incluirse directamente en el repositorio.

#### PowerShell

Variable temporal:

```powershell
$env:DB_PASSWORD="contraseña_mysql"
```

Variable persistente:

```powershell
setx DB_PASSWORD "contraseña_mysql"
```

Después de utilizar `setx`, se debe abrir una terminal nueva.

#### Git Bash

```bash
export DB_PASSWORD='contraseña_mysql'
```

## Instalación y ejecución

### Obtener el proyecto

```bash
git clone URL_DEL_REPOSITORIO
cd mesadeayuda
```

Reemplazar `URL_DEL_REPOSITORIO` por la dirección real del repositorio.

### Ejecutar las pruebas

```bash
mvn clean test
```

### Iniciar la aplicación

```bash
mvn spring-boot:run
```

También se puede limpiar e iniciar con:

```bash
mvn clean spring-boot:run
```

La aplicación estará disponible en:

```text
http://localhost:8080
```

## Rutas de la aplicación web

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/` | Página principal |
| `GET` | `/registro` | Formulario de registro |
| `POST` | `/registro` | Creación de usuario |
| `GET` | `/login` | Formulario de inicio de sesión |
| `POST` | `/login` | Validación de credenciales |
| `POST` | `/logout` | Cierre de sesión |
| `GET` | `/menu` | Centro de atención |
| `GET` | `/tickets` | Listado, búsqueda y paginación |
| `GET` | `/tickets/nuevo` | Preparación de un ticket nuevo |
| `POST` | `/tickets/agregar` | Creación de ticket |
| `GET` | `/tickets/editar/{id}` | Carga del formulario de edición |
| `POST` | `/tickets/actualizar/{id}` | Actualización de ticket |
| `POST` | `/tickets/eliminar/{id}` | Eliminación de ticket |
| `POST` | `/preferencia/tema` | Actualización del tema visual |

# API REST de tickets

La API REST permite administrar tickets desde clientes HTTP como Postman.

Ruta base:

```text
http://localhost:8080/api/tickets
```

## Endpoints disponibles

| Método | Endpoint | Operación | Respuesta exitosa |
|---|---|---|---|
| `GET` | `/api/tickets` | Consultar todos los tickets | `200 OK` |
| `GET` | `/api/tickets/{id}` | Consultar un ticket | `200 OK` |
| `POST` | `/api/tickets` | Crear un ticket | `201 Created` |
| `PUT` | `/api/tickets/{id}` | Actualizar un ticket | `200 OK` |
| `DELETE` | `/api/tickets/{id}` | Eliminar un ticket | `200 OK` |

## Contrato de entrada

Las solicitudes `POST` y `PUT` reciben:

```json
{
  "titulo": "Falla de impresora",
  "descripcion": "La impresora de recepción no responde",
  "prioridad": 3,
  "responsable": "Soporte técnico",
  "usuarioId": 1
}
```

El campo `usuarioId` debe corresponder a un usuario registrado.

## Contrato de salida

La API devuelve:

```json
{
  "id": 9,
  "titulo": "Falla de impresora",
  "descripcion": "La impresora de recepción no responde",
  "prioridad": 3,
  "responsable": "Soporte técnico",
  "usuarioId": 1,
  "usuarioNombre": "DavsPrueba1"
}
```

La respuesta no incluye:

- Contraseña.
- Correo del usuario.
- Colección completa de tickets del usuario.
- Entidad JPA completa.

## Consultar todos los tickets

### Solicitud

```http
GET /api/tickets
```

### Respuesta

```text
200 OK
```

```json
[
  {
    "id": 1,
    "titulo": "Prueba 1",
    "descripcion": "Ticket prueba 1",
    "prioridad": 3,
    "responsable": "Santiago",
    "usuarioId": 1,
    "usuarioNombre": "DavsPrueba1"
  }
]
```

Si no existen tickets, la API responde:

```json
[]
```

## Consultar un ticket por ID

### Solicitud

```http
GET /api/tickets/9
```

### Respuesta

```text
200 OK
```

```json
{
  "id": 9,
  "titulo": "Prueba API desde Postman",
  "descripcion": "Ticket creado mediante el endpoint POST",
  "prioridad": 2,
  "responsable": "Soporte técnico",
  "usuarioId": 1,
  "usuarioNombre": "DavsPrueba1"
}
```

## Crear un ticket

### Solicitud

```http
POST /api/tickets
Content-Type: application/json
```

```json
{
  "titulo": "Prueba API desde Postman",
  "descripcion": "Ticket creado mediante el endpoint POST",
  "prioridad": 2,
  "responsable": "Soporte técnico",
  "usuarioId": 1
}
```

### Respuesta

```text
201 Created
```

```json
{
  "id": 9,
  "titulo": "Prueba API desde Postman",
  "descripcion": "Ticket creado mediante el endpoint POST",
  "prioridad": 2,
  "responsable": "Soporte técnico",
  "usuarioId": 1,
  "usuarioNombre": "DavsPrueba1"
}
```

La cabecera `Location` contiene la ruta del recurso creado:

```text
Location: /api/tickets/9
```

## Actualizar un ticket

### Solicitud

```http
PUT /api/tickets/9
Content-Type: application/json
```

```json
{
  "titulo": "Prueba API actualizada",
  "descripcion": "Ticket modificado correctamente mediante PUT",
  "prioridad": 3,
  "responsable": "Infraestructura",
  "usuarioId": 1
}
```

### Respuesta

```text
200 OK
```

```json
{
  "id": 9,
  "titulo": "Prueba API actualizada",
  "descripcion": "Ticket modificado correctamente mediante PUT",
  "prioridad": 3,
  "responsable": "Infraestructura",
  "usuarioId": 1,
  "usuarioNombre": "DavsPrueba1"
}
```

## Eliminar un ticket

### Solicitud

```http
DELETE /api/tickets/9
```

### Respuesta

```text
200 OK
```

```json
{
  "mensaje": "Ticket eliminado correctamente",
  "id": 9
}
```

## Validaciones de la API

El DTO de entrada valida:

- Título obligatorio.
- Título de máximo 150 caracteres.
- Descripción obligatoria.
- Descripción de máximo 1000 caracteres.
- Prioridad obligatoria.
- Prioridad entre `1` y `3`.
- Responsable obligatorio.
- Responsable de máximo 150 caracteres.
- Usuario obligatorio.
- ID de usuario positivo.

## Error 400 por datos inválidos

### Solicitud

```json
{
  "titulo": "",
  "descripcion": "",
  "prioridad": 8,
  "responsable": "",
  "usuarioId": -1
}
```

### Respuesta

```text
400 Bad Request
```

```json
{
  "fecha": "2026-11-05T00:50:00",
  "codigo": 400,
  "estado": "Bad Request",
  "mensaje": "La solicitud contiene datos inválidos",
  "errores": {
    "titulo": "El título es obligatorio",
    "descripcion": "La descripción es obligatoria",
    "prioridad": "La prioridad debe estar entre 1 y 3",
    "responsable": "El responsable es obligatorio",
    "usuarioId": "El ID del usuario debe ser positivo"
  }
}
```

## Error 400 por JSON incorrecto

### Solicitud

```json
{
  "titulo": "JSON incorrecto"
  "descripcion": "Falta una coma",
  "prioridad": 2,
  "responsable": "Soporte",
  "usuarioId": 1
}
```

### Respuesta

```text
400 Bad Request
```

```json
{
  "codigo": 400,
  "estado": "Bad Request",
  "mensaje": "El cuerpo de la solicitud contiene un JSON inválido"
}
```

## Error 404 por ticket inexistente

### Solicitud

```http
GET /api/tickets/999999
```

### Respuesta

```text
404 Not Found
```

```json
{
  "codigo": 404,
  "estado": "Not Found",
  "mensaje": "No existe un ticket con el ID 999999"
}
```

## Error 404 por usuario inexistente

### Solicitud

```http
POST /api/tickets
Content-Type: application/json
```

```json
{
  "titulo": "Usuario inexistente",
  "descripcion": "Prueba de usuario inexistente",
  "prioridad": 1,
  "responsable": "Soporte",
  "usuarioId": 999999
}
```

### Respuesta

```text
404 Not Found
```

```json
{
  "codigo": 404,
  "estado": "Not Found",
  "mensaje": "No existe un usuario con el ID 999999"
}
```

## Manejo de errores REST

La API utiliza:

```java
@RestControllerAdvice
```

El manejador procesa:

- `MethodArgumentNotValidException`
- `HttpMessageNotReadableException`
- `MethodArgumentTypeMismatchException`
- `TicketNoEncontradoException`
- `UsuarioNoEncontradoException`

Las respuestas utilizan una estructura JSON consistente y no muestran stack traces.

# Pruebas con Postman

## Colección

La colección se llama:

```text
Mesa de Ayuda API
```

Incluye las siguientes solicitudes:

```text
GET     Consultar todos los tickets
GET     Consultar ticket por ID
POST    Crear un ticket
PUT     Actualizar un ticket
DELETE  Eliminar un ticket
POST    Error 400 - Datos inválidos
GET     Error 404 - Ticket inexistente
```

## Secuencia de prueba

1. Consultar todos los tickets.
2. Crear un ticket.
3. Guardar el ID retornado.
4. Consultar el ticket por ID.
5. Actualizarlo mediante PUT.
6. Consultarlo nuevamente.
7. Eliminarlo.
8. Consultarlo después de eliminarlo para demostrar el error `404`.
9. Enviar datos inválidos para demostrar el error `400`.

## Importar la colección

Si el repositorio incluye la colección exportada:

```text
docs/postman/Mesa-de-Ayuda-API.postman_collection.json
```

En Postman:

1. Seleccionar **Import**.
2. Elegir el archivo de colección.
3. Confirmar la importación.
4. Iniciar Spring Boot.
5. Ejecutar las solicitudes en el orden guardado.

## Evidencias

Las capturas de las pruebas pueden almacenarse en:

```text
docs/postman/
├── 01-get-todos.png
├── 02-post-crear.png
├── 03-get-por-id.png
├── 04-put-actualizar.png
├── 05-delete-eliminar.png
├── 06-error-400.png
└── 07-error-404.png
```

# Validaciones de la interfaz web

La interfaz utiliza DTO y Jakarta Validation:

```java
@NotBlank
@NotNull
@Email
@Valid
```

Los controladores procesan los resultados mediante `BindingResult`.

Cuando existen errores:

- La operación se cancela.
- Los datos válidos permanecen.
- Los mensajes aparecen junto al campo correspondiente.
- El formulario conserva el modo de creación o edición.

# Interfaz

La aplicación utiliza Bootstrap 5 y CSS personalizado.

La vista principal de tickets utiliza tres paneles:

```text
┌─────────────────┬──────────────────────────┬─────────────────┐
│ Crear o editar  │ Listado paginado         │ Buscar tickets  │
│ ticket          │ de tickets               │ y resultados    │
└─────────────────┴──────────────────────────┴─────────────────┘
```

En dispositivos pequeños, los paneles se presentan verticalmente.

Las vistas se dividen en fragmentos Thymeleaf:

```text
tickets.html
formulario-ticket.html
listado-tickets.html
busqueda-tickets.html
```

# Pruebas funcionales

## Usuarios y sesiones

- Registrar una cuenta válida.
- Rechazar correos inválidos o duplicados.
- Rechazar contraseñas diferentes.
- Iniciar sesión con credenciales válidas.
- Rechazar credenciales incorrectas.
- Mantener la sesión durante la navegación.
- Cerrar sesión.
- Impedir el acceso a rutas protegidas después del cierre.

## Tickets web

- Crear un ticket válido.
- Rechazar formularios incompletos.
- Editar y actualizar.
- Mantener los valores cuando falla una validación.
- Eliminar.
- Confirmar mensajes de éxito.

## Búsqueda y paginación

- Buscar por título.
- Buscar por descripción.
- Buscar por responsable.
- Buscar un valor inexistente.
- Confirmar que la búsqueda no cambia la tabla principal.
- Mostrar cinco registros por página.
- Navegar entre páginas.
- Verificar el comportamiento sin registros.

## Sesiones, cookies y errores

- Verificar el usuario activo.
- Seleccionar tema claro y oscuro.
- Confirmar la cookie `tema` en las herramientas del navegador.
- Cerrar sesión y comprobar que la preferencia permanezca.
- Acceder a una ruta inexistente.
- Comprobar la página personalizada para errores internos.
- Verificar la protección de `/menu`, `/tickets` y `/preferencia/**`.

## API REST

- Consultar todos los tickets con GET.
- Crear un ticket con POST.
- Consultar un ticket por ID.
- Actualizar un ticket con PUT.
- Eliminar un ticket con DELETE.
- Validar `400 Bad Request`.
- Validar `404 Not Found`.
- Comprobar que las respuestas sean JSON.
- Comprobar que no se expongan contraseñas.

# Seguridad

La aplicación evita guardar contraseñas dentro de cookies personalizadas. La cookie `tema` contiene únicamente la preferencia visual.

La API utiliza DTO de entrada y salida para evitar exponer entidades JPA, contraseñas, correos y relaciones completas.

La API permanece sin autenticación para facilitar las pruebas locales mediante Postman.

Para un despliegue público se recomienda incorporar:

- Spring Security.
- BCrypt para contraseñas.
- Autenticación mediante JWT u OAuth 2.0.
- HTTPS.
- Protección CSRF para formularios web.
- Gestión externa de secretos.
- Restricciones CORS.
- Migraciones con Flyway o Liquibase.
- Desactivación de `spring.jpa.show-sql` en producción.

# Licencia

El repositorio no incluye actualmente una licencia de distribución.

Si el proyecto será publicado o reutilizado por terceros, se debe agregar un archivo `LICENSE`.