package com.example.dm_skb.ui.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.dm_skb.R;



public class To_Be_Contacted_Tomorrow_Adapter extends BaseAdapter
{
    private Context context;

    private LayoutInflater layoutInflater;

    private List<Map<String, String>> list;

    //构造方法，参数list传递的就是这一组数据的信息
    public To_Be_Contacted_Tomorrow_Adapter(Context context, List<Map<String, String>> list)
    {
        this.context = context;

        layoutInflater = LayoutInflater.from(context);

        this.list = list;
    }

    //得到总的数量
    public int getCount()
    {
        // TODO Auto-generated method stub
        return this.list!=null? this.list.size(): 0 ;
    }

    //根据ListView位置返回View
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return this.list.get(position);
    }

    //根据ListView位置得到List中的ID
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    //根据位置得到View对象
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.cust_collection_list, null);
        }

        //得到条目中的子组件
        TextView fcust_name = (TextView)convertView.findViewById(R.id.fcust_name);
        TextView fcust_type = (TextView)convertView.findViewById(R.id.fcust_type);
        TextView date = (TextView)convertView.findViewById(R.id.date);
        ImageView phone=(ImageView)convertView.findViewById(R.id.phone);

        //从list对象中为子组件赋值
        fcust_name.setText(list.get(position).get("fcuer_name").toString());
        fcust_type.setText(list.get(position).get("fMyMemo").toString());
        date.setText(list.get(position).get("sTime").toString());

        return convertView;
    }
}
