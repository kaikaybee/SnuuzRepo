package com.example.snuuz;

// Imports for Android
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

// Imports for AnyChart
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.Chart;

import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;

import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
//import com.example.snuuz.dbObject;

public class HistoryActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // UI action bar and status bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.yourTranslucentColor)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Declare line chart and properties
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.title("Sleep Data");

        // Data entry
        List<DataEntry> seriesData = new ArrayList<>();
        //middle value = waketime
        //last value = sleeptime


        String dates ;
        String wakeTimes;
        String sleepTimes;
        double wakeTimeDouble;
        double sleepTimeDouble;
        for(int i=1; i<=3; i++){
            dates = MainActivity.db.getDates(i);
            wakeTimes = MainActivity.db.getwakeTimes(i);
            wakeTimeDouble = TimeScaledParser(wakeTimes);
            sleepTimes = MainActivity.db.getsleepTimes(i);
            sleepTimeDouble = TimeScaledParser(sleepTimes);

            DecimalFormat d = new DecimalFormat("#.##");
            double wake = Double.parseDouble(d.format(wakeTimeDouble));
            double sleep = Double.parseDouble(d.format(sleepTimeDouble));
            seriesData.add(new CustomDataEntry(dates, wake,sleep));
        }

        //seriesData.add(new CustomDataEntry("12-01-2019", 5, 5));
        //seriesData.add(new CustomDataEntry("Two", 2.55444444444444444444444, 4));
        //seriesData.add(new CustomDataEntry("Three", 3, 3));
        //seriesData.add(new CustomDataEntry("Four", 4, 2));
        //seriesData.add(new CustomDataEntry("Five", 5, 1));



        // Format data
        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping lineData = set.mapAs("{x: 'x', value: 'value'}");
        Mapping columnData = set.mapAs("{x: 'x', value: 'value2'}");

        Column col = cartesian.column(columnData);
        col.tooltip().format("{%value2}");


        col.name("Hours slept");

        Line line = cartesian.line(lineData);
        //line.tooltip().format("hello");
        line.name("Time waken up");
        //cartesian.tooltip().format("hello");
        /*cartesian.tooltip().format("function() {\n" +
                "      return  1+3;\n" +
                "    }");*/
        //cartesian.yAxis(0)
        //cartesian.tooltip().format("{value2} + 2");
        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setChart(cartesian);
    }
    public static double TimeScaledParser(String Time){
        String Hour="";
        String Min="";
        String zero = "0";
        if(Time.startsWith("0")){
            Hour = Time.substring(1,2);
        }
        else{
            Hour = Time.substring(0,2);
        }
        if(Time.substring(3,4).equals(zero)){
            Min = Time.substring(4,5);
        }
        else {
            Min = Time.substring(3,5);
        }
        double hour = (double)Integer.parseInt(Hour);
        double min = (double)Integer.parseInt(Min);
        double time = hour+(min/60);

     return time;
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
