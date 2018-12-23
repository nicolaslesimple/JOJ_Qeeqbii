package ch.epfl.sweng.qeeqbii;

import android.app.ProgressDialog;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.qeeqbii.activities.BarcodeScannerActivity;
import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.activities.SavedProductsDatesActivity;
import ch.epfl.sweng.qeeqbii.chat.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.EnumSet.allOf;

/**
 * Created by nicol on 01.12.2017.
 */

public class LoginTest {
    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @Test
    public void login(){
        String email = "nicolaslesimple@noos.fr";
        String password = "123456";

        // Enter email
        enterEmail(email);

        // Enter password
        enterPassword(password);

        // Click sign in
        ViewInteraction appCompatButton =onView(withId(R.id.login_btn));
        appCompatButton.perform(click());


        // Force closing the progressDialog box in order to start new activity
        mActivityRule.getActivity().dismissProgressDialog();

        // Wait that BarcodeScanner activity starts (it takes a little bit of time since all the CSV files are read
        // when this activity is launched for the first time
        while (!BarcodeScannerActivity.isRunning()) {
            try {
                Thread.sleep(100);
            }
            catch(InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }



    private void enterEmail(String email) {
        ViewInteraction emailField = onView(withId(R.id.email_login_chat));
        emailField.perform(replaceText(email));
    }

    private void enterPassword(String password) {
        ViewInteraction passwordField = onView((withId(R.id.password_login_chat)));
        passwordField.perform(replaceText(password));
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}
