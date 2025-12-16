CREATE DATABASE db_vaqueras CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db_vaqueras;

-- TABLAS DE CONFIGURACIÓN Y SISTEMA
-- esta funcion es para El administrador donde puede modificar este porcentaje global
CREATE TABLE comision_global(
    id_comision_global INT AUTO_INCREMENT PRIMARY KEY,
    porcentaje_global DECIMAL(5,2) NOT NULL DEFAULT 15.00,
    fecha_vigencia DATETIME DEFAULT CURRENT_TIMESTAMP
);
-- 2. MÓDULO DE USUARIOS Y ROLES
CREATE TABLE usuario (
	id_user INT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(50) NOT NULL UNIQUE,
	email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    fecha_nacimiento DATE NOT NULL,
    pais VARCHAR(50),
    rol ENUM('ADMIN','GAMER','EMPRESA') NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado_cuenta ENUM('ACTIVO', 'BLOQUEADO') DEFAULT 'ACTIVO',
	biblioteca_publica BOOLEAN DEFAULT TRUE
);
CREATE TABLE cartera (
	id_cartera INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL UNIQUE,
    saldo_actual DECIMAL(10,2) DEFAULT 0.00 CHECK (saldo_actual >= 0),
    ultima_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_cartera_user FOREIGN KEY (id_user) REFERENCES usuario(id_user) ON DELETE CASCADE
);
CREATE TABLE recarga_cartera (
	id_recarga INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    monto_recargado DECIMAL(10,2) NOT NULL CHECK (monto_recargado > 0),
    fecha_recarga DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recarga_usuario FOREIGN KEY (id_user) REFERENCES usuario(id_user)
);
CREATE TABLE empresa (
	id_empresa INT AUTO_INCREMENT PRIMARY KEY,
    nombre_empresa VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    -- comision_especifica DECIMAL(5,2) DEFAULT NULL,
    descripcion TEXT,
    fecha_afiliacion DATETIME DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE comision_empresa (
	id_comision_empresa INT AUTO_INCREMENT PRIMARY KEY,
    id_empresa INT NOT NULL,
    porcentaje_especifico DECIMAL(5,2) NOT NULL,
    fecha_vigencia DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ce_empresa FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa)
);

CREATE TABLE usuario_empresa (
	id_usuario_empresa INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    id_empresa INT NOT NULL,
    cargo VARCHAR(50) DEFAULT 'Administrador_Empresa',
    CONSTRAINT fk_ue_usuario FOREIGN KEY (id_user) REFERENCES usuario(id_user) ON DELETE CASCADE,
    CONSTRAINT fk_ue_empresa FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa) ON DELETE CASCADE,
    UNIQUE(id_user, id_empresa)
);
CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200)
);

CREATE TABLE videojuego (
	id_videojuego INT AUTO_INCREMENT PRIMARY KEY,
    id_empresa INT NOT NULL,
    -- id_clasificacion INT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
    recursos_minimos TEXT,
    fecha_lanzamiento DATE,
    estado ENUM('ACTIVO','SUSPENDIDO') DEFAULT 'ACTIVO',
    clasificacion_edad ENUM('E', 'T', 'M') NOT NULL,
    edad_minima INT NOT NULL,
    CONSTRAINT fk_juego_empresa FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa)
);
CREATE TABLE juego_categoria (
	id_videojuego INT NOT NULL,
    id_categoria INT NOT NULL,
    PRIMARY KEY (id_videojuego, id_categoria),
    CONSTRAINT fk_jc_juego FOREIGN KEY (id_videojuego) REFERENCES videojuego(id_videojuego) ON DELETE CASCADE,
    CONSTRAINT fk_jc_cat FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON DELETE RESTRICT
);
CREATE TABLE multimedia (
	id_multimedia INT AUTO_INCREMENT PRIMARY KEY,
    id_videojuego INT NOT NULL,
    url_imagen VARCHAR(255) NOT NULL,
    tipo ENUM('PORTADA', 'GALERIA', 'BANNER') DEFAULT 'GALERIA',
    CONSTRAINT fk_media_juego FOREIGN KEY (id_videojuego) REFERENCES videojuego(id_videojuego) ON DELETE CASCADE
);
-- aqui se procesa toda la transacción y manejo de comisiones
CREATE TABLE venta (
	id_venta INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL, -- comprador
    id_empresa INT NOT NULL,
    id_videojuego INT NOT NULL,
    fecha_compra DATETIME DEFAULT CURRENT_TIMESTAMP,
    precio_final DECIMAL(10,2) NOT NULL,
    retencion_plataforma DECIMAL(10,2) NOT NULL,
    ingreso_empresa DECIMAL(10,2) NOT NULL,
    tipo_comision ENUM('GLOBAL', 'ESPECIFICA') NOT NULL,
    porcentaje_aplicado DECIMAL(5,2) NOT NULL,
    CONSTRAINT fk_venta_usuario FOREIGN KEY (id_user) REFERENCES usuario(id_user),
    CONSTRAINT fk_ingreso_empresa FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa),
    CONSTRAINT fk_venta_juego FOREIGN KEY (id_videojuego) REFERENCES videojuego(id_videojuego)
);

