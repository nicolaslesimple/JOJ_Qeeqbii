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

public class AgeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout mAge;
    private Button mSavebtn;

    //Firebase
    private DatabaseReference mAgeDatabase;
    private FirebaseUser mCurrentUser;


    //Progress
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age);

        //Firebase
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mAgeDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mToolbar = (Toolbar) findViewById(R.id.age_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Age");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String age_value = getIntent().getStringExtra("age_value");

        mAge = (TextInputLayout) findViewById(R.id.age_input);
        mSavebtn = (Button) findViewById(R.id.age_save_btn);

        mAge.getEditText().setText(age_value);

        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Progress
                mProgress = new ProgressDialog(AgeActivity.this);
                mProgress.setTitle("Saving Changes");
                mProgress.setMessage("Please wait while we save the changes");
                mProgress.show();

                String age = mAge.getEditText().getText().toString();

                mAgeDatabase.child("age").setValue(age).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            mProgress.dismiss();

                        } else {

                            Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                        }

                    }
                });

                Intent intent = new Intent(AgeActivity.this, SettingsActivity.class);
                startActivity(intent);

            }
        });



    }
}
