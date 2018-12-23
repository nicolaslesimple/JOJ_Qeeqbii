package ch.epfl.sweng.qeeqbii;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.chat.LoginActivity;
import ch.epfl.sweng.qeeqbii.chat.RequestsFragment;

/**
 * Created by nicol on 27.11.2017.
 */

public class RequestFragmentTest {

    @Test
    public void test() {
        RequestsFragment frag = new RequestsFragment();


    }

    @Test
    public void viewholder() {
       // View mview ;
        //RequestsFragment.RequestsViewHolder tmp = new RequestsFragment.RequestsViewHolder(mview);

    }

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}
