package com.example.cs326_project.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.cs326_project.Models.Author;
import com.example.cs326_project.Models.Dialog;

import com.example.cs326_project.Models.DisplayedUserInfo;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

//additional

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogList extends Fragment {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
    ListenerRegistration registration;
    CollectionReference chatFirebaseRef = firestore.collection("chats");

    DialogsListAdapter dialogsListAdapter = new DialogsListAdapter<>(new ImageLoader() {
        @Override
        public void loadImage(ImageView imageView, String url, Object payload) {
            if(url==""|| url ==null)
                url="https://i.imgur.com/DvpvklR.png";
            Picasso.get().load(url).into(imageView);
        }
    });



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

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        DialogsList dialogsListView = view.findViewById(R.id.dialogsList);


        dialogsListAdapter.setOnDialogLongClickListener(new DialogsListAdapter.OnDialogLongClickListener<Dialog>() {
            @Override
            public void onDialogLongClick(Dialog dialog) {
                if(dialog.getOwnerId().equals(current_user.getUid()))
                {
                    showDeleteChatDialog(dialog);
                }
            }
        });

        dialogsListAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<Dialog>() {
            @Override
            public void onDialogClick(Dialog dialog) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(dialog.getFirebase_id(), dialog.getTotal_messages());
                editor.apply();

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.add(R.id.fragment_container_view_message, MessageList.newInstance(dialog, ""), "MessageDisp");
                transaction.addToBackStack("MessageList");
                transaction.commit();

                registration.remove();
            }
        });

        //dialogsListAdapter.setItems(dfix.getDialogs());
        registration = chatFirebaseRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "failed to fetch results", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialogsListAdapter.clear();
                ArrayList<Dialog> temp = new ArrayList<Dialog>();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.getData() != null) {
                        Dialog Chats = doc.toObject(Dialog.class);
                        Chats.setFirebase_id(doc.getId());
                        int defaultValue = 0;
                        int diff = Chats.getTotal_messages() - sharedPref.getInt(Chats.getFirebase_id(), defaultValue);
                        Chats.setUnreadCount(diff);
                        temp.add(Chats);
                        //dialogsListAdapter.upsertItem(Chats);
                    }
                }

                Collections.sort(temp, Collections.reverseOrder());
                dialogsListAdapter.setItems(temp);
                Toast.makeText(getContext(), "listening", Toast.LENGTH_SHORT).show();
            }
        });

        dialogsListView.setAdapter(dialogsListAdapter);
        //GetDialogList(getView());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        // Replace whatever is in the fragment_container view with this fragment
        DisplayedUserInfo cache = new DisplayedUserInfo(user.getDisplayName(), "https://ui-avatars.com/api/?name="+user.getDisplayName(), user.getEmail(), user.getPhoneNumber());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registration.remove();

    }

    public void newChatDialog(){
        Context context = getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Add a TextView here for the "Title" label, as noted in the comments
        final EditText titleEditText = new EditText(context);
        titleEditText.setHint("Enter your chat title");
        layout.addView(titleEditText); // Notice this is an add method

        // Add another TextView here for the "Description" label
        final EditText messageEditText = new EditText(context);
        messageEditText.setHint("Enter your first message");
        layout.addView(messageEditText); // Another add method
        layout.setPadding(30, 0, 30, 0);


        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Start a chat")
                .setMessage("We got you!")
                .setView(titleEditText)
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //code here
                        String message = String.valueOf(messageEditText.getText());
                        String title = String .valueOf(titleEditText.getText());

                        if(message.length()!=0&&title.length()!=0){
                            StartNewDialog(title,message,getView());
                        }
                    }
                })
                .setNegativeButton("Cancel",null)
                .create();

        alertDialog.show();
    }

    public void StartNewDialog(String title,String message,View v) {
        Author user = new Author(current_user.getUid(),current_user.getDisplayName(),"https://ui-avatars.com/api/?name="+current_user.getDisplayName());
        Message message1 = new Message(message,user,message,new Date());
      ///  ArrayList<Message> messages = new ArrayList<Message>();
      ///  messages.add(message1);
        user1.add(user);
        Dialog dialog = new Dialog(message,title,"", user1 ,message1,1,current_user.getUid(),1);

        chatFirebaseRef.add(dialog.hashMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                        CollectionReference messageRef = firestore
                                .collection("chats").document(documentReference.getId())
                                .collection("messages");

                        messageRef.add(message1.hashMap());

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
//                            }
//                        }
//                    }
//                });

    }

    public void showDeleteChatDialog(Dialog chat){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this.getContext());
        builder1.setTitle("Are you sure you want to delete this chat?");
        builder1.setMessage("");
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        chatFirebaseRef.document(chat.getFirebase_id())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void refreshDialogs(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        dialogsListAdapter.clear();
        chatFirebaseRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dialogsListAdapter.clear();
                            ArrayList<Dialog> temp = new ArrayList<Dialog>();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.getData() != null) {
                                    Dialog Chat = doc.toObject(Dialog.class);
                                    Chat.setFirebase_id(doc.getId());
                                    int defaultValue = 0;
                                    int diff = Chat.getTotal_messages() - sharedPref.getInt(Chat.getFirebase_id(), defaultValue);
                                    Chat.setUnreadCount(diff);
                                    temp.add(Chat);
                                    //dialogsListAdapter.upsertItem(Chats);
                                }
                            }

                            Collections.sort(temp, Collections.reverseOrder());
                            dialogsListAdapter.setItems(temp);
                            Toast.makeText(getContext(), "listening", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}



