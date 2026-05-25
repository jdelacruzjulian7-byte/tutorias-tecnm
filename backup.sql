-- MySQL dump 10.13  Distrib 8.0.46, for Linux (x86_64)
--
-- Host: localhost    Database: tutoria
-- ------------------------------------------------------
-- Server version	8.0.46

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
  `objetivo` varchar(255) DEFAULT NULL,
  `recursos` varchar(255) DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1x7b5ebl298f40cca193qph1d` (`id_pat`),
  CONSTRAINT `FK1x7b5ebl298f40cca193qph1d` FOREIGN KEY (`id_pat`) REFERENCES `pat` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actividad`
--

LOCK TABLES `actividad` WRITE;
/*!40000 ALTER TABLE `actividad` DISABLE KEYS */;
INSERT INTO `actividad` VALUES (3,_binary '','2026-01-30','Tutor asignado',1,'Presentación y encuadre del programa de tutorías',3,'Dar a conocer el programa institucional de tutorías y establecer acuerdos con el grupo.','Reglamento interno, tríptico informativo del PIT','INFORMACION'),(4,_binary '','2026-02-06','Tutor asignado',2,'Detección de necesidades académicas',3,'Identificar las necesidades académicas, personales y profesionales de los tutorados.','Formato de detección de necesidades','INFORMACION'),(5,_binary '','2026-02-13','Tutor asignado',3,'Hábitos y estrategias de estudio',3,'Fortalecer técnicas y hábitos de estudio para mejorar el rendimiento académico.','Guía de estrategias de estudio, material didáctico','FORMACION'),(6,_binary '','2026-02-20','Tutor asignado',4,'Administración del tiempo',3,'Desarrollar habilidades de planificación y organización del tiempo escolar.','Agenda escolar, formato de horario semanal','FORMACION'),(7,_binary '','2026-02-27','Tutor asignado',5,'Orientación vocacional y profesional',3,'Orientar a los tutorados sobre su perfil profesional y campo laboral de su carrera.','Material informativo de la carrera, plan de estudios','ORIENTACION'),(8,_binary '','2026-03-06','Tutor asignado',6,'Identificación de alumnos en riesgo académico',3,'Detectar alumnos con materias reprobadas o bajo rendimiento y brindar orientación.','Concentrado de calificaciones, instrumento de factores de reprobación','INFORMACION'),(9,_binary '','2026-03-13','Tutor asignado',7,'Motivación y autoestima',3,'Fomentar la confianza, motivación y actitud positiva ante los retos académicos.','Dinámica grupal, material de apoyo psicopedagógico','ORIENTACION'),(10,_binary '','2026-03-20','Tutor asignado',8,'Trabajo en equipo y habilidades sociales',3,'Promover el trabajo colaborativo y el desarrollo de habilidades interpersonales.','Actividad grupal, presentación','FORMACION'),(11,_binary '','2026-03-27','Tutor asignado',9,'Salud mental y manejo del estrés',3,'Brindar herramientas para el manejo del estrés y el cuidado de la salud mental.','Material de salud mental, contactos de orientación psicológica','ORIENTACION'),(12,_binary '','2026-04-03','Tutor asignado',10,'Seguimiento académico — revisión de calificaciones',3,'Revisar el avance académico de los tutorados e identificar áreas de mejora.','Concentrado de calificaciones del periodo','INFORMACION'),(13,_binary '','2026-04-10','Tutor asignado',11,'Orientación para exámenes y evaluaciones',3,'Preparar a los tutorados para los periodos de evaluación con estrategias efectivas.','Calendario de exámenes, guías de estudio','FORMACION'),(14,_binary '','2026-04-17','Tutor asignado',12,'Proyecto de vida y metas personales',3,'Reflexionar sobre metas personales, académicas y profesionales a corto y largo plazo.','Formato de proyecto de vida','ORIENTACION'),(15,_binary '','2026-04-24','Tutor asignado',13,'Cierre y evaluación del semestre tutorial',3,'Evaluar el proceso tutorial del semestre y reconocer los logros alcanzados.','Formato de evaluación del alumno a la tutoría grupal','INFORMACION'),(42,_binary '','2026-01-30','Tutor asignado',1,'Presentación y encuadre del programa de tutorías',6,'Dar a conocer el programa institucional de tutorías y establecer acuerdos con el grupo.','Reglamento interno, tríptico informativo del PIT','INFORMACION'),(43,_binary '','2026-02-06','Tutor asignado',2,'Detección de necesidades académicas',6,'Identificar las necesidades académicas, personales y profesionales de los tutorados.','Formato de detección de necesidades','INFORMACION'),(44,_binary '','2026-02-13','Tutor asignado',3,'Hábitos y estrategias de estudio',6,'Fortalecer técnicas y hábitos de estudio para mejorar el rendimiento académico.','Guía de estrategias de estudio, material didáctico','FORMACION'),(45,_binary '','2026-02-20','Tutor asignado',4,'Administración del tiempo',6,'Desarrollar habilidades de planificación y organización del tiempo escolar.','Agenda escolar, formato de horario semanal','FORMACION'),(46,_binary '','2026-02-27','Tutor asignado',5,'Orientación vocacional y profesional',6,'Orientar a los tutorados sobre su perfil profesional y campo laboral de su carrera.','Material informativo de la carrera, plan de estudios','ORIENTACION'),(47,_binary '','2026-03-06','Tutor asignado',6,'Identificación de alumnos en riesgo académico',6,'Detectar alumnos con materias reprobadas o bajo rendimiento y brindar orientación.','Concentrado de calificaciones, instrumento de factores de reprobación','INFORMACION'),(48,_binary '','2026-03-13','Tutor asignado',7,'Motivación y autoestima',6,'Fomentar la confianza, motivación y actitud positiva ante los retos académicos.','Dinámica grupal, material de apoyo psicopedagógico','ORIENTACION'),(49,_binary '','2026-03-20','Tutor asignado',8,'Trabajo en equipo y habilidades sociales',6,'Promover el trabajo colaborativo y el desarrollo de habilidades interpersonales.','Actividad grupal, presentación','FORMACION'),(50,_binary '','2026-03-27','Tutor asignado',9,'Salud mental y manejo del estrés',6,'Brindar herramientas para el manejo del estrés y el cuidado de la salud mental.','Material de salud mental, contactos de orientación psicológica','ORIENTACION'),(51,_binary '','2026-04-03','Tutor asignado',10,'Seguimiento académico — revisión de calificaciones',6,'Revisar el avance académico de los tutorados e identificar áreas de mejora.','Concentrado de calificaciones del periodo','INFORMACION'),(52,_binary '','2026-04-10','Tutor asignado',11,'Orientación para exámenes y evaluaciones',6,'Preparar a los tutorados para los periodos de evaluación con estrategias efectivas.','Calendario de exámenes, guías de estudio','FORMACION'),(53,_binary '','2026-04-17','Tutor asignado',12,'Proyecto de vida y metas personales',6,'Reflexionar sobre metas personales, académicas y profesionales a corto y largo plazo.','Formato de proyecto de vida','ORIENTACION'),(54,_binary '','2026-04-24','Tutor asignado',13,'Cierre y evaluación del semestre tutorial',6,'Evaluar el proceso tutorial del semestre y reconocer los logros alcanzados.','Formato de evaluación del alumno a la tutoría grupal','INFORMACION');
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
INSERT INTO `asignacion` VALUES (1,_binary '\0',2,1,1),(2,_binary '',1,2,1),(3,_binary '',1,2,2),(4,_binary '',1,2,3),(5,_binary '',1,2,5),(6,_binary '',1,4,6),(7,_binary '',1,4,7);
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
  `observacion` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_asistencia_tutorado_sesion` (`id_tutorado`,`id_sesion`),
  KEY `FKi55wocyhkhhly7xg70a3bsny7` (`id_sesion`),
  CONSTRAINT `FKi55wocyhkhhly7xg70a3bsny7` FOREIGN KEY (`id_sesion`) REFERENCES `sesion` (`id`),
  CONSTRAINT `FKk3ve1ek3tha160vve1qkwhi7j` FOREIGN KEY (`id_tutorado`) REFERENCES `tutorado` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asistencia`
