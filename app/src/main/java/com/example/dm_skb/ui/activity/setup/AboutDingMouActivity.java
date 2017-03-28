package com.example.dm_skb.ui.activity.setup;

import android.os.Bundle;
import android.view.View;

import com.example.dm_skb.R;
import com.example.dm_skb.ui.base.BaseActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;

//import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by yangxiaoping on 16/9/10.
 * 关于鼎谋
 */

public  class AboutDingMouActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aboutdingmoumain);


    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return){
            finish();
        }
    }

}
