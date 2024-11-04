package com.hunghq.librarymanagement.Model.Entity;

import java.time.LocalDateTime;

public class Author {

    private int authorId;
    private String name;
    private LocalDateTime birthDate;
    private String nationality;
    private String biography;

    public int getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getNationality() {
        return this.nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBiography() {
        return this.biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Author() {
        
    }

    public Author(int authorId, String name, LocalDateTime birthDate,
                  String nationality, String biography) {
        this.authorId = authorId;
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.biography = biography;
    }

    public String toString() {
        return "Author[authorId=" + authorId
        + ",name=" + name
        + ",birthDate=" + birthDate
        + ",nationality=" + nationality
        + ",biography=" + biography
        + "]";
    }

}
