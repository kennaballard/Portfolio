use chattest;
--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(100) PRIMARY KEY,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `roles` smallint DEFAULT 0,
  `last_name` varchar (100) DEFAULT NULL,
  `first_name` varchar (100) DEFAULT NULL,
  `phone` varchar (100) DEFAULT NULL,
  `active_flag` smallint DEFAULT 0,
  `last_active` timestamp DEFAULT 0,
  `created_date` timestamp DEFAULT 0

) ;

use baddb;
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `username` varchar(100) PRIMARY KEY,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `roles` smallint DEFAULT 0,
  `last_name` varchar (100) DEFAULT NULL,
  `first_name` varchar (100) DEFAULT NULL,
  `phone` varchar (100) DEFAULT NULL,
  `active_flag` smallint DEFAULT 0,
  `last_active` timestamp DEFAULT 0,
  `created_date` timestamp DEFAULT 0
) ;


