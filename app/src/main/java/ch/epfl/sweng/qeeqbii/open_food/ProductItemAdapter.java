package ch.epfl.sweng.qeeqbii.open_food;

/**
 * Created by sergei on 11/30/17.
 * This class allows to draw a pair of <Text, BarChart>
 * in the ListView
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import ch.epfl.sweng.qeeqbii.R;

public class ProductItemAdapter extends BaseAdapter {
    public static class ProductItem {
        public String name;
        public int image;
    }

    private ArrayList<ProductItem> items = null;

    // context (see BaseAdapter docs)
    private Context mContext = null;

    // create empty list
    public ProductItemAdapter(Context context, ArrayList<ProductItem> items) {
        this.items = items;
        mContext = context;
    }

    public void clear()
    {
        this.items.clear();
    }

    // number of elements
    @Override
    public int getCount() {
        return items.size();
    }

    public ProductItem getItemAtPosition(int position) {
        return items.get(position);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
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
            convertView = inflater.inflate(R.layout.list_item_recently_scanned_product, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.recently_scanned_product_text_view);
            holder.image = (ImageView) convertView.findViewById(R.id.recentProductImage);
            convertView.setTag(holder);
        } else {
            Log.d("STATE", "getView " + position + " already exists");
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(items.get(position).name);
        holder.image.setImageResource(items.get(position).image);

        return convertView;
    }

    // this class represents a single line in the list
    private class ViewHolder {
        TextView name;
        ImageView image;
    }
}