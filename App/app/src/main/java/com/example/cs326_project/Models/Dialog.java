package com.example.cs326_project.Models;


import android.widget.TextView;

import com.google.firebase.firestore.Exclude;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dialog implements IDialog {
    private Message lastMessage;
    private String id;
    private String dialogPhoto;
    private String dialogName;
    private ArrayList<Author> users;
    private int unreadCount;


    /*...*/
    public Dialog(){

    }

    public Dialog(String id, String name, String photo,
                         ArrayList<Author> users, Message lastMessage, int unreadCount) {

        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.users = new ArrayList<Author>();
        for (int i=0;i< users.size();i++)
        {
            this.users.add(users.get(i));
        }
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
        return users;
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
        hashMap.put("dialogName", dialogName);
        hashMap.put("dialogPhoto", dialogPhoto);
        hashMap.put("unreadCount", unreadCount);

        return hashMap;
    }
}
