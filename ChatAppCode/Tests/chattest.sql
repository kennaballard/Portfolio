/****************************************/
/********** good database ***************/
/****************************************/
use chattest;
--
-- Table structure for table `users`
--
DROP TABLE IF EXISTS `messages`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `username` varchar(100) PRIMARY KEY,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `roles` smallint DEFAULT 0,
  `last_name` varchar (100) DEFAULT NULL,
  `first_name` varchar (100) DEFAULT NULL,
  `phone` varchar (100) DEFAULT NULL,
  `last_active` timestamp DEFAULT 0,
  `created_date` timestamp DEFAULT 0
);

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
INSERT INTO `users` VALUES ('alex','alex@alex','$2y$10$rtBAmnC.EcMY.WMub/KqkOTaPd8bmy8lF.Nq9OJf8okFjrT1bF3Am',0,NULL,NULL,NULL,'0000-00-00 00:00:00','0000-00-00 00:00:00'),('asda','asdasd','$2y$10$LYbJqpD2q.Fjqa/zuE4twetQckcjbxCLvbdOFQm8i4acKCvnrWley',0,NULL,NULL,NULL,'0000-00-00 00:00:00','0000-00-00 00:00:00'),('Bob','bob','$2y$10$vzREB6gCaOVIruMd4Fjlxu8eyTfoVCOnyN9/hR8eI5A1xsDAxPs.y',0,NULL,NULL,NULL,'0000-00-00 00:00:00','0000-00-00 00:00:00'),('sandy','sandy','$2y$10$.5LgPrUcgFCdRJTGUvc/nuZBMkcs.hNaeXAPZpAZ53Z9ZUT94oyDy',0,NULL,NULL,NULL,'0000-00-00 00:00:00','0000-00-00 00:00:00'),('test','test','$2y$10$fwFee0GRb1m3YnYTflE3He.2Fe0zFKRHt.qQVlzAht5SbFkv50tga',0,NULL,NULL,NULL,'0000-00-00 00:00:00','0000-00-00 00:00:00');
UNLOCK TABLES;

--
-- Table structure for table `messages`
--
CREATE TABLE `messages` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `userID` varchar(100),
  `content` varchar(5000),
  `time` timestamp,
  FOREIGN KEY (`userID`) REFERENCES users(`username`)
  ON DELETE CASCADE
);

LOCK TABLES `messages` WRITE;
INSERT INTO `messages` (userID, content, time) VALUES ('alex', 'A message', '2018-11-13 04:14:52'), ('alex', 'Another message', '2018-11-13 04:14:52'), ('sandy', 'Starter message', '2018-11-13 04:14:52');
UNLOCK TABLES;
/****************************************/
/********** Bad database ****************/
/****************************************/
use baddb;

--
-- Table structure for table `users`
--
DROP TABLE IF EXISTS `message`;
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `username` varchar(100) PRIMARY KEY,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `roles` smallint DEFAULT 0,
  `last_name` varchar (100) DEFAULT NULL,
  `first_name` varchar (100) DEFAULT NULL,
  `phone` varchar (100) DEFAULT NULL,
  `last_active` timestamp DEFAULT 0,
  `created_date` timestamp DEFAULT 0
);

--
-- Dumping data for table `users`
--

LOCK TABLES `user` WRITE;
INSERT INTO `user` VALUES ('alex','alex@alex','$2y$10$rtBAmnC.EcMY.WMub/KqkOTaPd8bmy8lF.Nq9OJf8okFjrT1bF3Am',0,NULL,NULL,NULL,'0000-00-00 00:00:00','0000-00-00 00:00:00'),('asda','asdasd','$2y$10$LYbJqpD2q.Fjqa/zuE4twetQckcjbxCLvbdOFQm8i4acKCvnrWley',0,NULL,NULL,NULL,'0000-00-00 00:00:00','0000-00-00 00:00:00'),('Bob','bob','$2y$10$vzREB6gCaOVIruMd4Fjlxu8eyTfoVCOnyN9/hR8eI5A1xsDAxPs.y',0,NULL,NULL,NULL,'0000-00-00 00:00:00','0000-00-00 00:00:00'),('sandy','sandy','$2y$10$.5LgPrUcgFCdRJTGUvc/nuZBMkcs.hNaeXAPZpAZ53Z9ZUT94oyDy',0,NULL,NULL,NULL,'0000-00-00 00:00:00','0000-00-00 00:00:00'),('test','test','$2y$10$fwFee0GRb1m3YnYTflE3He.2Fe0zFKRHt.qQVlzAht5SbFkv50tga',0,NULL,NULL,NULL,'0000-00-00 00:00:00','0000-00-00 00:00:00');
UNLOCK TABLES;

--
-- Table structure for table `messages`
--
CREATE TABLE `message` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `userID` varchar(100),
  `content` varchar(5000),
  `time` timestamp,
  FOREIGN KEY (userID) REFERENCES user(username)
  ON DELETE CASCADE
);

LOCK TABLES `message` WRITE;
INSERT INTO `message` (userID, content, time) VALUES ('alex', 'A message', '2018-11-13 04:14:52'), ('alex', 'Another message', '2018-11-13 04:14:52'), ('sandy', 'Starter message', '2018-11-13 04:14:52');
UNLOCK TABLES;