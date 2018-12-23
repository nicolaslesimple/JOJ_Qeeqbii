package ch.epfl.sweng.qeeqbii;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import ch.epfl.sweng.qeeqbii.activities.BarcodeScannerActivity;
import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.chat.RegisterActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by nicol on 01.12.2017.
 */

public class RegisterTest {

    @Rule
    public final ActivityTestRule<RegisterActivity> mActivityRule =
            new ActivityTestRule<>(RegisterActivity.class);

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }


    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }

    @Test
    public void login() {

        String name = "UserTest";
        String email = "user" + randomString() + "@example.com";
        String password = "password" + randomString();

        //Enter Name
        enterName(name);
        // Enter email
        enterEmail(email);
        // Enter password
        enterPassword(password);

        // Click sign in
        ViewInteraction appCompatButton = onView(withId(R.id.reg_create_btn));
        appCompatButton.perform(click());

        // Force closing the progressDialog box in order to start new activity
        mActivityRule.getActivity().dismissProgressDialog();

    }

    private void enterName(String name) {
        ViewInteraction nameField = onView(withId(R.id.register_name));
        nameField.perform(replaceText(name));
    }

    private void enterEmail(String email) {
        ViewInteraction emailField = onView(withId(R.id.register_email_text));
        emailField.perform(replaceText(email));
    }

    private void enterPassword(String password) {
        ViewInteraction passwordField = onView((withId(R.id.register_password)));
        passwordField.perform(replaceText(password));
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }
}

