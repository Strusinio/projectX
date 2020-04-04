package com.example.michalwolowiec.mechapp.view.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.michalwolowiec.mechapp.R;
import com.example.michalwolowiec.mechapp.view.fragments.AccountInfoFragment;
import com.example.michalwolowiec.mechapp.view.fragments.JobListFragment;
import com.example.michalwolowiec.mechapp.view.fragments.LoginFragment;
import com.example.michalwolowiec.mechapp.view.fragments.OnFragmentInteractionListener;
import com.example.michalwolowiec.mechapp.view.fragments.RegisterFragment;
import com.example.michalwolowiec.mechapp.view.fragments.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener,  com.example.michalwolowiec.mechapp.view.fragments.JobListFragment.OnListFragmentInteractionListener{

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        LoginFragment loginFragment = new LoginFragment();
        fragmentTransaction.replace(R.id.mainContainer, loginFragment);
        fragmentTransaction.commit();
    }

    public void onFragmentInteraction(Fragment fragment) {

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if(fragment instanceof RegisterFragment) {
            fragmentTransaction.replace(R.id.mainContainer, fragment);
        }

        if(fragment instanceof LoginFragment){
            fragmentTransaction.replace(R.id.mainContainer, fragment);
        }

        if(fragment instanceof AccountInfoFragment){
            fragmentTransaction.replace(R.id.mainContainer, fragment);
        }

        if (fragment instanceof JobListFragment){
            fragmentTransaction.replace(R.id.mainContainer, fragment);
        }

        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
