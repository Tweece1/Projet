package uqac.dim.stopforgettest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;

public class MonServiceAlarm extends Service {

    private IBinder monBinder;
    private Alarm alarm = new Alarm();

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int[] dates=intent.getIntArrayExtra("test");
        String[] all_lists=intent.getStringArrayExtra("all");
        alarm.setAlarm(this, dates, all_lists);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }
}
