package com.dylan_wang.learnproject;

import android.app.LauncherActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity
{
    private static int			miCount			= 0;

    /* 数据库对象 */
    private SQLiteDatabase		mSQLiteDatabase	= null;

    /* 数据库名 */
    private final static String	DATABASE_NAME	= "Examples_06_05.db";
    /* 表名 */
    private final static String	TABLE_NAME		= "table1";

    /* 表中的字段 */
    private final static String	TABLE_ID		= "_id";
    private final static String	TABLE_NUM		= "num";
    private final static String	TABLE_DATA		= "data";

    /* 创建表的sql语句 */
    private final static String	CREATE_TABLE	= "CREATE TABLE " + TABLE_NAME + " (" + TABLE_ID + " INTEGER PRIMARY KEY," + TABLE_NUM + " INTEGER,"+ TABLE_DATA + " TEXT)";

    /* 线性布局 */
    LinearLayout				m_LinearLayout	= null;
    /* 列表视图-显示数据库中的数据 */
    ListView					m_ListView		= null;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

		/* 创建LinearLayout布局对象 */
        m_LinearLayout = new LinearLayout(this);
		/* 设置布局LinearLayout的属性 */
        m_LinearLayout.setOrientation(LinearLayout.VERTICAL);
        m_LinearLayout.setBackgroundColor(android.graphics.Color.BLACK);

		/* 创建ListView对象 */
        m_ListView = new ListView(this);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        m_ListView.setBackgroundColor(Color.BLACK);

		/* 添加m_ListView到m_LinearLayout布局 */
        m_LinearLayout.addView(m_ListView, param);

		/* 设置显示m_LinearLayout布局 */
        setContentView(m_LinearLayout);

        // 打开已经存在的数据库
        mSQLiteDatabase = this.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        // 获取数据库Phones的Cursor
        try
        {
			/* 在数据库mSQLiteDatabase中创建一个表 */
            mSQLiteDatabase.execSQL(CREATE_TABLE);
            UpdataAdapter();
        }
        catch (Exception e)
        {
            UpdataAdapter();
        }
    }


    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                AddData();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                DeleteData();
                break;
            case KeyEvent.KEYCODE_1:
                UpData();
                break;
            case KeyEvent.KEYCODE_2:
                DeleteTable();
                break;
            case KeyEvent.KEYCODE_3:
                DeleteDataBase();
                break;
        }
        return true;
    }


    /* 删除数据库 */
    public void DeleteDataBase()
    {
        this.deleteDatabase(DATABASE_NAME);
        this.finish();
    }


    /* 删除一个表 */
    public void DeleteTable()
    {
        mSQLiteDatabase.execSQL("DROP TABLE " + TABLE_NAME);
        this.finish();
    }


    /* 更新一条数据 */
    public void UpData()
    {
        ContentValues cv = new ContentValues();
        cv.put(TABLE_NUM, miCount);
        cv.put(TABLE_DATA, "修改后的数据" + miCount);

		/* 更新数据 */
        mSQLiteDatabase.update(TABLE_NAME, cv, TABLE_NUM + "=" + Integer.toString(miCount - 1), null);

        UpdataAdapter();
    }


    /* 向表中添加一条数据 */
    public void AddData()
    {
        ContentValues cv = new ContentValues();
        cv.put(TABLE_NUM, miCount);
        cv.put(TABLE_DATA, "测试数据库数据" + miCount);
		/* 插入数据 */
        mSQLiteDatabase.insert(TABLE_NAME, null, cv);
        miCount++;
        UpdataAdapter();
    }


    /* 从表中删除指定的一条数据 */
    public void DeleteData()
    {

		/* 删除数据 */
        mSQLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME + " WHERE _id=" + Integer.toString(miCount));

        miCount--;
        if (miCount < 0)
        {
            miCount = 0;
        }
        UpdataAdapter();
    }


    /* 更行试图显示 */
    public void UpdataAdapter()
    {
        // 获取数据库Phones的Cursor
        Cursor cur = mSQLiteDatabase.query(TABLE_NAME, new String[] { TABLE_ID, TABLE_NUM, TABLE_DATA }, null, null, null, null, null);

        miCount = cur.getCount();
        if (cur != null && cur.getCount() >= 0)
        {
            // ListAdapter是ListView和后台数据的桥梁
            ListAdapter adapter = new SimpleCursorAdapter(this,
                    // 定义List中每一行的显示模板
                    // 表示每一行包含两个数据项
                    android.R.layout.simple_list_item_2,
                    // 数据库的Cursor对象
                    cur,
                    // 从数据库的TABLE_NUM和TABLE_DATA两列中取数据
                    new String[] { TABLE_NUM, TABLE_DATA },
                    // 与NAME和NUMBER对应的Views
                    new int[] { android.R.id.text1, android.R.id.text2 });

			/* 将adapter添加到m_ListView中 */
            m_ListView.setAdapter(adapter);
        }
    }


    /* 按键事件处理 */
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
			/* 退出时，不要忘记关闭 */
            mSQLiteDatabase.close();
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}


/*
import android.app.ExpandableListActivity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.CalendarContract;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends LauncherActivity {
    String[] names = {"JAVA","Android"};
    Class<?>[] clazz = {MainActivity.class, MainActivity.class};
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        setListAdapter(adapter);

        Process sh;
        try
        {
            sh = Runtime.getRuntime().exec("su", null, null);
            OutputStream os = sh.getOutputStream();
            os.write(("/system/bin/screencap -p " + "/sdcard/Image.png").getBytes("ASCII"));
            os.flush();
            os.close();
            sh.waitFor();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Intent intentForPosition(int position){
        return new Intent(MainActivity.this,clazz[position]);
    }
}
*/

/*
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

                    //Intent intent2 = new Intent();
                    //intent2.setAction(Intent.ACTION_SENDTO);
                    //intent2.setData(Uri.parse("smsto:"+inputStr));
                    //MainActivity.this.startActivity(intent2);

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
*/