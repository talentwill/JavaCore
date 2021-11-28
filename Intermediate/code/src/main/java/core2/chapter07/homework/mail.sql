CREATE DATABASE email;

use email;

DROP TABLE t_mail;

CREATE TABLE IF NOT EXISTS `t_mail`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `from` VARCHAR(100) NOT NULL,
   `to` VARCHAR(100) NOT NULL,
   `subject` VARCHAR(100) NOT NULL,
   `content` VARCHAR(100) NOT NULL,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_mail(`from`, `to`, `subject`, `content`) values("talentwill@163.com", "247840938@qq.com", 'Hello', "The first mail, welcome to Java world!");
insert into t_mail(`from`, `to`, `subject`, `content`) values("talentwill@163.com", "247840938@qq.com", 'Hello', "The second mail, welcome to Java world!");