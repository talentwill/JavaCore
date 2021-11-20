CREATE DATABASE test;

use test;

CREATE TABLE IF NOT EXISTS `t_book`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `bookid` INT UNSIGNED NOT NULL,
   `bookname` VARCHAR(100) NOT NULL,
   `price` INT NOT NULL,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into t_book(bookid, bookname, price) values(10, 'Math', 90);
insert into t_book(bookid, bookname, price) values(1, 'English', 80);
insert into t_book(bookid, bookname, price) values(13, 'Physical', 70);