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

    GameGraphicView ggv;
    int width, height;
    int man_x, man_y;
    int man_box_w, man_box_h;
    int bt_start_x, bt_start_y;
    int ball_x, ball_y;
    int ball = 100;
    int move = 30;
    String game = "게임중지";
    String left_right = "우방향";
    String up_down = "상방향";
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ggv = new GameGraphicView(getApplicationContext());

        setContentView(ggv);

        width = this.getResources().getDisplayMetrics().widthPixels;
        height = this.getResources().getDisplayMetrics().heightPixels;
        man_x = width/2;
        man_y = height/8*6;
        man_box_w = width/5;
        man_box_h = height/15;
        bt_start_x = width;
        bt_start_y = man_y + man_box_h;
        ball_x = width/2;
        ball_y = height/2;

//        핸들러 스타트
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                if (game == "게임중지") {
                    if (event.getY() > bt_start_y) {
                        game = "게임중";
                        ball_x = width/2;
                        ball_y = height/2;
                        score = 0;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                man_x = (int)event.getX() + man_box_w/2;
        }

        return super.onTouchEvent(event);
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (game == "게임중") {
                if (left_right == "우방향") {
                    ball_x = ball_x + move;
                } else {
                    ball_x = ball_x - move;
                }

                if (ball_x > width)left_right = "좌방향";
                if (ball_x < 0)left_right = "우방향";

                if (up_down == "상방향") {
                    ball_y = ball_y - move;
                } else {
                    ball_y = ball_y + move;
                }

                if (ball_y < 0)up_down = "하방향";
                if (ball_y > man_y) {
                    if (ball_x > man_x) {
                        if (ball_x < man_x + man_box_w) {
                            up_down = "상방향";
                            score = score + 100;
                        } else {
                            game = "게임중지";
                        }
                    }
                }
            }
            ggv.invalidate();
            mHandler.sendEmptyMessageDelayed(0, 50);
        }
    };

    public class GameGraphicView extends View {

        public GameGraphicView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint p = new Paint();
            p.setColor(Color.BLUE);
            canvas.drawRect(man_x, man_y, man_x + man_box_w, man_y + man_box_h, p);

            p.setColor(Color.YELLOW);
            canvas.drawRect(0, bt_start_y, width, height, p);
            p.setColor(Color.BLACK);
            p.setTextAlign(Paint.Align.CENTER);
            if (game == "게임중"){
                canvas.drawText(score + "점", width/2, bt_start_y + height/10, p);
            } else {
                canvas.drawText("여기터치 : 게임시작", width/2, bt_start_y + height/10, p);
            }

            p.setColor(Color.RED);
            canvas.drawCircle(ball_x + ball/2, ball_y + ball/2, ball, p);

        }
    }

}