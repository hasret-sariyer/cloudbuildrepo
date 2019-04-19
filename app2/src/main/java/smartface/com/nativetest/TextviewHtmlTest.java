package smartface.com.nativetest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TextviewHtmlTest  extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.htmltexttest);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.htmlLayout);

        EditText editText = (EditText) findViewById(R.id.editText);

//      textView.setSingleLine(true);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD |InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS );
        Log.d("inputType" , " " + editText.getInputType());


        Button addBtn = (Button) findViewById(R.id.addBtn);
        Button removeBtn = (Button) findViewById(R.id.removeBtn);
        Button resetBtn = (Button) findViewById(R.id.resetBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int combined = setAndUnsetFlags(editText.getInputType(), 0 , Integer.valueOf(editText.getText().toString()));
                editText.setInputType(combined);
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int combined = setAndUnsetFlags(editText.getInputType(), Integer.valueOf(editText.getText().toString()) , 0);
                editText.setInputType(combined);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Toast.makeText(this, "on back button pressed " , Toast.LENGTH_SHORT).show();
    }
//
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Toast.makeText(this, "  onConfigurationChanged " , Toast.LENGTH_SHORT).show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                textView.setTransformationMethod(null);
//                textView.setTransformationMethod(new PasswordTransformationMethod());
            }
        },1000);
        super.onConfigurationChanged(newConfig);
    }

    int setAndUnsetFlags(int current, int unsetFlags, int setFlags){
        int combined = ( (current & ~unsetFlags) | setFlags);
        return combined;
    }
}
