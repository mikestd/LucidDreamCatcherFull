package com.mikestudio.luciddreamcatcher;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class CheckRealActivity extends ActionBarActivity implements View.OnTouchListener{
    private RealityDataBaseHelper sqh;
    private SQLiteDatabase db;
    ImageView pero;
    SharedPreferences saveCountFeather;
    final static String COUNT = "COUNT";
    static int countFeather = 0;
    TextView counter, headerText, SQLiteText, bottomText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_real);
       // getApplicationContext().stopService((new Intent(getApplicationContext(), MyService.class)));
        startService(new Intent(getApplicationContext(), MyService.class).setAction("TapFromActivity/Widget"));
        pero = (ImageView)findViewById(R.id.imageView2);
        pero.setOnTouchListener(this);

        counter = (TextView)findViewById(R.id.mandala_count_pero_real);
        SQLiteText = (TextView)findViewById(R.id.randomText);
        headerText = (TextView)findViewById(R.id.real_text);
        bottomText = (TextView)findViewById(R.id.then_tap_text);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        counter.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Myriad Pro Bold Condensed Italic.ttf"));
        headerText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Myriad Pro Bold Condensed Italic.ttf"));
        SQLiteText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Myriad Pro Bold Condensed Italic.ttf"));
        bottomText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Myriad Pro Bold Condensed Italic.ttf"));

        saveCountFeather = getSharedPreferences(COUNT, Context.MODE_PRIVATE);
        if (saveCountFeather.contains("countFeather")){
            countFeather = saveCountFeather.getInt("countFeather", 0);
            counter.setText(String.valueOf(countFeather));// счетчик + 1
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sqh = new RealityDataBaseHelper(this);
        db = sqh.getReadableDatabase();
        try {
            String randomText = "";
            randomText = getRandomEntry();
            SQLiteText.setText(randomText);
        } catch (Exception ex){
            Toast.makeText(getApplicationContext(),R.string.Error, Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check_real, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.create_custom_reality) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.custom_reality);

// Добавим поле ввода
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton(R.string.POSITIVE_BUTTON, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    // Получили значение введенных данных!
                    insertProverb(value);
                }
            });

            alert.setNegativeButton(R.string.NEGATIVE_BUTTON, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Если отменили.
                }
            });

            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    // вставляем новую запись в базу данных
    public long insertProverb(String proverb) {
        sqh = new RealityDataBaseHelper(this);
        db = sqh.getReadableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(RealityDataBaseHelper.KEY_TEXT, proverb);
        return db.insert(RealityDataBaseHelper.TABLE_NAME, null, initialValues);
    }

    @Override
    protected void onDestroy() {
        sqh.close();
        super.onDestroy();
    }
    public int getAllEntries(){
        String query = "SELECT COUNT(*) FROM tableRandomText";
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;

        while (cursor.moveToNext()){
            count = cursor.getInt(0);
            return count;
        }
        cursor.close();
        return count;
    }
    public String getRandomEntry(){
        //подключаем генератор случайных чисел
        Random random = new Random();
        //задаем диапазон
        int rand = random.nextInt(getAllEntries() + 1);
        if (rand == 0) rand++;
        Cursor cursor = db.rawQuery("SELECT Text FROM " + RealityDataBaseHelper.TABLE_NAME + " WHERE _id = " + rand, null);
        if (cursor.moveToFirst()){
            return cursor.getString(0);
        }
        cursor.close();
        return cursor.getString(0);


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) { //делаем подсветку
        if (event.getAction() == MotionEvent.ACTION_DOWN) {//подсветка при нажатии

                BitmapDrawable mydrawable = (BitmapDrawable) pero.getDrawable();
                Bitmap b = mydrawable.getBitmap();
                b = BacklightHelper.doHighlightImage(b);//подсветка картинки при нажатии
                pero.setImageBitmap(b);
            }
            if (event.getAction()==MotionEvent.ACTION_UP) { //сбрасываем подсветку при отпускании
                pero.setImageResource(R.mipmap.pero_horizontal);
            }
        return false;
    }

    public void onClick(View view) {
        Random randomize = new Random();
        int i = randomize.nextInt(3);
        AppWidget.peroDraw(getApplicationContext(),i);
        countFeather++;
        SharedPreferences.Editor editor = saveCountFeather.edit();
        editor.putInt("countFeather", countFeather);
        editor.apply();
        counter.setText(String.valueOf(countFeather));// счетчик + 1
        finishAffinity();
    }


}
