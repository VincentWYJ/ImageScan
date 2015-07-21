package com.dylan_wang.screenshot0720;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dylan_wang.screenshot0720.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    TextView dateScreen;
    Button getScreen;
    ImageView imgScreen;

    SimpleDateFormat dateFormat;
    String strDate;
    String pathImage;
    String nameImage;

    MediaProjectionManager mMediaProjectionManager;
    MediaProjection mMediaProjection;
    int mResultCode = 0;
    Intent mResultData = null;
    int REQUEST_MEDIA_PROJECTION = 1;
    Intent intent;
    WindowManager mWindowManager;
    int windowWidth;
    int windowHeight;
    ImageReader mImageReader;
    DisplayMetrics metrics;
    int mScreenDensity;
    Bitmap bitmap = null;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateScreen = (TextView)findViewById(R.id.nameScreen);
        getScreen = (Button)findViewById(R.id.getScreen);
        imgScreen = (ImageView)findViewById(R.id.imgScreen);

        dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        strDate = dateFormat.format(new java.util.Date());
        pathImage = Environment.getExternalStorageDirectory().getPath()+"/Pictures/";
        nameImage = pathImage+strDate+".png";

        dateScreen.setText(nameImage);

        imgScreen.setImageDrawable(getWallpaper());

        mMediaProjectionManager = (MediaProjectionManager)getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        intent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(intent,REQUEST_MEDIA_PROJECTION);

        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        windowWidth = mWindowManager.getDefaultDisplay().getWidth();
        windowHeight = mWindowManager.getDefaultDisplay().getHeight();

        mImageReader = ImageReader.newInstance(windowWidth, windowHeight, ImageFormat.RGB_565, 2);
        metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;

        getScreen.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                togetScreen(view);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            mResultCode = resultCode;
            mResultData = data;
        }
    }

    public boolean takeScreenShot(String imagePath){
        if(imagePath.equals("" )){
            imagePath = Environment.getExternalStorageDirectory()+File. separator+"Screenshot.png" ;
        }
        Bitmap mScreenBitmap = null;
        WindowManager mWindowManager;
        DisplayMetrics mDisplayMetrics;
        Display mDisplay;
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
        mDisplayMetrics = new DisplayMetrics();
        mDisplay.getRealMetrics(mDisplayMetrics);
        float[] dims = {mDisplayMetrics.widthPixels , mDisplayMetrics.heightPixels };
        //mScreenBitmap = Surface.screenshot((int) dims[0], ( int) dims[1]);
        if (mScreenBitmap == null) {
            return false ;
        }
        try {
            FileOutputStream out = new FileOutputStream(imagePath);
            mScreenBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            return false ;
        }
        return true ;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void togetScreen(View view){
        strDate = dateFormat.format(new java.util.Date());
        nameImage = pathImage+strDate+".png";
        dateScreen.setText(nameImage);
/*
        mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mResultData);

        mMediaProjection.createVirtualDisplay("screen-mirror",
                windowWidth, windowHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);

        Image image = mImageReader.acquireLatestImage();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int offset = 0;
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * windowWidth;
        bitmap = Bitmap.createBitmap(windowWidth+rowPadding/pixelStride, windowHeight, Bitmap.Config.RGB_565);
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();
        imgScreen.setImageBitmap(bitmap);

        if(bitmap != null) {
            try{
                File fileImage = new File(nameImage);
                if(!fileImage.exists()){
                    fileImage.createNewFile();
                }
                //FileOutputStream out = new FileOutputStream(nameImage); //1 can also use the string object directly
                FileOutputStream out = new FileOutputStream(fileImage);   //2
                if(out != null){
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Toast.makeText(this,"get phone's screen succeed",Toast.LENGTH_SHORT).show();

                    Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(fileImage);
                    media.setData(contentUri);
                    MainActivity.this.sendBroadcast(media);
                }
            }catch(FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        else{
            //Toast.makeText(this,"cannot get phone's screen",Toast.LENGTH_SHORT).show();
        }
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
