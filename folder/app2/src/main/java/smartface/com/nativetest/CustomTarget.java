package smartface.com.nativetest;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class CustomTarget implements Target {
    ImageView mImageView;
    Context parentContext;

    CustomTarget(Context parentContext, View myImageView){
        mImageView =(ImageView) myImageView;
        this.parentContext = parentContext;
    }
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(parentContext.getResources(),bitmap);
//        bitmapDrawable.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        mImageView.setImageDrawable(bitmapDrawable);
    }


    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//        Drawable myDrawable = parentContext.getResources().getDrawable(R.drawable.cast_ic_mini_controller_play);

//        ViewCompat.setLayoutDirection(mImageView,ViewCompat.LAYOUT_DIRECTION_RTL);
//        myDrawable.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        myDrawable.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        mImageView.setImageDrawable(myDrawable);
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        mImageView.setImageDrawable(placeHolderDrawable);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
