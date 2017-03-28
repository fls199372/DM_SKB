/*******************************************************************************
 *
 * Copyright (c) Weaver Info Tech Co. Ltd
 *
 * TabView
 *
 * app.ui.widget.TabView.java
 * TODO: File description or class description.
 *
 * @author: Administrator
 * @since:  2014-4-22
 * @version: 1.0.0
 *
 * @changeLogs:
 *     1.0.0: First created this class.
 *
 ******************************************************************************/
package com.example.dm_skb.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.util.TypedValue;
import com.example.dm_skb.R;
import com.example.dm_skb.util.Common;
import com.zhy.autolayout.AutoLinearLayout;
/**
 * @author Administrator
 *
 */
public class ReportTabView extends LinearLayout implements OnClickListener {

    private OnTabChangeListener mOnTabChangedListener;
    private int mState = 0;
    private final Button mStateButton1;
    private final Button mStateButton2;
    private final Button mStateButton3;
    private final Button mStateButton4;
    private final Button mStateButton5;
    public ReportTabView(Context context) {
        this(context, null);
    }
    public ReportTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    @SuppressLint("NewApi")
	public ReportTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.homecard_main, this);
        mStateButton1 = (Button) findViewById(R.id.button_state1);
        mStateButton2 = (Button) findViewById(R.id.button_state2);
        mStateButton3 = (Button) findViewById(R.id.button_state3);
        mStateButton4= (Button) findViewById(R.id.button_state4);
        mStateButton5= (Button) findViewById(R.id.button_state5);
        //控制登录用户名图标大小
        mStateButton1.setTextSize(TypedValue.COMPLEX_UNIT_PX,Common
                .screenHeight/57); //像素
        mStateButton2.setTextSize(TypedValue.COMPLEX_UNIT_PX,Common
                .screenHeight/57); //像素
        mStateButton3.setTextSize(TypedValue.COMPLEX_UNIT_PX,Common
                .screenHeight/57); //像素
        mStateButton4.setTextSize(TypedValue.COMPLEX_UNIT_PX,Common
                .screenHeight/57); //像素
        mStateButton5.setTextSize(TypedValue.COMPLEX_UNIT_PX,Common
                .screenHeight/57); //像素
        Drawable drawable1 = getResources().getDrawable(R.drawable.search_customer);
        drawable1.setBounds(0,1, Common.screenWidth/14, Common
                .screenHeight/31);
        //第一0是距左边距离，第二0是距上边距离，40分别是长宽
        mStateButton1.setCompoundDrawables(null,drawable1,null,null);//只放左边
      //控制登录用户名图标大小
        Drawable drawable2 = getResources().getDrawable(R.drawable.customer_management);
        drawable2.setBounds(0,1, Common.screenWidth/14, Common
                .screenHeight/31);
        //第一0是距左边距离，第二0是距上边距离，40分别是长宽
        mStateButton2.setCompoundDrawables(null, drawable2, null, null);//只放左边
      //控制登录用户名图标大小
        Drawable drawable3 = getResources().getDrawable(R.drawable.share_points);
        drawable3.setBounds(0,1,Common.screenWidth/14, Common
                .screenHeight/31);
        //第一0是距左边距离，第二0是距上边距离，40分别是长宽
        mStateButton3.setCompoundDrawables(null, drawable3, null, null);//只放左边
        Drawable drawable4 = getResources().getDrawable(R.drawable.about_me);
        drawable4.setBounds(0,1,Common.screenWidth/14, Common
                .screenHeight/31);
        //第一0是距左边距离，第二0是距上边距离，40分别是长宽
        mStateButton4.setCompoundDrawables(null, drawable4, null, null);//只放左边
        Drawable drawable5 = getResources().getDrawable(R.drawable.earn_points);
        drawable5.setBounds(0,1, Common.screenWidth/14, Common
                .screenHeight/31);
        //第一0是距左边距离，第二0是距上边距离，40分别是长宽
        mStateButton5.setCompoundDrawables(null, drawable5, null, null);//只放左边
        mStateButton1.setOnClickListener(this);
        mStateButton2.setOnClickListener(this);
        mStateButton3.setOnClickListener(this);
        mStateButton4.setOnClickListener(this);
        mStateButton5.setOnClickListener(this);
        mStateButton1.setSelected(true);
    }
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param
     *（DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public void setOnTabChangeListener(OnTabChangeListener listener) {
        mOnTabChangedListener = listener;
    }

    public void setCurrentTab(int index) {
        switchState(index);
    }

    private void switchState(int state) {
        if (mState == state) {
            return;
        } // else continue

        mState = state;
        mStateButton1.setSelected(false);
        mStateButton2.setSelected(false);
        mStateButton3.setSelected(false);
        mStateButton4.setSelected(false);
        mStateButton5.setSelected(false);
        Object tag = null;
        switch (mState) {
            case 0:
                mStateButton1.setSelected(true);
                tag = mStateButton1.getTag();
                break;

            case 1:
                mStateButton2.setSelected(true);
                tag = mStateButton2.getTag();
                break;
            case 2:
                mStateButton3.setSelected(true);
                tag = mStateButton3.getTag();
                break;
            case 3:
                mStateButton4.setSelected(true);
                tag=mStateButton4.getTag();
                break;
            case 4:
                mStateButton5.setSelected(true);
                tag=mStateButton5.getTag();
                break;
            default:
                break;
        }

        if (mOnTabChangedListener != null) {
            if (tag != null && mOnTabChangedListener != null) {
                mOnTabChangedListener.onTabChange(tag.toString());
            } else {
                mOnTabChangedListener.onTabChange(null);
            }
        } // else ignored
    }


    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {

        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.button_state1:
                switchState(0);
                break;
            case R.id.button_state2:
                switchState(1);
                break;
            case R.id.button_state3:
                switchState(2);
                break;
            case R.id.button_state4:
                switchState(3);
                break;
            case R.id.button_state5:
                switchState(4);
                break;
            default:
                break;
        }
    }

    public static interface OnTabChangeListener {
        public void onTabChange(String tag);
    }
}
