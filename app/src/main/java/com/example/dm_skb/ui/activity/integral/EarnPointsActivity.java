package com.example.dm_skb.ui.activity.integral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dm_skb.R;
import com.example.dm_skb.ui.activity.main.StartActivity;
import com.example.dm_skb.ui.activity.newcustomer.NewCustomerActivity;
import com.example.dm_skb.ui.base.BaseActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.example.dm_skb.ui.activity.newcustomer.CardPicturesActivity;
/**
 * Created by yangxiaoping on 16/9/10.
 * 赚积分
 */

public  class EarnPointsActivity extends BaseActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.earnpointsmain);
    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return){
            finish();
        }else if(view.getId() == R.id.report)
        {
            startActivity(new Intent(EarnPointsActivity.this, NewCustomerActivity.class));
        }else if(view.getId() == R.id.evaluate){
            Intent intent=new Intent(EarnPointsActivity.this,StartActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("id", "2");
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
        }else if(view.getId() == R.id.correction){
            Intent intent=new Intent(EarnPointsActivity.this,StartActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("id", "3");
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
        }else if(view.getId()==R.id.card)
        {
            startActivity(new Intent(EarnPointsActivity.this, CardPicturesActivity.class));
        }

    }

}
