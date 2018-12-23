package ch.epfl.sweng.qeeqbii.activities;

/*
Created by sergei on 30 Nov 2017

This class implements a product comparison activity
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Map;

import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.Slider;
import ch.epfl.sweng.qeeqbii.comparison.ComparisonGraphAdapter;
import ch.epfl.sweng.qeeqbii.comparison.ProductsLine;
import ch.epfl.sweng.qeeqbii.custom_exceptions.ProductException;
import ch.epfl.sweng.qeeqbii.open_food.Product;
import ch.epfl.sweng.qeeqbii.open_food.RecentlyScannedProducts;

import static java.lang.Double.parseDouble;


public class ProductComparisonActivity extends AppCompatActivity {

    private ComparisonGraphAdapter adapter = null;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_comparison);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.ComparisonLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // load list from recently scanned
        updateData();

    }

    // update data from recently scanned products
    public void updateData() {

        // creating list adapter
        adapter = new ComparisonGraphAdapter();
        adapter.setContext(getApplicationContext());

        // obtaining nutrients of products
        // using last two recently scanned products
        LinkedList<String> barcodes = RecentlyScannedProducts.getBarcodeList();
        if (barcodes.size() < 2) {
            Toast.makeText(this, R.string.scan_at_least_two, Toast.LENGTH_SHORT).show();
            Log.d("STATE", "Insufficient products");
            return;
        } else {
            Product product1 = RecentlyScannedProducts.getProduct(barcodes.get(barcodes.size() - 1));
            Product product2 = RecentlyScannedProducts.getProduct(barcodes.get(barcodes.size() - 2));

            // setting names of products
            TextView name1 = (TextView) findViewById(R.id.product_name_1);
            TextView name2 = (TextView) findViewById(R.id.product_name_2);

            name1.setText(product1.getName());
            name2.setText(product2.getName());

            Map<String, Double> nutrients1;
            Map<String, Double> nutrients2;

            String quantity1 = product1.getQuantity();
            String quantity2 = product2.getQuantity();

            Log.d("STATE", "Q1 " + quantity1 + " Q2 " + quantity2);

            // Adding quantity chart
            if (quantity1.length() > 0 && quantity2.length() > 0 &&
                    (quantity1.substring(quantity1.length() - 1).equals(quantity2.substring(quantity2.length() - 1)))) {
                adapter.addLine(new ProductsLine("Quantity (" + quantity2.substring(quantity2.length() - 1) + ")",
                        parseDouble(quantity1.substring(0, quantity1.length() - 1)),
                        parseDouble(quantity2.substring(0, quantity2.length() - 1))));
            }

            try {
                nutrients1 = product1.getParsedNutrients();
                nutrients2 = product2.getParsedNutrients();
            } catch (ProductException e) {
                Toast.makeText(this, R.string.cannot_obtain_nutrients, Toast.LENGTH_SHORT).show();
                Log.d("STATE", getResources().getString(R.string.cannot_obtain_nutrients));
                e.printStackTrace();
                return;
            }

            // if nutrient is present in both products, showing the chart
            for (Map.Entry<String, Double> entry : nutrients1.entrySet()) {
                if (nutrients2.containsKey(entry.getKey())) {
                    ProductsLine line = new ProductsLine(entry.getKey(), entry.getValue(), nutrients2.get(entry.getKey()));
                    adapter.addLine(line);
                    Log.d("STATE", "Adding product" + line.criteria + " " + line.value1 + " " + line.value2);
                }
            }

        }

        ListView list = (ListView) findViewById(R.id.graphs);
        list.setAdapter(adapter);

    }

    // scan barcode button handler
    public void scanBarcode(View w) {
        Intent intent = new Intent(this, BarcodeScannerActivity.class);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void sliderGoToActivity(MenuItem item) {
        Slider slider = new Slider();
        slider.goToActivity(item, this);
    }
}
