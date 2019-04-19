package smartface.com.nativetest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import  com.google.android.gms.location.LocationServices;


/**
 * Created by smartface on 13.08.2018.
 */

public class GoogleServiceLocationText extends AppCompatActivity {

    LocationRequest locationRequest;
    final long LOCATION_INTERVAL = 1000 * 1;
    FusedLocationProviderClient locationProviderClient ;
    LocationCallback lc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.textviewsize_animate);

        Button  btn  =  (Button)  findViewById(R.id.btnAnimate);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationProviderClient.removeLocationUpdates(lc); //removes thhe given callback registration
            }
        });

        startLocationUpdates();
    }


    public void startLocationUpdates(){

        locationRequest  = LocationRequest.create();
        locationRequest.setInterval(LOCATION_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  }, 150);
        }

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

//        lc =new LocationCallback(){
//
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                locationUpdate(locationResult.getLastLocation());
//            }
//        };
        lc = new SFLocationCallback();

        locationProviderClient.requestLocationUpdates(locationRequest ,lc, Looper.myLooper());

        getLastKnownLocation();
    }


    public void locationUpdate(Location location) {
        System.out.println("location  getLatitude "  + location.getLatitude() +  "  getLongitude "  + location.getLongitude());
    }

    void getLastKnownLocation() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            locationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            System.out.println("LAST KNOWN LOCATION IS " + location.getLatitude()  + "  getLongitude  " + location.getLongitude());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == resultCode){

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("on Destroy");
    }
}
