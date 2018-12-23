package ch.epfl.sweng.qeeqbii.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.Slider;
import ch.epfl.sweng.qeeqbii.open_food.OpenFoodQuery;
import ch.epfl.sweng.qeeqbii.open_food.Product;

public class GraphsActivity extends AppCompatActivity {

    private static final String TAG = "GraphsActivity";
    private PieChart pieChart;
    private final float[] yDataCalories = {0, 2000};
    private final float[] yDataFats= {0, 70};
    private final float[] yDataSugars = {0, 55};
    private final float[] yDataSalts = {0, 5};
    private final String[] xData = {"Completed", "Left"};
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);
        Log.d(TAG, "onCreate: starting to create chart");

        Intent intent = getIntent();

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.GraphsLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (intent.hasExtra("barcode") | intent.hasExtra("product"))
        {
            Product product = new Product();
            TextView txt = (TextView) findViewById(R.id.product_name_graph_activity);

            if (intent.hasExtra("barcode"))
            {
                String barcode = intent.getExtras().getString("barcode");
                try
                {
                    product = OpenFoodQuery.get(barcode);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            } else {
                product = (Product) intent.getSerializableExtra("product");
            }

            txt.setText(product.getName());

            try
            {
                Map<String,Double> nutrients = product.getParsedNutrients();
                if (nutrients.containsKey("Énergie (kCal)"))
                {
                    yDataCalories[0] = nutrients.get("Énergie (kCal)").floatValue();
                }
                if (nutrients.containsKey("Matières grasses (g)"))
                {
                    yDataFats[0] = nutrients.get("Matières grasses (g)").floatValue();
                }
                if (nutrients.containsKey("Sucres (g)"))
                {
                    yDataSugars[0] = nutrients.get("Sucres (g)").floatValue();
                }
                if (nutrients.containsKey("Sel (g)")) {
                    yDataSalts[0] = nutrients.get("Sel (g)").floatValue();
                }

            }
            catch (Exception e)
            {
                Log.d(TAG, e.getMessage());
            }
        }

        createGraph(R.id.idPieChartCalories, getString(R.string.calories_graph));
        createGraph(R.id.idPieChartFats, getString(R.string.fat_graph));
        createGraph(R.id.idPieChartSugars, getString(R.string.sugar_graph));
        createGraph(R.id.idPieChartSalts, getString(R.string.salt_graph));
    }

    private void createGraph(final int numberChart, final String nameGraph) {
        pieChart = (PieChart) findViewById(numberChart);
        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(80f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText(nameGraph);
        pieChart.setCenterTextSize(13);
        //pieChart.setDrawEntryLabels(true);
        //pieChart.setEntryLabelTextSize(20);
        //More options just check out the documentation!

        float[] yData = {0, 1};
        if (numberChart == R.id.idPieChartCalories) yData = yDataCalories;
        if (numberChart == R.id.idPieChartFats) yData = yDataFats;
        if (numberChart == R.id.idPieChartSugars) yData = yDataSugars;
        if (numberChart == R.id.idPieChartSalts) yData = yDataSalts;

        final float[] final_yData = yData;

        addDataSet(nameGraph,final_yData);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());

                int pos1 = e.toString().indexOf("(sum): ");
                String sales = e.toString().substring(pos1 + 7);

                for (int i = 0; i < final_yData.length; i++) {
                    if (final_yData[i] == Float.parseFloat(sales)) {
                        pos1 = i;
                        break;
                    }
                }
                String employee = xData[pos1 + 1];
                Toast.makeText(GraphsActivity.this, "Employee " + employee + "\n" + "Sales: $" + sales + "K", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    private void addDataSet(String nameGraph, float[] yData) {
        Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        //ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], i));
        }

        /*for (int i = 1; i < xData.length; i++) {
            xEntrys.add(xData[i]);
        }*/

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, nameGraph + " in 100g / daily needs");
        pieDataSet.setSliceSpace(0); //sets the size of the yEntrys on the graph
        pieDataSet.setValueTextSize(0);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.GRAY);
        /*colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);*/

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);

        Description description = pieChart.getDescription();
        float percentage = yData[0]/yData[1]*100;
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        String str_per = numberFormat.format(percentage);
        description.setText( str_per + "% of your daily need in " + nameGraph + ".          ");

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        //pieChart.invalidate();
        pieChart.setEnabled(true);
    }

    public void shareOnFacebook(View view)
    {
        Intent intent = new Intent(this, ShareOnFacebookActivity.class);
        view.setVisibility(View.INVISIBLE);
        ShareOnFacebookActivity.view = findViewById(R.id.GraphsLayout);
        startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        findViewById(R.id.fab1).setVisibility(View.VISIBLE);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    public void sliderGoToActivity(MenuItem item) {
        Slider slider = new Slider();
        slider.goToActivity(item, this);
    }
}