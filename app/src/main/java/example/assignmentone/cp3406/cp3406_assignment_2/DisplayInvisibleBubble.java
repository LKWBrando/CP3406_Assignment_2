package example.assignmentone.cp3406.cp3406_assignment_2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DisplayInvisibleBubble extends View {

    int currentHeight, currentWidth, bubbleRadius;

    float cX=0.0f;
    InvisibleBubble bubble;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;

    public DisplayInvisibleBubble(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        bubble = new InvisibleBubble(0,0,0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        currentHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        currentWidth = View.MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(currentWidth, currentHeight);

        bitmap = Bitmap.createBitmap(currentWidth, currentHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        cX = currentWidth /2;

        bubbleRadius = (int)(currentWidth /20);
        bubble.setR((int) bubbleRadius);
        bubble.setX((int)cX);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setBackgroundColor(Color.BLACK);

        bubble.draw(this.canvas, paint);

        canvas.drawBitmap(bitmap, 0, 0, paint);
        invalidate();
    }

    public InvisibleBubble getBubble(){
        return bubble;
    }
}
