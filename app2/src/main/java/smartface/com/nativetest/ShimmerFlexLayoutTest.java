package smartface.com.nativetest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

/*
        ColorHighlightBuilder
        -- setHighlightColor +
        -- setDuration
        -- setBaseColor      +
        -- setBaseAlpha
        -- setHighlightAlpha
        -- setIntensity
        -- setRepeatCount
        -- setRepeatDelay
        -- setRepeatMode
        -- setDirection This property must be used if given shimmer is {UI.ShimmerFlexLayout.Android.Shimmer.ColorHighlight}
        -- setTilt
        isShimmerStarted
*/

public class ShimmerFlexLayoutTest extends AppCompatActivity {

    Context parentContex;
    Shimmer shimmerColorHighLight;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.picassotest);

        parentContex = this;

        ShimmerFrameLayout myShimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.myShimmerLayout);

         shimmerColorHighLight = new Shimmer.AlphaHighlightBuilder()
                .setAutoStart(false)
                .build();

        myShimmerFrameLayout.setShimmer(shimmerColorHighLight);

        myShimmerFrameLayout.startShimmer();

        handleButtononClick( ((Button)findViewById(R.id.stopBtn) ), myShimmerFrameLayout ); //Stop shimmer

    }

    public void handleButtononClick(Button btn, ShimmerFrameLayout shimmer){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shimmer.stopShimmer();
            }
        });
    }
}
