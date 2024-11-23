package com.hunghq.librarymanagement.Model.Entity;

import java.time.LocalDateTime;

public class Review {
    
    private int reviewId;
    private Document document;
    private User user;
    private double rating;
    private String reviewText;
    private LocalDateTime reviewDate;

    public int getReviewId() {
        return this.reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getRating() {
        return this.rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return this.reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public LocalDateTime getReviewDate() {
        return this.reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Review() {
        
    }

    public Review(int reviewId, Document document, User user, double rating,
    String reviewText, LocalDateTime reviewDate) {
        this.reviewId = reviewId;
        this.document = document;
        this.user = user;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
    }

    public String toString() {
        return "Review[reviewId=" + reviewId
        + ", document=" + document
        + ", user=" + user
        + ", rating=" + rating
        + ", reviewText=" + reviewText
        + ", reviewDate=" + reviewDate
        + "]";
    }




}
