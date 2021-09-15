package goRest.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Comment {
    private int id;
    private int postid;
    private String name;
    private String email;
    private String body;



    public int getId() {
        return id;
    }

    public int getPostid() {
        return postid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBody() {
        return body;
    }

    public void setId(int id) {
        this.id = id;
    }
    @JsonProperty("post_id")
    public void setPostid(int postid) {
        this.postid = postid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postid=" + postid +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
