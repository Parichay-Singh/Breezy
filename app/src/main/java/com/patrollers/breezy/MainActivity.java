package com.patrollers.breezy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patrollers.breezy.fragments.ChatFragment;
import com.patrollers.breezy.fragments.HomeFragment;
import com.patrollers.breezy.fragments.ProfileFragment;
import com.patrollers.breezy.fragments.QuizFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottomNavigation) BottomNavigationView bottomNavigation;
    @BindView(R.id.taskFloatingBtn) FloatingActionButton taskFloatingBtn;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.nav_host_frame) FrameLayout nav_host_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ButterKnife.bind(this);

        taskFloatingBtn.setOnClickListener(view -> {
            //TODO : Add functionality
            setItemCheckable(false);
        });

        bottomNavigation.setSelectedItemId(R.id.nav_home);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_frame, new HomeFragment()).commit();

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_frame, new HomeFragment()).commit();
                    setItemCheckable(true);
                    return true;

                case R.id.nav_quiz:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_frame, new QuizFragment()).commit();
                    setItemCheckable(true);
                    return true;

                case R.id.nav_chat:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_frame, new ChatFragment()).commit();
                    setItemCheckable(true);
                    return true;

                case R.id.nav_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_frame, new ProfileFragment()).commit();
                    setItemCheckable(true);
                    return true;
            }
            return false;
        });
    }

    private void setItemCheckable(boolean b) {
        bottomNavigation.getMenu().getItem(0).setCheckable(b);
        bottomNavigation.getMenu().getItem(1).setCheckable(b);
        bottomNavigation.getMenu().getItem(3).setCheckable(b);
        bottomNavigation.getMenu().getItem(4).setCheckable(b);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor Ed = userPrefs.edit();
                    Ed.putString("Name", String.valueOf(snapshot.child("Name").getValue()));
                    Ed.putString("Age", String.valueOf(snapshot.child("Age").getValue()));
                    Ed.putString("Contact", String.valueOf(snapshot.child("Contact").getValue()));
                    Ed.putString("Gender", String.valueOf(snapshot.child("Gender").getValue()));
                    Ed.putString("Points", String.valueOf(snapshot.child("Points").getValue()));
                    Ed.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Something is wrong with database", Toast.LENGTH_LONG).show();
                Log.e("Error:Database", error.getMessage());
            }
        });
    }
}