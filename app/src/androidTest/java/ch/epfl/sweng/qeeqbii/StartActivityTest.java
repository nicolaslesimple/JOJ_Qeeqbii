package ch.epfl.sweng.qeeqbii;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.chat.AllergiesActivity;
import ch.epfl.sweng.qeeqbii.chat.StartActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by nicol on 27.11.2017.
 */

public class StartActivityTest {

    @Rule
    public ActivityTestRule<StartActivity> mActivityTestRule =
            new ActivityTestRule<>(StartActivity.class);

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @Test
    public void button_reg () {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.start_reg_btn),
                        isDisplayed()));
        appCompatButton.perform(click());
    }

    @Test
    public void button_login () {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.start_login_btn),
                        isDisplayed()));
        appCompatButton.perform(click());
    }
    @Test
    public void text () {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.textView),
                        isDisplayed()));
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}

