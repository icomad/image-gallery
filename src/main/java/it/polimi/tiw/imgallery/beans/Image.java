package it.polimi.tiw.imgallery.beans;

import java.util.Date;

public class Image {
    private int id;
    private String title;
    private String description;
    private String path;
    private Date createdAt;
    private int userId;
    private int albumId;

    public Image() { }

    public Image(int id, String title, String description, String path, Date createdAt, int userId, int albumId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.path = path;
        this.createdAt = createdAt;
        this.userId = userId;
        this.albumId = albumId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }
}
