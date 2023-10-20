package com.eco.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

public class LEDView extends SurfaceView implements Runnable {
    Context mContext;
    Thread mThread;
    private int textSize;
    volatile boolean running = false;
    final SurfaceHolder mHolder = getHolder();
    private Paint paint;
    private float zeroPointValue = 1f;
    private int moveX;
    private int moveYFixed;
    String inputTextColor;
    private String message = "";
    private int lengMessage;
    private String backroundColor = "#292929";
    private boolean isBlink = false;
    public static final int ACTION_LEFT = 1;
    public static final int ACTIONE_RIGHT = 2;
    private int actionScroll = ACTIONE_RIGHT;
    private float speedText = 25;
    private int previewHeight;
    private int previewWidth;
    private int screenWidth;

    public LEDView(Context context) {
        super(context);
        mContext = context;
    }

    public LEDView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public LEDView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public void setInfor(Activity activity) {
        inputTextColor = "#FDFE02";
        if (Build.VERSION.SDK_INT >= 30) {
            Rect bounds = activity.getWindowManager().getMaximumWindowMetrics().getBounds();
            this.screenWidth = bounds.width();
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            this.screenWidth = displayMetrics.widthPixels;
        }
        this.previewWidth = screenWidth;
        previewHeight = (int) pxFromDp(activity, 200);
    }

    public void resume() {
        running = true;
        Thread thread = new Thread(this);
        mThread = thread;
        thread.start();
    }

    public void pause() {
        if (mThread == null) return;
        this.running = false;
        while (true) {
            try {
                mThread.join();
                return;
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void run() {
        Canvas canvas;
        int i;
        paint = new Paint();
        paint.setDither(true);
        paint.setFilterBitmap(true);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        this.textSize = (int) ((zeroPointValue / 3.0f) * previewHeight);
        paint.setTextSize(this.textSize);
        lengMessage = (int) paint.measureText(message);
        moveX = 0;
        moveYFixed = ((int) ((previewHeight / 2) - ((paint.descent() + paint.ascent()) / 2.0f)));
        if (actionScroll == 0) {
            if (lengMessage >= screenWidth) {
                moveX = 0;
            } else {
                moveX = (screenWidth / 2) - (lengMessage / 2);
            }
        } else if (actionScroll == ACTION_LEFT) {
            if (lengMessage >= screenWidth) {
                moveX = lengMessage;
            } else {
                moveX = (screenWidth / 2) - (lengMessage / 2);
            }
        } else if (actionScroll == ACTIONE_RIGHT) {
            if (lengMessage >= screenWidth) {
                moveX = screenWidth;
            } else {
                moveX = (screenWidth / 2) - (lengMessage / 2);
            }
        }
        long nanoTime = System.nanoTime();
        int blinkTimeShow = 120;
        int blinkTimeHide = 80;
        boolean z = false;
        int blinkTimeCompare = 0;
        boolean blink = true;
        int count = 0;
        int widthS = 0;
        while (this.running) {
            if (Build.VERSION.SDK_INT >= 26) {
                canvas = this.mHolder.lockHardwareCanvas();
            } else {
                canvas = this.mHolder.lockCanvas();
            }
            if (canvas != null) {
                try {
                    synchronized (this.mHolder) {
                        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
                        canvas.drawRect(0.0f, 0.0f, screenWidth, previewHeight, paint);
                        this.textSize = (int) ((zeroPointValue / 3) * previewHeight);
                        paint.setTextSize(this.textSize);
                        moveYFixed = ((int) ((previewHeight / 2) - ((paint.descent() + paint.ascent()) / 2.0f)));
                        lengMessage = (int) paint.measureText(message);
                        paint.setColor(Color.parseColor(backroundColor));
                        //canvas.drawRect(0, 0, previewWidth, /*previewHeight + offset*/getHeight(), paint);
                        paint.setColor(Color.parseColor(inputTextColor));
                        if (!isBlink) {
                            i = blinkTimeShow;
                            canvas.drawText(message, moveX, moveYFixed, paint);
                        } else if (z) {
                            blinkTimeCompare++;
                            if (blink) {
                                if (blinkTimeCompare > blinkTimeShow) {
                                    blinkTimeCompare = 0;
                                    blink = false;
                                }
                                i = blinkTimeShow;
                                canvas.drawText(message, moveX, moveYFixed, paint);
                            } else {
                                i = blinkTimeShow;
                                if (blinkTimeCompare > blinkTimeHide) {
                                    blinkTimeCompare = 0;
                                    blink = true;
                                }
                                canvas.drawText("", moveX, moveYFixed, paint);
                            }
                        } else {
                            i = blinkTimeShow;
                            canvas.drawText("", moveX, moveYFixed, paint);
                        }
                        //canvas.drawBitmap(this.bg, 0, offset, paint);
                        if (actionScroll != 0) {
                            if (actionScroll == ACTION_LEFT) {
                                if (getWidth() >= moveX) {
                                    moveX += widthS;
                                } else {
                                    moveX = -lengMessage;
                                }
                            } else if (actionScroll == ACTIONE_RIGHT) {
                                if (moveX >= (-lengMessage)) {
                                    moveX -= widthS;
                                } else {
                                    moveX = screenWidth;
                                }
                            }
                        }
                    }
                } finally {
                    if (mHolder.getSurface().isValid()) {
                        mHolder.unlockCanvasAndPost(canvas);
                    }
                }
            } else {
                i = blinkTimeShow;
            }
            count++;
            if (System.nanoTime() - nanoTime >= 1000000000) {
                double d8 = speedText * count;
                Log.e("TAN", "run: " + d8 + "##" + (screenWidth / 2 / d8) + "##" + screenWidth);
                widthS = screenWidth / ((int) (d8 * 0.1d));
                blinkTimeHide = (int) (count * 0.15d);
                i = (int) (0.2d * count);
                nanoTime = System.nanoTime();
                z = true;
                count = 0;
            }
            try {
                Thread.sleep(1L);
            } catch (Exception unused5) {
            }
            blinkTimeShow = i;
        }
    }

    public void setSpeedText(float speedText) {
        this.speedText = speedText;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setColor(String idColor) {
        this.inputTextColor = idColor;
    }

    public void setAction(int action) {
        this.actionScroll = action;
    }

    public void setBlink(boolean isBlink) {
        this.isBlink = isBlink;
    }
}
