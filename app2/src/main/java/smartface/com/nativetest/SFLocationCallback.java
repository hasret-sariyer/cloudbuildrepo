package smartface.com.nativetest;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

/**
 * Created by smartface on 14.08.2018.
 */

public class SFLocationCallback extends LocationCallback {
    GoogleServiceLocationText googleServiceLocationText;
     static int testStatic= 5;

    public SFLocationCallback() {
        googleServiceLocationText = new GoogleServiceLocationText();
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        googleServiceLocationText.locationUpdate(locationResult.getLastLocation());
    }

    @Override
    public void onLocationAvailability(LocationAvailability locationAvailability) {
    }
}
