-- ============================================
-- CREACIÓN DEL USUARIO DE BASE DE DATOS
-- ============================================
-- Este script crea el usuario c##eatsmart_app que la aplicación usa para conectarse.
-- IMPORTANTE: La contraseña real NO se incluye en este archivo por seguridad.
-- Sustituir <TU_PASSWORD_AQUI> por una contraseña segura antes de ejecutar.
-- ============================================

-- Crear el usuario con prefijo c## (para Oracle Multitenant)
CREATE USER c##eatsmart_app IDENTIFIED BY "TuNuevaPassword2026";

-- Dar permisos básicos
GRANT CONNECT, RESOURCE TO c##eatsmart_app;

-- Dar permisos específicos
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE, CREATE TRIGGER TO c##eatsmart_app;

-- Asignar tablespace
ALTER USER c##eatsmart_app DEFAULT TABLESPACE users QUOTA UNLIMITED ON users;

-- Verificar que se creó (debe devolver 1 fila)
SELECT username FROM dba_users WHERE username = 'C##EATSMART_APP';