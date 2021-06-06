package com.example.cs326_project.Models;

import com.google.firebase.firestore.Exclude;
import com.stfalcon.chatkit.commons.models.IMessage;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message implements IMessage, Serializable {

    private String msgText;
    private Author author;
    private String id;
    private Date createdAt;

    public Message(String id, Author author, String text, Date createdAt) {
        this.id = id;
        this.msgText = text;
        this.author = author;
        this.createdAt = createdAt;
    }

    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return msgText;
    }

    @Exclude
    public Author getUser() {
        return author;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    public Map<String,Object> hashMap(){

        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("msgText",msgText);
        hashMap.put("author",author);
        hashMap.put("createdAt",createdAt);

        return  hashMap;

    }
}

