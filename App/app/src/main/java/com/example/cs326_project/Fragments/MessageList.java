package com.example.cs326_project.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cs326_project.FcmNotificationsSender;
import com.example.cs326_project.MainActivity;
import com.example.cs326_project.Models.Author;
import com.example.cs326_project.Models.Dialog;
import com.example.cs326_project.Models.Message;
import com.example.cs326_project.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

//additional


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
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
    int InitialMessages;
    ListenerRegistration msg_registration;
    int counter=1;

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
    public void onDestroyView() {
        super.onDestroyView();

        MainActivity mainActivity = (MainActivity)MessageList.this.getActivity();
        mainActivity.refreshDialogList();
        msg_registration.remove();
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
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //InitialMessages= sharedPref.getInt(mParam1.getFirebase_id(), 1);

        SharedPreferences.Editor editor = sharedPref.edit();

        MessagesList messageListView = view.findViewById(R.id.messagesList);

        MessagesListAdapter messagesListAdapter = new MessagesListAdapter(current_user.getUid(),new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                Picasso.get().load(url).into(imageView);
            }
        });

        docRef.collection("messages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    QuerySnapshot document = task.getResult();
                    if (document!=null) {

                        List<Message> msgs =document.toObjects(Message.class);
                        Collections.sort(msgs,Collections.reverseOrder());
                        messagesListAdapter.addToEnd(msgs,false);


                    } else {

                    }
                } else {

                }

            }
        });




        messageListView.setAdapter(messagesListAdapter);

        MessageInput inputView = view.findViewById(R.id.input);

        inputView.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                //select attachments
                messagesListAdapter.addToStart(mParam1.getLastMessage(), false);
            }
        });

        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                //validate and send message
                //DocumentReference dialogRef = firestore.collection("chats").document(mParam1.get);

                Author user = new Author(current_user.getUid(),current_user.getDisplayName(),"https://ui-avatars.com/api/?name="+current_user.getDisplayName());
                Message temp = new Message(input.toString(),user,input.toString(), new Date());
                FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender("/topics/notify","New Message",input.toString(),getContext(),getActivity());
                fcmNotificationsSender.SendNotifications();
                CollectionReference messageRef = firestore
                        .collection("chats").document(docRef.getId())
                        .collection("messages");


                docRef.update("lastMessage",temp);
                docRef.update("total_messages",FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(mParam1.getFirebase_id(), mParam1.getTotal_messages()+1);
                        editor.apply();
                    }
                });

                messageRef.add(temp.hashMap());
                messagesListAdapter.addToStart(temp, true);
                        // Success


                /*messageRef.add(temp.hashMap()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(mParam1.getFirebase_id(), mParam1.getTotal_messages()+1);
                        editor.apply();

                        docRef.update("total_messages",FieldValue.increment(1));

                    }
                });*/

                //docRef.update("total_messages",FieldValue.increment(1));
                return true;
            }
        });

        msg_registration=docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";

                if(source=="Local")
                    Toast.makeText(getContext(), "local", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "server", Toast.LENGTH_SHORT).show();


                if (snapshot != null && snapshot.exists()) {
                    if (snapshot.getData() != null) {
                        Dialog updDialog = snapshot.toObject(Dialog.class);

                        mParam1.update(updDialog,snapshot.getId());
                        counter=1;

                        docRef.collection("messages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    QuerySnapshot document = task.getResult();
                                    if (document!=null) {

                                        List<Message> msgs =document.toObjects(Message.class);
                                        messagesListAdapter.clear();
                                        Collections.sort(msgs,Collections.reverseOrder());
                                        messagesListAdapter.addToEnd(msgs,false);

                                    } else {

                                    }
                                } else {

                                }
                            }
                        });

                        Toast.makeText(getContext(), "QQQQQQQQQQQQ", Toast.LENGTH_SHORT).show();
                        //SharedPreferences.Editor editor = sharedPref.edit();
                        //editor.putInt(mParam1.getFirebase_id(), mParam1.getTotal_messages());
                        //editor.apply();
                    }
                } else {

                }
            }
        });
    }
}