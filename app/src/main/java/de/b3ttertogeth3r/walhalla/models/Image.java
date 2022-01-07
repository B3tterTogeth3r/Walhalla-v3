package de.b3ttertogeth3r.walhalla.models;

import com.google.firebase.firestore.Exclude;

public class Image {
    @Exclude
    private String id;
    private String description;
    private String icon_path;
    private String large_path;

    public Image () {
    }

    public Image (String description, String icon_path, String large_path) {
        this.description = description;
        this.icon_path = icon_path;
        this.large_path = large_path;
    }

    @Exclude
    public String getId () {
        return id;
    }

    @Exclude
    public void setId (String id) {
        this.id = id;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getIcon_path () {
        return icon_path;
    }

    public void setIcon_path (String icon_path) {
        this.icon_path = icon_path;
    }

    public String getLarge_path () {
        return large_path;
    }

    public void setLarge_path (String large_path) {
        this.large_path = large_path;
    }
}
