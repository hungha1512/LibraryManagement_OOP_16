package com.hunghq.librarymanagement.Model.Entity;

import java.time.LocalDateTime;
public class User {

    private String userId;
    private String fullName;
    private String userName;
    private String passwordHash;
    private String email;
    private String phone;
    private LocalDateTime joinDate;
    private LocalDateTime dateOfBirth;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getpasswordHash() {
        return this.passwordHash;
    }

    public void setpasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getJoinDate() {
        return this.joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public LocalDateTime getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public User() {;}

    public User(String userId, String fullName, String userName, String passwordHash
    , String email, String phone, LocalDateTime joinDate, LocalDateTime dateOfBirth) {
        this.userId = userId;
        this.fullName = fullName;
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.email = email; 
        this.phone = phone;
        this.joinDate = joinDate;
        this.dateOfBirth = dateOfBirth;
    }

    public String toString() {
        return "User[userId=" + userId
        + ", fullName=" + fullName
        + ", userName=" + userName
        + ", passwordHash=" + passwordHash
        + ", email=" + email
        + ", phone=" + phone
        + ", joinDate=" + joinDate
        + ", dateOfBirth=" + dateOfBirth
        + "]"; 
    }

}
