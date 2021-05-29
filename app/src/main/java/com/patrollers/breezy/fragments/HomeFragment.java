package com.patrollers.breezy.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.patrollers.breezy.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    @BindView(R.id.username) TextView username;
    @BindView(R.id.detect_disease) TextView detect_disease;
    @BindView(R.id.graphView) LineChart graphView;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, root);

        SharedPreferences userPrefs = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        username.setText(userPrefs.getString("Name", "Name") + ",");
        detect_disease.setText(userPrefs.getString("Disease", "None"));

        graphView.setDragEnabled(true);
        graphView.setScaleEnabled(true);
        graphView.getAxisRight().setDrawGridLines(false);
        graphView.getAxisLeft().setDrawGridLines(false);
        graphView.getXAxis().setDrawGridLines(false);
        graphView.getAxisLeft().setDrawLabels(false);
        graphView.getAxisRight().setDrawLabels(false);
        graphView.getXAxis().setDrawLabels(false);

        List<Entry> yValues = new ArrayList<>();
        yValues.add(new Entry(0, 60f));
        yValues.add(new Entry(1, 50f));
        yValues.add(new Entry(2, 70f));
        yValues.add(new Entry(3, 30f));
        yValues.add(new Entry(4, 50f));
        yValues.add(new Entry(5, 60f));
        yValues.add(new Entry(6, 65f));

        LineDataSet set = new LineDataSet(yValues, "Mood Graph");
        set.setFillAlpha(110);

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_graph);
        set.setDrawFilled(true);
        set.setFillDrawable(drawable);
        set.setColor(Color.rgb(0, 119, 182));
        set.setLineWidth(1.5f);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        LineData data = new LineData(dataSets);

        graphView.setData(data);

        return root;
    }
}