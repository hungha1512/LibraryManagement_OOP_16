package com.hunghq.librarymanagement.Model.Entity;

import java.time.LocalDateTime;

import com.hunghq.librarymanagement.Model.Enum.EGender;

public class User {
    private String userId;
    private String fullName;
    private String passwordHash;
    private EGender gender;
    private String email;
    private String phone;
    private LocalDateTime joinDate;
    private LocalDateTime dateOfBirth;
    private String imagePath;
    private Role role;
    private String otp;

    public User() {
        ;
    }

    public User(String userId, String fullName, String passwordHash, EGender gender, String email, String phone,
                LocalDateTime joinDate, LocalDateTime dateOfBirth, String imagePath, Role role, String otp) {
        this.userId = userId;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.joinDate = joinDate;
        this.dateOfBirth = dateOfBirth;
        this.imagePath = imagePath;
        this.role = role;
        this.otp = otp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public EGender getGender() {
        return gender;
    }

    public void setGender(EGender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public String toString() {
        return "User[" +
                "fullName=" + fullName + '\'' +
                ", gender=" + gender +
                ", email=" + email + '\'' +
                ", phone=" + phone + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", role=" + role +
                ']';
    }
}
