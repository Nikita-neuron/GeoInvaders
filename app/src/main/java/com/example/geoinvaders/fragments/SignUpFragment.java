package com.example.geoinvaders.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.geoinvaders.R;
import com.example.geoinvaders.database.DBUserManager;
import com.example.geoinvaders.serverCommunication.ServerMessages;
import com.example.geoinvaders.serverCommunication.User;

public class SignUpFragment extends Fragment implements View.OnClickListener{

    EditText editTextSignLogin;
    EditText editTextSignPassword;

    Button btnSignUp;

    ProgressBar progressBar;

    View view;

    ServerMessages serverMessages;

    DBUserManager dbUserManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        dbUserManager = DBUserManager.getInstance(view.getContext());

        editTextSignLogin = view.findViewById(R.id.editTextSignLogin);
        editTextSignPassword = view.findViewById(R.id.editTextSignPassword);

        btnSignUp = view.findViewById(R.id.btn_signUp);
        btnSignUp.setOnClickListener(this);

        progressBar = view.findViewById(R.id.progressBarSignUp);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        serverMessages = ServerMessages.getInstance();

        this.view = view;

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_signUp) {
            String userName = editTextSignLogin.getText().toString();
            String password = editTextSignPassword.getText().toString();

            if (!userName.equals("") && !password.equals("")) {
                User user = new User(userName, password);
                if (internetConnection()) {
                    serverMessages.registration(user, view.getContext(), progressBar, dbUserManager);
                } else {
                    Toast.makeText(view.getContext(), "Нет подключения к интернету", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean internetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) view.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED);
    }
}
