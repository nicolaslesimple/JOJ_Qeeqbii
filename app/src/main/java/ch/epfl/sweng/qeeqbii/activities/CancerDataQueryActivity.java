package ch.epfl.sweng.qeeqbii.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ch.epfl.sweng.qeeqbii.Slider;
import ch.epfl.sweng.qeeqbii.cancer.CancerDataBase;
import ch.epfl.sweng.qeeqbii.cancer.CancerSubstance;
import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.cancer.query.LevenshteinQueryCancerDB;
import ch.epfl.sweng.qeeqbii.cancer.query.RatcliffQueryCancerDB;

public class CancerDataQueryActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancer_data_query);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.cancer_query_design);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void processPerfectMatchQueryResult(View view) {

        // Getting the text entered by the user in the text area provided for the query
        EditText query_field = (EditText) findViewById(R.id.cancerDataQueryTextField);
        String string_queried_substance = query_field.getText().toString();

        // Carrying out the query
        CancerSubstance queried_substance = new CancerSubstance();
        try {
            queried_substance = CancerDataBase.getSubstanceByName(string_queried_substance);
        }
        catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }

        // Print answer in TextView
        String query_answer = queried_substance.toString();
        TextView answer_field = (TextView) findViewById(R.id.queryCancerDataAnswerArea);
        answer_field.setText(query_answer);
    }

    public void processLevenshteinQueryResult(View view) {

        // Getting the text entered by the user in the text area provided for the query
        EditText query_field = (EditText) findViewById(R.id.cancerDataQueryTextField);
        String string_queried_substance = query_field.getText().toString();

        // Carrying out the query
        CancerSubstance queried_substance = new CancerSubstance();
        LevenshteinQueryCancerDB levQuery = new LevenshteinQueryCancerDB();
        try {
            queried_substance = levQuery.query(string_queried_substance);
        }
        catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }

        // Print answer in TextView
        String query_answer = queried_substance.toString();
        TextView answer_field = (TextView) findViewById(R.id.queryCancerDataAnswerArea);
        answer_field.setText(query_answer);
    }

    public void processRatcliffQueryResult(View view) {
        // Getting the text entered by the user in the text area provided for the query
        EditText query_field = (EditText) findViewById(R.id.cancerDataQueryTextField);
        String string_queried_substance = query_field.getText().toString();

        EditText threshold = (EditText) findViewById(R.id.ratcliff_threshold_text);
        double threshold_value = Double.parseDouble(threshold.getText().toString());
        // Carrying out the query
        CancerSubstance queried_substance = new CancerSubstance();
        RatcliffQueryCancerDB ratcliffQuery = new RatcliffQueryCancerDB(threshold_value);
        try {
            queried_substance = ratcliffQuery.query(string_queried_substance);
        }
        catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }

        // Print answer in TextView
        String query_answer = queried_substance.toString();
        TextView answer_field = (TextView) findViewById(R.id.queryCancerDataAnswerArea);
        answer_field.setText(query_answer);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void sliderGoToActivity(MenuItem item) {
        Slider slider = new Slider();
        slider.goToActivity(item, this);
    }
}
