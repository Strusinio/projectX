package com.example.michalwolowiec.mechapp.view.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.michalwolowiec.mechapp.R;
import com.example.michalwolowiec.mechapp.viewModel.MainActivityViewModel;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {

    //views
    @BindView(R.id.emailEditText)
    EditText emailEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;

    //buttons / links
    @BindView(R.id.btn_login)
    AppCompatButton loginBtn;
    @BindView(R.id.link_signup)
    TextView linkSignUpTextView;
    @BindView(R.id.link_forgot_pass)
    TextView linkForgotPass;


    private OnFragmentInteractionListener mListener;

    private MainActivityViewModel mva;

    private LiveData<FirebaseUser> mUser;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
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

    @OnClick(R.id.btn_login)
    public void clickedLogIn(){

        //check of the form is filled properly
        if(!validateForm()) return;

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        //sign in the user using email and password
        //if the user is verified redirect user to the maps activity
        //else show him an alertdialog
        mva.signIn(email, password);
        final ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setBackgroundColor(getResources().getColor(R.color.colorDialog));
        if(mUser != null){
            mUser.observe(this, new Observer<FirebaseUser>() {
                @Override
                public void onChanged(@Nullable FirebaseUser user) {
                    if(user.isEmailVerified()){
                        mListener.onFragmentInteraction(new JobListFragment());
                    } else {
                        final AlertDialog alertDialog = createDialog();
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorDialog));
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorDialog));
                            }
                        });
                        alertDialog.show();
                    }
                }
            });
        }


    }

    /**
     * This is invoked after user clicks the "dont have an accout ?" link
     */
    @OnClick(R.id.link_signup)
    public void clickedSignUp(){
        mListener.onFragmentInteraction(new RegisterFragment());
    }

    private boolean validateForm(){
        boolean isValid = true;

        //get data
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        //TODO: check if the input is in fact a valid email address
        if(TextUtils.isEmpty(email)){
            emailEditText.setError("Required");
            isValid = false;
        } else {
            emailEditText.setError(null);
        }

        if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Required");
            isValid = false;
        } else {
            passwordEditText.setError(null);
        }

        return isValid;
    }

    /**
     * Creates a simple dialog
     * @return Created AlertDialog
     */
    private AlertDialog createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder.setTitle("Registration Finished")
                .setMessage("Please check your mailbox for a verification email!")
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .setNegativeButton("I didn't get the email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mva.sendEmailVerification(mUser.getValue());
                        Toast.makeText(getActivity(), "Verification email sent", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }

    /**
     * This gets invoked after user clicks "FOrgot password ?" link
     */
    @OnClick(R.id.link_forgot_pass)
    public void clickedForgotPassLink(){
        String email = emailEditText.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailEditText.setError("Required");
        } else{
            emailEditText.setError(null);
            mva.sendResetPassword(getActivity(), email);
        }
    }

}
