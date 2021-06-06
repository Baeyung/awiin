package com.example.cs326_project.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cs326_project.Models.Message;
import com.example.cs326_project.R;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

//additional
import com.example.cs326_project.Misc.dfix;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MessageList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageList.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageList newInstance(String param1, String param2) {
        MessageList fragment = new MessageList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MessagesList messageListView = view.findViewById(R.id.messagesList);

        MessagesListAdapter messagesListAdapter = new MessagesListAdapter("1",new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                Picasso.get().load(url).into(imageView);
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        calendar.add(Calendar.MINUTE,1);

        List<Message> what = new ArrayList<Message>();;

        what.add(dfix.getMessage(calendar.getTime()));

        messagesListAdapter.addToEnd(what,true);


        messageListView.setAdapter(messagesListAdapter);

        MessageInput inputView = view.findViewById(R.id.input);

        inputView.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                //select attachments
                messagesListAdapter.addToStart(what.get(0), true);
            }
        });

        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                //validate and send message
                messagesListAdapter.addToStart(what.get(0), true);
                return true;
            }
        });

    }
}