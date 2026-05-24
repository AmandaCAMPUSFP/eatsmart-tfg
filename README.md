# EatSmart

Aplicación web de control nutricional y seguimiento de hábitos alimenticios. Permite a los usuarios registrar su perfil nutricional, llevar un control de sus comidas diarias y consultar un resumen de calorías y macronutrientes frente a sus objetivos.

> **TFG — Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)**
> Autora: Amanda Fuentes Tirado · Curso 2024-2026

---

## Descripción

EatSmart es una aplicación web con arquitectura cliente-servidor desacoplada:

- **Frontend:** SPA (Single Page Application) desarrollada en Angular.
- **Backend:** API REST desarrollada en Spring Boot, protegida con Spring Security y autenticación JWT.
- **Base de datos:** Oracle Database XE.

El sistema implementa autenticación robusta, control de acceso por propiedad de recursos y diversas medidas de seguridad siguiendo el estándar **OWASP Top 10:2021**.

---

## Stack tecnológico

### Backend
- Java 21
- Spring Boot 3.2.4
- Spring Security + JWT (JJWT 0.11.5)
- Spring Data JPA / Hibernate
- Oracle Database XE 21c
- Bucket4j (rate limiting)
- Maven

### Frontend
- Angular 18 (componentes standalone)
- Angular Material
- SSR (Angular Universal)
- TypeScript
- SCSS

---

## Arquitectura

```
┌─────────────────┐   HTTPS/JSON    ┌──────────────────┐    JDBC    ┌──────────────┐
│  Navegador      │ ──────────────► │  Backend         │ ─────────► │  Oracle XE   │
│  Angular (SPA)  │   JWT Bearer    │  Spring Boot     │            │  (JPA/       │
│                 │ ◄────────────── │  + Spring Security│           │   Hibernate) │
└─────────────────┘                 └──────────────────┘            └──────────────┘
```

Arquitectura por capas en el backend: `Controller → Service → Repository → Entity`, con DTOs validados mediante Bean Validation y un manejador global de excepciones.

---

## Seguridad (OWASP Top 10:2021)

| OWASP | Vulnerabilidad | Mitigación implementada |
|-------|----------------|--------------------------|
| **A01** | Broken Access Control | `SecurityService.isOwner()` + `@PreAuthorize` en endpoints sensibles; listado de usuarios deshabilitado |
| **A02** | Cryptographic Failures | Hash BCrypt de contraseñas; credenciales y secreto JWT mediante variables de entorno; hash de contraseña excluido de la serialización |
| **A05** | Security Misconfiguration | Cabeceras de seguridad (CSP, HSTS, X-Frame-Options DENY, X-Content-Type-Options, Referrer-Policy); CORS configurable por variable de entorno |
| **A07** | Identification & Authentication Failures | JWT con access token (15 min) + refresh token (7 días); blacklist persistente de refresh tokens tras logout; rate limiting; política de contraseñas robusta; tokens fuera de la URL |

---

## Puesta en marcha

### Requisitos previos
- Java 21
- Maven
- Node.js y Angular CLI
- Oracle Database XE 21c en ejecución

### Variables de entorno (backend)

El proyecto **no incluye credenciales en el código**. Antes de arrancar el backend, define estas variables de entorno:

```
DB_USERNAME=tu_usuario_oracle
DB_PASSWORD=tu_contraseña_oracle
JWT_SECRET=tu_secreto_jwt_minimo_32_caracteres
```

> Consulta `eatsmart-backend/src/main/resources/application.properties.example` como referencia.

### Backend

```bash
cd eatsmart-backend
mvn spring-boot:run
```

El backend se inicia en `http://localhost:8080`.

### Frontend

```bash
cd eatsmart-frontend
npm install
ng serve
```

El frontend se sirve en `http://localhost:4200`.

---

## Endpoints principales

### Autenticación (`/api/auth`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/registro` | Registro de nuevo usuario |
| POST | `/api/auth/login` | Inicio de sesión (devuelve access + refresh token) |
| POST | `/api/auth/refresh` | Renueva el access token (refresh token en el body) |
| POST | `/api/auth/logout` | Cierra sesión e invalida el refresh token |
| POST | `/api/auth/validar` | Valida un token (en cabecera Authorization) |

### Recursos protegidos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET/POST/PUT/DELETE | `/api/perfiles-nutricionales/{idUsuario}` | Perfil nutricional (solo el propietario) |
| GET/PUT/DELETE | `/api/usuarios/{id}` | Datos de usuario (solo el propietario) |
| GET/POST/PUT/DELETE | `/api/comidas` | Registro de comidas |
| GET | `/api/alimentos` | Catálogo de alimentos (público) |
| GET | `/api/recetas` | Catálogo de recetas (público) |

> Todos los endpoints privados requieren la cabecera `Authorization: Bearer <token>`.

---

## Pruebas

El proyecto incluye pruebas automatizadas con JUnit 5:

- `SecurityServiceTest` — validación de propiedad de recursos (OWASP A01).
- `PasswordValidationServiceTest` — política de contraseñas (OWASP A07).

```bash
cd eatsmart-backend
mvn test
```

---

## Flujo principal de la aplicación

1. **Registro / Login** → el usuario crea su cuenta o inicia sesión.
2. **Perfil nutricional** → introduce sus datos; el sistema calcula su objetivo calórico (Harris-Benedict) e IMC.
3. **Registro de comidas** → añade los alimentos consumidos por tipo de comida.
4. **Resumen diario** → consulta calorías y macronutrientes frente a su objetivo.

---

## Líneas de trabajo futuro

- Almacenamiento de JWT en cookies `HttpOnly` + `Secure` + `SameSite`.
- Rol de administrador para la gestión del catálogo.
- Paginación en endpoints de listado.
- Migraciones de base de datos versionadas (Flyway/Liquibase).
- Documentación interactiva de la API (Swagger/OpenAPI).
- Integración continua (CI/CD) con GitHub Actions.
- Tabla intermedia con cantidad consumida para un cálculo nutricional exacto.
- Funcionalidad de recuperación de contraseña.

---

## Autoría

Proyecto desarrollado por **Amanda Fuentes Tirado** como Trabajo de Fin de Grado del CFGS en Desarrollo de Aplicaciones Multiplataforma.