--

LOCK TABLES `asistencia` WRITE;
/*!40000 ALTER TABLE `asistencia` DISABLE KEYS */;
INSERT INTO `asistencia` VALUES (1,_binary '',1,1,'es muy despistado');
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
  `nombre` varchar(120) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_carrera_nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrera`
--

LOCK TABLES `carrera` WRITE;
/*!40000 ALTER TABLE `carrera` DISABLE KEYS */;
INSERT INTO `carrera` VALUES (3,'Contaduria Fiscal'),(4,'Derecho Fiscal'),(1,'Ingeniería en Sistemas Computacionales'),(6,'ingenieria industrial');
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documento`
--

LOCK TABLES `documento` WRITE;
/*!40000 ALTER TABLE `documento` DISABLE KEYS */;
INSERT INTO `documento` VALUES (1,'2026-07-17','CARNET',1,13),(2,'2026-05-15','CONSTANCIA',1,16),(3,'2026-08-14','OFICIO_TERMINO',2,13),(4,'2026-05-21','OFICIO_TERMINO',1,13);
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
  `es_general` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_pat_carrera_semestre` (`id_carrera`,`id_semestre`),
  KEY `FK5xajyn5ghoax6n5lsl0yqk1u0` (`id_semestre`),
  CONSTRAINT `FK5xajyn5ghoax6n5lsl0yqk1u0` FOREIGN KEY (`id_semestre`) REFERENCES `semestre` (`id`),
  CONSTRAINT `FKpw618f24esadb61cwupsxbk5p` FOREIGN KEY (`id_carrera`) REFERENCES `carrera` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pat`
