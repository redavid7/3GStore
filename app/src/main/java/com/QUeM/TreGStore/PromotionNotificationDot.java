package com.QUeM.TreGStore;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class PromotionNotificationDot extends View {

    private Paint paint;

    public PromotionNotificationDot(Context context) {
        super(context);

        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.GRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawCircle(20, 20, 10, paint);
    }

}
