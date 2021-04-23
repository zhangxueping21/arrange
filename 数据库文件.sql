/*
SQLyog Ultimate v10.00 Beta1
MySQL - 5.5.62 : Database - arrange
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`arrange` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `arrange`;

/*Table structure for table `active` */

DROP TABLE IF EXISTS `active`;

CREATE TABLE `active` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `create_user` int(20) NOT NULL COMMENT '活动的发起人，和user表的id关联',
  `unit` int(20) NOT NULL COMMENT '活动所属的单位',
  `name` varchar(20) DEFAULT NULL COMMENT '活动名称',
  `start_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '开始时间',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '结束时间',
  `num` int(10) NOT NULL COMMENT '值班的人数，有1,2,3,4,5人，0表示不限',
  `position` varchar(50) DEFAULT NULL COMMENT '活动地点',
  `remarks` varchar(500) DEFAULT NULL COMMENT '活动备注',
  `state` int(10) NOT NULL COMMENT '活动的状态，已排班为1，未排班为0',
  `result` longtext COMMENT '排班结果',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `create_user` (`create_user`),
  KEY `unit` (`unit`),
  CONSTRAINT `active_ibfk_2` FOREIGN KEY (`create_user`) REFERENCES `user` (`id`),
  CONSTRAINT `active_ibfk_3` FOREIGN KEY (`unit`) REFERENCES `unit` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='存放活动信息的表';

/*Data for the table `active` */

LOCK TABLES `active` WRITE;

UNLOCK TABLES;

/*Table structure for table `first_day` */

DROP TABLE IF EXISTS `first_day`;

CREATE TABLE `first_day` (
  `id` int(10) NOT NULL,
  `year` int(10) NOT NULL COMMENT '年',
  `month` int(10) NOT NULL COMMENT '月',
  `day` int(10) NOT NULL COMMENT '日',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录每个学期的第一天';

/*Data for the table `first_day` */

LOCK TABLES `first_day` WRITE;

insert  into `first_day`(`id`,`year`,`month`,`day`) values (1,2021,1,4);

UNLOCK TABLES;

/*Table structure for table `invitation` */

DROP TABLE IF EXISTS `invitation`;

CREATE TABLE `invitation` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `active_id` int(20) NOT NULL COMMENT '活动id',
  `user_id` int(20) NOT NULL COMMENT '被邀请的用户的id',
  `state` int(20) DEFAULT NULL COMMENT '邀请状态，0表示未查看，1代表已查看未表态，10代表已查看并且拒绝，11代表已查看并且同意',
  `time` longtext COMMENT '值班时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `active_id` (`active_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `invitation_ibfk_1` FOREIGN KEY (`active_id`) REFERENCES `active` (`id`),
  CONSTRAINT `invitation_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8 COMMENT='邀请信息';

/*Data for the table `invitation` */

LOCK TABLES `invitation` WRITE;

UNLOCK TABLES;

/*Table structure for table `unit` */

DROP TABLE IF EXISTS `unit`;

CREATE TABLE `unit` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `unit_name` varchar(20) DEFAULT NULL COMMENT '单位名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='所属的单位信息';

/*Data for the table `unit` */

LOCK TABLES `unit` WRITE;

insert  into `unit`(`id`,`unit_name`,`create_time`,`update_time`) values (3,'testunit2','2021-01-24 17:05:48','2020-11-15 21:27:51'),(4,'比特工场','2020-11-08 15:36:20','2020-11-08 15:36:24'),(5,'计科1804','2021-01-24 17:05:54','2020-11-15 21:22:13'),(6,'JavaEE','2020-11-15 21:22:43','2020-11-15 21:22:45'),(8,'testunit1','2020-11-15 21:28:05','2020-11-15 21:28:07');

UNLOCK TABLES;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL COMMENT '用户的姓名',
  `stu_number` varchar(20) DEFAULT NULL COMMENT '学号',
  `unit` varchar(50) DEFAULT NULL COMMENT '所属单位，一个由数字字符组成的字符串，每个数字字符都在unit表中能找到',
  `timetable` longtext COMMENT '课表信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='用户信息';

/*Data for the table `user` */

LOCK TABLES `user` WRITE;

insert  into `user`(`id`,`name`,`stu_number`,`unit`,`timetable`,`create_time`,`update_time`) values (1,'张雪萍','201821091126','4 5 7 4 5 7','{\"curriculum\":[[{\"from_week\":1,\"to_week\":16,\"from_section\":5,\"to_section\":7,\"arrange_type\":0},{\"from_week\":11,\"to_week\":16,\"from_section\":9,\"to_section\":10,\"arrange_type\":0}],[{\"from_week\":11,\"to_week\":16,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":1,\"to_week\":16,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":11,\"to_week\":16,\"from_section\":5,\"to_section\":8,\"arrange_type\":1}],[{\"from_week\":1,\"to_week\":16,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":1,\"to_week\":16,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":11,\"to_week\":16,\"from_section\":5,\"to_section\":6,\"arrange_type\":0},{\"from_week\":11,\"to_week\":16,\"from_section\":7,\"to_section\":8,\"arrange_type\":0}],[{\"from_week\":11,\"to_week\":16,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":1,\"to_week\":16,\"from_section\":3,\"to_section\":4,\"arrange_type\":0}],[{\"from_week\":1,\"to_week\":16,\"from_section\":5,\"to_section\":7,\"arrange_type\":0}],[],[]]}','2021-01-24 08:53:06','2021-01-24 08:53:06'),(13,'谭超宇','201821091124','4','{\"curriculum\":[[{\"from_week\":2,\"to_week\":11,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":5,\"to_section\":6,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":9,\"to_section\":11,\"arrange_type\":0}],[{\"from_week\":6,\"to_week\":16,\"from_section\":1,\"to_section\":2,\"arrange_type\":2},{\"from_week\":2,\"to_week\":17,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":7,\"to_section\":8,\"arrange_type\":0},{\"from_week\":7,\"to_week\":18,\"from_section\":9,\"to_section\":10,\"arrange_type\":1}],[{\"from_week\":2,\"to_week\":17,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":2,\"to_week\":11,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":5,\"to_section\":6,\"arrange_type\":0},{\"from_week\":4,\"to_week\":9,\"from_section\":7,\"to_section\":8,\"arrange_type\":0},{\"from_week\":10,\"to_week\":16,\"from_section\":9,\"to_section\":11,\"arrange_type\":2}],[{\"from_week\":2,\"to_week\":17,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":7,\"to_section\":8,\"arrange_type\":0},{\"from_week\":5,\"to_week\":10,\"from_section\":9,\"to_section\":10,\"arrange_type\":0}],[{\"from_week\":11,\"to_week\":16,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":5,\"to_section\":7,\"arrange_type\":0},{\"from_week\":8,\"to_week\":10,\"from_section\":9,\"to_section\":11,\"arrange_type\":0}],[],[]]}','2020-11-19 08:43:10','2020-11-19 08:43:10'),(22,'测试1','20001','4','{\"curriculum\":[[{\"from_week\":2,\"to_week\":11,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":5,\"to_section\":6,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":9,\"to_section\":11,\"arrange_type\":0}],[{\"from_week\":6,\"to_week\":16,\"from_section\":1,\"to_section\":2,\"arrange_type\":2},{\"from_week\":2,\"to_week\":17,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":7,\"to_section\":8,\"arrange_type\":0},{\"from_week\":7,\"to_week\":18,\"from_section\":9,\"to_section\":10,\"arrange_type\":1}],[{\"from_week\":2,\"to_week\":17,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":2,\"to_week\":11,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":5,\"to_section\":6,\"arrange_type\":0},{\"from_week\":4,\"to_week\":9,\"from_section\":7,\"to_section\":8,\"arrange_type\":0},{\"from_week\":10,\"to_week\":16,\"from_section\":9,\"to_section\":11,\"arrange_type\":2}],[{\"from_week\":2,\"to_week\":17,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":7,\"to_section\":8,\"arrange_type\":0},{\"from_week\":5,\"to_week\":10,\"from_section\":9,\"to_section\":10,\"arrange_type\":0}],[{\"from_week\":11,\"to_week\":16,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":5,\"to_section\":7,\"arrange_type\":0},{\"from_week\":8,\"to_week\":10,\"from_section\":9,\"to_section\":11,\"arrange_type\":0}],[],[]]}','2021-01-24 17:08:26','0000-00-00 00:00:00'),(23,'测试2','20002','4','{\"curriculum\":[[{\"from_week\":1,\"to_week\":16,\"from_section\":5,\"to_section\":7,\"arrange_type\":0},{\"from_week\":11,\"to_week\":16,\"from_section\":9,\"to_section\":10,\"arrange_type\":0}],[{\"from_week\":11,\"to_week\":16,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":1,\"to_week\":16,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":11,\"to_week\":16,\"from_section\":5,\"to_section\":8,\"arrange_type\":1}],[{\"from_week\":1,\"to_week\":16,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":1,\"to_week\":16,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":11,\"to_week\":16,\"from_section\":5,\"to_section\":6,\"arrange_type\":0},{\"from_week\":11,\"to_week\":16,\"from_section\":7,\"to_section\":8,\"arrange_type\":0}],[{\"from_week\":11,\"to_week\":16,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":1,\"to_week\":16,\"from_section\":3,\"to_section\":4,\"arrange_type\":0}],[{\"from_week\":1,\"to_week\":16,\"from_section\":5,\"to_section\":7,\"arrange_type\":0}],[],[]]}','2021-01-24 17:08:40','0000-00-00 00:00:00'),(24,'测试3','20003','4','{\"curriculum\":[[{\"from_week\":2,\"to_week\":11,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":5,\"to_section\":6,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":9,\"to_section\":11,\"arrange_type\":0}],[{\"from_week\":6,\"to_week\":16,\"from_section\":1,\"to_section\":2,\"arrange_type\":2},{\"from_week\":2,\"to_week\":17,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":7,\"to_section\":8,\"arrange_type\":0},{\"from_week\":7,\"to_week\":18,\"from_section\":9,\"to_section\":10,\"arrange_type\":1}],[{\"from_week\":2,\"to_week\":17,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":2,\"to_week\":11,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":5,\"to_section\":6,\"arrange_type\":0},{\"from_week\":4,\"to_week\":9,\"from_section\":7,\"to_section\":8,\"arrange_type\":0},{\"from_week\":10,\"to_week\":16,\"from_section\":9,\"to_section\":11,\"arrange_type\":2}],[{\"from_week\":2,\"to_week\":17,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":7,\"to_section\":8,\"arrange_type\":0},{\"from_week\":5,\"to_week\":10,\"from_section\":9,\"to_section\":10,\"arrange_type\":0}],[{\"from_week\":11,\"to_week\":16,\"from_section\":1,\"to_section\":2,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":3,\"to_section\":4,\"arrange_type\":0},{\"from_week\":2,\"to_week\":17,\"from_section\":5,\"to_section\":7,\"arrange_type\":0},{\"from_week\":8,\"to_week\":10,\"from_section\":9,\"to_section\":11,\"arrange_type\":0}],[],[]]}','2021-01-24 17:09:07','0000-00-00 00:00:00');

UNLOCK TABLES;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
