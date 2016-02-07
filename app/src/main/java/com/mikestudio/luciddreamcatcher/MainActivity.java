package com.mikestudio.luciddreamcatcher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnLongClickListener,
        View.OnTouchListener {

    ImageView target1, target2, target3, target4, pero;
    String description1, description2, description3, description4;
    int mTapImg;
    int tempImg1;//хранение ответа от активити-->реусурс картинки
    int tempImg2;
    int tempImg3;
    int tempImg4;
    TextView toast_text, counter, countPero, Today, Targets, textDate;
    int count =0;//счетчик на мандале
    int tempCount=0;
    Toolbar toolbar;
    String colorBall;//цвет нажатого шарика
    boolean isDraw = false; // нужно ли рисовать шарик на виджете
    static String SETTING_MAIN_ACTIVITY = "SETTING_MAIN_ACTIVITY";
    SharedPreferences mainSettings, saveCountFeather;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        target1 = (ImageView) findViewById(R.id.target1);
        target2 = (ImageView) findViewById(R.id.target2);
        target3 = (ImageView) findViewById(R.id.target3);
        target4 = (ImageView) findViewById(R.id.target4);
        pero = (ImageView)findViewById(R.id.peroCheck);
        counter =  (TextView)findViewById(R.id.mandala_count);
        countPero = (TextView)findViewById(R.id.mandala_count_pero);
        Today = (TextView)findViewById(R.id.today);
        Targets = (TextView)findViewById(R.id.targets);
        textDate = (TextView)findViewById(R.id.textDate);


        counter.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Myriad Pro Bold Condensed Italic.ttf"));
        countPero.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Myriad Pro Bold Condensed Italic.ttf"));
        Today.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Myriad Pro Bold Condensed Italic.ttf"));
        Targets.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Myriad Pro Bold Condensed Italic.ttf"));
        textDate.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Myriad Pro Bold Condensed Italic.ttf"));


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        target1.setOnClickListener(this);
        target2.setOnClickListener(this);
        target3.setOnClickListener(this);
        target4.setOnClickListener(this);
        target1.setOnLongClickListener(this);
        target2.setOnLongClickListener(this);
        target3.setOnLongClickListener(this);
        target4.setOnLongClickListener(this);
        target1.setOnTouchListener(this);
        target2.setOnTouchListener(this);
        target3.setOnTouchListener(this);
        target4.setOnTouchListener(this);
        pero.setOnTouchListener(this);

        mainSettings = getSharedPreferences(SETTING_MAIN_ACTIVITY, Context.MODE_PRIVATE);
        editor = mainSettings.edit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
        }
        if (id == R.id.action_schedule){
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        mTapImg = v.getId();
        if (((ImageView) v).getDrawable() == null) {//проверяем есть ли картинка, если нет - открываем список, да - счетчик.
            if ((mTapImg == R.id.target1) || (mTapImg == R.id.target2) || (mTapImg == R.id.target3) || (mTapImg == R.id.target4)) {
                Intent intent = new Intent(this, TargetListActivity.class);
                startActivityForResult(intent, 0);
            }
        } else {
            if (tempCount==0) {
                AnimatorHelper.AnimRotate_plus(counter);
                count++;
                counter.setText(String.valueOf(count));
                tempCount = 1;
                isDraw = true;
                ////////////////////////////////
                switch (mTapImg) {
                    case R.id.target1:
                        colorBall = "redball";
                        target2.setEnabled(false);
                        target3.setEnabled(false);
                        target4.setEnabled(false);
                        break;
                    case R.id.target2:
                        colorBall = "greenball";
                        target1.setEnabled(false);
                        target3.setEnabled(false);
                        target4.setEnabled(false);
                        break;
                    case R.id.target3:
                        colorBall = "yellowball";
                        target2.setEnabled(false);
                        target1.setEnabled(false);
                        target4.setEnabled(false);
                        break;
                    case R.id.target4:
                        colorBall = "blueball";
                        target2.setEnabled(false);
                        target3.setEnabled(false);
                        target1.setEnabled(false);
                        break;
                }
                /////////////////////////////////////
            } else {
                AnimatorHelper.AnimRotate_minus(counter);
                count--;
                counter.setText(String.valueOf(count));
                tempCount = 0;
                isDraw = false;
                ////////////////////////////////
                switch (mTapImg) {
                    case R.id.target1:
                        target2.setEnabled(true);
                        target3.setEnabled(true);
                        target4.setEnabled(true);
                        break;
                    case R.id.target2:
                        target1.setEnabled(true);
                        target3.setEnabled(true);
                        target4.setEnabled(true);
                        break;
                    case R.id.target3:
                        target2.setEnabled(true);
                        target1.setEnabled(true);
                        target4.setEnabled(true);
                        break;
                    case R.id.target4:
                        target2.setEnabled(true);
                        target3.setEnabled(true);
                        target1.setEnabled(true);
                        break;
                }
                /////////////////////////////////////
            }

            editor.putInt("mandala_count", count);
            editor.apply();

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String tempStr = data.getStringExtra(TargetListActivity.STRING);
                int tempImg = data.getIntExtra(TargetListActivity.IMAGE, -1);
                switch (mTapImg) {
                    case R.id.target1:
                        tempImg1=tempImg;
                       // tempStr1=tempStr;
                        target1.setImageResource(tempImg);
                        //description1 = getString(tempStr);
                        description1 = tempStr;

                        editor.putInt("img1",tempImg1);
                        editor.putString("str1", description1);
                        editor.apply();
                        break;
                    case R.id.target2:
                        tempImg2=tempImg;
                       // tempStr2=tempStr;
                        target2.setImageResource(tempImg);
                        //description2 = getString(tempStr);
                        description2 = tempStr;

                        editor.putInt("img2",tempImg2);
                        editor.putString("str2", description2);
                        editor.apply();
                        break;
                    case R.id.target3:
                        tempImg3=tempImg;
                       // tempStr3=tempStr;
                        target3.setImageResource(tempImg);
                       // description3 = getString(tempStr);
                        description3 = tempStr;

                        editor.putInt("img3",tempImg3);
                        editor.putString("str3", description3);
                        editor.apply();
                        break;
                    case R.id.target4:
                        tempImg4=tempImg;
                       // tempStr4=tempStr;
                        target4.setImageResource(tempImg);
                        //description4 = getString(tempStr);
                        description4 = tempStr;

                        editor.putInt("img4",tempImg4);
                        editor.putString("str4", description4);
                        editor.apply();
                        break;
                }
            }
        }
    }

    @Override
    public boolean onLongClick(View v){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        toast_text = (TextView) layout.findViewById(R.id.text);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        mTapImg = v.getId();
        switch (mTapImg) {
            case R.id.target1: if (description1!=null) {
                toast_text.setText(description1);
                toast_text.setTextColor(Color.RED);
                toast.show();
            }
                break;
            case R.id.target2: if (description2!=null) {
                toast_text.setText(description2);
                toast_text.setTextColor(getResources().getColor(R.color.custom_green));
                toast.show();
            }
                break;
            case R.id.target3: if (description3!=null) {
                toast_text.setText(description3);
                toast_text.setTextColor(Color.YELLOW);
                toast.show();
            }
                break;
            case R.id.target4: if (description4!=null) {
                toast_text.setText(description4);
                toast_text.setTextColor(Color.BLUE);
                toast.show();
            }
                break;
        }

        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) { //делаем подсветку
        mTapImg=v.getId();
        if (event.getAction()==MotionEvent.ACTION_DOWN) {//подсветка при нажатии
            switch (mTapImg) {
                case R.id.target1:
                    if (target1.getDrawable() != null) {
                        BitmapDrawable mydrawable = (BitmapDrawable) target1.getDrawable();
                        Bitmap b = mydrawable.getBitmap();
                        b = BacklightHelper.doHighlightImage(b);//подсветка картинки при нажатии
                        target1.setImageBitmap(b);
                    }
                    break;
                case R.id.target2:
                    if (target2.getDrawable() != null) {
                        BitmapDrawable mydrawable = (BitmapDrawable) target2.getDrawable();
                        Bitmap b = mydrawable.getBitmap();
                        b = BacklightHelper.doHighlightImage(b);//подсветка картинки при нажатии
                        target2.setImageBitmap(b);
                    }
                    break;
                case R.id.target3:
                    if (target3.getDrawable() != null) {
                        BitmapDrawable mydrawable = (BitmapDrawable) target3.getDrawable();
                        Bitmap b = mydrawable.getBitmap();
                        b = BacklightHelper.doHighlightImage(b);//подсветка картинки при нажатии
                        target3.setImageBitmap(b);
                    }
                    break;
                case R.id.target4:
                    if (target4.getDrawable() != null) {
                        BitmapDrawable mydrawable = (BitmapDrawable) target4.getDrawable();
                        Bitmap b = mydrawable.getBitmap();
                        b = BacklightHelper.doHighlightImage(b);//подсветка картинки при нажатии
                        target4.setImageBitmap(b);
                    }
                    break;
                case R.id.peroCheck: BitmapDrawable mydrawable = (BitmapDrawable) pero.getDrawable();
                    Bitmap b = mydrawable.getBitmap();
                    b = BacklightHelper.doHighlightImage(b);//подсветка картинки при нажатии
                    pero.setImageBitmap(b);
                    break;
            }
        } if (event.getAction()==MotionEvent.ACTION_UP) { //сбрасываем подсветку при отпускании
            switch (mTapImg) {
                case R.id.target1: if (target1.getDrawable()!=null) {
                    target1.setImageResource(tempImg1);
                    //description1 = getString(tempStr1);
                }
                    break;
                case R.id.target2: if (target2.getDrawable()!=null) {
                    target2.setImageResource(tempImg2);
                    //description2 = getString(tempStr2);
                }
                    break;
                case R.id.target3: if (target3.getDrawable()!=null) {
                    target3.setImageResource(tempImg3);
                    //description3 = getString(tempStr3);
                }
                    break;
                case R.id.target4: if (target4.getDrawable()!=null) {
                    target4.setImageResource(tempImg4);
                    //description4 = getString(tempStr4);
                }
                    break;
                case R.id.peroCheck: pero.setImageResource(R.mipmap.pero);
                    break;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        target1.setEnabled(true);
        target2.setEnabled(true);
        target3.setEnabled(true);
        target4.setEnabled(true);
        mainSettings =  getSharedPreferences(SETTING_MAIN_ACTIVITY, Context.MODE_PRIVATE);
        boolean firstRun = mainSettings.getBoolean("firstRun", true);
        if (firstRun) {
            mainSettings.edit().putBoolean("firstRun", false).apply();
            editor.remove("img1").apply();
            editor.remove("img2").apply();
            editor.remove("img3").apply();
            editor.remove("img4").apply();
            editor.remove("str1").apply();
            editor.remove("str2").apply();
            editor.remove("str3").apply();
            editor.remove("str4").apply();
        }
       /* target1.setOnClickListener(this);
        target2.setOnClickListener(this);
        target3.setOnClickListener(this);
        target4.setOnClickListener(this);*/
        tempCount=0;
        saveCountFeather = getSharedPreferences(CheckRealActivity.COUNT, Context.MODE_PRIVATE);
        if (saveCountFeather.contains("countFeather")) {
            countPero.setText(String.valueOf(saveCountFeather.getInt("countFeather",0)));
        }  else {
            CheckRealActivity.countFeather = 0;
            countPero.setText(String.valueOf(CheckRealActivity.countFeather));
        }

        if (mainSettings.contains("mandala_count")){
            count = mainSettings.getInt("mandala_count", 0);
            counter.setText(String.valueOf(count));
        } else {
            count = 0;
            counter.setText(String.valueOf(count));
        }
        if (mainSettings.contains("img1")){
            tempImg1 = mainSettings.getInt("img1", 0);
            target1.setImageResource(tempImg1);
            description1 = mainSettings.getString("str1", "");
        } else {
            target1.setImageDrawable(null);
            description1 = null;
        }
        if (mainSettings.contains("img2")){
            tempImg2 = mainSettings.getInt("img2", 0);
            target2.setImageResource(tempImg2);
            description2 = mainSettings.getString("str2", "");
        } else {
            target2.setImageDrawable(null);
            description2 = null;
        }
        if (mainSettings.contains("img3")){
            tempImg3 = mainSettings.getInt("img3", 0);
            target3.setImageResource(tempImg3);
            description3 = mainSettings.getString("str3", "");
        } else {
            target3.setImageDrawable(null);
            description3 = null;
        }
        if (mainSettings.contains("img4")){
            tempImg4 = mainSettings.getInt("img4", 0);
            target4.setImageResource(tempImg4);
            description4 = mainSettings.getString("str4", "");
        } else {
            target4.setImageDrawable(null);
            description4 = null;
        }


        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String dateString = formatter.format(new Date(System.currentTimeMillis()));
        textDate.setText(dateString);

    }

    @Override
    protected void onPause() {//рисуем бусину на виджете при сворачивании активити
        super.onPause();
        if (isDraw){
            AppWidget.drawBall(getApplicationContext(), colorBall);
            isDraw = false;
        }


    }

    public void pero_Click(View view) {
        Intent intent = new Intent(this, CheckRealActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }





}
