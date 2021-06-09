package com.example.cs326_project.Models;


import android.widget.TextView;

import com.google.firebase.firestore.Exclude;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Dialog implements IDialog, Serializable, Comparable<Dialog> {
    private Message lastMessage;
    private String id;
    private String OwnerId;
    private String dialogPhoto;
    private String dialogName;
    private ArrayList<Author> members;
    private int unreadCount;
    private String firebase_id;
    private int total_messages;

    /*...*/
    public Dialog(){

    }

    @Exclude
    public int compareTo(Dialog o) {
        return lastMessage.getCreatedAt().compareTo(o.lastMessage.getCreatedAt());
    }

    public Dialog(String id, String name, String photo,
                         ArrayList<Author> users, Message lastMessage, int unreadCount, String Owner, int total_messages) {

        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.members=users;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
        this.OwnerId=Owner;
        this.total_messages=total_messages;

    }

    @Override
    public String getId() {
        return id;
    }
    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public int getTotal_messages() {
        return total_messages;
    }

    @Exclude
    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    @Exclude
    public String getFirebase_id() {
        return firebase_id;
    }

    @Exclude
    public void update(Dialog updatedDialog, String firebase_id ){

        this.id = updatedDialog.id;
        this.dialogName = updatedDialog.dialogName;
        this.dialogPhoto = updatedDialog.dialogPhoto;
        this.members=updatedDialog.members;
        this.lastMessage = updatedDialog.lastMessage;
        this.unreadCount = updatedDialog.unreadCount;
        this.firebase_id=firebase_id;
        this.OwnerId=updatedDialog.OwnerId;
        this.total_messages=updatedDialog.total_messages;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
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
        hashMap.put("total_messages",total_messages);
        hashMap.put("OwnerId", OwnerId);
        return hashMap;
    }
}
