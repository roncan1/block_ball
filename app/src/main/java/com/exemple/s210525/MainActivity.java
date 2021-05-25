package com.exemple.s210525;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    GameGraphic gg;
    int width, height, ball_x, ball_y;
    int man_width, man_height, man_x, man_y;
    String left_right = "left";
    String up_down = "up";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gg = new GameGraphic(this);
        setContentView(gg);
        width = this.getResources().getDisplayMetrics().widthPixels;
        height = this.getResources().getDisplayMetrics().heightPixels;

        ball_x = width/2;
        ball_y = height/2;

        man_x = width/2;
        man_y = height - 200;
        man_height = 75;
        man_width = width/5;

        handler.sendEmptyMessage(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                man_x = (int)event.getX();
                break;
        }
        return super.onTouchEvent(event);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (left_right == "left") {
                ball_x -= 20;
            } else if (left_right == "right") {
                ball_x += 20;
            }
            if (ball_x < 0) {
                left_right = "right";
            } else if (ball_x > width - 100) {
                left_right = "left";
            }

            if (up_down == "up") {
                ball_y -= 20;
            } else if (up_down == "down") {
                ball_y += 20;
            }
            if (ball_y < 0) {
                up_down = "down";
            } else if (ball_y > height - 100) {
                up_down = "up";
            }

            gg.invalidate();
            handler.sendEmptyMessageDelayed(0, 30);
        }
    };
    public class GameGraphic extends View {

        public GameGraphic(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint p = new Paint();
            p.setColor(Color.BLACK);

            canvas.drawColor(Color.GRAY);
            p.setColor(Color.BLUE);
            p.setTextSize(50);

            p.setTextSize(100);
            canvas.drawText(width+"", width/2, 300,p);
            canvas.drawText(height+"", width/2, 500,p);

            p.setColor(Color.BLUE);
            canvas.drawText("●", ball_x,ball_y,p);

            canvas.drawRect(man_x, man_y, man_x + man_width, man_y + man_height,p);
        }
    }


}