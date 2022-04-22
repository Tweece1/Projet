package uqac.dim.stopforgettest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class Alarm extends BroadcastReceiver {

    public static String EXTRA_ALARME = "EXTRA_ALARME";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("DIM", "Alarm.onReceive");
    }

    public void setAlarm(Context context) {

        Log.i("DIM", "Alarm.setAlarm");

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);

        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra("EXTRA_ALARME", "IL EST l'HEURE");
        PendingIntent pi = PendingIntent.getBroadcast(context, 1, i, 0);

        //PendingIntent pi2 = PendingIntent.getBroadcast(context, 1, i2, PendingIntent.FLAG_CANCEL_CURRENT);
        //am.setAlarmClock(new AlarmManager.AlarmClockInfo(getTargetTime(2, 18, 30).getTimeInMillis(), pi), pi);        // méthode normale

        Calendar now_plus_1_minute = Calendar.getInstance();
        now_plus_1_minute.add(Calendar.MINUTE, 1);
        am.setAlarmClock(new AlarmManager.AlarmClockInfo(now_plus_1_minute.getTimeInMillis(), pi), pi);
    }

    public void cancelAlarm(Context context) {

        Log.i("DIM", "Alarm.cancelAlarm");

        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private Calendar getTargetTime(int jour, int heure, int minute) {

        Calendar alarme = Calendar.getInstance();
        alarme.set(Calendar.DAY_OF_MONTH, jour);
        alarme.set(Calendar.HOUR_OF_DAY, heure);
        alarme.set(Calendar.MONTH,12);
        alarme.set(Calendar.MINUTE, minute);

        return alarme;
    }

}