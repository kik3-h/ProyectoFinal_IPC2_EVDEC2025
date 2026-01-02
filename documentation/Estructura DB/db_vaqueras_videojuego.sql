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
-- Table structure for table `videojuego`
--

DROP TABLE IF EXISTS `videojuego`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `videojuego` (
  `id_videojuego` int NOT NULL AUTO_INCREMENT,
  `id_empresa` int NOT NULL,
  `titulo` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text COLLATE utf8mb4_unicode_ci,
  `precio` decimal(10,2) NOT NULL,
  `recursos_minimos` text COLLATE utf8mb4_unicode_ci,
  `fecha_lanzamiento` date DEFAULT NULL,
  `estado` enum('ACTIVO','SUSPENDIDO') COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVO',
  `clasificacion_edad` enum('E','T','M') COLLATE utf8mb4_unicode_ci NOT NULL,
  `edad_minima` int NOT NULL,
  PRIMARY KEY (`id_videojuego`),
  KEY `fk_juego_empresa` (`id_empresa`),
  CONSTRAINT `fk_juego_empresa` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id_empresa`),
  CONSTRAINT `videojuego_chk_1` CHECK ((`precio` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `videojuego`
--

LOCK TABLES `videojuego` WRITE;
/*!40000 ALTER TABLE `videojuego` DISABLE KEYS */;
INSERT INTO `videojuego` VALUES (1,1,'Halo Master Chief Collection','Descripción actualizada',39.99,'8GB RAM, i5','2025-12-01','ACTIVO','T',13),(2,1,'hunt','Juego multijugador 2',49.99,'8GB RAM, i5','2025-12-01','ACTIVO','T',13),(3,1,'Batlefield','Juego multijugador 3',49.99,'8GB RAM, i5','2025-12-01','ACTIVO','T',13),(4,1,'Dragon Ball','Juego multijugador 4',49.99,'8GB RAM, i5','2025-12-01','ACTIVO','T',13),(5,1,'It Takes Two','Juego multijugador 5',100.00,'8GB RAM, i5','2025-12-01','ACTIVO','T',13),(6,1,'Rust','Juego multijugador 6',100.00,'8GB RAM, i5','2025-12-01','ACTIVO','T',13),(7,1,'Silksong','Juego multijugador 7',100.00,'8GB RAM, i5','2025-12-01','ACTIVO','T',13),(9,1,'Jedi Fallen Order','Videojuego de star wars',89.99,'Interl core i 5, ram 16 GB, SSD 250GB',NULL,'ACTIVO','E',13),(10,1,'Cicsito','Cicsito jaskjda',100.00,'Interl core i 5, ram 16 GB, SSD 250GB',NULL,'ACTIVO','E',18);
/*!40000 ALTER TABLE `videojuego` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-02 11:31:10
