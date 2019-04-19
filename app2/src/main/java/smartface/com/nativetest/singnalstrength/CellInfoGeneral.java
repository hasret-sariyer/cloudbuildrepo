package smartface.com.nativetest.singnalstrength;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import smartface.com.nativetest.R;

public class CellInfoGeneral extends AppCompatActivity {

    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cellinfogeneral);

        telephonyManager = (TelephonyManager)this.getSystemService(this.TELEPHONY_SERVICE);

        TextView cellInfoTextView = (TextView) findViewById(R.id.cellInfoTextView);

        String cellInfoText = " Cell Info " + " \n" +
                " Cell Service " + getCellService() + "\n" +
                " " + getCellStrength() ;

        cellInfoTextView.setText(cellInfoText);
    }

    public String getCellService(){
        int  phoneType = telephonyManager.getPhoneType();
         switch (phoneType){
            case TelephonyManager.PHONE_TYPE_CDMA:
                return "cdma";

            case TelephonyManager.PHONE_TYPE_GSM:
                return "gsm";
            case TelephonyManager.PHONE_TYPE_SIP:
                return "sip";
            case TelephonyManager.PHONE_TYPE_NONE:
                return "none";

                default:
                    return  "defaul";
        }
    }

    public String getCellStrength(){

        List<CellInfo> infos = telephonyManager.getAllCellInfo();

        Toast.makeText(this, " size " + infos.size() , Toast.LENGTH_SHORT).show();
        String cellStrengthText = "";
        for (CellInfo info : infos) {
            CellSignalStrengthGsm cellLte = ((CellInfoGsm)info).getCellSignalStrength();
//            CellIdentityLte identityLte = ((CellInfoLte) info).getCellIdentity();
            cellStrengthText += " " + " Cell Strength " + cellLte.getTimingAdvance() + "\n" +
                    " Dbm " + cellLte.getDbm();
        }

        return cellStrengthText;

    }

}
