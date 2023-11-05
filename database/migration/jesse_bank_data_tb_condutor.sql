-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: jesse_bank_data
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.28-MariaDB

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
-- Table structure for table `tb_condutor`
--

DROP TABLE IF EXISTS `tb_condutor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_condutor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cell` varchar(255) DEFAULT NULL,
  `dataDeCriacao` datetime(6) DEFAULT NULL,
  `dataNascimento` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fotosUrls` varchar(255) DEFAULT NULL,
  `idade` int(11) DEFAULT NULL,
  `nat` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `sexo` varchar(255) DEFAULT NULL,
  `sobrenome` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_condutor`
--

LOCK TABLES `tb_condutor` WRITE;
/*!40000 ALTER TABLE `tb_condutor` DISABLE KEYS */;
INSERT INTO `tb_condutor` VALUES (2,'7783297933','2023-09-06 03:10:29.000000','1971-08-18T09:49:47.726Z','mitesh.banerjee@example.com','https://randomuser.me/api/portraits/men/88.jpg',52,'IN','Mr Mitesh','9094035647','male','Banerjee',NULL),(3,'92269534','2023-09-06 03:10:42.000000','1987-01-17T07:59:18.048Z','ines.gjesdal@example.com','https://randomuser.me/api/portraits/women/92.jpg',36,'NO','Ms Ines','74843040','female','Gjesdal',NULL),(4,'41277510','2023-09-06 03:10:44.000000','1966-01-03T05:48:45.684Z','katarina.bruheim@example.com','https://randomuser.me/api/portraits/women/35.jpg',57,'NO','Ms Katarina','34698905','female','Bruheim',NULL),(5,'(096) B45-5149','2023-09-06 03:10:46.000000','1995-08-30T19:36:04.818Z','stefaniya.grodskiy@example.com','https://randomuser.me/api/portraits/women/28.jpg',28,'UA','Miss Stefaniya','(097) T48-2399','female','Grodskiy',NULL),(6,'(945) 320-5131','2023-09-06 03:13:13.000000','1956-05-21T02:59:09.716Z','pauline.barrett@example.com','https://randomuser.me/api/portraits/women/89.jpg',67,'US','Ms Pauline','(299) 646-7738','female','Barrett',NULL);
/*!40000 ALTER TABLE `tb_condutor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-12 21:39:54
