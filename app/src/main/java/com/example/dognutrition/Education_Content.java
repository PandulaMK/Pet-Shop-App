package com.example.dognutrition;

public class Education_Content {
    private String title;
    private String description;
    private String imageUrl; // Add this field

    public Education_Content() {
    }

    public Education_Content(String title, String description, String imageUrl) { // Modify constructor
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl; // Initialize this field
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
