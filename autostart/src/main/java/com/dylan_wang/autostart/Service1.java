package com.dylan_wang.autostart;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;

public class Service1 extends Service
{
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    WindowManager mWindowManager;

    ImageButton mFloatView;

    private static final String STATE_RESULT_CODE = "result_code";
    private static final String STATE_RESULT_DATA = "result_data";
    private static final int REQUEST_MEDIA_PROJECTION = 1;

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();

        createFloatView();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    private void createFloatView()
    {
        wmParams = new WindowManager.LayoutParams();

        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);

        wmParams.type = LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.RGBA_8888;

        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;

        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        wmParams.x = 0;
        wmParams.y = 0;

        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);

        mWindowManager.addView(mFloatLayout, wmParams);

        mFloatView = (ImageButton)mFloatLayout.findViewById(R.id.float_id);
        //mFloatLayout.setBackgroundColor(Color.TRANSPARENT);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));


        mFloatView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth() / 2;

                wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight() / 2 - 25;

                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;
            }
        });

        mFloatView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mFloatView.setVisibility(View.INVISIBLE);

                //takeScreenshot(v);  //can only get the activity interface, not whole

                takeScreenshot2(v);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        mFloatView.setVisibility(View.VISIBLE);
                    }
                }, 3000);

            }
        });
    }

    public void takeScreenshot(View v) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String strDate = dateFormat.format(new java.util.Date());
        String pathImage = Environment.getExternalStorageDirectory().getPath()+"/Pictures/";
        String nameImage = pathImage + strDate + ".png";
        int windowWidth = mWindowManager.getDefaultDisplay().getWidth();
        int windowHeight = mWindowManager.getDefaultDisplay().getHeight();
        Bitmap bitmap = null;

        View viewScreen = v.getRootView();
        viewScreen.setDrawingCacheEnabled(true);
        viewScreen.buildDrawingCache();
        bitmap = Bitmap.createBitmap(viewScreen.getDrawingCache(),0,0,viewScreen.getWidth(),viewScreen.getHeight());
        viewScreen.destroyDrawingCache();

        if (bitmap != null) {
            //Toast.makeText(this, "go to measure bitmap value", Toast.LENGTH_SHORT).show();
            try {
                File fileImage = new File(nameImage);
                if (!fileImage.exists()) {
                    fileImage.createNewFile();
                    //Toast.makeText(this, nameImage, Toast.LENGTH_SHORT).show();
                }
                //FileOutputStream out = new FileOutputStream(nameImage); //1 can also use the string object directly
                FileOutputStream out = new FileOutputStream(fileImage);   //2
                if (out != null) {
                    //Toast.makeText(this, "go to measure out value", Toast.LENGTH_SHORT).show();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Toast.makeText(this, "get phone's screen succeed", Toast.LENGTH_SHORT).show();

                    Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(fileImage);
                    media.setData(contentUri);
                    getApplicationContext().sendBroadcast(media);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Toast.makeText(this, "cannot get phone's screen", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void takeScreenshot2(View v){
        MediaProjectionManager projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent intent = projectionManager.createScreenCaptureIntent();
        //startActivity(intent);
        int mWidth = mWindowManager.getDefaultDisplay().getWidth();
        int mHeight = mWindowManager.getDefaultDisplay().getHeight();
        ImageReader mImageReader = ImageReader.newInstance(mWidth, mHeight, ImageFormat.RGB_565, 2);
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        int mScreenDensity = metrics.densityDpi;
        /*
        MediaProjection mProjection = projectionManager.getMediaProjection(1, intent);
        final VirtualDisplay virtualDisplay = mProjection.createVirtualDisplay("screen-mirror",
                mWidth, mHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
        Image image = mImageReader.acquireLatestImage();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int offset = 0;
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * mWidth;
        Bitmap bitmap = Bitmap.createBitmap(mWidth+rowPadding/pixelStride, mHeight, Bitmap.Config.RGB_565);
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();
        */
        /*
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String strDate = dateFormat.format(new java.util.Date());
        String pathImage = Environment.getExternalStorageDirectory().getPath()+"/Pictures/";
        String nameImage = pathImage+strDate+".png";
        if(bitmap != null) {
            try{
                File fileImage = new File(nameImage);
                if(!fileImage.exists()){
                    fileImage.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(fileImage);
                if(out != null){
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    Toast.makeText(this,"get phone's screen succeed",Toast.LENGTH_SHORT).show();
                    Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(fileImage);
                    media.setData(contentUri);
                    getApplicationContext().sendBroadcast(media);
                }
            }catch(FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this,"cannot get phone's screen",Toast.LENGTH_SHORT).show();
        }
        */
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mFloatLayout != null)
        {
            mWindowManager.removeView(mFloatLayout);
        }
    }

}