package ch.epfl.sweng.qeeqbii.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.zxing.Result;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;

import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.Slider;
import ch.epfl.sweng.qeeqbii.cancer.CancerDataBase;
import ch.epfl.sweng.qeeqbii.clustering.ClusterClassifier;
import ch.epfl.sweng.qeeqbii.clustering.NutrientNameConverter;
import ch.epfl.sweng.qeeqbii.custom_exceptions.BadlyFormatedFile;
import ch.epfl.sweng.qeeqbii.custom_exceptions.NotOpenFileException;

import ch.epfl.sweng.qeeqbii.chat.StartActivity;

import ch.epfl.sweng.qeeqbii.open_food.OpenFoodQuery;
import ch.epfl.sweng.qeeqbii.open_food.SavedProductsDatabase;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.content.ContentValues.TAG;

/**
 * Created by sergei on 02/11/17.
 * Activity which uses the ZXing library with the me.dm7 wrapper
 * In order to scan barcodes
 * Each scanned barcode is then sent to the BarcodeToProductActivity via an intent
 * If the barcode is invalid, activity is finished
 */

public class BarcodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    // default next activity
    public static final String DEFAULT_NEXT_ACTIVITY = BarcodeToProductActivity.class.getName();

    // dummy next activity doing nothing
    public static final String NEXT_DUMMY = "ch.epfl.sweng.qeeqbii.BarcodeScannerActivity.next.dummy";

    // Extra name for barcode
    public static final String EXTRA_BARCODE = "ch.epfl.sweng.qeeqbii.BarcodeScannerActivity.barcode";

    // Extra name for next activity
    public static final String EXTRA_NEXT = "ch.epfl.sweng.qeeqbii.BarcodeScannerActivity.next";

    // name of the permission
    public static final String CAMERA_PERMISSION = android.Manifest.permission.CAMERA;

    // code for permission call
    private static final int ZXING_CAMERA_PERMISSION = 1;

    // next activity
    public String mNextActivity = null;

    private static boolean active = false;

    // zxing library object
    private ZXingScannerView mScannerView;

    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;

    // last received barcode
    private String mLastBarcode = null;

    public static boolean isRunning() {
        return active;
    }

    // returns last scanned barcode
    public String getLastBarcode() {
        return mLastBarcode;
    }

    public void setNextActivity(String nextActivity) {
        mNextActivity = nextActivity;
    }

    // on activity creation
    // ask permissions and launch barcode reader
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Make the user stayed login between using
        mAuth = FirebaseAuth.getInstance();
       // if (mAuth.getCurrentUser() != null) {
       //     mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
       // }
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            Intent startIntent = new Intent(BarcodeScannerActivity.this, StartActivity.class);
            startActivity(startIntent);
            finish();
        }

        // disable autorotate if it was enabled
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        setContentView(R.layout.activity_barcode_scanner);

        try {
            SavedProductsDatabase.load(getApplicationContext());
            SavedProductsDatabase.getDates();
            if (ShoppingListActivity.getClusterProductList() == null)
                ShoppingListActivity.load(getApplicationContext());
        } catch(IOException|JSONException|ParseException e){
            System.err.println(e.getMessage());
        }

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.barcode_scanner);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // request for camera permission if it is not present
        checkCameraPermissionAndRequest();

        // initialize ZXing scanner
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

        // obtain action if present
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(EXTRA_NEXT)) {
            mNextActivity = (String) extras.getSerializable(EXTRA_NEXT);
        } else {
            mNextActivity = DEFAULT_NEXT_ACTIVITY;
        }


        // Reading files that are needed in the rest of the app
        this.readCSVFiles();


        Log.d("STATE", "NEXT activity is " + mNextActivity);
    }

    // check if the camera permission is given
    // request one if it was not
    private void checkCameraPermissionAndRequest() {
        if (ContextCompat.checkSelfPermission(this, CAMERA_PERMISSION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{CAMERA_PERMISSION}, ZXING_CAMERA_PERMISSION);
        }
    }

    // this method is called when system gives (or not gives) the permission to use camera
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, getString(R.string.camera_perm_ok));
                } else {
                    Toast.makeText(this, R.string.please_allow_camera, Toast.LENGTH_SHORT).show();
                }
        }
    }

    // when the activity is started again (ex. when application is switched into)
    // start the camera again
    @Override
    public void onResume() {
        super.onResume();

        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);

        // Start camera on resume
        mScannerView.startCamera();
    }

    // when the app is in the background, switch the camera off
    @Override
    public void onPause() {
        super.onPause();

        // Stop camera on pause
        mScannerView.stopCamera();
    }

    // process the scanned barcode
    @Override
    public void handleResult(Result rawResult) {
        // Prints scan results
        Log.v(TAG, rawResult.getText());

        // Prints the scan format (qr code, pdf417 etc.)
        Log.v(TAG, rawResult.getBarcodeFormat().toString());

        // storing the barcode
        String barcode = rawResult.getText();

        // do processing
        processBarcode(barcode);
    }

    // go back
    public void goBack() {
        finish();
    }

    // process the barcode given as a string
    public void processBarcode(String barcode) {
        mLastBarcode = barcode;
        // go back if the barcode was invalid
        // or the scan was interrupted
        if (barcode == null || barcode.equals("")) {
            Log.d("STATE", "Barcode is invalid, going back");
            goBack();
        } else if (mNextActivity.equals(NEXT_DUMMY)) {
            Log.d("STATE", "Doing nothing");
        } else {
            Log.d("STATE", "Barcode " + barcode + " found, going to OpenFood");
            Intent intent = null;
            try {
                intent = new Intent(this, Class.forName(mNextActivity));
                if (Class.forName(mNextActivity) == ShoppingListActivity.class)
                {
                    ShoppingListActivity.addProduct(OpenFoodQuery.GetOrCreateProduct(mLastBarcode,
                            getApplicationContext()), getApplicationContext());
                    this.finish();
                    return;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new Error("Barcode activity got invalid next activity: " + mNextActivity);
            } catch (Exception e)
            {
                e.printStackTrace();
                System.err.println(e.getMessage());
            }

            intent.putExtra(EXTRA_BARCODE, barcode);
            startActivity(intent);
        }
    }

    // slider actions below
    public void readCSVFiles() {
        if (!NutrientNameConverter.isRead()) {
            NutrientNameConverter.readCSVFile(getApplicationContext());
        }


        if (!ClusterClassifier.isRead()) {
            try {
                ClusterClassifier.readClusterNutrientCentersFile(getApplicationContext());
            } catch (NotOpenFileException | BadlyFormatedFile e) {
                System.err.println(e.getMessage());
            }
        }

        if (!CancerDataBase.isRead()) {
            CancerDataBase.readCSVFile(getApplicationContext());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void sliderGoToActivity(MenuItem item) {
        Slider slider = new Slider();
        slider.goToActivity(item, this);
    }

}
