package smartface.com.nativetest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import java.util.Locale;
/*
The intend of creating this class is to provide easy way to change configurations.
 */
public class LocaleConfigurationUtil {
    static String DEFAULT = "auto";
    static Context context;
    static Configuration config;

    public  static void changeConfigurationLocale(Context activityContext){
        context = activityContext;
        config = context.getResources().getConfiguration();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Configuration config = context.getResources().getConfiguration();

        if(sharedPreferences.getString("AppLocale", "NoneChanges") != "NoneChanges") {
            String localeString = Locale.getDefault().getLanguage();

            String gotLocale = sharedPreferences.getString("AppLocale",DEFAULT);
            String defaulLang = (sharedPreferences.getString("AppLocale",DEFAULT).equals(DEFAULT) ?  Locale.getDefault().getLanguage() : sharedPreferences.getString("AppLocale",DEFAULT));
            Locale locale = new Locale(defaulLang);
            locale.setDefault(locale);
            config.setLocale(locale);
            config.updateFrom(config);
            context.createConfigurationContext(config);
        }
    }

    public static String getDeviceLanguage() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return  config.getLocales().get(0).getLanguage();
        }
        return config.locale.getLanguage();
    }
}

