package smartface.com.nativetest;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.util.Log;

import java.util.Locale;

public class ContextWrappers extends ContextWrapper {

    ContextWrappers(Context context){
        super(context);
    }

    protected  static Context wrapContext(Context context){
        Configuration config = context.getResources().getConfiguration();
        Locale locale = new Locale("ar");
        locale.setDefault(locale);
        config.setLocale(locale);


        return new ContextWrappers(context.createConfigurationContext(config));
    }


}
