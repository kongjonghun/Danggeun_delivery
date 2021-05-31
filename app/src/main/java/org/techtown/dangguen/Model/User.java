package org.techtown.dangguen.Model;

public class User {

    private String id;
    private String username;
    private String imageURL;
    private String x;
    private String y;


    public User(String id, String username, String imageURL, String status, String x, String y){
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public User() {
    }


}
