package smartface.com.nativetest;

import android.support.v4.app.Fragment;

/**
 * Created by smartface on 16.08.2018.
 */

public class FragmentInstance {

    public static Fragment ourInstance = new SecondFragement();

    public static Fragment getInstance() {
        return ourInstance;
    }

    public FragmentInstance() {
    }
}
