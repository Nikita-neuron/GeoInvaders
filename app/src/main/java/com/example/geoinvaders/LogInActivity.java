package com.example.geoinvaders;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.geoinvaders.fragments.LogInFragment;

public class LogInActivity extends AppCompatActivity {

    LinearLayout loginFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        loginFragments = findViewById(R.id.login_fragments);

        LogInFragment logInFragment = new LogInFragment();
        changeFragment(getSupportFragmentManager().beginTransaction(), logInFragment);
    }

    private void changeFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        fragmentTransaction.replace(R.id.login_fragments, fragment);
        fragmentTransaction.commit();
    }

}
