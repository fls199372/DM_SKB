package com.example.dm_skb.ui.activity.news;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dm_skb.bean.MyNewsType;
import com.example.dm_skb.dao.NewsDao;
import com.example.dm_skb.R;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.bean.ResponseList;
import com.example.dm_skb.ui.activity.integral.ExchangeActivity;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.widget.CustomProgress;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangxiaoping on 16/9/10.
 * 积分充值
 */

public  class NewsMainActivity extends BaseActivity implements ResponseT<ResponseList<MyNewsType>> {

    private ListView integral_list;
    public List<Map<String, String>> resultList=new ArrayList<Map<String,String>>();//
    MyAdapter adapter;
    private LayoutInflater mInflater;
    NewsDao newsDao;
    Dialog dialog;
    DisplayImageOptions options;		// DisplayImageOptions是用于设置图片显示的类
    String[] imageUrls;					// 图片路径
    ArrayList list = new ArrayList();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_main);
        newsDao=new NewsDao();
        integral_list=(ListView)findViewById(R.id.integral_list);
        mInflater=LayoutInflater.from(this);
        adapter = new MyAdapter();
        integral_list.setAdapter(adapter);
        dialog= CustomProgress.show(NewsMainActivity.this, " " +
                "", false, null);

        integral_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {

                Intent intent = new Intent(NewsMainActivity.this, NewsDetailedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", ""+resultList.get(position).get("NewsTypeId").toString());
                bundle.putString("name", ""+resultList.get(position).get("NewsTypeName").toString());
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.sb)			// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.sb)	// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.sb)		// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
                .displayer(new RoundedBitmapDisplayer(20))	// 设置成圆角图片
                .build();
        newsDao.getNewsMain(""+Common.fuser_id,this);
    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return){
            finish();
        }
    }
    class MyAdapter extends BaseAdapter {
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        public int getCount() {

            return resultList.size();
        }
        public Object getItem(int position) {

            return resultList.get(position);
        }
        @Override
        public long getItemId(int position) {
            System.out.println(""+position);
            return position;
        }
        @Override
        public View getView(int position, View v, final ViewGroup parent) {
            // TODO Auto-generated method stub
            MyHolder holder=new MyHolder();
            LinearLayout layout=null;
            if(v==null){
                layout=(LinearLayout) mInflater.inflate(R.layout.news_main_list,null);
                v=layout;
                holder.integral=(TextView) v.findViewById(R.id.integral);
                holder.number=(TextView) v.findViewById(R.id.number);
                holder.right=(ImageView) v.findViewById(R.id.right);
                holder.messages_picture=(ImageView) v.findViewById(R.id.messages_picture);
                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.integral.setText(resultList.get(position).get("NewsTypeName").toString());
            String total=resultList.get(position).get("Total").toString();
            if(total.equals("0"))
            {
                holder.number.setVisibility(View.GONE);
                holder.right.setVisibility(View.VISIBLE);
            }else
            {
                holder.number.setVisibility(View.VISIBLE);
                holder.number.setText(total);
                holder.right.setVisibility(View.GONE);
            }
            imageLoader.displayImage(imageUrls[position], holder.messages_picture, options);
            return layout;
        }
        class MyHolder {
            public TextView integral;//
            public TextView number;
            public ImageView right;
            public ImageView messages_picture;

        }
    }
    /**
     * 图片加载第一次显示监听器
     * @author Administrator
     *
     */
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                // 是否第一次显示
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    // 图片淡入效果
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
    public void getT(ResponseList<MyNewsType> t) {
        resultList.clear();
        list.clear();
        for(int i=0;i<t.getData().size();i++){
            Map<String, String> arg=new HashMap<String, String>();
            arg.put("NewsTypeId",t.getData().get(i).getNewsTypeId());
            arg.put("Total",t.getData().get(i).getTotal());
            arg.put("NewsTypeName",t.getData().get(i).getNewsTypeName());

            list.add(""+t.getData().get(i).getPicture());
            resultList.add(arg);
        }
        imageUrls= (String[])list.toArray(new String[list.size()]);
        adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
        dialog.dismiss();

    }
    public void getError(String msg) {
        // TODO Auto-generated method stub
        dialog.dismiss();
        postmainHandler(""+msg);
    }
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        newsDao.getNewsMain(""+Common.fuser_id,this);

    }

}
