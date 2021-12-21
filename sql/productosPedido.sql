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
-- Estructura de tabla para la tabla `productosPedido`
--

CREATE TABLE `productosPedido` (
  `id_productoPedido` int(11) NOT NULL,
  `id_pedido` int(11) NOT NULL,
  `id_anuncio` int(11) NOT NULL,
  `cantidad` int(5) NOT NULL,
  `precioProductoPedido` decimal(12,2) DEFAULT NULL,
  `flagDescuento` varchar(1) COLLATE utf8_spanish_ci DEFAULT 'N',
  `fecha_alta` date DEFAULT NULL,
  `fecha_modif` date DEFAULT NULL,
  `fecha_baja` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Indices de la tabla `productosPedido`
--
ALTER TABLE `productosPedido`
  ADD PRIMARY KEY (`id_productoPedido`),
  ADD UNIQUE KEY `id` (`id_productoPedido`);

--
-- AUTO_INCREMENT de la tabla `productosPedido`
--
ALTER TABLE `productosPedido`
  MODIFY `id_productoPedido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=420;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
