package com.example.fitapp.Model;

public class Exercise {
    String name;
    String image;
    String description;

    // Constructors
    public Exercise() {
    }

    public Exercise(String name, String image, String description) {
        this.name = name;
        this.image = image;
        this.description = description;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
