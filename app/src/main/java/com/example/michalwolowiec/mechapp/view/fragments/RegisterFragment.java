package com.example.michalwolowiec.mechapp.view.fragments;



import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.michalwolowiec.mechapp.R;
import com.example.michalwolowiec.mechapp.model.User;
import com.example.michalwolowiec.mechapp.viewModel.MainActivityViewModel;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private MainActivityViewModel mva;

    //Views
    @BindView(R.id.link_login)
    TextView linkLoginTextView;
    @BindView(R.id.input_email)
    EditText emailEditText;
    @BindView(R.id.input_password)
    EditText passwordEditText;
    @BindView(R.id.input_reEnterPassword)
    EditText passwordConfirmationEditText;

    //Button
    @BindView(R.id.btn_signup)
    AppCompatButton createAccBtn;

    //Callback to the activity implementing this listener
    private OnFragmentInteractionListener mListener;

    private LiveData<FirebaseUser> mUser;
    private LiveData<User> mUserInfo;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);

        mva = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mUser = mva.getmUser();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * If user clicks the login link he gets transported to the login Fragment
     */
    @OnClick(R.id.link_login)
    public void clickedLinkLogin(){
        mListener.onFragmentInteraction(new LoginFragment());
    }


    @OnClick(R.id.btn_signup)
    public void clickedCreateAcc(){

        //get data from EditTexts
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirmation = passwordConfirmationEditText.getText().toString();

        //If the form is correct then proceed
        if(!validateForm()) return;

        //if the passwords match create the account
        if(password.equals(passwordConfirmation)){
            mva.registerUser(email, password);
        }

        //when the user is created send the confirmation Email.
        mUser.observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(@Nullable FirebaseUser user) {
                if(user != null) {
                    mva.sendEmailVerification(user);
                    mListener.onFragmentInteraction(AccountInfoFragment.newInstance(user.getUid()));
                }
            }
        });

    }

    /**
     * This checks if the form is filled properly
     * if some field is empty show the user error
     * @return a boolean indicating if the form is filled correctly
     */
    private boolean validateForm(){
        boolean valid = true;

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirmation = passwordConfirmationEditText.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailEditText.setError("Required!");
            valid = false;
        } else {
            emailEditText.setError(null);
        }
        if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Required!");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }
        if(TextUtils.isEmpty(passwordConfirmation)){
            passwordConfirmationEditText.setError("Required!");
            valid = false;
        } else {
            passwordConfirmationEditText.setError(null);
        }

        if(!password.equals(passwordConfirmation)){
            passwordEditText.setError("Passwords must match");
            passwordConfirmationEditText.setError("Passwords must match");
            valid = false;
        } else {
            passwordEditText.setError(null);
            passwordConfirmationEditText.setError(null);
        }

        return valid;
    }

}
