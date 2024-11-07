CREATE DATABASE IF NOT EXISTS `lmsoopcsag`;
USE `lmsoopcsag`;

CREATE TABLE `roles` (
  `roleId` varchar(19) NOT NULL,
  `title` varchar(225) NOT NULL,
  `slug` varchar(225) NOT NULL,
  `description` text,
  `createdAt` datetime NOT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `content` text,
  `isDeleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `permisionId` varchar(19) NOT NULL,
  `title` varchar(225) NOT NULL,
  `slug` varchar(225) NOT NULL,
  `description` text,
  `createdAt` datetime NOT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `content` text,
  `isDeleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`permisionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `documents`;
CREATE TABLE `documents` (
  `documentId` varchar(19) NOT NULL,
  `title` varchar(225) NOT NULL,
  `author` text,
  `rating` double DEFAULT NULL,
  `description` text,
  `language` varchar(30) NOT NULL,
  `isbn` varchar(225) DEFAULT NULL,
  `genre` varchar(225) DEFAULT NULL,
  `publisher` varchar(225) DEFAULT NULL,
  `publishedDate` varchar(100) NOT NULL,
  `award` text,
  `numRatings` int DEFAULT NULL,
  `coverImg` varchar(225) DEFAULT NULL,
  PRIMARY KEY (`documentId`),
  UNIQUE KEY `isbn` (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tạo bảng phụ thuộc vào các bảng trên
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `userId` varchar(19) NOT NULL,
  `fullName` varchar(225) NOT NULL,
  `userName` varchar(225) NOT NULL,
  `passwordHash` varchar(255) NOT NULL,
  `gender` ENUM('Male', 'Female') NOT NULL,
  `email` varchar(225) DEFAULT NULL,
  `phone` varchar(225) DEFAULT NULL,
  `joinDate` date DEFAULT NULL,
  `dateOfBirth` date DEFAULT NULL,
  `imagePath` varchar(225),
  `roleId` varchar(19),
  `otp` varchar(6),
  PRIMARY KEY (`userId`),
  UNIQUE KEY `userName` (`userName`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`roleId`) REFERENCES `roles` (`roleId`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `borrowdocuments`;
CREATE TABLE `borrowdocuments` (
  `borrowId` varchar(19) NOT NULL,
  `documentId` varchar(19) DEFAULT NULL,
  `userId` varchar(19) DEFAULT NULL,
  `borrowDate` date DEFAULT NULL,
  `dueDate` date DEFAULT NULL,
  `returnDate` date DEFAULT NULL,
  `state` enum('Borrowed', 'Returned', 'Overdue') DEFAULT 'Borrowed',
  PRIMARY KEY (`borrowId`),
  UNIQUE KEY `documentId_userId` (`documentId`, `userId`),
  KEY `userId` (`userId`),
  CONSTRAINT `borrowdocuments_ibfk_1` FOREIGN KEY (`documentId`) REFERENCES `documents` (`documentId`) ON DELETE CASCADE,
  CONSTRAINT `borrowdocuments_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `reviews`;
CREATE TABLE `reviews` (
  `reviewId` varchar(19) NOT NULL,
  `documentId` varchar(19) DEFAULT NULL,
  `userID` varchar(19) DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `reviewText` text,
  `reviewDate` date DEFAULT NULL,
  PRIMARY KEY (`reviewId`),
  KEY `documentId` (`documentId`),
  KEY `userID` (`userID`),
  CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`documentId`) REFERENCES `documents` (`documentId`) ON DELETE CASCADE,
  CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`userID`) REFERENCES `users` (`userId`) ON DELETE CASCADE,
  CONSTRAINT `reviews_chk_1` CHECK (`rating` BETWEEN 1 AND 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `rolepermision`;
CREATE TABLE `rolepermision` (
  `roleId` varchar(19) NOT NULL,
  `permisionId` varchar(19) NOT NULL,
  `createdAt` datetime NOT NULL,
  `updatedAt` datetime NOT NULL,
  KEY `roleId` (`roleId`),
  KEY `permisionId` (`permisionId`),
  CONSTRAINT `rolepermision_ibfk_1` FOREIGN KEY (`roleId`) REFERENCES `roles` (`roleId`),
  CONSTRAINT `rolepermision_ibfk_2` FOREIGN KEY (`permisionId`) REFERENCES `permissions` (`permisionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
