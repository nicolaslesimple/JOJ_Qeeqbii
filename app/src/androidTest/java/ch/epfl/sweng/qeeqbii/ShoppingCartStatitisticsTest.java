package ch.epfl.sweng.qeeqbii;

/**
 * Created by davidcleres on 13.11.17.
 */

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.activities.StatisticsActivity;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ShoppingCartStatitisticsTest {

    @Rule
    public final ActivityTestRule<StatisticsActivity> mActivityRule =
            new ActivityTestRule<>(StatisticsActivity.class);


    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    /*@Test
    public void testCanOpenMonthButton() throws InterruptedException {
        onView(withId(R.id.showMonthGraph)).perform(click());
    }*/

    @Test
    public void CountTest() throws InterruptedException {
        FragmentManager fm = mActivityRule.getActivity().getSupportFragmentManager();
        StatisticsActivity.SectionsPagerAdapter section;
        section = new StatisticsActivity.SectionsPagerAdapter(fm);
        assertEquals(section.getCount(), 3);
    }

    @Test
    public void getPageTitleTest() throws InterruptedException {
        FragmentManager fm = mActivityRule.getActivity().getSupportFragmentManager();
        StatisticsActivity.SectionsPagerAdapter section;
        section = new StatisticsActivity.SectionsPagerAdapter(fm);
        assertNotNull(section.getPageTitle(2));
    }

    @Test
    public void getPageTitleNullTest() throws InterruptedException {
        FragmentManager fm = mActivityRule.getActivity().getSupportFragmentManager();
        StatisticsActivity.SectionsPagerAdapter section;
        section = new StatisticsActivity.SectionsPagerAdapter(fm);
        assertNull(section.getPageTitle(4));
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}