--

LOCK TABLES `pat` WRITE;
/*!40000 ALTER TABLE `pat` DISABLE KEYS */;
INSERT INTO `pat` VALUES (3,_binary '','PENDIENTE','2025',1,2,_binary '\0'),(6,_binary '','PENDIENTE','3.0',NULL,1,_binary '');
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
  `nombre` varchar(30) NOT NULL,
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
  `anio` int DEFAULT NULL,
  `periodo` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_semestre_periodo_anio` (`periodo`,`anio`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `semestre`
--

LOCK TABLES `semestre` WRITE;
/*!40000 ALTER TABLE `semestre` DISABLE KEYS */;
INSERT INTO `semestre` VALUES (1,_binary '','2026-07-17','2026-01-26','Enero-Julio',NULL,NULL),(2,_binary '','2026-12-11','2026-08-24','Julio-Agosto',NULL,NULL),(3,_binary '','2027-06-25','2027-01-25','enero-julio',NULL,NULL);
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
  `fecha` date DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `id_tutor` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9gco9khqw48mahpufcrlerssd` (`id_actividad`),
  KEY `FK42d25em3fmx3ty3ius4lhqesm` (`id_tutor`),
  CONSTRAINT `FK42d25em3fmx3ty3ius4lhqesm` FOREIGN KEY (`id_tutor`) REFERENCES `tutor` (`id`),
  CONSTRAINT `FK9gco9khqw48mahpufcrlerssd` FOREIGN KEY (`id_actividad`) REFERENCES `actividad` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sesion`
--

LOCK TABLES `sesion` WRITE;
/*!40000 ALTER TABLE `sesion` DISABLE KEYS */;
INSERT INTO `sesion` VALUES (1,_binary '','V-1','10:00:00.000000','09:00:00.000000',8,'2026-05-18','Sesion 3.- Dia del estudiante',1);
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
  `activo` tinyint(1) DEFAULT NULL,
  `foto` varchar(255) DEFAULT NULL,
  `id_carrera` bigint DEFAULT NULL,
  `id_usuario` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tutor_usuario` (`id_usuario`),
  KEY `FKglotqtx6xbxvb8h4iv5il15a` (`id_carrera`),
  CONSTRAINT `FKatqlk4alesvwki6uqidbew46d` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`),
  CONSTRAINT `FKglotqtx6xbxvb8h4iv5il15a` FOREIGN KEY (`id_carrera`) REFERENCES `carrera` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tutor`
--

LOCK TABLES `tutor` WRITE;
/*!40000 ALTER TABLE `tutor` DISABLE KEYS */;
INSERT INTO `tutor` VALUES (1,1,'tutores/1778051235966_WhatsApp Image 2026-04-27 at 8.51.03 AM.jpeg',1,8),(2,1,'tutores/1778051199201_alumno1.webp',4,12),(3,1,'tutores/1778682812987_alumna2.webp',1,17),(4,1,'tutores/1779391511285_alumna1.webp',1,19);
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
  `activo` tinyint(1) DEFAULT NULL,
  `foto` varchar(255) DEFAULT NULL,
  `matricula` varchar(20) NOT NULL,
  `semestre_ingreso` varchar(255) DEFAULT NULL,
  `id_carrera` bigint DEFAULT NULL,
  `id_usuario` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tutorado_matricula` (`matricula`),
  UNIQUE KEY `uk_tutorado_usuario` (`id_usuario`),
  KEY `FK2d9epa1iw4kr1t1mw3bnqp06a` (`id_carrera`),
  CONSTRAINT `FK2d9epa1iw4kr1t1mw3bnqp06a` FOREIGN KEY (`id_carrera`) REFERENCES `carrera` (`id`),
  CONSTRAINT `FKq2ieefknumkmvnl89yhy4clu` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tutorado`
