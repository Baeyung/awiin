package com.example.cs326_project.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cs326_project.MainActivity;
import com.example.cs326_project.Models.DisplayedUserInfo;
import com.example.cs326_project.R;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageInput;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link userProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class userProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private DisplayedUserInfo user;
    private String mParam2;

    public userProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment userProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static userProfileFragment newInstance(DisplayedUserInfo param1, String param2) {
        userProfileFragment fragment = new userProfileFragment();
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
            user = (DisplayedUserInfo) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        TextView textView = view.findViewById(R.id.textView);
        TextView textView1 = view.findViewById(R.id.textView1);
        ImageView imageView = view.findViewById(R.id.imageView);
        textView1.setText(user.getUser_name());
        textView.setText(user.get_email());
        Picasso.get().load(user.getUser_image_url()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

    }

    public void showLogoutDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this.getContext());
        builder1.setTitle("Sign Out?");
        builder1.setMessage("We will miss You");
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity parentFrag = ((MainActivity)userProfileFragment.this.getActivity());
                        parentFrag.signOut();
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
}