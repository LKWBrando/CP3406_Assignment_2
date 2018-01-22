package example.assignmentone.cp3406.cp3406_assignment_2;

/**
 * Created by Brandon on 22/01/2018.
 */
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class InvisibleBubble {
        private int x, y, radius;

        public InvisibleBubble(int xP, int yP, int r){
            x = xP;
            y = yP;
            radius = r;
        }

        public void draw(Canvas c, Paint g){
            g.setColor(Color.CYAN);
            c.drawColor(Color.BLACK);
            c.drawCircle(x, y, radius, g);
        }

        public void setY(int newYValue){
            y = newYValue;
        }

        public void setX(int newXValue){
            x = newXValue;
        }

        public void setR(int newRadius){
            radius = newRadius;
        }

        public void setXY(int newXValue, int newYValue){
            x = newXValue;
            y = newYValue;
        }
}
