DROP TABLE IF EXISTS `tbl_employee`;

CREATE TABLE `tbl_employee` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `lastName` varchar(10) DEFAULT NULL,
    `email` varchar(50) DEFAULT NULL,
    `gender` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `tbl_employee` VALUES('1', 'peter', 'peteryuanpan@gmail.com', 'male');
