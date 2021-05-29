package com.patrollers.breezy.helper;

import org.json.JSONException;
import org.json.JSONObject;

public class Tasks {

    static String taskJsonStr = "{\n" +
            "\"2\":\"Cat1\",\"3\":\"Cat1\",\"4\":\"Cat1\",\"5\":\"Cat1\",\"6\":\"Cat1\",\n" +
            "\"7\":\"Cat2\",\"8\":\"Cat2\",\"9\":\"Cat2\",\"10\":\"Cat2\",\"11\":\"Cat2\",\n" +
            "\"12\":\"Cat3\",\"13\":\"Cat3\",\"14\":\"Cat3\",\n" +
            "\"Cat1\":{\n" +
            "\"Exercises\":\"Break a sweat by walking\",\n" +
            "\"Meditation\":\"Pranayama\",\"medi_time\":\"10\",\n" +
            "\"url\":\"cMfChJLqma4\"},\n" +
            "\"Cat2\":{\n" +
            "\"Exercises\":\"Running for a couple of kilometers\",\n" +
            "\"Meditation\":\"Pranayama\",\"medi_time\":\"15\",\n" +
            "\"url\":\"N2wR1OWhD4s\"},\n" +
            "\"Cat3\":{\n" +
            "\"Exercises\":\"High intensity workout session\",\n" +
            "\"Meditation\":\"Pranayama\",\"medi_time\":\"20\",\n" +
            "\"url\":\"d1ZjcoaYHlE\"}\n" +
            "}";

    public static JSONObject taskJson;

    public static JSONObject getTasks(int valuePoints) throws JSONException {
        if (taskJson == null){
            taskJson = new JSONObject(taskJsonStr);
        }
        return taskJson.getJSONObject(taskJson.getString(String.valueOf(valuePoints)));
    }
}
