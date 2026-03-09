use parqueadero_autos_colombia;
INSERT INTO usuarios (identificacion, nombre_completo, telefono, email, direccion) VALUES
('123456789', 'Juan Pérez Gómez', '3001234567', 'juan.perez@email.com', 'Calle 123 #45-67, Bogotá'),
('987654321', 'María Rodríguez López', '3017654321', 'maria.rodriguez@email.com', 'Carrera 50 #20-30, Medellín');

INSERT INTO vehiculos (placa, marca, modelo, color, tipo, id_usuario) VALUES
('ABC123', 'Renault', '2020', 'Rojo', 'Carro', 1),
('DEF456', 'Yamaha', '2022', 'Negro', 'Moto', 2);

INSERT INTO celdas (numero_celda, tipo_celda, estado) VALUES
('A01', 'Carro', 'Disponible'),
('A02', 'Carro', 'Disponible'),
('B01', 'Moto', 'Disponible'),
('B02', 'Moto', 'Disponible'),
('C01', 'Discapacitados', 'Disponible');

-- Insertar un registro de parqueo de ejemplo
INSERT INTO registros_parqueo (id_vehiculo, id_celda, es_mensualidad) VALUES
(1, 1, FALSE);

-- Mensaje de confirmación
SELECT 'Base de datos creada exitosamente con las 5 tablas' AS 'Mensaje';