package com.example.cs326_project.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.cs326_project.Models.Author;
import com.example.cs326_project.Models.Dialog;

import com.example.cs326_project.MainActivity;

import com.example.cs326_project.Models.DisplayedUserInfo;
import com.example.cs326_project.Models.Message;
import com.example.cs326_project.R;
import com.example.cs326_project.chats;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

//additional
import com.example.cs326_project.Misc.dfix;
import com.stfalcon.chatkit.messages.MessagesList;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogList extends Fragment {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<Author> user1 = new ArrayList<Author>();
    public DialogList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DialogList.
     */
    // TODO: Rename and change types and number of parameters
    public static DialogList newInstance(String param1, String param2) {
        DialogList fragment = new DialogList();
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
        return inflater.inflate(R.layout.fragment_dialog_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DialogsList dialogsListView = view.findViewById(R.id.dialogsList);

        DialogsListAdapter dialogsListAdapter = new DialogsListAdapter<>(new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                Picasso.get().load(url).into(imageView);
            }
        });

        dialogsListAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<Dialog>() {
            @Override
            public void onDialogClick(Dialog dialog) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.add(R.id.fragment_container_view_message, MessageList.newInstance("", ""), "MessageDisp");
                transaction.addToBackStack("MessageList");
                transaction.commit();
            }
        });

        //dialogsListAdapter.setItems(dfix.getDialogs());
        firestore.collection("chats").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Dialog Chats = document.toObject(Dialog.class);
                                dialogsListAdapter.addItem(Chats);

                            }
                        }
                    }
                });
        dialogsListView.setAdapter(dialogsListAdapter);
        GetDialogList(getView());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        // Replace whatever is in the fragment_container view with this fragment
        DisplayedUserInfo cache = new DisplayedUserInfo(user.getDisplayName(), "https://i.imgur.com/DvpvklR.png", user.getEmail(), user.getPhoneNumber());
        transaction.add(R.id.fragment_container_view_user, userProfileFragment.newInstance(cache, ""), "userProfileDisp");
        transaction.commit();

        Button newChatButton = view.findViewById(R.id.button2);

        newChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newChatDialog();
            }
        });
    }

    public void newChatDialog(){
        final EditText messageEditText = new EditText(getContext());
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Start a chat")
                .setMessage("Watchu Got?")
                .setView(messageEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //code here
                        String message = String.valueOf(messageEditText.getText());

                        if(message.length()!=0){
                            StartNewDialog(message,getView());
                        }
                    }
                })
                .setNegativeButton("Cancel",null)
                .create();

        alertDialog.show();
    }

    public void StartNewDialog(String message,View v) {
        FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
        Author user = new Author(current_user.getUid(),current_user.getDisplayName(),"https://i.imgur.com/DvpvklR.png");
        Message message1 = new Message(message,user,message,new Date());
        user1.add(user);
        Dialog dialog = new Dialog(message,FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),"", user1 ,message1,1);

        firestore.collection("chats").add(dialog.hashMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetDialogList(View v){
        String s="";
//        firestore.collection("chats").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Dialog Chats = document.toObject(Dialog.class);
//
//                            }
//                        }
//                    }
//                });

    }

}



