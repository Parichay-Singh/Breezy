package com.patrollers.breezy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.patrollers.breezy.MainActivity;
import com.patrollers.breezy.R;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.patrollers.breezy.LoginActivity.login_progress;

public class LoginFragment extends Fragment {

    @BindView(R.id.login_email) EditText login_email;
    @BindView(R.id.login_pass) EditText login_pass;
    @BindView(R.id.login_btn) Button login_btn;
    float v = 0;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, root);
        mAuth = FirebaseAuth.getInstance();

        login_email.setTranslationX(800);
        login_pass.setTranslationX(800);
        login_btn.setTranslationX(800);

        login_email.setAlpha(v);
        login_pass.setAlpha(v);
        login_btn.setAlpha(v);

        login_email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        login_pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login_btn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        login_btn.setOnClickListener(view -> {
            String email = login_email.getText().toString().trim();
            String pass = login_pass.getText().toString();

            if (email.isEmpty() || pass.isEmpty())
                Snackbar.make(view, "Please fill all fields!", Snackbar.LENGTH_LONG).show();
            else {
                login_progress.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(getContext(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        Log.e("signInWith:failure", task.getException().getMessage());
                        Snackbar.make(view, "Authentication failed.", Snackbar.LENGTH_LONG).show();
                        login_progress.setVisibility(View.GONE);
                    }
                });
            }
        });

        return root;
    }
}
