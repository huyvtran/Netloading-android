package com.ketnoivantai.companies.commons;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ketnoivantai.R;

/**
 * Created by Dandoh on 3/7/16.
 */
public class GenericActivity extends AppCompatActivity {

    protected String TAG = getClass().getSimpleName();


    protected void initFragment(Fragment loginFragment) {
        // Add the NotesFragment to the layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, loginFragment);
        transaction.commit();
    }
}
