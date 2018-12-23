package ch.epfl.sweng.qeeqbii;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.activities.MainActivity;

/**
 * Created by davidcleres on 12.10.17.
 */
@RunWith(AndroidJUnit4.class)
public class GraphActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public GraphActivityTest() {
        super(MainActivity.class);
    }

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // Injecting the Instrumentation instance is required
        // for your test to run with AndroidJUnitRunner.
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void recognizesWhenCalled() throws InterruptedException {

        MainActivity activity = mActivityRule.getActivity();
        //Instrumentation.ActivityMonitor am = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);

        // Click the menu option
        getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        //getInstrumentation().invokeMenuActionSync(activity, R.id.nav_graphs, 0);

        //Activity a = getInstrumentation().waitForMonitorWithTimeout(am, 1000);
        //assertEquals(true, getInstrumentation().checkMonitorHit(am, 1));

        // Check type of returned Activity:
        //assertNotNull(a);
        //assertTrue(a instanceof GraphsActivity);
        //a.finish();

        //onView(withId(R.id.textView2)).perform(click());
        //onView(withId(R.id.nav_graphs)).perform(click());
        //openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        //onView(withId(R.id.nav_graphs)).perform(click());
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}