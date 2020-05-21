package it.polimi.tiw.imgallery.beans;

import java.util.Date;

public class Album {
    private int id;
    private String title;
    private Date createdAt;
    private int userId;

    public Album() {}

    public Album(int id, String title, Date createdAt, int userId) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.userId = userId;
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
}
