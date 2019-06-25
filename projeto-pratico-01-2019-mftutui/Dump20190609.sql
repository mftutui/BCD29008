-- MySQL dump 10.13  Distrib 8.0.16, for macos10.14 (x86_64)
--
-- Host: 127.0.0.1    Database: projeto1
-- ------------------------------------------------------
-- Server version	5.7.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Eleicao`
--

DROP TABLE IF EXISTS `Eleicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `Eleicao` (
  `idEleicao` int(11) NOT NULL AUTO_INCREMENT,
  `nomeEleicao` varchar(45) NOT NULL,
  `inicioEleicao` date DEFAULT NULL,
  `fimEleicao` date DEFAULT NULL,
  `estadoEleicao` tinyint(4) DEFAULT NULL,
  `apuracaoEleicao` tinyint(4) NOT NULL,
  PRIMARY KEY (`idEleicao`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Eleicao`
--

LOCK TABLES `Eleicao` WRITE;
/*!40000 ALTER TABLE `Eleicao` DISABLE KEYS */;
INSERT INTO `Eleicao` VALUES (56,'Eleição teste','2019-06-09',NULL,1,0);
/*!40000 ALTER TABLE `Eleicao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Eleitor`
--

DROP TABLE IF EXISTS `Eleitor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `Eleitor` (
  `idEleicao` int(11) NOT NULL,
  `loginPessoa` varchar(45) NOT NULL,
  `statusEleitor` varchar(45) NOT NULL,
  PRIMARY KEY (`idEleicao`,`loginPessoa`),
  KEY `fk_Eleicao_has_Pessoa_Pessoa1_idx` (`loginPessoa`),
  KEY `fk_Eleicao_has_Pessoa_Eleicao1_idx` (`idEleicao`),
  CONSTRAINT `fk_Eleicao_has_Pessoa_Eleicao1` FOREIGN KEY (`idEleicao`) REFERENCES `Eleicao` (`idEleicao`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Eleicao_has_Pessoa_Pessoa1` FOREIGN KEY (`loginPessoa`) REFERENCES `Pessoa` (`loginPessoa`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Eleitor`
--

LOCK TABLES `Eleitor` WRITE;
/*!40000 ALTER TABLE `Eleitor` DISABLE KEYS */;
INSERT INTO `Eleitor` VALUES (56,'ana','0'),(56,'angela','0'),(56,'camila','0'),(56,'clara','0'),(56,'dilma','0'),(56,'edna','0'),(56,'estela','0');
/*!40000 ALTER TABLE `Eleitor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Pergunta`
--

DROP TABLE IF EXISTS `Pergunta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `Pergunta` (
  `idPergunta` int(11) NOT NULL AUTO_INCREMENT,
  `pergunta` varchar(45) NOT NULL,
  `minRespostas` int(11) NOT NULL,
  `maxRespostas` int(11) NOT NULL,
  `idEleicao` int(11) NOT NULL,
  PRIMARY KEY (`idPergunta`,`idEleicao`),
  KEY `fk_Pergunta_Eleicao1_idx` (`idEleicao`),
  CONSTRAINT `fk_Pergunta_Eleicao1` FOREIGN KEY (`idEleicao`) REFERENCES `Eleicao` (`idEleicao`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Pergunta`
--

LOCK TABLES `Pergunta` WRITE;
/*!40000 ALTER TABLE `Pergunta` DISABLE KEYS */;
INSERT INTO `Pergunta` VALUES (32,'Questão teste 1',2,3,56),(33,'Questão teste 2',1,1,56);
/*!40000 ALTER TABLE `Pergunta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Pessoa`
--

DROP TABLE IF EXISTS `Pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `Pessoa` (
  `loginPessoa` varchar(45) NOT NULL,
  `nomePessoa` varchar(45) NOT NULL,
  `senhaPessoa` varchar(45) NOT NULL,
  PRIMARY KEY (`loginPessoa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Pessoa`
--

LOCK TABLES `Pessoa` WRITE;
/*!40000 ALTER TABLE `Pessoa` DISABLE KEYS */;
INSERT INTO `Pessoa` VALUES ('ana','Ana','senhaAna'),('angela','Angela','senhaAngela'),('camila','Camila','senhaCamila'),('clara','Clara','senhaClara'),('dilma','Dilma','senhaDilma'),('edna','Edna','senhaEdna'),('estela','Estela','senhaEstela'),('gabriel','Gabriel','senhaGabriel'),('jane','Jane','senhaJane'),('joana','Joana','senhaJoana'),('juca','Juca','senhaJuca'),('julia','Julia','senhaJulia'),('juliana','Juliana','senhaJuliana'),('liz','Liz','senhaLiz'),('luisa','Luisa','senhaLuisa'),('mara','Mara','senhaMara'),('maria','Maria','senhaMaria'),('marina','Marina','senhaMarina'),('paulo','Paulo','senhaPaulo'),('rebeca','Rebeca','senhaRebeca'),('sara','Sara','senhaSara'),('thiago','Thiago','senhaThiago'),('zeca','Zeca','senhaZeca');
/*!40000 ALTER TABLE `Pessoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Resposta`
--

DROP TABLE IF EXISTS `Resposta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `Resposta` (
  `idResposta` int(11) NOT NULL AUTO_INCREMENT,
  `resposta` varchar(45) NOT NULL,
  `votoResposta` varchar(45) NOT NULL,
  `idPergunta` int(11) NOT NULL,
  `idEleicao` int(11) NOT NULL,
  PRIMARY KEY (`idResposta`,`idPergunta`,`idEleicao`),
  KEY `fk_Resposta_Pergunta1_idx` (`idPergunta`,`idEleicao`),
  CONSTRAINT `fk_Resposta_Pergunta1` FOREIGN KEY (`idPergunta`, `idEleicao`) REFERENCES `Pergunta` (`idPergunta`, `idEleicao`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Resposta`
--

LOCK TABLES `Resposta` WRITE;
/*!40000 ALTER TABLE `Resposta` DISABLE KEYS */;
INSERT INTO `Resposta` VALUES (70,'Resposta q1 - 1','0',32,56),(71,'Resposta q1 - 2','0',32,56),(72,'Resposta q1 - 3','0',32,56),(73,'Resposta q1 - 4','0',32,56),(74,'Resposta q2 - 1','0',33,56),(75,'Resposta q2 - 2','0',33,56);
/*!40000 ALTER TABLE `Resposta` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-06-09 23:20:17
