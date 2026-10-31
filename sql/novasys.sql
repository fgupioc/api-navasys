-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         8.0.30 - MySQL Community Server - GPL
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para novasys
CREATE DATABASE IF NOT EXISTS `novasys` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `novasys`;

-- Volcando estructura para tabla novasys.almacenes
CREATE TABLE IF NOT EXISTS `almacenes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `cod_status` varchar(255) DEFAULT NULL,
  `code` varchar(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_default` bit(1) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `branch_id` bigint DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK9lx59aqvu5p6m8fyt558ejiut` (`code`),
  UNIQUE KEY `idx_almacenes_uuid` (`uuid`),
  KEY `FKsml41waaeie5rihfynj5etgc0` (`branch_id`),
  CONSTRAINT `FKsml41waaeie5rihfynj5etgc0` FOREIGN KEY (`branch_id`) REFERENCES `tiendas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.almacenes: ~0 rows (aproximadamente)
INSERT INTO `almacenes` (`id`, `address`, `cod_status`, `code`, `description`, `is_default`, `name`, `uuid`, `branch_id`, `created_at`, `deleted_at`, `updated_at`) VALUES
	(1, 'Av. Los Pinos 123', '1', 'ALM001', 'Almacén principal', b'1', 'Almacén Principal', _binary 0xb5b3893d87a311f1bc2f0a0027000004, 1, '2026-07-24 16:07:42.000000', NULL, NULL),
	(2, 'Av. Los Pinos 123', '1', 'ALM002', 'Almacén secundario', b'1', 'Almacén Secundario', _binary 0xb5b3893d87a311f1bc2f0a0027000003, 1, '2026-07-24 16:07:42.000000', NULL, NULL),
	(3, 'Av. Los Pinos 123', '1', 'ALM003', 'Almacén tercero', b'1', 'Almacén tercero', _binary 0xb5b3893d87a311f1bc2f0a0027000002, 1, '2026-07-24 16:07:42.000000', NULL, NULL);

-- Volcando estructura para tabla novasys.cajas
CREATE TABLE IF NOT EXISTS `cajas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` enum('CANCELLED','CLOSED','OPEN') NOT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `warehouse_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6ie0kwkxnid2xvxhjl410l0nb` (`warehouse_id`),
  CONSTRAINT `FK6ie0kwkxnid2xvxhjl410l0nb` FOREIGN KEY (`warehouse_id`) REFERENCES `almacenes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.cajas: ~5 rows (aproximadamente)
INSERT INTO `cajas` (`id`, `created_at`, `deleted_at`, `updated_at`, `code`, `description`, `name`, `status`, `uuid`, `warehouse_id`) VALUES
	(1, '2026-08-07 14:35:15.593605', NULL, '2026-10-26 17:03:54.722504', 'CAJ000001', 'Caja 01 - Principal', 'Caja 01', 'OPEN', _binary 0x1d658478929711f1a68500090faa0001, 1),
	(2, '2026-08-07 14:35:15.593605', NULL, '2026-10-27 15:30:15.369247', 'CAJ000002', 'Caja 02 - Secundaria', 'Caja 02 ', 'OPEN', _binary 0x1d66c335929711f1a68500090faa0001, 1),
	(3, '2026-08-07 14:35:15.593605', NULL, '2026-08-07 14:35:15.593605', 'CAJ000003', 'Caja 03 - Rápida', 'Caja 03', 'CLOSED', _binary 0x1d66c7f4929711f1a68500090faa0001, 2),
	(4, '2026-08-07 14:35:15.593605', NULL, '2026-08-07 14:35:15.593605', 'CAJ000004', 'Caja 04 - Atención', 'Caja 04 ', 'CLOSED', _binary 0x1d66c916929711f1a68500090faa0001, 2),
	(5, '2026-08-07 14:35:15.593605', NULL, '2026-08-07 14:35:15.593605', 'CAJ000005', 'Caja 05 - Respaldo', 'Caja 05', 'CLOSED', _binary 0x1d66c9de929711f1a68500090faa0001, 3);

-- Volcando estructura para tabla novasys.caja_movimiento
CREATE TABLE IF NOT EXISTS `caja_movimiento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `amount` decimal(12,2) DEFAULT NULL,
  `description` varchar(300) DEFAULT NULL,
  `movement_date` datetime(6) DEFAULT NULL,
  `type` enum('ADJUSTMENT','CLOSING','EXPENSE','INCOME','OPENING') DEFAULT NULL,
  `caja_sesion_id` bigint DEFAULT NULL,
  `reason_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKch3pio8i460wq3iywrs6i2tyb` (`caja_sesion_id`),
  KEY `FK5u5apemgc68pni6upe9xl72tu` (`reason_id`),
  KEY `FKd8fcs62v01i2v9daqhig8asx3` (`user_id`),
  CONSTRAINT `FK5u5apemgc68pni6upe9xl72tu` FOREIGN KEY (`reason_id`) REFERENCES `caja_movimiento_motivo` (`id`),
  CONSTRAINT `FKch3pio8i460wq3iywrs6i2tyb` FOREIGN KEY (`caja_sesion_id`) REFERENCES `caja_sesion` (`id`),
  CONSTRAINT `FKd8fcs62v01i2v9daqhig8asx3` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.caja_movimiento: ~0 rows (aproximadamente)

