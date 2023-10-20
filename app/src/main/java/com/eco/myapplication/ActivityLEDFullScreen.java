package com.eco.myapplication;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.LinearLayout;


import java.io.IOException;
import java.io.InputStream;

public class ActivityLEDFullScreen extends Activity {
    private String backroundColor;
    private String inputTextColor;
    private boolean isBlink;
    LEDView ledView;
    private int actionScroll;
    private String message;
    private float speedText;
    private float textSizeRatio;
    WindowManager.LayoutParams layout;
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            ActivityLEDFullScreen.this.hideNavigationBar();
        }
    };

    public void hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= 30) {
            getWindow().setDecorFitsSystemWindows(false);
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController == null) {
                return;
            }
            insetsController.hide(WindowInsets.Type.navigationBars());
            insetsController.setSystemBarsBehavior(2);
            return;
        }
        getWindow().getDecorView().setSystemUiVisibility(3846);
    }

    public static Point getRealScreenSize(Context context) {
        Display display;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= 30) {
            display = context.getDisplay();
        } else {
            display = windowManager.getDefaultDisplay();
        }
        Point point = new Point();
        display.getRealSize(point);
        return point;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.ledView = new LEDView(this);
        Window window = getWindow();
        requestWindowFeature(1);
        window.setContentView(this.ledView);
        window.addContentView((LinearLayout) ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_ledfull_screen, (ViewGroup) null), new LinearLayout.LayoutParams(-1, -1));
        window.setContentView(R.layout.activity_ledfull_screen);
        window.addContentView(ledView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout = window.getAttributes();
        layout.screenBrightness = 1F;
        getWindow().setAttributes(layout);
        if (Build.VERSION.SDK_INT >= 30) {
            window.setDecorFitsSystemWindows(false);
            WindowInsetsController insetsController = window.getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            window.setFlags(1024, 1024);
        }
        this.inputTextColor = "#EB35A4";
        this.backroundColor = "#000000";
        this.actionScroll = 2;
        this.isBlink = false;
        this.speedText = 20;
        this.textSizeRatio = 0.33f;
        this.message = " Anh yêu em nhiều lắm !!! \uD83D\uDC96";
        getWindow().addFlags(128);
        hideNavigationBar();
        if (Build.VERSION.SDK_INT >= 30) {
            getWindow().getDecorView().setOnApplyWindowInsetsListener((view, windowInsets) -> {
                if (windowInsets.isVisible(WindowInsets.Type.navigationBars())) {
                    ActivityLEDFullScreen.this.mHandler.sendEmptyMessageDelayed(0, 4000L);
                }
                return windowInsets;
            });
        } else {
            getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(i -> {
                if (i == 0) {
                    ActivityLEDFullScreen.this.mHandler.sendEmptyMessageDelayed(0, 4000L);
                }
            });
        }
    }

    class LEDView extends SurfaceView implements Runnable {
        private Bitmap bg, bg2;
        private int height;
        Context mContext;
        Thread mThread;
        private int width;
        volatile boolean running = false;
        final SurfaceHolder mHolder = getHolder();

        public LEDView(Context context) {
            super(context);
            this.mContext = context;
            if (Build.VERSION.SDK_INT >= 30) {
                Rect bounds = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getMaximumWindowMetrics().getBounds();
                this.height = bounds.height();
                this.width = bounds.width();
                return;
            }
            this.width = getRealScreenSize(this.mContext).x;
            this.height = getRealScreenSize(this.mContext).y;
        }

        public void resume() {
            this.running = true;
            Thread thread = new Thread(this);
            this.mThread = thread;
            thread.start();
        }

        public void pause() {
            this.running = false;
            while (true) {
                try {
                    this.mThread.join();
                    return;
                } catch (Exception ignored) {
                }
            }
        }

        @Override
        public void run() {
            int moveX = 0;
            Canvas canvas;
            int blinkTimeShow = 0;
            loadBg();
            loadBg2();
            Paint paint = new Paint();
            paint.setDither(true);
            paint.setFilterBitmap(true);
            paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, 1));
            int textSize = (int) (this.height * ActivityLEDFullScreen.this.textSizeRatio);
            paint.setTextSize(textSize);
            int measureText = (int) paint.measureText(ActivityLEDFullScreen.this.message);
            int descent = (int) ((this.height / 2) - ((paint.descent() + paint.ascent()) / 2.0f));
            if (ActivityLEDFullScreen.this.actionScroll == 0) {
                if (measureText < this.width) {
                    moveX = (this.width / 2) - (measureText / 2);
                }
            } else if (ActivityLEDFullScreen.this.actionScroll == 1) {
                if (measureText >= this.width) {
                    moveX = measureText;
                } else {
                    moveX = this.width / 2 - measureText / 2;
                }
            } else {
                if (ActivityLEDFullScreen.this.actionScroll == 2) {
                    if (measureText < width) {
                        moveX = (this.width / 2) - (measureText / 2);
                    } else {
                        moveX = this.width;
                    }
                }
            }
            int blinkTimeHide = 120;
            long nanoTime = System.nanoTime();
            int i = 80;
            boolean z = false;
            int blinkTimeCompare = 0;
            boolean isBlink = true;
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
                            paint.setColor(Color.parseColor(ActivityLEDFullScreen.this.backroundColor));
                            blinkTimeShow = i;
                            canvas.drawRect(0.0f, 0.0f, this.width, this.height, paint);
                             float centreX = ((float) canvas.getWidth()  - bg2.getWidth()) /2;

                            float centreY = ((float) canvas.getHeight() - bg2.getHeight()) /2;
                             canvas.drawBitmap(this.bg2, 0, 0, paint);
                            paint.setColor(Color.parseColor(ActivityLEDFullScreen.this.inputTextColor));
                            if (!ActivityLEDFullScreen.this.isBlink) {
                                canvas.drawText(ActivityLEDFullScreen.this.message, moveX, descent, paint);
                            } else if (z) {
                                blinkTimeCompare++;
                                if (isBlink) {
                                    if (blinkTimeCompare > blinkTimeHide) {
                                        blinkTimeCompare = 0;
                                        isBlink = false;
                                    }
                                    canvas.drawText(ActivityLEDFullScreen.this.message, moveX, descent, paint);
                                } else {
                                    if (blinkTimeCompare > blinkTimeShow) {
                                        blinkTimeCompare = 0;
                                        isBlink = true;
                                    }
                                    canvas.drawText("", moveX, descent, paint);
                                }
                            } else {
                                canvas.drawText("", moveX, descent, paint);
                            }
                            canvas.drawBitmap(this.bg, 0.0f, 0.0f, paint);
                            if (ActivityLEDFullScreen.this.actionScroll != 0) {
                                if (ActivityLEDFullScreen.this.actionScroll == 1) {
                                    moveX = this.width >= moveX ? moveX + widthS : -measureText;
                                } else if (ActivityLEDFullScreen.this.actionScroll == 2) {
                                    moveX = moveX >= (-measureText) ? moveX - widthS : this.width;
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
                    double d = ActivityLEDFullScreen.this.speedText * count;
                    widthS = this.width / ((int) (d * 0.1d));
                    blinkTimeHide = (int) (0.2d * count);
                    i = (int) (count * 0.15d);
                    nanoTime = System.nanoTime();
                    z = true;
                    count = 0;
                }
                try {
                    Thread.sleep(1L);
                } catch (Exception unused5) {
                }
            }
            Bitmap bitmap = this.bg;
            if (bitmap == null || bitmap.isRecycled()) {
                return;
            }
            this.bg.recycle();
            this.bg = null;
            Bitmap bitmap2 = this.bg2;
            if (bitmap2 == null || bitmap2.isRecycled()) {
                return;
            }
            this.bg2.recycle();
            this.bg2 = null;
        }

        private void loadBg() {
            InputStream inputStream;
            int inSampleSize;
            int desH;
            int desW;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            AssetManager assets = ActivityLEDFullScreen.this.getApplicationContext().getAssets();
            try {
                inputStream = assets.open("bg_led.png");
            } catch (IOException unused) {
                inputStream = null;
            }
            BitmapFactory.decodeStream(inputStream, null, options);
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException unused2) {
            }
            int outHeight = options.outHeight;
            int outWidth = options.outWidth;
            if (outHeight > this.height || outWidth > this.width) {
                inSampleSize = 1;
                while (outHeight / 2 / inSampleSize >= this.height && outWidth / 2 / inSampleSize >= this.width) {
                    inSampleSize *= 2;
                }
            } else {
                inSampleSize = 1;
            }
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            try {
                inputStream = assets.open("bg_led.png");
            } catch (IOException unused3) {
            }
            Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
            if (this.width >= this.height) {
                desW = decodeStream.getWidth();
                desH = (int) ((float) this.height / this.width * decodeStream.getWidth());
            } else {
                assert decodeStream != null;
                desW = decodeStream.getWidth();
                desH = decodeStream.getHeight();
            }
            Bitmap createBitmap = Bitmap.createBitmap(decodeStream, 0, 0, desW, desH);
            try {
                assert inputStream != null;
                inputStream.close();
            } catch (IOException unused4) {
            }
            this.bg = Bitmap.createScaledBitmap(createBitmap, this.width, this.height, false);
            decodeStream.recycle();
            createBitmap.recycle();
        }

        public void loadBg2() {
            InputStream inputStream;
            int inSampleSize;
            int desH;
            int desW;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            AssetManager assets = ActivityLEDFullScreen.this.getApplicationContext().getAssets();
            try {
                inputStream = assets.open("image_demo.png");
            } catch (IOException unused) {
                inputStream = null;
            }
            BitmapFactory.decodeStream(inputStream, null, options);
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException unused2) {
            }
            int outHeight = options.outHeight;
            int outWidth = options.outWidth;
            if (outHeight > this.height || outWidth > this.width) {
                inSampleSize = 1;
                while (outHeight / 2 / inSampleSize >= this.height && outWidth / 2 / inSampleSize >= this.width) {
                    inSampleSize *= 2;
                }
            } else {
                inSampleSize = 1;
            }
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            try {
                inputStream = assets.open("image_demo.png");
            } catch (IOException unused3) {
            }
            Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
            if (this.width >= this.height) {
                desW = decodeStream.getWidth();
                desH = (int) ((float) this.height / this.width * decodeStream.getWidth());
            } else {
                assert decodeStream != null;
                desW = decodeStream.getWidth();
                desH = decodeStream.getHeight();
            }

            desH = decodeStream.getHeight();


            Bitmap createBitmap = Bitmap.createBitmap(decodeStream, 0, 0, desW, desH);
            try {
                assert inputStream != null;
                inputStream.close();
            } catch (IOException unused4) {
            }
            this.bg2 = Bitmap.createScaledBitmap(createBitmap, this.width, this.height, false);
            decodeStream.recycle();
            createBitmap.recycle();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.ledView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.ledView.pause();
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }
}