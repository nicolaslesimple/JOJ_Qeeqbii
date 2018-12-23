package ch.epfl.sweng.qeeqbii;
/*

import android.support.test.espresso.NoMatchingViewException;
import android.test.suitebuilder.annotation.LargeTest;

import android.support.test.runner.AndroidJUnit4;


import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GoogleSignInTest {

        //@Rule
        //public ActivityTestRule<GoogleSignInActivity> mActivityTestRule =
         //       new ActivityTestRule<>(GoogleSignInActivity.class);

        @Test
        public void GoogleSignInTest_() {
            // Sign out if possible
        //    signOutIfPossible();
            //Disconnect if possible
          //  DisconnectIfPossible();
            //Disconnect if possible
            //SaveIfPossible();

            // Click sign in
            //onView(allOf(withId(R.id.sign_in_button), isDisplayed()))
              //      .perform(click());


        }

        private void signOutIfPossible() {
            try {
                onView(allOf(withId(R.id.sign_out_button), isDisplayed()))
                        .perform(click());
            } catch (NoMatchingViewException e) {
                // Ignore
            }

        }
        private void DisconnectIfPossible() {
            try {
                onView(allOf(withId(R.id.disconnect_button), isDisplayed()))
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


}
*/
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.qeeqbii.chat.FacebookLoginActivity;
import ch.epfl.sweng.qeeqbii.chat.GoogleSignInActivity;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;


@RunWith(AndroidJUnit4.class)
public class GoogleSignInTest {
    @Rule
    public final IntentsTestRule<GoogleSignInActivity> mActivityRule =
            new IntentsTestRule<>(GoogleSignInActivity.class);

    @Test
    public void useAppContext() throws Exception {

        if((R.id.sign_out_button==1)){onView(withId(R.id.sign_out_button)).perform(click());}
        GoogleSignInActivity activity = mActivityRule.getActivity();
        Thread.sleep(200);
        sleep(100);
        Espresso.closeSoftKeyboard();
        sleep(100);
        if((R.id.sign_out_button==1)){onView(withId(R.id.sign_in_button)).perform(click());};
        //Thread.sleep(200);
        //onView(withId(R.id.sign_out_button)).perform(click());
    }

   /* @Test
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

