package smartface.com.nativetest;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Locale;

public class ApplicationDirection extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.attributedtextest);

        RelativeLayout myRelativLayout=  (RelativeLayout) findViewById(R.id.myRelativeLayout);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        Configuration configuration =  this.getResources().getConfiguration();

        changeConfigurationLocale(this, "ar");

        Button btn = new Button(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("getDeviceLanguage " + getDeviceLanguage(configuration));
            }
        });

        myRelativLayout.addView(btn);
    }

    static String DEFAULT = "auto";

    public  static void changeConfigurationLocale(Context context , String locale){
        Configuration config = context.getResources().getConfiguration();

        String defaulLang = locale == DEFAULT ?  getDeviceLanguage(config) : locale;
        Locale localeObj = new Locale(defaulLang);
        localeObj.setDefault(localeObj);
        config.setLayoutDirection(localeObj);
        context.createConfigurationContext(config);
    }

    public static String getDeviceLanguage(Configuration config) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return  config.getLocales().get(0).getLanguage();
        }
        return Locale.getDefault().getLanguage();
    }
}
