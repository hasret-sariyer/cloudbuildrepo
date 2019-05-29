package smartface.com.nativetest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionSet;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class AutoTransitionTest extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.autotransitiontest);


        FrameLayout frameLayout =  findViewById(R.id.myFrameLayout);

        Button btn = findViewById(R.id.startBtn);


//        AutoTransition autoTransition = new AutoTransition();
//        autoTransition.setOrdering(TransitionSet.ORDERING_TOGETHER);
//        autoTransition.setDuration(1500);
//        autoTransition.setInterpolator(new FastOutSlowInInterpolator());

        TransitionSet set = new TransitionSet();
        set.setDuration(1500);
        set.addTransition(new ScalaTransition());

        TransitionManager.beginDelayedTransition(frameLayout,set);
        btn.setOnClickListener((click) ->{
            TransitionManager.beginDelayedTransition(frameLayout,set);
//            frameLayout.findViewById(R.id.myImageView).setVisibility(View.VISIBLE);
            frameLayout.findViewById(R.id.myImageView).setScaleX(0.1f);
            frameLayout.findViewById(R.id.myImageView).setScaleY(0.1f);

            frameLayout.requestLayout();
//            frameLayout.invalidate();
//            frameLayout.refreshDrawableState();

//            frameLayout.findViewById(R.id.myImageView).setY(250);

        });
    }
}
