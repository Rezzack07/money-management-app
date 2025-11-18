package com.tasya.smartbudget;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class IncomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_income);

        // FIX: inset
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // -------------------------
        //  SETUP PIE CHART (DONUT)
        // -------------------------
        PieChart pieChart = findViewById(R.id.donutChart);

        // Disable description label
        pieChart.getDescription().setEnabled(false);

        // Disable entry labels
        pieChart.setDrawEntryLabels(false);

        // Donut mode
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(60f);                   // donut hole size
        pieChart.setTransparentCircleRadius(65f);
        pieChart.setHoleColor(Color.WHITE);

        // Disable rotation
        pieChart.setRotationEnabled(false);

        // Data slices
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(40f));
        entries.add(new PieEntry(20f));
        entries.add(new PieEntry(10f));
        entries.add(new PieEntry(15f));
        entries.add(new PieEntry(15f));

        // Colors like Figma
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#1E88E5")); // Blue
        colors.add(Color.parseColor("#26C6DA")); // Cyan
        colors.add(Color.parseColor("#8E24AA")); // Purple
        colors.add(Color.parseColor("#FFB300")); // Yellow
        colors.add(Color.parseColor("#66BB6A")); // Green

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(4f);                 // spacing between slices
        dataSet.setSelectionShift(0f);             // disable shift

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);                 // remove numbers on slices

        // Set to chart
        pieChart.setData(data);
        pieChart.invalidate();                     // refresh
    }
}
