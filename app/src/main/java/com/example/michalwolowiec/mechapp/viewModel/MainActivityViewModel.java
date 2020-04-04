package com.example.michalwolowiec.mechapp.viewModel;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.michalwolowiec.mechapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivityViewModel extends ViewModel implements ValueEventListener {

    private final static String TAG = "MAVM";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    private MutableLiveData<FirebaseUser> mUser;
    private MutableLiveData<DocumentSnapshot> mUserData;
    private MutableLiveData<ArrayList<User>> mUsersInfo;


    public MainActivityViewModel() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = new MutableLiveData<>();
        mUserData = new MutableLiveData<>();
        mUsersInfo = new MutableLiveData<>();
    }

    /**
     * This method registers users using email and password
     * @param email user Email to register
     * @param password user password to register
     */
    public void registerUser(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Registration completed successfully");
                            mUser.setValue(mAuth.getCurrentUser());
                        } else{
                            Log.d(TAG, "onComplete: Registration Complete with a failure");
                        }
                    }
                });
    }

    /**
     * This method send the email verification to the user
     * @param user User to send verification to
     */
    public void sendEmailVerification(FirebaseUser user){

        user.sendEmailVerification()
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: Verification Email Send Succesfully");
                } else {
                    Log.d(TAG, "onComplete: Verification Email Send Failure");
                }
            }
        });
    }

    /**
     * This method signs In the user using email and password
     * @param email email to sing in
     * @param password password to sign in
     */
    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: Signed In Succesfully");
                    mUser.setValue(mAuth.getCurrentUser());
                } else {
                    Log.d(TAG, "onComplete: Signed In Failure");
                }
            }
        });
    }

    /**
     * This method is saving user data to a collection named after the user UID
     * When the data is send shows the user an AlertDialog with a button
     * taking him to the login fragment
     * @param uid user's UID
     * @param name user's name
     * @param surname user's surname
     * @param bio user's bio
     * @param phoneNumber user's phone number
     * @param age user's age
     */
    public void saveToFirebaseCloud(Context context, String uid, String name, String surname, String sex, String bio, int phoneNumber, int age){

        User user = new User(uid, name, surname, bio, phoneNumber, sex, age);

        db.collection("users")
                .document(uid)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "saveToFirebaseCloud: Successful save to cloud");
                        Toast toast = Toast.makeText(context, "Successful write to cloud", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

//        Log.d(TAG, "saveToFirebaseCloud: Successful save to cloud");
//        Toast toast = Toast.makeText(context, "Successful write to cloud", Toast.LENGTH_SHORT);

    }

    /**
     * This method fetches user data from firestore based on provided userUID
     * @param userUID String to fetch the right data
     */
    public void getUserInfo(final String userUID){
        //get the reference to database
        DocumentReference docRef = db.collection("users").document(userUID);
        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                mUserData.setValue(task.getResult());
            } else Log.d(TAG, "get failed with ", task.getException());
        });
    }




    public void sendResetPassword(final Context context, String email){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Email sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    //Getters and Setters

    public MutableLiveData<FirebaseUser> getmUser() {
        return mUser;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
