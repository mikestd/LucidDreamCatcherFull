package com.mikestudio.luciddreamcatcher;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.Calendar;

public class MyService extends Service {
    public MediaPlayer mediaPlayer;
    public Vibrator vibrator;
    CountDownTimer counter;
    AudioManager audiomanager;
    long start;
    long end;
    long timeStart=-1;
    long timeEnd=-1;
    SharedPreferences sharedPreferences;
    long pattern[] = {0,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000};
    int getVolume = 100;
    private final static int MAX_VOLUME = 100;
    float currentVolume = 1 ;
    int SYSTEM_ALARM_VOLUME;
    boolean isVibro = true;
    int id_Sound;
    String audioPath;
    boolean isSound = true;
    boolean isCustomSound;//����� ������ ����� �� 2 ����� ������� ��� ���������.
    boolean isServiceStop = false;//����� ��� ����� ������!!!!!!!!!
    boolean isVibrate = false;// ����� ������ ��������� ��� ���
    boolean isTap = false;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        audiomanager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        SYSTEM_ALARM_VOLUME = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);//�������� ��������� �������� �����, ����� ������� ��� �� ���������
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains("timeSTART")) {
            timeStart = sharedPreferences.getLong("timeSTART", -1);
            start = timeStart;
        } else if (timeStart==-1){//���� ����� �� �����������, �� �� ��������� ������ 22:00 �����������. ����� ����� ��������������� �� ���� ���� � ������ ������
            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, 22);
            calSet.set(Calendar.MINUTE, 0);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            timeStart = calSet.getTimeInMillis();
            start = timeStart;
        }
        if (sharedPreferences.contains("timeEND")) {
            timeEnd = sharedPreferences.getLong("timeEND", -1);
            end = timeEnd;
        } else if (timeEnd==-1){
            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, 6);
            calSet.set(Calendar.MINUTE, 0);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            //calSet.add(Calendar.DATE, 1);// 6 a.m. ���������� ���
            timeEnd = calSet.getTimeInMillis();
            end = timeEnd;
        }
        if (sharedPreferences.contains("currVolume")) {
            getVolume = sharedPreferences.getInt("currVolume", 100);
            currentVolume = (float) (1 - (Math.log(MAX_VOLUME - getVolume) / Math.log(MAX_VOLUME)));
        }
        if (sharedPreferences.contains("isVibro")) {
            isVibro = sharedPreferences.getBoolean("isVibro", true);
        }
        if (sharedPreferences.contains("path")){
            audioPath = sharedPreferences.getString("path", "");
        }
        if (sharedPreferences.contains("isSound")){
            isSound = sharedPreferences.getBoolean("isSound",false);
        }
        if (isSound){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), sharedPreferences.getInt("ResIdSound", R.raw.zvuk1));
            mediaPlayer.setLooping(true);
        } else {
            if (audioPath!=null) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse("file:///"+audioPath));
            }
        }




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() == "TapOnWidget"){//������ �� ���� �� ������
           if (mediaPlayer!=null || isVibrate){
                if (mediaPlayer.isPlaying() || isVibrate){
                    Intent intent1 = new Intent(getApplicationContext(),CheckRealActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);


                } else {
                    Intent intent2 = new Intent(getApplicationContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent2);
                }
            }  else {
                Intent intent2 = new Intent(getApplicationContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
            }
            isTap = true;
            isServiceStop = true;
            stopSelf();
        }

        if (intent.getAction() == "TapFromActivity/Widget") {//������ �� ���� �� ������
            isTap = true;
            stopSelf();
        }

        if (intent.getAction()=="silence") {
            if (intent.getExtras().containsKey("timeStart")) {
                timeStart = intent.getLongExtra("timeStart", -1);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("timeSTART", timeStart);
                editor.apply();
                timeStart = sharedPreferences.getLong("timeSTART", -1);
            }
            if (intent.getExtras().containsKey("timeEnd")) {
                timeEnd = intent.getLongExtra("timeEnd", -1);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("timeEND", timeEnd);
                editor.apply();
                timeEnd = sharedPreferences.getLong("timeEND", -1);
            }
            isTap = true;
            stopSelf();
        }

        if (intent.getAction()=="setAlarmSound"){
            isCustomSound = false;
            if (mediaPlayer!=null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.release();
                }
            }
            id_Sound = intent.getIntExtra("id_sound", R.raw.zvuk1);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("ResIdSound",id_Sound);
            editor.putBoolean("isSound",true);
            editor.apply();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), id_Sound);
           // stopSelf();
        }

        if (intent.getAction()=="setCustomAlarmSound"){
            isSound = false;
            if (mediaPlayer!=null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.release();
                }
            }
            audioPath = intent.getStringExtra("soundPath");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("path", audioPath);
            editor.putBoolean("isSound", false);
            editor.apply();
            if (audioPath!=null) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse("file:///"+audioPath));
            }
            //stopSelf();
        }

        if (intent.getAction()=="changeVolume") {
            getVolume = intent.getIntExtra("volume", 100);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("currVolume", getVolume);
            editor.apply();
           // stopSelf();
        }
        if (intent.getAction()=="setVibro") {
            isVibro = intent.getBooleanExtra("Vibro", true);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isVibro", isVibro);
            editor.apply();
           // stopSelf();
        }


        if (intent.getAction() == "AlarmStart") {

            if (System.currentTimeMillis() > end) {//�� ���������� ������ ������ �������������� �������� ������ � �����
                // � ���� �� ������� ����� ��� ��������� ����� 24 ����
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (start < end) {
                    start = start + 24 * 60 * 60 * 1000;
                    editor.putLong("timeSTART", start);
                    end = end + 24 * 60 * 60 * 1000;
                    editor.putLong("timeEND", end);
                    editor.apply();
                }
                if (start > end) {
                    end = end + 24 * 60 * 60 * 1000;
                    editor.putLong("timeEND", end);
                    editor.apply();
                }
                //����� ����� ���� ������ ����� ������� ��� ��������
                if(System.currentTimeMillis()>=start + 24 * 60 * 60 * 1000){
                    int days = (int)(System.currentTimeMillis()-start)/(24 * 60 * 60 * 1000);//������ ������� ����� ���� �� ������ ������
                    start = start + days * 24 * 60 * 60 * 1000;//���������� � ������� ������ ���-�� ������ ���� ����� ������ �� ������
                    editor.putLong("timeSTART", start);
                    editor.apply();
                }
                if(System.currentTimeMillis()>=end + 24 * 60 * 60 * 1000){
                    int days = (int)(System.currentTimeMillis()-end)/(24 * 60 * 60 * 1000);//������ ������� ����� ���� �� ������ ������
                    end = end + days * 24 * 60 * 60 * 1000;
                    editor.putLong("timeEND", end);
                    editor.apply();
                }
            }

            if (start>end){//���� �� ������ ����� ��������� ������ ������ ��������� ����� ������
                start = start - 24* 60 * 60 *1000;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("timeSTART", start);
                editor.apply();
            }
            if (start + 24 * 60 * 60 * 1000 <= end) {// 22 - 6 ������ �������� �� ����� �����, 6 ������� �� ���� ���� � ����� �� 22 ������� �� 4 ��
                end = end - 24 * 60 * 60 * 1000;//����� ����� ����� � 4 ���� �� 6 ���� ���� ���. ��� ������� ��� ����������!!
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("timeEND", end);
                editor.apply();
            }

            if ((System.currentTimeMillis() >= start) && (System.currentTimeMillis() <= end)) {
                isServiceStop = true;
                stopSelf();
            }

            if (start > end && end > System.currentTimeMillis()) {//������� - time 5 am, ������������� �� 22 pm � �� 6 am. �� ���� ������ �� ������� + ���� ���.
                if (System.currentTimeMillis() < end) {
                    isServiceStop = true;
                    stopSelf();
                }
            }





            if (!isServiceStop) {
                if (audiomanager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL ||
                        audiomanager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {//��������� ��������� ����� ����������
                    if (audiomanager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE) {
                        audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                        if (mediaPlayer != null) {
                            mediaPlayer.setVolume(currentVolume, currentVolume);
                            mediaPlayer.start();//���� ����� �������� �������� �� ������ �� ������
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.CustomSound), Toast.LENGTH_LONG).show();
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.zvuk1);
                            mediaPlayer.setVolume(currentVolume, currentVolume);
                            mediaPlayer.start();//���� ����� �������� �������� �� ������ �� ������
                        }

                    }
                    if (isVibro) {//���� ������� ��� - ����� �����
                        vibrator.vibrate(pattern, -1);
                        isVibrate = true;
                    }
                    counter = new CountDownTimer(10000, 1000) { //��������� ������ 10000 - ��� �����, 1000 - ���� ���!

                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            isVibrate = false;
                            if (!isTap)
                            AppWidget.fallingPero(getApplicationContext());
                            stopSelf();
                        }

                    }.start();

                }
            }
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isVibrate = false;
        isServiceStop = false;
        vibrator.cancel();
        audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, SYSTEM_ALARM_VOLUME, 0);

    }




}

