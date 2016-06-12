package com.example.xm2;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Toast;

public class Scan {
	private static final Context Context = null;
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
		else {
			Toast.makeText(Context,"没有找到对应music", Toast.LENGTH_SHORT).show();
		}
		return arrList;
	}

//	//歌词时间
//	public static int lyrdata(String time)
//	{
//		//替换：.为#
//		time=time.replace(":", "#");
//		time=time.replace(".", "#");
//		//消除#
//		String mTime[]=time.split("#");
//		//毫秒、秒、分
//		int mtime=Integer.parseInt(mTime[0]);
//		int stime=Integer.parseInt(mTime[1]);
//		int mitime=Integer.parseInt(mTime[2]);
//		//每行转换的时间
//		int ctime=(mtime*60+stime)*1000+mitime*10;
//		return ctime;
//		
//	}
}
