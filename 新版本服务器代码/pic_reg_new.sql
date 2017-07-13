/*
Navicat MySQL Data Transfer

Source Server         : gengxiangyi
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : pic_reg_new

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-06-30 10:57:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for hw_administrator
-- ----------------------------
DROP TABLE IF EXISTS `hw_administrator`;
CREATE TABLE `hw_administrator` (
  `A_id` int(11) NOT NULL,
  `A_telephone` varchar(255) NOT NULL,
  `A_password` varchar(255) NOT NULL,
  `A_nick` varchar(255) DEFAULT NULL,
  `A_icon` int(11) DEFAULT NULL,
  PRIMARY KEY (`A_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hw_day_push
-- ----------------------------
DROP TABLE IF EXISTS `hw_day_push`;
CREATE TABLE `hw_day_push` (
  `D_id` int(11) NOT NULL,
  `V_id` int(11) NOT NULL,
  `Pic_id` int(11) NOT NULL,
  `D_time` datetime DEFAULT NULL,
  `D_isvis` int(11) DEFAULT NULL,
  PRIMARY KEY (`D_id`),
  KEY `hw_day_push_ibfk_1` (`V_id`),
  KEY `Pic_id` (`Pic_id`),
  CONSTRAINT `hw_day_push_ibfk_1` FOREIGN KEY (`V_id`) REFERENCES `hw_volunteer` (`V_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `hw_day_push_ibfk_2` FOREIGN KEY (`Pic_id`) REFERENCES `hw_picture` (`Pic_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hw_have
-- ----------------------------
DROP TABLE IF EXISTS `hw_have`;
CREATE TABLE `hw_have` (
  `H_id` int(11) NOT NULL,
  `V_id` int(11) NOT NULL,
  `I_id` int(11) NOT NULL,
  PRIMARY KEY (`H_id`),
  KEY `hw_have_ibfk_1` (`V_id`),
  KEY `hw_have_ibfk_2` (`I_id`),
  CONSTRAINT `hw_have_ibfk_1` FOREIGN KEY (`V_id`) REFERENCES `hw_volunteer` (`V_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `hw_have_ibfk_2` FOREIGN KEY (`I_id`) REFERENCES `hw_interest` (`I_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hw_interest
-- ----------------------------
DROP TABLE IF EXISTS `hw_interest`;
CREATE TABLE `hw_interest` (
  `I_id` int(11) NOT NULL,
  `Inter` varchar(255) DEFAULT NULL,
  `I_level` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`I_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hw_picture
-- ----------------------------
DROP TABLE IF EXISTS `hw_picture`;
CREATE TABLE `hw_picture` (
  `Pic_id` int(11) NOT NULL,
  `Pic_name` varchar(255) DEFAULT NULL,
  `Pic_passway` varchar(255) DEFAULT NULL,
  `Pic_isvis` int(11) DEFAULT NULL,
  `Pic_finishtime` datetime DEFAULT NULL,
  `Final_label` varchar(255) DEFAULT NULL,
  `Up_time` datetime DEFAULT NULL,
  PRIMARY KEY (`Pic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hw_semantics
-- ----------------------------
DROP TABLE IF EXISTS `hw_semantics`;
CREATE TABLE `hw_semantics` (
  `S_id` int(11) NOT NULL,
  `Pic_id` int(11) DEFAULT NULL,
  `Label_handle` varchar(255) DEFAULT NULL,
  `Num` int(11) DEFAULT NULL,
  PRIMARY KEY (`S_id`),
  KEY `Pic_id` (`Pic_id`),
  CONSTRAINT `hw_semantics_ibfk_1` FOREIGN KEY (`Pic_id`) REFERENCES `hw_picture` (`Pic_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hw_tag
-- ----------------------------
DROP TABLE IF EXISTS `hw_tag`;
CREATE TABLE `hw_tag` (
  `T_id` int(11) NOT NULL,
  `V_id` int(11) NOT NULL,
  `Pic_id` int(11) DEFAULT NULL,
  `Label` varchar(255) DEFAULT NULL,
  `T_time` datetime DEFAULT NULL,
  PRIMARY KEY (`T_id`),
  KEY `V_id` (`V_id`),
  KEY `Pic_id` (`Pic_id`),
  CONSTRAINT `hw_tag_ibfk_1` FOREIGN KEY (`V_id`) REFERENCES `hw_volunteer` (`V_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `hw_tag_ibfk_2` FOREIGN KEY (`Pic_id`) REFERENCES `hw_picture` (`Pic_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hw_tag_push
-- ----------------------------
DROP TABLE IF EXISTS `hw_tag_push`;
CREATE TABLE `hw_tag_push` (
  `T_id` int(11) NOT NULL,
  `V_id` int(11) NOT NULL,
  `Pic_id` int(11) DEFAULT NULL,
  `Label` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`T_id`),
  KEY `V_id` (`V_id`),
  KEY `Pic_id` (`Pic_id`),
  CONSTRAINT `hw_tag_push_ibfk_1` FOREIGN KEY (`V_id`) REFERENCES `hw_volunteer` (`V_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `hw_tag_push_ibfk_2` FOREIGN KEY (`Pic_id`) REFERENCES `hw_picture` (`Pic_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hw_volunteer
-- ----------------------------
DROP TABLE IF EXISTS `hw_volunteer`;
CREATE TABLE `hw_volunteer` (
  `V_id` int(11) NOT NULL,
  `V_telephone` varchar(255) NOT NULL,
  `V_password` varchar(255) NOT NULL,
  `V_nick` varchar(255) DEFAULT NULL,
  `V_icon` int(11) DEFAULT NULL,
  `V_emial` varchar(255) DEFAULT NULL,
  `V_credits` int(11) DEFAULT NULL,
  `V_major` varchar(255) DEFAULT NULL,
  `V_renwu` int(11) DEFAULT NULL,
  PRIMARY KEY (`V_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
