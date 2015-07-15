package com.dylan_wang.imagescan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;


public class editimg extends ActionBarActivity {

    Bundle bundle;
    int position;
    ArrayList<String>fullpathImg;
    ImageView showImg;
    int windowWidth;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editimg);

        bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        fullpathImg = bundle.getStringArrayList("listnameImg");
        showImg = (ImageView)findViewById(R.id.showImg);

        windowWidth = getWindowManager().getDefaultDisplay().getWidth();
        //showImg.setLayoutParams(new GridView.LayoutParams(R.dimen.height, R.dimen.width));
        //showImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        showImg.setSaveEnabled(true);
        //imageviewImg.setPadding(pad,pad,pad,pad);
        showImg.setImageBitmap(BitmapFactory.decodeFile(fullpathImg.get(position)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editimg, menu);
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
