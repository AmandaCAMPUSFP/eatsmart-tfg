-- Crear el usuario con prefijo c## (para Multitenant)
CREATE USER c##eatsmart_app IDENTIFIED BY "Eatsmart_Secure_2026!";

-- Dar permisos básicos
GRANT CONNECT, RESOURCE TO c##eatsmart_app;

-- Dar permisos específicos
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE, CREATE TRIGGER TO c##eatsmart_app;

-- Asignar tablespace
ALTER USER c##eatsmart_app DEFAULT TABLESPACE users QUOTA UNLIMITED ON users;

-- Verificar que se creó (debe devolver 1 fila)
SELECT username FROM dba_users WHERE username = 'C##EATSMART_APP';