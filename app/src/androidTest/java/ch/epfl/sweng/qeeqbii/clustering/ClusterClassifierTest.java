package ch.epfl.sweng.qeeqbii.clustering;

import android.support.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Set;

import ch.epfl.sweng.qeeqbii.ActivityFinisher;
import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.activities.MainActivity;
import ch.epfl.sweng.qeeqbii.custom_exceptions.BadlyFormatedFile;
import ch.epfl.sweng.qeeqbii.custom_exceptions.NotOpenFileException;

import static junit.framework.Assert.assertEquals;





public class ClusterClassifierTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @Test
    public void canThrowExceptionReadingClusterNutrientCenters() {
        // Check that an exception is thrown if nutrient_name_converter.csv is not read before
        boolean wentInCatch1 = false;
        try {
            if (NutrientNameConverter.isRead()) {
                NutrientNameConverter.clear();
            }
            ClusterClassifier.readClusterNutrientCentersFile(mActivityRule.getActivity().getApplicationContext());
        }
        catch (NotOpenFileException|BadlyFormatedFile e) {
            wentInCatch1 = true;
        }
        assertEquals(wentInCatch1, true);


    }


    @Test
    public void canThrowExceptionGettingClusterType() {
        // First creating a new zero NutrientVector to carry out classification
        NutrientNameConverter.readCSVFile(mActivityRule.getActivity().getApplicationContext());
        NutrientVector zeroVector = null;
        try {
            zeroVector = new NutrientVector();
        }
        catch (NotOpenFileException e) {
            System.err.println(e.getMessage());
        }

        System.out.println("Nutrient cluster centers is open: " + ClusterClassifier.isRead());

        boolean wentInCatch = false;
        try {
            if (ClusterClassifier.isRead()) {
                ClusterClassifier.clear();
            }
            ClusterClassifier.getClusterTypeFromNutrients(zeroVector);
        }
        catch (NotOpenFileException e) {
            wentInCatch = true;
        }
        assertEquals(wentInCatch, true);
    }


    @Test
    public void canClassify() {

        NutrientNameConverter.readCSVFile(mActivityRule.getActivity().getApplicationContext());
        try {
            ClusterClassifier.readClusterNutrientCentersFile(mActivityRule.getActivity().getApplicationContext());
        }
        catch (NotOpenFileException|BadlyFormatedFile e) {
            System.err.println(e.getMessage());
        }


        NutrientVector zeroVector = null;
        try {
            zeroVector = new NutrientVector();
        }
        catch (NotOpenFileException e) {
            System.err.println(e.getMessage());
        }

        ArrayList<ComparableCluster> topClusters = null;
        try {
            topClusters = ClusterClassifier.getClusterTypeFromNutrients(zeroVector);
        }
        catch(NotOpenFileException e) {
            System.err.println(e.getMessage());
        }

        ComparableCluster first = topClusters.get(0);
        ComparableCluster second = topClusters.get(1);

        Set<String> setStandardNames = null;
        try {
            setStandardNames = NutrientNameConverter.getStandardNutrientNames();
        }
        catch (NotOpenFileException e) {
            System.err.println(e.getMessage());
        }

        double firstDistance = 0.0;
        double secondDistance = 0.0;
        for (int i = 0; i < setStandardNames.size(); i++) {
            firstDistance += 1.0;
            secondDistance += Math.pow(2.0, 2);
        }
        assertEquals(first.getCluster(), ClusterTypeSecondLevel.FROMAGES);
        assertEquals(first.getDistance(), Math.sqrt(firstDistance));
        assertEquals(second.getCluster(), ClusterTypeSecondLevel.OEUFS);
        assertEquals(second.getDistance(), Math.sqrt(secondDistance));
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}
