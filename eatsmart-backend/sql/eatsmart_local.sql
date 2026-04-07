-- EATSMART: Come Inteligente, Vive Mejor
-- Scripts SQL para Oracle
-- Creación de tablas y datos de prueba

-- ============================================
-- 1. CREAR TABLAS
-- ============================================

-- Tabla USUARIO
CREATE TABLE USUARIO (
    id_usuario NUMBER PRIMARY KEY,
    email VARCHAR2(100) NOT NULL UNIQUE,
    contrasena_hash VARCHAR2(255) NOT NULL,
    fecha_creacion DATE DEFAULT SYSDATE,
    activo CHAR(1) DEFAULT '1'
);

-- Tabla PERFIL_NUTRICIONAL
CREATE TABLE PERFIL_NUTRICIONAL (
    id_usuario NUMBER PRIMARY KEY,
    sexo VARCHAR2(10),
    fecha_nacimiento DATE,
    altura_cm NUMBER,
    peso_kg DECIMAL(5,2),
    nivel_actividad VARCHAR2(50),
    objetivo VARCHAR2(100),
    fecha_actualizacion DATE DEFAULT SYSDATE,
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)
);

-- Tabla ALIMENTO
CREATE TABLE ALIMENTO (
    id_alimento NUMBER PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    kcal_100g DECIMAL(6,2),
    proteinas_100g DECIMAL(5,2),
    carbohidratos_100g DECIMAL(5,2),
    grasas_100g DECIMAL(5,2),
    fecha_creacion DATE DEFAULT SYSDATE
);

-- Tabla RECETA
CREATE TABLE RECETA (
    id_receta NUMBER PRIMARY KEY,
    nombre VARCHAR2(150) NOT NULL,
    descripcion VARCHAR2(500),
    raciones NUMBER,
    total_kcal DECIMAL(8,2),
    total_proteinas DECIMAL(6,2),
    total_carbohidratos DECIMAL(6,2),
    total_grasas DECIMAL(6,2),
    fecha_creacion DATE DEFAULT SYSDATE
);

-- Tabla de relación RECETA-ALIMENTO (N:M)
CREATE TABLE RECETA_ALIMENTO (
    id_receta NUMBER,
    id_alimento NUMBER,
    gramos DECIMAL(6,2),
    PRIMARY KEY (id_receta, id_alimento),
    FOREIGN KEY (id_receta) REFERENCES RECETA(id_receta),
    FOREIGN KEY (id_alimento) REFERENCES ALIMENTO(id_alimento)
);

-- Tabla COMIDA_REGISTRO
CREATE TABLE COMIDA_REGISTRO (
    id_comida NUMBER PRIMARY KEY,
    id_usuario NUMBER NOT NULL,
    fecha DATE NOT NULL,
    tipo_comida VARCHAR2(50),
    fecha_creacion DATE DEFAULT SYSDATE,
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)
);

-- Tabla de relación COMIDA_REGISTRO-ALIMENTO (N:M)
CREATE TABLE COMIDA_ALIMENTO (
    id_comida NUMBER,
    id_alimento NUMBER,
    gramos_consumidos DECIMAL(6,2),
    PRIMARY KEY (id_comida, id_alimento),
    FOREIGN KEY (id_comida) REFERENCES COMIDA_REGISTRO(id_comida),
    FOREIGN KEY (id_alimento) REFERENCES ALIMENTO(id_alimento)
);

-- Tabla de relación COMIDA_REGISTRO-RECETA (N:M)
CREATE TABLE COMIDA_RECETA (
    id_comida NUMBER,
    id_receta NUMBER,
    raciones_consumidas DECIMAL(5,2),
    PRIMARY KEY (id_comida, id_receta),
    FOREIGN KEY (id_comida) REFERENCES COMIDA_REGISTRO(id_comida),
    FOREIGN KEY (id_receta) REFERENCES RECETA(id_receta)
);

-- ============================================
-- 2. CREAR SECUENCIAS PARA IDs
-- ============================================

CREATE SEQUENCE seq_usuario START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_alimento START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_receta START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_comida START WITH 1 INCREMENT BY 1;

