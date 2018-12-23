package ch.epfl.sweng.qeeqbii;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;

import com.google.zxing.Result;

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
import ch.epfl.sweng.qeeqbii.activities.MainActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.zxing.BarcodeFormat.QR_CODE;
import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertFalse;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

// DO NOT RENAME THIS FILE
// THIS TEST MUST RUN FIRST!
// SINCE BARCODE MIGHT DISPLAY
// A POPUP REQUESTING FOR PERMISSIONS!


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class A00_BarcodeScannerActivityTest_DO_NOT_RENAME_SEE_INSIDE {
    private static final int GRANT_BUTTON_INDEX = 1;
    private static final long PERMISSIONS_DELAY = 1000;

    @Rule
    public final IntentsTestRule<BarcodeScannerActivity> mActivityRule =
            new IntentsTestRule<>(BarcodeScannerActivity.class);

    // function to reset permissions.
    // see https://stackoverflow.com/questions/33929937/android-marshmallow-test-permissions-with-espresso
//    @BeforeClass
    public static void resetPermission() {
        assertTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
        getInstrumentation().getUiAutomation().executeShellCommand("pm revoke " +
                getTargetContext().getPackageName() + " " +
                BarcodeScannerActivity.CAMERA_PERMISSION);
        Log.d("Barcode Permission", "Revoked");
    }

    // checks if the permission was granted already
    public boolean isPermissionGranted() {
        boolean res = ContextCompat.checkSelfPermission(mActivityRule.getActivity(),
                BarcodeScannerActivity.CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED;

        Log.d("Barcode Permission", "Permission is granted: " + res);
        return res;
    }

    // function adds permission to the app by clicking to the button
    // see https://stackoverflow.com/questions/33929937/android-marshmallow-test-permissions-with-espresso
    public void grantPermission() {
        assertTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);

        SystemClock.sleep(PERMISSIONS_DELAY);

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject allowPermissions = device.findObject(new UiSelector()
                .clickable(true)
                .checkable(false)
                .index(GRANT_BUTTON_INDEX));

        assertTrue(allowPermissions.exists());

        try {
            allowPermissions.click();
        } catch (UiObjectNotFoundException e) {
            Log.d("Barcode Permission", "Cannot click the button");
            e.printStackTrace();
            fail();
        }

        Log.d("Barcode Permission", "Granted");
        SystemClock.sleep(PERMISSIONS_DELAY);
    }

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @Test
    public void t00_permissions() {
        //resetPermission();
        //SystemClock.sleep(PERMISSIONS_DELAY);
        if(!isPermissionGranted())
            grantPermission();
        assertTrue(isPermissionGranted());
    }

    @Test
    public void t01_testCanObtainBarcode() throws Exception {
        // evian water, see #85

        BarcodeScannerActivity activity = mActivityRule.getActivity();
        activity.setNextActivity(BarcodeScannerActivity.NEXT_DUMMY);
        activity.processBarcode("3068320353500");
        assertTrue(activity.getLastBarcode().equals("3068320353500"));
    }

    @Test
    public void t02_testCanStartActivity() throws Exception {
        // https://www.openfood.ch/en/products/972

        BarcodeScannerActivity activity = mActivityRule.getActivity();
        activity.setNextActivity(BarcodeToProductActivity.class.getName());
        activity.processBarcode("7611654884033");
        intended(hasComponent(new ComponentName(getTargetContext(), BarcodeToProductActivity.class)));
    }

    @Test
    public void t03_testCanFinishOnInvalid() throws Exception {
        BarcodeScannerActivity activity = mActivityRule.getActivity();
        Result x = new Result("", null, null, QR_CODE);
        activity.handleResult(x);
        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}
