package ch.epfl.sweng.qeeqbii.open_food;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.List;

import ch.epfl.sweng.qeeqbii.ActivityFinisher;
import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.activities.SavedProductsDatesActivity;
import ch.epfl.sweng.qeeqbii.clustering.NutrientNameConverter;
import ch.epfl.sweng.qeeqbii.clustering.ClusterTypeSecondLevel;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by guillaume on 14/11/17.
 * Test for all the SavedProducts pipeline.
 */

@RunWith(AndroidJUnit4.class)
public class SavedProductsDatabaseTest {

    @Rule
    public final ActivityTestRule<SavedProductsDatesActivity> mActivityRule =
            new ActivityTestRule<>(SavedProductsDatesActivity.class);

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @Before
    public void loadTestDatabase() {
        Intent intent = new Intent(mActivityRule.getActivity(), SavedProductsDatesActivity.class);
        intent.putExtra("test", R.raw.saved_products_database_test);
        mActivityRule.launchActivity(intent);
        NutrientNameConverter.readCSVFile(mActivityRule.getActivity().getApplicationContext());
    }

    @Test
    public void loadDatabaseTest()
    {
        try
        {
            Date[] dates = SavedProductsDatabase.getDates();
            assertEquals((new Date("13/11/2017")),(dates[0]));
            assertEquals(1,dates.length);

        } catch( Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void getProductTest()
    {
        try
        {
            Product[] products = SavedProductsDatabase.getProductsFromDate(new Date("13/11/2017"));

            assertEquals(2,products.length);
            assertEquals("Tartiflette", products[0].getName());
            assertEquals("Rebloch, Rebloch, Rebloch et Reblochh", products[1].getIngredients());
            assertEquals(1,SavedProductsDatabase.getDates().length);
            assertEquals(ClusterTypeSecondLevel.FROMAGES, products[0].getCluster());

        } catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void addProductTest()
    {
        System.out.println("NutrientNameConverter read: " + NutrientNameConverter.isRead());
        Product product = new Product("Raclette", "1000g", "Cheese," +
                " Cheese ,Cheese", "Sel: 0.200g", "00055232323", ClusterTypeSecondLevel.FROMAGES);

        Product product2 = new Product("Fondue", "1500g", "Cheese," +
                " Cheese ,Cheese", "Sel: 0.300g", "00055237323", ClusterTypeSecondLevel.FROMAGES);
        try
        {
            assertEquals(1,SavedProductsDatabase.getDates().length);
            System.out.println(product.toString());
            System.out.println(product2.toString());
            SavedProductsDatabase.addProduct(product);
            SavedProductsDatabase.addProduct(product2);
            Date[] dates = SavedProductsDatabase.getDates();
            Date today_date = new Date();
            assertEquals(today_date, dates[dates.length-1]);

            Product entered_product = SavedProductsDatabase.getProductsFromDate(today_date)[0];
/*
            assertEquals(product.getName(),entered_product.getName());
            assertEquals(product.getIngredients(),entered_product.getIngredients());
            assertEquals(product.getNutrients(),entered_product.getNutrients());
            assertEquals(product.getQuantity(),entered_product.getQuantity());
            assertEquals(product.getBarcode(),entered_product.getBarcode());
            assertEquals(product.getCluster(),entered_product.getCluster());

            Product entered_product2 = SavedProductsDatabase.getProductsFromDate(today_date)[1];

            assertEquals(product2.getName(),entered_product2.getName());

            Intent intent = new Intent(mActivityRule.getActivity(), SavedProductsDatesActivity.class);
            mActivityRule.launchActivity(intent);

            onView(withText(today_date.toString())).perform(click());
            //TimeUnit.SECONDS.sleep(5);
            onView(withText("Raclette")).perform(click());
            Product product3 = SavedProductsDatabase.getProductsFromDate(today_date)[0];
            onView(withText(product3.toString())).check(matches(isDisplayed()));

            SavedProductsDatabase.save(mActivityRule.getActivity().getApplicationContext(),"json_save_test.json");*/


        } catch (Exception e)
        {
            fail(e.getMessage());
        }


    }

    @Test
    public void showProduct()
    {
        try
        {
            onView(withText("13/11/2017")).perform(click());
            onView(withText("Tartiflette")).perform(click());
            Product product = SavedProductsDatabase.getProductsFromDate(new Date("13/11/2017"))[0];
            onView(withText(product.toString())).check(matches(isDisplayed()));

        } catch (Exception e)
        {
            fail(e.getMessage());
        }

    }

    /*
    @Test
    public void getProductsBetweenTwoDates()
    {
        assertEquals(true, !new Date().isBefore(new Date("11/10/2017")));
        Product added_product = new Product("Carbonara", "1kg", "lardons, fromage oignons",
        "Sel: 250g", "032485623", ClusterTypeSecondLevel.FROMAGES);
        try{
            SavedProductsDatabase.addProduct(added_product);
            List<Product> products = SavedProductsDatabase.getProductsBetweenTodayAndDate(new Date("13/11/2017"));
            for( Product elem : products)
            {
                System.out.println(elem.toString());
            }
            assertEquals(added_product.getName(),products.get(products.size()-1).getName());
            assertEquals("Tartiflette", products.get(0).getName());
        } catch (JSONException|ParseException e)
        {
            fail(e.getMessage());
        }
    }*/

   // @AfterClass
   // public static void finish_all_activities() {
    //    ActivityFinisher.finishOpenActivities();
    //}
}
