package com.hunghq.librarymanagement.Model.Entity;

public class Document {

    private int documentId;
    private String title;
    private Author author;
    private String publisher;
    private String isbn;
    private int yearPublished;
    private int quantity;

    public int getDocumentId() {
        return this.documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return this.author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getYearPublished() {
        return this.yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    

    public Document() {

    }

    public Document(int documentId, String title, Author author,
                String publisher, String isbn, int yearPublished, int quantity) {
        this.documentId = documentId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.yearPublished = yearPublished;
        this.quantity = quantity;
    }

    public String toString() {
        return "Document[documentId=" + documentId
        + ",title=" + title
        + ",authorId=" + author.getAuthorId()
        + ",publisher=" + publisher
        + ",isbn=" + isbn
        + ",yearPublished=" + yearPublished
        + ",quantity=" + quantity 
        + "]";
    }

}
