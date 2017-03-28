package com.example.dm_skb.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class ListViewCompat extends ListView {

	
	private SideView  mFocusedItemSideView;//
	
	
	
	public ListViewCompat(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				
				int mx=(int) ev.getX();
				int my=(int) ev.getY();
				
				//依据触摸点的坐标计算出点击的是ListView的哪个Item
				int  position=pointToPosition(mx, my);
				Log.i("position", position+"");
				
				if (position!=INVALID_POSITION) {
					//mes
					MessageItem dataItem=(MessageItem) getItemAtPosition(position);
					mFocusedItemSideView=dataItem.getmSideView();
				}
				break;
	
			default:
				break;
		}
		
		if (mFocusedItemSideView!=null) {
			mFocusedItemSideView.onRequireTouchEvent(ev);
		}
		return super.onTouchEvent(ev);
	}

}
