package com.patrollers.breezy.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.patrollers.breezy.R;

import com.patrollers.breezy.database.DailyDao;
import com.patrollers.breezy.database.DailyPointDb;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static com.patrollers.breezy.helper.Tasks.getTasks;

public class TaskFragment extends Fragment {

    @BindView(R.id.media_player) PlayerView media_player;
    @BindView(R.id.playing_song_name) TextView playing_song_name;
    @BindView(R.id.exercise) TextView exercise;
    @BindView(R.id.time_exercise) TextView time_exercise;
    @BindView(R.id.meditation) TextView meditation;
    @BindView(R.id.time_meditation) TextView time_meditation;
    @BindView(R.id.meditate_done) ImageView meditate_done;
    @BindView(R.id.exc_done) ImageView exc_done;

    private SimpleExoPlayer player;
    private boolean playWhenReady = false;
    private int temp, currentWindow = 0;
    private long playbackPosition = 0;
    private List<String> mediaTitle;
    private String meditate_url;

    public TaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task, container, false);

        ButterKnife.bind(this, root);

        SharedPreferences userPrefs = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor Ed = userPrefs.edit();
        int valuePoints = userPrefs.getInt("ValuePoints", 0);
        temp = Integer.parseInt(userPrefs.getString("Points", "0"));

        DailyDao dao = DailyPointDb.getInstance(getContext()).dailyDao();
        boolean isExercise = dao.getExercise();
        boolean isMeditate = dao.getMeditate();

        if (isExercise)
            exc_done.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
        if (isMeditate)
            meditate_done.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));

        try {
            JSONObject tasksJson = getTasks(valuePoints);
            exercise.setText(tasksJson.getString("Exercises"));
            meditation.setText(tasksJson.getString("Meditation"));
            time_exercise.setText("30 minutes");
            time_meditation.setText(tasksJson.getString("medi_time") + " minutes");
            meditate_url = tasksJson.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mediaTitle = Arrays.asList("Deep Meditation", "Earthly Music", "Relaxing Music", "Stars", "The Light", "Yoga", "Yoga Tune");
        playing_song_name.setText(mediaTitle.get(0));

        meditate_done.setOnClickListener(view -> {
            watchYoutubeVideo(getContext(), meditate_url);
            if (!isMeditate) {
                meditate_done.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
                temp += 10;
                Ed.putString("Points", String.valueOf(temp));
                Ed.apply();
                storeInFirebase(String.valueOf(temp));
                dao.updateMeditate(true);
            }
        });

        exc_done.setOnClickListener(view -> {
            if (!isExercise)
                new AlertDialog.Builder(getContext())
                        .setTitle("Have you completed the Exercise for 30 minutes?")
                        .setCancelable(false)
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            exc_done.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
                            temp += 10;
                            Ed.putString("Points", String.valueOf(temp));
                            Ed.apply();
                            storeInFirebase(String.valueOf(temp));
                            dao.updateExercise(true);
                        })
                        .setIcon(R.drawable.circlebreezy)
                        .show();
        });

        return root;
    }

    private void storeInFirebase(String points) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("Points")
                .setValue(points);
    }

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(getContext()).build();
        media_player.setPlayer(player);

        MediaItem mediaItem1 = MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.deep_meditation));
        MediaItem mediaItem2 = MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.earth));
        MediaItem mediaItem3 = MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.relaxing));
        MediaItem mediaItem4 = MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.stars));
        MediaItem mediaItem5 = MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.the_light));
        MediaItem mediaItem6 = MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.yoga));
        MediaItem mediaItem7 = MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.yoga_tune));

        player.setMediaItem(mediaItem1);
        player.addMediaItem(mediaItem2);
        player.addMediaItem(mediaItem3);
        player.addMediaItem(mediaItem4);
        player.addMediaItem(mediaItem5);
        player.addMediaItem(mediaItem6);
        player.addMediaItem(mediaItem7);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();

        player.addListener(new Player.EventListener() {
            @Override
            public void onPositionDiscontinuity(int reason) {
                int latestWindowIndex = player.getCurrentWindowIndex();
                playing_song_name.setText(mediaTitle.get(latestWindowIndex));
            }
        });
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        media_player.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }
}