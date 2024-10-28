package com.hunghq.librarymanagement.Model.Entity;

import java.time.LocalDateTime;

public class BorrowDocument {

    private int borrowId;
    private Document document;
    private User user;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private State state;

    private enum State {
        BORROWED,
        RETURNED,
        OVERDUE
    };

    public int getBorrowId() {
        return this.borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
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

    public LocalDateTime getBorrowDate() {
        return this.borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnDate() {
        return this.returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }
    
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public BorrowDocument(State state) {
        this.state = state;
    }

    public BorrowDocument() {
        
    }

    public BorrowDocument(int borrowId, Document document, User user,
    LocalDateTime borrowDate, LocalDateTime dueDate, LocalDateTime returnDate, State state) {
        this.borrowId = borrowId;
        this.document = document;
        this.user = user;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.state = state;
    }

    public String toString() {
        return "BorrowDocument[borrowId=" + borrowId
        + ",documentId=" + document.getDocumentId()
        + ",userId=" + user.getUserId()
        + ",borrowDate=" + borrowDate
        + ",dueDate=" + dueDate
        + ",returnDate=" + returnDate
        + ",state=" + state
        + "]";
    }
}
