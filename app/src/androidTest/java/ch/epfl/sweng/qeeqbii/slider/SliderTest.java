package ch.epfl.sweng.qeeqbii.slider;

import android.app.Instrumentation;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.view.Gravity;

import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.activities.BarcodeScannerActivity;
import ch.epfl.sweng.qeeqbii.activities.CancerDataQueryActivity;
import ch.epfl.sweng.qeeqbii.activities.CancerDataShowActivity;
import ch.epfl.sweng.qeeqbii.activities.DidYouKnowActivity;
import ch.epfl.sweng.qeeqbii.activities.GraphsActivity;
import ch.epfl.sweng.qeeqbii.activities.MainActivity;
import ch.epfl.sweng.qeeqbii.activities.OtherActivity;
import ch.epfl.sweng.qeeqbii.activities.ProductComparisonActivity;
import ch.epfl.sweng.qeeqbii.activities.SavedProductsDatesActivity;
import ch.epfl.sweng.qeeqbii.activities.ShoppingListActivity;
import ch.epfl.sweng.qeeqbii.activities.StatisticsActivity;
import ch.epfl.sweng.qeeqbii.chat.MainActivityChat;
import ch.epfl.sweng.qeeqbii.chat.UsersActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by adrien on 07.12.17.
 */

class SliderTest {





    void canGoToBarcodeScanner(int layout_id, int nav_view_id) {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(BarcodeScannerActivity.class.getName(), null, false);

        onView(withId(layout_id))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
        // Start the screen of your activity.
        onView(withId(nav_view_id)).perform(NavigationViewActions.navigateTo(R.id.nav_scan));

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        BarcodeScannerActivity nextActivity = (BarcodeScannerActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity.finish();
    }


    void canGoToShoppingList(int layout_id, int nav_view_id) {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ShoppingListActivity.class.getName(), null, false);

        onView(withId(layout_id))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
        // Start the screen of your activity.
        onView(withId(nav_view_id)).perform(NavigationViewActions.navigateTo(R.id.nav_shopping_list));

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        ShoppingListActivity nextActivity5 = (ShoppingListActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity5);
        nextActivity5.finish();
    }



    void canGoToSavedProductsDate(int layout_id, int nav_view_id) {

        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(SavedProductsDatesActivity.class.getName(), null, false);

        onView(withId(layout_id))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
        // Start the screen of your activity.
        onView(withId(nav_view_id)).perform(NavigationViewActions.navigateTo(R.id.nav_scanning_history));

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        SavedProductsDatesActivity nextActivity7 = (SavedProductsDatesActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity7);
        nextActivity7.finish();
    }


    void canGoToStatistics(int layout_id, int nav_view_id) {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(StatisticsActivity.class.getName(), null, false);

        onView(withId(layout_id))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
        // Start the screen of your activity.
        onView(withId(nav_view_id)).perform(NavigationViewActions.navigateTo(R.id.nav_stats));

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        StatisticsActivity nextActivity8 = (StatisticsActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity8);
        nextActivity8.finish();

    }



    void canGoToCancerMainActivityChat(int layout_id, int nav_view_id) {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MainActivityChat.class.getName(), null, false);

        onView(withId(layout_id))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
        // Start the screen of your activity.
        onView(withId(nav_view_id)).perform(NavigationViewActions.navigateTo(R.id.nav_chat));

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        MainActivityChat nextActivity9 = (MainActivityChat) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity9);
        nextActivity9.finish();
    }

    void canGoToProductsComparisonActivity(int layout_id, int nav_view_id) {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ProductComparisonActivity.class.getName(), null, false);

        onView(withId(layout_id))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
        // Start the screen of your activity.
        onView(withId(nav_view_id)).perform(NavigationViewActions.navigateTo(R.id.nav_comparison));

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        ProductComparisonActivity nextActivity10 = (ProductComparisonActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity10);
        nextActivity10.finish();
    }





    void canGoToDidYouKnowActivity(int layout_id, int nav_view_id) {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(DidYouKnowActivity.class.getName(), null, false);

        onView(withId(layout_id))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
        // Start the screen of your activity.
        onView(withId(nav_view_id)).perform(NavigationViewActions.navigateTo(R.id.nav_didyouknow));

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        DidYouKnowActivity nextActivity10 = (DidYouKnowActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity10);
        nextActivity10.finish();
    }




    void canGoToUsersActivityActivity(int layout_id, int nav_view_id) {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(UsersActivity.class.getName(), null, false);

        onView(withId(layout_id))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
        // Start the screen of your activity.
        onView(withId(nav_view_id)).perform(NavigationViewActions.navigateTo(R.id.nav_user_profile));

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        UsersActivity nextActivity = (UsersActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity.finish();
    }




    void canGoToOtherActivity(int layout_id, int nav_view_id) {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(OtherActivity.class.getName(), null, false);

        onView(withId(layout_id))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
        // Start the screen of your activity.
        onView(withId(nav_view_id)).perform(NavigationViewActions.navigateTo(R.id.nav_other));

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        OtherActivity nextActivity = (OtherActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity.finish();
    }
}
