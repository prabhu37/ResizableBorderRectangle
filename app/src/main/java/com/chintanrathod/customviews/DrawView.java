/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.chintanrathod.customviews;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chintanrathod.resizablerectangle.R;

import org.w3c.dom.Text;

public class DrawView extends RelativeLayout implements View.OnTouchListener {

    Point point1, point3;
    Point point2, point4;
    int pointRight, pointLeft, pointTop, pointBottom;
    Point startMovePoint;
    TextView imageView, textView2, commonTextView;
    int xValue, yValue, width, height;
    boolean resizeEnabled = true;
    int touchRight, touchLeft, touchTop, touchBottom;
    /**
     * point1 and point 3 are of same group and same as point 2 and point4
     */
    int groupId = 2;
    private ArrayList<ColorBall> colorballs;
    // array that holds the balls
    private int balID = 0;
    // variable to know what ball is being dragged
    Paint paint;
    Canvas canvas;

    public DrawView(Context context) {
        super(context);
        init(context);
    }


    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void init(Context context) {
        paint = new Paint();
        imageView = new TextView(context);
        textView2 = new TextView(context);
        textView2.setGravity(Gravity.CENTER);
        textView2.setTextSize(15);
        textView2.setText("WidgetView 2");
        addView(textView2);
        addView(imageView);
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        // setting the start point for the balls
        point1 = new Point();
        point2 = new Point();
        point3 = new Point();
        point4 = new Point();
        setDefaultPointValues();
        int width = point3.x - point1.x;
        int height = point3.y - point1.y;
        textView2.setBackgroundColor(Color.parseColor("#40C00F"));
        RelativeLayout.LayoutParams layoutParams1= new RelativeLayout.LayoutParams(width, height);
        textView2.setLayoutParams(layoutParams1);
        textView2.setX(100);
        textView2.setY(100);
        imageView.setText("WidgetView");
        imageView.setTextSize(15);
        imageView.setGravity(Gravity.CENTER);
        imageView.setBackgroundColor(Color.parseColor("#40C4FF"));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        imageView.setLayoutParams(layoutParams);
        imageView.setX(point1.x);
        imageView.setY(point1.y);
        commonTextView = imageView;



        invalidate();

        textView2.setOnTouchListener(this);
        imageView.setOnTouchListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resizeEnabled  =true;
                invalidate();

            }
        },10000);

       /* textView2.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {


                Log.e("TouchEvent : " ,"X  :"+X+"   Y :"+Y);
                return false;
            }
        });
        imageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                commonTextView = imageView;
                int X = (int) view.getX();
                int Y = (int) view.getY();
                point1.x = X;
                point2.x = X;
                point4.x = X+view.getWidth();
                point3.x = X+view.getWidth();
                point1.y = Y;
                point2.y =Y+view.getHeight();
                point3.y = Y+view.getHeight();
                point4.y = Y;

                Log.e("TouchEvent : " ,"X  :"+X+"   Y :"+Y);
                return false;
            }
        });*/
        // declare each ball with the ColorBall class
        colorballs = new ArrayList<ColorBall>();
        colorballs.add(0, new ColorBall(context, R.drawable.ic_widget_resize_handle, point1, 0));
        colorballs.add(1, new ColorBall(context, R.drawable.ic_widget_resize_handle, point2, 1));
        colorballs.add(2, new ColorBall(context, R.drawable.ic_widget_resize_handle, point3, 2));
        colorballs.add(3, new ColorBall(context, R.drawable.ic_widget_resize_handle, point4, 3));

    }


    private void setDefaultPointValues() {
        point1.x = 200;
        point1.y = 200;


        point2.x = 200;
        point2.y = 350;


        point3.x = 350;
        point3.y = 350;


        point4.x = 350;
        point4.y = 200;
    }

    // the method that draws the balls
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onDraw(Canvas canvas) {
        // canvas.drawColor(0xFFCCCCCC); //if you want another background color
        if(resizeEnabled) {
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(Color.parseColor("#55000000"));
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeJoin(Paint.Join.ROUND);
            // mPaint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(5);

            canvas.drawPaint(paint);
            paint.setColor(Color.parseColor("#55FFFFFF"));
            Log.e("PointsX :", (point1.x) + "  " + point4.x);
            Log.e("PointsY :", (point1.y) + "  " + point3.y);
            if (((point2.x + 100) < point4.x) && ((point1.x + 100) < point3.x) && ((point4.y + 100) < point2.y) && ((point1.y + 100) < point3.y)) {
                if (groupId == 1) {
                    pointLeft = point1.x + colorballs.get(0).getWidthOfBall() / 2;
                    pointTop = point3.y + colorballs.get(2).getWidthOfBall() / 2;
                    pointRight = point3.x + colorballs.get(2).getWidthOfBall() / 2;
                    pointBottom = point1.y + colorballs.get(0).getWidthOfBall() / 2;
                    canvas.drawRect(pointLeft, pointTop, pointRight, pointBottom, paint);

                    width = (point3.x - point1.x) - colorballs.get(0).getWidthOfBall();
                    height = (point3.y - point1.y) - colorballs.get(0).getWidthOfBall();
                    Log.e("Values :", "Width : " + width + "  Height : " + height);
                    xValue = point1.x + (colorballs.get(0).getWidthOfBall());
                    yValue = point1.y + (colorballs.get(0).getWidthOfBall());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
                    commonTextView.setLayoutParams(layoutParams);
                    commonTextView.setX(xValue);
                    commonTextView.setY(yValue);

                    Log.e("DrawPoint1 :", "P1 :" + point1.x + " " + point3.x + " P2 :" + point1.y + " " + point3.y);

                } else {
                    pointLeft = point2.x + colorballs.get(1).getWidthOfBall() / 2;
                    pointTop = point4.y + colorballs.get(3).getWidthOfBall() / 2;
                    pointRight = point4.x + colorballs.get(3).getWidthOfBall() / 2;
                    pointBottom = point2.y + colorballs.get(1).getWidthOfBall() / 2;
                    canvas.drawRect(pointLeft, pointTop, pointRight, pointBottom, paint);

                    width = (point4.x - point2.x) - colorballs.get(1).getWidthOfBall();
                    height = (point2.y - point4.y) - colorballs.get(1).getWidthOfBall();
                    xValue = point2.x + colorballs.get(1).getWidthOfBall();
                    yValue = (point2.y - height);
                    Log.e("Values :", "Width : " + width + "  Height : " + height);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
                    commonTextView.setLayoutParams(layoutParams);
                    commonTextView.setX(xValue);
                    commonTextView.setY(yValue);
                    Log.e("DrawPoint2 :", "P1 :" + point2.x + " " + point4.x + " P2 :" + point4.y + " " + point2.y);

                }


                BitmapDrawable mBitmap;
                mBitmap = new BitmapDrawable();

                // draw the balls on the canvas
                for (ColorBall ball : colorballs) {
                    canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                            new Paint());
                }
            } else {
                canvas.drawRect(pointLeft, pointTop, pointRight, pointBottom, paint);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
                commonTextView.setLayoutParams(layoutParams);
                commonTextView.setX(xValue);
                commonTextView.setY(yValue);
                for (ColorBall ball : colorballs) {
                    canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                            new Paint());
                }


         /*  this.postDelayed(new Runnable() {
               @Override
               public void run() {
                   setDefaultPointValues();
               }
           },3000);*/

            }
        }

    }

    // events when touching the screen
    public boolean onTouchEvent(MotionEvent event) {
        if(resizeEnabled) {
            int eventaction = event.getAction();

            int X = (int) event.getX();
            int Y = (int) event.getY();

            switch (eventaction) {

                case MotionEvent.ACTION_DOWN: // touch down so check if the finger is on

                    // a ball
                    balID = -1;
                    startMovePoint = new Point(X, Y);
                    for (ColorBall ball : colorballs) {
                        // check if inside the bounds of the ball (circle)
                        // get the center for the ball
                        int centerX = ball.getX() + ball.getWidthOfBall();
                        int centerY = ball.getY() + ball.getHeightOfBall();
                        paint.setColor(Color.CYAN);
                        // calculate the radius from the touch to the center of the ball
                        double radCircle = Math
                                .sqrt((double) (((centerX - X) * (centerX - X)) + (centerY - Y)
                                        * (centerY - Y)));

                        if (radCircle < ball.getWidthOfBall()) {

                            balID = ball.getID();
                            if (balID == 1 || balID == 3) {
                                groupId = 2;
                                canvas.drawRect(point1.x, point3.y, point3.x, point1.y,
                                        paint);
                            } else {
                                groupId = 1;
                                canvas.drawRect(point2.x, point4.y, point4.x, point2.y,
                                        paint);
                            }
                            invalidate();
                            break;
                        }
                        invalidate();
                    }

                    break;

                case MotionEvent.ACTION_MOVE: // touch drag with the ball
                    // move the balls the same as the finger
                    if (((point2.x + 100) < point4.x) && ((point1.x + 100) < point3.x) && ((point4.y + 100) < point2.y) && ((point1.y + 100) < point3.y)) {

                        if (balID > -1) {
                            colorballs.get(balID).setX(X);
                            colorballs.get(balID).setY(Y);

                            paint.setColor(Color.CYAN);
                            if (groupId == 1) {
                                colorballs.get(1).setX(colorballs.get(0).getX());
                                colorballs.get(1).setY(colorballs.get(2).getY());
                                colorballs.get(3).setX(colorballs.get(2).getX());
                                colorballs.get(3).setY(colorballs.get(0).getY());
                                canvas.drawRect(point1.x, point3.y, point3.x, point1.y,
                                        paint);
                            } else {
                                colorballs.get(0).setX(colorballs.get(1).getX());
                                colorballs.get(0).setY(colorballs.get(3).getY());
                                colorballs.get(2).setX(colorballs.get(3).getX());
                                colorballs.get(2).setY(colorballs.get(1).getY());
                                canvas.drawRect(point2.x, point4.y, point4.x, point2.y,
                                        paint);
                            }


                        } else {
                            if (startMovePoint != null) {
                                paint.setColor(Color.CYAN);
                                int diffX = X - startMovePoint.x;
                                int diffY = Y - startMovePoint.y;
                                startMovePoint.x = X;
                                startMovePoint.y = Y;
                                colorballs.get(0).addX(diffX);
                                colorballs.get(1).addX(diffX);
                                colorballs.get(2).addX(diffX);
                                colorballs.get(3).addX(diffX);
                                colorballs.get(0).addY(diffY);
                                colorballs.get(1).addY(diffY);
                                colorballs.get(2).addY(diffY);
                                colorballs.get(3).addY(diffY);
                                if (groupId == 1) {
                                    canvas.drawRect(point1.x, point3.y, point3.x, point1.y,
                                            paint);

                                } else {
                                    canvas.drawRect(point2.x, point4.y, point4.x, point2.y,
                                            paint);

                                }


                            }
                        }
                    } else {
                        resetPointsView();


                        Toast.makeText(getContext().getApplicationContext(), "NotMove", Toast.LENGTH_LONG).show();
                    }
                    invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                    // touch drop - just do things here after dropping

                    break;
            }
            // redraw the canvas
            invalidate();
        }
        return true;

    }

    private void resetPointsView(){
        if(!((point2.x + 100)<point4.x)){
            point4.x = point2.x+105;
            point3.x = point2.x+105;
        }else if(!((point1.x + 100) <point3.x)){
            point4.x = point1.x+105;
            point3.x = point1.x+105;
        }else if(!((point4.y + 100) <point2.y)){
            point2.y = point4.y+105;
            point3.y = point4.y+105;
        }else if(!((point1.y + 100) <point3.y)){
            point3.y = point1.y+105;
            point2.y = point1.y+105;
        }

    }


    public void shade_region_between_points() {
        canvas.drawRect(point1.x, point3.y, point3.x, point1.y, paint);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(resizeEnabled) {
           commonTextView = (TextView) view;
           int X = (int) view.getX();
         int Y = (int) view.getY();
          point1.x = X;
           point2.x = X;
            point4.x = X + view.getWidth();
            point3.x = X + view.getWidth();
            point1.y = Y;
            point2.y = Y + view.getHeight();
           point3.y = Y + view.getHeight();
           point4.y = Y;
        }
        return false;
    }

}
