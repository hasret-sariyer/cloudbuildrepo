package smartface.com.nativetest;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by smartface on 15.08.2018.
 */

public class FragmentOnDestroyTest extends AppCompatActivity  {
    static Context parentContext;
    static MutableContextWrappers mutableContextWrappers;

    public static ViewGroup rootViewFragments = null;
    public static Parcelable restoreInstance;

    static AppCompatActivity activity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.textviewsize_animate);

        Button btn  = (Button) findViewById(R.id.btnAnimate);
        TextView  tv = (TextView) findViewById(R.id.textV);

        activity = this;

        parentContext = this;

        CoordinatorLayout myCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.page_CoordinatorLayout);

        myCoordinatorLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("MyonKey", " myCoordinatorLayout  setOnKeyListener");
                return true;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager  fg = getSupportFragmentManager();
                FragmentTransaction ft = fg.beginTransaction();

                FirstFragemnet firstFragemnet1 = new FirstFragemnet();

                ft.setCustomAnimations(R.anim.onshow_animation,R.anim.ondismiss_animation);

                ft.replace(R.id.page_layout , firstFragemnet1);
                ft.addToBackStack("test1234");
                // Complete the changes added above
                ft.commitAllowingStateLoss();

                fg.executePendingTransactions();
            }
        });

    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Log.d("MyonKey", "onBackPressed ");
    }



}
