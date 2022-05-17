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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.geoinvaders.R;
import com.example.geoinvaders.database.DBUserManager;
import com.example.geoinvaders.serverCommunication.ServerMessages;
import com.example.geoinvaders.serverCommunication.User;

public class LogInFragment extends Fragment implements View.OnClickListener{

    DBUserManager dbUserManager;

    EditText editTextInLogin;
    EditText editTextInPassword;

    View view;

    TextView signUp;

    Button btnLogin;

    ProgressBar progressBar;

    ServerMessages serverMessages;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        dbUserManager = DBUserManager.getInstance(view.getContext());

        editTextInLogin = view.findViewById(R.id.editTextInLogin);
        editTextInPassword = view.findViewById(R.id.editTextInPassword);

        signUp = view.findViewById(R.id.signUp);
        signUp.setOnClickListener(this);

        btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        progressBar = view.findViewById(R.id.progressBarLogIn);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        serverMessages = ServerMessages.getInstance();

        this.view = view;

        return view;
    }

    private void changeFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        fragmentTransaction.replace(R.id.login_fragments, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            String userName = editTextInLogin.getText().toString();
            String password = editTextInPassword.getText().toString();

            if (!userName.equals("") && !password.equals("")) {
                User user = new User(userName, password);
                if (internetConnection()) {
                    serverMessages.login(user, view.getContext(), progressBar, dbUserManager);
                } else {
                    Toast.makeText(view.getContext(), "Нет подключения к интернету", Toast.LENGTH_LONG).show();
                }
            }
        }
        else if (view.getId() == R.id.signUp) {
            assert getFragmentManager() != null;
            changeFragment(getFragmentManager().beginTransaction(), new SignUpFragment());
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
