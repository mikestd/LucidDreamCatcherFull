package com.mikestudio.luciddreamcatcher;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    final static String savedWidgetState = "savedWidgetState";
    final static String savedWidgetStateExtra = "savedWidgetStateExtra";
    final static String Reset = "Reset";
    static HashMap<String,String> map = new HashMap<>();
    final static String[] ball = {"imagePoint", "imageView3","imageView4","imageView5","imageView6","imageView7",
            "imageView8","imageView9","imageView10","imageView11","imageView12","imageView13",
            "imageView14", "imageView15","imageView16","imageView17","imageView18","imageView19",
            "imageView20", "imageView21","imageView22","imageView23","imageView24","imageView25",
            "imageView26","imageView27", "imageView28","imageView29","imageView30","imageView31",
            "imageView32","imageView33","imageView34", "imageView35","imageView36","imageView37",
            "imageView38","imageView39","imageView40", "imageView41","imageView42","imageView43",
            "imageView44","imageView45","imageView46","imageView47", "imageView48","imageView49",
            "imageView50","imageView51"};
    final String UPDATE_ALL_WIDGETS = "update_all_widgets";
    final static String[] pero1 = {"pero_1_1_1","pero_1_2_2","pero_1_3_3","pero_1_4_4","pero_1_5_5","pero_1_6_6"};
    final static String[] pero2 = {"pero2_1","pero2_2","pero2_3","pero2_4","pero2_5","pero2_6","pero2_7","pero2_8"};
    final static String[] pero3 = {"pero3_1","pero3_2","pero3_3","pero3_4","pero3_5","pero3_6"};
    final static String[] pile = {"pero1","pero2","pero3","pero4","pero5","pero6","pero7","pero8","pero10","pero11"};
    static int array1, array2, array3 = 0;
    static  boolean isReset = false;
    static boolean isClear = false;
    static int count = -1;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        SharedPreferences sharedPreferences = context.getSharedPreferences(savedWidgetState, Context.MODE_PRIVATE);
        SharedPreferences savePero = context.getSharedPreferences(savedWidgetStateExtra, Context.MODE_PRIVATE);
        SharedPreferences reset = context.getSharedPreferences(Reset, Context.MODE_PRIVATE); // чтобы виджет не чистился при добавлении на экран в промежутке от 00.00 до 00.30
        SharedPreferences mainSettings = context.getSharedPreferences(MainActivity.SETTING_MAIN_ACTIVITY, Context.MODE_PRIVATE);
        SharedPreferences saveCountFeather = context.getSharedPreferences(CheckRealActivity.COUNT, Context.MODE_PRIVATE);
      // update(context, views, sharedPreferences, savePero, reset, saveCountFeather, mainSettings);

        postUpdateWidget(context, views, sharedPreferences, savePero, isClear);//отрисовка всех бусин при добавлении виджета на экран


        for (int i = 0; i < N; i++) {
            updateAppWidget(views, context, appWidgetManager, appWidgetIds[i]);//сюда надо потом будет передать "состояние" виджета чтобы при добавлении на экран он показывал норм инфу
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);
        Intent intent = new Intent(context, AppWidget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                60000, pIntent);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context);
        Intent intent = new Intent(context, AppWidget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    static void updateAppWidget(RemoteViews views, Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object

        Intent startService = new Intent(context, MyService.class).setAction("TapOnWidget");
        PendingIntent pendingIntent = PendingIntent.getService(context, -1, startService, 0);
        views.setOnClickPendingIntent(R.id.net, pendingIntent);

        Intent checkReal = new Intent(context, CheckRealActivity.class);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 1, checkReal, 0);
        views.setOnClickPendingIntent(R.id.pero_1_1_1, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero_1_2_2, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero_1_3_3, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero_1_4_4, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero_1_5_5, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero_1_6_6, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero2_1, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero2_2, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero2_3, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero2_4, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero2_5, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero2_6, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero2_7, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero2_8, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero3_1, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero3_2, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero3_3, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero3_4, pendingIntent1);
        views.setOnClickPendingIntent(R.id.peroCheck, pendingIntent1);
        views.setOnClickPendingIntent(R.id.pero3_6, pendingIntent1);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void drawBall(Context context, String ballColor){
        SharedPreferences sharedPreferences = context.getSharedPreferences(savedWidgetState, Context.MODE_PRIVATE);

        for (Map.Entry entry : sharedPreferences.getAll().entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue().toString());
        }

        RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        while (1==1){
            Random random = new Random(System.currentTimeMillis());
            String numberBall = ball[random.nextInt(50)];//"id" шарика на виджете
            if (map.size()==50){
                Toast.makeText(context, context.getString(R.string.Balls_Is_Full),Toast.LENGTH_SHORT).show();
                return;
            }

            if (!map.containsKey(numberBall)) {

                extraDraw(v,context, numberBall, ballColor, sharedPreferences);//логика отрисовки бусин и их цветов
                break;
            }
        }
        //обновляем экземпляры виджета
        ComponentName name = new ComponentName(context, AppWidget.class);
        int [] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(name);//получаем id всех экземпляров

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        for (int i = 0; i <  ids.length; i++) {
            updateAppWidget(v, context, appWidgetManager, ids[i]);
        }

    }

    static void postUpdateWidget(Context context, RemoteViews v, SharedPreferences sharedPreferences, SharedPreferences savePero, boolean isClear) {
       // SharedPreferences sharedPreferences = context.getSharedPreferences(savedWidgetState, Context.MODE_PRIVATE);
        SharedPreferences reset = context.getSharedPreferences(Reset, Context.MODE_PRIVATE);
        if (reset.contains("counter")){
            count = reset.getInt("counter",0);
            for (int i = 0; i <= count; i++){
                if (i >= 0 && i <=9){
                v.setViewVisibility(context.getResources().getIdentifier(pile[i], "id", context.getPackageName()), View.VISIBLE);
                }
            }





        }

        for (Map.Entry entry : sharedPreferences.getAll().entrySet()) {
            Log.d("loop", "loop");
            map.put(entry.getKey().toString(), entry.getValue().toString());

            if (!isClear) {//ЕСЛИ НЕ ПРИШЛО ВРЕМЯ ЧИСТИТЬ ВИДЖЕТ

                v.setImageViewResource(context.getResources().getIdentifier(entry.getKey().toString(), "id", context.getPackageName()),
                        context.getResources().getIdentifier(entry.getValue().toString(), "mipmap", context.getPackageName()));
                v.setViewVisibility(context.getResources().getIdentifier(entry.getKey().toString(), "id", context.getPackageName()), View.VISIBLE);
            } else {
                v.setViewVisibility(context.getResources().getIdentifier(entry.getKey().toString(), "id", context.getPackageName()), View.INVISIBLE);
            }

        }
        if (savePero.contains("arrayOne")){
            array1 = savePero.getInt("arrayOne",0);
        }
        if (savePero.contains("arrayTwo")){
            array2 = savePero.getInt("arrayTwo",0);
        }
        if (savePero.contains("arrayThree")){
            array3 = savePero.getInt("arrayThree",0);
        }
        for (int i = 0; i < array1; i++) {
            if (!isClear) {
                v.setViewVisibility(context.getResources().getIdentifier(pero1[i], "id", context.getPackageName()), View.VISIBLE);
            } else {
                v.setViewVisibility(context.getResources().getIdentifier(pero1[i], "id", context.getPackageName()), View.INVISIBLE);
            }
        }
        for (int i = 0; i < array2; i++) {
            if (!isClear) {
                v.setViewVisibility(context.getResources().getIdentifier(pero2[i], "id", context.getPackageName()), View.VISIBLE);
            } else {
                v.setViewVisibility(context.getResources().getIdentifier(pero2[i], "id", context.getPackageName()), View.INVISIBLE);
            }
        }
        for (int i = 0; i < array3; i++) {
            if (!isClear) {
                v.setViewVisibility(context.getResources().getIdentifier(pero3[i], "id", context.getPackageName()), View.VISIBLE);
            } else {
                v.setViewVisibility(context.getResources().getIdentifier(pero3[i], "id", context.getPackageName()), View.INVISIBLE);
            }
        }
        if (isClear) {
            SharedPreferences.Editor editor1 = sharedPreferences.edit();
            editor1.clear();
            editor1.apply();
            SharedPreferences.Editor editor2 = savePero.edit();
            editor2.clear();
            editor2.apply();
            array1 = 0;
            array2 = 0;
            array3 = 0;
            map.clear();
            for (int i = 0; i <= 9; i++){
                v.setViewVisibility(context.getResources().getIdentifier(pile[i], "id", context.getPackageName()), View.INVISIBLE);
            }
            count = -1;
            reset.edit().putInt("counter", count).apply();
        }

    }


    static public void extraDraw(RemoteViews v, Context context, String numberBall, String ballColor, SharedPreferences sharedPreferences){
        //SharedPreferences sharedPreferences = context.getSharedPreferences(savedWidgetState, Context.MODE_PRIVATE);

        map.put(numberBall, ballColor);
        v.setImageViewResource(context.getResources().getIdentifier(numberBall, "id", context.getPackageName()),
                context.getResources().getIdentifier(ballColor, "mipmap", context.getPackageName()));
        v.setViewVisibility(context.getResources().getIdentifier(numberBall, "id", context.getPackageName()), View.VISIBLE);

        SharedPreferences.Editor editor = sharedPreferences.edit();//cохраняем в sharedPreferences

        for( Map.Entry entry : map.entrySet() ) {
            editor.putString(entry.getKey().toString(), entry.getValue().toString());
            editor.apply();
        }
        //Log.d("extraDraw", numberBall);
        //Log.d("extraDraw", ballColor);

        //обновляем виджеты
        ComponentName name = new ComponentName(context, AppWidget.class);
        int [] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(name);//получаем id всех экземпляров

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        for (int i = 0; i <  ids.length; i++) {
            updateAppWidget(v, context, appWidgetManager, ids[i]);
        }
    }

    static public void peroDraw (Context context, int temp){
        SharedPreferences savePero = context.getSharedPreferences(savedWidgetStateExtra, Context.MODE_PRIVATE);

        if (savePero.contains("arrayOne")){
            array1 = savePero.getInt("arrayOne",0);
        }
        if (savePero.contains("arrayTwo")){
            array2 = savePero.getInt("arrayTwo",0);
        }
        if (savePero.contains("arrayThree")){
            array3 = savePero.getInt("arrayThree",0);
        }
        if (array1 == pero1.length  && array2 == pero2.length && array3 == pero3.length){
            Toast.makeText(context,R.string.Balls_Is_Full,Toast.LENGTH_SHORT).show();
            return;
        }
        RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        switch (temp){
            case 0 :
                if (array1 <= pero1.length - 1) {
                    v.setViewVisibility(context.getResources().getIdentifier(pero1[array1], "id", context.getPackageName()), View.VISIBLE);
                    array1++;
                } else peroDraw(context, 1);

                //сделать проверку на переполнение размера массива
                //если забит под завязку то переходим на другие массивы
                break;
            case 1 :

                if (array2<= pero2.length -1) {
                    v.setViewVisibility(context.getResources().getIdentifier(pero2[array2], "id", context.getPackageName()), View.VISIBLE);
                    array2++;
                } else peroDraw(context,2);

                break;
            case 2 :
                if (array3 <= pero3.length - 1) {
                    v.setViewVisibility(context.getResources().getIdentifier(pero3[array3], "id", context.getPackageName()), View.VISIBLE);
                    array3++;
                } else peroDraw(context,0);

                break;
        }
        SharedPreferences.Editor editor = savePero.edit();
        editor.putInt("arrayOne", array1);
        editor.putInt("arrayTwo",array2);
        editor.putInt("arrayThree",array3);
        editor.apply();

        //обновляем виджеты
        ComponentName name = new ComponentName(context, AppWidget.class);
        int [] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(name);//получаем id всех экземпляров

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        for (int i = 0; i <  ids.length; i++) {
            updateAppWidget(v, context, appWidgetManager, ids[i]);
        }

    }

    /*static public void message(Context context){
        Toast.makeText(context,"Falling", Toast.LENGTH_SHORT).show();
    }*/
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equalsIgnoreCase(UPDATE_ALL_WIDGETS)) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            SharedPreferences sharedPreferences = context.getSharedPreferences(savedWidgetState, Context.MODE_PRIVATE);
            SharedPreferences savePero = context.getSharedPreferences(savedWidgetStateExtra, Context.MODE_PRIVATE);
            SharedPreferences reset = context.getSharedPreferences(Reset, Context.MODE_PRIVATE); // чтобы виджет не чистился при добавлении на экран в промежутке от 00.00 до 00.30
            SharedPreferences mainSettings = context.getSharedPreferences(MainActivity.SETTING_MAIN_ACTIVITY, Context.MODE_PRIVATE);
            SharedPreferences saveCountFeather = context.getSharedPreferences(CheckRealActivity.COUNT, Context.MODE_PRIVATE);
            update(context, views, sharedPreferences, savePero, reset, saveCountFeather, mainSettings);
            ComponentName thisAppWidget = new ComponentName(
                    context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID : ids) {
                updateAppWidget(views,context, appWidgetManager, appWidgetID);
            }
        }
    }
    static void update (Context context, RemoteViews views, SharedPreferences sharedPreferences, SharedPreferences savePero, SharedPreferences reset, SharedPreferences saveCountFeather, SharedPreferences mainSettings){
        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);

       // Toast.makeText(context, "UPDATE", Toast.LENGTH_LONG).show();
        //Log.d("ONUPDATE", "ONUPDATE");

        if (reset.contains("isReset")) {
            isReset = reset.getBoolean("isReset", false);
        }

        if (hours * 3600 + minutes * 60 + seconds <= 1800) {
            if (!isReset) {
                isReset = true;
                isClear = true;
                Log.d("CLEAR", "CLEAR");

               /* SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.clear();
                editor1.apply();*/

               /* SharedPreferences.Editor editor2 = savePero.edit();
                editor2.clear();
                editor2.apply();*/

                SharedPreferences.Editor editor3 = mainSettings.edit();
                editor3.clear();
                editor3.apply();

                SharedPreferences.Editor editor4 = saveCountFeather.edit();
                editor4.clear();
                editor4.apply();

                reset.edit().putBoolean("isReset", isReset).apply();
                postUpdateWidget(context, views, sharedPreferences, savePero, isClear);//отрисовка всех бусин при добавлении виджета на экран
                isClear = false;
            }
        } else {
            isReset = false;
            reset.edit().putBoolean("isReset", isReset).apply();
        }


    }

    static void fallingPero (Context context){
        RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        SharedPreferences reset = context.getSharedPreferences(Reset, Context.MODE_PRIVATE);

        if (reset.contains("counter")){
            count = reset.getInt("counter",0);
        }
        count ++;
        reset.edit().putInt("counter", count).apply();
        if (count >= 0 && count <=9){
            v.setViewVisibility(context.getResources().getIdentifier(pile[count], "id", context.getPackageName()), View.VISIBLE);
        }





        //обновляем экземпляры виджета
        ComponentName name = new ComponentName(context, AppWidget.class);
        int [] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(name);//получаем id всех экземпляров

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        for (int i = 0; i <  ids.length; i++) {
            updateAppWidget(v, context, appWidgetManager, ids[i]);
        }

    }
}


