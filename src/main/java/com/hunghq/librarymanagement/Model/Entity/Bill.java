package com.hunghq.librarymanagement.Model.Entity;

import java.sql.Timestamp;

public class Bill {

    private int billId;
    private Document document;
    private User user;
    private Timestamp timeBorrow;
    private Timestamp timeReturn;
    private double latelyFee;
    private double costPerDayLate;

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getTimeBorrow() {
        return timeBorrow;
    }

    public void setTimeBorrow(Timestamp timeBorrow) {
        this.timeBorrow = timeBorrow;
    }

    public Timestamp getTimeReturn() {
        return timeReturn;
    }

    public void setTimeReturn(Timestamp timeReturn) {
        this.timeReturn = timeReturn;
    }

    public double getLatelyFee() {
        return latelyFee;
    }

    public void setLatelyFee(double latelyFee) {
        this.latelyFee = latelyFee;
    }

    public double getCostPerDayLate() {
        return costPerDayLate;
    }

    public void setCostPerDayLate(double costPerDayLate) {
        this.costPerDayLate = costPerDayLate;
    }

    public Bill() {

    }
    public Bill(int billId, Document document, User user, Timestamp timeBorrow, Timestamp timeReturn,
                double latelyFee, double costPerDayLate) {
        this.billId = billId;
        this.document = document;
        this.user = user;
        this.timeBorrow = timeBorrow;
        this.timeReturn = timeReturn;
        this.latelyFee = latelyFee;
        this.costPerDayLate = costPerDayLate;
    }

    public String toString() {
        return "Bill[billId=" + billId
                + ", document" + document
                + ", user" + user
                + ", timeBorrow=" + timeBorrow
                + ", timeReturn=" + timeReturn
                + ", latelyFee=" + latelyFee
                + ", costPerDayLate=" + costPerDayLate
                + "]";
    }



}
