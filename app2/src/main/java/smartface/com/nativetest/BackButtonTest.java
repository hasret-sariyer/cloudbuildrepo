package smartface.com.nativetest;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BackButtonTest extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.backbuttonlayout);

        CoordinatorLayout myRelativeLayout = (CoordinatorLayout) findViewById(R.id.myRelativeLayout);
//        LinearLayout myLinearLAyout = ( LinearLayout ) findViewById(R.id.myLinearLayout);
//
        WebView myWebView = new WebView(this);
        myWebView.setWebViewClient(new WebClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                System.out.println("error " + error.toString()  );
                super.onReceivedSslError(view, handler, error);
            }
        });
        myWebView.loadUrl("https://mobileapp.teknosa.com/");

        myWebView.getSettings().setDomStorageEnabled(false);


        myRelativeLayout.setFocusableInTouchMode(true);
        myRelativeLayout.setFocusable(true);

        myRelativeLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.e("@@SMF", "" + "myRelativeLayout.setOnKeyListener ");
                return false;
            }
        });

        myRelativeLayout.addView(myWebView);

//        myLinearLAyout.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                Log.d("onKey", "myLinearLAyout.setOnKeyListener");
//                return false;
//            }
//        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Log.e("@@SMF", "" + "on BACK BUTTON ");
    }

    public class WebClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    }
}
