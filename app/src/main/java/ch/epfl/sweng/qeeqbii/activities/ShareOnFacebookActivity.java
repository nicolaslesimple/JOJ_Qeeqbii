package ch.epfl.sweng.qeeqbii.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import ch.epfl.sweng.qeeqbii.R;

/**
 * Created by guillaume on 02/11/17.
 *
 */

public class ShareOnFacebookActivity extends AppCompatActivity {
    ImageView bmImage;
    static View view;
    Bitmap image_to_share;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_on_facebook);
        bmImage = (ImageView) findViewById(R.id.share_facebook_image);

        view.setDrawingCacheEnabled(true);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        view.layout(0, 0, width, height);

        view.buildDrawingCache(true);
        image_to_share = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false); // clear drawing cache

        bmImage.setImageBitmap(image_to_share);
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogue();
            }
        });
    }

    public void dialogue () {
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image_to_share)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
           shareDialog.show(content);
    }
}
