package com.example.xm2;

import java.util.ArrayList;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	ArrayList<Example> arrayList;
	Madapter adapter;
	ListView list;
	ImageView btn1;
	ImageView btn2;
	ImageView btn3;
	ImageView btn4;
	
	int a=0;
	int b=0;
	String d;//总时间
	String e;//当前时间
	int x=0;
	SeekBar seekbar1;
	MediaPlayer media;
	Mjindutiao mjindu;// 进度条

	TextView textview1;//显示当前播放歌名
	TextView t1;//当前时间
	TextView t2;//总时间
	
	
	Scan sc;
	int flag = 0;
	Boolean isplay = true;
	int position=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sc = new Scan();
		mjindu = new Mjindutiao();
		seekbar1 = (SeekBar) this.findViewById(R.id.seekbar1);
		textview1 = (TextView) this.findViewById(R.id.geming);//歌名显示
		t1=(TextView)this.findViewById(R.id.timenow);//当前时间
		t2=(TextView)this.findViewById(R.id.timeall);//总时间
		
		
		btn1 = (ImageView) this.findViewById(R.id.last);
		btn2 = (ImageView) this.findViewById(R.id.pause);
		btn3 = (ImageView) this.findViewById(R.id.next);
		btn4 = (ImageView) this.findViewById(R.id.exit);
		
		
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);

		
		media = new MediaPlayer();
		arrayList = sc.query(this);
		list = (ListView) findViewById(R.id.listview1);
		adapter = new Madapter(this, arrayList);
		//透明度
		list.setCacheColorHint(0);
		list.setAdapter(adapter);
		// 启动Service
		Intent intent1 = new Intent();
		intent1.setClass(MainActivity.this, Mservive.class);
		startService(intent1);
		Zhuce2();
		// 点击列表播放音乐
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent();
				intent2.setAction("ACTION_list");
				intent2.putExtra("list", position);
				sendBroadcast(intent2);
				btn2.setImageResource(R.drawable.pause);
			}
		});
		
		// 进度条拖动事件
		seekbar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar1) {
				// TODO Auto-generated method stub

				btn2.setImageResource(R.drawable.pause);
				flag = 0;
				
				int p = seekbar1.getProgress();
				Intent intent3 = new Intent();
				intent3.setAction("ACTION_bofang");
				intent3.putExtra("bofang", p);
				sendBroadcast(intent3);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
			}
		});
	}

	// 注册 广播2
	public void Zhuce2() {
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction("ACTION_list");

		mFilter.addAction("ACTION_jdnow");
		
		mFilter.addAction("ACTION_jindu");

		mFilter.addAction("ACTION_bofang");

		mFilter.addAction("ACTION_play");

		mFilter.addAction("ACTION_last");

		mFilter.addAction("ACTION_next");

		mFilter.addAction("ACTION_name");
		
		mFilter.addAction("ACTION_timeall");

		mFilter.addAction("ACTION_timenow");
		
		
		registerReceiver(mjindu, mFilter);																							
	}

	// 自定义广播接收者
	class Mjindutiao extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// 判断是哪个广播事件
			if (intent.getAction().equals("ACTION_jindu")) {
				a = intent.getIntExtra("jindu", 0);
				seekbar1.setMax(a);
				seekbar1.setProgress(0);
			}
			if (intent.getAction().equals("ACTION_jdnow")) {
				b = intent.getIntExtra("jdnow", 0);
				seekbar1.setProgress(b);
			}
			//接收歌曲名称
			if(intent.getAction().equals("ACTION_name"))
			{
				String c=intent.getStringExtra("name");
				textview1.setText("歌名"+"："+c);
			}
			//接收总时间
			if(intent.getAction().equals("ACTION_timeall"))
			{
				d=intent.getStringExtra("timeall");
				t2.setText(d);
			}
			//接收当前时间
			if(intent.getAction().equals("ACTION_timenow"))
			{
				e=intent.getStringExtra("timenow");
				t1.setText(e);
				if(e==d)
				{
					seekbar1.setProgress(0);
					btn2.setImageResource(R.drawable.play);
				}
			}
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.last: {
			// 上一首
			if (position - 1>=0) 
				{
					position--;
				}
				else
				{
					Toast.makeText(this, "当前已经是第一首歌曲了", Toast.LENGTH_SHORT).show();
				}
				btn2.setImageResource(R.drawable.pause);
				Intent intent5 = new Intent();
				intent5.setAction("ACTION_last");
				intent5.putExtra("last", position);
				sendBroadcast(intent5);
			break;
		}
		case R.id.pause: {
			// 暂停
			if (flag == 0) 
			{
				btn2.setImageResource(R.drawable.play);
				flag = 1;
			} 
			else if (flag == 1) 
			{
				btn2.setImageResource(R.drawable.pause);
				flag = 0;
			}
			Intent intent4 = new Intent();
			intent4.setAction("ACTION_play");
			intent4.putExtra("play", flag);
			sendBroadcast(intent4);
			break;
		}
		case R.id.next: {
			// 下一首
			if (position + 1 <arrayList.size()) {
				position++;
			}
			else
			{
				Toast.makeText(this, "当前已经是最后一首歌曲了", Toast.LENGTH_SHORT).show(); 
			}
			btn2.setImageResource(R.drawable.pause);
			Intent intent6 = new Intent();
			intent6.setAction("ACTION_next");
			intent6.putExtra("next", position);
			sendBroadcast(intent6);
			break;
		}
		case R.id.exit: {
			// 退出
			//MainActivity.this.finish();
			//System.exit(0);
			dialog();
			break;
		}

		default:
			break;
		}
	}
	// 弹出对话框
		public void dialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setMessage("确认退出吗？");
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					// MainActivity.this.finish();
					// 退出当前界面
					System.exit(0);
					// 完全退出
					ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					manager.killBackgroundProcesses(getPackageName());
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mjindu);
	}
	
	
}
