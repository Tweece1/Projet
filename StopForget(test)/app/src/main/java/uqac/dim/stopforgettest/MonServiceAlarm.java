package uqac.dim.stopforgettest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MonServiceAlarm extends Service {

    private IBinder monBinder;
    private Alarm alarm = new Alarm();

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarm.setAlarm(this);
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
