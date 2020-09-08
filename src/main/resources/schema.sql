DROP TABLE IF EXISTS `image_details`;

create TABLE `image_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` TEXT NOT NULL,
  `type` varchar(255) NOT NULL,
  `size` BIGINT NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
);
