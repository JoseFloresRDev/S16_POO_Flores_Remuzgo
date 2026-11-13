CREATE DATABASE IF NOT EXISTS empresa_transporte
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE empresa_transporte;

CREATE TABLE IF NOT EXISTS vehiculo (
  id_vehiculo INT AUTO_INCREMENT PRIMARY KEY,
  placa VARCHAR(10) NOT NULL UNIQUE,
  marca VARCHAR(40) NOT NULL,
  modelo VARCHAR(40) NOT NULL,
  capacidad INT NOT NULL,
  conductor VARCHAR(80) NOT NULL,
  fecha_mantenimiento DATE NOT NULL,
  estado VARCHAR(20) NOT NULL DEFAULT 'Activo'
);

INSERT INTO vehiculo (placa, marca, modelo, capacidad, conductor, fecha_mantenimiento, estado) VALUES
('ABC-123','Toyota','Hiace',15,'Carlos Ramos','2026-10-15','Activo'),
('DEF-456','Mercedes-Benz','Sprinter',19,'Luis Torres','2026-09-20','Activo'),
('GHI-789','Hyundai','H1',9,'Marco Vega','2026-08-05','En mantenimiento'),
('JKL-012','Volvo','B8R',45,'Pedro Salas','2026-11-01','Activo'),
('MNO-345','Isuzu','NQR',30,'Juan Pérez','2026-07-12','Inactivo');
