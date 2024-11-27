package com.hunghq.librarymanagement.Model.Entity;

import com.hunghq.librarymanagement.Model.Enum.EState;

import java.time.LocalDateTime;
import java.util.Optional;


public class BorrowDocument {

    private int borrowId;
    private Document document;
    private User user;
    private double fee;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private LocalDateTime extendDate;
    private EState eState;

    public BorrowDocument() {

    }

    public BorrowDocument(int borrowId, Document document, User user,
                          double fee, LocalDateTime borrowDate, LocalDateTime dueDate,
                          LocalDateTime returnDate, LocalDateTime extendDate, EState eState) {
        this.borrowId = borrowId;
        this.document = document;
        this.user = user;
        this.fee = fee;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.extendDate = extendDate;
        this.eState = eState;
    }

    public int getBorrowId() {
        return this.borrowId;
    }

    public void setBorrowId(int borrowId) {
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

    public LocalDateTime getExtendDate() {
        return this.extendDate;
    }

    public void setExtendDate(LocalDateTime extendDate) {
        this.extendDate = extendDate;
    }
    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public EState getState() {
        return this.eState;
    }

    public void setState(EState eState) {
        this.eState = eState;
    }

    @Override
    public String toString() {
        return "BorrowDocument[borrowId=" + borrowId
                + ", document=" + document
                + ", user=" + user
                + ", fee=" + fee
                + ", borrowDate=" + borrowDate
                + ", dueDate=" + dueDate
                + ", returnDate=" + returnDate
                + ", extendDate=" + extendDate
                + ", state=" + eState
                + "]";
    }
}
