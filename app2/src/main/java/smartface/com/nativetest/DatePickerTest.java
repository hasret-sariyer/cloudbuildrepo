package smartface.com.nativetest;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class DatePickerTest extends AppCompatActivity {

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.htmltexttest);


        DatePickerDialog datePickerTest = new DatePickerDialog(this,android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                System.out.println("datePicker " + datePicker.getYear());
            }
        },2018,12,12);



        datePickerTest.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                System.out.println("setOnCancelListener ");
            }
        });




        int[][] state = new int[][]{
               new int[]{android.R.attr.state_focused},
                new int[]{-android.R.attr.state_focused}
        };

        int[] colors  = new int[] {Color.RED,Color.BLACK};

        ColorStateList colorStateList = new ColorStateList(state,colors);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            positiveButton.setBackgroundTintList(colorStateList);
//        }

        datePickerTest.show();
    }
}
