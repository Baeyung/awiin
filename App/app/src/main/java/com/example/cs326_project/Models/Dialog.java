package com.example.cs326_project.Models;


import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dialog implements IDialog {
    private IMessage lastMessage;
    private String id;
    private String dialogPhoto;
    private String dialogName;
    private ArrayList<Author> users;
    private int unreadCount;


    /*...*/
    public Dialog(String id, String name, String photo,
                         ArrayList<Author> users, IMessage lastMessages, int unreadCount) {

        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.users = users;
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

    @Override
    public ArrayList<Author> getUsers() {
        return users;
    }

    @Override
    public IMessage getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(IMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    public Map<String,Object> hashMap() {

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("lastMessage", lastMessage);
        hashMap.put("id", id);
        hashMap.put("users",users);
        hashMap.put("dialogName", dialogName);
        hashMap.put("dialogPhoto", dialogPhoto);
        hashMap.put("unreadCount", unreadCount);

        return hashMap;
    }
}
