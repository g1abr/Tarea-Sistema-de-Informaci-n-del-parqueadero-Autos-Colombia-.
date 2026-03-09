DROP DATABASE IF EXISTS parqueadero_autos_colombia;
CREATE DATABASE parqueadero_autos_colombia;
USE parqueadero_autos_colombia;

/*Tabla usuarios*/
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    identificacion VARCHAR(15) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    email VARCHAR(50) NOT NULL,
    direccion VARCHAR(50) NOT NULL,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Restricciones 
    
    CONSTRAINT uk_usuarios_identificacion UNIQUE (identificacion),
    CONSTRAINT uk_usuarios_email UNIQUE (email),
    CONSTRAINT chk_usuarios_identificacion CHECK (
        identificacion REGEXP '^[0-9]{5,20}$' -- Solo números, entre 5 y 20 dígitos
    ),
    CONSTRAINT chk_usuarios_email CHECK (
        email LIKE '%_@__%.__%' AND 
        email REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
    ),
    CONSTRAINT chk_usuarios_telefono CHECK (
        telefono REGEXP '^[0-9+][0-9]{7,15}$'
    ),
    CONSTRAINT chk_usuarios_nombre CHECK (
        nombre_completo != '' AND LENGTH(nombre_completo) >= 3
    ),
    CONSTRAINT chk_usuarios_direccion CHECK (
        direccion != '' AND LENGTH(direccion) >= 5
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Tabla vehiculos*/
CREATE TABLE vehiculos (
    id_vehiculo INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(10) NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(20) NOT NULL,
    color VARCHAR(30) NOT NULL,
    tipo ENUM('Carro', 'Moto', 'Camioneta', 'Bicicleta', 'Otro') NOT NULL,
    id_usuario INT NOT NULL,
    
    -- Restricciones
    CONSTRAINT uk_vehiculos_placa UNIQUE (placa),
    CONSTRAINT fk_vehiculos_usuario FOREIGN KEY (id_usuario) 
        REFERENCES usuarios(id_usuario) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    CONSTRAINT chk_vehiculos_placa CHECK (
        placa REGEXP '^[A-Z0-9]{3,10}$'
    ),
    CONSTRAINT chk_vehiculos_marca CHECK (
        marca != '' AND LENGTH(marca) >= 2
    ),
    CONSTRAINT chk_vehiculos_modelo CHECK (
        modelo REGEXP '^[0-9]{4}$|^[0-9]{2}$' 
    ),
    CONSTRAINT chk_vehiculos_color CHECK (
        color != '' AND color REGEXP '^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$'
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Tabla celdas (espacio)*/

CREATE TABLE celdas (
    id_celda INT AUTO_INCREMENT PRIMARY KEY,
    numero_celda VARCHAR(5) NOT NULL,
    tipo_celda ENUM('Carro', 'Moto', 'Camioneta', 'Bicicleta', 'Discapacitados') NOT NULL,
    estado ENUM('Disponible', 'Ocupada', 'Mantenimiento') NOT NULL DEFAULT 'Disponible',
    
    -- Restricciones
    CONSTRAINT uk_celdas_numero UNIQUE (numero_celda),
    CONSTRAINT chk_celdas_numero CHECK (
        numero_celda REGEXP '^[A-Z]{1,2}[0-9]{1,3}$' 
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Tabla registros de parqueo */
CREATE TABLE registros_parqueo (
    id_registro INT AUTO_INCREMENT PRIMARY KEY,
    id_vehiculo INT NOT NULL,
    id_celda INT NOT NULL,
    fecha_hora_entrada DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_hora_salida DATETIME NULL,
    es_mensualidad BOOLEAN NOT NULL DEFAULT FALSE,
    
      -- Restricciones 
      
    CONSTRAINT fk_registros_vehiculo FOREIGN KEY (id_vehiculo) 
        REFERENCES vehiculos(id_vehiculo) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    CONSTRAINT fk_registros_celda FOREIGN KEY (id_celda) 
        REFERENCES celdas(id_celda) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    CONSTRAINT chk_registros_fechas CHECK (
        fecha_hora_salida IS NULL OR fecha_hora_salida > fecha_hora_entrada
    ),
   
    CONSTRAINT uk_vehiculo_activo UNIQUE (id_vehiculo, fecha_hora_salida)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Tabla de novedades*/
CREATE TABLE novedades (
    id_novedad INT AUTO_INCREMENT PRIMARY KEY,
    id_registro INT NOT NULL,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    descripcion TEXT NOT NULL,
    
    -- Restricciones 
    CONSTRAINT fk_novedades_registro FOREIGN KEY (id_registro) 
        REFERENCES registros_parqueo(id_registro) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    CONSTRAINT chk_novedades_descripcion CHECK (
        descripcion != '' AND LENGTH(descripcion) >= 5
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Indices de búsqueda */
CREATE INDEX idx_vehiculos_placa ON vehiculos(placa);
CREATE INDEX idx_vehiculos_usuario ON vehiculos(id_usuario);
CREATE INDEX idx_registros_vehiculo ON registros_parqueo(id_vehiculo);
CREATE INDEX idx_registros_fechas ON registros_parqueo(fecha_hora_entrada, fecha_hora_salida);
CREATE INDEX idx_celdas_estado ON celdas(estado, tipo_celda);
CREATE INDEX idx_novedades_registro ON novedades(id_registro);

/*Automatizaciones*/

-- Actualizar estado de celda al registrar entrada
DELIMITER $$
CREATE TRIGGER trg_celda_ocupar
AFTER INSERT ON registros_parqueo
FOR EACH ROW
BEGIN
    UPDATE celdas SET estado = 'Ocupada' WHERE id_celda = NEW.id_celda;
END$$
DELIMITER ;

-- Actualizar estado de celda al registrar salida
DELIMITER $$
CREATE TRIGGER trg_celda_liberar
AFTER UPDATE ON registros_parqueo
FOR EACH ROW
BEGIN
    IF NEW.fecha_hora_salida IS NOT NULL AND OLD.fecha_hora_salida IS NULL THEN
        UPDATE celdas SET estado = 'Disponible' WHERE id_celda = NEW.id_celda;
    END IF;
END$$
DELIMITER ;

-- Validar celda disponible antes de insertar
DELIMITER $$
CREATE TRIGGER trg_validar_celda_disponible
BEFORE INSERT ON registros_parqueo
FOR EACH ROW
BEGIN
    DECLARE estado_actual VARCHAR(15);
    
    SELECT estado INTO estado_actual
    FROM celdas
    WHERE id_celda = NEW.id_celda;
    
    IF estado_actual != 'Disponible' THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: La celda no está disponible';
    END IF;
END$$
DELIMITER ;

-- Validar que la fecha de entrada no sea futura
DELIMITER $$
CREATE TRIGGER trg_validar_fecha_entrada
BEFORE INSERT ON registros_parqueo
FOR EACH ROW
BEGIN
    IF NEW.fecha_hora_entrada > NOW() THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: La fecha de entrada no puede ser futura';
    END IF;
END$$
DELIMITER ;

