package com.example.cs326_project.Misc;

import com.example.cs326_project.Models.Author;
import com.example.cs326_project.Models.Dialog;
import com.example.cs326_project.Models.Message;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public final class dfix extends fixd {
    private dfix() {
        throw new AssertionError();
    }

    public static ArrayList<Dialog> getDialogs() {
        ArrayList<Dialog> chats = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -(i * i));
            calendar.add(Calendar.MINUTE, -(i * i));

            chats.add(getDialog(i, calendar.getTime()));
        }

        return chats;
    }

    private static Dialog getDialog(int i, Date lastMessageCreatedAt) {
        ArrayList<Author> users = getUsers();
        return new Dialog(
                getRandomId(),
                users.size() > 1 ? groupChatTitles.get(users.size() - 2) : users.get(0).getName(),
                users.size() > 1 ? groupChatImages.get(users.size() - 2) : getRandomAvatar(),
                users,
                getMessage(lastMessageCreatedAt),
                i < 3 ? 3 - i : 0);
    }

    private static ArrayList<Author> getUsers() {
        ArrayList<Author> users = new ArrayList<>();
        int usersCount = 1 + rnd.nextInt(4);

        for (int i = 0; i < usersCount; i++) {
            users.add(getUser());
        }

        return users;
    }

    private static Author getUser() {
        return new Author(
                getRandomId(),
                getRandomName(),
                getRandomAvatar());
    }

    public static Message getMessage(final Date date) {
        return new Message(
                getRandomId(),
                getUser(),
                getRandomMessage(),
                date);
    }
}
