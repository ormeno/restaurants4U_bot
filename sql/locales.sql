-- phpMyAdmin SQL Dump
-- version 4.6.6deb5ubuntu0.5
-- https://www.phpmyadmin.net/
--

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `restaurants4ubd`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `locales`
--

CREATE TABLE `locales` (
  `id_local` int(11) NOT NULL,
  `nombre` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `descripcion` varchar(600) COLLATE utf8_spanish_ci NOT NULL,
  `tipo_local` varchar(100) COLLATE utf8_spanish_ci DEFAULT NULL,
  `idioma` varchar(20) COLLATE utf8_spanish_ci DEFAULT NULL,
  `plan` varchar(20) COLLATE utf8_spanish_ci DEFAULT 'B',
  `pais` varchar(200) COLLATE utf8_spanish_ci DEFAULT NULL,
  `ciudad` varchar(200) COLLATE utf8_spanish_ci DEFAULT NULL,
  `moneda` varchar(3) COLLATE utf8_spanish_ci DEFAULT NULL,
  `impuesto` varchar(3) COLLATE utf8_spanish_ci DEFAULT NULL,
  `porcenImpuesto` decimal(4,2) DEFAULT NULL,
  `direccion` varchar(200) COLLATE utf8_spanish_ci DEFAULT NULL,
  `datosEnvio` varchar(400) COLLATE utf8_spanish_ci DEFAULT NULL,
  `datosConfirPago` varchar(400) COLLATE utf8_spanish_ci DEFAULT NULL,
  `datosSupleEnvio` varchar(200) COLLATE utf8_spanish_ci DEFAULT NULL,
  `impSupleEnvio` decimal(5,2) DEFAULT NULL,
  `fecha_alta` date DEFAULT NULL,
  `fecha_modif` date DEFAULT NULL,
  `fecha_baja` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Indices de la tabla `locales`
--
ALTER TABLE `locales`
  ADD PRIMARY KEY (`id_local`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD UNIQUE KEY `id` (`id_local`);

--
-- AUTO_INCREMENT de la tabla `locales`
--
ALTER TABLE `locales`
  MODIFY `id_local` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