CREATE TABLE biblioteca (
	id_biblioteca INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    id_videojuego INT NOT NULL,
    fecha_adquisicion DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado_instalacion ENUM('INSTALADO','NO_INSTALADO') DEFAULT 'NO_INSTALADO',
    es_propietario BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_lib_usuario FOREIGN KEY (id_user) REFERENCES usuario(id_user),
    CONSTRAINT fk_lib_juego FOREIGN KEY (id_videojuego) REFERENCES videojuego(id_videojuego),
    UNIQUE(id_user, id_videojuego)
);

CREATE TABLE grupo_familiar (
	id_grupo INT AUTO_INCREMENT PRIMARY KEY,
    nombre_grupo VARCHAR(100) NOT NULL,
    id_admin_user INT NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_grupo_admin FOREIGN KEY (id_admin_user) REFERENCES usuario(id_user),
    UNIQUE(id_grupo, id_admin_user)
);

CREATE TABLE miembro_grupo (
	id_miembro_grupo INT AUTO_INCREMENT PRIMARY KEY,
	id_grupo INT NOT NULL,
    id_user INT NOT NULL,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mem_grupo FOREIGN KEY (id_grupo) REFERENCES grupo_familiar(id_grupo) ON DELETE CASCADE,
    CONSTRAINT fk_mem_user FOREIGN KEY (id_user) REFERENCES usuario(id_user)
);

CREATE TABLE comentario (
	id_comentario INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    id_videojuego INT NOT NULL,
    id_comentario_padre INT DEFAULT NULL, -- Recursividad para respuestas
    texto TEXT,
    calificacion INT CHECK (calificacion BETWEEN 1 AND 5),
    texto_visible BOOLEAN DEFAULT TRUE,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_com_usuario FOREIGN KEY (id_user) REFERENCES usuario(id_user),
    CONSTRAINT fk_com_juego FOREIGN KEY (id_videojuego) REFERENCES videojuego(id_videojuego),
    -- Relación recursiva consigo misma pa ahorrarme una entidad
    CONSTRAINT fk_com_padre FOREIGN KEY (id_comentario_padre) REFERENCES comentario(id_comentario) ON DELETE CASCADE
);
-- Tabla de control para la regla de 1 solo juego instalado
CREATE TABLE instalacion_prestamo (
	id_instalacion INT AUTO_INCREMENT PRIMARY KEY,
    id_user_prestando INT NOT NULL,
    id_videojuego INT NOT NULL,
    estado ENUM('INSTALADO', 'NO_INSTALADO') DEFAULT 'NO_INSTALADO',
    fecha_ultimo_cambio DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_inst_usuario FOREIGN KEY (id_user_prestando) REFERENCES usuario(id_user),
    CONSTRAINT fk_inst_juego FOREIGN KEY (id_videojuego) REFERENCES videojuego(id_videojuego)
);

CREATE TABLE banner_principal (
	id_banner INT AUTO_INCREMENT PRIMARY KEY,
    id_videojuego INT NULL,
    imagen_url VARCHAR(255) NOT NULL,
    posicion INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_banner_juego FOREIGN KEY (id_videojuego) REFERENCES videojuego(id_videojuego) ON DELETE SET NULL
);