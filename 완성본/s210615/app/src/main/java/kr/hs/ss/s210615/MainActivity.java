package kr.hs.ss.s210615;

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
    float man_x, man_y, man_box_width, man_box_height;

    float ball_x, ball_y;
    int ball = 30;
    int move = 30;

    String game = "게임중지";
    String left_right = "좌방향", up_down = "상방향";

    int[][] brick, brick_x, brick_y;
    int brick_width, brick_height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ggv = new GameGraphicView(getApplicationContext());
        setContentView(ggv);
//        생성 및 초기값 지정
        width = this.getResources().getDisplayMetrics().widthPixels;
        height = this.getResources().getDisplayMetrics().heightPixels;
        man_y = height/10*8;
        man_box_width = width/5;
        man_box_height = height/20;
        ball_x = width/2;
        ball_y = height/2;

        brick = new int[2][10];
        brick_x = new int[2][10];
        brick_y = new int[2][10];
        brick_width = width/10;
        brick_height = height/30;
        brick[0][0] = 1;
        brick[0][1] = 1;
        brick_x[0][0] = 0;
        brick_x[0][1] = brick_width*1;

        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 10; x++) {
                brick[y][x] = 1;
                brick_x[y][x] = brick_width*x;
                brick_y[y][x] = brick_height*y;
            }
        }
        mHandler.sendEmptyMessage(0);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (game == "게임중") {

                if (left_right == "좌방향") {
                    ball_x = ball_x - move;
                } else {
                    ball_x = ball_x + move;
                }

                if (up_down == "상방향") {
                    ball_y = ball_y - move;
                } else {
                    ball_y = ball_y + move;
                }

                if (ball_x < 0)left_right = "우방향";
                if (ball_x > width)left_right = "좌방향";
                for (int y = 0; y < 2; y++) {
                    for (int x = 0; x < 10; x++) {

                        if (brick[y][x] == 1) {

                            if (ball_x > brick_x[y][x] &&
                            ball_x < brick_x[y][x] + brick_width) {

                                if (ball_y < brick_y[y][x] + brick_height) {
                                    brick[y][x] = 0;
                                    up_down = "하방향";
                                }
                            }

                        }

                    }
                }
                if (ball_y < 0)up_down = "하방향";
                if (ball_y > height/10*8) {
                    if (ball_x > man_x && ball_x < man_x + man_box_width) {
                        up_down = "상방향";
                    } else {
                        game = "게임중지";
                    }
                }

            }

            ggv.invalidate();
            mHandler.sendEmptyMessageDelayed(0, 50);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                man_x = event.getX()-man_box_width/2;
                break;
            case MotionEvent.ACTION_DOWN:
                if (game == "게임중지") {
                    game = "게임중";
                    ball_x = width/2;
                    ball_y = height/2;
                    for (int y = 0; y < 2; y++) {
                        for (int x = 0; x < 10; x++) {
                            brick[y][x] = 1;
                        }
                    }
                }
        }
        ggv.invalidate();
        return super.onTouchEvent(event);
    }

    private class GameGraphicView extends View {

        public GameGraphicView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // 그림 그리는곳
            Paint p = new Paint();
            p.setColor(Color.RED);
            canvas.drawRect(man_x, man_y,
                    man_x + man_box_width,
                    man_y + man_box_height, p);
            p.setColor(Color.BLACK);
            p.setTextSize(150);
            p.setTextAlign(Paint.Align.CENTER);
            if (game == "게임중지") {
                canvas.drawText("터치하면 게임시작", width/2, height/2,p);
            } else {
                canvas.drawText("게임중", width/2, height-200,p);
            }
            canvas.drawCircle(ball_x-ball/2, ball_y-ball/2, ball,p);
            for (int y = 0; y < 2; y++) {
                for (int x = 0; x < 10; x++) {
                    if (brick[y][x] == 1) {
                        p.setColor(Color.BLUE);
                        canvas.drawRect(brick_x[y][x], brick_y[y][x],
                                brick_x[y][x] + brick_width-2,
                                brick_y[y][x] + brick_height-2, p);
                    }
                }
            }    

        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.getLooper().quit();
    }
}