package ch.epfl.sweng.qeeqbii.clustering;

import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

import ch.epfl.sweng.qeeqbii.ActivityFinisher;
import ch.epfl.sweng.qeeqbii.activities.BarcodeScannerActivity;
import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.clustering.ClusterClassifier;
import ch.epfl.sweng.qeeqbii.clustering.NutrientNameConverter;
import ch.epfl.sweng.qeeqbii.clustering.NutrientVector;
import ch.epfl.sweng.qeeqbii.custom_exceptions.BadlyFormatedFile;
import ch.epfl.sweng.qeeqbii.custom_exceptions.IllegalNutrientKeyException;
import ch.epfl.sweng.qeeqbii.custom_exceptions.NotOpenFileException;

import static junit.framework.Assert.assertEquals;


public class NutrientVectorTest {

    @Rule
    public final ActivityTestRule<BarcodeScannerActivity> mActivityRule =
            new ActivityTestRule<>(BarcodeScannerActivity.class);


    private NutrientVector nutrientVectorTest1;
    private NutrientVector nutrientVectorTest2;
    private NutrientVector nutrientVectorTest3;

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @Before
    public void initialize() {
        NutrientNameConverter.readCSVFile(mActivityRule.getActivity().getApplicationContext());

        try {
            ClusterClassifier.readClusterNutrientCentersFile(mActivityRule.getActivity().getApplicationContext());

            HashMap<String, Double> nutrientMap1 = new HashMap<>();
            nutrientMap1.put("Sel (g)", 3.0);
            nutrientMap1.put("Acides gras saturés (g)", 4.0);
            nutrientMap1.put("lourd", 7.0);

            HashMap<String, Double> nutrientMap2 = new HashMap<>();
            nutrientMap2.put("Sel (g)", 2.0);
            nutrientMap2.put("Glucides (g)", 1.0);
            nutrientMap2.put("caca", 5.0);

            HashMap<String, Double> nutrientMap3 = new HashMap<>();
            nutrientMap3.put("Sel (g)", 0.0);



            nutrientVectorTest1 = new NutrientVector(nutrientMap1);
            nutrientVectorTest2 = new NutrientVector(nutrientMap2);
            nutrientVectorTest3 = new NutrientVector(nutrientMap3);
        }
        catch (NotOpenFileException|BadlyFormatedFile e) {
            System.err.println(e.getMessage());
            nutrientVectorTest1 = null;
            nutrientVectorTest2 = null;
        }


    }


    @Test
    public void canConvertNutrientName() {

        String toConvert = "Acides gras saturées (g)";
        String converted = "";
        try {
            converted = NutrientNameConverter.convertToStandardName(toConvert);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }

        assertEquals(converted, "Acides gras saturés (g)");
    }


    @Test
    public void canCreateNutrientVectorFromNutrientMap() {

        int numberOfStandardKeys = 0;
        try {
            numberOfStandardKeys = NutrientNameConverter.getStandardNutrientNames().size();
        }
        catch (NotOpenFileException e) {
            System.err.println(e.getMessage());
        }

        try {
            assertEquals(nutrientVectorTest1.getDim(), numberOfStandardKeys);
            assertEquals(nutrientVectorTest1.getComponent("Sel (g)"), 3.0);
            assertEquals(nutrientVectorTest1.getComponent("Glucides (g)"), 0.0);
        }
        catch (IllegalNutrientKeyException e) {
            System.err.println(e.getMessage());
        }

        // Test if an exception is thrown when asking for an invalid key
        boolean wentInException = false;
        try {
            nutrientVectorTest1.getComponent("lourd");
        }
        catch (IllegalNutrientKeyException e) {
            wentInException = true;
        }
        assertEquals(wentInException, true);
    }

    @Test
    public void canComputeDiff() {
        try {
            NutrientVector diffTest = nutrientVectorTest1.diff(nutrientVectorTest2);

            assertEquals(diffTest.getComponent("Sel (g)"), 1.0);
            assertEquals(diffTest.getComponent("Glucides (g)"), -1.0);
            assertEquals(diffTest.getComponent("Acides gras saturés (g)"), 4.0);
            assertEquals(diffTest.getDim(), NutrientNameConverter.getStandardNutrientNames().size());
        }
        catch (NotOpenFileException|IllegalNutrientKeyException e) {
            System.err.println(e.getMessage());
            assertEquals(1.0, 2.0);
        }
    }




    @Test
    public void canComputeDistance() {
        try {
            NutrientVector zeroVector = new NutrientVector(new HashMap<String, Double>());
            double normZero = nutrientVectorTest1.computeDistance(zeroVector);
            double normSecond = nutrientVectorTest1.computeDistance(nutrientVectorTest2);
            assertEquals(normZero, 5.0);
            assertEquals(normSecond, Math.sqrt(18.0));
        }
        catch (NotOpenFileException e) {
            e.getMessage();
            assertEquals(1.0, 2.0);
        }
    }



    @Test
    public void canDivideByZero() {
        NutrientVector divided = nutrientVectorTest1.componentWiseDivision(nutrientVectorTest3);
        double selResult = 0.0;
        try {
            selResult = divided.getComponent("Sel (g)");
        }
        catch (IllegalNutrientKeyException e) {
            System.err.println(e.getMessage());
        }
        assertEquals(selResult, Double.POSITIVE_INFINITY);
    }


    @Test
    public void readNutrientNamesBeforeOperations() {
        if (NutrientNameConverter.isRead()) {
            NutrientNameConverter.clear();
        }
        NutrientVector testDiff = nutrientVectorTest1.diff(nutrientVectorTest2);
        NutrientVector testDivision = nutrientVectorTest1.componentWiseDivision(nutrientVectorTest2);

        // Just for code coverage
        try {
            nutrientVectorTest3.setComponent("Sel (g)", 0.0);
        }
        catch (IllegalNutrientKeyException e) {
            System.err.println(e.getMessage());
        }


        assertEquals(testDiff, null);
        assertEquals(testDivision, null);
    }




    @Test
    public void getComponentCanThrowExceptions() {


        boolean wentInCatch1 = false;
        try {
            nutrientVectorTest1.getComponent("caca");
        }
        catch (IllegalNutrientKeyException e) {
            wentInCatch1 = true;
        }
        assertEquals(wentInCatch1, true);


        // Just visit a catch block inside getComponent method for code coverage
        if (NutrientNameConverter.isRead()) {
            NutrientNameConverter.clear();
        }
        try {
            nutrientVectorTest1.getComponent("Glucides (g)");
        }
        catch (IllegalNutrientKeyException e) {
            System.err.println(e.getMessage());
        }
    }


    @Test
    public void toStringWorks() {
        String outputString = nutrientVectorTest1.toString();
        assertEquals(outputString.startsWith("Dimension"), true);
    }

    @Test
    public void copyConstructorWorks() {
        NutrientVector copy = null;
        double selValue = 1.0;
        double copySelValue = 2.0;
        double glucidesValue = 1.0;
        double copyGlucidesValue = 2.0;
        try {
            copy = new NutrientVector(nutrientVectorTest1);
            copySelValue = copy.getComponent("Sel (g)");
            selValue = nutrientVectorTest1.getComponent("Sel (g)");
            copyGlucidesValue = copy.getComponent("Glucides (g)");
            glucidesValue = nutrientVectorTest1.getComponent("Glucides (g)");

        }
        catch (NotOpenFileException|IllegalNutrientKeyException e) {
            System.err.println(e.getMessage());
        }

        assertEquals(copySelValue, selValue);
        assertEquals(copyGlucidesValue, glucidesValue);
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}
