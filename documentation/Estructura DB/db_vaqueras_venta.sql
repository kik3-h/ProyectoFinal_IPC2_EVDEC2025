-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: db_vaqueras
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `venta`
--

DROP TABLE IF EXISTS `venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `venta` (
  `id_venta` int NOT NULL AUTO_INCREMENT,
  `id_user` int NOT NULL,
  `id_empresa` int NOT NULL,
  `id_videojuego` int NOT NULL,
  `fecha_compra` datetime DEFAULT CURRENT_TIMESTAMP,
  `precio_final` decimal(10,2) NOT NULL,
  `retencion_plataforma` decimal(10,2) NOT NULL,
  `ingreso_empresa` decimal(10,2) NOT NULL,
  `tipo_comision` enum('GLOBAL','ESPECIFICA') COLLATE utf8mb4_unicode_ci NOT NULL,
  `porcentaje_aplicado` decimal(5,2) NOT NULL,
  PRIMARY KEY (`id_venta`),
  KEY `fk_venta_usuario` (`id_user`),
  KEY `fk_ingreso_empresa` (`id_empresa`),
  KEY `fk_venta_juego` (`id_videojuego`),
  CONSTRAINT `fk_ingreso_empresa` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id_empresa`),
  CONSTRAINT `fk_venta_juego` FOREIGN KEY (`id_videojuego`) REFERENCES `videojuego` (`id_videojuego`),
  CONSTRAINT `fk_venta_usuario` FOREIGN KEY (`id_user`) REFERENCES `usuario` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venta`
--

LOCK TABLES `venta` WRITE;
/*!40000 ALTER TABLE `venta` DISABLE KEYS */;
INSERT INTO `venta` VALUES (1,14,1,2,'2025-12-28 16:00:00',49.99,7.50,42.49,'GLOBAL',15.00),(2,14,1,3,'2025-12-28 16:00:00',49.99,7.50,42.49,'GLOBAL',15.00),(3,14,1,4,'2025-12-28 16:00:00',49.99,7.50,42.49,'GLOBAL',15.00),(4,14,1,5,'2025-12-28 16:00:00',100.00,15.00,85.00,'GLOBAL',15.00),(5,10,1,1,'2026-01-02 16:46:13',39.99,4.00,35.99,'ESPECIFICA',10.00),(6,10,1,4,'2026-01-02 17:00:15',49.99,5.00,44.99,'ESPECIFICA',10.00);
/*!40000 ALTER TABLE `venta` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-02 11:31:11
