CREATE TABLE `augment` (
  `id_augument` int NOT NULL AUTO_INCREMENT,
  `augment_text` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin,
  `power` int NOT NULL,
  PRIMARY KEY (`id_augument`),
  KEY `power_Id_idx` (`power`),
  CONSTRAINT `power_Id` FOREIGN KEY (`power`) REFERENCES `power` (`id_power`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3844 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;


CREATE TABLE `class` (
  `id_class` int NOT NULL AUTO_INCREMENT,
  `class_name` varchar(45) COLLATE utf8mb3_bin DEFAULT NULL,
  `manual` varchar(45) COLLATE utf8mb3_bin DEFAULT NULL,
  PRIMARY KEY (`id_class`),
  UNIQUE KEY `class_name_UNIQUE` (`class_name`)
) ENGINE=InnoDB AUTO_INCREMENT=216 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;


CREATE TABLE `class_level` (
  `class_id` int NOT NULL,
  `level_id` int NOT NULL,
  PRIMARY KEY (`class_id`,`level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;


CREATE TABLE `level` (
  `id_level` int NOT NULL,
  `level` int DEFAULT NULL,
  PRIMARY KEY (`id_level`),
  UNIQUE KEY `idLevel_UNIQUE` (`id_level`),
  UNIQUE KEY `level_UNIQUE` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;


CREATE TABLE `power` (
  `id_power` int NOT NULL AUTO_INCREMENT,
  `power_name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `power_cost` int DEFAULT NULL,
  `power_description` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin,
  `power_class` varchar(45) COLLATE utf8mb3_bin DEFAULT NULL,
  `is_augmentable` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id_power`)
) ENGINE=InnoDB AUTO_INCREMENT=5720 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;


CREATE TABLE `power_class` (
  `power_id` int NOT NULL,
  `class_level_id` varchar(45) COLLATE utf8mb3_bin NOT NULL,
  PRIMARY KEY (`power_id`,`class_level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
