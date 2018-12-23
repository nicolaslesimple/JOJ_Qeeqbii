package ch.epfl.sweng.qeeqbii.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.Slider;
import ch.epfl.sweng.qeeqbii.cancer.query.RatcliffQueryCancerDB;
import ch.epfl.sweng.qeeqbii.custom_exceptions.ProductException;
import ch.epfl.sweng.qeeqbii.open_food.OpenFoodQuery;
import ch.epfl.sweng.qeeqbii.open_food.RecentlyScannedProducts;

import static ch.epfl.sweng.qeeqbii.activities.BarcodeScannerActivity.EXTRA_BARCODE;

/**
 * Created by guillaume on 10/10/17.
 * Activity that prints product details relative to the barcode entered
 * in the barcode text field in the MainActivity.
 */

public class BarcodeToProductActivity extends AppCompatActivity {

    // if true, on an unknown barcode a yes/no popup for adding
    // the product will be displayed
    private static Boolean mAllowAddProducts = Boolean.TRUE;
    // storage for activity context
    // https://stackoverflow.com/questions/20044163/starting-an-android-activity-from-a-static-method
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private String barcode;
    private ActionBarDrawerToggle mToggle;

    // if true, enables product addition via a popup
    public static void setProductAddingAllowed(Boolean value) {
        BarcodeToProductActivity.mAllowAddProducts = value;
    }

    // call to create request to add product
    // see https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
    public static void requestAddProduct(final String barcode) {
        // doing nothing if addition is not allowed
        if (!mAllowAddProducts) {
            Log.d("STATE", "Barcode " + barcode + " not found in OpenFood, adding disabled");
            return;
        }

        // creating dialog object
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        // creating dialog onClick object
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        Log.d("STATE", "Barcode " + barcode + " not found in OpenFood, user clicked No");

                        // going to ProductAddActivity
                        Intent intent = new Intent(mContext, ProductAddActivity.class);
                        intent.putExtra(EXTRA_BARCODE, barcode);
                        mContext.startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // doing nothing
                        Log.d("STATE", "Barcode " + barcode + " not found in OpenFood, user clicked No");
                        break;
                }
            }
        };

        // displaying the dialog with onClick set
        builder.setMessage(R.string.product_not_found_want_add)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_details);

        Intent intent = getIntent();
        barcode = intent.getStringExtra(EXTRA_BARCODE);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.barcode_to_product);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Capture the layout's; TextView and set the string as its text
        final TextView txt = (TextView) findViewById(R.id.product_details);
        txt.setTextSize(20);
        txt.setTextColor(Color.rgb(0, 0, 0));
        //OpenFoodQuery.ShowProduct(barcode, txt, getApplicationContext());

        OpenFoodQuery.ShowProduct(barcode, txt, getApplicationContext());

        /*
        try {
            Product product = OpenFoodQuery.GetOrCreateProduct(barcode, null);
            txt.setText(product.toString());
        }
        catch (Exception e) {
            System.err.print(e.getMessage());
            txt.setText(e.getMessage());
        }
        */

        mContext = this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // go back to main activity after BACK button was pressed
            //Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);
            finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    // Search for harmful ingredients contained in the product, making a query to the Cancer database.
    public void searchHarmfulIngredients(View view) {

        if (!(RecentlyScannedProducts.contains(barcode))) {
            return;
        }

        TextView txt_harmfull_ing = (TextView) findViewById(R.id.harmful_ingredients);

        String parsed_ingredients[];

        try {
            parsed_ingredients = RecentlyScannedProducts.getProduct(barcode).getParsedIngredients();
        } catch (ProductException e) {
            txt_harmfull_ing.setText(e.getMessage());
            return;
        }


        String str = "";
        RatcliffQueryCancerDB ratcliffQuery = new RatcliffQueryCancerDB();
        try {
            for (String ingredient : parsed_ingredients) {
                str += ratcliffQuery.query(ingredient).toString() + "\n";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        txt_harmfull_ing.setText(str);

    }

    // Call the Graphs activity to make generate plots from the nutrients of the product.
    public void showNutrientsGraphs(View view) {
        Intent intent = new Intent(this, GraphsActivity.class);
        intent.putExtra("barcode", barcode);
        startActivity(intent);
    }

    public void buyProduct(View view) {
        ShoppingListActivity.addProduct(RecentlyScannedProducts.getProduct(barcode), getApplicationContext());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void sliderGoToActivity(MenuItem item) {
        Slider slider = new Slider();
        slider.goToActivity(item, this);
    }
}
