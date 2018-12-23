package ch.epfl.sweng.qeeqbii;

import android.content.Context;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;

import static ch.epfl.sweng.qeeqbii.chat.GetTimeAgo.getTimeAgo;
import static com.facebook.FacebookSdk.getApplicationContext;
import static junit.framework.Assert.assertEquals;

/**
 * Created by nicol on 26.11.2017.
 */

public class GetTimeAgoTest {
    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }

    @Test
    public void test_time() {
        long time = 100;
        Context cxt = getApplicationContext();

        String test = getTimeAgo(time, cxt);
        long now = System.currentTimeMillis();
       // assertEquals(test,"17513 days ago");
        final int DAY_MILLIS = 24 * 60 * 60 * 1000;
        assertEquals(test, ((now - time * 1000) / DAY_MILLIS) + " days ago");
    }

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }
}
