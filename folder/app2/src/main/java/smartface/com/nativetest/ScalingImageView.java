package smartface.com.nativetest;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class ScalingImageView extends AppCompatActivity {

    ScaleGestureDetector scaleLister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scalingimageview);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_top);

        setSupportActionBar(myToolbar);

        PhotoView photoView = findViewById(R.id.scalingImageView);

        Drawable arrowDrawable = getResources().getDrawable(R.drawable.com_mixpanel_android_arrowleft);
        arrowDrawable.setAutoMirrored(true);


        photoView.setImageDrawable(arrowDrawable);
        photoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        photoView.setImageDrawable(photoView.getDrawable());

//        photoView.setScale();
//        photoView.setScale();

//        photoView.setMinimumScale(0.1f);

//        photoView.setImageDrawable();

//        photoView.setScaleType();

//        photoView.setImageURI();

//        photoView.setZoomable();

//        photoView.isZoomEnabled()

//        photoView.setRotationTo();
//        photoView.setRotationBy();
//        photoView.getScaleType()

//        photoView.getScaleType();

//        photoView.setImageResource();
//        photoView.setImageDrawable();
//        photoView.setOnClickListener();
//        photoView.setScale();


//        photoView.setMinimumScale(0.1f);
////        photoView.setMaximumScale();
////        photoView.setZoomTransitionDuration();
//        photoView.getMaximumScale();

//        photoView.setMediumScale();


        ImageView imageView  = findViewById(R.id.imageView);

        Drawable arrowDrawable1 = getResources().getDrawable(R.drawable.com_mixpanel_android_arrowleft);
        arrowDrawable1.setAutoMirrored(true);

        imageView.setImageDrawable(arrowDrawable1);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);


//        Picasso.get().load("http://images2.fanpop.com/image/photos/12200000/Kitten-Wallpaper-1024x768-cats-12251034-1024-768.jpg")
//                .into(photoView);

        /*
        * Medium zoom must be bigger than minimum zoom and smaller than max zoom
        */
//        photoView.setMinimumScale(0.5f);
//        photoView.setMediumScale(1.4f);
//        photoView.setMaximumScale(1.5f);

//        photoView.setZoomable(false);
    }

}
