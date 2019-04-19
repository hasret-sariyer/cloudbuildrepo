package smartface.com.nativetest;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.content.res.Configuration;

import java.util.Locale;

public class MutableContextWrappers extends MutableContextWrapper {
    public MutableContextWrappers(Context base) {
        super(base);
    }
}
