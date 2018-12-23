package ch.epfl.sweng.qeeqbii;

/**
 * Created by sergei on 11/30/17.
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.widget.ListView;

import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import ch.epfl.sweng.qeeqbii.activities.BarcodeScannerActivity;
import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.activities.ProductComparisonActivity;
import ch.epfl.sweng.qeeqbii.clustering.NutrientNameConverter;
import ch.epfl.sweng.qeeqbii.open_food.Product;
import ch.epfl.sweng.qeeqbii.open_food.RecentlyScannedProducts;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.VerificationModes.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.qeeqbii.clustering.ClusterTypeFirstLevel.CHOCOLAT;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductComparisonActivityTest {

    @Rule
    public final IntentsTestRule<ProductComparisonActivity> mActivityRule =
            new IntentsTestRule<>(ProductComparisonActivity.class);

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
        RecentlyScannedProducts.clear();
    }

    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }

    @Test
    public void t01_testCanShowInsufficientProducts() {
        onView(withId(R.id.product_name_1)).check(matches(withText(R.string.name_1)));
        onView(withId(R.id.product_name_2)).check(matches(withText(R.string.name_2)));
        ListView ls = (ListView) mActivityRule.getActivity().findViewById(R.id.graphs);
        assertTrue(ls.getCount() == 0);
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }


    public Activity getActivityInstance(final String className) {
        final Activity[] currentActivity = new Activity[1];

        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                ActivityLifecycleMonitor activityLifecycleMonitor = ActivityLifecycleMonitorRegistry.getInstance();

                for (final Stage stage : EnumSet.of(Stage.CREATED, Stage.RESUMED, Stage.STARTED)) {
                    final Collection<Activity> activities_in_stage = activityLifecycleMonitor.getActivitiesInStage(stage);
                    for (final Activity activity : activities_in_stage) {
                        if (Objects.equals(activity.getClass().getName(), className)) {
                            currentActivity[0] = activity;
                        }
                    }
                }
            }
        });

        return currentActivity[0];
    }

    public BarcodeScannerActivity getCurrentScanner() {
        return ((BarcodeScannerActivity) getActivityInstance(BarcodeScannerActivity.class.getName()));
    }

    @Test
    public void t02_testCanPressScan() {
        onView(withId(R.id.scan_button)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), BarcodeScannerActivity.class)), times(1));
        getCurrentScanner().finish();
    }

    @Test
    public void t03_testCanCompareProducts() throws Throwable {
        // reading csv file if it was not read before
        NutrientNameConverter.readCSVFile(getTargetContext());

        /*
        Random nutrients generator:
        import random

        def get_random_int():
            return int(random.random() * 1000)

        all_nutrients = <content from nutrient_name_converter.csv>

        lines = all_nutrients.split('\n')
        lines = [x.split(',')[0].split(' ') for x in lines]
        lines = [' '.join(x[:-1]) + ": " + str(get_random_int()) + x[-1][1:-1] for x in lines]
        print('\n'.join(lines))
         */

        RecentlyScannedProducts.add("12346", new Product("Cba", "2g", "", "Énergie: 995kJ\nSel: 985g\nProtéines: 136g\nSucres: 411g\nGlucides: 160g\nMatières grasses: 153g\nAcides gras saturés: 350g\nFibres alimentaires: 197g\nBiotine: 814µg\nVitamine B1 (Thiamine): 862mg\nVitamine E (Tocopherol): 12mg\nAcide folique: 806µg\nVitamine A: 661µg\nVitamine D (Cholacalciferol): 891µg\nVitamine C (Acide ascorbique): 654mg\nVitamine B6 (Pyridoxine): 3mg\nVitamine B5 (acide pantothénique): 406mg\nVitamine B3 (Niacine): 536mg\nVitamine B2 (Riboflavine): 103mg\nVitamine B12 (Cobalamine): 331µg\n", "12346", CHOCOLAT));
        RecentlyScannedProducts.add("12345", new Product("Aba", "3g", "", "Énergie: 108kJ\nSel: 923g\nProtéines: 752g\nSucres: 553g\nGlucides: 453g\nMatières grasses: 471g\nAcides gras saturés: 505g\nFibres alimentaires: 641g\nBiotine: 495µg\nVitamine B1 (Thiamine): 959mg\nVitamine E (Tocopherol): 518mg\nAcide folique: 179µg\nVitamine A: 208µg\nVitamine D (Cholacalciferol): 334µg\nVitamine C (Acide ascorbique): 455mg\nVitamine B6 (Pyridoxine): 771mg\nVitamine B5 (acide pantothénique): 591mg\nVitamine B3 (Niacine): 852mg\nVitamine B2 (Riboflavine): 285mg\nVitamine B12 (Cobalamine): 171µg\n", "12345", CHOCOLAT));

        final CountDownLatch latch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivityRule.getActivity().updateData();
                latch.countDown();
            }
        });
        latch.await();

        // checking if the view displays names and charts
        onView(withId(R.id.product_name_1)).check(matches(withText(startsWith("Aba"))));
        onView(withId(R.id.product_name_2)).check(matches(withText(startsWith("Cba"))));
        ListView ls = (ListView) mActivityRule.getActivity().findViewById(R.id.graphs);

        // check if button is visible
        onView(withId(R.id.scan_button)).check(matches(isDisplayed()));

        // checking if the listview adapter has at least one element
        assertTrue(ls.getAdapter().getCount() > 0);
    }
}
