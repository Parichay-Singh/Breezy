package com.patrollers.breezy.fragments;

import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.patrollers.breezy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static com.patrollers.breezy.helper.Questionnaire.getQuestions;

public class QuizFragment extends Fragment {

    @BindView(R.id.quiz_ques_view) TextView question_view;
    @BindView(R.id.quiz_ques_number) TextView question_number;
    @BindView(R.id.quiz_progress) ProgressBar question_progress;
    @BindView(R.id.options_grp_quiz) RadioGroup options_group;
    @BindView(R.id.next_ques_quiz) Button next_btn;
    @BindView(R.id.next_ques_btn_quiz) Button next_ques_btn;
    @BindView(R.id.main_ques_layout) ConstraintLayout main_ques_layout;
    @BindView(R.id.confirmation_cardView) CardView confirmation_cardView;
    @BindView(R.id.confirm_btn_start) Button confirm_btn_start;

    private int position;
    private List<String> currentCode;
    private JSONArray basicQuestions = null;
    private JSONObject currentQuestion;
    private List<String> allQuestions, temp;
    private Map<String, Integer> numberOfQues;
    Animation left,right;

    public QuizFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_quiz, container, false);

        ButterKnife.bind(this, root);

        confirm_btn_start.setOnClickListener(view -> {
            left = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
            right= AnimationUtils.loadAnimation(getContext(),R.anim.fadeout);
            confirmation_cardView.setAnimation(right);
            confirmation_cardView.setVisibility(View.GONE);
            main_ques_layout.setAnimation(left);
            main_ques_layout.setVisibility(View.VISIBLE);
        });

        numberOfQues = new HashMap<>();
        currentCode = new ArrayList<>();
        Map<String, Integer> basicQuesPoints = new HashMap<>();
        basicQuesPoints.put("Schizophrenia", 0);
        basicQuesPoints.put("PTSD", 0);
        basicQuesPoints.put("Anxiety", 0);
        basicQuesPoints.put("OCD", 0);
        basicQuesPoints.put("Depression", 0);

        try {
            basicQuestions = new JSONArray(getResources().getString(R.string.basicQues));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        position = 0;

        question_progress.setMax(basicQuestions.length());
        question_progress.setProgress(position + 1);
        question_number.setText("Question " + (position + 1) + "/" + basicQuestions.length());

        try {
            currentQuestion = new JSONObject(basicQuestions.get(position).toString());
            question_view.setText(currentQuestion.get("ques").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        next_btn.setOnClickListener(view -> {
            try {
                String code = currentQuestion.get("code").toString();
                if (root.findViewById(options_group.getCheckedRadioButtonId()).getTag().toString().equals("yes")) {
                    basicQuesPoints.put(code, basicQuesPoints.get(code) + 1);
                    numberOfQues.put(code, (Integer) currentQuestion.get("id"));
                }

                if (position == 4) {
                    next_btn.setVisibility(View.GONE);
                    next_ques_btn.setVisibility(View.VISIBLE);
                    temp = new ArrayList<>();
                    for (Map.Entry<String, Integer> check : basicQuesPoints.entrySet()) {
                        if (check.getValue() == 1) {
                            temp.add(check.getKey());
                        }
                    }
                    allQuestions = getQuestions(temp);

                    if (allQuestions.size() == 0){
                        SharedPreferences userPrefs = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor Ed = userPrefs.edit();
                        Ed.putString("Disease", "You are super healthy");
                        Ed.commit();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_frame, new HomeFragment()).commit();
                        return;
                    }
                    question_view.setText(allQuestions.get(0));
                    position = 0;
                    for (Map.Entry<String, Integer> check : numberOfQues.entrySet()) {
                        for (int i = 0; i < check.getValue(); i++) {
                            currentCode.add(check.getKey());
                        }
                    }

                    question_number.setText("Question " + "6/" + (allQuestions.size() + 5));
                    question_progress.setMax(allQuestions.size() + 5);
                    question_progress.setProgress(6);
                    return;
                }
                currentQuestion = new JSONObject(basicQuestions.get(++position).toString());
                question_view.setText(currentQuestion.get("ques").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            question_number.setText("Question " + (position + 1) + "/5");
            question_progress.setProgress(position + 1);

            Log.e("Info", basicQuesPoints.toString());
        });

        next_ques_btn.setOnClickListener(view -> {
            if (root.findViewById(options_group.getCheckedRadioButtonId()).getTag().toString().equals("yes")) {
                basicQuesPoints.put(currentCode.get(position), basicQuesPoints.get(currentCode.get(position)) + 1);
            }
            if (position == allQuestions.size() - 1) {
                int maxValue = 0;
                String finalDisease = null;
                for (Map.Entry<String, Integer> entry : basicQuesPoints.entrySet()){
                    if (entry.getValue() > maxValue) {
                        maxValue = entry.getValue();
                        finalDisease = entry.getKey();
                    }
                }
                SharedPreferences userPrefs = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor Ed = userPrefs.edit();
                Ed.putString("Disease", finalDisease);
                Ed.commit();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_frame, new HomeFragment()).commit();
                return;
            }
            question_view.setText(allQuestions.get(++position));
            question_number.setText("Question " + (position + 6) + "/" + (allQuestions.size() + 5));
            question_progress.setProgress(position + 6);
        });

        return root;
    }
}
