package com.mikestudio.luciddreamcatcher;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootCompleteReceiver extends BroadcastReceiver {
    public BootCompleteReceiver() {
    }
    SharedPreferences sharedPreferences;
    AlarmManager alarmManager;
    int interval;
    private final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
    //√Œ¬Œ–ﬂ“ ◊“Œ –≈—»¬≈– ÃŒ∆≈“ Õ≈ —–¿¡Œ“¿“‹ Õ¿ œ≈–≈Ÿ¿√–”« ” » œŒ›“ŒÃ” ÃŒ∆ÕŒ ¬€—“¿¬»“‹ ≈Ÿ≈ Õ¿ »«Ã≈Õ≈Õ»≈ ¡¿“¿–≈… »

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String action = intent.getAction();
        interval = sharedPreferences.getInt("AlarmInterval", -1);
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);



        if(action.equals("luciddreamcatcher.action.alarmOn")){

            int state = intent.getExtras().getInt("extraOn", -1);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("AlarmInterval", state);
            editor.apply();

        }

        if(action.equals("luciddreamcatcher.action.alarmOff")){
            int state = intent.getExtras().getInt("extraOff", -1);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("AlarmInterval", state);
            editor.apply();
        }

        if (action.equalsIgnoreCase(BOOT_ACTION)) {
            if (interval!=-1) {
                Intent intentAlarm = new Intent(context, MyService.class).setAction("AlarmStart");
                long currentTime = System.currentTimeMillis();
                PendingIntent pendingIntent = PendingIntent.getService(context, 0, intentAlarm, 0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, currentTime + (interval * 60 * 1000 / 2), (interval * 60 * 1000), pendingIntent);
            }
        }
    }
}
