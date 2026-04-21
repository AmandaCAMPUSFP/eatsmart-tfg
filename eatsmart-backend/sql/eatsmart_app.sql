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
-- LIMPIAR DATOS EXISTENTES
-- ============================================
DELETE FROM COMIDA_RECETA;
DELETE FROM COMIDA_ALIMENTO;
DELETE FROM COMIDA_REGISTRO;
DELETE FROM RECETA_ALIMENTO;
DELETE FROM RECETA;
DELETE FROM ALIMENTO;
DELETE FROM PERFIL_NUTRICIONAL;
DELETE FROM USUARIO;

COMMIT;

-- Reiniciar secuencias
ALTER SEQUENCE seq_usuario RESTART START WITH 1;
ALTER SEQUENCE seq_alimento RESTART START WITH 1;
ALTER SEQUENCE seq_receta RESTART START WITH 1;
ALTER SEQUENCE seq_comida RESTART START WITH 1;

-- ============================================
-- INSERTAR ALIMENTOS CON SECUENCIA
-- ============================================
INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Pollo pechuga', 165, 31, 0, 3.6, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Arroz blanco', 130, 2.7, 28, 0.3, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Brócoli', 34, 2.8, 7, 0.4, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Manzana', 52, 0.3, 14, 0.2, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Plátano', 89, 1.1, 23, 0.3, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Huevo', 155, 13, 1.1, 11, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Leche desnatada', 35, 3.4, 5, 0.1, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Pan integral', 247, 8.7, 41, 1.5, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Salmón', 208, 20, 0, 13, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Zanahoria', 41, 0.9, 10, 0.2, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Lechuga', 15, 1.2, 2.9, 0.2, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Tomate', 18, 0.9, 3.9, 0.2, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Atún enlatado', 132, 29, 0, 1.3, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Almendras', 579, 21, 22, 50, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Yogur natural', 63, 3.5, 4.7, 0.4, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Lentejas', 116, 9, 20, 0.4, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Aceite de oliva', 884, 0, 0, 100, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Patata cocida', 77, 2, 17, 0.1, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Pechuga de pavo', 135, 29, 0, 1.6, SYSDATE);

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
VALUES (seq_alimento.NEXTVAL, 'Espinaca', 23, 2.7, 3.6, 0.4, SYSDATE);

-- ============================================
-- INSERTAR RECETAS CON SECUENCIA
-- ============================================
INSERT INTO RECETA (id_receta, nombre, descripcion, raciones, total_kcal, total_proteinas, total_carbohidratos, total_grasas, fecha_creacion)
VALUES (seq_receta.NEXTVAL, 'Pechuga de pollo con arroz', 'Pechuga de pollo asada con arroz blanco y brócoli al vapor', 2, 450, 62, 56, 5, SYSDATE);

INSERT INTO RECETA (id_receta, nombre, descripcion, raciones, total_kcal, total_proteinas, total_carbohidratos, total_grasas, fecha_creacion)
VALUES (seq_receta.NEXTVAL, 'Ensalada Mediterránea', 'Lechuga, tomate, zanahoria con aceite de oliva y sal', 1, 250, 5, 15, 20, SYSDATE);

INSERT INTO RECETA (id_receta, nombre, descripcion, raciones, total_kcal, total_proteinas, total_carbohidratos, total_grasas, fecha_creacion)
VALUES (seq_receta.NEXTVAL, 'Tortilla de huevo', 'Tortilla de 3 huevos con pan integral', 1, 380, 18, 41, 18, SYSDATE);

INSERT INTO RECETA (id_receta, nombre, descripcion, raciones, total_kcal, total_proteinas, total_carbohidratos, total_grasas, fecha_creacion)
VALUES (seq_receta.NEXTVAL, 'Salmón a la mantequilla', 'Filete de salmón con espinaca y limón', 2, 450, 60, 8, 22, SYSDATE);

INSERT INTO RECETA (id_receta, nombre, descripcion, raciones, total_kcal, total_proteinas, total_carbohidratos, total_grasas, fecha_creacion)
VALUES (seq_receta.NEXTVAL, 'Smoothie proteico', 'Yogur natural, plátano, leche y avena', 1, 280, 12, 45, 6, SYSDATE);

COMMIT;

SELECT 'Alimentos insertados:' AS resultado FROM DUAL;
SELECT COUNT(*) FROM ALIMENTO;

SELECT 'Recetas insertadas:' AS resultado FROM DUAL;
SELECT COUNT(*) FROM RECETA;