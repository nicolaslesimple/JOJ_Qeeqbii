package ch.epfl.sweng.qeeqbii;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.Button;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.qeeqbii.chat.TwitterLoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;


@RunWith(AndroidJUnit4.class)
public class TwitterTest {
    private IdlingResource mActivityResource;

    @Rule
    public ActivityTestRule<TwitterLoginActivity> mActivityTestRule =
            new ActivityTestRule<>(TwitterLoginActivity.class);


    @Test
    public void useAppContext() throws Exception {

        onView(withId(R.id.button_twitter_signout)).perform(click());
        Activity current_activity = mActivityTestRule.getActivity();
        Thread.sleep(200);
        Espresso.closeSoftKeyboard();
        //Button buttonTwitterLogin = (Button) current_activity.findViewById(R.id.button_twitter_login);
        Thread.sleep(200);
        onView(withId(R.id.button_twitter_login)).perform(click());
    }
    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
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
/*
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
@LargeTest
@RunWith(AndroidJUnit4.class)
public class TwitterTest {
    private IdlingResource mActivityResource;
    @Rule
    public ActivityTestRule<AnonymousAuthActivity> mActivityTestRule =
            new ActivityTestRule<>(AnonymousAuthActivity.class);
    @Test
    public void TwitterSignInTest_() {
        // Sign out if possible
        signOutIfPossible();
        //Continue activity
        SaveIfPossible();
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
    /*private void signInIfPossible() {
        try {
            onView(allOf(withId(R.id.button_twitter_login), isDisplayed()))
                    .perform(click());
        } catch (NoMatchingViewException e) {
            // Ignore
        }
    }*/

   /* private void SaveIfPossible() {
        try {
            onView(allOf(withId(R.id.buttonSave), isDisplayed()))
                    .perform(click());
        } catch (NoMatchingViewException e) {
            // Ignore
        }
    }
}*/