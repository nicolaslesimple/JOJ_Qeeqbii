package ch.epfl.sweng.qeeqbii.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import ch.epfl.sweng.qeeqbii.R;

import static ch.epfl.sweng.qeeqbii.activities.BarcodeScannerActivity.EXTRA_BARCODE;

public class ProductAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);

        // obtaining barcode from extras
        Intent intent = getIntent();
        String barcode = intent.getStringExtra(EXTRA_BARCODE);

        // setting it to the textview if it is not null
        if(barcode != null) {
            Log.d("STATE", "Found " + barcode + " in extras");
            final TextView barcode_field = (TextView) findViewById(R.id.view_add_barcode);
            barcode_field.setText(barcode);
        }
        else {
            Log.d("STATE", "No barcode in extras");
        }
    }
}
