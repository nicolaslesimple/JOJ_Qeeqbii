package ch.epfl.sweng.qeeqbii.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareDialog;

import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.Slider;
import ch.epfl.sweng.qeeqbii.open_food.SavedProductsDatabase;

import static ch.epfl.sweng.qeeqbii.activities.BarcodeScannerActivity.EXTRA_BARCODE;

public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            SavedProductsDatabase.load(getApplicationContext());
            SavedProductsDatabase.getDates();
        }
        catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_main);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void searchProductFromBarcode(View view) {
        Intent intent = new Intent(this, BarcodeToProductActivity.class);
        EditText editText = (EditText) findViewById(R.id.Barcode);
        String barcode = editText.getText().toString();
        intent.putExtra(EXTRA_BARCODE, barcode);
        startActivity(intent);
    }

    public void productComparison(View view) {
        Intent intent = new Intent(this, ProductComparisonActivity.class);
        startActivity(intent);
    }

    // launch ProductAdd activity with the barcode from EditText
    public void productAdd(View view) {
        Intent intent = new Intent(this, ProductAddActivity.class);
        EditText editText = (EditText) findViewById(R.id.Barcode);
        String barcode = editText.getText().toString();
        intent.putExtra(EXTRA_BARCODE, barcode);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void sliderGoToActivity(MenuItem item) {
        Slider slider = new Slider();
        slider.goToActivity(item, this);
    }
}