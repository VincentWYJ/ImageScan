package com.dylan_wang.autostart;

import android.graphics.Color;
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

public class Service1 extends Service
{
    //���帡�����ڲ���
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //���������������ò��ֲ����Ķ���
    WindowManager mWindowManager;

    ImageButton mFloatView;

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();

        createFloatView();

        //kill the main acticity
        //MainActivity ma = new MainActivity();
        //ma.finish();
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
        //��ȡ����WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);

        //����window type
        wmParams.type = LayoutParams.TYPE_PHONE;
        //����ͼƬ��ʽ��Ч��Ϊ����͸��
        wmParams.format = PixelFormat.RGBA_8888;
        //���ø������ڲ��ɾ۽���ʵ�ֲ���������������������ɼ����ڵĲ�����
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        //������������ʾ��ͣ��λ��Ϊ����ö�
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ�������gravity
        wmParams.x = 0;
        wmParams.y = 0;

        //�����������ڳ�������
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		 /*// �����������ڳ�������
        wmParams.width = 200;
        wmParams.height = 80;*/

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //��ȡ����������ͼ���ڲ���
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        //���mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //�������ڰ�ť
        mFloatView = (ImageButton)mFloatLayout.findViewById(R.id.float_id);
        //mFloatLayout.setBackgroundColor(Color.TRANSPARENT);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        //���ü����������ڵĴ����ƶ�
        mFloatView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                //getRawX�Ǵ���λ���������Ļ�����꣬getX������ڰ�ť������
                wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth() / 2;

                //��25Ϊ״̬���ĸ߶�
                wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight() / 2 - 25;

                //ˢ��
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;  //�˴����뷵��false������OnClickListener��ȡ��������
            }
        });

        mFloatView.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                mFloatView.setVisibility(View.INVISIBLE);   //������ʧ
                /*
                Toast.makeText(Service1.this, "Service1", Toast.LENGTH_SHORT).show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        mFloatView.setVisibility(View.VISIBLE); //view��Ҫ���صĿؼ�
                    }
                }, 3000);  //3000�����ִ��
                */

                Intent intent = new Intent(getApplicationContext(),Service1.class);
                stopService(intent);
            }
        });
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mFloatLayout != null)
        {
            //�Ƴ���������
            mWindowManager.removeView(mFloatLayout);
        }
    }

}