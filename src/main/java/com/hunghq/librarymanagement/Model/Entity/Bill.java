package com.hunghq.librarymanagement.Model.Entity;

import com.hunghq.librarymanagement.Model.Enum.EPaymentStatus;

import java.time.LocalDateTime;

public class Bill {

    private int billId;
    private BorrowDocument borrowDocument;
    private User user;
    private double totalPayment;
    private LocalDateTime creationDate;
    private LocalDateTime paymentDate;
    private EPaymentStatus ePaymentStatus;

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public BorrowDocument getBorrowDocument() {
        return borrowDocument;
    }

    public void setBorrowDocument(BorrowDocument borrowDocument) {
        this.borrowDocument = borrowDocument;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public EPaymentStatus getPaymentStatus() {
        return ePaymentStatus;
    }

    public void setPaymentStatus(EPaymentStatus paymentStatus) {
        this.ePaymentStatus = paymentStatus;
    }

    public Bill(int billId, BorrowDocument borrowDocument, User user,
                double totalPayment, LocalDateTime creationDate,
                LocalDateTime paymentDate, EPaymentStatus paymentStatus) {
        this.billId = billId;
        this.borrowDocument = borrowDocument;
        this.user = user;
        this.totalPayment = totalPayment;
        this.creationDate = creationDate;
        this.paymentDate = paymentDate;
        this.ePaymentStatus = paymentStatus;
    }

    public String toString() {
        return "Bill[billId=" + billId
                + ", borrowDocument=" + borrowDocument
                + ", user=" + user
                + ", totalPayment=" + totalPayment
                + ", creationDate=" + creationDate
                + ", paymentDate=" + paymentDate
                + ", paymentStatus=" + ePaymentStatus
                + "]";
    }
}
