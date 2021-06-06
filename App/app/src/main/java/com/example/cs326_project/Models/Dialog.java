package com.example.cs326_project.Models;


import android.widget.TextView;

import com.google.firebase.firestore.Exclude;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dialog implements IDialog, Serializable {
    private Message lastMessage;
    private ArrayList<Message>messages;
    private String id;
    private String dialogPhoto;
    private String dialogName;
    private ArrayList<Author> members;
    private int unreadCount;


    /*...*/
    public Dialog(){

    }

    public Dialog(String id, String name, String photo,
                         ArrayList<Author> users, Message lastMessage, int unreadCount, ArrayList<Message> messages) {

        this.messages=messages;
        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.members=users;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    @Override
    public String getId() {
        return id;
    }
    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Exclude
    public ArrayList<Author> getUsers() {
        return members;
    }

    public ArrayList<Author> getMembers() {
        return members;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    @Override
    public IMessage getLastMessage() {
        return lastMessage;
    }

    @Exclude
    public void setLastMessage(IMessage lastMessage) {
        this.lastMessage = (Message)lastMessage;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    @Exclude
    public Map<String,Object> hashMap() {

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("lastMessage", lastMessage);
        hashMap.put("id", id);
        hashMap.put("members",members);
        hashMap.put("dialogName", dialogName);
        hashMap.put("dialogPhoto", dialogPhoto);
        hashMap.put("unreadCount", unreadCount);
        hashMap.put("messages",messages);

        return hashMap;
    }
}
