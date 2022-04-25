package uqac.dim.stopforgettest;

import static uqac.dim.stopforgettest.MainActivity.CHANNEL_ID;
import static uqac.dim.stopforgettest.MainActivity.NOTIFICATION_ID;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String[] all_lists=intent.getStringArrayExtra("all");
        String message="Il vous manque des éléments dans ";
        for (String s:all_lists){
            message+=s+" ";
        }

        Intent intent1=new Intent(context,MainActivity.class);
        PendingIntent pedIntent=PendingIntent.getActivity(context, 0, intent1, 0);
        Notification n  = new Notification.Builder(context, CHANNEL_ID)
                .setContentTitle("Vous avez un rappel")
                .setContentText("Allez viens le ou la rencontrer")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pedIntent)
                .setAutoCancel(false)
                .setOngoing(false)
                .setFullScreenIntent(pedIntent, true)
                .setStyle(new Notification.BigTextStyle().bigText("IMPORTANT"))
                .setTicker("Hey! tu as recu un message!")
                .build();
        try{
            MainActivity.nm.notify(NOTIFICATION_ID, n);
        }
        catch(Exception e){
            Log.i("DICJ", e.getMessage(), e);
        }

        Log.i("DIM", "Alarm.onReceive");
    }

    public void setAlarm(Context context, int[] date_time, String[] all_lists) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);

        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra("all",all_lists);
        PendingIntent pi = PendingIntent.getBroadcast(context, 1, i, 0);

        Calendar now_plus_1_minute = Calendar.getInstance();
        now_plus_1_minute.add(Calendar.MINUTE, 1);
        am.setAlarmClock(new AlarmManager.AlarmClockInfo(getTargetTime(date_time[0], date_time[1], date_time[2],date_time[3]).getTimeInMillis(), pi), pi);
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private Calendar getTargetTime(int jour, int hour, int minute, int month ) {
        Calendar alarme = Calendar.getInstance();

        if (month>0 && month<13){
            if (month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12){
                if (jour>0 && jour<32){
                    if (hour>-1 && hour<24 || minute>-1 && minute<60){
                        alarme.set(Calendar.DAY_OF_MONTH,jour);
                        alarme.set(Calendar.MONTH,month);
                        alarme.set(Calendar.HOUR_OF_DAY,hour);
                        alarme.set(Calendar.MINUTE,minute);
                        alarme.set(Calendar.YEAR,alarme.get(Calendar.YEAR));
                    }
                }
            }
            if (month==4 || month==6 || month==9 || month==11){
                if (jour>0 && jour<31){
                    if (hour>-1 && hour<24 || minute>-1 && minute<60){
                        alarme.set(Calendar.DAY_OF_MONTH,jour);
                        alarme.set(Calendar.MONTH,month);
                        alarme.set(Calendar.HOUR_OF_DAY,hour);
                        alarme.set(Calendar.MINUTE,minute);
                        alarme.set(Calendar.YEAR,alarme.get(Calendar.YEAR));
                    }
                }
            }
            else{
                if (jour>0 && jour<30)
                    if (hour>-1 && hour<24 || minute>-1 && minute<60){
                        alarme.set(Calendar.DAY_OF_MONTH,jour);
                        alarme.set(Calendar.MONTH,month);
                        alarme.set(Calendar.HOUR_OF_DAY,hour);
                        alarme.set(Calendar.MINUTE,minute);
                        alarme.set(Calendar.YEAR,alarme.get(Calendar.YEAR));
                    }
            }
        }

        return alarme;
    }

}
