-- MySQL dump 10.13  Distrib 9.5.0, for Win64 (x86_64)
--
-- Host: localhost    Database: tutoria
-- ------------------------------------------------------
-- Server version	9.5.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '4d8a7b8e-ce0b-11f0-9d1e-e41fd53a576d:1-1049';

--
-- Table structure for table `actividad`
--

DROP TABLE IF EXISTS `actividad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actividad` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activo` bit(1) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `ponente` varchar(255) DEFAULT NULL,
  `semana` int DEFAULT NULL,
  `tema` varchar(255) DEFAULT NULL,
  `id_pat` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1x7b5ebl298f40cca193qph1d` (`id_pat`),
  CONSTRAINT `FK1x7b5ebl298f40cca193qph1d` FOREIGN KEY (`id_pat`) REFERENCES `pat` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actividad`
--

LOCK TABLES `actividad` WRITE;
/*!40000 ALTER TABLE `actividad` DISABLE KEYS */;
/*!40000 ALTER TABLE `actividad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asignacion`
--

DROP TABLE IF EXISTS `asignacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activo` bit(1) DEFAULT NULL,
  `id_semestre` bigint DEFAULT NULL,
  `id_tutor` bigint DEFAULT NULL,
  `id_tutorado` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_asignacion_tutor_tutorado_semestre` (`id_tutor`,`id_tutorado`,`id_semestre`),
  KEY `FKkxa72o2x7h21ywvplo7o942se` (`id_semestre`),
  KEY `FK8bg3bpjo4linc0c2l390n1970` (`id_tutorado`),
  CONSTRAINT `FK8bg3bpjo4linc0c2l390n1970` FOREIGN KEY (`id_tutorado`) REFERENCES `tutorado` (`id`),
  CONSTRAINT `FKkxa72o2x7h21ywvplo7o942se` FOREIGN KEY (`id_semestre`) REFERENCES `semestre` (`id`),
  CONSTRAINT `FKt5dtnu0ew2ql9no61sm4owmmb` FOREIGN KEY (`id_tutor`) REFERENCES `tutor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignacion`
--

