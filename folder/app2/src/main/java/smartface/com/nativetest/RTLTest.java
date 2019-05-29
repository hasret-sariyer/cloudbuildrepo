package smartface.com.nativetest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;

import java.util.Locale;

public class RTLTest extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        getConfigurationWithDirection(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.rtltest);

        Bundle myBundle = null;
//        try {
            myBundle = this.getApplicationInfo().metaData;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }

        Log.d("RTLTestActivity" , " onCreate  value is " + myBundle.getString("direction"));

        Configuration config = this.getResources().getConfiguration();

        View rootLayout  = this.getLayoutInflater().inflate(R.layout.rtltest, null);

        View myRootView = rootLayout.findViewById(R.id.myRootView);

        Toolbar toolbar =  (Toolbar) findViewById(R.id.toolbar_top);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Hello Toolbar");


        Button getLayoutBtn = (Button) findViewById(R.id.getLDBtn);
        Button getLocale = (Button) findViewById(R.id.getLocale);
        Button resetBtn = (Button) findViewById(R.id.resetBtn);


        getLayoutBtn.setOnClickListener((touch) -> {
            Context uiContext = this;
            Configuration config1 = this.getResources().getConfiguration();
        });
        resetBtn.setOnClickListener((touch) -> {
            getConfigurationWithDirection(this);
            Intent appIntent = getIntent();
            finish();
            startActivity(appIntent);
        });

        }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        Log.d("RTLTestActivity" , " attachBaseContext ");
//
//        super.attachBaseContext(getConfigurationWithDirection(newBase));
//    }


    Context getConfigurationWithDirection(Context context){
        Configuration config = context.getResources().getConfiguration();

        Locale locale = new Locale("ar");
        locale.setDefault(locale);
        config.setLocale(locale);
        Context newContext = context.createConfigurationContext(config);

        return  newContext;
    }

}
