package ch.epfl.sweng.qeeqbii;


/*
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.startsWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FacebookLoginTest {



    @Test
    public void FacebookLoginInTest_() {
        // Sign out if possible
        signOutIfPossible();
        //Disconnect if possible
        //SaveIfPossible();

        // Click sign in
        //onView(allOf(withId(R.id.button_facebook_login), isDisplayed())).perform(click());

    }

    private void signOutIfPossible() {
        try {
            onView(allOf(withId(R.id.button_facebook_signout), isDisplayed()))
                    .perform(click());
        } catch (NoMatchingViewException e) {
            // Ignore
        }

    }
    private void SaveIfPossible() {
        try {
            onView(allOf(withId(R.id.buttonSave), isDisplayed()))
                    .perform(click());
        } catch (NoMatchingViewException e) {
            // Ignore
        }

    }


}*/


import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.qeeqbii.chat.ChatActivity;
import ch.epfl.sweng.qeeqbii.chat.FacebookLoginActivity;
import ch.epfl.sweng.qeeqbii.chat.StartActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@RunWith(AndroidJUnit4.class)
public class FacebookLoginTest {
    @Rule
    public final IntentsTestRule<FacebookLoginActivity> mActivityRule =
            new IntentsTestRule<>(FacebookLoginActivity.class);

    @Test
    public void useAppContext() throws Exception {


        onView(withId(R.id.button_facebook_signout)).perform(click());
        FacebookLoginActivity activity = mActivityRule.getActivity();
        Thread.sleep(200);
        Espresso.closeSoftKeyboard();
        Thread.sleep(200);
        onView(withId(R.id.button_facebook_login)).perform(click());


    }
/*
    @Test
    public void informationsTest () {
        String firstname= "Nicolas";
        String lastname="Lesimple";
        String allergie = "cacahètes";
        String aliment = "fruits";

        //enter informations
        enterFirstName(firstname);
        enterLastName(lastname);
        enterAllergies(allergie);
        enterAliment(aliment);
    }

    private void enterFirstName (String firstname){
        ViewInteraction passwordField = onView(
                allOf(withId(R.id.editTextFirstName),
                        isDisplayed()));
        passwordField.perform(replaceText(firstname));
    }
    private void enterLastName (String lastname) {
        ViewInteraction passwordField = onView(
                allOf(withId(R.id.editTextLastName),
                        isDisplayed()));
        passwordField.perform(replaceText(lastname));
    }
    private void enterAllergies (String allergies) {
        ViewInteraction passwordField = onView(
                allOf(withId(R.id.editTextAllergie),
                        isDisplayed()));
        passwordField.perform(replaceText(allergies));
    }
    private void enterAliment (String aliment) {
        ViewInteraction passwordField = onView(
                allOf(withId(R.id.editTextGout),
                        isDisplayed()));
        passwordField.perform(replaceText(aliment));
    }*/
}
