package ch.epfl.sweng.qeeqbii.clustering;

import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.qeeqbii.ActivityFinisher;
import ch.epfl.sweng.qeeqbii.activities.BarcodeScannerActivity;
import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.activities.MainActivity;
import ch.epfl.sweng.qeeqbii.custom_exceptions.IllegalNutrientKeyException;
import ch.epfl.sweng.qeeqbii.custom_exceptions.NotOpenFileException;

import static junit.framework.Assert.assertEquals;

/**
 * Created by adrien on 05.12.17.
 */

public class NutrientNameConverterTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @Test
    public void nutrientNameConversionThrowsExceptions() {
        boolean wentInCatch1 = false;
        try {
            if (NutrientNameConverter.isRead()) {
                NutrientNameConverter.clear();
            }
            NutrientNameConverter.convertToStandardName("Acides gras saturées (g)");
        } catch (NotOpenFileException | IllegalNutrientKeyException e) {
            wentInCatch1 = true;
        }
        assertEquals(wentInCatch1, true);
    }





    @Test
    public void nutrientNameConversion() {



        NutrientNameConverter.readCSVFile(mActivityRule.getActivity().getApplicationContext());

        String converted = "";
        String stayTheSame = "";
        try {
            converted = NutrientNameConverter.convertToStandardName("Acides gras saturées (g)");
            stayTheSame = NutrientNameConverter.convertToStandardName("Énergie (kJ)");
        }
        catch (NotOpenFileException|IllegalNutrientKeyException e) {
            System.err.println(e.getMessage());
        }

        assertEquals(converted, "Acides gras saturés (g)");
        assertEquals(stayTheSame, "Énergie (kJ)");

        boolean wentInCatch2 = false;
        try {
            NutrientNameConverter.convertToStandardName("caca");
        }
        catch (NotOpenFileException|IllegalNutrientKeyException e) {
            wentInCatch2 = true;
        }
        assertEquals(wentInCatch2, true);

    }


    @Test
    public void standardNutrientNamesThrowsException() {
        boolean wentInCatch = false;
        try {
            if (NutrientNameConverter.isRead()) {
                NutrientNameConverter.clear();
            }
            NutrientNameConverter.getStandardNutrientNames();
        }
        catch (NotOpenFileException e) {
            wentInCatch = true;
        }
        assertEquals(wentInCatch, true);
    }




    @Test
    public void standardNutrientNames() {

        NutrientNameConverter.readCSVFile(mActivityRule.getActivity().getApplicationContext());
        String standardName = "Vitamine B5 (acide pantothénique) (mg)";

        Set<String> setStandardNames = new HashSet<>();
        try {
            setStandardNames = NutrientNameConverter.getStandardNutrientNames();
        }
        catch (NotOpenFileException e) {
            System.err.println(e.getMessage());
        }

        assertEquals(setStandardNames.contains(standardName), true);
        assertEquals(setStandardNames.size(), 20);
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}