-- INSERTAR DATOS DE PRUEBA

-- Usuarios de prueba
INSERT INTO USUARIO (id_usuario, email, contrasena_hash) 
VALUES (1, 'amanda@eatsmart.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36gZvQm');

INSERT INTO USUARIO (id_usuario, email, contrasena_hash) 
VALUES (2, 'user2@eatsmart.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36gZvQm');

-- Perfiles nutricionales
INSERT INTO PERFIL_NUTRICIONAL (id_usuario, sexo, fecha_nacimiento, altura_cm, peso_kg, nivel_actividad, objetivo)
VALUES (1, 'F', TO_DATE('2000-01-15', 'YYYY-MM-DD'), 165, 62.5, 'Moderado', 'Pérdida de peso');

INSERT INTO PERFIL_NUTRICIONAL (id_usuario, sexo, fecha_nacimiento, altura_cm, peso_kg, nivel_actividad, objetivo)
VALUES (2, 'M', TO_DATE('1998-06-20', 'YYYY-MM-DD'), 180, 78.0, 'Activo', 'Ganancia muscular');

-- Alimentos de prueba
INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g)
VALUES (1, 'Pollo sin piel', 165, 31.0, 0, 3.6);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g)
VALUES (2, 'Arroz blanco', 130, 2.7, 28, 0.3);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g)
VALUES (3, 'Brócoli', 34, 2.8, 7, 0.4);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g)
VALUES (4, 'Huevo', 155, 13, 1.1, 11);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g)
VALUES (5, 'Pan integral', 265, 8.7, 49, 3.3);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g)
VALUES (6, 'Manzana', 52, 0.3, 14, 0.2);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g)
VALUES (7, 'Salmón', 208, 20, 0, 13);

-- Recetas de prueba
INSERT INTO RECETA (id_receta, nombre, descripcion, raciones, total_kcal, total_proteinas, total_carbohidratos, total_grasas)
VALUES (1, 'Pechuga de pollo con arroz', 'Pechuga de pollo a la plancha con arroz blanco y brócoli', 2, 450, 45, 35, 8);

INSERT INTO RECETA (id_receta, nombre, descripcion, raciones, total_kcal, total_proteinas, total_carbohidratos, total_grasas)
VALUES (2, 'Tortilla de huevos', 'Tortilla de 3 huevos con pan integral tostado', 1, 400, 35, 45, 15);

-- Relación RECETA-ALIMENTO
INSERT INTO RECETA_ALIMENTO (id_receta, id_alimento, gramos)
VALUES (1, 1, 150);

INSERT INTO RECETA_ALIMENTO (id_receta, id_alimento, gramos)
VALUES (1, 2, 100);

INSERT INTO RECETA_ALIMENTO (id_receta, id_alimento, gramos)
VALUES (1, 3, 100);

INSERT INTO RECETA_ALIMENTO (id_receta, id_alimento, gramos)
VALUES (2, 4, 150);

INSERT INTO RECETA_ALIMENTO (id_receta, id_alimento, gramos)
VALUES (2, 5, 100);

-- Registro de comidas
INSERT INTO COMIDA_REGISTRO (id_comida, id_usuario, fecha, tipo_comida)
VALUES (1, 1, TRUNC(SYSDATE), 'Desayuno');

INSERT INTO COMIDA_REGISTRO (id_comida, id_usuario, fecha, tipo_comida)
VALUES (2, 1, TRUNC(SYSDATE), 'Comida');

-- Relación COMIDA_REGISTRO-ALIMENTO
INSERT INTO COMIDA_ALIMENTO (id_comida, id_alimento, gramos_consumidos)
VALUES (1, 5, 100);

INSERT INTO COMIDA_ALIMENTO (id_comida, id_alimento, gramos_consumidos)
VALUES (1, 4, 2);

-- Relación COMIDA_REGISTRO-RECETA
INSERT INTO COMIDA_RECETA (id_comida, id_receta, raciones_consumidas)
VALUES (2, 1, 1);

COMMIT;

