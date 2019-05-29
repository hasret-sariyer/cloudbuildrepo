package smartface.com.nativetest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TextViewTouchListenerTest extends AppCompatActivity {

    Context parentContext;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.textviewtest);

        parentContext = this;

        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.myRootView);
        EditText myTextView = (EditText) findViewById(R.id.myTextView);

        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(parentContext, "rootLayout.setOnTouchListener", Toast.LENGTH_SHORT).show();
                Log.i("onTouch ", "rootLayout.setOnTouchListener");
                return true;
            }
        });

        myTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(parentContext, "myTextView.setOnTouchListener", Toast.LENGTH_SHORT).show();
                Log.i("onTouch ", "myTextView.setOnTouchListener");
                return false;
            }
        });
    }
}
