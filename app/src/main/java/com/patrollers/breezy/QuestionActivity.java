package com.patrollers.breezy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.patrollers.breezy.helper.Questionnaire.getQuestions;

public class QuestionActivity extends AppCompatActivity {

    @BindView(R.id.question_view) TextView question_view;
    @BindView(R.id.question_number) TextView question_number;
    @BindView(R.id.question_progress) ProgressBar question_progress;
    @BindView(R.id.options_group) RadioGroup options_group;
    @BindView(R.id.next_ques) Button next_btn;
    @BindView(R.id.next_ques_btn) Button next_ques_btn;

    private int position;
    private List<String> currentCode;
    private JSONArray basicQuestions = null;
    private JSONObject currentQuestion;
    private List<String> allQuestions, temp;
    private Map<String, Integer> numberOfQues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        ButterKnife.bind(this);

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
                if (findViewById(options_group.getCheckedRadioButtonId()).getTag().toString().equals("yes")) {
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

                    if (allQuestions.size() == 0) {
                        SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor Ed = userPrefs.edit();
                        Ed.putString("Disease", "You are super healthy");
                        Ed.commit();
                        startActivity(new Intent(QuestionActivity.this, MainActivity.class));
                        finish();
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
            if (findViewById(options_group.getCheckedRadioButtonId()).getTag().toString().equals("yes")) {
                basicQuesPoints.put(currentCode.get(position), basicQuesPoints.get(currentCode.get(position)) + 1);
            }
            if (position == allQuestions.size() - 1) {
                int maxValue = 0;
                String finalDisease = null;
                for (Map.Entry<String, Integer> entry : basicQuesPoints.entrySet()) {
                    if (entry.getValue() > maxValue) {
                        maxValue = entry.getValue();
                        finalDisease = entry.getKey();
                    }
                }
                SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor Ed = userPrefs.edit();
                Ed.putString("Disease", finalDisease);
                Ed.commit();
                startActivity(new Intent(QuestionActivity.this, MainActivity.class));
                finish();
                return;
            }
            question_view.setText(allQuestions.get(++position));
            question_number.setText("Question " + (position + 6) + "/" + (allQuestions.size() + 5));
            question_progress.setProgress(position + 6);
        });
    }
}