package com.example.cs326_project;

public class chats {

    private String dialogName;
    private String dialogPhoto;
    private String id;
    private int unreadCount;
    private class lastMessage{
        private String createdAt;
        private String id;
        private String text;
        private class author{
            private String avatar;
            private String id;
            private String name;

            public author(){
                //empty constructor
            }

            public author(String avatar,String id,String name){
                this.name = name;
                this.id = id;
                this.avatar=avatar;
            }

            public String getAvatar() {
                return avatar;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        }

        public lastMessage(){
            //empty constructor
        }

        public lastMessage(String createdAt,String id,String text){
            this.createdAt=createdAt;
            this.id=id;
            this.text=text;
        }

        public String getId() {
            return id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getText() {
            return text;
        }
    }

    public chats(){
        //empty constructor
    }
    public chats(String dialogName,String id,String dialogPhoto,int unreadCount){
        this.dialogName=dialogName;
        this.dialogPhoto=dialogPhoto;
        this.id=id;
        this.unreadCount=unreadCount;
    }

    public String getDialogName() {
        return dialogName;
    }

    public String getId() {
        return id;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public String getDialogPhoto() {
        return dialogPhoto;
    }
}
