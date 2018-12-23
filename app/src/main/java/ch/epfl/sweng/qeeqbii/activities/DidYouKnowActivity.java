package ch.epfl.sweng.qeeqbii.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.Slider;

public class DidYouKnowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_did_you_know);
    }

    public void sliderGoToActivity(MenuItem item) {
        Slider slider = new Slider();
        slider.goToActivity(item, this);
    }
}
