package ch.epfl.sweng.qeeqbii.open_food;


import android.content.ComponentName;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ArrayAdapter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.qeeqbii.ActivityFinisher;
import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.activities.ProductComparisonActivity;
import ch.epfl.sweng.qeeqbii.activities.RecentlyScannedProductsActivity;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.VerificationModes.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class RecentlyScannedProductsActivityTest {

    @Rule
    public final IntentsTestRule<RecentlyScannedProductsActivity> mActivityRule =
            new IntentsTestRule<>(RecentlyScannedProductsActivity.class);

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @Test
    public void canUseDeleteButton() {
        ArrayAdapter adapter = mActivityRule.getActivity().getmAdapter();

        onView(withId(R.id.delete_recently_scanned_product_button)).perform(click());

        assertEquals(adapter.getCount(), 0);
    }


    @Test
    public void testCanGoFromRecentlyScanned() {
        // press the comparison button
        onView(withId(R.id.productComparisonButtonOnRecentlyScanned)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), ProductComparisonActivity.class)), times(1));
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}
