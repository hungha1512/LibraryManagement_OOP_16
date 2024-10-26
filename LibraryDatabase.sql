CREATE DATABASE libraryManagementOfficial;
USE libraryManagementOfficial;

CREATE TABLE authors (
    authorId INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    birthDate DATE,
    nationality VARCHAR(50),
    biography TEXT
);

CREATE TABLE categories (
    categoryId INT PRIMARY KEY,
    categoryName VARCHAR(225) NOT NULL UNIQUE
);

CREATE TABLE documents (
    documentId INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(225) NOT NULL,
    authorId INT,
    categoryId INT,
    publisher VARCHAR(225),
    isbn VARCHAR(225) UNIQUE,
    yearPublished INT,
    quantity INT NOT NULL,
    FOREIGN KEY (authorId) REFERENCES authors(authorId),
    FOREIGN KEY (categoryId) REFERENCES categories(categoryId)
);

CREATE TABLE users (
    userId INT PRIMARY KEY AUTO_INCREMENT,
    userName VARCHAR(225) NOT NULL,
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



