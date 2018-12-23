package ch.epfl.sweng.qeeqbii.comparison;

/**
 * Created by sergei on 11/30/17.
 * This class allows to draw a pair of <Text, BarChart>
 * in the ListView
 */

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.qeeqbii.R;

public class ComparisonGraphAdapter extends BaseAdapter {
    // contained product lines
    private List<ProductsLine> lines = null;

    // data for the bar charts
    private ArrayList<BarData> data = null;

    // context (see BaseAdapter docs)
    private Context mContext = null;

    // create empty list
    public ComparisonGraphAdapter() {
        lines = new ArrayList<ProductsLine>();
        data = new ArrayList<BarData>();
    }

    // preprocessing data and creating objects for the bar chart
    private static BarData getBarData(ProductsLine line) {
        // two valuesets, which would have a single value
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();

        // adding nutrient values to valuesets
        valueSet1.add(new BarEntry(1, line.value1.floatValue()));
        valueSet2.add(new BarEntry(0, line.value2.floatValue()));

        // creating two datasets, each valueset goes to its own dataset
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Product 1");
        barDataSet1.setColor(Color.rgb(0, 155, 0));

        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Product 2");
        barDataSet1.setColor(Color.rgb(155, 0, 0));

        // creating data consisting of two datasets
        BarData data = new BarData();
        data.addDataSet(barDataSet1);
        data.addDataSet(barDataSet2);

        return data;
    }

    // initialize chart appearance
    private static void initChart(BarChart chart) {
        // disable scrolling and zooming
        chart.setPinchZoom(false);
        chart.setNestedScrollingEnabled(false);
        chart.setScaleEnabled(false);
        chart.setTouchEnabled(false);


        // set up x axis
        chart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
        chart.getXAxis().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);

        // set up left axis
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisLeft().setDrawGridLines(false);

        // set up right axis
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);

        chart.setDrawGridBackground(false);
    }

    // add multiple lines to a bar chart
    private void setData(BarChart chart, Integer position) {
        // set data in the chart
        chart.setData(data.get(position));

        // set nutrient name as description
        chart.getDescription().setText(lines.get(position).criteria);

        // fix the chart data mangled on scrolling
        // see https://github.com/PhilJay/MPAndroidChart/issues/1480
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    public void setContext(Context context) {
        mContext = context;
    }

    // add a product line
    public void addLine(ProductsLine line) {
        lines.add(line.copy());
        data.add(getBarData(line));
    }

    // number of elements
    @Override
    public int getCount() {
        return lines.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // paints one element in the list
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (position >= getCount())
            return (convertView);

        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            Log.d("STATE", "getView " + position + " creating");
            convertView = inflater.inflate(R.layout.activity_product_comparison_graph, parent, false);
            holder = new ViewHolder();
            holder.criteria = (TextView) convertView.findViewById(R.id.criteria);
            holder.chart = (HorizontalBarChart) convertView.findViewById(R.id.chart);
            initChart(holder.chart);
            convertView.setTag(holder);
        } else {
            Log.d("STATE", "getView " + position + " already exists");
            holder = (ViewHolder) convertView.getTag();
        }

        // setting text
        holder.criteria.setText(lines.get(position).criteria);

        // adding data to the chart
        setData(holder.chart, position);

        return convertView;
    }

    // this class represents a single line in the list
    private class ViewHolder {
        TextView criteria;
        HorizontalBarChart chart;
    }
}