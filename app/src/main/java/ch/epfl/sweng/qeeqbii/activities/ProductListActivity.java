package ch.epfl.sweng.qeeqbii.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.Slider;
import ch.epfl.sweng.qeeqbii.clustering.ClusterTypeSecondLevel;
import ch.epfl.sweng.qeeqbii.open_food.Product;

/**
 * Created by guillaume on 01/12/17.
 * A simple list of products
 */

public class ProductListActivity extends AppCompatActivity {

    private static final String TAG = "ProductListActivity";
    private List<Product> product_list = new ArrayList<>();
    private Map<String, Integer> name_to_index_map = new HashMap<>();
    ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mToggle;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_scanned_products);

        ListView list_saved_products= (ListView) findViewById(R.id.recently_scanned_products_list_view);

        String[] product_names = new String[0];

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.SavedProductsLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try
        {
            String which_list_to_get = getIntent().getStringExtra("product_list");
            if( which_list_to_get.equals("ShoppingList") )
            {
                product_list = ShoppingListActivity.getProductList(ClusterTypeSecondLevel.getClusterType(getIntent()
                        .getStringExtra("cluster")));
            }

            product_names = new String[product_list.size()];
            int i = 0;
            for (Product prod : product_list)
            {
                product_names[i] = prod.getName();
                name_to_index_map.put(prod.getName(), i);
                ++i;
            }
        } catch (Exception e)
        {
            Log.d(TAG,e.getMessage());
        }

        mAdapter = new ArrayAdapter<>(this.getApplicationContext(), R.layout.list_item_recently_scanned_product,
                R.id.recently_scanned_product_text_view, product_names);

        list_saved_products.setAdapter(mAdapter);

        list_saved_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position,
                                    long arg3) {
                Intent intent = new Intent(ProductListActivity.this, ShowProductActivity.class);
                String txt = (String) adapter.getItemAtPosition(position);
                intent.putExtra("product", product_list.get(name_to_index_map.get(txt)));
                startActivity(intent);
            }

        });
    }

    public void deleteItems(View view) {
        product_list.clear();
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void shareOnFacebookRecentlyScanned(View view)
    {
        Intent intent = new Intent(this, ShareOnFacebookActivity.class);
        view.setVisibility(View.INVISIBLE);
        ShareOnFacebookActivity.view = findViewById(R.id.recently_scanned_products);
        startActivity(intent);
    }

    public ArrayAdapter getmAdapter() {
        return mAdapter;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void sliderGoToActivity(MenuItem item) {
        Slider slider = new Slider();
        slider.goToActivity(item, this);
    }
}
