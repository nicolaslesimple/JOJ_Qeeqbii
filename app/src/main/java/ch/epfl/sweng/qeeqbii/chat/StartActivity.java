package ch.epfl.sweng.qeeqbii.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import ch.epfl.sweng.qeeqbii.R;

public class StartActivity extends AppCompatActivity {

    private ImageButton mGoogleBtn;
    private Button mRegBtn;
    private Button mLoginBtn;
    private Button mFacebookBtn;
    private ImageButton mTwitterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mGoogleBtn= (ImageButton)  findViewById(R.id.google);
        mRegBtn = (Button) findViewById(R.id.start_reg_btn);
        mLoginBtn = (Button) findViewById(R.id.start_login_btn);
        mFacebookBtn = (Button) findViewById(R.id.facebook);
        mTwitterBtn = (ImageButton) findViewById(R.id.twitter);


        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent reg_intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(reg_intent);

            }
        });



        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent login_intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(login_intent);

            }
        });
        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent login_intent_google = new Intent(StartActivity.this, GoogleSignInActivity.class);
                startActivity(login_intent_google);

            }
        });

        mFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent reg_intent = new Intent(StartActivity.this, FacebookLoginActivity.class);
                startActivity(reg_intent);

            }
        });

        mTwitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent reg_intent = new Intent(StartActivity.this, TwitterLoginActivity.class);
                startActivity(reg_intent);

            }
        });

    }
}
