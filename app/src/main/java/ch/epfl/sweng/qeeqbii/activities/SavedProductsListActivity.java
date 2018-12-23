package ch.epfl.sweng.qeeqbii.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.Slider;
import ch.epfl.sweng.qeeqbii.open_food.ProductItemAdapter;
import ch.epfl.sweng.qeeqbii.open_food.RecentlyScannedProducts;
import ch.epfl.sweng.qeeqbii.open_food.SavedProductsDatabase;
import ch.epfl.sweng.qeeqbii.open_food.Product;
import ch.epfl.sweng.qeeqbii.open_food.Date;

/**
 * Created by guillaume on 14/11/17.
 * This activity shows the list of dates present in the history of scanned products.
 */

public class SavedProductsListActivity extends AppCompatActivity {

    private static final String TAG = "SavedProductsListActiv";

    private Map<String, Integer> name_to_index_map = new HashMap<>();
    private ActionBarDrawerToggle mToggle;

    Product[] mProducts;

    ProductItemAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_scanned_products);

        ListView list_saved_products= (ListView) findViewById(R.id.recently_scanned_products_list_view);

        String[] product_names = new String[0];

        ArrayList<ProductItemAdapter.ProductItem> listItems = new ArrayList<>();

        try
        {
            SavedProductsDatabase.load(getApplicationContext());
            mProducts = SavedProductsDatabase.getProductsFromDate((Date) getIntent().getSerializableExtra("date"));
            product_names = new String[mProducts.length];
            int i = 0;
            for (Product prod : mProducts)
            {
                product_names[i] = prod.getName();
                ProductItemAdapter.ProductItem item = new ProductItemAdapter.ProductItem();
                item.name = prod.getName();
                item.image = prod.getCluster().getImageId();
                listItems.add(item);
                name_to_index_map.put(prod.getName(), i);
                ++i;
            }
        } catch (Exception e)
        {
            Log.d(TAG,e.getMessage());
        }


        mAdapter = new ProductItemAdapter(this.getApplicationContext(), listItems);

        list_saved_products.setAdapter(mAdapter);

        list_saved_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position,
                                    long arg3) {
                System.out.println("////////////////////////////////////////////////////////////CLICKED");
                Intent intent = new Intent(SavedProductsListActivity.this, ShowProductActivity.class);
                System.out.println("////////////////////////////////////////////////////////////INTENT CREATED");
                String txt = (String) ((ProductItemAdapter.ProductItem) adapter.getItemAtPosition(position)).name;
                System.out.println("////////////////////////////////////////////////////////////TXT OK");
                intent.putExtra("product", mProducts[name_to_index_map.get(txt)]);
                System.out.println("////////////////////////////////////////////////////////////" + mProducts[name_to_index_map.get(txt)].toString());
                startActivity(intent);
            }

        });
    }

    public void shareOnFacebookRecentlyScanned(View view)
    {
        Intent intent = new Intent(this, ShareOnFacebookActivity.class);
        view.setVisibility(View.INVISIBLE);
        ShareOnFacebookActivity.view = findViewById(R.id.recently_scanned_products);
        startActivity(intent);
    }

    public void deleteItems(View view) {
        RecentlyScannedProducts.clear();
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }

    // go to product comparison onClick handler
    public void productComparison(View view) {
        Intent intent = new Intent(this, ProductComparisonActivity.class);
        startActivity(intent);
    }

    public ProductItemAdapter getmAdapter() {
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
