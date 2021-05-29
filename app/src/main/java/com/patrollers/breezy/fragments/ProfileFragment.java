package com.patrollers.breezy.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.patrollers.breezy.LoginActivity;
import com.patrollers.breezy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    @BindView(R.id.username_profile) TextView username_profile;
    @BindView(R.id.profile_age) TextView profile_age;
    @BindView(R.id.profile_contact) TextView profile_contact;
    @BindView(R.id.profile_credit) TextView profile_credit;
    @BindView(R.id.signout_btn) Button signout_btn;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, root);

        SharedPreferences userPrefs = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);

        username_profile.setText(userPrefs.getString("Name", "Name"));
        profile_age.setText(userPrefs.getString("Age", "100"));
        profile_contact.setText(userPrefs.getString("Contact", "9876543210"));
        profile_credit.setText(userPrefs.getString("Points", "0"));

        signout_btn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        return root;
    }
}