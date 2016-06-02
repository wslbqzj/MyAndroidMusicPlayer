package com.example.xm2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

public class Scan {
	public ArrayList<Example> query(Context mContext)
	{
		ArrayList<Example> arrList = null;
		Cursor c = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				null);

		if (null != c) {
			arrList=new ArrayList<Example>();
			Example example;
			// 循环读取
			while (c.moveToNext()) {
				example =new Example();
				// 歌曲名
				String music_name = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
				// 歌手
				String singer = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
				// 路径
				String path = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
				
				//设置值
				example.setMusic_name(music_name);
				example.setSinger(singer);
				example.setPath(path);
				
				arrList.add(example);
			}
		}
		return arrList;
	}
	public  ArrayList<Lrcexample> redLrc(String path)
	{
		ArrayList<Lrcexample> alist=new ArrayList<Lrcexample>();
		//创建文件，替换mp3为lrc
		File f=new File(path.replace(".mp3", ".lrc"));
		try {
			//文件输入流，读取歌词文件
			FileInputStream fs=new FileInputStream(f);
			//将字节转为字符
			InputStreamReader inputstreamreader=new InputStreamReader(fs,"utf-8");
			//获取字节流
			BufferedReader br=new BufferedReader(inputstreamreader);
			String s="";
			//每次读取一行
			while(null!=(s=br.readLine()))
			{
				//判断s是否为空
				if(!TextUtils.isEmpty(s))
				{
					Lrcexample lrcexam=new Lrcexample();
					//将[替换成空格
					String lyric =s.replace("[", "");
					//消除]
					String data_lr[]=lyric.split("]");
					if(data_lr.length>1)
					{
						//时间
						String time = data_lr[0];
						lrcexam.setm_time(lyrdata(time));
						//歌词
						String lrc =data_lr[1];
						lrcexam.setwenzi(lrc);
						alist.add(lrcexam);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return alist;
	}
	//歌词时间
	public static int lyrdata(String time)
	{
		//替换：.为#
		time=time.replace(":", "#");
		time=time.replace(".", "#");
		//消除#
		String mTime[]=time.split("#");
		//毫秒、秒、分
		int mtime=Integer.parseInt(mTime[0]);
		int stime=Integer.parseInt(mTime[1]);
		int mitime=Integer.parseInt(mTime[2]);
		//每行转换的时间
		int ctime=(mtime*60+stime)*1000+mitime*10;
		return ctime;
		
	}
}
