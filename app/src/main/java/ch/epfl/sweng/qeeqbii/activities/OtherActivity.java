package ch.epfl.sweng.qeeqbii.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ch.epfl.sweng.qeeqbii.R;

public class OtherActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
    }



    public void openCancerDataBase(View view) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, CancerDataShowActivity.class);
        context.startActivity(intent);

    }
}
