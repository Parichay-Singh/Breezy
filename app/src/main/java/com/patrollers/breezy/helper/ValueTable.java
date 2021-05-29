package com.patrollers.breezy.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.patrollers.breezy.database.DailyDao;
import com.patrollers.breezy.database.DailyPointDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ValueTable {

    static String mainValueStr = "{\n" +
            "\"Schizophrenia\":[{\n" +
            "\"age_min\": 14,\"age_max\": 17,\n" +
            "\"sleep_min\":8,\"sleep_max\":10,\"value\":2},\n" +
            "{\"age_min\": 18,\"age_max\": 24,\n" +
            "\"sleep_min\":7,\"sleep_max\":9,\"value\":3},\n" +
            "{\"age_min\": 25,\"age_max\": 34,\n" +
            "\"sleep_min\":7,\"sleep_max\":8,\"value\":5},\n" +
            "{\"age_min\": 35,\"age_max\": 49,\n" +
            "\"sleep_min\":7,\"sleep_max\":8,\"value\":6},\n" +
            "{\"age_min\": 50,\"age_max\": 64,\n" +
            "\"sleep_min\":7,\"sleep_max\":9,\"value\":2},\n" +
            "{\"age_min\": 65,\"age_max\": 150,\n" +
            "\"sleep_min\":7,\"sleep_max\":10,\"value\":1}],\n" +
            "\"Schizophrenia-Male\":[{\n" +
            "\"age_min\": 14,\"age_max\": 17,\n" +
            "\"sleep_min\":8,\"sleep_max\":10,\"value\":3},\n" +
            "{\"age_min\": 18,\"age_max\": 24,\n" +
            "\"sleep_min\":7,\"sleep_max\":9,\"value\":4},\n" +
            "{\"age_min\": 25,\"age_max\": 34,\n" +
            "\"sleep_min\":7,\"sleep_max\":8,\"value\":6},\n" +
            "{\"age_min\": 35,\"age_max\": 49,\n" +
            "\"sleep_min\":7,\"sleep_max\":8,\"value\":5},\n" +
            "{\"age_min\": 50,\"age_max\": 64,\n" +
            "\"sleep_min\":7,\"sleep_max\":9,\"value\":2},\n" +
            "{\"age_min\": 65,\"age_max\": 150,\n" +
            "\"sleep_min\":7,\"sleep_max\":10,\"value\":1\n" +
            "}],\n" +
            "\"PTSD\":[{\n" +
            "\"age_min\": 14,\"age_max\": 17,\n" +
            "\"sleep_min\":8,\"sleep_max\":10,\"value\":2},\n" +
            "{\"age_min\": 18,\"age_max\": 24,\n" +
            "\"sleep_min\":7,\"sleep_max\":9,\"value\":6},\n" +
            "{\"age_min\": 25,\"age_max\": 34,\n" +
            "\"sleep_min\":7,\"sleep_max\":8,\"value\":4},\n" +
            "{\"age_min\": 35,\"age_max\": 49,\n" +
            "\"sleep_min\":7,\"sleep_max\":8,\"value\":5},\n" +
            "{\"age_min\": 50,\"age_max\": 64,\n" +
            "\"sleep_min\":7,\"sleep_max\":9,\"value\":3},\n" +
            "{\"age_min\": 65,\"age_max\": 150,\n" +
            "\"sleep_min\":7,\"sleep_max\":10,\"value\":1\n" +
            "}],\n" +
            "\"Depression\":[{\n" +
            "\"age_min\": 14,\"age_max\": 17,\n" +
            "\"sleep_min\":8,\"sleep_max\":10,\"value\":2},\n" +
            "{\"age_min\": 18,\"age_max\": 24,\n" +
            "\"sleep_min\":7,\"sleep_max\":9,\"value\":6},\n" +
            "{\"age_min\": 25,\"age_max\": 34,\n" +
            "\"sleep_min\":7,\"sleep_max\":8,\"value\":5},\n" +
            "{\"age_min\": 35,\"age_max\": 49,\n" +
            "\"sleep_min\":7,\"sleep_max\":8,\"value\":4},\n" +
            "{\"age_min\": 50,\"age_max\": 64,\n" +
            "\"sleep_min\":7,\"sleep_max\":9,\"value\":3},\n" +
            "{\"age_min\": 65,\"age_max\": 150,\n" +
            "\"sleep_min\":7,\"sleep_max\":10,\"value\":1\n" +
            "}],\n" +
            "\"OCD\":[{\n" +
            "\"age_min\": 14,\"age_max\": 17,\n" +
            "\"sleep_min\":8,\"sleep_max\":10,\"value\":3},\n" +
            "{\"age_min\": 18,\"age_max\": 24,\n" +
            "\"sleep_min\":7,\"sleep_max\":9,\"value\":6},\n" +
            "{\"age_min\": 25,\"age_max\": 34,\n" +
            "\"sleep_min\":7,\"sleep_max\":8,\"value\":5},\n" +
            "{\"age_min\": 35,\"age_max\": 49,\n" +
            "\"sleep_min\":7,\"sleep_max\":8,\"value\":4},\n" +
            "{\"age_min\": 50,\"age_max\": 64,\n" +
            "\"sleep_min\":7,\"sleep_max\":9,\"value\":1},\n" +
            "{\"age_min\": 65,\"age_max\": 150,\n" +
            "\"sleep_min\":7,\"sleep_max\":10,\"value\":2\n" +
            "}],\n" +
            "\"Anxiety\":[{\n" +
            "\"age_min\": 14,\"age_max\": 17,\n" +
            "\"sleep_min\":8,\"sleep_max\":10,\"value\":3},\n" +
            "{\"age_min\": 18,\"age_max\": 24,\n" +
            "\"sleep_min\":7,\"sleep_max\":9,\"value\":6},\n" +
            "{\"age_min\": 25,\"age_max\": 34,\n" +
            "\"sleep_min\":7,\"sleep_max\":8,\"value\":5},\n" +
            "{\"age_min\": 35,\"age_max\": 49,\n" +
            "\"sleep_min\":7,\"sleep_max\":8,\"value\":4},\n" +
            "{\"age_min\": 50,\"age_max\": 64,\n" +
            "\"sleep_min\":7,\"sleep_max\":9,\"value\":2},\n" +
            "{\"age_min\": 65,\"age_max\": 150,\n" +
            "\"sleep_min\":7,\"sleep_max\":10,\"value\":1\n" +
            "}]\n" +
            "}";

    static JSONObject mainValueJson;

    public static int getPoints(Context context) throws JSONException, InterruptedException {
        if (mainValueJson == null){
            mainValueJson = new JSONObject(mainValueStr);
        }

        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String disease = prefs.getString("Disease", "You are super healthy");
        int age = Integer.parseInt(prefs.getString("Age", "0"));
        String gender = prefs.getString("Gender", "Others");

        DailyDao dao = DailyPointDb.getInstance(context).dailyDao();
        int sleep = dao.getSleep();
        int points = dao.getMoodPoints();
        int hydrated = dao.getHydrated();

        if (hydrated == 0)
            points += 1;

        if (disease.equals("You are super healthy"))
            return 0;
        if (disease.equals("Schizophrenia") && gender.equals("Male"))
            disease += "-Male";

        JSONArray diseaseRow = mainValueJson.getJSONArray(disease);
        Thread.sleep(100);

        for (int i = 0; i < diseaseRow.length(); i++) {
            int max_age = (int) diseaseRow.getJSONObject(i).get("age_max");
            if (age <= max_age){
                int sleep_min = (int) diseaseRow.getJSONObject(i).get("sleep_min");
                int sleep_max = (int) diseaseRow.getJSONObject(i).get("sleep_max");
                int point = (int) diseaseRow.getJSONObject(i).get("value");
                if (sleep_min > sleep || sleep > sleep_max)
                    points += (1 + point);
                else
                    points += point;
                return points;
            }
        }
        return 0;
    }
}
