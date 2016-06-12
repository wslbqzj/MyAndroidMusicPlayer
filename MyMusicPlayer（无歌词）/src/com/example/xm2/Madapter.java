package com.example.xm2;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Madapter extends BaseAdapter{
	ArrayList<Example> aList;
	Context mContext;
	
	//构造方法
    public Madapter(Context mContext,ArrayList<Example> aList) {
		this.aList=aList;
	    this.mContext=mContext;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return aList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return aList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHodler viewHodler;
		if (convertView==null) {
			viewHodler=new ViewHodler();
			//引用布局
			convertView=LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
			//实例化控件
			viewHodler.text_name=(TextView) convertView.findViewById(R.id.gequ);
			viewHodler.text_singer=(TextView) convertView.findViewById(R.id.geshou);
		
			convertView.setTag(viewHodler);
		}else {
			viewHodler=(ViewHodler) convertView.getTag();
		}
		viewHodler.text_name.setText(aList.get(position).getMusic_name());
		viewHodler.text_singer.setText("("+aList.get(position).getSinger()+")");
		
		
		return convertView;
	}
	static class ViewHodler{
		TextView text_name,text_singer;
		ImageView img;
	}

}
