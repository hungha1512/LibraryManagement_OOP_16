CREATE DATABASE libraryManagement;
USE libraryManagement;

CREATE TABLE documents (
   documentId INT PRIMARY KEY,
   title VARCHAR(225) NOT NULL,
   author VARCHAR(225),
   category VARCHAR(225),
   publisher VARCHAR(225),
   isbn VARCHAR(225),
   yearPublished INT, 
   quantity INT
);

CREATE TABLE users (
   userId INT PRIMARY KEY,
   userName VARCHAR(225) NOT NULL,
   email VARCHAR(225), 
   phone VARCHAR(225),
   joinDate DATE,
   dateOfBirth DATE
);

CREATE TABLE borrowDocuments (
   documentId INT,
   userId INT,
   borrowDate DATE,
   dueDate DATE,
   returnDate DATE,
   state VARCHAR(50), 
   FOREIGN KEY (documentId) REFERENCES documents(documentId),
   FOREIGN KEY (userId) REFERENCES users(userId)
);
CREATE TABLE reviews (
   reviewId INT PRIMARY KEY AUTO_INCREMENT,
   documentId INT,
   userID INT,
   rating INT CHECK (rating BETWEEN 1 AND 5),
   reviewText TEXT,
   reviewDate DATE,
   FOREIGN KEY (documentId) REFERENCES documents(documentId),
   FOREIGN KEY (userId) REFERENCES users(userId)
)


