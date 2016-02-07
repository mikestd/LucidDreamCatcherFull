package com.mikestudio.luciddreamcatcher;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;

/**
 * Вспомогательный класс для подсветки нажатого элемента
 */
public class BacklightHelper {
    public static Bitmap doHighlightImage(Bitmap src) {
        // создадим новый битмап, который станет итоговым
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(),
                src.getHeight() , Bitmap.Config.ARGB_8888);
        // подключаем холст
        Canvas canvas = new Canvas(bmOut);
        // установим цвет по умолчанию
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        // создадим размытие для прозрачности
        Paint ptBlur = new Paint();
        ptBlur.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
        int[] offsetXY = new int[2];
        // получим прозрачный слепок из изображения
        Bitmap bmAlpha = src.extractAlpha(ptBlur, offsetXY);
        // готовимся к рисованию
        Paint ptAlphaColor = new Paint();
        ptAlphaColor.setColor(Color.WHITE);
        // закрашиваем цветом цветной слепок (bitmap)
        canvas.drawBitmap(bmAlpha, offsetXY[0], offsetXY[1], ptAlphaColor);
        // освобождаем ресурсы
        bmAlpha.recycle();

        // рисуем исходник
        canvas.drawBitmap(src, 0, 0, null);

        // возвращаем финальный рисунок
        return bmOut;
    }
}
