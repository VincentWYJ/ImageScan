package com.dylan_wang.learnproject;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {

    AnalogClock analogclock;

    EditText content_Search;
    Button to_Search;
    Button to_Delete;
    Button to_Call;
    TextView content_Show;

    String search;
    String result;
    ArrayList<String>fullpathImg;
    String pathRoot;
    File root;
    int countImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        analogclock = (AnalogClock)findViewById(R.id.analogClock);
        content_Search = (EditText)findViewById(R.id.content_Search);
        to_Search = (Button)findViewById(R.id.to_Search);
        to_Delete = (Button)findViewById(R.id.to_Delete);
        to_Call = (Button)findViewById(R.id.to_Call);
        content_Show = (TextView)findViewById(R.id.content_Show);

        pathRoot = Environment.getExternalStorageDirectory().getPath()+"/";
        root = new File(pathRoot);

        fullpathImg = new ArrayList<String>();

        getAllFiles(root);

        countImg = fullpathImg.size();

        result = "";

        to_Search.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                SearchFiles(view);
            }
        });

        to_Delete.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                DeleteFiles(view);  //1.delete file
                Animation am = AnimationUtils.loadAnimation(MainActivity.this,R.anim.snake);
                view.startAnimation(am);  //2.animation button
            }
        });

        to_Call.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                String inputStr = content_Search.getText().toString();
                Toast.makeText(MainActivity.this,"check",Toast.LENGTH_SHORT).show();
                if(isPhoneNumberValid(inputStr) == true){
                    //method1
                    //Intent intent = new Intent("android.intent.action.DIAL",Uri.parse("tel:"+inputStr));

                    //method2 same as method1, the action parameter has three style
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    // intent.setAction("android.intent.action.DIAL");
                    //intent.setAction(android.content.Intent.ACTION_DIAL);

                    intent.setData(Uri.parse("tel:"+inputStr));
                    MainActivity.this.startActivity(intent);
                    //intent.setAction(Intent.ACTION_CALL);

                    /*
                    Intent intent2 = new Intent();
                    intent2.setAction(Intent.ACTION_SENDTO);
                    intent2.setData(Uri.parse("smsto:"+inputStr));
                    MainActivity.this.startActivity(intent2);
                    */

                    PendingIntent mPI = PendingIntent.getBroadcast(MainActivity.this,0,new Intent(),0);
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(inputStr,null,"hello world",mPI,null);
                }
            }
        });

        content_Search.setOnKeyListener(new EditText.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                content_Show.setText(content_Search.getText());
                return false;
            }
        });
    }

    private void SearchFiles(View view){
        search = content_Search.getText().toString();
        result = "";
        if(search.equals("")){
            content_Show.setText("Please input none_null content!!!");
        }
        else{
            searchFile(root);
            if(result.equals("")) {
                content_Show.setText("No target file");
            }
            else{
                content_Show.setText(result);
            }
        }
    }

    private void searchFile(File root){

        File[] files = root.listFiles();
        if(files != null){
            for(File f : files){
                if(f.isDirectory()){
                    searchFile(f);
                }
                else if(f.toString().indexOf(search)>0){
                    result += f.toString()+"\n";
                }
            }
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
                    if(str.equals(".bmp")||str.equals(".jpg")||str.equals(".png")||str.equals(".gif")||str.equals(".tif")){
                        boolean bool = fullpathImg.add(strf);
                    }
                }
            }
        }
    }

    private void DeleteFiles(View view){
        if(countImg>0){
            File removeFile = new File(fullpathImg.get(--countImg));
            if(removeFile.exists()){
                removeFile.delete();
                Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                //Intent media = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_DIR");
                Uri contentUri = Uri.fromFile(removeFile);
                media.setData(contentUri);
                MainActivity.this.sendBroadcast(media);
                //Toast.makeText(this,"Has deleted file: "+removeFile.toString(),Toast.LENGTH_SHORT).show();
                Toast toast = Toast.makeText(this,"Has deleted file: "+removeFile.toString(),Toast.LENGTH_SHORT);
                LinearLayout ll = new LinearLayout(this);
                ImageView iv = new ImageView(this);
                View str = toast.getView();  //in fact this sentence get the View(string content) in toasts'
                ll.setOrientation(LinearLayout.HORIZONTAL);
                iv.setImageResource(R.mipmap.ic_launcher);
                ll.addView(iv);
                ll.addView(str);
                toast.setView(ll);
                toast.show();
            }
        }
        else{
            Toast.makeText(this,"No file",Toast.LENGTH_SHORT).show();
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

    public boolean isPhoneNumberValid(String phoneNumber){
        boolean isValid = false;
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        Pattern pattern2 = Pattern.compile(expression2);
        Matcher matcher2 = pattern2.matcher(inputStr);
        if(matcher.matches()||matcher2.matches()){
            isValid = true;
        }
        return isValid;
    }

    public boolean iswithin70(String sms){
        if(sms.length()<=70){
            return true;
        }
        else{
            return false;
        }
    }
}
