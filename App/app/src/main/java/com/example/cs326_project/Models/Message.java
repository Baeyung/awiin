package com.example.cs326_project.Models;

import com.google.firebase.firestore.Exclude;
import com.stfalcon.chatkit.commons.models.IMessage;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message implements IMessage, Serializable, Comparable<Message>{

    private String text;
    private Author author;
    private String id;
    private Date createdAt;

    public  Message(){
        //empty constructor
    }

    @Exclude
    public int compareTo(Message o) {
        return this.createdAt.compareTo(o.getCreatedAt());
    }

    public Message(String id, Author author, String text, Date createdAt) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.createdAt = createdAt;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

   @Exclude
    public Author getUser() {
        return author;
    }

    public Author getAuthor() { //NOT getUser()
        return author;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

@Exclude
    public Map<String,Object> hashMap(){

        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("text",text);
        hashMap.put("author",author);
        hashMap.put("createdAt",createdAt);
        return  hashMap;

    }
}

