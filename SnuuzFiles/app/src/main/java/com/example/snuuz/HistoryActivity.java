package com.example.snuuz;

// Imports for Android
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

// Imports for AnyChart
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Sets custom Toolbar to replace built-in actionBar
        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        // Declare line chart and properties
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.title("Sleep Data");

        // Data entry
        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("One", 1, 5));
        seriesData.add(new CustomDataEntry("Two", 2, 4));
        seriesData.add(new CustomDataEntry("Three", 3, 3));
        seriesData.add(new CustomDataEntry("Four", 4, 2));
        seriesData.add(new CustomDataEntry("Five", 5, 1));

        // Format data
        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping lineData = set.mapAs("{x: 'x', value: 'value'}");
        Mapping columnData = set.mapAs("{x: 'x', value: 'value2'}");

        Column col = cartesian.column(columnData);
        col.name("Hours slept");

        Line line = cartesian.line(lineData);
        line.name("Time waken up");

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setChart(cartesian);
    }

    // Helper class for AnyChart
    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }

    //Replaces overflow menu of Toolbar with custom buttons
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_bar, menu);
        return true;
    }

    //Upon a Toolbar button press, loads whichever activity it should
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent settingsIntent = new Intent(HistoryActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            }
            case R.id.action_clock: {
                Intent mainIntent = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(mainIntent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
