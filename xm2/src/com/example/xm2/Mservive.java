package com.example.xm2;

import java.io.IOException;
import java.util.ArrayList;

import android.R.array;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.IBinder;
import android.widget.SeekBar;
import android.widget.TextView;

public class Mservive extends Service {

	ArrayList<Example> aList;
	ArrayList<Lrcexample> lrclist;
	int len;
	Scan sc;
	MediaPlayer mplayer;
	MyBroadMusic myBM;
	xc r = new xc();
	String path;
	String geming;
	String str_fennow;
	String str_miaonow;
	String str_fenall;
	String str_miaoall;
	int num = 0;
	int num2=0;
	int num3=0;
	int num4=0;
	int num5=0;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sc = new Scan();
		aList = sc.query(this);
		mplayer = new MediaPlayer();
		myBM = new MyBroadMusic();
		path = new String();
		Zhuce();
	}

	public void setdata(String path) {
		//重置
		mplayer.reset();
		try {
			// 设置播放路径
			mplayer.setDataSource(path);
			// 缓存
			mplayer.prepare();
			//开始播放
			mplayer.start();
			//传输歌名
			Intent intent4 = new Intent();
			intent4.setAction("ACTION_name");//歌名
			intent4.putExtra("name", geming);
			sendBroadcast(intent4);
			//传输进度条总长度
			Intent intent2 = new Intent();
			intent2.setAction("ACTION_jindu");
			intent2.putExtra("jindu", mplayer.getDuration()); 
			sendBroadcast(intent2);
			//扫描获取歌词
			lrclist=new Scan().redLrc(path);
			//设置初始时间
			str_fennow="0";
			str_miaonow="0";
			Intent intent8 = new Intent();
			intent8.setAction("ACTION_timenow");
			intent8.putExtra("timenow", str_fennow + ":" + str_miaonow);
			sendBroadcast(intent8);
			// 总时间
			str_miaoall = String.valueOf(mplayer.getDuration() / 1000 % 60);
			str_fenall = String.valueOf(mplayer.getDuration() / 1000 / 60);
			Intent intent5 = new Intent();
			intent5.setAction("ACTION_timeall");
			intent5.putExtra("timeall", str_fenall + ":" + str_miaoall);
			sendBroadcast(intent5);
            //开启线程
			r.start();
			

//			mplayer.setOnCompletionListener(new OnCompletionListener() {
//
//				@Override
//				public void onCompletion(MediaPlayer mp) {
//					// TODO Auto-generated method stub
//					try {
//
//						len=0;
//						if(num+1>=aList.size())
//						{
//							num=(num+1)%aList.size();
//						}
//						else
//						{
//							num++;
//						}
//						String path=aList.get(num).getPath();
//						setdata(path);
//						
//					} catch (IllegalArgumentException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (SecurityException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IllegalStateException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			});

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 自定义广播接收者
	class MyBroadMusic extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// 判断是哪个广播事件
			// 点击列表
			if (intent.getAction().equals("ACTION_list")) {
				num = intent.getIntExtra("list", 0);
				geming = aList.get(num).music_name;
				path = aList.get(num).getPath();
				setdata(path);
			}
			// 拖动进度条
			if (intent.getAction().equals("ACTION_bofang")) {
				num2 = intent.getIntExtra("bofang", 0);
				mplayer.seekTo(num2);
				
				str_miaonow = String.valueOf(num2 / 1000 % 60);
				str_fennow = String.valueOf(num2 / 1000 / 60);
				Intent intent9 = new Intent();
				intent9.setAction("ACTION_timenow");
				intent9.putExtra("timenow", str_fennow + ":" + str_miaonow);
				sendBroadcast(intent9);
				mplayer.start();
			}
			// 暂停
			if (intent.getAction().equals("ACTION_play")) {
				num3 = intent.getIntExtra("play", 0);
				if (num3 == 1) {
					mplayer.pause();
					num3=0;
				}
				else if (num3 == 0) {
					mplayer.start();
					num3=1;
				}
			}
			// 上一首
			if (intent.getAction().equals("ACTION_last")) {
				num4 = intent.getIntExtra("last", 0);
				geming = aList.get(num4).music_name;
				path = aList.get(num4).getPath();
				setdata(path);
				System.out.println("上一首标号"+path);
			}
			// 下一首
			if (intent.getAction().equals("ACTION_next")) {
				num5 = intent.getIntExtra("next", 0);
				geming = aList.get(num5).music_name;
				path = aList.get(num5).getPath();
				setdata(path);
				System.out.println("下一首标号"+path);
			}
		}
	}

	// 注册广播方法
	public void Zhuce() {
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
		
		mFilter.addAction("ACTION_gc");
		
		registerReceiver(myBM, mFilter);
	}
	//判断歌词显示
	public int Lrcindex()
	{
		int nowtime=0,alltime=0,index=0;
		if(mplayer.isPlaying())
		{
			nowtime=mplayer.getCurrentPosition();
			alltime=mplayer.getDuration();
		}
		//播放当前时间小于总时间，歌曲正在播放
		if(nowtime<alltime)
		{
			for(int i=0;i<lrclist.size();i++)
			{
				if(i<lrclist.size()-1)
				{
					if(nowtime<lrclist.get(i).getm_time()&&i==0)
					{
						index=i;
					}
					if(nowtime>lrclist.get(i).getm_time()&&nowtime<lrclist.get(i+1).getm_time())
					{
						index=i;
					}
				}
				if(i==lrclist.size()-1&&nowtime>lrclist.get(i).getm_time())
				{
					index=i;
				}
			}
		}
		return index;
	}
	
	// 创建内部类
	class xc extends Thread {
		public void run() {
			try {
				
				for (len = 0; len < mplayer.getDuration();) {

					len = mplayer.getCurrentPosition();
					
					Intent intent3 = new Intent();
					intent3.setAction("ACTION_jdnow");
					intent3.putExtra("jdnow", mplayer.getCurrentPosition());
					sendBroadcast(intent3);
					
					str_miaonow = String.valueOf(len / 1000 % 60);
					str_fennow = String.valueOf(len / 1000 / 60);
					Intent intent6 = new Intent();
					intent6.setAction("ACTION_timenow");
					intent6.putExtra("timenow", str_fennow + ":" + str_miaonow);
					sendBroadcast(intent6);

					//发送歌词广播
					String lrc =lrclist.get(Lrcindex()).getwenzi();
					Intent intent7 = new Intent();
					intent7.setAction("ACTION_gc");
					intent7.putExtra("gc", lrc);
					sendBroadcast(intent7);
					sleep(1000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myBM);
		
	}
	
}
