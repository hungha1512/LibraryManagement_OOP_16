CREATE DATABASE libraryManagementOop16Official;
USE libraryManagementOop16Official;

CREATE TABLE authors (
    authorId INT PRIMARY KEY AUTO_INCREMENT,
    authorName VARCHAR(100) NOT NULL,
    birthDate DATE,
    nationality VARCHAR(50),
    biography TEXT
);

CREATE TABLE documents (
    documentId INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(225) NOT NULL,
    authorId INT,
    rating INT,
    genre VARCHAR(225),
    `language` VARCHAR(30) NOT NULL,
    `description` TEXT,
    voterAmount INT,
    publisher VARCHAR(225),
    isbn VARCHAR(225) UNIQUE,
    publishedDate DATE NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (authorId) REFERENCES authors(authorId)
);

CREATE TABLE users (
    userId INT PRIMARY KEY AUTO_INCREMENT,
    fullName VARCHAR(225) NOT NULL,
    userName VARCHAR(225) NOT NULL UNIQUE,
    passwordHash VARCHAR(20) NOT NULL,
    email VARCHAR(225) UNIQUE,
    phone VARCHAR(225) UNIQUE,
    joinDate DATE DEFAULT NULL,
    dateOfBirth DATE
);

CREATE TABLE borrowDocuments (
    borrowId INT PRIMARY KEY AUTO_INCREMENT,
    documentId INT,
    userId INT,
    borrowDate DATE,
    dueDate DATE,
    returnDate DATE,
    state ENUM('Borrowed', 'Returned', 'Overdue') DEFAULT 'Borrowed',
    FOREIGN KEY (documentId) REFERENCES documents(documentId) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE,
    UNIQUE (documentId, userId)
);

CREATE TABLE reviews (
    reviewId INT PRIMARY KEY AUTO_INCREMENT,
    documentId INT,
    userID INT,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    reviewText TEXT DEFAULT NULL,
    reviewDate DATE DEFAULT NULL,
    FOREIGN KEY (documentId) REFERENCES documents(documentId) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE
);

CREATE TABLE roles (
    roleId INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(225) NOT NULL,
    slug VARCHAR(225) NOT NULL,
    `description` TEXT NULL,
    createdAt DATETIME NOT NULL,
    updatedAt DATETIME DEFAULT NULL,
    content TEXT DEFAULT NULL,
    isDeleted TINYINT(1) NOT NULL DEFAULT 0
);

CREATE TABLE permissions (
    permisionId INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(225) NOT NULL,
    slug VARCHAR(225) NOT NULL,
    description TEXT NULL,
    createdAt DATETIME NOT NULL,
    updatedAt DATETIME DEFAULT NULL,
    content TEXT DEFAULT NULL,
    isDeleted TINYINT(1) NOT NULL DEFAULT 0
);

CREATE TABLE rolePermision (
    roleId INT NOT NULL,
    permisionId int NOT NULL,
    createdAt DATETIME NOT NULL,
    updatedAt DATETIME NOT NULL,
    FOREIGN KEY (roleId) REFERENCES roles (roleId),
    FOREIGN KEY (permisionId) REFERENCES permissions (permisionId)
);
