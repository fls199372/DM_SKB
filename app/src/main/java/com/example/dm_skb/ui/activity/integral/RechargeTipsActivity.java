package com.example.dm_skb.ui.activity.integral;

import android.os.Bundle;
import android.view.View;

import com.example.dm_skb.R;
import com.example.dm_skb.ui.base.BaseActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;

//import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by yangxiaoping on 16/9/10.
 * 充值提示
 */

public  class RechargeTipsActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rechargetipsmain);


    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return){
            finish();
        }else if(view.getId()==R.id.return1)
        {
            finish();
        }
    }

}
