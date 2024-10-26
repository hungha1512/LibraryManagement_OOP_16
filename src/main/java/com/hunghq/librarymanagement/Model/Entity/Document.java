package com.hunghq.librarymanagement.Model.Entity;

abstract class Document {
    protected String id;
    protected String title;
    protected String author;
    protected int quantity;
    protected String qrCode;

    public Document() {
        ;
    }

    public Document(String id, String title, String author, int quantity, String qrCode) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.qrCode = qrCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public abstract void printInfo();
}
