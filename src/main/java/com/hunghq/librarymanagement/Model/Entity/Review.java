package com.hunghq.librarymanagement.Model.Entity;

import java.time.LocalDateTime;

public class Review {
    
<<<<<<< HEAD
    private String reviewId;
=======
    private int reviewId;
>>>>>>> cc31ce91e1f43171ca1bb986dee1f79adab998e0
    private Document document;
    private User user;
    private int rating;
    private String reviewText;
    private LocalDateTime reviewDate;

<<<<<<< HEAD
    public String getReviewId() {
        return this.reviewId;
    }

    public void setReviewId(String reviewId) {
=======
    public int getReviewId() {
        return this.reviewId;
    }

    public void setReviewId(int reviewId) {
>>>>>>> cc31ce91e1f43171ca1bb986dee1f79adab998e0
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

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
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

<<<<<<< HEAD
    public Review(String reviewId, Document document, User user, int rating,
=======
    public Review(int reviewId, Document document, User user, int rating,
>>>>>>> cc31ce91e1f43171ca1bb986dee1f79adab998e0
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
<<<<<<< HEAD
        + ", document=" + document
        + ", user=" + user
        + ", rating=" + rating
        + ", reviewText=" + reviewText
        + ", reviewDate=" + reviewDate
=======
        + ",documentId=" + document.getDocumentId()
        + ",userId=" + user.getUserId()
        + ",rating=" + rating
        + ",reviewText=" + reviewText
        + ",reviewDate=" + reviewDate
>>>>>>> cc31ce91e1f43171ca1bb986dee1f79adab998e0
        + "]";
    }




}
