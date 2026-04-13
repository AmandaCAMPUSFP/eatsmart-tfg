# EatSmart - Come Inteligente, Vive Mejor

**Trabajo Fin de Grado (TFG) - Ciclo Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)**

---

## Descripción

**EatSmart** es una aplicación inteligente de nutrición que permite a los usuarios:
- Registrar y gestionar su perfil nutricional
- Crear y almacenar recetas personalizadas
- Registrar el consumo diario de comidas
- Calcular automáticamente macronutrientes (proteínas, carbohidratos, grasas, calorías)
- Obtener recomendaciones basadas en objetivos (pérdida de peso, mantenimiento, ganancia muscular)
- Monitorear su progreso nutricional

---

## Arquitectura

### Backend
- **Framework:** Spring Boot 3.2.4
- **Lenguaje:** Java 21
- **Base de Datos:** Oracle XE 21c
- **Seguridad:** Spring Security + BCrypt
- **Validación:** Jakarta Bean Validation
- **Patrón:** Arquitectura por capas (entity, dto, repository, service, controller)
---
### Stack Tecnológico
├── Spring Boot 3.2.4 
├── Spring Security + BCrypt 
├── Spring Data JPA + Hibernate 
├── Oracle JDBC Driver 23.2.0.0 
├── Lombok 
├── Jakarta Validation 
├── JWT (io.jsonwebtoken 0.12.3) 
└── Maven

---

## Estructura del Proyecto
eatsmart-tfg/ 
├── eatsmart-backend/ 
│ ├── src/main/java/com/eatsmart/eatsmart_backend/ 
│ │ ├── entity/ # Entidades JPA (Usuario, Alimento, Receta, etc.) 
│ │ ├── dto/ # Data Transfer Objects (validaciones) 
│ │ ├── repository/ # Interfaces JPA Repository 
│ │ ├── service/ # Lógica de negocio 
│ │ ├── controller/ # Endpoints REST 
│ │ ├── config/ # Configuraciones (Security, CORS, Exception Handler) 
│ │ ├── exception/ # Excepciones personalizadas 
│ │ └── Eatsmart_backendApplication.java 
│ ├── src/main/resources/ 
│ │ └── application.properties # Configuración de la aplicación 
│ ├── pom.xml # Dependencias Maven 
│ └── target/ # Compilados 
└── README.md
---

---

## Diagrama de Entidades
USUARIO 
├── id_usuario (PK) 
├── email 
├── contrasena_hash 
├── fecha_creacion 
└── activo

PERFIL_NUTRICIONAL 
├── id_usuario (PK, FK) 
├── sexo 
├── fecha_nacimiento 
├── altura_cm 
├── peso_kg 
├── nivel_actividad 
├── objetivo 
└── fecha_actualizacion

ALIMENTO 
├── id_alimento (PK) 
├── nombre 
├── kcal_100g 
├── proteinas_100g 
├── carbohidratos_100g 
├── grasas_100g 
└── fecha_creacion

RECETA 
├── id_receta (PK) 
├── nombre 
├── descripcion 
├── raciones 
├── total_kcal 
├── total_proteinas 
├── total_carbohidratos 
├── total_grasas 
└── fecha_creacion

COMIDA_REGISTRO 
├── id_comida (PK) 
├── id_usuario (FK) 
├── fecha 
├── tipo_comida 
└── fecha_creacion

Relaciones N:M 
├── COMPUESTA_POR (Receta-Alimento) 
├── INCLUYE_ALIMENTO (ComidaRegistro-Alimento) 
└── INCLUYE_RECETA (ComidaRegistro-Receta)

---

## API Endpoints

### Autenticación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/registrar` | Registrar nuevo usuario |
| POST | `/api/auth/login` | Iniciar sesión |
| POST | `/api/auth/cambiar-contrasena/{idUsuario}` | Cambiar contraseña |

#### Request: Registrar
```json
{
  "email": "usuario@example.com",
  "contrasena": "MiContraseña123"
}
