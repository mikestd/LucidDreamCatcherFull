package com.mikestudio.luciddreamcatcher;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChoosePicDescriptionActivity extends AppCompatActivity implements View.OnClickListener {
    final static String TEXT ="com.mikestudio.luciddreamcatcher.TEXT";
    final static String SRC = "com.mikestudio.luciddreamcatcher.SRC";
    int temp_count = 0;
    ImageView img_temp;
    String img_tag;

    Intent answerIntent;

    ImageView bird, bed, bike, brain, cash, cat, sun, clouds, dig, door, drops, fire, flower, goman,
                home, icecream, kettle, keys, lamp, leafs, lightning, lights, loudspeaker, penguin,
                phonecell, phonewire, plain, purse, pyramid, rocket, snow, snowman, spider, stair, stop,
                stopman, clock, teacher, trash, tv, vegetables, wheel, window;
    TextView textCancel, textOk;
    EditText edit_Description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pic_description);
        bird = (ImageView)findViewById(R.id.img_bird); bird.setOnClickListener(this);
        bed = (ImageView)findViewById(R.id.img_bed); bed.setOnClickListener(this);
        bike = (ImageView)findViewById(R.id.img_bike); bike.setOnClickListener(this);
        brain = (ImageView)findViewById(R.id.img_brain); brain.setOnClickListener(this);
        cash = (ImageView)findViewById(R.id.img_cash); cash.setOnClickListener(this);
        cat = (ImageView)findViewById(R.id.img_cat); cat.setOnClickListener(this);
        sun = (ImageView)findViewById(R.id.img_sun); sun.setOnClickListener(this);
        clouds = (ImageView)findViewById(R.id.img_clouds); clouds.setOnClickListener(this);
        dig = (ImageView)findViewById(R.id.img_dig); dig.setOnClickListener(this);
        door = (ImageView)findViewById(R.id.img_door); door.setOnClickListener(this);
        drops = (ImageView)findViewById(R.id.img_drops); drops.setOnClickListener(this);
        fire = (ImageView)findViewById(R.id.img_fire); fire.setOnClickListener(this);
        flower = (ImageView)findViewById(R.id.img_flower); flower.setOnClickListener(this);
        goman = (ImageView)findViewById(R.id.img_goman); goman.setOnClickListener(this);
        home = (ImageView)findViewById(R.id.img_home); home.setOnClickListener(this);
        icecream = (ImageView)findViewById(R.id.img_icecream); icecream.setOnClickListener(this);
        kettle = (ImageView)findViewById(R.id.img_kettle); kettle.setOnClickListener(this);
        keys = (ImageView)findViewById(R.id.img_keys); keys.setOnClickListener(this);
        lamp = (ImageView)findViewById(R.id.img_lamp); lamp.setOnClickListener(this);
        leafs = (ImageView)findViewById(R.id.img_leafs); leafs.setOnClickListener(this);
        lightning = (ImageView)findViewById(R.id.img_lightning); lightning.setOnClickListener(this);
        lights = (ImageView)findViewById(R.id.img_lights); lights.setOnClickListener(this);
        loudspeaker = (ImageView)findViewById(R.id.img_loudspeaker); loudspeaker.setOnClickListener(this);
        penguin = (ImageView)findViewById(R.id.img_penguin); penguin.setOnClickListener(this);
        phonecell = (ImageView)findViewById(R.id.img_phonecell); phonecell.setOnClickListener(this);
        phonewire = (ImageView)findViewById(R.id.img_phonewire); phonewire.setOnClickListener(this);
        plain = (ImageView)findViewById(R.id.img_plain); plain.setOnClickListener(this);
        purse = (ImageView)findViewById(R.id.img_purse); purse.setOnClickListener(this);
        pyramid = (ImageView)findViewById(R.id.img_pyramid); pyramid.setOnClickListener(this);
        rocket = (ImageView)findViewById(R.id.img_rocket); rocket.setOnClickListener(this);
        snow = (ImageView)findViewById(R.id.img_snow); snow.setOnClickListener(this);
        snowman = (ImageView)findViewById(R.id.img_snowman); snowman.setOnClickListener(this);
        spider = (ImageView)findViewById(R.id.img_spider); spider.setOnClickListener(this);
        stair = (ImageView)findViewById(R.id.img_stair); stair.setOnClickListener(this);
        stop = (ImageView)findViewById(R.id.img_stop); stop.setOnClickListener(this);
        stopman = (ImageView)findViewById(R.id.img_stopman); stopman.setOnClickListener(this);
        clock = (ImageView)findViewById(R.id.img_clock); clock.setOnClickListener(this);
        teacher = (ImageView)findViewById(R.id.img_teacher); teacher.setOnClickListener(this);
        trash = (ImageView)findViewById(R.id.img_trash); trash.setOnClickListener(this);
        tv = (ImageView)findViewById(R.id.img_tv); tv.setOnClickListener(this);
        vegetables = (ImageView)findViewById(R.id.img_vegetables); vegetables.setOnClickListener(this);
        wheel = (ImageView)findViewById(R.id.img_wheel); wheel.setOnClickListener(this);
        window = (ImageView)findViewById(R.id.img_window); window.setOnClickListener(this);
        textCancel = (TextView)findViewById(R.id.text_cancel); textCancel.setOnClickListener(this);
        textOk = (TextView)findViewById(R.id.text_yes); textOk.setOnClickListener(this);
        edit_Description = (EditText)findViewById(R.id.editText);

        answerIntent = new Intent();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.text_yes && v.getId() != R.id.text_cancel) {
            if (temp_count == 1){
                img_temp.setBackgroundColor(Color.TRANSPARENT);// снимаем выделение
                temp_count = 0;
            }
            img_tag = v.getTag().toString();//получаем тег imgView
            img_temp = (ImageView)v; // запоминаем imageView
            answerIntent.putExtra(SRC, img_tag);
            v.setBackgroundColor(getResources().getColor(R.color.pressed_item));
            temp_count++;
        }
        switch (v.getId()){
            case R.id.text_cancel :
                setResult(RESULT_CANCELED, answerIntent);
                finish();
                break;
            case R.id.text_yes :
                if (edit_Description.getText().toString().length() <= 50) {
                    if (img_tag != null) {
                        answerIntent.putExtra(TEXT, edit_Description.getText().toString());
                        setResult(RESULT_OK, answerIntent);
                        finish();
                    } else Toast.makeText(getApplicationContext(), getString(R.string.picture), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.length), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
