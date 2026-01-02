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
-- Table structure for table `comentario`
--

DROP TABLE IF EXISTS `comentario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comentario` (
  `id_comentario` int NOT NULL AUTO_INCREMENT,
  `id_user` int NOT NULL,
  `id_videojuego` int NOT NULL,
  `id_comentario_padre` int DEFAULT NULL,
  `texto` text COLLATE utf8mb4_unicode_ci,
  `calificacion` int DEFAULT NULL,
  `texto_visible` tinyint(1) DEFAULT '1',
  `fecha` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_comentario`),
  KEY `fk_com_usuario` (`id_user`),
  KEY `fk_com_juego` (`id_videojuego`),
  KEY `fk_com_padre` (`id_comentario_padre`),
  CONSTRAINT `fk_com_juego` FOREIGN KEY (`id_videojuego`) REFERENCES `videojuego` (`id_videojuego`),
  CONSTRAINT `fk_com_padre` FOREIGN KEY (`id_comentario_padre`) REFERENCES `comentario` (`id_comentario`) ON DELETE CASCADE,
  CONSTRAINT `fk_com_usuario` FOREIGN KEY (`id_user`) REFERENCES `usuario` (`id_user`),
  CONSTRAINT `comentario_chk_1` CHECK ((`calificacion` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comentario`
--

LOCK TABLES `comentario` WRITE;
/*!40000 ALTER TABLE `comentario` DISABLE KEYS */;
INSERT INTO `comentario` VALUES (1,14,2,NULL,'Muy buen juego, lo recomiendo jaksdjas',5,1,'2025-12-28 21:58:29'),(2,14,2,1,'Estoy de acuerdo!',4,1,'2025-12-28 21:59:45'),(3,14,2,NULL,'Muy buen juego, aunque cambio de opinion',5,1,'2025-12-28 22:00:53');
/*!40000 ALTER TABLE `comentario` ENABLE KEYS */;
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
