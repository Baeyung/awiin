package com.example.cs326_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.cs326_project.Fragments.DialogList;
import com.example.cs326_project.Fragments.userProfileFragment;
import com.example.cs326_project.Models.DisplayedUserInfo;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        FirebaseUser current_user = mAuth.getCurrentUser();
        if(current_user != null ){
            SignInTransactions();

        } else {
            createSignInIntent();
        }

    }
    @Override
    public void onStart (){
        super.onStart();

    }

    public void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                SignInTransactions();
            } else {
                //sign in failed
            }
        }
    }

    public void res ()
    {
        Intent starterIntent = getIntent();
        finish();
        startActivity(starterIntent);

    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        //
                        res();
                    }
                });
    }

    public void SignInTransactions(){
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        // Replace whatever is in the fragment_container view with this fragment
        //DisplayedUserInfo cache= new DisplayedUserInfo(user.getDisplayName(),"https://i.imgur.com/DvpvklR.png",user.getEmail(),user.getPhoneNumber());
        //transaction.add(R.id.fragment_container_view_user, userProfileFragment.newInstance(cache,""),"userProfileDisp");
        transaction.add(R.id.fragment_container_view,DialogList.newInstance("",""), "userDisp");
        transaction.commit();
    }

}