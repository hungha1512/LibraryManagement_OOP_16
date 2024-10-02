package com.hunghq.librarymanagement.Model.Entity;

public class Book extends Document{
    private String genre;
    private String isbn;

    public Book(String id, String title, String author, int quantity, String genre, String qrCode, String isbn) {
        super(id, title, author, quantity, qrCode);
        this.genre = genre;
        this.isbn = isbn;
    }

    @Override
    public void printInfo() {
        // Show book details
    }
}
