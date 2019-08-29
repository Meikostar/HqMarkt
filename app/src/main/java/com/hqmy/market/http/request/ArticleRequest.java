package com.hqmy.market.http.request;

public class ArticleRequest extends BaseRequestModel{
    private String comment;
    private String object;
    private String id  ;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
