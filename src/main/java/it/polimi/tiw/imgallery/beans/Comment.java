package it.polimi.tiw.imgallery.beans;

import java.util.Date;

public class Comment {
    private int id;
    private String body;
    private int userId;
    private int imageId;
    private Date createdAt;

    public Comment() { }

    public Comment(int id, String body, int userId, int imageId, Date createdAt) {
        this.id = id;
        this.body = body;
        this.userId = userId;
        this.imageId = imageId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
