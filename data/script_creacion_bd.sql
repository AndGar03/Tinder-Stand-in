-- Script de creación de base de datos para Tinder Stand-in

CREATE DATABASE IF NOT EXISTS tinderstandin_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tinderstandin_db;

-- Tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL UNIQUE
);

-- Tabla de usuarios (incluye datos de persona)
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    descripcion VARCHAR(200),
    genero CHAR(1),
    ciudad VARCHAR(100),
    fecha_nacimiento VARCHAR(50),
    foto_perfil VARCHAR(255),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL,
    fecha_creacion DATETIME,
    fecha_actualizacion DATETIME,
    ultimo_acceso DATETIME,
    cuenta_expirada TINYINT(1) DEFAULT 0,
    cuenta_bloqueada TINYINT(1) DEFAULT 0,
    credenciales_expiradas TINYINT(1) DEFAULT 0,
    habilitado TINYINT(1) DEFAULT 1
);

-- Tabla de unión usuarios_roles
CREATE TABLE IF NOT EXISTS usuarios_roles (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    CONSTRAINT fk_usuarios_roles_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_usuarios_roles_rol FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- Tabla de likes
CREATE TABLE IF NOT EXISTS likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_origen_id BIGINT NOT NULL,
    usuario_destino_id BIGINT NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    INDEX idx_likes_origen (usuario_origen_id),
    INDEX idx_likes_destino (usuario_destino_id)
);

-- Tabla de matches
CREATE TABLE IF NOT EXISTS matches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario1_id BIGINT NOT NULL,
    usuario2_id BIGINT NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    INDEX idx_matches_usuario1 (usuario1_id),
    INDEX idx_matches_usuario2 (usuario2_id)
);

-- Tabla de fotos
CREATE TABLE IF NOT EXISTS fotos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(500) NOT NULL,
    usuario_id BIGINT NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    INDEX idx_fotos_usuario (usuario_id)
);
