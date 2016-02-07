package com.mikestudio.luciddreamcatcher;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Михаил on 05.06.2015.
 */
/////////ОСТАВЛЯЮ НА ТРЕТИЙ МЕСЯЦ ОБУЧЕНИЯ АНДРОИД - ПОСЛОВИЦЫ О КОТАХ
public class RealityDataBaseHelper extends SQLiteOpenHelper {

    private final Context fContext;
    //константы для конструктора
    //имя файла для БД
    private static final String DATABASE_NAME = "CheckRealTexts.db";
    //номер версии БД
    private static final int DATABASE_VERSION = 1;
    //имя таблицы
    public static final String TABLE_NAME = "tableRandomText";
    //колонка
    public static final String KEY_ROWID = "_id";
    //вторая колонка
    public static final String KEY_TEXT = "Text";
    //строка для создания таблицы в БД
    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME
            + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TEXT + " VARCHAR(255));";

    //Строка для удаления таблицы в БД
    private static final String DELETE_ENTRIES = "DROP TABLE IF EXISTS "+ TABLE_NAME;

    private static RealityDataBaseHelper dbInstance;

    //Конструктор
    public RealityDataBaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        fContext = context;
    }

    public static RealityDataBaseHelper getInstance(Context context){
        if (dbInstance == null){
            dbInstance = new RealityDataBaseHelper(context.getApplicationContext());
        }
        return dbInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        ContentValues  values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] records = res.getStringArray(R.array.TextCheckReal);
        for (int i = 0; i < records.length; i++) {
            values.put(KEY_TEXT, records[i]);
            db.insert(TABLE_NAME, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Удаляем таблицу при апгрейде
        db.execSQL(DELETE_ENTRIES);
        //Создаем новый экземпляр таблицы
        onCreate(db);
    }
}
