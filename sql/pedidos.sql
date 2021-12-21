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
-- Estructura de tabla para la tabla `pedidos`
--

CREATE TABLE `pedidos` (
  `id_pedido` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `id_anuncio` int(11) NOT NULL,
  `id_local` int(11) NOT NULL,
  `estado` varchar(20) COLLATE utf8_spanish_ci DEFAULT NULL,
  `id_stripe` varchar(200) COLLATE utf8_spanish_ci NOT NULL,
  `payload` varchar(200) COLLATE utf8_spanish_ci DEFAULT NULL,
  `precio_total` decimal(12,2) DEFAULT NULL,
  `ciudad` varchar(400) COLLATE utf8_spanish_ci DEFAULT NULL,
  `cod_pais` varchar(50) COLLATE utf8_spanish_ci DEFAULT NULL,
  `cod_postal` varchar(50) COLLATE utf8_spanish_ci DEFAULT NULL,
  `estadoDir` varchar(100) COLLATE utf8_spanish_ci DEFAULT NULL,
  `direccion1` varchar(400) COLLATE utf8_spanish_ci DEFAULT NULL,
  `direccion2` varchar(400) COLLATE utf8_spanish_ci DEFAULT NULL,
  `nombre_ped` varchar(100) COLLATE utf8_spanish_ci DEFAULT NULL,
  `mail_ped` varchar(400) COLLATE utf8_spanish_ci DEFAULT NULL,
  `telefono_ped` varchar(20) COLLATE utf8_spanish_ci DEFAULT NULL,
  `fecha_alta` date DEFAULT NULL,
  `fecha_modif` date DEFAULT NULL,
  `fecha_baja` date DEFAULT NULL,
  `flag` varchar(40) COLLATE utf8_spanish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;


--
-- Indices de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`id_pedido`),
  ADD UNIQUE KEY `id` (`id_pedido`);

--
-- AUTO_INCREMENT de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  MODIFY `id_pedido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
