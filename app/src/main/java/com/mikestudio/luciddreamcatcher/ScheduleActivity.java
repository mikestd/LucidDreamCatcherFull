package com.mikestudio.luciddreamcatcher;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    TextView mTextInterval, start, end;
    Spinner spinnerInterval;
    Spinner spinnerSound;
    TimePickerDialog timePickerDialog;
    AlarmManager alarmManager;
    boolean isCustomAlarmSet = false;
    boolean isStart;
    boolean isEnd;
    long silenceStart, silenceEnd;
    private SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    final static String savedTime = "savedTime";
    int startHours;
    int startMinutes;
    int endHours;
    int endMinutes;
    Toolbar toolbar;
    AppCompatCheckBox switch_On_Off;
    boolean isSwitchOn = false;
    boolean isItemLoad = true;//передаем ее чтобы узнать загрузилось у нас значение из памяти или мы выбрали сами, чтобы сигнал (setAlarm) не переставлялся каждый раз при заходе в активность
    boolean isCancelDialog = false;
    SeekBar volumeBar;
    CheckBox vibro;
    MediaPlayer mMediaPlayer;
    boolean isActivityOpen = true;//чтобы узнать что активити открылась и не играл вариант звука сигнала из списка
    RadioButton sound;
    RadioButton custom_sound;
    boolean isSoundChecked; //выбрана ли RadioButton sound
    Button chooseSoundButton;// виниловая пластинка
    TextView mPath;//текст с названием файла
    String audioPath;//путь до файла
    String file_name;//имя файла
    boolean isActivityResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mSettings = getSharedPreferences(savedTime, Context.MODE_PRIVATE);
        editor = mSettings.edit();

        toolbar = (Toolbar) findViewById(R.id.toolbar_schedule);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);

        mTextInterval = (TextView) findViewById(R.id.interval_text);
        start = (TextView) findViewById(R.id.textStart);
        end = (TextView) findViewById(R.id.textEnd);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mPath = (TextView)findViewById(R.id.UriPath);
        chooseSoundButton = (Button)findViewById(R.id.buttonChooseSound);

        start.setOnClickListener(startListener);
        end.setOnClickListener(endListener);
        start.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/digitClock.TTF"));
        end.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/digitClock.TTF"));

        volumeBar = (SeekBar)findViewById(R.id.seekBarVolume);
        volumeBar.setProgress(100);
        volumeBar.setOnSeekBarChangeListener(this);

        vibro = (CheckBox)findViewById(R.id.checkBoxVibro);
        vibro.setOnClickListener(TapOnVibroListener);



        spinnerInterval = (Spinner) findViewById(R.id.spinner_time);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.AlarmTime,R.layout.spinner);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerInterval.setAdapter(adapter);
        spinnerInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt("currentSpinnerItem", position);
                editor.apply();
                if (switch_On_Off.isChecked() && !isItemLoad) {//isItemLoad = true если элемент был выбран при старте активити автоматически, а не в ручную.
                    getSpinnerItemToSetAlarm();
                }
                isItemLoad = false;//сам метод запускается при старте активности, поэтому эта запись нужна чтобы потом (второй раз) можно было выбрать элемент
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSound = (Spinner)findViewById(R.id.spinner_Sound);
        ArrayAdapter<?> adapter_sound = ArrayAdapter.createFromResource(this, R.array.SoundChoose,R.layout.spinner);
        adapter_sound.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerSound.setAdapter(adapter_sound);
        spinnerSound.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("currentSoundItem", position);
                editor.apply();

                if (!isActivityOpen || isActivityResult) {//чтобы узнать что активити открылась и не играл вариант звука сигнала из списка
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                    }
                    switch (position) {
                        case 0:
                            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.zvuk1);
                            startService(new Intent(getApplicationContext(), MyService.class).putExtra("id_sound", R.raw.zvuk1).setAction("setAlarmSound"));
                            break;
                        case 1:
                            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.zvuk5);
                            startService(new Intent(getApplicationContext(), MyService.class).putExtra("id_sound", R.raw.zvuk5).setAction("setAlarmSound"));
                            break;
                        case 2:
                            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.zvuk2);
                            startService(new Intent(getApplicationContext(), MyService.class).putExtra("id_sound", R.raw.zvuk2).setAction("setAlarmSound"));
                            break;
                        case 3:
                            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.zvuk3);
                            startService(new Intent(getApplicationContext(), MyService.class).putExtra("id_sound", R.raw.zvuk3).setAction("setAlarmSound"));

                            break;
                        case 4:
                            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.zvuk4);
                            startService(new Intent(getApplicationContext(), MyService.class).putExtra("id_sound", R.raw.zvuk4).setAction("setAlarmSound"));

                            break;
                    }

                    mMediaPlayer.start();
                }
                isActivityOpen = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        switch_On_Off = (AppCompatCheckBox) findViewById(R.id.monitored_switch);//переключатель в toolbar'e
        switch_On_Off.setOnClickListener(this);

        sound = (RadioButton)findViewById(R.id.radioButtonRingtone);
        sound.setOnClickListener(radioButtonListener);
        custom_sound = (RadioButton)findViewById(R.id.radioButtonCustomRingtone);
        custom_sound.setOnClickListener(radioButtonListener);



    }
    RadioButton.OnClickListener radioButtonListener = new RadioButton.OnClickListener() {//
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.radioButtonRingtone){
                spinnerSound.setEnabled(true);
                spinnerSound.setAlpha(1f);
                custom_sound.setChecked(false);
                mPath.setEnabled(false);
                chooseSoundButton.setEnabled(false);

                switch (spinnerSound.getSelectedItemPosition()){
                    case 0 :
                        startService(new Intent(getApplicationContext(), MyService.class).putExtra("id_sound", R.raw.zvuk1).setAction("setAlarmSound"));
                        break;
                    case 1 :
                        startService(new Intent(getApplicationContext(), MyService.class).putExtra("id_sound", R.raw.zvuk5).setAction("setAlarmSound"));
                        break;
                    case 2 :
                        startService(new Intent(getApplicationContext(), MyService.class).putExtra("id_sound", R.raw.zvuk2).setAction("setAlarmSound"));
                        break;
                    case 3 :
                        startService(new Intent(getApplicationContext(), MyService.class).putExtra("id_sound", R.raw.zvuk3).setAction("setAlarmSound"));
                        break;
                    case 4 :
                        startService(new Intent(getApplicationContext(), MyService.class).putExtra("id_sound", R.raw.zvuk4).setAction("setAlarmSound"));
                        break;
                }
                editor.putBoolean("soundChecked", true);
                editor.apply();
            }
            if (v.getId()==R.id.radioButtonCustomRingtone){
                spinnerSound.setEnabled(false);
                spinnerSound.setAlpha(0.4f);//потом запоминаем состояния переключателей и добавляем прозрачность и отключаем спинер
                sound.setChecked(false);
                mPath.setEnabled(true);
                chooseSoundButton.setEnabled(true);
                audioPath = mSettings.getString("audio_path","");
                startService(new Intent(getApplicationContext(), MyService.class).putExtra("soundPath", audioPath).setAction("setCustomAlarmSound"));

                editor.putBoolean("soundChecked", false);
                editor.apply();
            }
        }
    };


    TextView.OnClickListener startListener = new TextView.OnClickListener() {
        @Override
        public void onClick(View v) {
            isEnd = false;
            isStart = true;
            openTimePickerDialog(true);
        }
    };
    TextView.OnClickListener endListener = new TextView.OnClickListener() {
        @Override
        public void onClick(View v) {
            isStart = false;
            isEnd = true;
            openTimePickerDialog(true);
        }
    };

    CheckBox.OnClickListener TapOnVibroListener = new CheckBox.OnClickListener() { //ВКЛ/ВЫКЛ вибратор при сигнале
        @Override
        public void onClick(View v) {
            if (!vibro.isChecked()){
                startService(new Intent(getApplicationContext(), MyService.class).putExtra("Vibro", false).setAction("setVibro"));
                //SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean("VibroSet", false);
                editor.apply();

            } else {
                startService(new Intent(getApplicationContext(), MyService.class).putExtra("Vibro", true).setAction("setVibro"));
                //SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean("VibroSet", true);
                editor.apply();
            }
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        editor.putInt("currentBarProgress", seekBar.getProgress());
        editor.apply();
        startService(new Intent(getApplicationContext(), MyService.class).putExtra("volume", seekBar.getProgress()).setAction("changeVolume"));
    }

    public void setAlarm(int interval) {

        Intent intent = new Intent(this, MyService.class).setAction("AlarmStart");
        long currentTime = System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, currentTime + (interval*60 * 1000), (interval*60 * 1000), pendingIntent);
        isCustomAlarmSet = true;
        //SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("isAlarmSet", isCustomAlarmSet);
        editor.apply();
    }


    public void cancelAlarm() {
        /*if (mSettings.contains("isAlarmSet")) {
            isCustomAlarmSet = (mSettings.getBoolean("isAlarmSet", false));
        }*/
        if (isCustomAlarmSet) {
            Intent intent = new Intent(this, MyService.class).setAction("AlarmStart");
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
            alarmManager.cancel(pendingIntent);
            isCustomAlarmSet = false;
            //Toast.makeText(getApplicationContext(),"CancelAlarm",Toast.LENGTH_SHORT).show();
           // SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean("isAlarmSet", isCustomAlarmSet);
            editor.apply();
            //отправляем в броадкаст значение -1 чтобы при ребуте не играла
            Intent intent1 = new Intent("luciddreamcatcher.action.alarmOff" );
            intent1.putExtra("extraOff", -1);
            sendBroadcast(intent1);
        }


    }

    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24r);
        timePickerDialog.setTitle(getResources().getString(R.string.ChooseTime));
        if (isStart) {
            timePickerDialog.updateTime(startHours, startMinutes);
        }
        if (isEnd) {
            timePickerDialog.updateTime(endHours, endMinutes);
        }
        isCancelDialog = true;
        timePickerDialog.setCancelable(false);
        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.NEGATIVE_BUTTON), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    isCancelDialog = false;
                }
            }
        });

        timePickerDialog.show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (isCancelDialog) {
                Calendar calNow = Calendar.getInstance();
                Calendar calSet = (Calendar) calNow.clone();

                calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calSet.set(Calendar.MINUTE, minute);
                calSet.set(Calendar.SECOND, 0);
                calSet.set(Calendar.MILLISECOND, 0);

                CharSequence date = DateFormat.format("HH:mm", calSet);
                if (isStart) {
                    startHours = calSet.get(Calendar.HOUR_OF_DAY);
                    startMinutes = calSet.get(Calendar.MINUTE);
                    start.setText(date);

                    //SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString("start", start.getText().toString());

                    silenceStart = calSet.getTimeInMillis();

                    editor.putInt("startHour", startHours);
                    editor.putInt("startMinute", startMinutes);
                    editor.apply();
                    startService(new Intent(getApplicationContext(), MyService.class).putExtra("timeStart", silenceStart).setAction("silence"));
                }
                if (isEnd) {
                    endHours = calSet.get(Calendar.HOUR_OF_DAY);
                    endMinutes = calSet.get(Calendar.MINUTE);
                    end.setText(date);

                    //SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString("end", end.getText().toString());

                    silenceEnd = calSet.getTimeInMillis();

                    editor.putInt("endHour", endHours);
                    editor.putInt("endMinute", endMinutes);
                    editor.apply();

                    startService(new Intent(getApplicationContext(), MyService.class).putExtra("timeEnd", silenceEnd).setAction("silence"));
                }
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mSettings.contains("startHour")) {
            startHours = mSettings.getInt("startHour", 22);
        } else {
            startHours = 22;
        }
        if (mSettings.contains("startMinute")) {
            startMinutes = mSettings.getInt("startMinute", 0);
        } else {
            startMinutes = 0;
        }
        if (mSettings.contains("endHour")) {
            endHours = mSettings.getInt("endHour", 6);
        } else {
            endHours = 6;
        }
        if (mSettings.contains("endMinute")) {
            endMinutes = mSettings.getInt("endMinute", 0);
        } else {
            endMinutes = 0;
        }
        if (mSettings.contains("start")) {
            start.setText(mSettings.getString("start", getString(R.string.Error)));
        }
        if (mSettings.contains("end")) {
            end.setText(mSettings.getString("end", getString(R.string.Error)));
        }
        if (mSettings.contains("currentSpinnerItem")){
            spinnerInterval.setSelection(mSettings.getInt("currentSpinnerItem", 0));
            isItemLoad = true;
        }
        if (mSettings.contains("currentSoundItem")){
            spinnerSound.setSelection(mSettings.getInt("currentSoundItem", 0));
            isActivityOpen = true;
        }
        if (mSettings.contains("switch")) {
            isSwitchOn = mSettings.getBoolean("switch", false);
            switch_On_Off.setChecked(isSwitchOn);
        }
        if (mSettings.contains("isAlarmSet")) {
            isCustomAlarmSet = (mSettings.getBoolean("isAlarmSet", false));
        }
        if (mSettings.contains("currentBarProgress")) {
            volumeBar.setProgress(mSettings.getInt("currentBarProgress", 100));
        }
        if (mSettings.contains("VibroSet")) {
            vibro.setChecked(mSettings.getBoolean("VibroSet", true));
        }
        if (mSettings.contains("soundChecked")){
            isSoundChecked = mSettings.getBoolean("soundChecked", false);
            sound.setChecked(isSoundChecked);
        }
        if (sound.isChecked()){
            custom_sound.setChecked(false);
            sound.setChecked(true);
            mPath.setEnabled(false);
            chooseSoundButton.setEnabled(false);
            //потом добавим сюда выключение пути до файла
        } else  {
            spinnerSound.setEnabled(false);
            spinnerSound.setAlpha(0.3f);
            sound.setChecked(false);
            custom_sound.setChecked(true);
        }

        mPath.setText(mSettings.getString("audio_name", getString(R.string.ChooseAudioFile)));


        mMediaPlayer = new MediaPlayer(); //по заходу/возврату в активность создаем новый объект чтобы ошибки не вылетело при выборе звука из спиннера


    }


    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.release();//высвобождаем ресурсы при выходе/сворачивании активности
    }

    @Override
    public void onClick(View v) {//Switch in toolbar
        //SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("switch", switch_On_Off.isChecked());
        editor.apply();
       /* if (mSettings.contains("isAlarmSet")) {
            isCustomAlarmSet = (mSettings.getBoolean("isAlarmSet", false));//нужна для проверки условия else if(!isCustomAlarmSet)
        }*/
        if (!switch_On_Off.isChecked()) {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.AlarmCancelToast),Toast.LENGTH_LONG).show();
            cancelAlarm();
        } else if (!isCustomAlarmSet) {

            if (spinnerInterval.getSelectedItem().toString() != null) {
                //на текущей дате считываем время
                //Этого можно не делать, т.к. это повтор функционала сервиса, но это на случай если сервис давно не запускался и там старая инфа
                //но т.к. я буду сигналку переделывать немного, для работы после ребута, то возможно этот код ниже можно будет убрать
                Calendar calNow = Calendar.getInstance();
                Calendar calSetStart = (Calendar) calNow.clone();

                calSetStart.set(Calendar.HOUR_OF_DAY, startHours);
                calSetStart.set(Calendar.MINUTE, startMinutes);
                calSetStart.set(Calendar.SECOND, 0);
                calSetStart.set(Calendar.MILLISECOND, 0);

                silenceStart = calSetStart.getTimeInMillis();
                startService(new Intent(getApplicationContext(), MyService.class).putExtra("timeStart", silenceStart).setAction("silence"));

                Calendar calSetEnd = (Calendar) calNow.clone();

                calSetEnd.set(Calendar.HOUR_OF_DAY, endHours);
                calSetEnd.set(Calendar.MINUTE, endMinutes);
                calSetEnd.set(Calendar.SECOND, 0);
                calSetEnd.set(Calendar.MILLISECOND, 0);

                silenceEnd = calSetEnd.getTimeInMillis();
                startService(new Intent(getApplicationContext(), MyService.class).putExtra("timeEnd", silenceEnd).setAction("silence"));

                getSpinnerItemToSetAlarm();
            }
        }
    }
    public void getSpinnerItemToSetAlarm(){ // ставит сигнал на время выбранное в спиннере
        int strChoose = spinnerInterval.getSelectedItemPosition();
        int time = -1;
        switch (strChoose) {
            case 0:
                time = 15;
                break;
            case 1:
                time = 30;
                break;
            case 2:
                time = 45;
                break;
            case 3:
                time = 60;
                break;
            case 4:
                time = 90;
                break;
            case 5:
                time = 180;
                break;
        }
        if (time != -1) {
            cancelAlarm();
            setAlarm(time);
            //отправляем в броадкаст сообщение с интервалом сигнала
            Intent intent = new Intent("luciddreamcatcher.action.alarmOn");
            intent.putExtra("extraOn", time);
            sendBroadcast(intent);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.ToastChooseTime), Toast.LENGTH_LONG).show();
        }
    }

    public void ClickButtonSound(View view) {
        Intent pickIntent = new Intent();
        pickIntent.setType("audio/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(pickIntent, getString(R.string.ChooseAudioFile)), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode ==1){
                isActivityResult = true;
                Uri pickedUri = data.getData();


                String[] audioData = {MediaStore.Audio.Media.DATA};

                Cursor audioCursor = getApplicationContext().getContentResolver().query(pickedUri, audioData, null, null, null);
                if (audioCursor!=null){
                    int index = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    audioCursor.moveToFirst();
                    audioPath = audioCursor.getString(index);
                    audioCursor.close();
                } else
                    audioPath = pickedUri.getPath();
                if (audioPath!=null) {
                    int temp = audioPath.lastIndexOf("/");
                    file_name = audioPath.substring(temp + 1, audioPath.length()); //разделяем строку чтобы получить имя
                }

                mPath.setText(file_name);//don't work on lollipop +
                startService(new Intent(getApplicationContext(), MyService.class).putExtra("soundPath", audioPath).setAction("setCustomAlarmSound"));
                editor.putString("audio_name", file_name);
                editor.putString("audio_path", audioPath);
                editor.apply();

            }
        }
    }
}
