package ch.epfl.sweng.qeeqbii.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ch.epfl.sweng.qeeqbii.R;


public class AllergiesActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout mAllergies;
    private Button mSavebtn;

    //Firebase
    private DatabaseReference mAllergiesDatabase;
    private FirebaseUser mCurrentUser;


    //Progress
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergies);

        //Firebase
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mAllergiesDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mToolbar = (Toolbar) findViewById(R.id.allergies_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Allergies");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String allergies_value = getIntent().getStringExtra("allergies_value");

        mAllergies = (TextInputLayout) findViewById(R.id.allergies_input);
        mSavebtn = (Button) findViewById(R.id.allergies_save_btn);

        mAllergies.getEditText().setText(allergies_value);

        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Progress
                mProgress = new ProgressDialog(AllergiesActivity.this);
                mProgress.setTitle("Saving Changes");
                mProgress.setMessage("Please wait while we save the changes");
                mProgress.show();

                String allergies = mAllergies.getEditText().getText().toString();

                mAllergiesDatabase.child("allergies").setValue(allergies).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            mProgress.dismiss();

                        } else {

                            Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                        }

                    }
                });

                Intent intent = new Intent(AllergiesActivity.this, SettingsActivity.class);
                startActivity(intent);

            }
        });



    }
}
