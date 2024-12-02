package com.hunghq.librarymanagement.Model.Entity;

import com.hunghq.librarymanagement.Model.Enum.EIsDeleted;

public class Document {

    private String documentId;
    private String title;
    private String author;
    private double rating;
    private String description;
    private String language;
    private String isbn;
    private String genre;
    private int quantity;
    private String publisher;
    private String publishedDate;
    private String award;
    private int numRatings;
    private String coverImg;

    public Document() {
        ;
    }

    public Document(String documentId, String title, String author, double rating,
                    String description, String language, String isbn, String genre, int quantity, String publisher,
                    String publishedDate, String award, int numRatings, String coverImg) {
        this.documentId = documentId;
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.description = description;
        this.language = language;
        this.isbn = isbn;
        this.genre = genre;
        this.quantity = quantity;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.award = award;
        this.numRatings = numRatings;
        this.coverImg = coverImg;
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String authorName) {
        this.author = authorName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }


    public String toString() {
        return "Document[" +
                "documentId=" + documentId +
                ", title=" + title +
                ", authorName=" + author +
                ", rating=" + rating +
                ", genre=" + genre +
                ", quantity=" + quantity +
                ", language=" + language +
                ", description=" + description +
                ", numRatings=" + numRatings +
                ", publisher=" + publisher +
                ", isbn=" + isbn +
                ", publishedDate=" + publishedDate +
                ", award=" + award +
                ", coverImg=" + coverImg +
                "]";
    }

}
