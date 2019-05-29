package smartface.com.nativetest;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
//import android.support.v4.view.ViewCompat;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Set;


public class NativeTest extends AppCompatActivity {

    Context cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_test);

        cnt=this;
        impContentResolver();


        int horizontal = convertDpToPixel(12,this);
        int vertical = convertDpToPixel(10,this);

        RelativeLayout rlt = (RelativeLayout) findViewById(R.id.relativeL);

        RelativeLayout imgBtnRlt = new RelativeLayout(this);
        imgBtnRlt.setId(View.generateViewId());


        ImageButton imgBtn = new ImageButton(this);
        imgBtn.setBackground(null);
        imgBtn.setPadding(vertical,horizontal,vertical,horizontal);
        imgBtn.setImageResource(R.drawable.ic_launcher_background);

        RelativeLayout.LayoutParams imgBtnlayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        imgBtnlayoutParams.setMargins(0,0,convertDpToPixel(50,this),0);



        imgBtnRlt.addView(imgBtn); //Relative layout
        rlt.addView(imgBtnRlt,imgBtnlayoutParams);


        TextView tv = new TextView(this);
        tv.setText("2");
        tv.setGravity(17);
        setBackGroundColor(tv);
        tv.setTextColor(Color.BLACK);

        tv.setPadding(convertDpToPixel(5,this), 0, convertDpToPixel(5,this), 0);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(350, 350);
        tv.setId(View.generateViewId());
        tv.setX(-50f);
        tv.setY(150f);
        layoutParams.addRule(RelativeLayout.ALIGN_END,imgBtnRlt.getId());
//        layoutParams.setMargins(0, convertDpToPixel( 0,this), 0, 0);
//        layoutParams.setMarginStart(0);
//        layoutParams.setMarginEnd(convertDpToPixel(35,this));

        tv.setGravity(Gravity.START);

        tv.setLayoutParams(layoutParams);

//        tv.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Toast.makeText(cnt, " TextView touch " , Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        rlt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(cnt, " Relative Layout touch " , Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        rlt.addView(tv);

        rlt.setBackgroundColor(Color.WHITE);

    }

    void impContentResolver() {
        ContentResolver cntResolver = getContentResolver();
        Uri setting  =  Settings.System.getUriFor(Settings.System.FONT_SCALE);

        ContentObserver observer = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                System.out.println("in onchage");

            }
        };
        cntResolver.registerContentObserver(setting, false, observer);
    }

    void setBackGroundColor (TextView tv){
        int borderRadius = convertDpToPixel(20,this);
        int borderWidth = convertDpToPixel(2,this);

//        int borderRadius2 = convertDpToPixel(10,this);
//
        float[] radii = new float[]{borderRadius,
                borderRadius,borderRadius,
                borderRadius,0,0,
                0,0};
//
//        float[] radii2 = new float[]{borderRadius2,
//                borderRadius2,borderRadius2,
//                borderRadius2,borderRadius2,borderRadius2,
//                borderRadius2,borderRadius2};
//
//        RectF rc = new RectF(0,0,0,0);
//        RoundRectShape rrs = new RoundRectShape(radii,rc,radii);
//        ShapeDrawable sd = new ShapeDrawable(rrs);
//        sd.getPaint().setColor(Color.RED);
//
//        RectF rc2 = new RectF(5,5,5,5);
//        RoundRectShape rrs2 = new RoundRectShape(radii2,rc2,radii2);
//        ShapeDrawable sd2 = new ShapeDrawable(rrs2);
//        sd2.setPadding(15,15,15,15);
//        //sd2.setPadding(10,10,10,10);
//        sd2.getPaint().setColor(Color.WHITE);


//        Drawable[] dr = {sd,sd2};
//
//        LayerDrawable layerList = new LayerDrawable(dr);
////        layerList.setDrawableByLayerId(1,sd);
//        layerList.setDrawableByLayerId(2,sd2);
//        layerList.invalidateDrawable(sd);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.GREEN); // Changes this drawbale to use a single color instead of a gradient
        gd.setCornerRadius(borderRadius);
        gd.setCornerRadii(radii);
        gd.invalidateSelf();
        gd.setStroke(borderWidth, Color.BLACK);
        tv.setBackground(gd);
    }


    public static int convertDpToPixel(int dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = Math.round(dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
