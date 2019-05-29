package smartface.com.nativetest;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.customtabs.CustomTabsIntent;

import com.google.firebase.analytics.FirebaseAnalytics;

//import android.util.Log;

public class WebbrowserTest extends AppCompatActivity {

    boolean buildEmulator = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.util.Log.d("PackageName" , "package name is  " +getResourcePackageName(this) );

        int resources = this.getResources().getIdentifier("google_app_id", "string",getResourcePackageName(this) );
        String mystring = getResources().getString(resources);

        android.util.Log.d("PackageName" , "resource value is  " +mystring);


        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ID");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "NAME");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent("TESTDENEME", bundle);

        if (buildEmulator) {
            com.crashlytics.android.Crashlytics.getInstance().crash();
        }

    }

    public static String getResourcePackageName(Context context) {
        int iconId = context.getApplicationContext().getApplicationInfo().icon;

        String packageName = "";
        if (iconId > 0) {
            packageName= context.getResources().getResourcePackageName(iconId);
            android.util.Log.d("PackageName" , " iconId package name is  " + packageName );
        } else {
            packageName =context.getPackageName();
            android.util.Log.d("PackageName" , "else  package name is  " +packageName );
        }
        return packageName;
    }
}
