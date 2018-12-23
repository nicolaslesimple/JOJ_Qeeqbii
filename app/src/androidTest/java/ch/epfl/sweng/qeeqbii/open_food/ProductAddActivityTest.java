package ch.epfl.sweng.qeeqbii.open_food;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import ch.epfl.sweng.qeeqbii.ActivityFinisher;
import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.activities.BarcodeScannerActivity;
import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.activities.ProductAddActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.qeeqbii.activities.BarcodeScannerActivity.EXTRA_BARCODE;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductAddActivityTest {

    private static final String mBadBarcode = "YYY12345XXX";

    @Rule
    public final IntentsTestRule<BarcodeToProductActivity> mActivityRule =
            new IntentsTestRule<BarcodeToProductActivity>(BarcodeToProductActivity.class) {
                    @Override
                    protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, BarcodeToProductActivity.class);
                    result.putExtra(EXTRA_BARCODE, mBadBarcode);
                    return result;
                };
            };

    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(true);
    }

    // see https://stackoverflow.com/questions/21045509/check-if-a-dialog-is-displayed-with-espresso
    @Test
    public void clickNoButton() {
        onView(withText(R.string.product_not_found_want_add)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).perform(click());
    }

    @Test
    public void clickYesButton() {
        onView(withText(R.string.product_not_found_want_add)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.view_add_barcode)).check(matches(withText(mBadBarcode)));
    }
    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}
