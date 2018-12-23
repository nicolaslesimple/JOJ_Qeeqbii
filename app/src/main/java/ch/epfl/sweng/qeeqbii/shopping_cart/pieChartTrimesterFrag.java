package ch.epfl.sweng.qeeqbii.shopping_cart;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.activities.StatisticsActivity;
import ch.epfl.sweng.qeeqbii.custom_exceptions.ProductException;
import ch.epfl.sweng.qeeqbii.open_food.Product;

public class pieChartTrimesterFrag extends SimpleFragment implements OnChartGestureListener {

    public static Fragment newInstance() {
        return new pieChartTrimesterFrag();
    }

    private BarChart mChart;
    private PieChart mChartPie; //For calories
    private PieChart mChartPieSalt;
    private PieChart mChartPieFats;
    private PieChart mChartPieGlucides;

    private final float[] yDataCalories = {0, 2000*30};
    private final float[] yDataFats= {0, 70*30};
    private final float[] yDataSugars = {0, 55*30};
    private final float[] yDataSalts = {0, 5*30};

    private List<Float> mSalts = new ArrayList<>();
    private List<Float> mGlucides = new ArrayList<>();
    private List<Float> mFats = new ArrayList<>();
    private List<Float> mCalories = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2_shopping_cart_statistics, container, false);

        // NEW BAR CHART
        mChart = new BarChart(getActivity());
        mChart.getDescription().setEnabled(false);
        mChart.setOnChartGestureListener(this);

        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        List<BarEntry> entries = new ArrayList<>();

        //GIVES THE VALUES FOR THE GRAPHS
        try {
            fillLists();
        } catch (ProductException e) {
            e.printStackTrace();
        }

        entries.add(new BarEntry(0f, sumList(mCalories)));
        entries.add(new BarEntry(2f, sumList(mFats)));
        entries.add(new BarEntry(4f, sumList(mGlucides)));
        entries.add(new BarEntry(6f, sumList(mSalts)));

        BarDataSet set = new BarDataSet(entries, "Nutrients Intake over the last Month");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        mChart.setData(data);
        mChart.setFitBars(true); // make the x-axis fit exactly all bars
        mChart.invalidate(); // refresh

        Legend legend = mChart.getLegend();
        legend.setTypeface(tf);
        //legend.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "Set1", "Set2", "Set3", "Set4" });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setAxisMaximum(2000*30);

        mChart.getAxisRight().setEnabled(false);

        final ArrayList<String> xLabel = new ArrayList<>();
        xLabel.add("Calories");
        xLabel.add("");
        xLabel.add("Fats");
        xLabel.add("");
        xLabel.add("Glucides");
        xLabel.add("");
        xLabel.add("Salts");
        xLabel.add("");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int)value);
            }
        });
        xAxis.setEnabled(true);

        //programatically add the chart
        FrameLayout parent = (FrameLayout) v.findViewById(R.id.barChartTrimester);
        parent.addView(mChart);

        yDataSugars[0] = sumList(mGlucides);
        yDataSalts[0] = sumList(mSalts);
        yDataFats[0] = sumList(mFats);
        yDataCalories[0] = sumList(mCalories);

        //GRAPH CONTAINS THE INFORMATION ABOUT THE CALORIES
        mChartPie = (PieChart) v.findViewById(R.id.idPieChartTabTrimester);
        mChartPie.getDescription().setEnabled(false);

        mChartPie.setCenterTextTypeface(tf);
        mChartPie.setCenterText(generateCenterText( "Calories"));
        mChartPie.setCenterTextSize(10f);
        mChartPie.setCenterTextTypeface(tf);

        // radius of the center hole in percent of maximum radius
        mChartPie.setHoleRadius(70f);
        mChartPie.setTransparentCircleRadius(50f);

        Legend l = mChartPie.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        generateGraph(yDataCalories, mChartPie, "Calories");

        //GRAPH CONTAINS THE INFORMATION ABOUT THE CALORIES
        mChartPieSalt = (PieChart) v.findViewById(R.id.idPieChartSaltTabTrimester);
        mChartPieSalt.getDescription().setEnabled(false);

        mChartPieSalt.setCenterTextTypeface(tf);
        mChartPieSalt.setCenterText(generateCenterText("Salts"));
        mChartPieSalt.setCenterTextSize(10f);
        mChartPieSalt.setCenterTextTypeface(tf);

        // radius of the center hole in percent of maximum radius
        mChartPieSalt.setHoleRadius(70f);
        mChartPieSalt.setTransparentCircleRadius(50f);

        Legend legendSalt = mChartPieSalt.getLegend();
        legendSalt.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legendSalt.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legendSalt.setOrientation(Legend.LegendOrientation.VERTICAL);
        legendSalt.setDrawInside(false);

        generateGraph(yDataSalts, mChartPieSalt, "Salts");

        //GRAPH CONTAINS THE INFORMATION ABOUT THE CALORIES
        mChartPieFats = (PieChart) v.findViewById(R.id.idPieChartFatTabTrimester);
        mChartPieFats.getDescription().setEnabled(false);

        mChartPieFats.setCenterTextTypeface(tf);
        mChartPieFats.setCenterText(generateCenterText("Fats"));
        mChartPieFats.setCenterTextSize(10f);
        mChartPieFats.setCenterTextTypeface(tf);

        // radius of the center hole in percent of maximum radius
        mChartPieFats.setHoleRadius(70f);
        mChartPieFats.setTransparentCircleRadius(50f);

        Legend lFat = mChartPieFats.getLegend();
        lFat.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lFat.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        lFat.setOrientation(Legend.LegendOrientation.VERTICAL);
        lFat.setDrawInside(false);

        generateGraph(yDataFats, mChartPieFats, "Fats");

        //GRAPH CONTAINS THE INFORMATION ABOUT THE CALORIES
        mChartPieGlucides = (PieChart) v.findViewById(R.id.idPieChartGlucideTabTrimester);
        mChartPieGlucides.getDescription().setEnabled(false);

        mChartPieGlucides.setCenterTextTypeface(tf);
        mChartPieGlucides.setCenterText(generateCenterText("Glucides"));
        mChartPieGlucides.setCenterTextSize(10f);
        mChartPieGlucides.setCenterTextTypeface(tf);

        // radius of the center hole in percent of maximum radius
        mChartPieGlucides.setHoleRadius(70f);
        mChartPieGlucides.setTransparentCircleRadius(50f);

        Legend lGlucides = mChartPieGlucides.getLegend();
        lGlucides.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lGlucides.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        lGlucides.setOrientation(Legend.LegendOrientation.VERTICAL);
        lGlucides.setDrawInside(false);

        generateGraph(yDataSugars, mChartPieGlucides, "Glucides");

        return v;
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START");
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END");
        mChart.highlightValues(null);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    private SpannableString generateCenterText(String name) {
        SpannableString s = new SpannableString(name + "\n  " + "\n 3 Months");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
    }

    //THE list cannot be empty
    private void fillLists() throws ProductException {
        if(!StatisticsActivity.m_items_trimester.isEmpty())
        {
            mCalories.clear(); //We have to clear the lists in order to not load twice the same information
            mFats.clear();
            mGlucides.clear();
            mSalts.clear();

            for (Product element : StatisticsActivity.m_items_trimester)
            {
                Map<String,Double> nutrients = element.getParsedNutrients();

                if (nutrients.containsKey("Énergie (kCal)"))
                {
                    mCalories.add(nutrients.get("Énergie (kCal)").floatValue());
                }
                if (nutrients.containsKey("Matières grasses (g)"))
                {
                    mFats.add(nutrients.get("Matières grasses (g)").floatValue());
                }
                if (nutrients.containsKey("Sucres (g)"))
                {
                    mGlucides.add(nutrients.get("Sucres (g)").floatValue());
                }
                if (nutrients.containsKey("Sel (g)")) {
                    mSalts.add(nutrients.get("Sel (g)").floatValue());
                }
            }
        }
        else {
            //In case the list is empty at the beginning by doing so we have not iterator on empyty list issue
            //CHANGE TO ZERO AGAIN !!!
            mSalts.add(0f);
            mGlucides.add(0f);
            mFats.add(0f);
            mCalories.add(0f);
        }
    }

    //Returns the sum of all the elements in the list
    private Float sumList(List<Float> list) {
        if (list.isEmpty()) {
            return 0f;
        }

        float sum = 0;
        for (float element : list) {
            sum += element;
        }
        return sum;
    }

    private void generateGraph(float[] yData, PieChart pie, String nameGraph) {

        ArrayList<PieEntry> yEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], i));
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, nameGraph + " in 100g / month needs");
        pieDataSet.setSliceSpace(0); //sets the size of the yEntrys on the graph
        pieDataSet.setValueTextSize(0);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(0, 153, 153));
        colors.add(Color.DKGRAY);
        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legendPie = pie.getLegend();
        legendPie.setForm(Legend.LegendForm.SQUARE);

        Description description = pie.getDescription();
        float percentage = yData[0]/yData[1]*100;
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        String str_per = numberFormat.format(percentage);
        description.setText(str_per + "% of your Monthly needs in " + nameGraph + ".          ");

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pie.setData(pieData);
        pie.setEnabled(true);
    }
}