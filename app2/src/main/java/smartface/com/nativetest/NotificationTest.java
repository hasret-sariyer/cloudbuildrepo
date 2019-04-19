package smartface.com.nativetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

public class NotificationTest extends AppCompatActivity {

    String NOTIFICATION_TITLE = "Nofication Test";
    int  EMULATOR_NOTIFICATION_CODE = 987;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotification();
    }

    public void createNotification(){
        String ns = Context.NOTIFICATION_SERVICE;
        final String defaultSmarfaceId = "7";
        NotificationManager mNotificationManager = (NotificationManager) getSystemService (ns);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            final String defaultSmarfaceTitle = NOTIFICATION_TITLE;
            int importance  = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(defaultSmarfaceId,defaultSmarfaceTitle,importance);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder (this,defaultSmarfaceId);
        Notification emulatorNotification = notiBuilder.setContentTitle(NOTIFICATION_TITLE)
                .setSmallIcon(R.drawable.com_mixpanel_android_ic_megaphone)
                .build();

        emulatorNotification.flags  |= Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(EMULATOR_NOTIFICATION_CODE,emulatorNotification);
    }
}
