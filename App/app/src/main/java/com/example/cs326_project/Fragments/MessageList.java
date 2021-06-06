package com.example.cs326_project.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cs326_project.Models.Author;
import com.example.cs326_project.Models.Dialog;
import com.example.cs326_project.Models.Message;
import com.example.cs326_project.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

//additional


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageList extends Fragment {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Dialog mParam1;
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
    public static MessageList newInstance(Dialog param1, String param2) {
        MessageList fragment = new MessageList();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Dialog) getArguments().getSerializable(ARG_PARAM1);
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
        final DocumentReference docRef = firestore.collection("chats").document(mParam1.getFirebase_id());

        MessagesList messageListView = view.findViewById(R.id.messagesList);

        MessagesListAdapter messagesListAdapter = new MessagesListAdapter(current_user.getUid(),new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                Picasso.get().load(url).into(imageView);
            }
        });

        messagesListAdapter.addToEnd(mParam1.getMessages(),true);


        messageListView.setAdapter(messagesListAdapter);

        MessageInput inputView = view.findViewById(R.id.input);

        inputView.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                //select attachments
                messagesListAdapter.addToStart(mParam1.getLastMessage(), true);
            }
        });

        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                //validate and send message
                //DocumentReference dialogRef = firestore.collection("chats").document(mParam1.get);

                Author user = new Author(current_user.getUid(),current_user.getDisplayName(),"https://randomuser.me/api/portraits/men/3.jpg");
                Message temp = new Message(input.toString(),user,input.toString(), new Date());
                docRef.update("messages", FieldValue.arrayUnion(temp));
                messagesListAdapter.addToStart(temp, true);
                return true;
            }
        });

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    if (snapshot.getData() != null) {
                        Dialog updDialog = snapshot.toObject(Dialog.class);
                        mParam1.update(updDialog,snapshot.getId());
                        messagesListAdapter.clear();
                        messagesListAdapter.addToEnd(mParam1.getMessages(),true);
                    }
                } else {

                }
            }
        });
    }
}