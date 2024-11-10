package com.hunghq.librarymanagement.Model.Entity;

import java.time.LocalDateTime;
import java.util.Optional;

import com.hunghq.librarymanagement.Model.Enum.State;


public class BorrowDocument {

    private String borrowId;
    private Document document;
    private User user;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private State state;

    public String getBorrowId() {
        return this.borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Optional<Document> document) {
        this.document = document.orElse(null);
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(Optional<User> user) {
        this.user = user.orElse(null);
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
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public BorrowDocument() {
        
    }

    public BorrowDocument(String borrowId, Document document, User user,
    LocalDateTime borrowDate, LocalDateTime dueDate, LocalDateTime returnDate, State state) {
        this.borrowId = borrowId;
        this.document = document;
        this.user = user;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.state = state;
    } 

    @Override
    public String toString() {
        return "BorrowDocument[borrowId=" + borrowId
        + ", document=" + document
        + ", user=" + user
        + ", borrowDate=" + borrowDate
        + ", dueDate=" + dueDate
        + ", returnDate=" + returnDate
        + ", state=" + state
        + "]";
    }
}