LOCK TABLES `asignacion` WRITE;
/*!40000 ALTER TABLE `asignacion` DISABLE KEYS */;
INSERT INTO `asignacion` VALUES (3,_binary '',5,16,5),(4,_binary '',1,19,7),(5,_binary '',1,19,8),(7,_binary '',1,19,6);
/*!40000 ALTER TABLE `asignacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asistencia`
--

DROP TABLE IF EXISTS `asistencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asistencia` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `presente` bit(1) DEFAULT NULL,
  `id_sesion` bigint DEFAULT NULL,
  `id_tutorado` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_asistencia_tutorado_sesion` (`id_tutorado`,`id_sesion`),
  KEY `FKi55wocyhkhhly7xg70a3bsny7` (`id_sesion`),
  CONSTRAINT `FKi55wocyhkhhly7xg70a3bsny7` FOREIGN KEY (`id_sesion`) REFERENCES `sesion` (`id`),
  CONSTRAINT `FKk3ve1ek3tha160vve1qkwhi7j` FOREIGN KEY (`id_tutorado`) REFERENCES `tutorado` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asistencia`
--

LOCK TABLES `asistencia` WRITE;
/*!40000 ALTER TABLE `asistencia` DISABLE KEYS */;
/*!40000 ALTER TABLE `asistencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carrera`
--

DROP TABLE IF EXISTS `carrera`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrera` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_carrera_nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrera`
--

LOCK TABLES `carrera` WRITE;
/*!40000 ALTER TABLE `carrera` DISABLE KEYS */;
INSERT INTO `carrera` VALUES (4,'Administración de Empresas'),(6,'Arquitectura'),(3,'Contaduría Pública'),(7,'Derecho'),(5,'Ingeniería Civil'),(1,'Ingeniería en Sistemas Computacionales'),(2,'Ingeniería Industrial'),(9,'Medicina'),(10,'Mercadotecnia'),(8,'Psicología');
/*!40000 ALTER TABLE `carrera` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documento`
--

DROP TABLE IF EXISTS `documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_emision` date DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `id_semestre` bigint DEFAULT NULL,
  `id_usuario` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjud5tv8bd2jfcrx0yo953lw8h` (`id_semestre`),
  KEY `FKj2mcq74rsx73865hgk2neuvl7` (`id_usuario`),
  CONSTRAINT `FKj2mcq74rsx73865hgk2neuvl7` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`),
  CONSTRAINT `FKjud5tv8bd2jfcrx0yo953lw8h` FOREIGN KEY (`id_semestre`) REFERENCES `semestre` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documento`
--

LOCK TABLES `documento` WRITE;
/*!40000 ALTER TABLE `documento` DISABLE KEYS */;
/*!40000 ALTER TABLE `documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pat`
--

DROP TABLE IF EXISTS `pat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pat` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activo` bit(1) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `id_carrera` bigint DEFAULT NULL,
  `id_semestre` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpw618f24esadb61cwupsxbk5p` (`id_carrera`),
  KEY `FK5xajyn5ghoax6n5lsl0yqk1u0` (`id_semestre`),
  CONSTRAINT `FK5xajyn5ghoax6n5lsl0yqk1u0` FOREIGN KEY (`id_semestre`) REFERENCES `semestre` (`id`),
  CONSTRAINT `FKpw618f24esadb61cwupsxbk5p` FOREIGN KEY (`id_carrera`) REFERENCES `carrera` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pat`
--

LOCK TABLES `pat` WRITE;
/*!40000 ALTER TABLE `pat` DISABLE KEYS */;
INSERT INTO `pat` VALUES (1,_binary '','Activo','1.2',6,3),(3,_binary '','Activo','3.0',1,1),(4,_binary '','Activo','3.0',1,1),(5,_binary '','PENDIENTE','2025',1,3),(6,_binary '','APROBADO','1.2',6,1);
/*!40000 ALTER TABLE `pat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rol_nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` VALUES (1,'ADMIN'),(4,'COORDINADOR'),(5,'JEFE'),(6,'SUBDIRECTOR'),(2,'TUTOR'),(3,'TUTORADO');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `semestre`
--

DROP TABLE IF EXISTS `semestre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `semestre` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activo` bit(1) DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `semestre`
--

LOCK TABLES `semestre` WRITE;
/*!40000 ALTER TABLE `semestre` DISABLE KEYS */;
INSERT INTO `semestre` VALUES (1,_binary '','2025-06-20','2025-01-20','2025-A'),(2,_binary '','2025-12-15','2025-08-01','2025-B'),(3,_binary '','2026-06-20','2026-01-20','2026-A'),(4,_binary '\0','2024-12-15','2024-08-01','2024-B'),(5,_binary '\0','2024-06-20','2024-01-20','2024-A');
/*!40000 ALTER TABLE `semestre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sesion`
--

DROP TABLE IF EXISTS `sesion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sesion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activo` bit(1) DEFAULT NULL,
  `aula` varchar(255) DEFAULT NULL,
  `hora_fin` time(6) DEFAULT NULL,
  `hora_inicio` time(6) DEFAULT NULL,
  `id_actividad` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9gco9khqw48mahpufcrlerssd` (`id_actividad`),
  CONSTRAINT `FK9gco9khqw48mahpufcrlerssd` FOREIGN KEY (`id_actividad`) REFERENCES `actividad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sesion`
--

LOCK TABLES `sesion` WRITE;
/*!40000 ALTER TABLE `sesion` DISABLE KEYS */;
/*!40000 ALTER TABLE `sesion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tutor`
--

DROP TABLE IF EXISTS `tutor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tutor` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activo` tinyint(1) DEFAULT '1',
  `foto` varchar(255) DEFAULT NULL,
  `id_carrera` bigint DEFAULT NULL,
  `id_usuario` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKtkmr8h46o6oyn1wa2u65hg7dy` (`id_usuario`),
  UNIQUE KEY `uk_tutor_usuario` (`id_usuario`),
  KEY `FKglotqtx6xbxvb8h4iv5il15a` (`id_carrera`),
  CONSTRAINT `FKatqlk4alesvwki6uqidbew46d` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`),
  CONSTRAINT `FKglotqtx6xbxvb8h4iv5il15a` FOREIGN KEY (`id_carrera`) REFERENCES `carrera` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tutor`
--

LOCK TABLES `tutor` WRITE;
/*!40000 ALTER TABLE `tutor` DISABLE KEYS */;
INSERT INTO `tutor` VALUES (15,1,'1776921380164_R.jpg',5,23),(16,1,NULL,2,22),(17,1,'tutores/1776950982407_RH.jpg',5,26),(18,1,'tutores/1776951048614_sist.jpg',8,30),(19,1,'tutores/1776951860965_WhatsApp Image 2026-04-17 at 11.28.11 AM.jpeg',3,33);
/*!40000 ALTER TABLE `tutor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tutorado`
--

DROP TABLE IF EXISTS `tutorado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tutorado` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activo` tinyint(1) DEFAULT '1',
  `foto` varchar(255) DEFAULT NULL,
  `matricula` varchar(255) DEFAULT NULL,
  `semestre_ingreso` varchar(255) DEFAULT NULL,
  `id_carrera` bigint DEFAULT NULL,
  `id_usuario` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tutorado_matricula` (`matricula`),
  UNIQUE KEY `UKsl6utscwkqy4bpjvxp7ck6owp` (`id_usuario`),
  UNIQUE KEY `uk_tutorado_usuario` (`id_usuario`),
  KEY `FK2d9epa1iw4kr1t1mw3bnqp06a` (`id_carrera`),
  CONSTRAINT `FK2d9epa1iw4kr1t1mw3bnqp06a` FOREIGN KEY (`id_carrera`) REFERENCES `carrera` (`id`),
  CONSTRAINT `FKq2ieefknumkmvnl89yhy4clu` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tutorado`
--

LOCK TABLES `tutorado` WRITE;
/*!40000 ALTER TABLE `tutorado` DISABLE KEYS */;
INSERT INTO `tutorado` VALUES (5,1,'1776923262868_IMG_5821.JPG','23520007','2023',7,24),(6,1,'1776950607541_WhatsApp Image 2026-04-01 at 5.09.19 PM.jpeg','22521111','2025',7,29),(7,1,'tutorados/1776951595259_inicio.jpg','123456789','2022',6,27),(8,1,'tutorados/1776951961287_WhatsApp Image 2026-04-20 at 6.49.44 AM (2).jpeg','lllll141212','seme ene/jum/2026',4,28);
/*!40000 ALTER TABLE `tutorado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apellido` varchar(255) DEFAULT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  `correo` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `id_rol` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_usuario_correo` (`correo`),
  KEY `FKmyv3138vvci6kaq3y5kt4cntu` (`id_rol`),
  CONSTRAINT `FKmyv3138vvci6kaq3y5kt4cntu` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (11,'Perez','$2a$10$fOXB4jZkT/jTjOXIYSim8.jBksOhNE9zWuzVLN.KOyx10xJK.hNG.','juan.perez@gmail.com','Juan',1),(22,'De La Cruz Julian','$2a$10$LAVR9.vLYSOAbT0q3IHVPOMWpy7R3p2FvDOMCY2n4jBkv9WJ0LkOa','jose@gmail.com','José Antonio',2),(23,'Lopez','$2a$10$vekrg/xiNZdKGbXYktOvR.7.QC2VXPSICDRxhZRJpkWBoxeLwDaIu','luis@gmail.com','Luis',2),(24,'Picuyo','$2a$10$rYOa4FyCcn5frNsbsDERheP6/tgGHgEkPIG5StsJjH2EtmX52dpKS','picuyo@outlook.com','Yaritzi',3),(25,'juan','$2a$10$Af1wLRfq5lfynadP6o0YruPdpktV4ZnAJCS5nwK8O0TvN0l7xU64W','jj@gmail.com','Jesus',4),(26,'Castillo','$2a$10$xDbyi3kiS0pd/Eopz80/9u/Zj3ku2M2HRE/2lAWOdJgkqqEp6QtcO','felix@gmail.com','Felix',2),(27,'Villanueva','$2a$10$c4F6ZwbTT178jYfhzozZ6enbLKCXzwWOC9TnI92Ptl24SemMFWmqK','ale@gmail.com','Alexa',3),(28,'zavala','$2a$10$wiZhxPVGRgId2wv65PDq/eMYpTajW/vJ7qQ0Nnon93NuDljaZ4BJS','mari@gmail.com','maria',3),(29,'Fuentes','$2a$10$pYP4Q2aRvWQxGke2hi/wSuDZYlNuBGwfVMk2XlC4CoNitZRLhLk5S','rodri@gmail.com','Rodrigo',3),(30,'Vera','$2a$10$vsn3xOecaxhUDnj/Z2XzIOxDCwN4Yh/BwEcjXPVgjaLZpGnsIOxny','tomy@gmail.com','Tomas',2),(33,'sss','$2a$10$dLveRMUUH0oQRpu4aSJg1OGkfE9VbOK6rFGoRg7zJlWThGxb4IoC2','prueba@sls.com','prueba',2);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-27 23:18:24