package com.dylan_wang.imagescan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class slideImg extends ActionBarActivity implements GestureDetector.OnGestureListener {

    ViewFlipper viewflipperImg;
    private GestureDetector detector;
    int position;
    ArrayList<String>fullpathImg;
    int countImg;
    int indexImg;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideimg);

        bundle = getIntent().getExtras();
        fullpathImg = bundle.getStringArrayList("listnameImg");
        position = bundle.getInt("position");

        viewflipperImg = (ViewFlipper)findViewById(R.id.viewflipperImg);
        detector = new GestureDetector(this);

        countImg = fullpathImg.size();

        for(indexImg=0;indexImg<countImg;++indexImg){
            ImageView imageviewImg = new ImageView(this);
            //imageviewImg.setImageBitmap(BitmapFactory.decodeFile(fullpathImg.get((indexImg+position)%countImg)));
            InputStream is = null;
            try{
                is = new java.io.FileInputStream(fullpathImg.get((indexImg+position)%countImg));
            }
            catch(Exception e){
                e.printStackTrace();
            }
            imageviewImg.setImageBitmap(BitmapFactory.decodeStream(is));
            viewflipperImg.addView(imageviewImg);
        }

        viewflipperImg.setFlipInterval(2000);
        viewflipperImg.startFlipping();

        Toast.makeText(this,Integer.toString(countImg),Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_slide_img, menu);
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

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 120) {
            this.viewflipperImg.showNext();
            return true;
        } else if (e1.getX() - e2.getX() < -120) {
            this.viewflipperImg.showPrevious();
            return true;
        } else if(e1.getY()-e2.getY() > 120){
            this.viewflipperImg.showNext();
        }else if(e1.getY()-e2.getY() < -120){
            this.viewflipperImg.showPrevious();
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if(viewflipperImg.isFlipping()){
            viewflipperImg.stopFlipping();
        }
        return this.detector.onTouchEvent(event);
    }
}
