package ch.epfl.sweng.qeeqbii;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.qeeqbii.activities.BarcodeScannerActivity;
import ch.epfl.sweng.qeeqbii.activities.CancerDataQueryActivity;
import ch.epfl.sweng.qeeqbii.activities.CancerDataShowActivity;
import ch.epfl.sweng.qeeqbii.activities.DidYouKnowActivity;
import ch.epfl.sweng.qeeqbii.activities.GraphsActivity;
import ch.epfl.sweng.qeeqbii.activities.MainActivity;
import ch.epfl.sweng.qeeqbii.activities.OtherActivity;
import ch.epfl.sweng.qeeqbii.activities.ProductComparisonActivity;
import ch.epfl.sweng.qeeqbii.activities.RecentlyScannedProductsActivity;
import ch.epfl.sweng.qeeqbii.activities.SavedProductsDatesActivity;
import ch.epfl.sweng.qeeqbii.activities.ShoppingListActivity;
import ch.epfl.sweng.qeeqbii.activities.StatisticsActivity;
import ch.epfl.sweng.qeeqbii.chat.MainActivityChat;
import ch.epfl.sweng.qeeqbii.chat.UsersActivity;

/**
 * Created by adrien on 07.12.17.
 */

public class Slider {

    // mapping menu item id -> activity class name
    private static Map<Integer, Class> activityNames;

    // mapping current activity class -> slider drawer id
    private static Map<Class, Integer> drawerIds;

    // mapping current activity class -> slider drawer id
    private static ArrayList<Class> doNotReuse;

    // true if mappings are filled
    private static Boolean mIsInisizlied = false;

    // fill the mappings if it is neccessary
    private static void initialize() {
        if (!mIsInisizlied) {
            activityNames = new HashMap<>();
            drawerIds = new HashMap<>();
            doNotReuse = new ArrayList<>();

            doNotReuse.add(ProductComparisonActivity.class);

            // New slider content


            activityNames.put(R.id.nav_scan, BarcodeScannerActivity.class);
            activityNames.put(R.id.nav_shopping_list, ShoppingListActivity.class);
            activityNames.put(R.id.nav_scanning_history, SavedProductsDatesActivity.class);
            activityNames.put(R.id.nav_stats, StatisticsActivity.class);
            activityNames.put(R.id.nav_chat, MainActivityChat.class);
            activityNames.put(R.id.nav_comparison, ProductComparisonActivity.class);
            activityNames.put(R.id.nav_didyouknow, DidYouKnowActivity.class);
            activityNames.put(R.id.nav_user_profile, UsersActivity.class);
            activityNames.put(R.id.nav_other, OtherActivity.class);


            // adding slider drawer ids
            drawerIds.put(MainActivity.class, R.id.drawer_main);
            //drawerIds.put(MainActivityChat.class, None);
            drawerIds.put(GraphsActivity.class, R.id.GraphsLayout);
            drawerIds.put(CancerDataShowActivity.class, R.id.cancerDataShow);
            drawerIds.put(BarcodeScannerActivity.class, R.id.barcode_scanner);
            //drawerIds.put(ShoppingListActivity.class, None);
            drawerIds.put(CancerDataQueryActivity.class, R.id.cancer_query_design);
            drawerIds.put(RecentlyScannedProductsActivity.class, R.id.SavedProductsLayout);
            drawerIds.put(SavedProductsDatesActivity.class, R.id.SavedProductsLayout);
            //drawerIds.put(StatisticsActivity.class, None);
            drawerIds.put(ProductComparisonActivity.class, R.id.ComparisonLayout);

            mIsInisizlied = true;
        }
    }

    // called on click in slider
    public void goToActivity(MenuItem item, Context context) {

        // initialize mappings on start
        // done only once
        initialize();

        // requested menu item id
        Integer requested_id = item.getItemId();

        // checking if name is known
        if (activityNames.containsKey(requested_id)) {

            // obtaining calling activity
            Activity currentActivity = (Activity) context;

            // checking if current activity has a drawer to close it
            if (drawerIds.containsKey(currentActivity.getClass())) {
                // obtaining drawer id
                Integer drawerId = drawerIds.get(currentActivity.getClass());

                // obtaining the drawer
                DrawerLayout mDrawerLayout = (DrawerLayout) currentActivity.findViewById(drawerId);

                // closing the drawer
                if (mDrawerLayout != null) {
                    Log.d("STATE", "Closing drawer");
                    mDrawerLayout.closeDrawers();
                } else {
                    Log.d("STATE", "Drawer is null");
                }
            } else {
                Log.d("STATE", "Drawer not detected");
            }

            // obtaining activity class
            Class activity_class = activityNames.get(requested_id);

            Log.d("STATE", "Slider: going to " + activity_class);

            // launching new intent
            Intent intent = new Intent(context, activity_class);

            // if the activity exists, just bring it to front
            // unless it is in doNotReuse
            if(!doNotReuse.contains(activity_class)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }

            // start activity
            context.startActivity(intent);

        } else {
            // the pressed menu item is unknown to this class
            Log.d("STATE", "Slider: unknown menu id " + requested_id);
        }
    }
}
