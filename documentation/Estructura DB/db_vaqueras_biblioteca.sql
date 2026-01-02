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
-- Table structure for table `biblioteca`
--

DROP TABLE IF EXISTS `biblioteca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `biblioteca` (
  `id_biblioteca` int NOT NULL AUTO_INCREMENT,
  `id_user` int NOT NULL,
  `id_videojuego` int NOT NULL,
  `fecha_adquisicion` datetime DEFAULT CURRENT_TIMESTAMP,
  `estado_instalacion` enum('INSTALADO','NO_INSTALADO') COLLATE utf8mb4_unicode_ci DEFAULT 'NO_INSTALADO',
  `es_propietario` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id_biblioteca`),
  UNIQUE KEY `id_user` (`id_user`,`id_videojuego`),
  KEY `fk_lib_juego` (`id_videojuego`),
  CONSTRAINT `fk_lib_juego` FOREIGN KEY (`id_videojuego`) REFERENCES `videojuego` (`id_videojuego`),
  CONSTRAINT `fk_lib_usuario` FOREIGN KEY (`id_user`) REFERENCES `usuario` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biblioteca`
--

LOCK TABLES `biblioteca` WRITE;
/*!40000 ALTER TABLE `biblioteca` DISABLE KEYS */;
INSERT INTO `biblioteca` VALUES (1,14,2,'2025-12-28 16:46:29','INSTALADO',1),(2,14,3,'2025-12-28 18:03:01','INSTALADO',1),(3,14,4,'2025-12-28 18:05:37','NO_INSTALADO',1),(4,14,5,'2025-12-28 18:05:42','NO_INSTALADO',1),(5,10,5,'2025-12-28 22:18:22','INSTALADO',0),(7,16,5,'2025-12-28 22:30:40','NO_INSTALADO',0),(8,10,2,'2025-12-29 04:17:48','INSTALADO',0),(9,15,2,'2025-12-29 04:18:06','NO_INSTALADO',0),(10,15,3,'2025-12-29 04:22:10','NO_INSTALADO',0),(11,10,3,'2025-12-29 04:22:31','NO_INSTALADO',0),(12,10,1,'2026-01-02 10:46:13','NO_INSTALADO',1),(13,10,4,'2026-01-02 11:00:14','NO_INSTALADO',1);
/*!40000 ALTER TABLE `biblioteca` ENABLE KEYS */;
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
