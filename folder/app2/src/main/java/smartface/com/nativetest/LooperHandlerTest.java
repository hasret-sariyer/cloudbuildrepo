package smartface.com.nativetest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthLte;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class LooperHandlerTest extends AppCompatActivity implements Handler.Callback, View.OnClickListener {
    private Handler mainThreadHandler;
    public CalculatorThread workerThread;

    private TextView displayerTextView;

    //Message IDs
    private static final int MSG_START = 1;
    private static final int MSG_END = 0;

    private static final int MSG_UPDATE = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.looperhandlertest);

        displayerTextView = findViewById(R.id.displayerView);

        Button myButton = (Button) findViewById(R.id.startBtn);
        myButton.setOnClickListener(this);

        mainThreadHandler = new Handler(this);
        workerThread = new CalculatorThread();
        workerThread.start();

    }

    @Override
    public void onClick(View view) {
        if (view instanceof Button) {
            Message startMessage = workerThread.myHandler.obtainMessage(MSG_START);
            startMessage.sendToTarget();
        }
    }

    String makeText;
    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case MSG_UPDATE:
                 makeText += "" + " Cell :" + " \n " +
                        " Timeing Advance : " + message.arg1 + " \n " +
                        "Dbm : " + message.arg2 + " \n \n \n";

                displayerTextView.setText(makeText);

            case MSG_END:
                Message workerMSG = workerThread.myHandler.obtainMessage(MSG_END);
                workerMSG.sendToTarget();
        }
        return false;
    }

    public void getSignalStrength() {
//        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
////            // TODO: Consider calling
////            //    ActivityCompat#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for ActivityCompat#requestPermissions for more details.
////            return;
////        }
//        List<CellInfo> cellInfoList = tm.getAllCellInfo();
//        int count =0;
//        for (CellInfo cellInfo : cellInfoList)
//        {
//            if (cellInfo instanceof CellInfoLte)
//            {
//                count += 1;
//                int dbm = ((CellInfoLte)cellInfo).getCellSignalStrength().getDbm();
//                int timingAdvanve = ((CellInfoLte)cellInfo).getCellSignalStrength().getTimingAdvance();
//
//                Message activityMsg = mainThreadHandler.obtainMessage(MSG_UPDATE, timingAdvanve,dbm);
//                activityMsg.sendToTarget();
//            }
//        }
//
//        Message activityMsg = mainThreadHandler.obtainMessage(MSG_END);
//        activityMsg.sendToTarget();
    }


    private class CalculatorThread extends  Thread implements Handler.Callback{
        private Looper myLooper;
        protected  Handler myHandler;

        @Override
        public void run() {
            Looper.prepare();
            myLooper = Looper.myLooper();
            myHandler = new Handler(myLooper, this);
            Looper.loop();
        }

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case MSG_START:
                    getSignalStrength();
                case MSG_END:
                    myLooper.quit();
            }
            return false;
        }
    }
}
