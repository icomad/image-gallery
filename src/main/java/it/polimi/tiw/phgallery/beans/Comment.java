package it.polimi.tiw.phgallery.beans;

public class Comment {
    private int id;
    private String body;
    private int userId;
    private int imageId;

    public Comment() { }

    public Comment(int id, String body, int userId, int imageId) {
        this.id = id;
        this.body = body;
        this.userId = userId;
        this.imageId = imageId;
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
}
