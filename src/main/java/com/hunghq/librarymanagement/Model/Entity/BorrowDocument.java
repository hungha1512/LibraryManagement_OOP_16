package com.hunghq.librarymanagement.Model.Entity;

import java.time.LocalDateTime;
import java.util.Optional;

import com.hunghq.librarymanagement.Model.Enum.EState;


public class BorrowDocument {

    private String borrowId;
    private Document document;
    private User user;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private EState eState;

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

    public EState getState() {
        return this.eState;
    }

    public void setState(EState eState) {
        this.eState = eState;
    }

    public BorrowDocument() {
        
    }

    public BorrowDocument(String borrowId, Document document, User user,
    LocalDateTime borrowDate, LocalDateTime dueDate, LocalDateTime returnDate, EState eState) {
        this.borrowId = borrowId;
        this.document = document;
        this.user = user;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.eState = eState;
    } 

    @Override
    public String toString() {
        return "BorrowDocument[borrowId=" + borrowId
        + ", document=" + document
        + ", user=" + user
        + ", borrowDate=" + borrowDate
        + ", dueDate=" + dueDate
        + ", returnDate=" + returnDate
        + ", state=" + eState
        + "]";
    }
}
