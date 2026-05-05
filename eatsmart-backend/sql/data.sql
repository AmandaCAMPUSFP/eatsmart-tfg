-- ===== ALIMENTOS DE PRUEBA =====
-- Esta tabla se ejecuta solo si la tabla ALIMENTO está vacía
-- En Hibernate, usa: spring.jpa.hibernate.ddl-auto=update + data.sql

INSERT INTO ALIMENTO (id_alimento, nombre, kcal_100g, proteinas_100g, carbohidratos_100g, grasas_100g, fecha_creacion) 
SELECT * FROM (
    SELECT 1, 'Pollo pechuga', 165, 31, 0, 3.6, SYSDATE FROM DUAL
    UNION ALL
    SELECT 2, 'Arroz blanco', 130, 2.7, 28, 0.3, SYSDATE FROM DUAL
    UNION ALL
    SELECT 3, 'Brócoli', 34, 2.8, 7, 0.4, SYSDATE FROM DUAL
    UNION ALL
    SELECT 4, 'Huevo', 155, 13, 1.1, 11, SYSDATE FROM DUAL
    UNION ALL
    SELECT 5, 'Manzana', 52, 0.26, 14, 0.17, SYSDATE FROM DUAL
    UNION ALL
    SELECT 6, 'Leche descremada', 35, 3.4, 5, 0.1, SYSDATE FROM DUAL
    UNION ALL
    SELECT 7, 'Pan integral', 265, 8.7, 47, 3.3, SYSDATE FROM DUAL
    UNION ALL
    SELECT 8, 'Atún enlatado', 132, 29.9, 0, 1.3, SYSDATE FROM DUAL
    UNION ALL
    SELECT 9, 'Plátano', 89, 1.1, 23, 0.3, SYSDATE FROM DUAL
    UNION ALL
    SELECT 10, 'Almendras', 579, 21.2, 22, 50, SYSDATE FROM DUAL
    UNION ALL
    SELECT 11, 'Espinaca', 23, 2.7, 3.6, 0.4, SYSDATE FROM DUAL
    UNION ALL
    SELECT 12, 'Salmón', 208, 20, 0, 13, SYSDATE FROM DUAL
    UNION ALL
    SELECT 13, 'Zanahoria', 41, 0.9, 10, 0.2, SYSDATE FROM DUAL
    UNION ALL
    SELECT 14, 'Queso fresco', 265, 26, 3.3, 17, SYSDATE FROM DUAL
    UNION ALL
    SELECT 15, 'Yogur griego', 59, 10, 3.3, 0.4, SYSDATE FROM DUAL
) WHERE NOT EXISTS (SELECT 1 FROM ALIMENTO WHERE id_alimento = 1);

-- ===== RECETAS DE PRUEBA =====
INSERT INTO RECETA (id_receta, nombre, descripcion, raciones, total_kcal, total_proteinas, total_carbohidratos, total_grasas, fecha_creacion)
SELECT * FROM (
    SELECT 1, 'Pechuga de pollo con arroz', 'Pollo a la plancha con arroz blanco y brócoli', 2, 500, 50, 60, 10, SYSDATE FROM DUAL
    UNION ALL
    SELECT 2, 'Ensalada de atún', 'Atún con espinaca y limón', 1, 250, 40, 5, 5, SYSDATE FROM DUAL
    UNION ALL
    SELECT 3, 'Desayuno proteico', 'Huevos con pan integral y frutas', 1, 400, 20, 45, 15, SYSDATE FROM DUAL
) WHERE NOT EXISTS (SELECT 1 FROM RECETA WHERE id_receta = 1);

-- ===== RELACIÓN RECETA-ALIMENTO =====
INSERT INTO COMPUESTA_POR (id_receta, id_alimento, gramos)
SELECT * FROM (
    SELECT 1, 1, 200 FROM DUAL  -- Receta 1: 200g pollo
    UNION ALL
    SELECT 1, 2, 100 FROM DUAL  -- Receta 1: 100g arroz
    UNION ALL
    SELECT 1, 3, 150 FROM DUAL  -- Receta 1: 150g brócoli
    UNION ALL
    SELECT 2, 8, 150 FROM DUAL  -- Receta 2: 150g atún
    UNION ALL
    SELECT 2, 11, 100 FROM DUAL -- Receta 2: 100g espinaca
    UNION ALL
    SELECT 3, 4, 2 FROM DUAL    -- Receta 3: 2 huevos
    UNION ALL
    SELECT 3, 7, 50 FROM DUAL   -- Receta 3: 50g pan
    UNION ALL
    SELECT 3, 5, 100 FROM DUAL  -- Receta 3: 100g manzana
) WHERE NOT EXISTS (SELECT COUNT(*) FROM COMPUESTA_POR WHERE id_receta = 1 AND id_alimento = 1);

COMMIT;