-- Volcando estructura para tabla novasys.caja_movimiento_motivo
CREATE TABLE IF NOT EXISTS `caja_movimiento_motivo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `movement_type` enum('ADJUSTMENT','CLOSING','EXPENSE','INCOME','OPENING') NOT NULL,
  `name` varchar(100) NOT NULL,
  `sort_order` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKen31mfx0q7tyksct1oolyo06i` (`movement_type`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.caja_movimiento_motivo: ~11 rows (aproximadamente)
INSERT INTO `caja_movimiento_motivo` (`id`, `active`, `description`, `movement_type`, `name`, `sort_order`) VALUES
	(1, b'1', 'Registro inicial de apertura de caja.', 'OPENING', 'Apertura de caja', 1),
	(2, b'1', 'Ingreso de efectivo para aumentar el fondo de caja.', 'INCOME', 'Fondo adicional', 1),
	(3, b'1', 'Ingreso temporal de dinero.', 'INCOME', 'Préstamo de caja', 2),
	(4, b'1', 'Reintegro de un gasto previamente registrado.', 'INCOME', 'Devolución de gasto', 3),
	(5, b'1', 'Salida de efectivo para una compra urgente.', 'EXPENSE', 'Compra urgente', 1),
	(6, b'1', 'Pago de servicios básicos o administrativos.', 'EXPENSE', 'Pago de servicios', 2),
	(7, b'1', 'Compra de materiales o insumos.', 'EXPENSE', 'Compra de insumos', 3),
	(8, b'1', 'Pago realizado a un proveedor.', 'EXPENSE', 'Pago a proveedor', 4),
	(9, b'0', 'Ajuste por diferencia encontrada en el arqueo.', 'ADJUSTMENT', 'Corrección de diferencia', 1),
	(10, b'0', 'Corrección por error en un movimiento registrado.', 'ADJUSTMENT', 'Error de registro', 2),
	(11, b'1', 'Registro del cierre de la sesión de caja.', 'CLOSING', 'Cierre de caja', 1);

-- Volcando estructura para tabla novasys.caja_sesion
CREATE TABLE IF NOT EXISTS `caja_sesion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cash_difference_amount` decimal(38,2) DEFAULT NULL,
  `cash_in_amount` decimal(38,2) DEFAULT NULL,
  `cash_out_amount` decimal(38,2) DEFAULT NULL,
  `cash_sales_amount` decimal(38,2) DEFAULT NULL,
  `closed_at` datetime(6) DEFAULT NULL,
  `closing_amount` decimal(18,2) DEFAULT NULL,
  `collections_amount` decimal(38,2) DEFAULT NULL,
  `counted_cash_amount` decimal(38,2) DEFAULT NULL,
  `digital_wallet_amount` decimal(38,2) DEFAULT NULL,
  `executive_cash_amount` decimal(38,2) DEFAULT NULL,
  `expected_cash_amount` decimal(38,2) DEFAULT NULL,
  `movements_count` int DEFAULT NULL,
  `notes` varchar(501) DEFAULT NULL,
  `opened_at` datetime(6) DEFAULT NULL,
  `opening_amount` decimal(18,2) NOT NULL,
  `physical_cash_amount` decimal(38,2) DEFAULT NULL,
  `sales_count` int DEFAULT NULL,
  `status` enum('CANCELLED','CLOSED','OPEN') NOT NULL,
  `total_payments_amount` decimal(38,2) DEFAULT NULL,
  `cash_register_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhl4lyi9nhtaw2iqqutjcaqej2` (`cash_register_id`),
  KEY `FKhv2tb0htjfnnq61x6skr4c4dh` (`user_id`),
  CONSTRAINT `FKhl4lyi9nhtaw2iqqutjcaqej2` FOREIGN KEY (`cash_register_id`) REFERENCES `cajas` (`id`),
  CONSTRAINT `FKhv2tb0htjfnnq61x6skr4c4dh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.caja_sesion: ~5 rows (aproximadamente)
INSERT INTO `caja_sesion` (`id`, `created_at`, `deleted_at`, `updated_at`, `cash_difference_amount`, `cash_in_amount`, `cash_out_amount`, `cash_sales_amount`, `closed_at`, `closing_amount`, `collections_amount`, `counted_cash_amount`, `digital_wallet_amount`, `executive_cash_amount`, `expected_cash_amount`, `movements_count`, `notes`, `opened_at`, `opening_amount`, `physical_cash_amount`, `sales_count`, `status`, `total_payments_amount`, `cash_register_id`, `user_id`) VALUES
	(1, '2026-08-07 14:36:25.803015', NULL, '2026-08-07 14:36:25.803015', 0.00, 0.00, 0.00, 0.00, NULL, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '', '2026-08-07 14:36:25.789219', 120.00, 0.00, 0, 'CLOSED', 0.00, 1, 4),
	(2, '2026-08-08 11:31:31.769324', NULL, '2026-08-08 11:31:31.769324', 0.00, 0.00, 0.00, 0.00, NULL, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '', '2026-08-08 11:31:31.757334', 80.00, 0.00, 0, 'CLOSED', 0.00, 1, 4),
	(3, '2026-08-09 11:06:34.039193', NULL, '2026-08-09 11:06:34.039193', 0.00, 0.00, 0.00, 0.00, NULL, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '', '2026-08-09 11:06:34.024231', 0.00, 0.00, 0, 'CLOSED', 0.00, 1, 4),
	(4, '2026-08-20 14:03:32.674205', NULL, '2026-08-20 14:03:32.674205', 0.00, 0.00, 0.00, 0.00, NULL, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '', '2026-08-20 14:03:32.650505', 0.00, 0.00, 0, 'CLOSED', 0.00, 1, 1),
	(5, '2026-08-20 14:11:19.921889', NULL, '2026-08-20 14:11:19.921889', 0.00, 0.00, 0.00, 0.00, NULL, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '', '2026-08-20 14:11:19.920343', 0.00, 0.00, 0, 'CLOSED', 0.00, 2, 4),
	(6, '2026-10-26 17:03:54.690833', NULL, '2026-10-26 17:03:54.690833', 0.00, 0.00, 0.00, 0.00, NULL, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '', '2026-10-26 17:03:54.680793', 0.00, 0.00, 0, 'OPEN', 0.00, 1, 1),
	(7, '2026-10-27 15:30:15.332855', NULL, '2026-10-27 15:30:15.332855', 0.00, 0.00, 0.00, 0.00, NULL, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '', '2026-10-27 15:30:15.320363', 0.00, 0.00, 0, 'OPEN', 0.00, 2, 1);

-- Volcando estructura para tabla novasys.categorias
CREATE TABLE IF NOT EXISTS `categorias` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cod_status` varchar(255) NOT NULL,
  `code` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKj35i5tkyea55ce29tcshy221e` (`code`),
  KEY `FKs16jgrnxeaeknm1ypa44932bw` (`parent_id`),
  CONSTRAINT `FKs16jgrnxeaeknm1ypa44932bw` FOREIGN KEY (`parent_id`) REFERENCES `categorias` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.categorias: ~90 rows (aproximadamente)
INSERT INTO `categorias` (`id`, `created_at`, `deleted_at`, `updated_at`, `cod_status`, `code`, `name`, `parent_id`) VALUES
	(1, NULL, NULL, NULL, '1', 'ABA', 'Abarrotes', NULL),
	(2, NULL, NULL, NULL, '1', 'BEB', 'Bebidas', NULL),
	(3, NULL, NULL, NULL, '1', 'LAC', 'Lácteos', NULL),
	(4, NULL, NULL, NULL, '1', 'PAN', 'Panadería', NULL),
	(5, NULL, NULL, NULL, '1', 'SNK', 'Snacks', NULL),
	(6, NULL, NULL, NULL, '1', 'GOL', 'Golosinas', NULL),
	(7, NULL, NULL, NULL, '1', 'LIM', 'Limpieza', NULL),
	(8, NULL, NULL, NULL, '1', 'CUI', 'Cuidado Personal', NULL),
	(9, NULL, NULL, NULL, '1', 'BAB', 'Bebés', NULL),
	(10, NULL, NULL, NULL, '1', 'MAS', 'Mascotas', NULL),
	(11, NULL, NULL, NULL, '1', 'CON', 'Congelados', NULL),
	(12, NULL, NULL, NULL, '1', 'FRU', 'Frutas y Verduras', NULL),
	(13, NULL, NULL, NULL, '1', 'CAR', 'Carnes', NULL),
	(14, NULL, NULL, NULL, '1', 'HUE', 'Huevos', NULL),
	(15, NULL, NULL, NULL, '1', 'HOG', 'Útiles del Hogar', NULL),
	(16, NULL, NULL, NULL, '1', 'ABA001', 'Arroz', 1),
	(17, NULL, NULL, NULL, '1', 'ABA002', 'Azúcar', 1),
	(18, NULL, NULL, NULL, '1', 'ABA003', 'Fideos', 1),
	(19, NULL, NULL, NULL, '1', 'ABA004', 'Aceites', 1),
	(20, NULL, NULL, NULL, '1', 'ABA005', 'Harinas', 1),
	(21, NULL, NULL, NULL, '1', 'ABA006', 'Menestras', 1),
	(22, NULL, NULL, NULL, '1', 'ABA007', 'Conservas', 1),
	(23, NULL, NULL, NULL, '1', 'ABA008', 'Salsas', 1),
	(24, NULL, NULL, NULL, '1', 'ABA009', 'Condimentos', 1),
	(25, NULL, NULL, NULL, '1', 'BEB001', 'Agua', 2),
	(26, NULL, NULL, NULL, '1', 'BEB002', 'Gaseosas', 2),
	(27, NULL, NULL, NULL, '1', 'BEB003', 'Jugos', 2),
	(28, NULL, NULL, NULL, '1', 'BEB004', 'Energizantes', 2),
	(29, NULL, NULL, NULL, '1', 'BEB005', 'Rehidratantes', 2),
	(30, NULL, NULL, NULL, '1', 'BEB006', 'Cervezas', 2),
	(31, NULL, NULL, NULL, '1', 'BEB007', 'Licores', 2),
	(32, NULL, NULL, NULL, '1', 'LAC001', 'Leche', 3),
	(33, NULL, NULL, NULL, '1', 'LAC002', 'Yogurt', 3),
	(34, NULL, NULL, NULL, '1', 'LAC003', 'Quesos', 3),
	(35, NULL, NULL, NULL, '1', 'LAC004', 'Mantequilla', 3),
	(36, NULL, NULL, NULL, '1', 'LAC005', 'Margarina', 3),
	(37, NULL, NULL, NULL, '1', 'PAN001', 'Pan', 4),
	(38, NULL, NULL, NULL, '1', 'PAN002', 'Galletas', 4),
	(39, NULL, NULL, NULL, '1', 'PAN003', 'Tortas', 4),
	(40, NULL, NULL, NULL, '1', 'PAN004', 'Queques', 4),
	(41, NULL, NULL, NULL, '1', 'PAN005', 'Bizcochos', 4),
	(42, NULL, NULL, NULL, '1', 'SNK001', 'Papas Fritas', 5),
	(43, NULL, NULL, NULL, '1', 'SNK002', 'Chifles', 5),
	(44, NULL, NULL, NULL, '1', 'SNK003', 'Canchita', 5),
	(45, NULL, NULL, NULL, '1', 'SNK004', 'Frutos Secos', 5),
	(46, NULL, NULL, NULL, '1', 'SNK005', 'Mix Snacks', 5),
	(47, NULL, NULL, NULL, '1', 'GOL001', 'Chocolates', 6),
	(48, NULL, NULL, NULL, '1', 'GOL002', 'Caramelos', 6),
	(49, NULL, NULL, NULL, '1', 'GOL003', 'Chicles', 6),
	(50, NULL, NULL, NULL, '1', 'GOL004', 'Gomitas', 6),
	(51, NULL, NULL, NULL, '1', 'GOL005', 'Paletas', 6),
	(52, NULL, NULL, NULL, '1', 'LIM001', 'Detergentes', 7),
	(53, NULL, NULL, NULL, '1', 'LIM002', 'Lejías', 7),
	(54, NULL, NULL, NULL, '1', 'LIM003', 'Desinfectantes', 7),
	(55, NULL, NULL, NULL, '1', 'LIM004', 'Lavavajillas', 7),
	(56, NULL, NULL, NULL, '1', 'LIM005', 'Suavizantes', 7),
	(57, NULL, NULL, NULL, '1', 'LIM006', 'Escobas', 7),
	(58, NULL, NULL, NULL, '1', 'LIM007', 'Trapeadores', 7),
	(59, NULL, NULL, NULL, '1', 'CUI001', 'Shampoo', 8),
	(60, NULL, NULL, NULL, '1', 'CUI002', 'Acondicionador', 8),
	(61, NULL, NULL, NULL, '1', 'CUI003', 'Jabones', 8),
	(62, NULL, NULL, NULL, '1', 'CUI004', 'Pasta Dental', 8),
	(63, NULL, NULL, NULL, '1', 'CUI005', 'Cepillos Dentales', 8),
	(64, NULL, NULL, NULL, '1', 'CUI006', 'Desodorantes', 8),
	(65, NULL, NULL, NULL, '1', 'CUI007', 'Papel Higiénico', 8),
	(66, NULL, NULL, NULL, '1', 'BAB001', 'Pañales', 9),
	(67, NULL, NULL, NULL, '1', 'BAB002', 'Toallitas Húmedas', 9),
	(68, NULL, NULL, NULL, '1', 'BAB003', 'Leche Infantil', 9),
	(69, NULL, NULL, NULL, '1', 'BAB004', 'Biberones', 9),
	(70, NULL, NULL, NULL, '1', 'BAB005', 'Talcos', 9),
	(71, NULL, NULL, NULL, '1', 'MAS001', 'Alimento para Perros', 10),
	(72, NULL, NULL, NULL, '1', 'MAS002', 'Alimento para Gatos', 10),
	(73, NULL, NULL, NULL, '1', 'MAS003', 'Snacks para Mascotas', 10),
	(74, NULL, NULL, NULL, '1', 'MAS004', 'Arena Sanitaria', 10),
	(75, NULL, NULL, NULL, '1', 'MAS005', 'Accesorios', 10),
	(76, NULL, NULL, NULL, '1', 'CON001', 'Helados', 11),
	(77, NULL, NULL, NULL, '1', 'CON002', 'Carnes Congeladas', 11),
	(78, NULL, NULL, NULL, '1', 'CON003', 'Pollo Congelado', 11),
	(79, NULL, NULL, NULL, '1', 'CON004', 'Verduras Congeladas', 11),
	(80, NULL, NULL, NULL, '1', 'FRU001', 'Frutas', 12),
	(81, NULL, NULL, NULL, '1', 'FRU002', 'Verduras', 12),
	(82, NULL, NULL, NULL, '1', 'FRU003', 'Tubérculos', 12),
	(83, NULL, NULL, NULL, '1', 'CAR001', 'Pollo', 13),
	(84, NULL, NULL, NULL, '1', 'CAR002', 'Res', 13),
	(85, NULL, NULL, NULL, '1', 'CAR003', 'Cerdo', 13),
	(86, NULL, NULL, NULL, '1', 'CAR004', 'Pavo', 13),
	(87, NULL, NULL, NULL, '1', 'CAR005', 'Embutidos', 13),
	(88, NULL, NULL, NULL, '1', 'HUE001', 'Huevos Blancos', 14),
	(89, NULL, NULL, NULL, '1', 'HUE002', 'Huevos Pardos', 14),
	(90, NULL, NULL, '2026-07-21 21:16:11.168127', '1', 'HUE003', 'Huevos de Codorniz', 14),
	(91, NULL, NULL, NULL, '1', 'HOG001', 'Pilas', 15),
	(92, NULL, NULL, NULL, '1', 'HOG002', 'Velas', 15),
	(93, NULL, NULL, NULL, '1', 'HOG003', 'Fósforos', 15),
	(94, NULL, NULL, NULL, '1', 'HOG004', 'Bolsas', 15),
	(95, NULL, NULL, NULL, '1', 'HOG005', 'Papel Aluminio', 15),
	(96, NULL, NULL, '2026-07-21 21:15:44.309318', '1', 'HOG006', 'Film Plástico', 15),
	(97, '2026-07-21 17:55:27.954606', NULL, '2026-07-21 21:24:24.549926', '0', 'HUE004', 'Huevos De Codorniz', 14);

-- Volcando estructura para tabla novasys.clientes
CREATE TABLE IF NOT EXISTS `clientes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cod_status` varchar(255) DEFAULT NULL,
  `document_number` varchar(255) DEFAULT NULL,
  `document_type` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.clientes: ~30 rows (aproximadamente)
INSERT INTO `clientes` (`id`, `created_at`, `deleted_at`, `updated_at`, `cod_status`, `document_number`, `document_type`, `first_name`, `last_name`, `phone`) VALUES
	(1, NULL, NULL, NULL, '1', '00000000', 'DNI', 'Publico en General', '', ''),
	(2, NULL, NULL, NULL, '1', '45896321', 'DNI', 'María', 'López Torres', '956321478'),
	(3, NULL, NULL, NULL, '1', '73452189', 'DNI', 'Carlos', 'Ramírez Díaz', '945123678'),
	(4, NULL, NULL, NULL, '1', '40125698', 'DNI', 'Ana', 'Sánchez Ruiz', '998741236'),
	(5, NULL, NULL, NULL, '1', '76985412', 'DNI', 'Luis', 'Castillo Flores', '977456123'),
	(6, NULL, NULL, NULL, '1', '48963215', 'DNI', 'Rosa', 'Vargas Mendoza', '966852741'),
	(7, NULL, NULL, NULL, '1', '72369854', 'DNI', 'Pedro', 'Morales Silva', '955741236'),
	(8, NULL, NULL, NULL, '1', '45698712', 'DNI', 'Lucía', 'Fernández Gómez', '944258963'),
	(9, NULL, NULL, NULL, '1', '78541236', 'DNI', 'Miguel', 'Herrera León', '933852741'),
	(10, NULL, NULL, NULL, '1', '46985213', 'DNI', 'Patricia', 'Navarro Rojas', '922147896'),
	(11, NULL, NULL, NULL, '1', 'CE00000001', 'CEX', 'José', 'Martínez Gómez', '901111111'),
	(12, NULL, NULL, NULL, '1', 'CE00000002', 'CEX', 'Andrea', 'Rodríguez Pérez', '902222222'),
	(13, NULL, NULL, NULL, '1', 'CE00000003', 'CEX', 'Miguel', 'Fernández Castro', '903333333'),
	(14, NULL, NULL, NULL, '1', 'CE00000004', 'CEX', 'Camila', 'Silva Torres', '904444444'),
	(15, NULL, NULL, NULL, '1', 'CE00000005', 'CEX', 'Daniel', 'Moreno Ruiz', '905555555'),
	(16, NULL, NULL, NULL, '1', 'CE00000006', 'CEX', 'Valentina', 'Herrera López', '906666666'),
	(17, NULL, NULL, NULL, '1', 'CE00000007', 'CEX', 'Sebastián', 'Rojas Mendoza', '907777777'),
	(18, NULL, NULL, NULL, '1', 'CE00000008', 'CEX', 'Gabriela', 'Navarro Díaz', '908888888'),
	(19, NULL, NULL, NULL, '1', 'CE00000009', 'CEX', 'Alejandro', 'Paredes Salazar', '909999999'),
	(20, NULL, NULL, NULL, '1', 'CE00000010', 'CEX', 'Natalia', 'Vega Castillo', '910101010'),
	(21, NULL, NULL, NULL, '1', '20601234567', 'RUC', 'Distribuidora Los Andes SAC', '', '987111222'),
	(22, NULL, NULL, NULL, '1', '20602345678', 'RUC', 'Comercial El Sol SAC', '', '987222333'),
	(23, NULL, NULL, NULL, '1', '20603456789', 'RUC', 'Inversiones Santa Rosa SAC', '', '987333444'),
	(24, NULL, NULL, NULL, '1', '20604567890', 'RUC', 'Minimarket San José EIRL', '', '987444555'),
	(25, NULL, NULL, NULL, '1', '20605678901', 'RUC', 'Distribuciones Norte SAC', '', '987555666'),
	(26, NULL, NULL, NULL, '1', '20606789012', 'RUC', 'Importadora Perú SAC', '', '987666777'),
	(27, NULL, NULL, NULL, '1', '20607890123', 'RUC', 'Corporación Andina SAC', '', '987777888'),
	(28, NULL, NULL, NULL, '1', '20608901234', 'RUC', 'Comercial La Familia SAC', '', '987888999'),
	(29, NULL, NULL, NULL, '1', '20609012345', 'RUC', 'Inversiones Trujillo SAC', '', '987999111'),
	(30, NULL, NULL, NULL, '1', '20610123456', 'RUC', 'Mercados Del Norte SAC', '', '987123456'),
	(31, '2026-07-23 22:20:21.474592', NULL, '2026-07-23 22:26:03.934117', '1', '46588976', 'DNI', 'FRANZ JUNIOR', 'GUPIOC VENTURA', '904748025'),
	(32, '2026-07-26 18:19:23.417812', NULL, '2026-07-26 18:19:23.417812', '1', '33780761', 'DNI', 'MARIA GENOVEBA', 'VENTURA ALVIS', '923876487');

-- Volcando estructura para tabla novasys.compras
CREATE TABLE IF NOT EXISTS `compras` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cod_status` varchar(255) DEFAULT NULL,
  `code` varchar(20) NOT NULL,
  `created_by` bigint DEFAULT NULL,
  `currency` varchar(5) DEFAULT NULL,
  `discount` decimal(12,2) DEFAULT NULL,
  `document_number` varchar(20) DEFAULT NULL,
  `document_series` varchar(10) DEFAULT NULL,
  `document_type` varchar(5) NOT NULL,
  `observation` varchar(300) DEFAULT NULL,
  `payment_type` varchar(20) DEFAULT NULL,
  `purchase_date` date NOT NULL,
  `subtotal` decimal(12,2) NOT NULL,
  `tax` decimal(12,2) DEFAULT NULL,
  `tax_applied` decimal(38,2) DEFAULT NULL,
  `total` decimal(12,2) NOT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `warehouse_id` bigint NOT NULL,
  `supplier_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhhq486r8alj9mabpka72jea85` (`code`),
  KEY `FKq3vvqbw3tqdfobf7w5aa3htha` (`warehouse_id`),
  KEY `FK7d7m3i91svklc86u89dsbwuiv` (`supplier_id`),
  KEY `FKi31wdh233dekj8mhprb6fl0so` (`user_id`),
  CONSTRAINT `FK7d7m3i91svklc86u89dsbwuiv` FOREIGN KEY (`supplier_id`) REFERENCES `proveedores` (`id`),
  CONSTRAINT `FKi31wdh233dekj8mhprb6fl0so` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKq3vvqbw3tqdfobf7w5aa3htha` FOREIGN KEY (`warehouse_id`) REFERENCES `almacenes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.compras: ~0 rows (aproximadamente)
INSERT INTO `compras` (`id`, `created_at`, `deleted_at`, `updated_at`, `cod_status`, `code`, `created_by`, `currency`, `discount`, `document_number`, `document_series`, `document_type`, `observation`, `payment_type`, `purchase_date`, `subtotal`, `tax`, `tax_applied`, `total`, `uuid`, `warehouse_id`, `supplier_id`, `user_id`) VALUES
	(1, '2026-08-08 14:42:49.092324', NULL, '2026-08-08 14:42:56.331127', '5', 'COM000001', 1, 'PEN', 0.00, '0001', 'EE01', '01', '', '1', '2026-08-08', 128.81, 23.19, 0.18, 152.00, _binary 0x2e651f2e47bd43818b8dac6ccb01f579, 1, 15, 4),
	(2, '2026-08-08 14:43:25.219575', NULL, '2026-08-08 14:43:31.325382', '5', 'COM000002', 1, 'PEN', 0.00, '0002', 'EE01', '01', '', '1', '2026-08-08', 3.22, 0.58, 0.18, 3.80, _binary 0x2b09f27635bd4b0aaee0b608e8c5fe52, 1, 14, 4),
	(3, '2026-08-20 14:05:55.335709', NULL, '2026-08-20 14:06:49.184060', '5', 'COM000003', 1, 'PEN', 0.00, '0000', 'EE01', '01', '1231', '1', '2026-08-20', 96.61, 17.39, 0.18, 114.00, _binary 0x193ae04b2f4644108f0e15d313b0ca41, 2, 15, 1);

-- Volcando estructura para tabla novasys.compra_detalle
CREATE TABLE IF NOT EXISTS `compra_detalle` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `discount` decimal(38,2) DEFAULT NULL,
  `quantity` decimal(38,2) DEFAULT NULL,
  `sale_price` decimal(38,2) DEFAULT NULL,
  `subtotal` decimal(38,2) DEFAULT NULL,
  `unit_cost` decimal(38,2) DEFAULT NULL,
  `purchase_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd1f7mw28fsjagdyhpf4tqscsi` (`purchase_id`),
  KEY `FKry6p6ckgj0x398ymfq79a3tga` (`product_id`),
  CONSTRAINT `FKd1f7mw28fsjagdyhpf4tqscsi` FOREIGN KEY (`purchase_id`) REFERENCES `compras` (`id`),
  CONSTRAINT `FKry6p6ckgj0x398ymfq79a3tga` FOREIGN KEY (`product_id`) REFERENCES `productos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.compra_detalle: ~3 rows (aproximadamente)
INSERT INTO `compra_detalle` (`id`, `discount`, `quantity`, `sale_price`, `subtotal`, `unit_cost`, `purchase_id`, `product_id`) VALUES
	(2, 0.00, 40.00, 5.00, 152.00, 3.80, 1, 1),
	(4, 0.00, 1.00, 5.00, 3.80, 3.80, 2, 1),
	(6, 0.00, 30.00, 5.20, 114.00, 3.80, 3, 1);

-- Volcando estructura para tabla novasys.inventario_movimiento
CREATE TABLE IF NOT EXISTS `inventario_movimiento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cod_status` varchar(255) DEFAULT NULL,
  `quantity` decimal(38,2) NOT NULL,
  `reference_id` bigint NOT NULL,
  `sale_price` decimal(12,2) DEFAULT NULL,
  `stock_after` decimal(38,2) NOT NULL,
  `stock_before` decimal(38,2) NOT NULL,
  `movement_type` enum('ADJUSTMENT_IN','ADJUSTMENT_OUT','PURCHASE_IN','PURCHASE_RETURN','SALE_OUT','SALE_RETURN','TRANSFER_IN','TRANSFER_OUT') NOT NULL,
  `reference_type` enum('ADJUSTMENT','PURCHASE','SALE','TRANSFER') NOT NULL,
  `unit_cost` decimal(12,2) DEFAULT NULL,
  `warehouse_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKix7lpl02su0bfqif0mjy0ce0k` (`warehouse_id`),
  KEY `FK70xlig3386l5wwon3890o54xl` (`product_id`),
  KEY `FK6o9nq6uwg9insjuu1ipx0gn0x` (`user_id`),
  CONSTRAINT `FK6o9nq6uwg9insjuu1ipx0gn0x` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK70xlig3386l5wwon3890o54xl` FOREIGN KEY (`product_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `FKix7lpl02su0bfqif0mjy0ce0k` FOREIGN KEY (`warehouse_id`) REFERENCES `almacenes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.inventario_movimiento: ~5 rows (aproximadamente)
INSERT INTO `inventario_movimiento` (`id`, `cod_status`, `quantity`, `reference_id`, `sale_price`, `stock_after`, `stock_before`, `movement_type`, `reference_type`, `unit_cost`, `warehouse_id`, `product_id`, `user_id`) VALUES
	(1, '1', 40.00, 1, 5.00, 40.00, 0.00, 'PURCHASE_IN', 'PURCHASE', 3.80, 1, 1, 4),
	(2, '1', 5.00, 1, 5.00, 35.00, 40.00, 'SALE_OUT', 'SALE', 5.00, 1, 1, 4),
	(3, '1', 1.00, 2, 5.00, 36.00, 35.00, 'PURCHASE_IN', 'PURCHASE', 3.80, 1, 1, 4),
	(4, '1', 30.00, 3, 5.20, 30.00, 0.00, 'PURCHASE_IN', 'PURCHASE', 3.80, 2, 1, 1),
	(5, '1', 5.00, 2, 5.00, 31.00, 36.00, 'SALE_OUT', 'SALE', 5.00, 1, 1, 1);

-- Volcando estructura para tabla novasys.inventario_stock
CREATE TABLE IF NOT EXISTS `inventario_stock` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `average_cost` decimal(12,2) DEFAULT NULL,
  `cod_status` varchar(255) NOT NULL,
  `initial_quantity` decimal(12,2) NOT NULL,
  `last_cost` decimal(12,2) DEFAULT NULL,
  `quantity` decimal(12,2) NOT NULL,
  `warehouse_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_inventario_stock_producto_almacen` (`product_id`,`warehouse_id`),
  KEY `FKkkfo5291q12hfhgoobephypws` (`warehouse_id`),
  CONSTRAINT `FKkkfo5291q12hfhgoobephypws` FOREIGN KEY (`warehouse_id`) REFERENCES `almacenes` (`id`),
  CONSTRAINT `FKpn8am82x2kme925v7b87fmy15` FOREIGN KEY (`product_id`) REFERENCES `productos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.inventario_stock: ~0 rows (aproximadamente)
INSERT INTO `inventario_stock` (`id`, `average_cost`, `cod_status`, `initial_quantity`, `last_cost`, `quantity`, `warehouse_id`, `product_id`) VALUES
	(1, 0.11, '1', 35.00, 0.00, 31.00, 1, 1),
	(2, 3.80, '1', 0.00, 0.00, 30.00, 2, 1);

-- Volcando estructura para tabla novasys.maestro
CREATE TABLE IF NOT EXISTS `maestro` (
  `codigo` varchar(255) NOT NULL,
  `tabla_id` int NOT NULL,
  `activo` bit(1) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `valor_numerico` double DEFAULT NULL,
  `valor_string` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`codigo`,`tabla_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.maestro: ~47 rows (aproximadamente)
INSERT INTO `maestro` (`codigo`, `tabla_id`, `activo`, `nombre`, `valor_numerico`, `valor_string`) VALUES
	('-', 1, b'0', 'Estados', NULL, ' '),
	('-', 2, b'0', 'Tipo de Unidades', NULL, NULL),
	('-', 3, b'1', 'Estados de compra', NULL, NULL),
	('-', 4, b'1', 'Estado de Venta', NULL, ''),
	('-', 5, b'1', 'Tipo de Pagos', NULL, ''),
	('-', 6, b'1', 'Documentos SUNAT', NULL, NULL),
	('-', 7, b'1', 'tipo referencia inventario movimiento', NULL, NULL),
	('-', 8, b'1', 'tipo movimientos inventario movimiento', NULL, NULL),
	('0', 1, b'1', 'Inactivo', NULL, NULL),
	('01', 6, b'1', 'Factura', NULL, ''),
	('03', 6, b'1', 'Boleta', NULL, ''),
	('07', 6, b'1', 'Nota de Crédito', NULL, ''),
	('08', 6, b'1', 'Nota de Débito', NULL, ''),
	('09', 6, b'1', 'Guía de Remisión', NULL, ''),
	('1', 1, b'1', 'Activo', NULL, NULL),
	('1', 3, b'1', 'BORRADOR', NULL, 'badge-draft'),
	('1', 4, b'1', 'COMPLETADA', NULL, 'badge-completed'),
	('1', 5, b'1', 'EFECTIVO', NULL, 'badge-completed'),
	('12', 6, b'1', 'Ticket', NULL, ''),
	('2', 3, b'1', 'PENDIENTE', NULL, 'badge-pending'),
	('2', 4, b'1', 'ANULADA', NULL, 'badge-cancelled'),
	('2', 5, b'1', 'TARJETA', NULL, 'badge-partial'),
	('3', 3, b'1', 'RECIBIDA', NULL, 'badge-received'),
	('3', 4, b'1', 'PENDIENTE', NULL, 'badge-pending'),
	('3', 5, b'1', 'TRANSFERENCIA', NULL, 'badge-returned'),
	('4', 3, b'1', 'PARCIAL', NULL, 'badge-partial'),
	('4', 4, b'1', 'DEVUELTA', NULL, 'badge-returned'),
	('4', 5, b'1', 'YAPE/PLIN', NULL, 'badge-pending'),
	('5', 3, b'1', 'COMPLETADA', NULL, 'badge-completed'),
	('5', 5, b'1', 'MIXTO', NULL, 'badge-draft'),
	('6', 3, b'1', 'ANULADA', NULL, 'badge-cancelled'),
	('7', 3, b'1', 'DEVUELTA', NULL, 'badge-returned'),
	('ADJUSTMENT', 7, b'1', 'Ajuste', NULL, NULL),
	('ADJUSTMENT_IN', 8, b'1', 'Ajuste Entrada', NULL, NULL),
	('ADJUSTMENT_OUT', 8, b'1', 'Ajuste Salida', NULL, NULL),
	('C', 2, b'1', 'Cantidad', NULL, NULL),
	('P', 2, b'1', 'Peso', NULL, NULL),
	('PURCHASE', 7, b'1', 'Compra', NULL, NULL),
	('PURCHASE_IN', 8, b'1', 'Compra a Proveedor', NULL, NULL),
	('PURCHASE_RETURN', 8, b'1', 'Compra Devolución', NULL, NULL),
	('SALE', 7, b'1', 'Venta', NULL, NULL),
	('SALE_OUT', 8, b'1', 'Venta POS', NULL, NULL),
	('SALE_RETURN', 8, b'1', 'Venta Devolución', NULL, NULL),
	('TRANSFER', 7, b'1', 'Transferencia', NULL, NULL),
	('TRANSFER_IN', 8, b'1', 'Transferencia Entrada', NULL, NULL),
	('TRANSFER_OUT', 8, b'1', 'Transferencia Salida', NULL, NULL),
	('V', 2, b'1', 'Volumen', NULL, NULL);

-- Volcando estructura para tabla novasys.marcas
CREATE TABLE IF NOT EXISTS `marcas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cod_status` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.marcas: ~4 rows (aproximadamente)
INSERT INTO `marcas` (`id`, `created_at`, `deleted_at`, `updated_at`, `cod_status`, `code`, `name`) VALUES
	(1, '2026-07-21 08:56:43.604337', NULL, '2026-07-21 08:56:43.604337', '1', 'GLO', 'Gloria'),
	(2, '2026-07-21 08:56:56.914910', NULL, '2026-07-21 08:56:56.914910', '1', 'COC', 'Coca Cola'),
	(3, '2026-07-21 08:57:04.158892', NULL, '2026-07-21 08:57:04.158892', '1', 'SAM', 'Samsung'),
	(4, '2026-07-21 08:57:12.110828', NULL, '2026-07-21 08:57:22.766265', '1', 'NES', 'Nestlé');

-- Volcando estructura para tabla novasys.permissions
CREATE TABLE IF NOT EXISTS `permissions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `code` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7lcb6glmvwlro3p2w2cewxtvd` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.permissions: ~47 rows (aproximadamente)
INSERT INTO `permissions` (`id`, `created_at`, `deleted_at`, `updated_at`, `code`, `description`, `name`) VALUES
	(1, NULL, NULL, NULL, 'productos.ver', 'Consultar el catálogo y buscar productos', 'Ver productos'),
	(2, NULL, NULL, NULL, 'productos.crear', 'Dar de alta productos nuevos', 'Registrar productos'),
	(3, NULL, NULL, NULL, 'productos.editar', 'Modificar datos del producto', 'Editar productos'),
	(4, NULL, NULL, NULL, 'productos.eliminar', 'Baja lógica de productos', 'Eliminar productos'),
	(5, NULL, NULL, NULL, 'productos.costo.ver', 'Ver último costo y margen de utilidad', 'Ver costo y margen'),
	(6, NULL, NULL, NULL, 'productos.precios.ver', 'Consultar precios por almacén', 'Ver precios'),
	(7, NULL, NULL, NULL, 'productos.precios.editar', 'Fijar o modificar precios de venta', 'Editar precios'),
	(8, NULL, NULL, NULL, 'maestros.ver', 'Categorías, marcas y unidades', 'Ver maestros'),
	(9, NULL, NULL, NULL, 'maestros.gestionar', 'Crear y editar categorías, marcas y unidades', 'Gestionar maestros'),
	(10, NULL, NULL, NULL, 'proveedores.ver', 'Consultar proveedores', 'Ver proveedores'),
	(11, NULL, NULL, NULL, 'proveedores.gestionar', 'Crear y editar proveedores', 'Gestionar proveedores'),
	(12, NULL, NULL, NULL, 'compras.ver', 'Consultar compras registradas', 'Ver compras'),
	(13, NULL, NULL, NULL, 'compras.crear', 'Ingresar mercadería al almacén', 'Registrar compras'),
	(14, NULL, NULL, NULL, 'compras.anular', 'Anular una compra y revertir su stock', 'Anular compras'),
	(15, NULL, NULL, NULL, 'inventario.ver', 'Consultar stock por almacén', 'Ver inventario'),
	(16, NULL, NULL, NULL, 'inventario.ajustar', 'Ajustes por merma, vencimiento o conteo', 'Ajustar inventario'),
	(17, NULL, NULL, NULL, 'inventario.transferir', 'Mover stock de un almacén a otro', 'Transferir entre almacenes'),
	(18, NULL, NULL, NULL, 'inventario.kardex', 'Historial de movimientos de un producto', 'Ver kardex'),
	(19, NULL, NULL, NULL, 'ventas.crear', 'Operar el punto de venta', 'Registrar ventas'),
	(20, NULL, NULL, NULL, 'ventas.ver', 'Consultar las ventas de su propia sesión', 'Ver ventas propias'),
	(21, NULL, NULL, NULL, 'ventas.ver_todas', 'Consultar ventas de cualquier usuario o caja', 'Ver todas las ventas'),
	(22, NULL, NULL, NULL, 'ventas.anular', 'Anular una venta emitida', 'Anular ventas'),
	(23, NULL, NULL, NULL, 'ventas.descuento', 'Descontar sobre el precio de lista', 'Aplicar descuentos'),
	(24, NULL, NULL, NULL, 'ventas.precio_editar', 'Vender a un precio distinto al fijado', 'Modificar precio en venta'),
	(25, NULL, NULL, NULL, 'ventas.reimprimir', 'Volver a imprimir un ticket o factura', 'Reimprimir comprobante'),
	(26, NULL, NULL, NULL, 'clientes.ver', 'Consultar y buscar clientes', 'Ver clientes'),
	(27, NULL, NULL, NULL, 'clientes.gestionar', 'Registrar y editar clientes', 'Gestionar clientes'),
	(28, NULL, NULL, NULL, 'caja.abrir', 'Iniciar una sesión de caja', 'Abrir caja'),
	(29, NULL, NULL, NULL, 'caja.cerrar', 'Cerrar su propia sesión de caja', 'Cerrar caja propia'),
	(30, NULL, NULL, NULL, 'caja.cerrar_ajena', 'Forzar el cierre de la sesión de otro usuario', 'Cerrar caja de otro'),
	(31, NULL, NULL, NULL, 'caja.reabrir', 'Reabrir una sesión ya cerrada', 'Reabrir caja'),
	(32, NULL, NULL, NULL, 'caja.arqueo', 'Ver el detalle y diferencia del cierre', 'Ver arqueo'),
	(33, NULL, NULL, NULL, 'caja.movimiento', 'Registrar entradas y salidas de efectivo', 'Ingresos y retiros'),
	(34, NULL, NULL, NULL, 'caja.gestionar', 'Crear cajas y definir su horario', 'Gestionar cajas'),
	(35, NULL, NULL, NULL, 'almacenes.ver', 'Consultar los almacenes de la tienda', 'Ver almacenes'),
	(36, NULL, NULL, NULL, 'almacenes.gestionar', 'Crear y editar almacenes', 'Gestionar almacenes'),
	(37, NULL, NULL, NULL, 'tienda.configurar', 'Datos de la empresa, RUC, logo e IGV', 'Configurar tienda'),
	(38, NULL, NULL, NULL, 'usuarios.ver', 'Consultar usuarios del sistema', 'Ver usuarios'),
	(39, NULL, NULL, NULL, 'usuarios.gestionar', 'Crear, editar y desactivar usuarios', 'Gestionar usuarios'),
	(40, NULL, NULL, NULL, 'usuarios.almacenes', 'Definir en qué almacenes puede trabajar un usuario', 'Asignar almacenes'),
	(41, NULL, NULL, NULL, 'roles.ver', 'Consultar roles y sus permisos', 'Ver roles'),
	(42, NULL, NULL, NULL, 'roles.gestionar', 'Crear roles y asignarles permisos', 'Gestionar roles'),
	(43, NULL, NULL, NULL, 'reportes.ventas', 'Ventas por periodo, caja y vendedor', 'Reporte de ventas'),
	(44, NULL, NULL, NULL, 'reportes.compras', 'Compras por periodo y proveedor', 'Reporte de compras'),
	(45, NULL, NULL, NULL, 'reportes.inventario', 'Stock valorizado y productos por reponer', 'Reporte de inventario'),
	(46, NULL, NULL, NULL, 'reportes.caja', 'Arqueos y diferencias por sesión', 'Reporte de caja'),
	(47, NULL, NULL, NULL, 'reportes.utilidad', 'Márgenes y rentabilidad por producto', 'Reporte de utilidad');

-- Volcando estructura para tabla novasys.productos
CREATE TABLE IF NOT EXISTS `productos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `barcode` varchar(255) DEFAULT NULL,
  `cod_status` varchar(255) DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `last_cost` decimal(38,2) DEFAULT NULL,
  `minimum_sale_price` decimal(38,2) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `sale_price` decimal(38,2) DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `wholesale_price` decimal(38,2) DEFAULT NULL,
  `category_id` bigint NOT NULL,
  `brand_id` bigint NOT NULL,
  `supplier_id` bigint DEFAULT NULL,
  `subcategory_id` bigint NOT NULL,
  `unit_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKi5hfvd0rq1sp8kuv4qr92ww5i` (`code`),
  KEY `FK626khgoki8ks0q5344cou8ptd` (`category_id`),
  KEY `FK2magwmrbwms4l2x8cs8cwi8tj` (`brand_id`),
  KEY `FKpffurd6t96c22uqlc77o75ck4` (`supplier_id`),
  KEY `FKm7qe7f9vs3lwuwf1q0b8foiwi` (`subcategory_id`),
  KEY `FK28g2sv1d8oeqejcrai0e7h4ib` (`unit_id`),
  CONSTRAINT `FK28g2sv1d8oeqejcrai0e7h4ib` FOREIGN KEY (`unit_id`) REFERENCES `unidades` (`id`),
  CONSTRAINT `FK2magwmrbwms4l2x8cs8cwi8tj` FOREIGN KEY (`brand_id`) REFERENCES `marcas` (`id`),
  CONSTRAINT `FK626khgoki8ks0q5344cou8ptd` FOREIGN KEY (`category_id`) REFERENCES `categorias` (`id`),
  CONSTRAINT `FKm7qe7f9vs3lwuwf1q0b8foiwi` FOREIGN KEY (`subcategory_id`) REFERENCES `categorias` (`id`),
  CONSTRAINT `FKpffurd6t96c22uqlc77o75ck4` FOREIGN KEY (`supplier_id`) REFERENCES `proveedores` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.productos: ~2 rows (aproximadamente)
INSERT INTO `productos` (`id`, `created_at`, `deleted_at`, `updated_at`, `barcode`, `cod_status`, `code`, `description`, `last_cost`, `minimum_sale_price`, `name`, `sale_price`, `uuid`, `wholesale_price`, `category_id`, `brand_id`, `supplier_id`, `subcategory_id`, `unit_id`) VALUES
	(1, '2026-07-30 16:36:57.000000', NULL, '2026-08-02 17:50:31.072303', '7707367080012', '1', 'PROD00001', 'Leche evaporada Gloria 400g', NULL, NULL, 'Leche Gloria 400g', NULL, _binary 0xca9b76d58c5e11f1b78900090faa0001, NULL, 3, 1, 1, 32, 1),
	(2, '2026-07-30 16:36:57.000000', NULL, '2026-07-30 16:36:57.000000', '7707367080111', '1', 'PROD00002', 'Leche evaporada Gloria 170g', NULL, NULL, 'Leche Gloria 170g', NULL, _binary 0xca9b878e8c5e11f1b78900090faa0001, NULL, 3, 1, 1, 32, 1),
	(3, '2026-08-01 18:40:36.762516', NULL, '2026-08-01 18:40:36.798910', '7707241940081', '1', 'SNK000003', 'wer', NULL, NULL, 'WEQR', NULL, _binary 0xc4bf37547c9841b2a0a9858ab7f91120, NULL, 5, 2, 6, 46, 1);

-- Volcando estructura para tabla novasys.producto_imagenes
CREATE TABLE IF NOT EXISTS `producto_imagenes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `es_principal` bit(1) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  `producto_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo89u1q4jan8gbgod33hv48u27` (`producto_id`),
  CONSTRAINT `FKo89u1q4jan8gbgod33hv48u27` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.producto_imagenes: ~2 rows (aproximadamente)
INSERT INTO `producto_imagenes` (`id`, `es_principal`, `url`, `producto_id`) VALUES
	(1, b'1', 'images.jpg', 1),
	(2, b'1', 'images2.jpg', 2),
	(3, b'1', '73e78f64-9cbd-4b04-b9a9-703f3910bf93.png', 3);

-- Volcando estructura para tabla novasys.producto_precios
CREATE TABLE IF NOT EXISTS `producto_precios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cod_status` varchar(255) DEFAULT NULL,
  `minimum_quantity` int DEFAULT NULL,
  `price_type` enum('REGULAR','SALE','WHOLESALE') NOT NULL,
  `sale_price` decimal(12,4) NOT NULL,
  `almacen_id` bigint DEFAULT NULL,
  `producto_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_producto_precio` (`producto_id`,`almacen_id`,`price_type`),
  KEY `FKjvpw4ossm45fe24bqeaj49s2c` (`almacen_id`),
  CONSTRAINT `FKjvpw4ossm45fe24bqeaj49s2c` FOREIGN KEY (`almacen_id`) REFERENCES `almacenes` (`id`),
  CONSTRAINT `FKo1j2gcykq6ywkipsrn8vdn82l` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.producto_precios: ~5 rows (aproximadamente)
INSERT INTO `producto_precios` (`id`, `cod_status`, `minimum_quantity`, `price_type`, `sale_price`, `almacen_id`, `producto_id`) VALUES
	(1, '0', 1, 'REGULAR', 4.8000, 1, 1),
	(2, '0', 1, 'WHOLESALE', 5.0000, 1, 1),
	(3, '1', 1, 'SALE', 5.0000, 1, 1),
	(4, '0', 1, 'REGULAR', 5.2000, 2, 1),
	(5, '0', 1, 'WHOLESALE', 5.2000, 2, 1),
	(6, '1', 1, 'SALE', 8.0000, 2, 1);

-- Volcando estructura para tabla novasys.proveedores
CREATE TABLE IF NOT EXISTS `proveedores` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cod_status` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `ruc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.proveedores: ~19 rows (aproximadamente)
INSERT INTO `proveedores` (`id`, `created_at`, `deleted_at`, `updated_at`, `cod_status`, `contact`, `name`, `phone`, `ruc`) VALUES
	(1, NULL, NULL, NULL, '1', 'Juan Pérez', 'Gloria S.A.', '987654321', '20100070970'),
	(2, NULL, NULL, NULL, '1', 'Carlos Ramírez', 'Alicorp S.A.A.', '912345678', '20100055237'),
	(3, NULL, NULL, NULL, '1', 'María López', 'Arca Continental Lindley S.A.', '956123789', '20330664093'),
	(4, NULL, NULL, NULL, '1', 'José Castillo', 'Nestlé Perú S.A.', '945678123', '20100190797'),
	(5, NULL, NULL, NULL, '1', 'Ana Torres', 'Laive S.A.', '934567812', '20100017491'),
	(6, NULL, NULL, NULL, '1', 'Luis Mendoza', 'Molitalia S.A.', '923456781', '20501524637'),
	(7, NULL, NULL, NULL, '1', 'Miguel Flores', 'Backus y Johnston S.A.A.', '998112233', '20501876341'),
	(8, NULL, NULL, NULL, '1', 'Rosa Sánchez', 'Mondelez Perú S.A.', '977665544', '20504378212'),
	(9, NULL, NULL, NULL, '1', 'Pedro Vargas', 'Distribuidora El Sol S.A.C.', '966554433', '20600521489'),
	(10, NULL, NULL, NULL, '1', 'Carmen Díaz', 'Importadora San Martín S.A.C.', '955443322', '20508765431'),
	(11, NULL, NULL, NULL, '1', 'Ricardo Gómez', 'Comercial Andina S.A.C.', '944332211', '20601234567'),
	(12, NULL, NULL, NULL, '1', 'Patricia Herrera', 'Distribuciones Norte S.A.C.', '933221100', '20602345678'),
	(13, NULL, NULL, NULL, '1', 'Fernando Ruiz', 'Mayorista Trujillo S.A.C.', '922110099', '20603456789'),
	(14, NULL, NULL, NULL, '1', 'Karla Salazar', 'Inversiones Los Andes S.A.C.', '911009988', '20604567890'),
	(15, NULL, NULL, NULL, '1', 'Diego Romero', 'Comercial El Mercado S.A.C.', '900998877', '20605678901'),
	(16, '2026-07-22 17:45:00.123221', '2026-07-22 17:50:09.000000', '2026-07-22 17:50:06.741132', '0', 'Skdfj Sdfjl', 'Los Angeles ', '87391728931', '11111111111'),
	(17, '2026-07-25 21:20:14.155226', NULL, '2026-07-25 21:20:14.155226', '1', '9192', 'Jqwje', '1233', '82391872398'),
	(18, '2026-07-25 21:21:24.958227', NULL, '2026-07-25 21:21:24.958227', '1', '12323423', 'Nuevo Proveedor', '234', '87973298748'),
	(19, '2026-07-25 21:23:06.663649', NULL, '2026-07-25 21:25:10.389547', '1', 'Weryiuy', 'Iuweyriuyiu', '3453', '38459738947');

-- Volcando estructura para tabla novasys.roles
CREATE TABLE IF NOT EXISTS `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `alcance` enum('ALMACENES_ASIGNADOS','ALMACEN_ACTIVO','TODOS') NOT NULL,
  `cod_status` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.roles: ~5 rows (aproximadamente)
INSERT INTO `roles` (`id`, `created_at`, `deleted_at`, `updated_at`, `alcance`, `cod_status`, `description`, `name`) VALUES
	(1, NULL, NULL, NULL, 'TODOS', '1', 'Control total del sistema', 'ADMINISTRADOR'),
	(2, NULL, NULL, NULL, 'ALMACENES_ASIGNADOS', '1', 'Encargado de uno o varios almacenes', 'SUPERVISOR'),
	(3, NULL, NULL, NULL, 'ALMACEN_ACTIVO', '1', 'Opera el punto de venta en el almacén de su caja', 'CAJERO'),
	(4, NULL, NULL, NULL, 'ALMACEN_ACTIVO', '1', 'Recepción de mercadería y control de stock', 'ALMACENERO'),
	(5, NULL, NULL, NULL, 'TODOS', '1', 'Consulta y reportes, sin operar', 'CONTADOR');

-- Volcando estructura para tabla novasys.role_permissions
CREATE TABLE IF NOT EXISTS `role_permissions` (
  `created_at` datetime(6) NOT NULL,
  `permission_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`permission_id`,`role_id`),
  KEY `FKn5fotdgk8d1xvo8nav9uv3muc` (`role_id`),
  CONSTRAINT `FKegdk29eiy7mdtefy5c7eirr6e` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`),
  CONSTRAINT `FKn5fotdgk8d1xvo8nav9uv3muc` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.role_permissions: ~128 rows (aproximadamente)
INSERT INTO `role_permissions` (`created_at`, `permission_id`, `role_id`) VALUES
	('0000-00-00 00:00:00.000000', 1, 1),
	('0000-00-00 00:00:00.000000', 1, 2),
	('0000-00-00 00:00:00.000000', 1, 3),
	('0000-00-00 00:00:00.000000', 1, 4),
	('0000-00-00 00:00:00.000000', 1, 5),
	('0000-00-00 00:00:00.000000', 2, 1),
	('0000-00-00 00:00:00.000000', 2, 2),
	('0000-00-00 00:00:00.000000', 2, 4),
	('0000-00-00 00:00:00.000000', 3, 1),
	('0000-00-00 00:00:00.000000', 3, 2),
	('0000-00-00 00:00:00.000000', 3, 4),
	('0000-00-00 00:00:00.000000', 4, 1),
	('0000-00-00 00:00:00.000000', 5, 1),
	('0000-00-00 00:00:00.000000', 5, 2),
	('0000-00-00 00:00:00.000000', 5, 4),
	('0000-00-00 00:00:00.000000', 5, 5),
	('0000-00-00 00:00:00.000000', 6, 1),
	('0000-00-00 00:00:00.000000', 6, 2),
	('0000-00-00 00:00:00.000000', 6, 3),
	('0000-00-00 00:00:00.000000', 6, 5),
	('0000-00-00 00:00:00.000000', 7, 1),
	('0000-00-00 00:00:00.000000', 7, 2),
	('0000-00-00 00:00:00.000000', 8, 1),
	('0000-00-00 00:00:00.000000', 8, 2),
	('0000-00-00 00:00:00.000000', 8, 4),
	('0000-00-00 00:00:00.000000', 8, 5),
	('0000-00-00 00:00:00.000000', 9, 1),
	('0000-00-00 00:00:00.000000', 9, 2),
	('0000-00-00 00:00:00.000000', 10, 1),
	('0000-00-00 00:00:00.000000', 10, 2),
	('0000-00-00 00:00:00.000000', 10, 4),
	('0000-00-00 00:00:00.000000', 10, 5),
	('0000-00-00 00:00:00.000000', 11, 1),
	('0000-00-00 00:00:00.000000', 11, 2),
	('0000-00-00 00:00:00.000000', 12, 1),
	('0000-00-00 00:00:00.000000', 12, 2),
	('0000-00-00 00:00:00.000000', 12, 4),
	('0000-00-00 00:00:00.000000', 12, 5),
	('0000-00-00 00:00:00.000000', 13, 1),
	('0000-00-00 00:00:00.000000', 13, 2),
	('0000-00-00 00:00:00.000000', 13, 4),
	('0000-00-00 00:00:00.000000', 14, 1),
	('0000-00-00 00:00:00.000000', 14, 2),
	('0000-00-00 00:00:00.000000', 15, 1),
	('0000-00-00 00:00:00.000000', 15, 2),
	('0000-00-00 00:00:00.000000', 15, 3),
	('0000-00-00 00:00:00.000000', 15, 4),
	('0000-00-00 00:00:00.000000', 15, 5),
	('0000-00-00 00:00:00.000000', 16, 1),
	('0000-00-00 00:00:00.000000', 16, 2),
	('0000-00-00 00:00:00.000000', 16, 4),
	('0000-00-00 00:00:00.000000', 17, 1),
	('0000-00-00 00:00:00.000000', 17, 2),
	('0000-00-00 00:00:00.000000', 17, 4),
	('0000-00-00 00:00:00.000000', 18, 1),
	('0000-00-00 00:00:00.000000', 18, 2),
	('0000-00-00 00:00:00.000000', 18, 4),
	('0000-00-00 00:00:00.000000', 18, 5),
	('0000-00-00 00:00:00.000000', 19, 1),
	('0000-00-00 00:00:00.000000', 19, 2),
	('0000-00-00 00:00:00.000000', 19, 3),
	('0000-00-00 00:00:00.000000', 20, 1),
	('0000-00-00 00:00:00.000000', 20, 2),
	('0000-00-00 00:00:00.000000', 20, 3),
	('0000-00-00 00:00:00.000000', 20, 5),
	('0000-00-00 00:00:00.000000', 21, 1),
	('0000-00-00 00:00:00.000000', 21, 2),
	('0000-00-00 00:00:00.000000', 21, 5),
	('0000-00-00 00:00:00.000000', 22, 1),
	('0000-00-00 00:00:00.000000', 22, 2),
	('0000-00-00 00:00:00.000000', 23, 1),
	('0000-00-00 00:00:00.000000', 23, 2),
	('0000-00-00 00:00:00.000000', 24, 1),
	('0000-00-00 00:00:00.000000', 24, 2),
	('0000-00-00 00:00:00.000000', 25, 1),
	('0000-00-00 00:00:00.000000', 25, 2),
	('0000-00-00 00:00:00.000000', 25, 3),
	('0000-00-00 00:00:00.000000', 26, 1),
	('0000-00-00 00:00:00.000000', 26, 2),
	('0000-00-00 00:00:00.000000', 26, 3),
	('0000-00-00 00:00:00.000000', 26, 5),
	('0000-00-00 00:00:00.000000', 27, 1),
	('0000-00-00 00:00:00.000000', 27, 2),
	('0000-00-00 00:00:00.000000', 27, 3),
	('0000-00-00 00:00:00.000000', 28, 1),
	('0000-00-00 00:00:00.000000', 28, 2),
	('0000-00-00 00:00:00.000000', 28, 3),
	('0000-00-00 00:00:00.000000', 29, 1),
	('0000-00-00 00:00:00.000000', 29, 2),
	('0000-00-00 00:00:00.000000', 29, 3),
	('0000-00-00 00:00:00.000000', 30, 1),
	('0000-00-00 00:00:00.000000', 30, 2),
	('0000-00-00 00:00:00.000000', 31, 1),
	('0000-00-00 00:00:00.000000', 32, 1),
	('0000-00-00 00:00:00.000000', 32, 2),
	('0000-00-00 00:00:00.000000', 32, 3),
	('0000-00-00 00:00:00.000000', 32, 5),
	('0000-00-00 00:00:00.000000', 33, 1),
	('0000-00-00 00:00:00.000000', 33, 2),
	('0000-00-00 00:00:00.000000', 34, 1),
	('0000-00-00 00:00:00.000000', 35, 1),
	('0000-00-00 00:00:00.000000', 35, 2),
	('0000-00-00 00:00:00.000000', 35, 4),
	('0000-00-00 00:00:00.000000', 35, 5),
	('0000-00-00 00:00:00.000000', 36, 1),
	('0000-00-00 00:00:00.000000', 37, 1),
	('0000-00-00 00:00:00.000000', 38, 1),
	('0000-00-00 00:00:00.000000', 39, 1),
	('0000-00-00 00:00:00.000000', 40, 1),
	('0000-00-00 00:00:00.000000', 41, 1),
	('0000-00-00 00:00:00.000000', 42, 1),
	('0000-00-00 00:00:00.000000', 43, 1),
	('0000-00-00 00:00:00.000000', 43, 2),
	('0000-00-00 00:00:00.000000', 43, 5),
	('0000-00-00 00:00:00.000000', 44, 1),
	('0000-00-00 00:00:00.000000', 44, 2),
	('0000-00-00 00:00:00.000000', 44, 4),
	('0000-00-00 00:00:00.000000', 44, 5),
	('0000-00-00 00:00:00.000000', 45, 1),
	('0000-00-00 00:00:00.000000', 45, 2),
	('0000-00-00 00:00:00.000000', 45, 4),
	('0000-00-00 00:00:00.000000', 45, 5),
	('0000-00-00 00:00:00.000000', 46, 1),
	('0000-00-00 00:00:00.000000', 46, 2),
	('0000-00-00 00:00:00.000000', 46, 5),
	('0000-00-00 00:00:00.000000', 47, 1),
	('0000-00-00 00:00:00.000000', 47, 2),
	('0000-00-00 00:00:00.000000', 47, 5);

-- Volcando estructura para tabla novasys.tiendas
CREATE TABLE IF NOT EXISTS `tiendas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `business_name` varchar(255) DEFAULT NULL,
  `cod_status` varchar(255) DEFAULT NULL,
  `code` varchar(20) NOT NULL,
  `department` varchar(255) DEFAULT NULL,
  `district` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_main` bit(1) DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `ruc` varchar(255) DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKoq1083k5ew602l7sh1n6xbd1e` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.tiendas: ~0 rows (aproximadamente)
INSERT INTO `tiendas` (`id`, `created_at`, `deleted_at`, `updated_at`, `address`, `business_name`, `cod_status`, `code`, `department`, `district`, `email`, `is_main`, `logo`, `name`, `phone`, `province`, `ruc`, `uuid`) VALUES
	(1, '2026-07-24 16:06:39.000000', NULL, NULL, 'Jr. Amazonas 450 - Chachapoyas', 'Comercial Nova S.A.C.', '1', 'SUC001', 'La Libertad', 'Trujillo', 'contacto@miminimarket.com', b'1', NULL, 'NOVASYS MARKET', '944123456', 'Trujillo', '20612345678', _binary 0x908eaa4a87a311f1bc2f0a0027000004);

-- Volcando estructura para tabla novasys.transferencias
CREATE TABLE IF NOT EXISTS `transferencias` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cod_status` enum('CANCELLED','COMPLETED','IN_TRANSIT','PENDING','RECEIVED') NOT NULL,
  `code` varchar(30) NOT NULL,
  `fecha_transferencia` datetime(6) NOT NULL,
  `observation` varchar(500) DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `destination_warehouse_id` bigint NOT NULL,
  `source_warehouse_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_transferencia_code` (`code`),
  UNIQUE KEY `uk_transferencia_uuid` (`uuid`),
  KEY `fk_transferencia_destino` (`destination_warehouse_id`),
  KEY `fk_transferencia_origen` (`source_warehouse_id`),
  KEY `FKc6mxwagc55r884s6yhukbvh4b` (`user_id`),
  CONSTRAINT `fk_transferencia_destino` FOREIGN KEY (`destination_warehouse_id`) REFERENCES `almacenes` (`id`),
  CONSTRAINT `fk_transferencia_origen` FOREIGN KEY (`source_warehouse_id`) REFERENCES `almacenes` (`id`),
  CONSTRAINT `FKc6mxwagc55r884s6yhukbvh4b` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.transferencias: ~0 rows (aproximadamente)

-- Volcando estructura para tabla novasys.transferencia_detalles
CREATE TABLE IF NOT EXISTS `transferencia_detalles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` decimal(12,3) NOT NULL,
  `quantity_received` decimal(12,3) DEFAULT NULL,
  `product_id` bigint NOT NULL,
  `transferencia_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_transferencia_detalle_producto` (`product_id`),
  KEY `fk_transferencia_detalle_transferencia` (`transferencia_id`),
  CONSTRAINT `fk_transferencia_detalle_producto` FOREIGN KEY (`product_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `fk_transferencia_detalle_transferencia` FOREIGN KEY (`transferencia_id`) REFERENCES `transferencias` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.transferencia_detalles: ~0 rows (aproximadamente)

-- Volcando estructura para tabla novasys.unidades
CREATE TABLE IF NOT EXISTS `unidades` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cod_status` varchar(255) DEFAULT NULL,
  `cod_type` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.unidades: ~6 rows (aproximadamente)
INSERT INTO `unidades` (`id`, `created_at`, `deleted_at`, `updated_at`, `cod_status`, `cod_type`, `code`, `name`) VALUES
	(1, '2026-07-20 21:41:55.672438', NULL, '2026-07-20 21:41:55.672438', '1', 'C', 'UND', 'Unidad'),
	(2, '2026-07-20 21:50:17.723450', NULL, '2026-07-20 21:50:17.723450', '1', 'C', 'CJ', 'Caja'),
	(3, '2026-07-20 21:50:29.566499', NULL, '2026-07-20 21:50:29.566499', '1', 'C', 'PQ', 'Paquete'),
	(4, '2026-07-20 21:50:59.312355', NULL, '2026-07-20 21:50:59.312355', '1', 'P', 'KG', 'Kilogramo'),
	(5, '2026-07-20 21:51:12.911975', NULL, '2026-07-20 21:51:12.911975', '1', 'P', 'G', 'Gramo'),
	(6, '2026-07-20 21:51:24.000900', NULL, '2026-07-20 21:51:24.000900', '1', 'V', 'LT', 'Litro'),
	(7, '2026-07-20 21:52:09.697543', NULL, '2026-07-20 22:10:44.236115', '1', 'V', 'ML', 'Mililitro');

-- Volcando estructura para tabla novasys.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cod_status` varchar(255) DEFAULT NULL,
  `document` varchar(255) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `registration_date` datetime(6) DEFAULT NULL,
  `role_id` bigint NOT NULL,
  `branch_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKkgnb8tmua5cirywqjj4m3id0l` (`document`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  KEY `FKk0tjgyo4ynsjeusr7isvj04e9` (`branch_id`),
  CONSTRAINT `FKk0tjgyo4ynsjeusr7isvj04e9` FOREIGN KEY (`branch_id`) REFERENCES `tiendas` (`id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.users: ~9 rows (aproximadamente)
INSERT INTO `users` (`id`, `created_at`, `deleted_at`, `updated_at`, `cod_status`, `document`, `last_name`, `name`, `password`, `phone`, `registration_date`, `role_id`, `branch_id`) VALUES
	(1, NULL, NULL, NULL, '1', '10000001', 'Ramírez Chávez', 'Franz', '$2a$10$PYEjcbV89E.OH.hJlNQgV.JJbeQdBfByc1EjodYGC75PkRhxwXA9m', '987654321', '2026-07-31 09:29:26.153240', 1, 1),
	(2, NULL, NULL, NULL, '1', '40000001', 'Ccahuana Flores', 'Julio', '$2a$10$PYEjcbV89E.OH.hJlNQgV.JJbeQdBfByc1EjodYGC75PkRhxwXA9m', '987333111', '2026-07-31 09:29:26.153240', 4, 1),
	(3, NULL, NULL, NULL, '1', '40000002', 'Nuñez Bautista', 'Marisol', '$2a$10$PYEjcbV89E.OH.hJlNQgV.JJbeQdBfByc1EjodYGC75PkRhxwXA9m', '987333222', '2026-07-31 09:29:26.153240', 4, 1),
	(4, NULL, NULL, NULL, '1', '30000001', 'Quispe Alvarado', 'Milagros', '$2a$10$PYEjcbV89E.OH.hJlNQgV.JJbeQdBfByc1EjodYGC75PkRhxwXA9m', '987222111', '2026-07-31 09:29:26.153240', 3, 1),
	(5, NULL, NULL, NULL, '1', '30000002', 'Torres Mendoza', 'Diego', '$2a$10$PYEjcbV89E.OH.hJlNQgV.JJbeQdBfByc1EjodYGC75PkRhxwXA9m', '987222222', '2026-07-31 09:29:26.153240', 3, 1),
	(6, NULL, NULL, NULL, '1', '30000003', 'Vílchez Campos', 'Rosa', '$2a$10$PYEjcbV89E.OH.hJlNQgV.JJbeQdBfByc1EjodYGC75PkRhxwXA9m', '987222333', '2026-07-31 09:29:26.153240', 3, 1),
	(7, NULL, NULL, NULL, '1', '50000001', 'Rojas Ticona', 'Elena', '$2a$10$PYEjcbV89E.OH.hJlNQgV.JJbeQdBfByc1EjodYGC75PkRhxwXA9m', '987444111', '2026-07-31 09:29:26.153240', 5, 1),
	(8, NULL, NULL, NULL, '1', '20000001', 'Sandoval Ríos', 'Lucía', '$2a$10$PYEjcbV89E.OH.hJlNQgV.JJbeQdBfByc1EjodYGC75PkRhxwXA9m', '987111222', '2026-07-31 09:29:26.153240', 2, 1),
	(9, NULL, NULL, NULL, '1', '20000002', 'Huamán Paredes', 'Óscar', '$2a$10$PYEjcbV89E.OH.hJlNQgV.JJbeQdBfByc1EjodYGC75PkRhxwXA9m', '987111333', '2026-07-31 09:29:26.153240', 2, 1);

-- Volcando estructura para tabla novasys.user_permissions
CREATE TABLE IF NOT EXISTS `user_permissions` (
  `created_at` datetime(6) NOT NULL,
  `granted` bit(1) NOT NULL,
  `permission_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`permission_id`,`user_id`),
  KEY `FKkowxl8b2bngrxd1gafh13005u` (`user_id`),
  CONSTRAINT `FKkowxl8b2bngrxd1gafh13005u` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKq4qlrabt4s0etm9tfkoqfuib1` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.user_permissions: ~3 rows (aproximadamente)
INSERT INTO `user_permissions` (`created_at`, `granted`, `permission_id`, `user_id`) VALUES
	('2026-07-31 09:29:26.153240', b'1', 13, 5),
	('2026-07-31 09:29:26.153240', b'1', 15, 5),
	('2026-07-31 09:29:26.153240', b'1', 18, 5),
	('2026-07-31 09:29:26.153240', b'0', 23, 6);

-- Volcando estructura para tabla novasys.usuario_almacen
CREATE TABLE IF NOT EXISTS `usuario_almacen` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cod_status` varchar(255) DEFAULT NULL,
  `is_default` bit(1) NOT NULL,
  `almacen_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgoecb5d6kdauvtiiw1fjev5lk` (`almacen_id`),
  KEY `FKsvp89vg4iv2wcqjwokog5hqe1` (`user_id`),
  CONSTRAINT `FKgoecb5d6kdauvtiiw1fjev5lk` FOREIGN KEY (`almacen_id`) REFERENCES `almacenes` (`id`),
  CONSTRAINT `FKsvp89vg4iv2wcqjwokog5hqe1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.usuario_almacen: ~11 rows (aproximadamente)
INSERT INTO `usuario_almacen` (`id`, `created_at`, `deleted_at`, `updated_at`, `cod_status`, `is_default`, `almacen_id`, `user_id`) VALUES
	(1, '2026-07-31 09:29:26.153240', NULL, '2026-07-31 09:29:26.153240', '1', b'0', 1, 8),
	(2, '2026-07-31 09:29:26.153240', NULL, '2026-07-31 09:29:26.153240', '1', b'0', 2, 8),
	(3, '2026-07-31 09:29:26.153240', NULL, '2026-07-31 09:29:26.153240', '1', b'0', 3, 9),
	(4, '2026-07-31 09:29:26.153240', NULL, '2026-07-31 09:29:26.153240', '1', b'0', 1, 4),
	(5, '2026-07-31 09:29:26.153240', NULL, '2026-07-31 09:29:26.153240', '1', b'0', 2, 4),
	(6, '2026-07-31 09:29:26.153240', NULL, '2026-07-31 09:29:26.153240', '1', b'0', 1, 5),
	(7, '2026-07-31 09:29:26.153240', NULL, '2026-07-31 09:29:26.153240', '1', b'0', 3, 6),
	(8, '2026-07-31 09:29:26.153240', NULL, '2026-07-31 09:29:26.153240', '1', b'0', 1, 2),
	(9, '2026-07-31 09:29:26.153240', NULL, '2026-07-31 09:29:26.153240', '1', b'0', 2, 2),
	(10, '2026-07-31 09:29:26.153240', NULL, '2026-07-31 09:29:26.153240', '1', b'0', 3, 2),
	(11, '2026-07-31 09:29:26.153240', NULL, '2026-07-31 09:29:26.153240', '1', b'0', 2, 3);

-- Volcando estructura para tabla novasys.ventas
CREATE TABLE IF NOT EXISTS `ventas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cod_status` varchar(255) DEFAULT NULL,
  `code` varchar(20) NOT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `discount` decimal(38,2) DEFAULT NULL,
  `document_number` varchar(255) DEFAULT NULL,
  `document_series` varchar(255) DEFAULT NULL,
  `document_type` varchar(255) DEFAULT NULL,
  `observation` varchar(300) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `sale_date` date DEFAULT NULL,
  `subtotal` decimal(38,2) DEFAULT NULL,
  `tax` decimal(38,2) DEFAULT NULL,
  `tax_applied` decimal(38,2) DEFAULT NULL,
  `total` decimal(38,2) DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `caja_sesion_id` bigint NOT NULL,
  `customer_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKc46uurwnobb90smk0hfnmjthd` (`code`),
  KEY `FK3k5xo755cxl9rugd62bl4ekxf` (`caja_sesion_id`),
  KEY `FKqy5bayaksn0i0go6v4mpg270n` (`customer_id`),
  KEY `FKm8j3rvd665op8ifu6nr54p0xj` (`user_id`),
  CONSTRAINT `FK3k5xo755cxl9rugd62bl4ekxf` FOREIGN KEY (`caja_sesion_id`) REFERENCES `caja_sesion` (`id`),
  CONSTRAINT `FKm8j3rvd665op8ifu6nr54p0xj` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKqy5bayaksn0i0go6v4mpg270n` FOREIGN KEY (`customer_id`) REFERENCES `clientes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.ventas: ~0 rows (aproximadamente)
INSERT INTO `ventas` (`id`, `created_at`, `deleted_at`, `updated_at`, `cod_status`, `code`, `currency`, `discount`, `document_number`, `document_series`, `document_type`, `observation`, `payment_method`, `sale_date`, `subtotal`, `tax`, `tax_applied`, `total`, `uuid`, `caja_sesion_id`, `customer_id`, `user_id`) VALUES
	(1, '2026-08-08 14:43:06.540893', NULL, '2026-08-08 14:43:06.540893', '1', 'VEN000001', 'PEN', 0.00, '000001', 'CF01', '12', '', '1', '2026-08-08', 21.19, 3.81, 0.18, 25.00, _binary 0x4a10302d9caf4ceb8b7fab20ce75d9fe, 2, 1, 4),
	(2, '2026-08-20 14:07:57.656309', NULL, '2026-08-20 14:07:57.656309', '1', 'VEN000002', 'PEN', 0.00, '000002', 'CF01', '12', '', '1', '2026-08-20', 21.19, 3.81, 0.18, 25.00, _binary 0x3e9fe5362af74101afc52db6be10e2cb, 4, 1, 1);

-- Volcando estructura para tabla novasys.venta_detalle
CREATE TABLE IF NOT EXISTS `venta_detalle` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `discount` decimal(38,2) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `quantity` decimal(38,2) DEFAULT NULL,
  `subtotal` decimal(38,2) DEFAULT NULL,
  `unit_price` decimal(38,2) DEFAULT NULL,
  `product_id` bigint NOT NULL,
  `sale_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK46o928i9rk1mg598p9g3m0xp` (`product_id`),
  KEY `FKt4cfpxteon6bqm5c4wlockhv` (`sale_id`),
  CONSTRAINT `FK46o928i9rk1mg598p9g3m0xp` FOREIGN KEY (`product_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `FKt4cfpxteon6bqm5c4wlockhv` FOREIGN KEY (`sale_id`) REFERENCES `ventas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.venta_detalle: ~0 rows (aproximadamente)
INSERT INTO `venta_detalle` (`id`, `discount`, `product_name`, `quantity`, `subtotal`, `unit_price`, `product_id`, `sale_id`) VALUES
	(1, 0.00, 'Leche Gloria 400g', 5.00, 25.00, 5.00, 1, 1),
	(2, 0.00, 'Leche Gloria 400g', 5.00, 25.00, 5.00, 1, 2);

-- Volcando estructura para tabla novasys.venta_pagos
CREATE TABLE IF NOT EXISTS `venta_pagos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `reference` varchar(255) DEFAULT NULL,
  `sale_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK93wsdwtklqkxa8b5u7vm0qmqy` (`sale_id`),
  CONSTRAINT `FK93wsdwtklqkxa8b5u7vm0qmqy` FOREIGN KEY (`sale_id`) REFERENCES `ventas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla novasys.venta_pagos: ~0 rows (aproximadamente)
INSERT INTO `venta_pagos` (`id`, `amount`, `method`, `reference`, `sale_id`) VALUES
	(1, 25.00, '1', '', 1),
	(2, 25.00, '1', '', 2);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
