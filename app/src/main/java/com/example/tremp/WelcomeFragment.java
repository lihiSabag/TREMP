package com.example.tremp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class WelcomeFragment extends Fragment {



    public WelcomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view =inflater.inflate(R.layout.fragment_welcome, container, false);

        Button loginBtn = view.findViewById(R.id.wellcom_frg_login_btn);
        loginBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_welcomeFragment_to_loginFragment));

        Button signupBtn = view.findViewById(R.id.wellcome_frg_signup_btn);
        signupBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_welcomeFragment_to_signUpFragment));
        return view;
    }
}