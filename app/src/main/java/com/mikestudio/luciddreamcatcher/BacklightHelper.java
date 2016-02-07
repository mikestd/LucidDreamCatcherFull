package com.mikestudio.luciddreamcatcher;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;

/**
 * ��������������� ����� ��� ��������� �������� ��������
 */
public class BacklightHelper {
    public static Bitmap doHighlightImage(Bitmap src) {
        // �������� ����� ������, ������� ������ ��������
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(),
                src.getHeight() , Bitmap.Config.ARGB_8888);
        // ���������� �����
        Canvas canvas = new Canvas(bmOut);
        // ��������� ���� �� ���������
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        // �������� �������� ��� ������������
        Paint ptBlur = new Paint();
        ptBlur.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
        int[] offsetXY = new int[2];
        // ������� ���������� ������ �� �����������
        Bitmap bmAlpha = src.extractAlpha(ptBlur, offsetXY);
        // ��������� � ���������
        Paint ptAlphaColor = new Paint();
        ptAlphaColor.setColor(Color.WHITE);
        // ����������� ������ ������� ������ (bitmap)
        canvas.drawBitmap(bmAlpha, offsetXY[0], offsetXY[1], ptAlphaColor);
        // ����������� �������
        bmAlpha.recycle();

        // ������ ��������
        canvas.drawBitmap(src, 0, 0, null);

        // ���������� ��������� �������
        return bmOut;
    }
}
