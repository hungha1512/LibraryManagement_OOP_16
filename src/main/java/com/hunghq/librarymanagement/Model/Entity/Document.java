package com.hunghq.librarymanagement.Model.Entity;

public class Document {

    private String documentId;
    private String title;
    private String authorName;
    private double rating;
    private String genre;
    private int quantity;
    private String language;
    private String description;
    private int numRatings;
    private String publisher;
    private String isbn;
    private String publishedDate;
    private String award;
    private String coverImg;

    public Document() {
        ;
    }

    public Document(String documentId, String title, String authorName, double rating,
                    String genre, int quantity, String language, String description, int numRatings, String publisher,
                    String isbn, String publishedDate, String award, String coverImg) {
        this.documentId = documentId;
        this.title = title;
        this.authorName = authorName;
        this.rating = rating;
        this.genre = genre;
        this.quantity = quantity;
        this.language = language;
        this.description = description;
        this.numRatings = numRatings;
        this.publisher = publisher;
        this.isbn = isbn;
        this.publishedDate = publishedDate;
        this.award = award;
        this.coverImg = coverImg;
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public double getRating() {
        return this.rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return this.genre;
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

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumRatings() {
        return this.numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
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

    public String getPublishedDate() {
        return this.publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getAward() {
        return this.award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getCoverImg() {
        return this.coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }


    public String toString() {
        return "Document[" +
                "documentId=" + documentId +
                ", title=" + title +
                ", authorName=" + authorName +
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
