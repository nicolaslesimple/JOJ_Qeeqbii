package ch.epfl.sweng.qeeqbii.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.RecyclerViewAdapter;
import ch.epfl.sweng.qeeqbii.Slider;
import ch.epfl.sweng.qeeqbii.clustering.ClusterType;
import ch.epfl.sweng.qeeqbii.open_food.Product;
import ch.epfl.sweng.qeeqbii.shopping_cart.ClusterProductList;

import static ch.epfl.sweng.qeeqbii.activities.BarcodeScannerActivity.EXTRA_NEXT;

public class ShoppingListActivity extends AppCompatActivity {

    private static ClusterProductList m_cart = null;
    private static RecyclerViewAdapter mAdapter = null;
    private static String json_file = "shopping_list.json";
    private static List<Product> tmpProductList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);

         if (m_cart == null)
            load(getApplicationContext());

        //create and set layout manager for each RecyclerView

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Initializing and set adapter for each RecyclerView

        View.OnClickListener onclicklistener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(v);
                String txt = m_cart.getSpecificItemInList(itemPosition).toString();
                Intent intent = new Intent(ShoppingListActivity.this, ProductListActivity.class);
                intent.putExtra("product_list", "ShoppingList");
                intent.putExtra("cluster", txt);
                startActivity(intent);
            }
        };

        mAdapter = new RecyclerViewAdapter(this.getLayoutInflater(), m_cart,
                R.layout.item_recycler_view_shopping_list, onclicklistener);
        recyclerView.setAdapter(mAdapter);
        for (Product product: tmpProductList)
        {
            addProduct(product, getApplicationContext());
        }
        tmpProductList = new ArrayList<>();

    }

    // To call when the app is openned.
    public static void load(Context context)
    {
        m_cart = new ClusterProductList(json_file, context);
    }

    public static void load(Context context, String filename)
    {
        mAdapter = null;
        m_cart = new ClusterProductList(filename, context);
    }

    public static void addCluster(ClusterType cluster, Context context)
    {
        if (!(mAdapter.getClusters().contains(cluster))) {
            mAdapter.addClusterAndSave(cluster, context);
        }
    }

    public static void deleteCluster(ClusterType cluster, Context context)
    {
        mAdapter.deleteClusterAndSave(cluster, context);
    }

    public static ClusterProductList getClusterProductList()
    {
        return m_cart;
    }

    public static void addProduct(Product product, Context context)
    {
        if (mAdapter == null) {
            tmpProductList.add(product);
            return;
        }
        if (!(m_cart.getClusters().contains(product.getCluster())))
        {
            addCluster(product.getCluster(), context);
        }
        m_cart.addProductAndSave(product, context);
        mAdapter.update();
    }

    public static void save(Context context) { m_cart.save( json_file, context); }

    public static List<Product> getProductList(ClusterType cluster)
    {
        return m_cart.getProductList(cluster);
    }

    public void showShoppingList(View view) {
        Intent intent = new Intent(this, ShoppingCartFirstLevelActivity.class);
        startActivity(intent);
    }

    public static void checkOrUncheckItem(ClusterType cluster)
    {
        m_cart.checkOrUncheckItem(cluster);
    }

    public void deleteShoppingList(View view) {
        List<ClusterType> clustersToDelete = new ArrayList<>();
        for (ClusterType cluster : m_cart.getClusters()) {
            clustersToDelete.add(cluster);
        }
        for (ClusterType cluster: clustersToDelete)
        {
            deleteCluster(cluster, getApplicationContext());
        }
    }

    public void deleteSingleItem (View view) {
        List<ClusterType> clustersToDelete = new ArrayList<>();
        for (ClusterType cluster : m_cart.getClusters()) {
            if (m_cart.isCheckedItem(cluster)) {
                clustersToDelete.add(cluster);
            }
        }
        for (ClusterType cluster: clustersToDelete)
        {
            deleteCluster(cluster, getApplicationContext());
        }
    }

    public void scanToCheck(View view) {
        Log.d("STATE", "Going to scan barcode");
        Intent intent = new Intent(this, BarcodeScannerActivity.class);
        intent.putExtra(EXTRA_NEXT, this.getClass().getName());
        startActivity(intent);
    }


    public void sliderGoToActivity(MenuItem item) {
        Slider slider = new Slider();
        slider.goToActivity(item, this);
    }
}
