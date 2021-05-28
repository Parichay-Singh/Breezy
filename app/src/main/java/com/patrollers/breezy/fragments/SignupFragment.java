package com.patrollers.breezy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrollers.breezy.MainActivity;
import com.patrollers.breezy.R;

import android.content.Intent;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.patrollers.breezy.LoginActivity.login_progress;

public class SignupFragment extends Fragment {

    @BindView(R.id.reg_name) EditText reg_name;
    @BindView(R.id.reg_email) EditText reg_email;
    @BindView(R.id.reg_age) EditText reg_age;
    @BindView(R.id.reg_contact) EditText reg_contact;
    @BindView(R.id.reg_password) EditText reg_password;
    @BindView(R.id.reg_conf_pass) EditText reg_conf_pass;
    @BindView(R.id.signup_btn) Button signup_btn;
    @BindView(R.id.genderRadio) RadioGroup genderRadio;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signup, container, false);

        ButterKnife.bind(this, root);
        mAuth = FirebaseAuth.getInstance();
        signup_btn.setOnClickListener(view -> {
            String email = reg_email.getText().toString();
            String password = reg_password.getText().toString();
            String confpass = reg_conf_pass.getText().toString();
            String gender = ((RadioButton) root.findViewById(genderRadio.getCheckedRadioButtonId())).getText().toString();

            if (email.isEmpty() || password.isEmpty() || confpass.isEmpty()) {
                Snackbar.make(view, "Please Enter all the Fields", Snackbar.LENGTH_LONG).show();
            } else if (!password.equals(confpass)) {
                Snackbar.make(view, "Passwords don't Match", Snackbar.LENGTH_LONG).show();
            } else {
                login_progress.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, String> userMap = new HashMap<>();
                        userMap.put("Name", reg_name.getText().toString());
                        userMap.put("Age", reg_age.getText().toString());
                        userMap.put("Contact", reg_contact.getText().toString());
                        userMap.put("Points", "0");
                        userMap.put("Gender", gender);

                        FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid()).setValue(userMap).addOnCompleteListener(taskDb -> {
                            if (taskDb.isSuccessful()) {
                                startActivity(new Intent(getContext(), MainActivity.class));
                                getActivity().finish();
                            } else {
                                Snackbar.make(view, "Sign Up Failed", Snackbar.LENGTH_LONG).show();
                                Log.e("error:firebase-database", taskDb.getException().getMessage());
                                login_progress.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Snackbar.make(view, "Sign Up Failed", Snackbar.LENGTH_LONG).show();
                        Log.e("error:firebase-auth", task.getException().getMessage());
                        login_progress.setVisibility(View.GONE);
                    }
                });
            }
        });
        return root;
    }
}
