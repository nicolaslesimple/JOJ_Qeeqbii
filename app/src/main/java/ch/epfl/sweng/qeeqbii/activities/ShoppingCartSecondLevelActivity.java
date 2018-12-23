package ch.epfl.sweng.qeeqbii.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;

import java.util.Arrays;

import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.RecyclerViewAdapter;
import ch.epfl.sweng.qeeqbii.clustering.ClusterType;
import ch.epfl.sweng.qeeqbii.clustering.ClusterTypeFirstLevel;
import ch.epfl.sweng.qeeqbii.clustering.ClusterTypeSecondLevel;
import ch.epfl.sweng.qeeqbii.shopping_cart.ClusterProductList;

public class ShoppingCartSecondLevelActivity extends AppCompatActivity {

    private ClusterProductList m_cart;

    private static RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_2);

        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_item_shopping_list);

        ClusterType first_level = ClusterTypeFirstLevel.getClusterType(getIntent().getStringExtra("cluster"));

        //create and set layout manager for each RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Initializing and set adapter for each RecyclerView
        ClusterType[] clustertypes = first_level.getChildren();
        m_cart = new ClusterProductList(Arrays.asList(clustertypes));

        View.OnClickListener onclicklistener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(v);
                String txt = m_cart.getSpecificItemInList(itemPosition).toString();
                ClusterTypeSecondLevel cluster = ClusterTypeSecondLevel.getClusterType(txt);
                if (!ShoppingListActivity.getClusterProductList().getClusters().contains(cluster))
                {
                    ShoppingListActivity.addCluster(cluster, getApplicationContext());
                } else {
                    ShoppingListActivity.deleteCluster(cluster, getApplicationContext());
                }
                m_cart.checkOrUncheckItem(cluster);
                mAdapter.changeOpacity(itemPosition);
                mAdapter.notifyDataSetChanged();
            }
        };

        mAdapter = new RecyclerViewAdapter(this.getLayoutInflater(), m_cart,
                R.layout.item_recycler_view_shopping_cart, onclicklistener);

        for (ClusterType cluster: ShoppingListActivity.getClusterProductList().getClusters())
        {
            if (m_cart.getClusters().contains(cluster)) {
                m_cart.checkItem(cluster);
                mAdapter.setOpacityChecked(m_cart.getClusterPosition(cluster));
            }
        }

        recyclerView.setAdapter(mAdapter);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        return Actions.newView("ShoppingList", "http://[ENTER-YOUR-URL-HERE]");
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().start(getIndexApiAction());
    }

    @Override
    public void onStop() {
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().end(getIndexApiAction());
        super.onStop();
    }
}