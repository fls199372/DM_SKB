package com.example.dm_skb.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.dm_skb.R;
import com.example.dm_skb.land.ImageCompress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Guide_News_Activity extends Activity implements
		ViewPager.OnPageChangeListener {

	public Context context ;
	
	public static int screenW, screenH;

	private static final int VIEW_NO_1 = 0;
	private static final int VIEW_NO_2 = 1;
	private static final int VIEW_NO_3 = 2;
	private static final int VIEW_NO_4 = 3;
	private static final int VIEW_NO_5 = 4;
	// 第5页的资源,坐标
	static Button button;

	private int preIndex = 0;
	private ViewPager mPager;
	private MyViewPagerAdapter mPagerAdapter;
	List<View> list = new ArrayList<View>();
	private OnClickListener clicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == button) {// 返回
				Intent intent = new Intent (Guide_News_Activity.this,MainActivity.class);			
				Bundle bundle=new Bundle();;
		    	bundle.putString("device_token",""+device_token);
		  
				intent.putExtras(bundle);
				startActivity(intent);
				SharedPreferences preferences = getSharedPreferences("isFirstUse",
						MODE_WORLD_READABLE);
				//实例化editor对象
				Editor editor=preferences.edit();
				//存入数据
				editor.putBoolean("isFirstUse", false);
				//提交修改
				editor.commit();
				finish();
			}
		}
	};
	String device_token="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide_main);
		Bundle bundle=getIntent().getExtras();
		device_token=bundle.getString("device_token");
		context = this ;
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenW = metric.widthPixels; // 屏幕宽度（像素）
		screenH = metric.heightPixels; // 屏幕高度（像素）
		
		LayoutInflater inflater = LayoutInflater.from(this);
		
		View view0 = inflater.inflate(R.layout.guide_fragment_main_1, null,
				false);

		View view1 = inflater.inflate(R.layout.guide_fragment_main_2, null,
				false);

		View view2 = inflater.inflate(R.layout.guide_fragment_main_3, null,
				false);

		View view3 = inflater.inflate(R.layout.guide_fragment_main_4, null,
				false);
		View view4 = inflater.inflate(R.layout.guide_fragment_main_5, null,
				false);
		list.add(view0);
		list.add(view1);
		list.add(view2);
		list.add(view3);
		list.add(view4);
		button = (Button)view4.findViewById(R.id.button);
		button.setOnClickListener(clicklistener);
		mPager = (ViewPager) findViewById(R.id.container);
		mPagerAdapter = new MyViewPagerAdapter(list);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(this);
		mPager.setPageTransformer(true, new com.example.dm_skb.transforms.StackTransformer());

		//animal(VIEW_NO_1);
	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = mListViews.get(position) ;
			BitmapDrawable drawable = (BitmapDrawable)view.getBackground() ;
			if (drawable != null) {
				drawable.getBitmap().recycle() ;
			}
			switch (position) {
				case VIEW_NO_1:
					break;
				case VIEW_NO_2:
					break;
				case VIEW_NO_3:
					break;
				case VIEW_NO_4:
					break;
				case VIEW_NO_5:
					break;
				default:
					break;
			}
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@SuppressWarnings("deprecation")
		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			View view = mListViews.get(position) ;
			container.addView(view, 0);// 添加页卡
			switch (position) {
			case VIEW_NO_1:
				view.setBackgroundDrawable(
						ImageCompress
						.getInstance()
						.getCompressFromId(context, R.drawable.firstpage, screenW, screenH)) ;
				break;
			case VIEW_NO_2:
				view.setBackgroundDrawable(
						ImageCompress
						.getInstance()
						.getCompressFromId(context, R.drawable.secondpage, screenW, screenH)) ;
				break;
				case VIEW_NO_3:
					view.setBackgroundDrawable(
							ImageCompress
									.getInstance()
									.getCompressFromId(context, R.drawable.guide_three_bg, screenW, screenH)) ;
					break;
				case VIEW_NO_4:
					view.setBackgroundDrawable(
							ImageCompress
									.getInstance()
									.getCompressFromId(context, R.drawable.thirdpage, screenW, screenH)) ;
					break;

			case VIEW_NO_5:
				view.setOnTouchListener(mOnTouchListener);
				view.setBackgroundDrawable(
						ImageCompress
						.getInstance()
						.getCompressFromId(context, R.drawable.guide_four_bg, screenW, screenH)) ;
				break;
			default:
				break;
			}
			
			return mListViews.get(position);
		}
		@Override
		public int getCount() {
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		//animal(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (preIndex == 4) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x1 = (int) event.getX();
					Toast.makeText(Guide_News_Activity.this, "X1--->" + x1,
							Toast.LENGTH_SHORT).show();
					break;
				case MotionEvent.ACTION_MOVE:

					x2 = (int) event.getX();
					Toast.makeText(Guide_News_Activity.this, "X2--->" + x2,
							Toast.LENGTH_SHORT).show();
					if ((x2 - x1) < 0) {
						finish();
					}
					break;
				case MotionEvent.ACTION_UP:
					x2 = (int) event.getX();
					Toast.makeText(Guide_News_Activity.this, "X2--->" + x2,
							Toast.LENGTH_SHORT).show();
					if ((x2 - x1) < 0) {
						finish();
					}
					break;
				default:
					break;
				}
			}
			return true;
		}
	};

	int x1 = 0, x2 = 0;

}
