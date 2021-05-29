package com.patrollers.breezy.helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Questionnaire {

    static String mainJsonStr = "{\n" +
            "\"Schizophrenia\":[\"Do you struggle to trust that what you are thinking is real?\",\n" +
            "\"Do you get the sense that others are controlling your thoughts and emotions?\",\n" +
            "\"Do other people say that it is difficult for you to stay on subject or for them to understand you?\",\n" +
            "\"Do you feel that you have little in common with family and friends?\",\n" +
            "\"Do you feel that you are being tracked, followed, or watched at home or outside?\"],\n" +
            "\"PTSD\":[\"Have you had nightmares or thought about the events when you did not want to?\",\n" +
            "\"Have you tried hard not to think about the event(s) or went out of your way to avoid situations that reminded you of the event(s)?\",\n" +
            "\"Have you been constantly on guard, watchful or easily startled?\",\n" +
            "\"Have you felt numb or detached from people, activities, or your surroundings?\",\n" +
            "\"Have you felt guilty or unable to stop blaming yourself or others for the event(s) or any problems the event(s) may have caused?\"],\n" +
            "\"OCD\":[\"Do you have frequent unwanted thoughts that seem uncontrollable?\",\n" +
            "\"Do you keep many useless things because you feel that you can’t throw them away?\",\n" +
            "\"Do you have rituals or repetitive behaviours that take a lot of time in a day?\",\n" +
            "\"Do you keep checking things over and over again?\",\n" +
            "\"Do your daily activities take a long time to complete?\"],\n" +
            "\"Depression\":[\"Have you or your family been into troubles lately?\",\n" +
            "\"Poor appetite or overeating?\",\n" +
            "\"Do you feel hopeless or unhappy frequently?\",\n" +
            "\"Do you face difficulty in making decisions?\",\n" +
            "\"Does it take great effort for you to do simple things?\"],\n" +
            "\"Anxiety\":[\"Being so restless that it is hard to sit still?\",\n" +
            "\"Feeling afraid, as if something awful might happen\",\n" +
            "\"Worrying too much about different things\",\n" +
            "\"Does worry or anxiety interfere with falling and/or staying asleep?\",\n" +
            "\"Do you experience strong fear that causes panic, shortness of breath, chest pains, a pounding heart, sweating, shaking, nausea, dizziness and/or fear of dying?\"]\n" +
            "}";

    public static JSONObject mainJson;

    public static List<String> getQuestions(List<String> disease) throws JSONException {
        List<String> questions = new ArrayList<>();

        if (mainJson == null){
            mainJson = new JSONObject(mainJsonStr);
        }
        for (String i : disease){
            for (int j = 0; j < mainJson.getJSONArray(i).length(); j++) {
                questions.add(mainJson.getJSONArray(i).get(j).toString());
            }
        }
        return questions;
    }
}
