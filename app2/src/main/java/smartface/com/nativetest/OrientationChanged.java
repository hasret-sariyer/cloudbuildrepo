package smartface.com.nativetest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
//import android.support.v4.content.res.TypedArrayUtils;
//import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by smartface on 23.07.2018.
 */

public class OrientationChanged extends AppCompatActivity {

    RelativeLayout rlParent;


    File outFile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        int Pwidth = rlParent.getWidth();
//        int Pheight = rlParent.getHeight();

//        Log.i("assets", "first Pwidth  "+ getAssets().open("s"));

//        Log.i("orientation", "first Pwidth  "+ Pwidth+"   Pheight  "+Pheight);

        setContentView(R.layout.activity_native_test);

         rlParent = (RelativeLayout) findViewById(R.id.relativeL);

        String[] str = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};




        try {
            outFile = new File(this.getExternalCacheDir().getAbsolutePath()+"/GBKCCN.pdf");
            InputStream inpt = getAssets().open("GBKCCN.pdf");
            BufferedInputStream buff =new BufferedInputStream(inpt);
            OutputStream  out = new FileOutputStream(outFile,false);
            FileUtil.copyStream(inpt,out);
            copyToFile(buff,out);
            openPdf();
            requestPermissions(str,10);
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        screenSize();
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    void openPdf(){
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.parse("file://" + outFile.getAbsolutePath()), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = Intent.createChooser(target, "Open File");

        startActivity(intent);
    }

    void copyToFile(InputStream in, OutputStream out)  throws IOException {
        byte[]  buffer = new byte[1024];
        int read =in.read(buffer);
        while(read != -1){
            System.out.println("read " + read);
            out.write(buffer, 0, read);
            read= in.read(buffer);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 10 ){
            openPdf();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        screenSize();
        System.out.println( "onConfigurationChanged ");
    }

    void screenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        int Pwidth = rlParent.getWidth();
        int Pheight = rlParent.getHeight();

        Log.i("orientation", "Pwidth  "+ Pwidth+"   Pheight  "+Pheight);

        Log.i("orientation", "width " + width + " height " +height);
    }


}
