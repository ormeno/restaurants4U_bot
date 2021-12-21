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
-- Estructura de tabla para la tabla `localUsuarios`
--

CREATE TABLE `localUsuarios` (
  `id_localUsuario` int(11) NOT NULL,
  `id_local` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `cont_mensajes` int(11) DEFAULT '1',
  `tipo` varchar(40) COLLATE utf8_spanish_ci DEFAULT NULL,
  `tipo_usuario` varchar(2) COLLATE utf8_spanish_ci DEFAULT NULL,
  `fecha_alta` date DEFAULT NULL,
  `fecha_modif` date DEFAULT NULL,
  `fecha_baja` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Indices de la tabla `localUsuarios`
--
ALTER TABLE `localUsuarios`
  ADD PRIMARY KEY (`id_localUsuario`),
  ADD UNIQUE KEY `id` (`id_localUsuario`);

--
-- AUTO_INCREMENT de la tabla `localUsuarios`
--
ALTER TABLE `localUsuarios`
  MODIFY `id_localUsuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
