package com.example.dm_skb.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.example.dm_skb.R;

public class SideView extends LinearLayout {

	private  Context  mContext;
	private  Scroller mScroller;
	private  LinearLayout mViewContentLayout;
	
	private  int  mHolderWidth=120;
	
	private  int mLastX=0;
	private  int mLastY=0;
	
	private  OnSlideListenter mOnSlideListenter;
	
	public OnSlideListenter getmOnSlideListenter() {
		return mOnSlideListenter;
	}

	public void setmOnSlideListenter(OnSlideListenter mOnSlideListenter) {
		this.mOnSlideListenter = mOnSlideListenter;
	}

	public  interface OnSlideListenter{
		public static  final  int STATUS_OFF=0;
		public static  final  int STATUS_SCROLL=1;
		public static  final  int STATUS_ON=2;
		
		void onSlide(View view, int status);
	}
	
	public SideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		// TODO Auto-generated constructor stub
	}

	private void initView() {
			mContext=getContext();
			mScroller=new Scroller(mContext);
			setOrientation(LinearLayout.HORIZONTAL);//水平布局
			View.inflate(mContext, R.layout.slide_view_merge, this);
			
			mViewContentLayout=(LinearLayout) findViewById(R.id.view_content);
			mHolderWidth=Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
					, mHolderWidth, getResources().getDisplayMetrics()));
			
			Log.i("mHolderWidth", mHolderWidth+"");
	}

	public void setContentView(View itemView) {
		// TODO Auto-generated method stub
		mViewContentLayout.addView(itemView);
	}

	public void onRequireTouchEvent(MotionEvent ev) {
		int mx=(int) ev.getX();
		int my=(int) ev.getY();
		
		//getScrollX() 就是当前view的左上角相对于母视图的左上角的X轴偏移量。
		int  scrollx=getScrollX();
		
		//Log.i("mx+scrollx", mx+"--"+scrollx);
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) { //判断滚动是否结束
				mScroller.abortAnimation();  //停止动画
			}
			
			//完善接口回调
			if (mOnSlideListenter!=null) {
				mOnSlideListenter.onSlide(this, OnSlideListenter.STATUS_OFF);
			}
			
			break;
			case MotionEvent.ACTION_MOVE:
			int  deltaX=mx-mLastX;
			int  deltaY=my-mLastY;
			
			//Log.i("deltaX+deltaY" , deltaX+"--"+deltaY);
			
			if (Math.abs(deltaX)<Math.abs(deltaY)*2) {
				break;
			}
			
			int   newScrollX=scrollx-deltaX;
			
			if (deltaX!=0) {
				if (newScrollX<0) {
					newScrollX=0;  //已经移动到最左边
				}else if (newScrollX>mHolderWidth) {
					newScrollX=mHolderWidth;
					
				}
				this.scrollTo(newScrollX, 0);//根据滑动坐标 滚动  
			}
			
			break;
		case MotionEvent.ACTION_UP:
					 int  newScrollX_Up=0;
					 if (scrollx-mHolderWidth*0.5>0) {
						 newScrollX_Up=mHolderWidth;
					}
					this.smoothScrollTo(newScrollX_Up,0);//立即滚动到 坐标
					
					if(mOnSlideListenter!=null )
					{
						mOnSlideListenter.onSlide(this, 
								newScrollX_Up==0?OnSlideListenter.STATUS_OFF:OnSlideListenter.STATUS_ON
								);
						 
					}

			break;

		default:
			break;
		}
		
		mLastX=mx;
		mLastY=my;
		//Log.i("mLastX+mLastY" , mLastX+"--"+mLastY);
		
	}

	/**
	 * 缓慢滚动到指定的位置
	 * @param
	 * @param
	 */
	private void smoothScrollTo(int x, int y) {
		// TODO Auto-generated method stub
		int scrollX=getScrollX();
		
		int  deltax=x-scrollX;
		
		mScroller.startScroll(scrollX, 0, deltax, 0, Math.abs(deltax)*3);
		
	}
	
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
			postInvalidate();
		}
	}

	public void shrink() {
		// TODO Auto-generated method stub
		if (getScrollX()!=0) {
			this.smoothScrollTo(0, 0);
		}
	}



}
