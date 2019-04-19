package smartface.com.nativetest;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ApplyLayoutTest extends AppCompatActivity {

    Context baseContext = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.applylayout);

        RelativeLayout rootRelativeLayout = (RelativeLayout) findViewById(R.id.rootRelativeLayout);

        LinearLayout linearLayout = new LinearLayout(this){
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {
                super.onLayout(changed, l, t, r, b);
                System.out.println("onLayout "  );
            }

            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                System.out.println("onMeasure "  );
            }
        };

        rootRelativeLayout.addView(linearLayout);

        Button  myButton = (Button) findViewById(R.id.applyLayoutBtn);

        Toast.makeText(baseContext, "TESST", Toast.LENGTH_SHORT);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.getLayoutParams().height = 250;

                linearLayout.requestLayout();
//                System.out.println("height " + linearLayout.getMeasuredHeight() );
//                rootRelativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        System.out.println("onGlobalLayout height " + linearLayout.getMeasuredHeight() );
//
//                        rootRelativeLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this); // ToDo: look for APIlevel
//                    }
//                });

            }
        });
    }
}
