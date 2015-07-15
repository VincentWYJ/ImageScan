package com.dylan_wang.getscreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    TextView dateScreen;
    Button getScreen;
    ImageView imgScreen;

    //time format
    SimpleDateFormat dateFormat;
    //indicate time,path,name,view,then save the view'image to file(name=pathImage+strDate+".png")
    String strDate;
    String pathImage;
    String nameImage;
    View viewScreen;
    //Bitmap bitmap;
    int windowWidth;
    int windowHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateScreen = (TextView)findViewById(R.id.dateScreen);
        getScreen = (Button)findViewById(R.id.getScreen);
        imgScreen = (ImageView)findViewById(R.id.imgScreen);

        dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        strDate = dateFormat.format(new java.util.Date());
        pathImage = Environment.getExternalStorageDirectory().getPath()+"/Pictures/";
        nameImage = pathImage+strDate+".png";

        dateScreen.setText(nameImage);
        //imgScreen.setBackgroundColor(Color.RED);
        //imgScreen.setImageResource(R.mipmap.ic_launcher);
        imgScreen.setImageDrawable(getWallpaper());  //get phone's background picture

        windowWidth = getWindowManager().getDefaultDisplay().getWidth();
        windowHeight = getWindowManager().getDefaultDisplay().getHeight();

        getScreen.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                togetScreen(view);  //function1
                //onClick_CaptureScreen(view);  //function2
            }
        });
    }

    private void togetScreen(View view){
        strDate = dateFormat.format(new java.util.Date());
        nameImage = pathImage+strDate+".png";
        dateScreen.setText(nameImage);

        //viewScreen = view.getRootView();
        viewScreen = getWindow().getDecorView();
        viewScreen.setDrawingCacheEnabled(true);
        viewScreen.buildDrawingCache();

        //Bitmap bitmap = viewScreen.getDrawingCache();
        //Bitmap bitmap = Bitmap.createBitmap(viewScreen.getDrawingCache(),0,0,viewScreen.getWidth(),viewScreen.getHeight());
        Bitmap bitmap = Bitmap.createBitmap(viewScreen.getDrawingCache(),0,0,windowWidth,windowHeight);
        viewScreen.destroyDrawingCache();
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
                    Toast.makeText(this,"get phone's screen succeed",Toast.LENGTH_SHORT).show();

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
            Toast.makeText(this,"cannot get phone's screen",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
