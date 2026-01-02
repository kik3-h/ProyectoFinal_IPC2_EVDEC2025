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
-- Table structure for table `instalacion_prestamo`
--

DROP TABLE IF EXISTS `instalacion_prestamo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `instalacion_prestamo` (
  `id_instalacion` int NOT NULL AUTO_INCREMENT,
  `id_user_prestando` int NOT NULL,
  `id_videojuego` int NOT NULL,
  `estado` enum('INSTALADO','NO_INSTALADO') COLLATE utf8mb4_unicode_ci DEFAULT 'NO_INSTALADO',
  `fecha_ultimo_cambio` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_instalacion`),
  KEY `fk_inst_usuario` (`id_user_prestando`),
  KEY `fk_inst_juego` (`id_videojuego`),
  CONSTRAINT `fk_inst_juego` FOREIGN KEY (`id_videojuego`) REFERENCES `videojuego` (`id_videojuego`),
  CONSTRAINT `fk_inst_usuario` FOREIGN KEY (`id_user_prestando`) REFERENCES `usuario` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instalacion_prestamo`
--

LOCK TABLES `instalacion_prestamo` WRITE;
/*!40000 ALTER TABLE `instalacion_prestamo` DISABLE KEYS */;
INSERT INTO `instalacion_prestamo` VALUES (2,10,5,'INSTALADO','2025-12-28 22:23:05'),(5,16,5,'NO_INSTALADO','2025-12-28 22:30:40'),(7,15,2,'NO_INSTALADO','2025-12-29 04:18:06'),(8,15,3,'NO_INSTALADO','2025-12-29 04:22:10'),(9,10,3,'NO_INSTALADO','2025-12-29 04:22:31'),(10,10,2,'INSTALADO','2025-12-29 04:25:10');
/*!40000 ALTER TABLE `instalacion_prestamo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-02 11:31:09
