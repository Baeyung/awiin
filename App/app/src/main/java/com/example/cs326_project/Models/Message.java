package com.example.cs326_project.Models;

import com.stfalcon.chatkit.commons.models.IMessage;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message implements IMessage, Serializable {

    private String msgText;
    private Author user;
    private String id;
    private Date createdAt;

    public Message(String id, Author usr, String text, Date createdAt) {
        this.id = id;
        this.msgText = text;
        this.user = usr;
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

    @Override
    public Author getUser() {
        return user;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setText(String msg){
        this.msgText=msg;
    }

    public Map<String,Object> hashMap(){

        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("msgText",msgText);
        hashMap.put("user",user);
        hashMap.put("createdAt",createdAt);

        return  hashMap;

    }
}