--

LOCK TABLES `tutorado` WRITE;
/*!40000 ALTER TABLE `tutorado` DISABLE KEYS */;
INSERT INTO `tutorado` VALUES (1,0,'tutorados/1778032256224_alumno1.webp','012036547','Enero-Julio',1,9),(2,1,'tutorados/1778052286102_alumna2.webp','22522222','Enero-Julio',4,13),(3,1,'tutorados/1778052331297_alumna1.webp','22523652','Enero-Julio',3,14),(5,1,'tutorados/1778073997205_alumno1.webp','0123456789','Enero-Julio',4,16),(6,1,'tutorados/1779391575657_alumno1.webp','2252','Enero-Julio',1,20),(7,1,'tutorados/1779391615927_alumno2.avif','2552','Enero-Julio',1,21);
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
  `apellido` varchar(100) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `correo` varchar(150) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `id_rol` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_usuario_correo` (`correo`),
  KEY `FKmyv3138vvci6kaq3y5kt4cntu` (`id_rol`),
  CONSTRAINT `FKmyv3138vvci6kaq3y5kt4cntu` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (2,'Admin','$2a$10$fOXB4jZkT/jTjOXIYSim8.jBksOhNE9zWuzVLN.KOyx10xJK.hNG.','admin@admin.com','Admin',1),(4,'Diaz','$2a$10$mYtD9LULTrZf7WAUI85smu2dlFv.0vjl9wkEsH0Zp6AjhRC.Xu6my','diaz@gmail.com','Porfirio',5),(5,'zavala','$2a$10$KpSMz7QwJCRJv7bfCzlQSuxFdY0ZpOhnXp1.H1XaXdOvNnkRVSA0a','zava@outlook.com','Maria',4),(8,'benitez','$2a$10$QLRcA5.6sJW/zXa7SMYwquDvfxm7RMnh8KN5Xmdy6m1.CqwuCaAou','juan.perez@gmail.com','juan',2),(9,'birago','$2a$10$tXc6XfH/L5gPrON8E8Fs3uZOpW16DRfi1ApwpHv5icfvhY4tJgSuC','mopet@gmail.com','mopert',3),(12,'Lopez','$2a$10$9pOrYz7/.zcfRaJAqhCDYuPfJUAKOLrf2D/1BAaOp13DE5gE1SkIK','lopez@gmail.com','Julio',2),(13,'Castillo','$2a$10$g7e3Ww/3rqJGqcSgOkW/YuRqYba.m/srLv3MMd3og5T6SWpMChtUS','sand@gmail.com','Sandra',3),(14,'Valenzo','$2a$10$lzYnvWvPVGcKXDHIiImSl.bMUFx41/.yENbL0LFTyszz6zsu/9DNu','vale@outlook.com','Robe',3),(16,'Segunda Avance','$2a$10$rzaEdr2j2o8l.jkxjK7hye2CQZoN/kIJ6TVl75I1ria.ZneOKDmrK','avance@prueba.com','Funciona Prueba',3),(17,'x','$2a$10$zrFEJxHg.Kd8z03ZBzK4kuvUgXXeM.RZ4S0u9zCwlkoLohuvq839C','x@gmail.com','x',2),(18,'Castillo','$2a$10$.VrakENSMJpSDKkAMZoef.D59DpN3rs6pmHddMrvfEIsZIIwBE2VO','feli@gmail.com','feli',6),(19,'4','$2a$10$GY2RC3KuywDSd82XXcM44ehjkClKfTjI/WPZCZIKmC4n7ZCgtFZKi','tutor@tutor.com','tutor',2),(20,'...','$2a$10$lc5tw2UNxzeVT6rcrNW3Getjbo/HWtqfIz8xKStyMwhXqoCIYHdqK','tutorado1@gmail.com','tutorado1',3),(21,'...','$2a$10$IGw99FYYDdqTTvRPCaoZPuzd/mJrjHnKq6QB1ASYKC/VzPO0lPYHq','tutorado2@gmail.com','tutorado2',3);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-23 21:52:32
