package com.example.dm_skb.wxapi;






import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dm_skb.bean.Integral;
import com.example.dm_skb.bean.IntegralJson;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.dao.IntegralDao;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.example.dm_skb.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler,ResponseT<RegisterJson<Register>> {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	TextView cemo;
	IntegralDao integralDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rechargetipsmain);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
		cemo=(TextView)findViewById(R.id.cemo);
		cemo.setText("本次充值金额:"+Common.amountOfMoney+"元,获得积"+Common
				.integral1+"分。\n账户积分余额:"+(Integer.parseInt(Common.integral1)+Integer.parseInt
				(Common
				.integral))+"积分");
		integralDao=new IntegralDao();
		integral();
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
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}
	@Override
	public void onReq(BaseReq req) {

	}
	@Override
	public void onResp(BaseResp resp) {
		if(resp.errCode==-2)
		{
			postmainHandler("积分充值取消");
			finish();

		}else if(resp.errCode==0)
		{

		}else
		{
			postmainHandler("积分充值失败");
			finish();

		}

	}
	public void getT(RegisterJson<Register> t) {
	}
	public void getError(String msg) {
		// TODO Auto-generated method stub
	}
	public void integral()
	{
		OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+"user/getUserScore/"+ Common.fuser_id,
				new OkHttpClientManager.ResultCallback<IntegralJson<Integral>>()
				{
					@Override
					public void onResponse(IntegralJson<Integral> u)
					{
						if(u!=null){
							Meta meta=u.getMeta();
							if(meta!=null && meta.getCode()==200){
								cemo.setText("本次充值金额:"+Common.amountOfMoney+"元,获得积分"+Common
										.integral1+"分。\n账户积分余额:"+u.getData().getfUsableScores()+"积分。");
							}else{

							}
						}
					}
					@Override
					public void onError(Request request, Exception e) {
					}
				});
	}
}