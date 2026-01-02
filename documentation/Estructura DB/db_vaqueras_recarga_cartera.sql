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
-- Table structure for table `recarga_cartera`
--

DROP TABLE IF EXISTS `recarga_cartera`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recarga_cartera` (
  `id_recarga` int NOT NULL AUTO_INCREMENT,
  `id_user` int NOT NULL,
  `monto_recargado` decimal(10,2) NOT NULL,
  `fecha_recarga` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_recarga`),
  KEY `fk_recarga_usuario` (`id_user`),
  CONSTRAINT `fk_recarga_usuario` FOREIGN KEY (`id_user`) REFERENCES `usuario` (`id_user`),
  CONSTRAINT `recarga_cartera_chk_1` CHECK ((`monto_recargado` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recarga_cartera`
--

LOCK TABLES `recarga_cartera` WRITE;
/*!40000 ALTER TABLE `recarga_cartera` DISABLE KEYS */;
INSERT INTO `recarga_cartera` VALUES (1,14,50.00,'2025-12-28 04:15:07'),(2,14,50.00,'2025-12-28 04:17:03'),(3,14,200.00,'2025-12-28 16:39:03'),(4,15,200.00,'2025-12-28 18:10:17'),(5,16,100.00,'2026-01-02 06:01:55'),(6,10,200.00,'2026-01-02 10:46:06');
/*!40000 ALTER TABLE `recarga_cartera` ENABLE KEYS */;
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
