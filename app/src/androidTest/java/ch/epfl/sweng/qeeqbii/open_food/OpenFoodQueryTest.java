package ch.epfl.sweng.qeeqbii.open_food;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import ch.epfl.sweng.qeeqbii.ActivityFinisher;
import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.clustering.NutrientNameConverter;
import ch.epfl.sweng.qeeqbii.open_food.OpenFoodQuery;
import ch.epfl.sweng.qeeqbii.open_food.Product;
import ch.epfl.sweng.qeeqbii.open_food.RecentlyScannedProducts;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 * Created by guillaume on 16.10.17.
 * Tests for open food queries.
 */

@RunWith(AndroidJUnit4.class)
public class OpenFoodQueryTest {



    private String string_nutrients = "Sel: 0.0g\nProtéines: 0.5g\nFibres alimentaires: 1.5g\nSucres: 15.0g\n" +
            "Glucides: 15.0g\nAcides gras saturées: 0.0g\nMatières grasses: 0.0g\nÉnergie (kCal): 67.0kCal\nÉnergie: 280.0kJ\n";

    private String string_ingredients = "mangue (Thaïlande), eau, sucre, acidifiant (E330)";

    private String string_name = "Mangue : en tranches";

    private String string_quantity = "245.0g";

    @Rule
    public final ActivityTestRule<BarcodeToProductActivity> mActivityRule =
            new ActivityTestRule<>(BarcodeToProductActivity.class);

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @Before
    public void readCSV() {
        // reading csv file if it was not read before
        NutrientNameConverter.readCSVFile(getTargetContext());
    }

    @Test
    public void QueryOfExistingProduct() {
        final CountDownLatch signal = new CountDownLatch(1);
        String[] barcode = new String[1];
        barcode[0] = "7610848337010";

        new OpenFoodQuery() {
            public void onPostExecute(Product product) {

                try {
                    assertEquals(product.getQuantity(), string_quantity);
                    assertEquals(product.getName(), string_name);
                    assertEquals(product.getIngredients(), string_ingredients);
                    assertEquals(product.getNutrients(), string_nutrients);

                    Map<String, Double> parsed_nutrients = product.getParsedNutrients();

                    //Set<Map.Entry<String,Double>> set = parsed_nutrients.entrySet();
                    //Iterator<Map.Entry<String,Double>> it = set.iterator();
                    //Map.Entry<String,Double> e = it.next();
                    //assertEquals(e.getValue(),new Double(0.0));

                    assertEquals(parsed_nutrients.get("Sel (g)"), Double.valueOf(0.0));
                    assertEquals(parsed_nutrients.get("Énergie (kCal)"), Double.valueOf(67.0));
                    assertEquals(parsed_nutrients.get("Énergie (kJ)"), Double.valueOf(280.0));

                } catch (Exception e) {

                    fail(e.getMessage());
                } finally {
                    signal.countDown();
                }


            }
        }.execute(barcode);

        try {
            signal.await(); //wait for callback
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void QueryOfNonExistingProduct() {
        {
            final CountDownLatch signal = new CountDownLatch(1);
            String[] barcode = new String[1];
            barcode[0] = "";

            final String bc = barcode[0];

            new OpenFoodQuery() {
                public void onPostExecute(Product product) {
                    assertEquals(null,product);

                    assertEquals(OpenFoodQuery.getErrorCache().get(bc), "ERROR: (openfood) Barcode not found in the database.");

                    signal.countDown();

                }
            }.execute(barcode);

            try {
                signal.await(); //wait for callback
            } catch (Exception e) {
                fail(e.getMessage());
            }
        }


    }

    @Test
    public void CacheQueryTest()
    {
        String barcode = "7610848337010";
        RecentlyScannedProducts.clear();
        try {
            Product product = OpenFoodQuery.GetOrCreateProduct(barcode, null);
            assertEquals(product.getQuantity(), string_quantity);
            assertEquals(product.getName(), string_name);

        } catch (Exception e) {
            fail(e.getMessage());
        }

        if (OpenFoodQuery.isCached(barcode))
        {
            try {
                Product product = OpenFoodQuery.get(barcode);
                assertEquals(product.getIngredients(), string_ingredients);
                assertEquals(product.getNutrients(), string_nutrients);

            } catch (Exception e) {
                fail(e.getMessage());
            }
        } else {
            fail("barcode should be cached");
        }
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}
