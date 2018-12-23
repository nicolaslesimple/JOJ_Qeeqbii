package ch.epfl.sweng.qeeqbii.open_food;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.qeeqbii.ActivityFinisher;
import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.activities.MainActivity;
import ch.epfl.sweng.qeeqbii.custom_exceptions.ProductException;
import ch.epfl.sweng.qeeqbii.clustering.ClusterTypeSecondLevel;

import static junit.framework.Assert.assertEquals;

/**
 * Created by davidcleres on 12.10.17.
 */
@RunWith(AndroidJUnit4.class)
public class ProductTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @Test
    public void defaultConstructorTest() {
        Product prod = new Product();
        assertEquals(prod.getName(), "");
    }

    @Test
    public void getNameIdTest() {
        Product item = new Product("cheese", "500 mg", "Stuff", "Empty nutrients", "001", ClusterTypeSecondLevel.FROMAGES);
        Assert.assertEquals(item.getImageId(), R.drawable.cheese);
    }

    @Test
    public void getPaserIngredientTest() throws ProductException {
        Product item = new Product("cheese", "500 mg", "Stuff", "Empty nutrients", "001", ClusterTypeSecondLevel.FROMAGES);
        String[] ingredients = new String[]{"beans", "tomato"};
        item.setParsedIngredients(ingredients);
        assertEquals(ingredients, item.getParsedIngredients());
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}

