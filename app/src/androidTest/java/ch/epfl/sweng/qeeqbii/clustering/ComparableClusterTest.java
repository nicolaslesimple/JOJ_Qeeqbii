package ch.epfl.sweng.qeeqbii.clustering;

import android.support.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.qeeqbii.ActivityFinisher;
import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.activities.MainActivity;

import static junit.framework.Assert.assertEquals;

/**
 * Created by adrien on 05.12.17.
 */

public class ComparableClusterTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @Test
    public void comparisonWorks() {
        ComparableCluster cluster1 = new ComparableCluster(ClusterTypeSecondLevel.OEUFS, 1.0);
        ComparableCluster cluster2 = new ComparableCluster(ClusterTypeSecondLevel.FROMAGES, 2.0);
        ComparableCluster cluster3 = new ComparableCluster(ClusterTypeSecondLevel.ACCOMPAGNEMENTS, 1.0);
        ComparableCluster comparator = new ComparableCluster();

        assertEquals(comparator.compare(cluster1, cluster2) < 0, true);
        assertEquals(comparator.compare(cluster2, cluster1) > 0, true);
        assertEquals(comparator.compare(cluster1, cluster3), 0);
        assertEquals(cluster1.getCluster(), ClusterTypeSecondLevel.OEUFS);
        assertEquals(cluster1.getDistance(), 1.0);
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }


}
