-- MySQL dump 10.13  Distrib 5.1.61, for redhat-linux-gnu (i386)
--
-- Host: localhost    Database: nut
-- ------------------------------------------------------
-- Server version	5.1.61

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `user_name` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `sex` varchar(10) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `depart` varchar(20) DEFAULT NULL,
  `title` varchar(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `start_ip` varchar(20) DEFAULT NULL,
  `end_ip` varchar(20) DEFAULT NULL,
  `start_hour` int(11) DEFAULT NULL,
  `end_hour` int(11) DEFAULT NULL,
  `description` text,
  `remote_ip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'admin','123qwe!@#','男','0571-88888888','2010-07-04 13:52:36','2012-06-12 18:02:31','有效','信息中心','主任','超级管理员','**@hzih.net','192.168.1.1','192.168.200.254',9,18,'这是一个默认的超级用户信息',''),(2,'authadmin','123qwe!@#','男','0571-88888888','2012-04-12 14:22:35','2012-06-12 12:00:06','有效','信息中心','主任','授权管理员','**@hzih.net','192.168.1.1','192.168.200.254',1,22,'这是一个默认的授权用户信息',''),(3,'configadmin','123qwe!@#','男','0571-88888888','2012-06-12 18:04:01','2012-06-12 18:23:16','有效','信息中心','主任','配置管理员','**@hzih.net','192.168.1.1','192.168.200.254',9,21,'这是一个默认的配置用户信息',''),(4,'auditadmin','123qwe!@#','男','0571-88888888','2012-07-03 10:19:57','2012-07-03 10:19:58','有效','信息中心','主任','审计管理员','**@hzih.net','192.168.1.1','192.168.200.254',7,22,'这是一个默认的审计用户信息',NULL);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_role`
--

DROP TABLE IF EXISTS `account_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_role` (
  `account_id` bigint(20) NOT NULL DEFAULT '0',
  `role_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`account_id`,`role_id`),
  KEY `FK410D03481FCE46BD` (`role_id`),
  KEY `FK410D034811351AF7` (`account_id`),
  KEY `FK410D0348D5FE8033` (`role_id`),
  KEY `FK410D03488723E5C1` (`account_id`),
  CONSTRAINT `FK410D03488723E5C1` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FK410D034811351AF7` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FK410D03481FCE46BD` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK410D0348D5FE8033` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_account_role_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FK_account_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='账户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_role`
--

LOCK TABLES `account_role` WRITE;
/*!40000 ALTER TABLE `account_role` DISABLE KEYS */;
INSERT INTO `account_role` VALUES (1,1),(2,2),(3,3),(4,4);
/*!40000 ALTER TABLE `account_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_log`
--

DROP TABLE IF EXISTS `business_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `level` varchar(10) DEFAULT NULL COMMENT '日志等级',
  `log_time` datetime DEFAULT NULL COMMENT '产生时间',
  `business_name` varchar(60) DEFAULT NULL COMMENT '业务名称',
  `platform_name` varchar(255) DEFAULT NULL COMMENT '平台名称',
  `audit_info` text,
  `source_ip` varchar(255) DEFAULT NULL,
  `source_dest` varchar(255) DEFAULT NULL,
  `dest_ip` varchar(255) DEFAULT NULL,
  `dest_port` varchar(10) DEFAULT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `operation` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `ind_business_audit` (`level`,`business_name`,`log_time`,`platform_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务日志审计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_log`
--

LOCK TABLES `business_log` WRITE;
/*!40000 ALTER TABLE `business_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equipment` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `equipment_name` varchar(255) COLLATE utf8_bin NOT NULL,
  `link_name` varchar(255) COLLATE utf8_bin NOT NULL,
  `link_type` varchar(3) COLLATE utf8_bin NOT NULL DEFAULT 'int',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment`
--

LOCK TABLES `equipment` WRITE;
/*!40000 ALTER TABLE `equipment` DISABLE KEYS */;
INSERT INTO `equipment` VALUES (1,'pubweb','int_link_1','int');
/*!40000 ALTER TABLE `equipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment_log`
--

DROP TABLE IF EXISTS `equipment_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equipment_log` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log_time` datetime DEFAULT NULL,
  `level` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `link_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `equipment_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `log_info` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment_log`
--

LOCK TABLES `equipment_log` WRITE;
/*!40000 ALTER TABLE `equipment_log` DISABLE KEYS */;
INSERT INTO `equipment_log` VALUES (2,'2012-07-05 16:22:50','INFO','link_1','pubweb','用户admin新增一台设备pubweb成功');
/*!40000 ALTER TABLE `equipment_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ext_link`
--

DROP TABLE IF EXISTS `ext_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ext_link` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `link_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `link_property` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `link_type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `link_Corp` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `link_security` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `link_amount` bigint(20) DEFAULT NULL,
  `link_bandwidth` bigint(20) DEFAULT NULL,
  `other_security` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK93220B5824A3350C` (`link_property`),
  KEY `FK93220B586E5C98D6` (`link_type`),
  CONSTRAINT `FK93220B5824A3350C` FOREIGN KEY (`link_property`) REFERENCES `ext_link_property` (`code`),
  CONSTRAINT `FK93220B586E5C98D6` FOREIGN KEY (`link_type`) REFERENCES `ext_link_type` (`link_type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='外部链路表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ext_link`
--

LOCK TABLES `ext_link` WRITE;
/*!40000 ALTER TABLE `ext_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `ext_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `int_link`
--

DROP TABLE IF EXISTS `int_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `int_link` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `link_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `jrdx` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `exchange_mode` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `link_bandwidth` int(11) DEFAULT NULL,
  `FW_used` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sec_gateway_used` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `gap_used` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `VPN_used` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `other_security` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='内部链路表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `int_link`
--

LOCK TABLES `int_link` WRITE;
/*!40000 ALTER TABLE `int_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `int_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `ID` bigint(20) NOT NULL DEFAULT '0',
  `CODE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `PARENT_ID` int(11) DEFAULT NULL,
  `SEQ` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (100,'TOP_QXGL','权限管理',NULL,0,0),(101,'SECOND_YHGL','用户管理',NULL,100,1),(102,'SECOND_JSGL','角色管理',NULL,100,2),(103,'SECOND_AQCL','安全策略',NULL,100,3),(110,'TOP_WLGL','网络管理',NULL,0,0),(111,'SECOND_JKGL','接口管理',NULL,110,1),(112,'SECOND_LTCS','连通测试',NULL,110,2),(113,'SECOND_DKCS','端口测试',NULL,110,3),(114,'SECOND_LYGL','路由管理',NULL,110,4),(120,'TOP_XTGL','系统管理',NULL,0,0),(121,'SECOND_PTSM','平台说明',NULL,120,1),(122,'SECOND_PTGL','平台管理',NULL,120,2),(123,'SECOND_PZGL','安全配置',NULL,120,3),(124,'SECOND_ZSGL','证书管理',NULL,120,4),(125,'SECOND_RZXZ','日志下载',NULL,120,5),(126,'SECOND_BBSJ','版本升级',NULL,120,6),(140,'TOP_PZGL','配置管理',NULL,0,0),(141,'SECOND_SJKGL','审计库管理',NULL,140,1),(142,'SECOND_SJBFCL','审计备份策略',NULL,140,2),(143,'SECOND_SBGL','设备管理',NULL,140,3),(160,'TOP_SJGL','审计管理',NULL,0,0),(161,'SECOND_YHRZ','用户日志',NULL,160,1),(162,'SECOND_SBRZ','设备日志',NULL,160,2),(170,'TOP_BJGL','报警管理',NULL,0,0),(171,'SECOND_BJPZ','报警配置',NULL,170,1),(172,'SECOND_YWYCBJ','业务异常报警',NULL,170,2),(173,'SECOND_AQSJBJ','安全事件报警',NULL,170,3),(174,'SECOND_SBGZBJ','设备故障报警',NULL,170,4),(180,'TOP_YXJK','运行监控',NULL,0,0),(181,'SECOND_YWYXJK','业务运行监控',NULL,180,1),(182,'SECOND_SBYXJK','设备运行监控',NULL,180,2),(190,'TOP_SJYGL','数据源管理',NULL,0,0),(191,'SECOND_YDSJU','源端数据源',NULL,190,1),(192,'SECOND_MBSJY','目标数据源',NULL,190,2),(200,'TOP_YYGL','应用管理',NULL,0,0),(201,'SECOND_GLGL','过滤管理',NULL,200,1),(202,'SECOND_WJTB','文件同步',NULL,200,2),(203,'SECOND_DBTB','数据库同步',NULL,200,3),(204,'SECOND_TYDL','通用代理',NULL,200,4),(205,'SECOND_DKYS','端口映射',NULL,200,5);
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'超级管理员','超级管理员','2010-07-04 15:07:08','2012-07-13 16:02:00'),(2,'授权管理员','授权管理员','2012-07-03 10:06:20','2012-07-03 10:06:20'),(3,'配置管理员','配置管理员','2012-03-14 12:33:05','2012-03-14 12:33:05'),(4,'审计管理员','审计管理员','2012-06-12 18:37:24','2012-07-03 13:21:44');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission` (
  `permission_id` bigint(20) NOT NULL DEFAULT '0',
  `role_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`permission_id`,`role_id`),
  KEY `FKBD40D53851BABF58` (`role_id`),
  KEY `FKBD40D53852A81638` (`permission_id`),
  KEY `FK9C6EC93851BABF58` (`role_id`),
  KEY `FK9C6EC93852A81638` (`permission_id`),
  KEY `FKBD40D53880878851` (`role_id`),
  KEY `FKBD40D5388AAE8071` (`permission_id`),
  KEY `FKBD40D5381FCE46BD` (`role_id`),
  KEY `FKBD40D5384E8FBDDD` (`permission_id`),
  KEY `FKBD40D538D5FE8033` (`role_id`),
  KEY `FKBD40D538461086D3` (`permission_id`),
  CONSTRAINT `FKBD40D538461086D3` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`ID`),
  CONSTRAINT `FK9C6EC93851BABF58` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK9C6EC93852A81638` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`ID`),
  CONSTRAINT `FKBD40D5381FCE46BD` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FKBD40D5384E8FBDDD` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`ID`),
  CONSTRAINT `FKBD40D53851BABF58` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FKBD40D53852A81638` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`ID`),
  CONSTRAINT `FKBD40D53880878851` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FKBD40D5388AAE8071` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`ID`),
  CONSTRAINT `FKBD40D538D5FE8033` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` VALUES (100,1),(101,1),(102,1),(103,1),(110,1),(111,1),(112,1),(113,1),(114,1),(120,1),(122,1),(123,1),(124,1),(125,1),(126,1),(140,1),(141,1),(142,1),(160,1),(161,1),(162,1),(170,1),(171,1),(172,1),(173,1),(174,1),(180,1),(181,1),(182,1),(190,1),(191,1),(192,1),(200,1),(201,1),(202,1),(203,1),(204,1),(205,1),(100,4),(101,4),(102,4),(103,4),(110,4),(111,4),(112,4),(113,4),(114,4),(120,4),(122,4),(123,4),(124,4),(125,4),(140,4),(141,4),(160,4),(161,4);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `safe_policy`
--

DROP TABLE IF EXISTS `safe_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `safe_policy` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `timeout` int(11) DEFAULT NULL,
  `passwordLength` int(11) DEFAULT NULL,
  `errorLimit` int(11) DEFAULT NULL,
  `remoteDisabled` tinyint(1) DEFAULT NULL,
  `passwordRules` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='安全策略表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `safe_policy`
--

LOCK TABLES `safe_policy` WRITE;
/*!40000 ALTER TABLE `safe_policy` DISABLE KEYS */;
INSERT INTO `safe_policy` VALUES (1,300,0,3,1,'/^[0-9a-zA-Z!$#%@^&amp;amp;amp;amp;amp;amp;amp;*()~_+]{8,20}$/');
/*!40000 ALTER TABLE `safe_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log`
--

DROP TABLE IF EXISTS `sys_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `log_time` datetime DEFAULT NULL COMMENT '产生时间',
  `level` varchar(10) DEFAULT NULL COMMENT '日志等级',
  `audit_module` varchar(40) DEFAULT NULL COMMENT '审计模块',
  `audit_action` varchar(40) DEFAULT NULL COMMENT '审计行为',
  `audit_info` varchar(255) DEFAULT NULL COMMENT '审计内容',
  PRIMARY KEY (`Id`),
  KEY `log_time` (`log_time`,`level`,`audit_module`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='系统日志审计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log`
--

LOCK TABLES `sys_log` WRITE;
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
INSERT INTO `sys_log` VALUES (1,'2012-03-14 13:26:55','info','yonghuguanli','aaa','sssss');
/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_oper_log`
--

DROP TABLE IF EXISTS `user_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_oper_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `log_time` datetime DEFAULT NULL COMMENT '审计时间',
  `level` varchar(10) DEFAULT NULL COMMENT '日志级别',
  `username` varchar(30) DEFAULT NULL COMMENT '用户名',
  `audit_module` varchar(255) DEFAULT NULL COMMENT '审计模块',
  `audit_info` varchar(255) DEFAULT NULL COMMENT '审计内容',
  PRIMARY KEY (`Id`),
  KEY `log_time` (`log_time`,`level`,`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1480 DEFAULT CHARSET=utf8 COMMENT='用户操作审计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_oper_log`
--

LOCK TABLES `user_oper_log` WRITE;
/*!40000 ALTER TABLE `user_oper_log` DISABLE KEYS */;
INSERT INTO `user_oper_log` VALUES (1,'2012-06-08 11:11:12','INFO','admin',' 用户登录 ',' 用户登录成功 '),(2,'2012-06-08 11:12:12','INFO','admin','用户登录','用户退出成功'),(786,'2012-06-21 09:37:09','INFO','admin','用户登录','用户登录成功'),(787,'2012-06-21 09:37:14','INFO','admin','用户管理','用户获取所有角色名成功'),(788,'2012-06-21 09:37:14','INFO','admin','用户管理','用户获取所有账号信息成功'),(789,'2012-06-21 09:37:15','INFO','admin','角色管理','用户获取角色信息成功'),(790,'2012-06-21 09:37:16','INFO','admin','安全策略','用户获取安全策略信息成功'),(791,'2012-06-21 16:28:58','INFO','admin','用户登录','用户登录成功'),(792,'2012-06-21 16:29:00','INFO','admin','用户管理','用户获取所有角色名成功'),(793,'2012-06-21 16:29:01','INFO','admin','用户管理','用户获取所有账号信息成功'),(794,'2012-06-21 16:29:07','INFO','admin','用户管理','用户获取所有角色名成功'),(795,'2012-06-21 16:29:22','INFO','admin','用户管理','用户获取所有角色名成功'),(796,'2012-06-21 16:31:19','INFO','admin','用户登录','用户登录成功'),(797,'2012-06-21 16:31:22','INFO','admin','用户管理','用户获取所有角色名成功'),(798,'2012-06-21 16:31:22','INFO','admin','用户管理','用户获取所有账号信息成功'),(799,'2012-06-21 16:31:23','INFO','admin','角色管理','用户获取角色信息成功'),(800,'2012-06-21 16:31:24','INFO','admin','安全策略','用户获取安全策略信息成功'),(801,'2012-06-25 15:15:56','INFO','admin','用户登录','用户登录成功'),(802,'2012-06-25 15:16:02','INFO','admin','安全策略','用户获取安全策略信息成功'),(803,'2012-06-25 15:16:03','INFO','admin','角色管理','用户获取角色信息成功'),(804,'2012-06-25 15:16:18','INFO','admin','角色管理','用户获取角色权限信息用于新增成功'),(805,'2012-06-25 15:16:57','INFO','admin','用户管理','用户获取所有角色名成功'),(806,'2012-06-25 15:16:57','INFO','admin','用户管理','用户获取所有账号信息成功'),(807,'2012-06-25 15:17:12','ERROR','admin','接口管理','用户读取接口信息失败 '),(808,'2012-06-25 15:17:16','INFO','admin','路由管理','用户读取路由信息成功 '),(809,'2012-06-25 15:17:26','INFO','admin','平台管理','用户重启系统成功 '),(810,'2012-06-25 17:56:29','INFO','admin','用户登录','用户登录成功'),(811,'2012-06-25 17:56:34','INFO','admin','安全策略','用户获取安全策略信息成功'),(812,'2012-06-25 17:56:35','INFO','admin','角色管理','用户获取角色信息成功'),(813,'2012-06-25 17:56:35','INFO','admin','用户管理','用户获取所有角色名成功'),(814,'2012-06-25 17:56:35','INFO','admin','用户管理','用户获取所有账号信息成功'),(815,'2012-06-25 18:03:47','INFO','admin','角色管理','用户获取角色信息成功'),(816,'2012-06-25 18:03:48','INFO','admin','安全策略','用户获取安全策略信息成功'),(817,'2012-06-25 18:06:17','INFO','admin','路由管理','用户读取路由信息成功 '),(818,'2012-06-25 18:06:19','ERROR','admin','路由管理','用户读取所有接口名失败 '),(819,'2012-06-25 18:06:46','INFO','admin','连通测试','用户ping成功 '),(820,'2012-06-25 18:07:01','INFO','admin','连通测试','用户ping成功 '),(821,'2012-06-25 18:07:18','INFO','admin','连通测试','用户ping成功 '),(822,'2012-06-25 18:07:36','INFO','admin','端口测试','用户telnet成功 '),(823,'2012-06-25 18:07:44','INFO','admin','端口测试','用户telnet成功 '),(824,'2012-06-25 18:07:58','INFO','admin','端口测试','用户telnet成功 '),(825,'2012-06-25 18:08:25','INFO','admin','配置管理','用户获取管理服务、集控采集数据接口设定IP地址成功 '),(826,'2012-06-25 18:08:25','INFO','admin','配置管理','用户获取管理客户机地址成功 '),(827,'2012-06-25 18:09:02','INFO','admin','配置管理','用户获取管理客户机地址成功 '),(828,'2012-06-26 10:24:07','INFO','admin','用户登录','用户登录成功'),(829,'2012-06-26 10:25:58','INFO','admin','用户登录','用户登录成功'),(830,'2012-06-26 10:26:01','INFO','admin','用户管理','用户获取所有角色名成功'),(831,'2012-06-26 10:26:02','INFO','admin','用户管理','用户获取所有账号信息成功'),(832,'2012-06-26 10:26:03','INFO','admin','角色管理','用户获取角色信息成功'),(833,'2012-06-26 10:26:04','INFO','admin','安全策略','用户获取安全策略信息成功'),(834,'2012-06-26 16:43:09','INFO','admin','用户登录','用户登录成功'),(835,'2012-06-26 16:43:22','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(836,'2012-06-26 16:43:43','INFO','admin','用户登录','用户退出成功'),(837,'2012-06-27 10:55:46','INFO','admin','用户登录','用户登录成功'),(838,'2012-06-27 11:06:31','INFO','admin','用户登录','用户登录成功'),(839,'2012-06-27 17:20:05','INFO','admin','用户登录','用户登录成功'),(840,'2012-06-27 17:29:21','INFO','admin','用户登录','用户登录成功'),(841,'2012-06-27 17:51:00','INFO','admin','用户登录','用户登录成功'),(842,'2012-06-27 17:51:15','INFO','admin','配置管理','用户获取管理客户机地址成功 '),(843,'2012-06-27 17:51:15','INFO','admin','配置管理','用户获取管理服务、集控采集数据接口设定IP地址成功 '),(844,'2012-06-27 17:51:24','INFO','admin','用户登录','用户退出成功'),(845,'2012-06-28 09:47:38','INFO','admin','用户登录','用户登录成功'),(846,'2012-06-28 09:48:49','INFO','admin','用户登录','用户登录成功'),(847,'2012-06-28 09:51:52','INFO','admin','用户登录','用户登录成功'),(848,'2012-06-29 09:31:17','INFO','admin','用户登录','用户登录成功'),(849,'2012-06-29 09:33:35','INFO','admin','用户登录','用户登录成功'),(850,'2012-06-29 09:35:00','INFO','admin','用户登录','用户登录成功'),(851,'2012-06-29 09:35:04','INFO','admin','用户管理','用户获取所有角色名成功'),(852,'2012-06-29 09:35:04','INFO','admin','角色管理','用户获取角色信息成功'),(853,'2012-06-29 09:35:04','INFO','admin','用户管理','用户获取所有账号信息成功'),(854,'2012-06-29 09:35:05','INFO','admin','安全策略','用户获取安全策略信息成功'),(855,'2012-06-29 09:57:02','INFO','admin','用户登录','用户登录成功'),(856,'2012-06-29 09:58:02','INFO','admin','用户管理','用户获取所有角色名成功'),(857,'2012-06-29 09:58:02','INFO','admin','用户管理','用户获取所有账号信息成功'),(858,'2012-06-29 09:58:05','INFO','admin','角色管理','用户获取角色信息成功'),(859,'2012-06-29 09:58:06','INFO','admin','安全策略','用户获取安全策略信息成功'),(860,'2012-07-01 13:34:52','INFO','admin','用户登录','用户登录成功'),(861,'2012-07-01 13:35:00','INFO','admin','用户管理','用户获取所有角色名成功'),(862,'2012-07-01 13:35:00','INFO','admin','用户管理','用户获取所有账号信息成功'),(863,'2012-07-01 13:35:41','INFO','admin','用户登录','用户登录成功'),(864,'2012-07-01 13:35:45','INFO','admin','用户管理','用户获取所有角色名成功'),(865,'2012-07-01 13:35:46','INFO','admin','用户管理','用户获取所有账号信息成功'),(866,'2012-07-01 13:35:46','INFO','admin','角色管理','用户获取角色信息成功'),(867,'2012-07-01 13:35:47','INFO','admin','安全策略','用户获取安全策略信息成功'),(868,'2012-07-01 13:36:11','INFO','admin','用户登录','用户登录成功'),(869,'2012-07-01 13:36:20','INFO','admin','用户管理','用户获取所有角色名成功'),(870,'2012-07-01 13:36:20','INFO','admin','用户管理','用户获取所有账号信息成功'),(871,'2012-07-01 13:36:46','INFO','admin','用户管理','用户获取所有账号信息成功'),(872,'2012-07-01 13:37:44','INFO','admin','用户登录','用户登录成功'),(873,'2012-07-01 13:37:50','INFO','admin','用户管理','用户获取所有角色名成功'),(874,'2012-07-01 13:37:50','INFO','admin','用户管理','用户获取所有账号信息成功'),(875,'2012-07-01 13:45:11','INFO','admin','用户登录','用户登录成功'),(876,'2012-07-01 13:45:15','INFO','admin','用户管理','用户获取所有角色名成功'),(877,'2012-07-01 13:45:15','INFO','admin','用户管理','用户获取所有账号信息成功'),(878,'2012-07-01 13:45:26','INFO','admin','用户管理','用户获取所有角色名成功'),(879,'2012-07-01 13:46:18','INFO','admin','用户管理','用户获取所有角色名成功'),(880,'2012-07-01 13:46:25','INFO','admin','角色管理','用户获取角色信息成功'),(881,'2012-07-01 13:46:27','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(882,'2012-07-01 13:46:35','INFO','admin','角色管理','用户删除角色信息成功'),(883,'2012-07-01 13:46:36','INFO','admin','角色管理','用户获取角色信息成功'),(884,'2012-07-01 13:46:41','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(885,'2012-07-01 13:46:49','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(886,'2012-07-01 13:47:03','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(887,'2012-07-01 13:49:09','INFO','admin','用户登录','用户退出成功'),(888,'2012-07-01 14:35:05','INFO','admin','用户登录','用户登录成功'),(889,'2012-07-01 14:36:44','INFO','admin','角色管理','用户获取角色信息成功'),(890,'2012-07-01 14:36:47','INFO','admin','角色管理','用户获取角色权限信息用于新增成功'),(891,'2012-07-02 10:14:15','INFO','test',' 用户登录 ',' 用户登录成功 '),(892,'2012-07-02 10:14:23','INFO','test','用户登录','用户退出成功'),(893,'2012-07-02 17:17:53','INFO','admin','用户登录','用户登录成功'),(894,'2012-07-02 17:18:05','INFO','admin','角色管理','用户获取角色信息成功'),(895,'2012-07-02 17:18:10','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(896,'2012-07-02 17:20:59','INFO','admin','用户登录','用户登录成功'),(897,'2012-07-02 17:21:03','INFO','admin','安全策略','用户获取安全策略信息成功'),(898,'2012-07-02 17:21:37','INFO','admin','用户管理','用户获取所有角色名成功'),(899,'2012-07-02 17:21:37','INFO','admin','用户管理','用户获取所有账号信息成功'),(900,'2012-07-02 17:21:40','INFO','admin','角色管理','用户获取角色信息成功'),(901,'2012-07-02 17:21:47','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(902,'2012-07-02 17:21:54','INFO','admin','角色管理','用户更新角色信息成功'),(903,'2012-07-02 17:21:56','INFO','admin','角色管理','用户获取角色信息成功'),(904,'2012-07-02 17:23:51','INFO','admin','用户登录','用户退出成功'),(905,'2012-07-02 17:24:02','INFO','admin','用户登录','用户登录成功'),(906,'2012-07-02 17:26:24','INFO','admin','用户登录','用户登录成功'),(907,'2012-07-02 17:29:29','INFO','admin','用户登录','用户登录成功'),(908,'2012-07-02 17:29:36','INFO','admin','审计库管理','用户查找审计库信息成功'),(909,'2012-07-02 17:32:15','INFO','admin','用户登录','用户登录成功'),(910,'2012-07-02 17:32:23','INFO','admin','审计库管理','用户查找审计库信息成功'),(911,'2012-07-02 17:41:59','INFO','admin','用户登录','用户登录成功'),(912,'2012-07-02 17:42:05','INFO','admin','审计库管理','用户查找审计库信息成功'),(913,'2012-07-02 17:43:41','INFO','admin','用户登录','用户登录成功'),(914,'2012-07-02 17:43:46','INFO','admin','审计库管理','用户查找审计库信息成功'),(915,'2012-07-02 17:43:52','INFO','admin','用户登录','用户退出成功'),(916,'2012-07-03 10:26:48','INFO','admin','用户登录','用户登录成功'),(917,'2012-07-03 10:26:51','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(918,'2012-07-03 10:27:01','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(919,'2012-07-03 10:27:11','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(920,'2012-07-03 10:27:21','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(921,'2012-07-03 10:27:31','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(922,'2012-07-03 10:27:41','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(923,'2012-07-03 10:27:51','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(924,'2012-07-03 10:28:01','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(925,'2012-07-03 10:28:11','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(926,'2012-07-03 10:28:21','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(927,'2012-07-03 10:28:31','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(928,'2012-07-03 10:28:41','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(929,'2012-07-03 10:28:51','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(930,'2012-07-03 10:29:01','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(931,'2012-07-03 10:29:11','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(932,'2012-07-03 10:29:21','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(933,'2012-07-03 10:29:31','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(934,'2012-07-03 10:29:34','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(935,'2012-07-03 10:29:36','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(936,'2012-07-03 10:29:46','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(937,'2012-07-03 10:31:27','INFO','admin','用户登录','用户登录成功'),(938,'2012-07-03 10:31:28','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(939,'2012-07-03 10:31:31','INFO','admin','角色管理','用户获取角色信息成功'),(940,'2012-07-03 10:31:35','INFO','admin','角色管理','用户获取角色信息成功'),(941,'2012-07-03 10:31:36','INFO','admin','角色管理','用户获取角色信息成功'),(942,'2012-07-03 10:31:37','INFO','admin','角色管理','用户获取角色信息成功'),(943,'2012-07-03 10:31:38','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(944,'2012-07-03 10:31:48','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(945,'2012-07-03 10:31:58','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(946,'2012-07-03 10:32:08','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(947,'2012-07-03 10:32:18','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(948,'2012-07-03 10:32:27','INFO','admin','角色管理','用户获取角色信息成功'),(949,'2012-07-03 10:32:30','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(950,'2012-07-03 10:32:40','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(951,'2012-07-03 10:32:50','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(952,'2012-07-03 10:33:00','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(953,'2012-07-03 10:33:07','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(954,'2012-07-03 10:33:17','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(955,'2012-07-03 10:33:27','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(956,'2012-07-03 10:33:30','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(957,'2012-07-03 10:33:40','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(958,'2012-07-03 10:33:47','INFO','admin','角色管理','用户获取角色信息成功'),(959,'2012-07-03 10:33:50','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(960,'2012-07-03 10:33:58','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(961,'2012-07-03 10:34:00','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(962,'2012-07-03 10:34:09','INFO','admin','角色管理','用户更新角色信息成功'),(963,'2012-07-03 10:34:10','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(964,'2012-07-03 10:34:10','INFO','admin','角色管理','用户获取角色信息成功'),(965,'2012-07-03 10:34:18','INFO','admin','用户登录','用户退出成功'),(966,'2012-07-03 10:34:30','INFO','admin','用户登录','用户登录成功'),(967,'2012-07-03 10:34:31','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(968,'2012-07-03 10:34:36','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(969,'2012-07-03 10:34:39','INFO','admin','审计库管理','用户查找审计库信息成功'),(970,'2012-07-03 10:34:41','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(971,'2012-07-03 10:34:51','INFO','admin','审计库管理','用户更新审计库信息成功'),(972,'2012-07-03 10:34:51','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(973,'2012-07-03 10:35:01','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(974,'2012-07-03 10:35:11','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(975,'2012-07-03 10:35:12','INFO','admin','角色管理','用户获取角色信息成功'),(976,'2012-07-03 10:35:21','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(977,'2012-07-03 10:35:27','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(978,'2012-07-03 10:35:31','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(979,'2012-07-03 10:35:37','INFO','admin','角色管理','用户更新角色信息成功'),(980,'2012-07-03 10:35:38','INFO','admin','角色管理','用户获取角色信息成功'),(981,'2012-07-03 10:35:41','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(982,'2012-07-03 10:35:44','INFO','admin','用户登录','用户退出成功'),(983,'2012-07-03 10:36:02','INFO','auditadmin','用户登录','用户登录成功'),(984,'2012-07-03 10:36:05','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(985,'2012-07-03 10:36:13','INFO','auditadmin','审计库管理','用户查找审计库信息成功'),(986,'2012-07-03 10:36:15','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(987,'2012-07-03 10:36:24','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(988,'2012-07-03 10:36:35','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(989,'2012-07-03 10:36:44','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(990,'2012-07-03 10:36:55','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(991,'2012-07-03 10:37:05','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(992,'2012-07-03 10:37:14','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(993,'2012-07-03 10:37:39','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(994,'2012-07-03 10:37:58','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(995,'2012-07-03 10:38:17','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(996,'2012-07-03 10:38:21','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(997,'2012-07-03 10:38:21','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(998,'2012-07-03 10:38:21','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(999,'2012-07-03 10:38:50','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1000,'2012-07-03 10:38:51','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1001,'2012-07-03 10:38:51','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1002,'2012-07-03 10:38:55','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1003,'2012-07-03 10:39:05','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1004,'2012-07-03 10:39:15','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1005,'2012-07-03 10:39:25','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1006,'2012-07-03 10:39:35','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1007,'2012-07-03 10:39:45','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1008,'2012-07-03 10:39:55','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1009,'2012-07-03 10:40:05','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1010,'2012-07-03 10:40:15','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1011,'2012-07-03 10:40:25','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1012,'2012-07-03 10:40:35','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1013,'2012-07-03 10:40:45','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1014,'2012-07-03 10:40:55','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1015,'2012-07-03 10:41:05','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1016,'2012-07-03 10:41:15','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1017,'2012-07-03 10:41:25','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1018,'2012-07-03 10:41:35','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1019,'2012-07-03 10:41:45','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1020,'2012-07-03 10:41:55','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1021,'2012-07-03 10:42:05','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1022,'2012-07-03 10:42:15','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1023,'2012-07-03 10:42:25','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1024,'2012-07-03 10:42:35','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1025,'2012-07-03 10:42:45','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1026,'2012-07-03 10:42:55','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1027,'2012-07-03 10:43:05','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1028,'2012-07-03 10:43:15','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1029,'2012-07-03 10:43:25','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1030,'2012-07-03 10:43:35','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1031,'2012-07-03 10:43:45','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1032,'2012-07-03 10:43:55','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1033,'2012-07-03 10:44:05','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1034,'2012-07-03 10:44:15','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1035,'2012-07-03 10:44:25','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1036,'2012-07-03 10:44:35','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1037,'2012-07-03 10:44:45','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1038,'2012-07-03 10:44:55','ERROR','auditadmin','用户日志审计','用户读取用户日志审计信息失败 '),(1039,'2012-07-03 13:17:25','INFO','admin','用户登录','用户登录成功'),(1040,'2012-07-03 13:17:38','INFO','admin','配置管理','用户获取管理客户机地址成功 '),(1041,'2012-07-03 13:17:38','INFO','admin','配置管理','用户获取管理服务、集控采集数据接口设定IP地址成功 '),(1042,'2012-07-03 13:17:53','INFO','admin','角色管理','用户获取角色信息成功'),(1043,'2012-07-03 13:17:54','INFO','admin','角色管理','用户获取角色权限信息用于新增成功'),(1044,'2012-07-03 13:20:42','INFO','admin','用户登录','用户登录成功'),(1045,'2012-07-03 13:20:51','INFO','admin','角色管理','用户获取角色信息成功'),(1046,'2012-07-03 13:20:56','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(1047,'2012-07-03 13:21:11','INFO','admin','角色管理','用户更新角色信息成功'),(1048,'2012-07-03 13:21:13','INFO','admin','角色管理','用户获取角色信息成功'),(1049,'2012-07-03 13:21:25','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(1050,'2012-07-03 13:21:34','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(1051,'2012-07-03 13:21:46','INFO','admin','角色管理','用户更新角色信息成功'),(1052,'2012-07-03 13:21:47','INFO','admin','角色管理','用户获取角色信息成功'),(1053,'2012-07-03 13:21:51','INFO','admin','用户登录','用户退出成功'),(1054,'2012-07-03 13:22:03','INFO','auditadmin','用户登录','用户登录成功'),(1055,'2012-07-03 13:23:02','INFO','auditadmin','审计库管理','用户查找审计库信息成功'),(1056,'2012-07-03 13:23:20','INFO','auditadmin','审计库管理','用户更新审计库信息成功'),(1057,'2012-07-03 13:23:24','INFO','auditadmin','审计库管理','用户查找审计库信息成功'),(1058,'2012-07-03 13:25:00','INFO','auditadmin','用户登录','用户退出成功'),(1059,'2012-07-03 13:25:15','INFO','admin','用户登录','用户登录成功'),(1060,'2012-07-03 13:25:57','INFO','admin','安全策略','用户获取安全策略信息成功'),(1061,'2012-07-03 13:26:18','INFO','admin','安全策略','用户修改安全策略信息成功'),(1062,'2012-07-03 13:30:08','INFO','admin','用户登录','用户退出成功'),(1063,'2012-07-03 13:30:18','INFO','admin','用户登录','用户登录成功'),(1064,'2012-07-03 13:30:23','INFO','admin','安全策略','用户获取安全策略信息成功'),(1065,'2012-07-03 13:41:37','INFO','admin','安全策略','用户获取安全策略信息成功'),(1066,'2012-07-03 13:44:51','INFO','admin','用户登录','用户登录成功'),(1067,'2012-07-03 13:48:07','INFO','admin','用户登录','用户登录成功'),(1068,'2012-07-03 13:49:57','INFO','admin','用户登录','用户登录成功'),(1069,'2012-07-03 13:50:17','INFO','admin','安全策略','用户获取安全策略信息成功'),(1070,'2012-07-03 13:59:37','INFO','admin','用户登录','用户登录成功'),(1071,'2012-07-03 14:06:48','INFO','admin','用户登录','用户登录成功'),(1072,'2012-07-03 15:05:04','INFO','admin','用户登录','用户登录成功'),(1073,'2012-07-03 15:05:09','INFO','admin','角色管理','用户获取角色信息成功'),(1074,'2012-07-03 15:05:14','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(1075,'2012-07-03 15:05:26','INFO','admin','角色管理','用户更新角色信息成功'),(1076,'2012-07-03 15:05:28','INFO','admin','角色管理','用户获取角色信息成功'),(1077,'2012-07-03 15:23:42','INFO','admin','用户登录','用户登录成功'),(1078,'2012-07-03 15:23:50','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1079,'2012-07-03 15:24:36','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1080,'2012-07-03 15:24:44','INFO','admin','审计备份策略','用户显示审计备份策略成功'),(1081,'2012-07-03 15:25:05','INFO','admin','审计备份策略','用户显示审计备份策略成功'),(1082,'2012-07-03 15:25:12','INFO','admin','审计库管理','用户查找审计库信息成功'),(1083,'2012-07-03 15:25:55','INFO','admin','审计库管理','用户查找审计库信息成功'),(1084,'2012-07-03 15:26:08','INFO','admin','审计备份策略','用户显示审计备份策略成功'),(1085,'2012-07-03 15:30:30','INFO','admin','用户登录','用户登录成功'),(1086,'2012-07-03 15:30:42','ERROR','admin','版本升级','用户获取服务软件包名列表不成功 '),(1087,'2012-07-03 15:30:42','ERROR','admin','版本升级','用户获取自有Jar软件包名列表不成功 '),(1088,'2012-07-03 15:30:46','ERROR','admin','版本升级','用户获取服务软件包名列表不成功 '),(1089,'2012-07-03 15:31:39','INFO','admin','版本升级','用户获取服务软件包名列表成功 '),(1090,'2012-07-03 15:31:48','INFO','admin','版本升级','用户获取服务软件包名列表成功 '),(1091,'2012-07-03 15:31:48','INFO','admin','版本升级','用户获取自有Jar软件包列表名成功 '),(1092,'2012-07-03 15:33:08','INFO','admin','版本升级','用户获取服务软件包名列表成功 '),(1093,'2012-07-03 15:33:08','INFO','admin','版本升级','用户获取自有Jar软件包列表名成功 '),(1094,'2012-07-03 15:34:20','INFO','admin','版本升级','用户升级失败,没有需要升级的服务'),(1095,'2012-07-03 15:56:55','INFO','admin','用户登录','用户登录成功'),(1096,'2012-07-03 15:57:03','INFO','admin','角色管理','用户获取角色信息成功'),(1097,'2012-07-03 15:57:08','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(1098,'2012-07-03 15:57:17','INFO','admin','角色管理','用户更新角色信息成功'),(1099,'2012-07-03 15:57:19','INFO','admin','角色管理','用户获取角色信息成功'),(1100,'2012-07-03 15:57:23','INFO','admin','用户登录','用户退出成功'),(1101,'2012-07-03 15:57:32','INFO','admin','用户登录','用户登录成功'),(1102,'2012-07-03 15:57:37','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1103,'2012-07-03 15:58:42','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1104,'2012-07-03 16:01:58','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1105,'2012-07-03 16:02:00','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1106,'2012-07-03 16:02:19','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1107,'2012-07-03 16:02:22','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1108,'2012-07-03 16:02:23','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1109,'2012-07-03 16:02:23','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1110,'2012-07-03 16:02:24','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1111,'2012-07-03 16:02:25','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1112,'2012-07-03 16:44:39','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1113,'2012-07-03 16:44:42','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1114,'2012-07-03 16:45:12','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1115,'2012-07-03 16:57:29','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1116,'2012-07-03 16:57:52','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1117,'2012-07-05 11:33:34','INFO','admin','用户登录','用户登录成功'),(1118,'2012-07-05 11:33:47','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1119,'2012-07-05 11:33:48','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1120,'2012-07-05 11:34:23','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1121,'2012-07-05 11:34:23','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1122,'2012-07-05 11:34:34','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1123,'2012-07-05 11:34:34','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1124,'2012-07-05 11:35:42','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1125,'2012-07-05 11:35:42','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1126,'2012-07-05 11:37:22','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1127,'2012-07-05 11:37:22','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1128,'2012-07-05 11:37:27','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1129,'2012-07-05 11:37:36','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1130,'2012-07-05 11:40:08','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1131,'2012-07-05 11:40:09','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1132,'2012-07-05 11:40:11','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1133,'2012-07-05 11:40:22','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1134,'2012-07-05 11:41:10','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1135,'2012-07-05 11:41:10','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1136,'2012-07-05 11:41:30','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1137,'2012-07-05 11:46:41','INFO','admin','用户登录','用户登录成功'),(1138,'2012-07-05 11:46:47','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1139,'2012-07-05 11:46:47','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1140,'2012-07-05 11:47:39','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1141,'2012-07-05 12:08:46','INFO','admin','用户登录','用户登录成功'),(1142,'2012-07-05 12:08:58','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1143,'2012-07-05 12:08:58','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1144,'2012-07-05 12:09:08','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1145,'2012-07-05 12:09:12','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1146,'2012-07-05 12:09:15','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1147,'2012-07-05 12:33:38','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1148,'2012-07-05 12:33:52','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1149,'2012-07-05 12:34:07','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1150,'2012-07-05 12:34:25','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1151,'2012-07-05 12:34:26','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1152,'2012-07-05 12:34:27','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1153,'2012-07-05 12:34:32','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1154,'2012-07-05 12:34:37','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1155,'2012-07-05 12:34:49','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1156,'2012-07-05 12:35:05','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1157,'2012-07-05 12:38:03','INFO','admin','用户登录','用户登录成功'),(1158,'2012-07-05 12:38:09','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1159,'2012-07-05 12:38:10','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1160,'2012-07-05 12:38:16','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1161,'2012-07-05 12:38:16','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1162,'2012-07-05 12:38:52','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1163,'2012-07-05 12:42:02','INFO','admin','用户登录','用户登录成功'),(1164,'2012-07-05 12:42:08','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1165,'2012-07-05 12:42:08','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1166,'2012-07-05 12:42:21','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1167,'2012-07-05 12:42:24','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1168,'2012-07-05 12:42:28','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1169,'2012-07-05 12:42:52','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1170,'2012-07-05 12:43:42','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1171,'2012-07-05 14:39:35','INFO','admin','用户登录','用户登录成功'),(1172,'2012-07-05 14:39:47','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1173,'2012-07-05 14:39:51','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1174,'2012-07-05 14:39:51','INFO','admin','设备日志审计','用户获取所有设备名列表成功'),(1175,'2012-07-05 14:40:32','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1176,'2012-07-05 14:40:32','INFO','admin','设备日志审计','用户获取所有设备名列表成功'),(1177,'2012-07-05 14:40:51','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1178,'2012-07-05 14:40:55','ERROR','admin','设备日志审计','用户清空设备日志审计信息失败 '),(1179,'2012-07-05 14:40:57','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1180,'2012-07-05 14:41:06','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1181,'2012-07-05 14:41:24','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1182,'2012-07-05 14:41:24','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1183,'2012-07-05 14:42:16','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1184,'2012-07-05 14:42:16','INFO','admin','设备日志审计','用户获取所有设备名列表成功'),(1185,'2012-07-05 14:42:34','ERROR','admin','设备日志审计','用户清空设备日志审计信息失败 '),(1186,'2012-07-05 14:42:36','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1187,'2012-07-05 14:42:38','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1188,'2012-07-05 14:45:35','INFO','admin','设备日志审计','用户获取所有设备名列表成功'),(1189,'2012-07-05 14:45:35','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1190,'2012-07-05 14:46:02','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1191,'2012-07-05 14:46:02','INFO','admin','设备日志审计','用户获取所有设备名列表成功'),(1192,'2012-07-05 14:46:33','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1193,'2012-07-05 14:46:33','INFO','admin','设备日志审计','用户获取所有设备名列表成功'),(1194,'2012-07-05 14:46:41','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1195,'2012-07-05 14:46:42','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1196,'2012-07-05 14:46:59','INFO','admin','用户日志审计','用户读取用户日志审计信息成功 '),(1197,'2012-07-05 14:46:59','INFO','admin','用户日志审计','用户获取所有账号名列表成功'),(1198,'2012-07-05 14:48:07','ERROR','admin','设备日志审计','用户清空设备日志审计信息失败 '),(1199,'2012-07-05 14:48:09','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1200,'2012-07-05 14:48:34','ERROR','admin','设备日志审计','用户清空设备日志审计信息失败 '),(1201,'2012-07-05 14:48:36','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1202,'2012-07-05 14:48:39','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1203,'2012-07-05 15:06:51','INFO','admin','用户登录','用户登录成功'),(1204,'2012-07-05 15:06:59','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1205,'2012-07-05 15:06:59','INFO','admin','设备日志审计','用户获取所有设备名列表成功'),(1206,'2012-07-05 15:07:28','ERROR','admin','设备日志审计','用户清空设备日志审计信息失败 '),(1207,'2012-07-05 15:08:17','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1208,'2012-07-05 15:08:41','ERROR','admin','设备日志审计','用户清空设备日志审计信息失败 '),(1209,'2012-07-05 15:09:36','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1210,'2012-07-05 15:11:15','ERROR','admin','设备日志审计','用户清空设备日志审计信息失败 '),(1211,'2012-07-05 15:11:28','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1212,'2012-07-05 15:11:29','INFO','admin','设备日志审计','用户获取所有设备名列表成功'),(1213,'2012-07-05 15:12:25','INFO','admin','用户登录','用户退出成功'),(1214,'2012-07-05 15:14:28','INFO','admin','用户登录','用户登录成功'),(1215,'2012-07-05 15:14:34','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1216,'2012-07-05 15:14:34','INFO','admin','设备日志审计','用户获取所有设备名列表成功'),(1217,'2012-07-05 15:15:12','ERROR','admin','设备日志审计','用户清空设备日志审计信息失败 '),(1218,'2012-07-05 15:15:24','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1219,'2012-07-05 15:15:24','INFO','admin','设备日志审计','用户获取所有设备名列表成功'),(1220,'2012-07-05 15:15:37','ERROR','admin','设备日志审计','用户清空设备日志审计信息失败 '),(1221,'2012-07-05 15:15:41','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1222,'2012-07-05 15:19:14','INFO','admin','用户登录','用户登录成功'),(1223,'2012-07-05 15:19:20','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1224,'2012-07-05 15:19:20','INFO','admin','设备日志审计','用户获取所有设备名列表成功'),(1225,'2012-07-05 15:19:32','ERROR','admin','设备日志审计','用户清空设备日志审计信息失败 '),(1226,'2012-07-05 15:19:42','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1227,'2012-07-05 15:20:48','ERROR','admin','设备日志审计','用户清空设备日志审计信息失败 '),(1228,'2012-07-05 15:21:50','INFO','admin','用户登录','用户登录成功'),(1229,'2012-07-05 15:21:55','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1230,'2012-07-05 15:21:55','INFO','admin','设备日志审计','用户获取所有设备名列表成功'),(1231,'2012-07-05 15:21:59','ERROR','admin','设备日志审计','用户清空设备日志审计信息失败 '),(1232,'2012-07-05 15:22:01','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1233,'2012-07-05 15:22:32','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1234,'2012-07-05 15:22:33','INFO','admin','设备日志审计','用户获取所有设备名列表成功'),(1235,'2012-07-05 15:22:49','ERROR','admin','设备日志审计','用户清空设备日志审计信息失败 '),(1236,'2012-07-05 15:24:06','INFO','admin','用户登录','用户登录成功'),(1237,'2012-07-05 15:25:06','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1238,'2012-07-05 15:25:06','INFO','admin','设备日志审计','用户获取所有设备名列表成功'),(1239,'2012-07-05 15:25:13','INFO','admin','设备日志审计','用户清空设备日志审计信息成功 '),(1240,'2012-07-05 15:25:14','INFO','admin','设备日志审计','用户读取设备日志审计信息成功 '),(1241,'2012-07-05 15:31:03','INFO','admin','配置管理','用户获取管理客户机地址成功 '),(1242,'2012-07-05 15:31:03','INFO','admin','配置管理','用户获取管理服务、集控采集数据接口设定IP地址成功 '),(1243,'2012-07-05 15:49:14','INFO','admin','用户登录','用户登录成功'),(1244,'2012-07-05 15:49:29','INFO','admin','设备管理','用户查找设备信息成功'),(1245,'2012-07-05 15:49:29','INFO','admin','用户管理','用户获取所有角色名成功'),(1246,'2012-07-05 15:49:40','INFO','admin','设备管理','用户查找设备信息成功'),(1247,'2012-07-05 15:53:34','INFO','admin','用户管理','用户获取所有角色名成功'),(1248,'2012-07-05 15:53:35','INFO','admin','用户管理','用户获取所有账号信息成功'),(1249,'2012-07-05 15:53:39','INFO','admin','用户管理','用户检查用户名是否存在成功'),(1250,'2012-07-05 15:53:44','INFO','admin','用户管理','用户检查用户名是否存在成功'),(1251,'2012-07-05 15:53:45','INFO','admin','用户管理','用户检查用户名是否存在成功'),(1252,'2012-07-05 15:53:52','INFO','admin','用户管理','用户检查用户名是否存在成功'),(1253,'2012-07-05 16:13:12','INFO','admin','用户管理','用户获取所有角色名成功'),(1254,'2012-07-05 16:13:12','INFO','admin','用户管理','用户获取所有账号信息成功'),(1255,'2012-07-05 16:13:23','INFO','admin','设备管理','用户查找设备信息成功'),(1256,'2012-07-05 16:13:36','INFO','admin','用户登录','用户退出成功'),(1257,'2012-07-05 16:14:23','INFO','admin','用户登录','用户登录成功'),(1258,'2012-07-05 16:14:32','INFO','admin','设备管理','用户获取所有链路名列表成功'),(1259,'2012-07-05 16:14:32','INFO','admin','设备管理','用户查找设备信息成功'),(1260,'2012-07-05 16:22:50','INFO','admin','设备管理','用户新增设备信息成功'),(1261,'2012-07-05 16:22:51','INFO','admin','设备管理','用户查找设备信息成功'),(1262,'2012-07-05 16:23:56','INFO','admin','设备管理','用户获取所有链路名列表成功'),(1263,'2012-07-05 16:23:56','INFO','admin','设备管理','用户查找设备信息成功'),(1264,'2012-07-05 16:26:55','INFO','admin','用户登录','用户登录成功'),(1265,'2012-07-05 16:27:04','INFO','admin','设备管理','用户获取所有链路名列表成功'),(1266,'2012-07-05 16:27:04','INFO','admin','设备管理','用户查找设备信息成功'),(1267,'2012-07-05 16:27:14','INFO','admin','设备管理','用户检查新增设备名是否已经存在成功'),(1268,'2012-07-05 16:27:23','INFO','admin','设备管理','用户检查新增设备名是否已经存在成功'),(1269,'2012-07-05 16:27:27','INFO','admin','设备管理','用户检查新增设备名是否已经存在成功'),(1270,'2012-07-05 16:27:36','INFO','admin','用户管理','用户获取所有角色名成功'),(1271,'2012-07-05 16:44:06','INFO','admin','用户登录','用户登录成功'),(1272,'2012-07-05 16:44:15','INFO','admin','设备管理','用户查找设备信息成功'),(1273,'2012-07-05 16:44:25','INFO','admin','设备管理','用户检查新增设备名是否已经存在成功'),(1274,'2012-07-05 16:44:28','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1275,'2012-07-05 16:44:34','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1276,'2012-07-05 16:46:08','INFO','admin','设备管理','用户查找设备信息成功'),(1277,'2012-07-05 16:46:12','INFO','admin','设备管理','用户检查新增设备名是否已经存在成功'),(1278,'2012-07-05 16:46:40','INFO','admin','设备管理','用户查找设备信息成功'),(1279,'2012-07-05 16:46:44','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1280,'2012-07-05 16:46:51','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1281,'2012-07-05 16:46:55','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1282,'2012-07-05 16:47:00','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1283,'2012-07-05 16:54:16','INFO','admin','用户登录','用户退出成功'),(1284,'2012-07-05 16:55:16','INFO','admin','用户登录','用户登录成功'),(1285,'2012-07-05 16:55:28','INFO','admin','设备管理','用户查找设备信息成功'),(1286,'2012-07-05 16:56:31','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1287,'2012-07-05 16:58:26','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1288,'2012-07-05 16:58:46','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1289,'2012-07-05 16:59:28','INFO','admin','设备管理','用户查找设备信息成功'),(1290,'2012-07-05 16:59:32','ERROR','admin','设备管理','用户获取所有链路名列表失败'),(1291,'2012-07-05 16:59:38','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1292,'2012-07-05 16:59:48','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1293,'2012-07-05 16:59:55','ERROR','admin','设备管理','用户获取所有链路名列表失败'),(1294,'2012-07-05 17:02:45','ERROR','admin','设备管理','用户获取所有链路名列表失败'),(1295,'2012-07-05 17:03:15','INFO','admin','用户登录','用户登录成功'),(1296,'2012-07-05 17:03:20','INFO','admin','设备管理','用户查找设备信息成功'),(1297,'2012-07-05 17:09:04','INFO','admin','设备管理','用户查找设备信息成功'),(1298,'2012-07-05 17:09:36','INFO','admin','设备管理','用户查找设备信息成功'),(1299,'2012-07-05 17:10:11','INFO','admin','设备管理','用户查找设备信息成功'),(1300,'2012-07-05 17:10:48','INFO','admin','设备管理','用户查找设备信息成功'),(1301,'2012-07-05 17:12:09','INFO','admin','设备管理','用户查找设备信息成功'),(1302,'2012-07-05 17:13:01','INFO','admin','设备管理','用户查找设备信息成功'),(1303,'2012-07-05 17:15:48','INFO','admin','设备管理','用户查找设备信息成功'),(1304,'2012-07-05 17:30:27','INFO','admin','设备管理','用户查找设备信息成功'),(1305,'2012-07-05 17:30:30','INFO','admin','设备管理','用户查找设备信息成功'),(1306,'2012-07-05 17:37:24','INFO','admin','设备管理','用户查找设备信息成功'),(1307,'2012-07-05 17:39:34','INFO','admin','设备管理','用户查找设备信息成功'),(1308,'2012-07-05 17:39:42','INFO','admin','设备管理','用户查找设备信息成功'),(1309,'2012-07-05 17:39:48','INFO','admin','用户登录','用户退出成功'),(1310,'2012-07-05 17:41:24','INFO','admin','用户登录','用户登录成功'),(1311,'2012-07-05 17:41:30','INFO','admin','设备管理','用户查找设备信息成功'),(1312,'2012-07-05 17:42:06','INFO','admin','用户登录','用户登录成功'),(1313,'2012-07-05 17:42:12','INFO','admin','设备管理','用户查找设备信息成功'),(1314,'2012-07-05 17:42:33','INFO','admin','设备管理','用户查找设备信息成功'),(1315,'2012-07-05 17:44:14','INFO','admin','设备管理','用户查找设备信息成功'),(1316,'2012-07-05 17:46:07','INFO','admin','设备管理','用户查找设备信息成功'),(1317,'2012-07-05 17:49:29','INFO','admin','设备管理','用户查找设备信息成功'),(1318,'2012-07-05 17:59:47','INFO','admin','用户登录','用户登录成功'),(1319,'2012-07-05 17:59:53','INFO','admin','设备管理','用户查找设备信息成功'),(1320,'2012-07-05 18:01:38','INFO','admin','设备管理','用户查找设备信息成功'),(1321,'2012-07-05 18:02:31','INFO','admin','设备管理','用户查找设备信息成功'),(1322,'2012-07-05 18:04:18','INFO','admin','设备管理','用户查找设备信息成功'),(1323,'2012-07-05 18:05:43','INFO','admin','设备管理','用户查找设备信息成功'),(1324,'2012-07-05 18:09:51','INFO','admin','设备管理','用户查找设备信息成功'),(1325,'2012-07-05 18:10:15','INFO','admin','设备管理','用户查找设备信息成功'),(1326,'2012-07-05 18:16:25','INFO','admin','设备管理','用户查找设备信息成功'),(1327,'2012-07-05 18:21:40','INFO','admin','设备管理','用户查找设备信息成功'),(1328,'2012-07-05 18:22:03','INFO','admin','设备管理','用户检查新增设备名是否已经存在成功'),(1329,'2012-07-05 18:26:14','INFO','admin','设备管理','用户查找设备信息成功'),(1330,'2012-07-05 18:27:54','INFO','admin','设备管理','用户查找设备信息成功'),(1331,'2012-07-05 18:36:03','INFO','admin','设备管理','用户查找设备信息成功'),(1332,'2012-07-05 18:36:23','INFO','admin','设备管理','用户查找设备信息成功'),(1333,'2012-07-05 18:41:11','INFO','admin','设备管理','用户查找设备信息成功'),(1334,'2012-07-05 18:42:07','INFO','admin','用户登录','用户登录成功'),(1335,'2012-07-05 18:42:13','INFO','admin','设备管理','用户查找设备信息成功'),(1336,'2012-07-05 18:44:15','INFO','admin','设备管理','用户查找设备信息成功'),(1337,'2012-07-05 18:51:14','INFO','admin','设备管理','用户查找设备信息成功'),(1338,'2012-07-05 18:51:55','INFO','admin','设备管理','用户查找设备信息成功'),(1339,'2012-07-05 18:53:28','INFO','admin','设备管理','用户查找设备信息成功'),(1340,'2012-07-05 18:53:30','INFO','admin','设备管理','用户查找设备信息成功'),(1341,'2012-07-05 18:53:56','INFO','admin','设备管理','用户查找设备信息成功'),(1342,'2012-07-05 18:56:03','INFO','admin','设备管理','用户查找设备信息成功'),(1343,'2012-07-05 18:56:18','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1344,'2012-07-05 18:57:03','INFO','admin','设备管理','用户查找设备信息成功'),(1345,'2012-07-05 18:57:07','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1346,'2012-07-05 18:57:35','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1347,'2012-07-05 18:57:41','INFO','admin','设备管理','用户查找设备信息成功'),(1348,'2012-07-05 18:57:47','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1349,'2012-07-05 18:57:54','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1350,'2012-07-05 18:58:37','INFO','admin','设备管理','用户查找设备信息成功'),(1351,'2012-07-05 18:58:40','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1352,'2012-07-05 18:58:52','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1353,'2012-07-05 18:58:57','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1354,'2012-07-05 18:59:01','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1355,'2012-07-05 18:59:06','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1356,'2012-07-05 19:00:25','INFO','admin','设备管理','用户查找设备信息成功'),(1357,'2012-07-05 19:00:58','INFO','admin','设备管理','用户查找设备信息成功'),(1358,'2012-07-05 19:01:16','INFO','admin','设备管理','用户查找设备信息成功'),(1359,'2012-07-05 19:01:29','INFO','admin','设备管理','用户查找设备信息成功'),(1360,'2012-07-05 19:02:02','INFO','admin','设备管理','用户查找设备信息成功'),(1361,'2012-07-05 19:02:54','INFO','admin','设备管理','用户查找设备信息成功'),(1362,'2012-07-05 19:03:01','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1363,'2012-07-05 19:03:14','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1364,'2012-07-05 19:03:21','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1365,'2012-07-05 19:03:28','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1366,'2012-07-05 19:03:36','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1367,'2012-07-05 19:04:00','INFO','admin','设备管理','用户查找设备信息成功'),(1368,'2012-07-05 19:04:15','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1369,'2012-07-05 19:04:20','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1370,'2012-07-05 19:04:27','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1371,'2012-07-05 19:04:34','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1372,'2012-07-05 19:04:53','INFO','admin','设备管理','用户查找设备信息成功'),(1373,'2012-07-05 19:04:59','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1374,'2012-07-05 19:05:06','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1375,'2012-07-05 19:05:48','INFO','admin','设备管理','用户查找设备信息成功'),(1376,'2012-07-05 19:05:54','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1377,'2012-07-05 19:06:05','INFO','admin','设备管理','用户获取所有外部链路名列表成功'),(1378,'2012-07-05 19:06:20','INFO','admin','设备管理','用户获取所有内部链路名列表成功'),(1379,'2012-07-05 19:08:09','INFO','admin','设备管理','用户查找设备信息成功'),(1380,'2012-07-05 19:09:26','INFO','admin','设备管理','用户查找设备信息成功'),(1381,'2012-07-05 19:10:04','INFO','admin','设备管理','用户查找设备信息成功'),(1382,'2012-07-05 19:10:17','INFO','admin','设备管理','用户查找设备信息成功'),(1383,'2012-07-06 09:51:57','INFO','admin','用户登录','用户登录成功'),(1384,'2012-07-06 09:54:23','INFO','admin','设备管理','用户查找设备信息成功'),(1385,'2012-07-06 09:59:55','INFO','admin','设备管理','用户查找设备信息成功'),(1386,'2012-07-06 10:04:28','INFO','admin','设备管理','用户查找设备信息成功'),(1387,'2012-07-06 10:04:48','INFO','admin','设备管理','用户查找设备信息成功'),(1388,'2012-07-06 10:05:40','INFO','admin','设备管理','用户查找设备信息成功'),(1389,'2012-07-06 10:06:01','INFO','admin','设备管理','用户查找设备信息成功'),(1390,'2012-07-06 10:06:51','INFO','admin','设备管理','用户查找设备信息成功'),(1391,'2012-07-06 10:19:46','INFO','admin','设备管理','用户查找设备信息成功'),(1392,'2012-07-06 10:26:34','INFO','admin','设备管理','用户查找设备信息成功'),(1393,'2012-07-06 10:27:26','INFO','admin','设备管理','用户查找设备信息成功'),(1394,'2012-07-06 10:28:43','INFO','admin','设备管理','用户查找设备信息成功'),(1395,'2012-07-06 10:29:11','INFO','admin','设备管理','用户查找设备信息成功'),(1396,'2012-07-06 10:29:34','INFO','admin','设备管理','用户查找设备信息成功'),(1397,'2012-07-06 10:33:31','INFO','admin','设备管理','用户查找设备信息成功'),(1398,'2012-07-06 10:35:41','INFO','admin','设备管理','用户查找设备信息成功'),(1399,'2012-07-06 10:36:00','INFO','admin','设备管理','用户查找设备信息成功'),(1400,'2012-07-06 10:36:22','INFO','admin','设备管理','用户查找设备信息成功'),(1401,'2012-07-06 10:37:39','INFO','admin','设备管理','用户查找设备信息成功'),(1402,'2012-07-06 10:39:06','INFO','admin','设备管理','用户查找设备信息成功'),(1403,'2012-07-06 10:40:52','INFO','admin','用户登录','用户登录成功'),(1404,'2012-07-06 10:40:57','INFO','admin','设备管理','用户查找设备信息成功'),(1405,'2012-07-06 10:53:56','INFO','admin','设备管理','用户查找设备信息成功'),(1406,'2012-07-06 10:58:04','INFO','admin','设备管理','用户查找设备信息成功'),(1407,'2012-07-06 10:59:58','INFO','admin','设备管理','用户查找设备信息成功'),(1408,'2012-07-06 11:00:37','INFO','admin','设备管理','用户查找设备信息成功'),(1409,'2012-07-06 11:01:09','INFO','admin','设备管理','用户查找设备信息成功'),(1410,'2012-07-06 11:01:21','INFO','admin','设备管理','用户查找设备信息成功'),(1411,'2012-07-06 11:02:19','INFO','admin','设备管理','用户查找设备信息成功'),(1412,'2012-07-06 11:03:12','INFO','admin','设备管理','用户查找设备信息成功'),(1413,'2012-07-06 11:03:19','INFO','admin','设备管理','用户查找设备信息成功'),(1414,'2012-07-06 11:03:08','INFO','admin','设备管理','用户查找设备信息成功'),(1415,'2012-07-06 11:03:32','INFO','admin','设备管理','用户查找设备信息成功'),(1416,'2012-07-06 11:04:54','INFO','admin','设备管理','用户查找设备信息成功'),(1417,'2012-07-06 11:08:12','INFO','admin','设备管理','用户查找设备信息成功'),(1418,'2012-07-06 11:08:30','INFO','admin','设备管理','用户查找设备信息成功'),(1419,'2012-07-06 11:09:05','INFO','admin','设备管理','用户查找设备信息成功'),(1420,'2012-07-06 11:09:54','INFO','admin','设备管理','用户查找设备信息成功'),(1421,'2012-07-06 11:10:16','INFO','admin','设备管理','用户查找设备信息成功'),(1422,'2012-07-06 11:13:32','INFO','admin','设备管理','用户查找设备信息成功'),(1423,'2012-07-06 11:18:32','INFO','admin','设备管理','用户查找设备信息成功'),(1424,'2012-07-06 11:29:38','INFO','admin','设备管理','用户查找设备信息成功'),(1425,'2012-07-06 11:30:33','INFO','admin','设备管理','用户查找设备信息成功'),(1426,'2012-07-06 11:31:06','INFO','admin','设备管理','用户查找设备信息成功'),(1427,'2012-07-06 12:40:55','INFO','admin','设备管理','用户查找设备信息成功'),(1428,'2012-07-06 12:42:32','INFO','admin','设备管理','用户查找设备信息成功'),(1429,'2012-07-06 12:46:58','INFO','admin','设备管理','用户查找设备信息成功'),(1430,'2012-07-06 12:48:34','INFO','admin','设备管理','用户查找设备信息成功'),(1431,'2012-07-06 12:48:49','INFO','admin','设备管理','用户查找设备信息成功'),(1432,'2012-07-06 12:49:10','INFO','admin','设备管理','用户查找设备信息成功'),(1433,'2012-07-06 12:49:39','INFO','admin','设备管理','用户查找设备信息成功'),(1434,'2012-07-06 13:06:09','INFO','admin','设备管理','用户查找设备信息成功'),(1435,'2012-07-06 13:06:23','INFO','admin','设备管理','用户查找设备信息成功'),(1436,'2012-07-06 13:10:27','INFO','admin','设备管理','用户查找设备信息成功'),(1437,'2012-07-06 13:11:07','INFO','admin','设备管理','用户查找设备信息成功'),(1438,'2012-07-06 13:11:46','INFO','admin','设备管理','用户查找设备信息成功'),(1439,'2012-07-06 13:12:22','INFO','admin','设备管理','用户查找设备信息成功'),(1440,'2012-07-06 13:12:43','INFO','admin','设备管理','用户查找设备信息成功'),(1441,'2012-07-06 13:13:22','INFO','admin','设备管理','用户查找设备信息成功'),(1442,'2012-07-12 17:20:07','INFO','admin','用户登录','用户登录成功'),(1443,'2012-07-12 17:20:16','INFO','admin','设备管理','用户查找设备信息成功'),(1444,'2012-07-12 17:38:12','INFO','admin','设备管理','用户查找设备信息成功'),(1445,'2012-07-12 17:38:13','INFO','admin','设备管理','用户查找设备信息成功'),(1446,'2012-07-12 17:38:14','INFO','admin','设备管理','用户查找设备信息成功'),(1447,'2012-07-12 17:38:14','INFO','admin','设备管理','用户查找设备信息成功'),(1448,'2012-07-12 17:38:15','INFO','admin','设备管理','用户查找设备信息成功'),(1449,'2012-07-12 17:38:16','INFO','admin','设备管理','用户查找设备信息成功'),(1450,'2012-07-12 17:38:16','INFO','admin','设备管理','用户查找设备信息成功'),(1451,'2012-07-12 17:38:17','INFO','admin','设备管理','用户查找设备信息成功'),(1452,'2012-07-12 17:38:17','INFO','admin','设备管理','用户查找设备信息成功'),(1453,'2012-07-12 17:38:18','INFO','admin','设备管理','用户查找设备信息成功'),(1454,'2012-07-12 17:38:18','INFO','admin','设备管理','用户查找设备信息成功'),(1455,'2012-07-12 17:38:18','INFO','admin','设备管理','用户查找设备信息成功'),(1456,'2012-07-12 17:38:19','INFO','admin','设备管理','用户查找设备信息成功'),(1457,'2012-07-12 17:38:19','INFO','admin','设备管理','用户查找设备信息成功'),(1458,'2012-07-12 17:49:43','INFO','admin','用户登录','用户退出成功'),(1459,'2012-07-13 10:19:56','INFO','admin','用户登录','用户登录成功'),(1460,'2012-07-13 10:20:15','INFO','admin','角色管理','用户获取角色信息成功'),(1461,'2012-07-13 10:20:22','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(1462,'2012-07-13 10:21:18','INFO','admin','角色管理','用户获取角色权限信息用于新增成功'),(1463,'2012-07-13 16:01:25','INFO','admin','用户登录','用户登录成功'),(1464,'2012-07-13 16:01:32','INFO','admin','用户管理','用户获取所有角色名成功'),(1465,'2012-07-13 16:01:32','INFO','admin','用户管理','用户获取所有账号信息成功'),(1466,'2012-07-13 16:01:35','INFO','admin','角色管理','用户获取角色信息成功'),(1467,'2012-07-13 16:01:37','INFO','admin','角色管理','用户获取角色权限信息用于新增成功'),(1468,'2012-07-13 16:01:48','INFO','admin','角色管理','用户获取角色权限信息用于修改成功'),(1469,'2012-07-13 16:02:03','INFO','admin','角色管理','用户更新角色信息成功'),(1470,'2012-07-13 16:02:05','INFO','admin','角色管理','用户获取角色信息成功'),(1471,'2012-07-13 16:02:09','INFO','admin','用户登录','用户退出成功'),(1472,'2012-07-13 16:02:22','INFO','admin','用户登录','用户登录成功'),(1473,'2012-07-13 16:03:04','INFO','admin','用户登录','用户退出成功'),(1474,'2012-07-13 16:03:12','INFO','admin','用户登录','用户登录成功'),(1475,'2012-07-13 16:13:26','INFO','admin','用户登录','用户退出成功'),(1476,'2012-07-13 16:13:35','INFO','admin','用户登录','用户登录成功'),(1477,'2012-07-13 16:14:08','INFO','admin','用户登录','用户退出成功'),(1478,'2012-07-13 16:14:55','INFO','admin','用户登录','用户登录成功'),(1479,'2012-07-13 16:15:42','INFO','admin','用户登录','用户登录成功');
/*!40000 ALTER TABLE `user_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'gap'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-07-16 15:00:24
