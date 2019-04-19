package smartface.com.nativetest;

import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;

import java.net.URI;

//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by smartface on 18.07.2018.
 */

public class EmailTest extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_native_test);

        String TO = "*****@gmail.com";
        String CC ="*****@gmail.com";

        String html = "<p><b>Some Content</b></p>";

        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Smartface");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, Uri.parse("market://details?id=io.smartface.SmartfaceDemo"));

//        intent.setType("text/html");
        //intent.setData(Uri.parse("mailto:"));
//        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{TO});
//        intent.putExtra(Intent.EXTRA_CC, new String[]{CC});
//        intent.putExtra(Intent.EXTRA_TEXT,Html.fromHtml(html ));


        Log.d("startActivity", " " + Uri.parse("market://details?id=io.smartface.SmartfaceDemo"));
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.facebook.katana")));
//        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 150){
            System.out.println(" in onActivityResult ");
        }
    }


}
