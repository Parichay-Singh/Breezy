package com.patrollers.breezy.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.patrollers.breezy.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.patrollers.breezy.database.DailyDao;
import com.patrollers.breezy.database.DailyPointDb;
import com.patrollers.breezy.models.DailyPoints;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static com.patrollers.breezy.helper.ValueTable.getPoints;

public class HomeFragment extends Fragment {

    @BindView(R.id.username) TextView username;
    @BindView(R.id.detect_disease) TextView detect_disease;
    @BindView(R.id.status_health) TextView status_health;
    @BindView(R.id.tasks_status) TextView tasks_status;
    @BindView(R.id.graphView) LineChart graphView;
    @BindView(R.id.animationView)
    LottieAnimationView animationView;

    private DailyDao dailyDao;
    private SharedPreferences.Editor Ed;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, root);

        SharedPreferences userPrefs = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        Ed = userPrefs.edit();
        username.setText(userPrefs.getString("Name", "Name") + ",");
        detect_disease.setText(userPrefs.getString("Disease", "None"));

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(new Date());

        dailyDao = DailyPointDb.getInstance(getContext()).dailyDao();
        if (dailyDao.isDatePresent(date) == 0)
            showDailyDialog(date);
        else {
            try {
                int valuePoints = getPoints(getContext());
                Ed.putInt("ValuePoints", valuePoints);
                Ed.apply();
                Log.e("Info", valuePoints + "");
            } catch (JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        boolean exr = dailyDao.getExercise();
        boolean med = dailyDao.getMeditate();

        if (exr && med) {
            tasks_status.setText("Completed");
            animationView.setVisibility(View.VISIBLE);
        } else if (exr || med)
            tasks_status.setText("1 task left");
        else
            tasks_status.setText("Pending");

        List<Integer> graphPoints = dailyDao.getGraphPoints();
        int listSize = graphPoints.size() - 1;

        Log.e("Info", listSize + "hi");

        graphView.setDragEnabled(true);
        graphView.setScaleEnabled(true);
        graphView.getAxisRight().setDrawGridLines(false);
        graphView.getAxisLeft().setDrawGridLines(false);
        graphView.getXAxis().setDrawGridLines(false);
        graphView.getAxisLeft().setDrawLabels(false);
        graphView.getAxisRight().setDrawLabels(false);
        graphView.getXAxis().setDrawLabels(false);

        List<Entry> yValues = new ArrayList<>();
        if (listSize != -1) {
            for (int i = listSize; i >= 0; i--) {
                yValues.add(new Entry(listSize - i, graphPoints.get(i)));
            }

            if (graphPoints.get(listSize) > graphPoints.get(0))
                status_health.setText("Improving");
            else
                status_health.setText("Deteriorating, take care!");
        }

        LineDataSet set = new LineDataSet(yValues, "Daily Mood Graph");
        set.setFillAlpha(110);

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_graph);
        set.setDrawFilled(true);
        set.setFillDrawable(drawable);
        set.setColor(Color.rgb(0,119,182));
        set.setLineWidth(1.5f);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        LineData data = new LineData(dataSets);
        graphView.setData(data);

        return root;
    }

    private void showDailyDialog(String date) {
        Dialog dailyDialog = new Dialog(getContext());
        dailyDialog.setContentView(R.layout.daily_dialog);
        dailyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dailyDialog.setCancelable(false);
        dailyDialog.show();

        Button sleep_decrease = dailyDialog.findViewById(R.id.sleep_decrease);
        Button sleep_increase = dailyDialog.findViewById(R.id.sleep_increase);
        Button submit_daily = dailyDialog.findViewById(R.id.submit_daily);
        TextView sleep_no = dailyDialog.findViewById(R.id.sleep_no);

        CardView happy = dailyDialog.findViewById(R.id.happy_card);
        CardView normal = dailyDialog.findViewById(R.id.normal_card);
        CardView sad = dailyDialog.findViewById(R.id.sad_card);
        CardView depress = dailyDialog.findViewById(R.id.depress_card);
        CardView angry = dailyDialog.findViewById(R.id.angry_card);

        Button hydrate_yes = dailyDialog.findViewById(R.id.hydrate_yes);
        Button hydrate_no = dailyDialog.findViewById(R.id.hydrate_no);

        AtomicInteger sleep_hours = new AtomicInteger(0);
        AtomicInteger mood = new AtomicInteger(0);
        AtomicBoolean isHyderated = new AtomicBoolean(false);

        sleep_decrease.setEnabled(false);
        hydrate_yes.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#555555")));

        sleep_increase.setOnClickListener(view -> {
            sleep_decrease.setEnabled(true);
            sleep_no.setText(String.valueOf(sleep_hours.incrementAndGet()));
            if (sleep_hours.get() == 18)
                sleep_increase.setEnabled(false);
        });

        sleep_decrease.setOnClickListener(view -> {
            sleep_increase.setEnabled(true);
            sleep_no.setText(String.valueOf(sleep_hours.decrementAndGet()));
            if (sleep_hours.get() == 0)
                sleep_decrease.setEnabled(false);
        });

        happy.setOnClickListener(view -> {
            mood.set(1);
            setCardsBackground(normal, sad, depress, angry);
            happy.setCardBackgroundColor(Color.parseColor("#00B4D8"));
        });
        normal.setOnClickListener(view -> {
            mood.set(2);
            setCardsBackground(happy, sad, depress, angry);
            normal.setCardBackgroundColor(Color.parseColor("#0096C7"));
        });
        sad.setOnClickListener(view -> {
            mood.set(3);
            setCardsBackground(normal, happy, depress, angry);
            sad.setCardBackgroundColor(Color.parseColor("#0077B6"));
        });
        depress.setOnClickListener(view -> {
            mood.set(4);
            setCardsBackground(normal, sad, happy, angry);
            depress.setCardBackgroundColor(Color.parseColor("#023E8A"));
        });
        angry.setOnClickListener(view -> {
            mood.set(5);
            setCardsBackground(normal, sad, depress, happy);
            angry.setCardBackgroundColor(Color.parseColor("#03045E"));
        });

        hydrate_yes.setOnClickListener(view -> {
            isHyderated.set(true);
            hydrate_yes.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B1D38")));
            hydrate_no.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#555555")));
        });

        hydrate_no.setOnClickListener(view -> {
            isHyderated.set(false);
            hydrate_no.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B1D38")));
            hydrate_yes.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#555555")));
        });

        submit_daily.setOnClickListener(view -> {
            if (mood.get() != 0) {
                DailyPoints point = new DailyPoints(date, sleep_hours.get(), mood.get(), isHyderated.get());
                dailyDao.insertDaily(point);
                dailyDialog.dismiss();
                try {
                    int valuePoints = getPoints(getContext());
                    dailyDao.updatePoints(valuePoints);
                    Ed.putInt("ValuePoints", valuePoints);
                    Ed.apply();
                    Log.e("Info", valuePoints + "");
                } catch (JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else
                Snackbar.make(view, "Please select your mood!", Snackbar.LENGTH_LONG).show();
        });
    }

    private void setCardsBackground(CardView card1, CardView card2, CardView card3, CardView card4) {
        int color = Color.parseColor("#555555");
        card1.setCardBackgroundColor(color);
        card2.setCardBackgroundColor(color);
        card3.setCardBackgroundColor(color);
        card4.setCardBackgroundColor(color);
    }
}