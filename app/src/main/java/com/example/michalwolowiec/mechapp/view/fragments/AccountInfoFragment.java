package com.example.michalwolowiec.mechapp.view.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.michalwolowiec.mechapp.R;
import com.example.michalwolowiec.mechapp.viewModel.MainActivityViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountInfoFragment extends Fragment {
    public final static String TAG = "AIF";

    //views
    @BindView(R.id.input_name)
    EditText nameEditText;
    @BindView(R.id.input_surname)
    EditText surnameEditText;
    @BindView(R.id.input_phoneNumber)
    EditText phoneNumberEditText;
    @BindView(R.id.input_age)
    EditText ageEditText;
//    @BindView(R.id.input_bio)
//    EditText bioEditText;
    @BindView(R.id.input_gender)
    EditText sexEditText;

    //buttons
    @BindView(R.id.btn_save)
    AppCompatButton saveBtn;

    private String mUserUID;

    private MainActivityViewModel mva;

    //callback to the activity implementing this listener
    private OnFragmentInteractionListener mListener;


    /**
     * Creates new instance of the AccountInfoFragment with a bundle
     * containing the userUID used to store user's data
     * @param userUID user's UID
     * @return instance of a AccountInfoFragment
     */
    public static AccountInfoFragment newInstance(String userUID){
        AccountInfoFragment fragment = new AccountInfoFragment();
        Bundle args = new Bundle();
        args.putString("UID", userUID);
        fragment.setArguments(args);
        return fragment;
    }


    public AccountInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            this.mUserUID = getArguments().getString("UID");
            Log.d(TAG, "onCreate: success getting the user, UID: " + mUserUID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_info, container, false);
        ButterKnife.bind(this,view);

        //Get the viewmodel
        mva = new ViewModelProvider(this).get(MainActivityViewModel.class);

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

    @OnClick(R.id.btn_save)
    public void clickedSaveBtn(){

        if(!validateForm()) return;

        String name;
        String surname;
        String sex;
        String bio;
        int phoneNumber;
        int age;

        name = nameEditText.getText().toString();
        surname = surnameEditText.getText().toString();
        sex = sexEditText.getText().toString();
        bio = "";
        phoneNumber = Integer.valueOf(phoneNumberEditText.getText().toString());
        age = Integer.valueOf(ageEditText.getText().toString());

        //save to cloud storage
        mva.saveToFirebaseCloud(getActivity(), mUserUID, name, surname, sex, bio, phoneNumber, age);



        final AlertDialog alertDialog = createDialog();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorDialog));
            }
        });
        alertDialog.show();
    }

    private boolean validateForm(){
        boolean isValid = true;

        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        String sex = sexEditText.getText().toString();
        String bio = "";
        String phoneNumber = phoneNumberEditText.getText().toString();
        String age = ageEditText.getText().toString();

        if(TextUtils.isEmpty(name)){
            nameEditText.setError("Required");
            isValid = false;
        } else {
            nameEditText.setError(null);
        }

        if(TextUtils.isEmpty(surname)){
            surnameEditText.setError("Required");
            isValid = false;
        } else {
            surnameEditText.setError(null);
        }

        if(TextUtils.isEmpty(sex)){
            sexEditText.setError("Required");
            isValid = false;
        } else {
            if(sex.equals("male") || sex.equals("female") || sex.equals("Male") || sex.equals("Female")){
                sexEditText.setError(null);
            } else{
                sexEditText.setError("Must be male or female");
                isValid = false;
            }
        }

//        if(TextUtils.isEmpty(bio)){
//            bioEditText.setError("Required");
//            isValid = false;
//        } else {
//            bioEditText.setError(null);
//        }

        //TODO: Add a check to see if the provided input is a number
        if(TextUtils.isEmpty(phoneNumber)){
            phoneNumberEditText.setError("Required");
            isValid = false;
        } else {
            phoneNumberEditText.setError(null);
        }

        //TODO: Add a check to see if the provided input is a number
        if(TextUtils.isEmpty(age)){
            ageEditText.setError("Required");
            isValid = false;
        } else {
            if(Integer.valueOf(age) < 100 && Integer.valueOf(age) > 13){
                ageEditText.setError("You must be between 13 and 100 years old");
            }
            ageEditText.setError(null);
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
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onFragmentInteraction(new LoginFragment());
                    }
                });

        return builder.create();
    }

}
