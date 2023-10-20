package com.eco.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.tv.AdRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivityM extends Activity {
    private static final String AD_UNIT_ID = "ca-app-pub-9000963779651236/9735876306";
    private boolean _isBtnDown;
    private FrameLayout adContainerView;
    AlertDialog alert;
    private String backroundColor;
    private ImageButton blink;
    private ImageButton color;
    private ImageButton color2;
    private List<Integer> colorList;
    Dialog dialog;
    Dialog dialog2;
    private EditText input;
    private String inputTextColor;
    private boolean isBlinker;
    private ImageButton left;
    private int lengthPM;
    MyGameView mGameView;
    private ImageButton minus;
    private int moveX;
    private int moveYFixed;
    private int movementType;
    private Paint paint;
    private ImageButton plus;
    private int previewHeight;
    private int previewWidth;
    private String printMessage;
    private ImageButton right;
    private int screen7over8;
    private int screenHeight;
    private int screenWidth;
    private int seekBarProgress;
    private float speedText2;
    private SeekBar speedbar;
    private ImageButton start;
    private ImageButton stop;
    private float textSizeRatio;
    private float zeroPointValue;
    private final int seekBarMax = 20;
    private boolean isShowingMessage = false;
    private boolean initialLayoutComplete = false;
    TextWatcher printMessageWatcher = new TextWatcher() { // from class: oops.ledscroller.ActivityM.16
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            ActivityM.this.printMessage = "";
            ActivityM.this.printMessage = editable.toString();
            mGameView.setMessage(editable.toString());
          /*  if (ActivityM.this.movementType == 0) {
                ActivityM.this.paint.setTextSize((int) ((ActivityM.this.zeroPointValue / 3.0f) * ActivityM.this.previewHeight));
                ActivityM ActivityM = ActivityM.this;
                ActivityM.lengthPM = (int) ActivityM.paint.measureText(ActivityM.this.printMessage);
                ActivityM.this.setInitCoordinate();
            }*/
        }
    };


    int getStatusBarHeight() {
        int identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mGameView = new MyGameView(this);
        Window window = getWindow();
        window.setContentView(this.mGameView);
        window.addContentView((LinearLayout) ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.activity_main, (ViewGroup) null), new LinearLayout.LayoutParams(-1, -1));
        window.addFlags(128);
        if (Build.VERSION.SDK_INT >= 30) {
            Rect bounds = getWindowManager().getMaximumWindowMetrics().getBounds();
            this.screenWidth = bounds.width();
            this.screenHeight = bounds.height() - getStatusBarHeight();
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            this.screenWidth = displayMetrics.widthPixels;
            this.screenHeight = displayMetrics.heightPixels;
        }
        mGameView.setMessage("\uD83D\uDC96 Enter your text");

        int i = this.screenHeight;
        double d = i;
        Double.isNaN(d);
        this.screen7over8 = (int) (d * 0.875d);
        int i2 = this.screenWidth;
        this.previewWidth = i2;
        double d2 = i2;
        double d3 = i2;
        double d4 = i;
        Double.isNaN(d3);
        Double.isNaN(d4);
        Double.isNaN(d2);
        this.previewHeight = (int) (d2 * (d3 / d4));
        ImageButton imageButton = (ImageButton) findViewById(R.id.left);
        this.left = imageButton;
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: oops.ledscroller.ActivityM.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ActivityM.this.movementType = 2;
                ActivityM.this.setInitCoordinate();
            }
        });
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.right);
        this.right = imageButton2;
        imageButton2.setOnClickListener(new View.OnClickListener() { // from class: oops.ledscroller.ActivityM.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ActivityM.this.movementType = 1;
                ActivityM.this.setInitCoordinate();
            }
        });
        ImageButton imageButton3 = (ImageButton) findViewById(R.id.stop);
        this.stop = imageButton3;
        imageButton3.setOnClickListener(new View.OnClickListener() { // from class: oops.ledscroller.ActivityM.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ActivityM.this.movementType = 0;
                ActivityM.this.setInitCoordinate();
            }
        });
        ImageButton imageButton4 = (ImageButton) findViewById(R.id.minus);
        this.minus = imageButton4;
        imageButton4.setOnTouchListener(new View.OnTouchListener() { // from class: oops.ledscroller.ActivityM.6
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    ActivityM.this._isBtnDown = true;
                    ActivityM.this.onBtnDown(0);
                } else if (action == 1) {
                    ActivityM.this._isBtnDown = false;
                    if (ActivityM.this.movementType == 0) {
                        ActivityM.this.paint.setTextSize((int) ((ActivityM.this.zeroPointValue / 3.0f) * ActivityM.this.previewHeight));
                        ActivityM ActivityM = ActivityM.this;
                        ActivityM.lengthPM = (int) ActivityM.paint.measureText(ActivityM.this.printMessage);
                        ActivityM.this.setInitCoordinate();
                    }
                    view.performClick();
                }
                return false;
            }
        });
        ImageButton imageButton5 = (ImageButton) findViewById(R.id.plus);
        this.plus = imageButton5;
        imageButton5.setOnTouchListener(new View.OnTouchListener() { // from class: oops.ledscroller.ActivityM.7
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    ActivityM.this._isBtnDown = true;
                    ActivityM.this.onBtnDown(1);
                } else if (action == 1) {
                    ActivityM.this._isBtnDown = false;
                    if (ActivityM.this.movementType == 0) {
                        ActivityM.this.paint.setTextSize((int) ((ActivityM.this.zeroPointValue / 3.0f) * ActivityM.this.previewHeight));
                        ActivityM ActivityM = ActivityM.this;
                        ActivityM.lengthPM = (int) ActivityM.paint.measureText(ActivityM.this.printMessage);
                        ActivityM.this.setInitCoordinate();
                    }
                    view.performClick();
                }
                return false;
            }
        });
        ImageButton imageButton6 = (ImageButton) findViewById(R.id.color);
        this.color = imageButton6;
        imageButton6.setOnClickListener(new View.OnClickListener() { // from class: oops.ledscroller.ActivityM.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ActivityM.this.isShowingMessage = true;
                ActivityM ActivityM = ActivityM.this;
                ActivityM ActivityM2 = ActivityM.this;
                ActivityM.this.dialog.show();
                ActivityM.this.dialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: oops.ledscroller.ActivityM.8.1
                    @Override // android.content.DialogInterface.OnKeyListener
                    public boolean onKey(DialogInterface dialogInterface, int i3, KeyEvent keyEvent) {
                        if (i3 == 4) {
                            dialogInterface.dismiss();
                            ActivityM.this.isShowingMessage = false;

                            return true;
                        }
                        return true;
                    }
                });
                ActivityM.this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: oops.ledscroller.ActivityM.8.2
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialogInterface) {
                        ActivityM.this.isShowingMessage = false;

                    }
                });
            }
        });
        ImageButton imageButton7 = (ImageButton) findViewById(R.id.color2);
        this.color2 = imageButton7;
        imageButton7.setOnClickListener(new View.OnClickListener() { // from class: oops.ledscroller.ActivityM.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ActivityM.this.isShowingMessage = true;

                ActivityM ActivityM = ActivityM.this;
                ActivityM ActivityM2 = ActivityM.this;
                ActivityM.this.dialog2.setTitle("back");
                ActivityM.this.dialog2.show();
                ActivityM.this.dialog2.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: oops.ledscroller.ActivityM.9.1
                    @Override // android.content.DialogInterface.OnKeyListener
                    public boolean onKey(DialogInterface dialogInterface, int i3, KeyEvent keyEvent) {
                        if (i3 == 4) {
                            dialogInterface.dismiss();

                            return true;
                        }
                        return true;
                    }
                });
                ActivityM.this.dialog2.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: oops.ledscroller.ActivityM.9.2
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialogInterface) {
                        ActivityM.this.isShowingMessage = false;

                    }
                });
            }
        });
        ImageButton imageButton8 = (ImageButton) findViewById(R.id.blink);
        this.blink = imageButton8;
        imageButton8.setOnClickListener(new View.OnClickListener() { // from class: oops.ledscroller.ActivityM.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ActivityM.this.isBlinker) {
                    ActivityM.this.isBlinker = false;
                    return;
                }
                ActivityM.this.isBlinker = true;
            }
        });
        SeekBar seekBar = (SeekBar) findViewById(R.id.speedbar);
        this.speedbar = seekBar;
        seekBar.setMax(20);
        this.speedbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: oops.ledscroller.ActivityM.11
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar2) {
                ActivityM.this.seekBarProgress = seekBar2.getProgress();
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar2, int i3, boolean z) {
               /* if (i3 < 16) {
                    ActivityM.this.speedText2 = i3 + 3;
                } else if (i3 < 19) {
                    ActivityM.this.speedText2 = i3 + 20;
                } else {
                    ActivityM.this.speedText2 = i3 + 50;
                }*/
            }
        });
        ImageButton imageButton9 = (ImageButton) findViewById(R.id.start);
        this.start = imageButton9;
        imageButton9.setOnTouchListener(new View.OnTouchListener() { // from class: oops.ledscroller.ActivityM.12
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    return false;
                } else if (action != 1) {
                    return false;
                } else {
                    ActivityM.this.saveSettings();
                    view.performClick();
                    return false;
                }
            }
        });


        final View findViewById = findViewById(R.id.ll);
        findViewById.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: oops.ledscroller.ActivityM.15
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                Rect rect = new Rect();
                findViewById.getWindowVisibleDisplayFrame(rect);
                if (((float) (findViewById.getBottom() - rect.bottom)) > findViewById.getResources().getDisplayMetrics().density * 128.0f) {
                    ActivityM.this.isShowingMessage = true;

                    return;
                }
                ActivityM.this.isShowingMessage = false;

            }
        });
    }

    void saveSettings() {
        SharedPreferences.Editor edit = getSharedPreferences("SaveSate", 0).edit();
        edit.putString("printmessage", this.printMessage);
        edit.putString("inputtextcolor", this.inputTextColor);
        edit.putString("backroundcolor", this.backroundColor);
        edit.putInt("movementtype", this.movementType);
        edit.putBoolean("isblinker", this.isBlinker);
        edit.putFloat("speedtext2", this.speedText2);
        float f = this.zeroPointValue / 3.0f;
        this.textSizeRatio = f;
        edit.putFloat("textsizeratio", f);
        edit.putInt("seekBarProgress", this.seekBarProgress);
        edit.apply();
    }

    void setInitCoordinate() {
        int i = this.movementType;
        if (i == 0) {
            int i2 = this.lengthPM;
            int i3 = this.screenWidth;
            if (i2 >= i3) {
                this.moveX = 0;
            } else {
                this.moveX = (i3 / 2) - (i2 / 2);
            }
        } else if (i == 1) {
            int i4 = this.lengthPM;
            int i5 = this.screenWidth;
            if (i4 >= i5) {
                this.moveX = i4;
            } else {
                this.moveX = (i5 / 2) - (i4 / 2);
            }
        } else if (i != 2) {
        } else {
            int i6 = this.lengthPM;
            int i7 = this.screenWidth;
            if (i6 >= i7) {
                this.moveX = i7;
            } else {
                this.moveX = (i7 / 2) - (i6 / 2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBtnDown(int i) {
        new TouchThread(i).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class TouchThread extends Thread {
        int type;

        TouchThread(int i) {
            this.type = i;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            while (ActivityM.this._isBtnDown) {
                if (this.type == 0) {
                    if (ActivityM.this.zeroPointValue > 0.5d) {
                        ActivityM.this.zeroPointValue -= 0.1f;
                    }
                } else if (ActivityM.this.zeroPointValue < 2.9d) {
                    ActivityM.this.zeroPointValue += 0.1f;
                }
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    /* loaded from: classes.dex */
    class MyGameView extends SurfaceView implements Runnable {
        private Bitmap back2;
        Context mContext;
        Thread mThread;
        private int textSize;
        volatile boolean running = false;
        SurfaceHolder mHolder = getHolder();
        String message = "";
        public MyGameView(Context context) {
            super(context);
            this.mContext = context;
        }

        public void resume() {
            this.running = true;
            Thread thread = new Thread(this);
            this.mThread = thread;
            thread.start();
        }
        public void setMessage(String message) {
         this.message = message;
        }
        public void pause() {
            this.running = false;
            while (true) {
                try {
                    this.mThread.join();
                    return;
                } catch (Exception unused) {
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            InputStream inputStream;
            int i;
            int i2;
            int i3;
            ActivityM ActivityM;
            Canvas canvas;
            ActivityM ActivityM2;
            previewHeight = 750;
            screenWidth = 1080;
            int i4;
            int i5;
            double d = ActivityM.this.screen7over8;
            Double.isNaN(d);
            double d2 = ActivityM.this.previewHeight;
            Double.isNaN(d2);
            int i6 = (int) ((d / 4.0d) - (d2 / 2.0d));
            double d3 = ActivityM.this.screen7over8;
            Double.isNaN(d3);
            if (ActivityM.this.previewHeight > ((int) (d3 / 2.0d))) {
                i6 = 0;
            }
           /* BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            AssetManager assets = ActivityM.this.getApplicationContext().getAssets();
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
            int i7 = options.outHeight;
            int i8 = options.outWidth;
            if (i7 > ActivityM.this.previewHeight || i8 > ActivityM.this.previewWidth) {
                int i9 = i7 / 2;
                int i10 = i8 / 2;
                i = 1;
                while (i9 / i >= ActivityM.this.previewHeight && i10 / i >= ActivityM.this.previewWidth) {
                    i *= 2;
                }
            } else {
                i = 1;
            }
            options.inSampleSize = i;
            options.inJustDecodeBounds = false;
            try {
                inputStream = assets.open("bg_led.png");
            } catch (IOException unused3) {
            }
            Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
            if (ActivityM.this.previewHeight <= ActivityM.this.previewWidth) {
                i3 = decodeStream.getWidth();
                double d4 = ActivityM.this.previewHeight;
                double d5 = ActivityM.this.previewWidth;
                Double.isNaN(d4);
                Double.isNaN(d5);
                double d6 = d4 / d5;
                double d7 = i3;
                Double.isNaN(d7);
                i2 = (int) (d6 * d7);
            } else {
                i3 = decodeStream.getWidth();
                i2 = decodeStream.getHeight();
            }
            Bitmap createBitmap = Bitmap.createBitmap(decodeStream, 0, 0, i3, i2);
            try {
                inputStream.close();
            } catch (IOException unused4) {
            }
            this.back2 = Bitmap.createScaledBitmap(createBitmap, ActivityM.this.previewWidth, ActivityM.this.previewHeight, false);
            if (decodeStream != null) {
                decodeStream.recycle();
            }
            if (createBitmap != null) {
                createBitmap.recycle();
            }*/
            ActivityM.this.paint = new Paint();
            ActivityM.this.paint.setDither(true);
            ActivityM.this.paint.setFilterBitmap(true);
            ActivityM.this.paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, 1));
            float f = 3.0f;
            this.textSize = (int) ((ActivityM.this.zeroPointValue / 3.0f) * ActivityM.this.previewHeight);
            ActivityM.this.paint.setTextSize(this.textSize);
            ActivityM ActivityM3 = ActivityM.this;
            ActivityM3.lengthPM = (int) ActivityM3.paint.measureText(message);
            ActivityM.this.moveX = 0;
            ActivityM.this.moveYFixed = ((int) ((previewHeight / 2) - ((ActivityM.this.paint.descent() + ActivityM.this.paint.ascent()) / 2.0f))) + i6;
            if (ActivityM.this.movementType == 0) {
                if (ActivityM.this.lengthPM >= ActivityM.this.screenWidth) {
                    ActivityM.this.moveX = 0;
                } else {
                    ActivityM ActivityM4 = ActivityM.this;
                    ActivityM4.moveX = (ActivityM4.screenWidth / 2) - (ActivityM.this.lengthPM / 2);
                }
            } else if (ActivityM.this.movementType == 1) {
                if (ActivityM.this.lengthPM >= ActivityM.this.screenWidth) {
                    ActivityM ActivityM5 = ActivityM.this;
                    ActivityM5.moveX = ActivityM5.lengthPM;
                } else {
                    ActivityM ActivityM6 = ActivityM.this;
                    ActivityM6.moveX = (ActivityM6.screenWidth / 2) - (ActivityM.this.lengthPM / 2);
                }
            } else if (ActivityM.this.movementType == 2) {
                if (ActivityM.this.lengthPM >= ActivityM.this.screenWidth) {
                    ActivityM ActivityM7 = ActivityM.this;
                    ActivityM7.moveX = ActivityM7.screenWidth;
                } else {
                    ActivityM ActivityM8 = ActivityM.this;
                    ActivityM8.moveX = (ActivityM8.screenWidth / 2) - (ActivityM.this.lengthPM / 2);
                }
            }
            long nanoTime = System.nanoTime();
            int i11 = 120;
            int i12 = 80;
            boolean z = false;
            int i13 = 0;
            boolean z2 = true;
            int i14 = 0;
            int i15 = 0;
            while (this.running) {
                if (Build.VERSION.SDK_INT >= 26) {
                    canvas = this.mHolder.lockHardwareCanvas();
                } else {
                    canvas = this.mHolder.lockCanvas();
                }
                if (canvas != null) {
                    try {
                        synchronized (this.mHolder) {
                            ActivityM.this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
                            canvas.drawRect(0.0f, 0.0f, ActivityM.this.screenWidth, previewHeight, ActivityM.this.paint);
                            this.textSize = (int) ((ActivityM.this.zeroPointValue / f) * ActivityM.this.previewHeight);
                            ActivityM.this.paint.setTextSize(this.textSize);
                            ActivityM.this.moveYFixed = ((int) ((previewHeight / 2) - ((ActivityM.this.paint.descent() + ActivityM.this.paint.ascent()) / 2.0f))) + i6;
                            ActivityM ActivityM9 = ActivityM.this;
                            ActivityM9.lengthPM = (int) ActivityM9.paint.measureText(message);
                            ActivityM.this.paint.setColor(Color.parseColor(ActivityM.this.backroundColor));
                           // float f2 = 0;
                            //float f3 = i6;
                            //i4 = i6;
                            //canvas.drawRect(f2, f3, ActivityM.this.previewWidth + 0, ActivityM.this.previewHeight + i6, ActivityM.this.paint);
                            ActivityM.this.paint.setColor(Color.parseColor(ActivityM.this.inputTextColor));
                            if (!ActivityM.this.isBlinker) {
                                i5 = i11;
                                canvas.drawText(message, ActivityM.this.moveX, ActivityM.this.moveYFixed, ActivityM.this.paint);
                            } else if (z) {
                                i13++;
                                if (z2) {
                                    if (i13 > i11) {
                                        i13 = 0;
                                        z2 = false;
                                    }
                                    i5 = i11;
                                    canvas.drawText(message, ActivityM.this.moveX, ActivityM.this.moveYFixed, ActivityM.this.paint);
                                } else {
                                    i5 = i11;
                                    if (i13 > i12) {
                                        i13 = 0;
                                        z2 = true;
                                    }
                                    canvas.drawText("", ActivityM.this.moveX, ActivityM.this.moveYFixed, ActivityM.this.paint);
                                }
                            } else {
                                i5 = i11;
                                canvas.drawText("", ActivityM.this.moveX, ActivityM.this.moveYFixed, ActivityM.this.paint);
                            }
                            //canvas.drawBitmap(this.back2, f2, f3, ActivityM.this.paint);
                            if (ActivityM.this.movementType != 0) {
                                if (ActivityM.this.movementType == 1) {
                                    if (ActivityM.this.screenWidth >= ActivityM.this.moveX) {
                                        ActivityM.this.moveX += i15;
                                    } else {
                                        ActivityM ActivityM10 = ActivityM.this;
                                        ActivityM10.moveX = -ActivityM10.lengthPM;
                                    }
                                } else if (ActivityM.this.movementType == 2) {
                                    if (ActivityM.this.moveX >= (-ActivityM.this.lengthPM)) {
                                        ActivityM.this.moveX -= i15;
                                    } else {
                                        ActivityM ActivityM11 = ActivityM.this;
                                        ActivityM11.moveX = ActivityM11.screenWidth;
                                    }
                                }
                            }
                        }
                    } finally {
                      //  if (canvas != null) {
                            this.mHolder.unlockCanvasAndPost(canvas);
                       // }
                    }
                } else {
                    //i4 = i6;
                    i5 = i11;
                }
                i14++;
                if (System.nanoTime() - nanoTime >= 1000000000) {
                    double d8 = ActivityM.this.speedText2 * i14;
                    Log.e("TAN", "rund9: "+d8+"##"+speedText2+"##"+i14);
                    Double.isNaN(d8);
                    i15 = ActivityM.this.screenWidth / ((int) (d8 * 0.1d));
                    double d9 = i14;
                    Double.isNaN(d9);
                    Double.isNaN(d9);
                    i12 = (int) (d9 * 0.15d);
                    i5 = (int) (0.2d * d9);
                    nanoTime = System.nanoTime();
                    z = true;
                    i14 = 0;
                }
                try {
                    Thread.sleep(1L);
                } catch (Exception unused5) {
                }
                i11 = i5;
                //i6 = i4;
                //f = 3.0f;
            }
           /* Bitmap bitmap = this.back2;
            if (bitmap == null || bitmap.isRecycled()) {
                return;
            }
            this.back2.recycle();
            this.back2 = null;*/
        }
    }



    @Override // android.app.Activity
    public void onStop() {
        super.onStop();
    }

    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
    }

    @Override // android.app.Activity
    public void onRestart() {
        super.onRestart();
    }

    @Override // android.app.Activity
    public void onDestroy() {

        super.onDestroy();
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("SaveSate", 0);
        this.printMessage = sharedPreferences.getString("printmessage","\uD83D\uDC96 Enter your text");
        this.inputTextColor = sharedPreferences.getString("inputtextcolor", "#00FF00");
        this.backroundColor = sharedPreferences.getString("backroundcolor", "#292929");
        this.movementType = sharedPreferences.getInt("movementtype", 2);
        this.isBlinker = sharedPreferences.getBoolean("isblinker", false);
        this.speedText2 = 20;
        this.textSizeRatio = sharedPreferences.getFloat("textsizeratio", 0.33333334f);
        this.seekBarProgress = sharedPreferences.getInt("seekBarProgress", 10);
        this.zeroPointValue = this.textSizeRatio * 3.0f;
        EditText editText = (EditText) findViewById(R.id.input);
        this.input = editText;
        editText.setText(this.printMessage);
        this.input.addTextChangedListener(this.printMessageWatcher);
        int i = this.movementType;

        this.speedbar.setProgress(this.seekBarProgress);
        this.mGameView.resume();
    }

    @Override // android.app.Activity
    public void onPause() {
        Dialog dialog = this.dialog;
        if (dialog != null) {
            dialog.cancel();
        }
        Dialog dialog2 = this.dialog2;
        if (dialog2 != null) {
            dialog2.cancel();
        }
        AlertDialog alertDialog = this.alert;
        if (alertDialog != null) {
            alertDialog.cancel();
        }
     
        saveSettings();
        this.mGameView.pause();
        super.onPause();
    }

    @Override // android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }
}
