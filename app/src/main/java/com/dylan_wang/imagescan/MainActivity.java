package com.dylan_wang.imagescan;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.preference.DialogPreference;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    GridView gridviewImg;

    ArrayList<String>fullPathImg;
    int indexImg;
    int countImg;

    ImageView imageviewImg;
    List<ImageView>listImg;
    ImageAdapter adapterImg;

    int windowWidth;
    int imageWidth;
    String message;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullPathImg = new ArrayList<String>();

        getImgs();

        countImg = fullPathImg.size();

        Toast.makeText(this, Integer.toString(countImg),Toast.LENGTH_SHORT).show();

        gridviewImg = (GridView)findViewById(R.id.gridviewImg);
        adapterImg = new ImageAdapter(this);
        gridviewImg.setAdapter(adapterImg);
        //gridviewImg.setAdapter(new ImageAdapter(this));//call mageAdapter.java

        gridviewImg.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int positon,long id) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("listnameImg",fullPathImg);
                bundle.putInt("position", positon);
                //Intent intent = new Intent(MainActivity.this,editimg.class);
                Intent intent = new Intent(MainActivity.this,slideImg.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        gridviewImg.setOnItemLongClickListener(new GridView.OnItemLongClickListener() {//监听事件
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final View v = view;
                final int p = position;
                final Bundle savedInstanceState1 = savedInstanceState;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.long_click)
                        .setItems(R.array.items_long_click,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //String[] arrayItems = getResources().getStringArray(R.array.items_long_click);
                                        if (which == 1) {
                                            CharSequence delete_firm = getString(R.string.delete_firm);
                                            new AlertDialog.Builder(MainActivity.this)
                                                    .setMessage(delete_firm)
                                                    .setPositiveButton(R.string.Ok,
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    File removeFile = new File(fullPathImg.get(p));
                                                                    if (removeFile.exists()) {
                                                                        removeFile.delete();
                                                                        //adapterImg.notifyDataSetChanged();
                                                                        //v.setVisibility(v.GONE);
                                                                        adapterImg.notifyDataSetChanged();
                                                                        finish();
                                                                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                                        startActivity(intent);

                                                                        Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                                                        Uri contentUri = Uri.fromFile(removeFile);
                                                                        media.setData(contentUri);
                                                                        MainActivity.this.sendBroadcast(media);
                                                                        /**/
                                                                        //gridviewImg.postInvalidate();
                                                                        //gridviewImg.invalidate();
                                                                        //onCreate(savedInstanceState1);  //failed
                                                                    }
                                                                }
                                                            })
                                                    .setNegativeButton(R.string.Cancel,
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    ;
                                                                }
                                                            }
                                                    ).show();
                                        } else if (which == 0) {
                                            Bundle bundle = new Bundle();
                                            bundle.putStringArrayList("listnameImg", fullPathImg);
                                            bundle.putInt("position", p);
                                            //Intent intent = new Intent(MainActivity.this,editimg.class);
                                            Intent intent = new Intent(MainActivity.this, slideImg.class);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    }
                                }
                        ).show();
                return true;
            }
        });
    }

    public void getImgs(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            String pathRoot = Environment.getExternalStorageDirectory().getPath()+"/";

            File file = new File(pathRoot);
            getAllFiles(file);
        }
    }

    private void getAllFiles(File root){
        File files[] = root.listFiles();
        if(files != null){
            for (File f : files){
                if(f.isDirectory()){
                    getAllFiles(f);
                }else{
                    String strf = f.toString();
                    String str = strf.substring(strf.length() - 4, strf.length());
                    if(str.equals(".bmp")||str.equals(".jpg")||str.equals(".jpeg")||str.equals(".png")||str.equals(".gif")||str.equals(".tif")||str.equals(".tiff")){
                        boolean bool = fullPathImg.add(strf);
                    }
                }
            }
        }
    }
/**/

    public class ImageAdapter extends BaseAdapter {

        private Context context;

        int countImg2;

        public ImageAdapter(Context context){
            this.context = context;
            this.countImg2 = countImg;
        }

        @Override
        public int getCount(){
            return countImg2;
        }

        public Object getItem(int position){
            return position;
        }

        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            windowWidth = getWindowManager().getDefaultDisplay().getWidth();
            int pad = 4;
            if(convertView == null){
                imageviewImg = new ImageView(context);
            }
            else{
                imageviewImg = (ImageView)convertView;
            }
            //imageviewImg = new ImageView(context);
            imageviewImg.setLayoutParams(new GridView.LayoutParams((windowWidth - pad * 12) / 4, (windowWidth - pad * 12) / 4));
            imageviewImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageviewImg.setPadding(pad,pad,pad,pad);
            imageviewImg.setImageBitmap(BitmapFactory.decodeFile(fullPathImg.get(position)));
            return imageviewImg;
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
