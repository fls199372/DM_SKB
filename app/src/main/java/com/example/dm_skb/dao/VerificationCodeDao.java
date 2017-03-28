package com.example.dm_skb.dao;



import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.util.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.example.dm_skb.bean.VerificationCode;
import com.example.dm_skb.bean.ResponseJsonCode;
/**
 * 验证码Dao包
 */

public class VerificationCodeDao {
    private String verificationocde="user/getMobileReqValidCode/";
    public void getFInfo(String kv,final ResponseT<ResponseJsonCode<VerificationCode>> res){
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+verificationocde+kv,
                new OkHttpClientManager.ResultCallback<ResponseJsonCode<VerificationCode>>()
                {
                    @Override
                    public void onResponse(ResponseJsonCode<VerificationCode> u)
                    {
                        if(res!=null ){
                            if(u!=null){
                                Meta meta=u.getMeta();
                                if(meta!=null && meta.getCode()==200){
                                    res.getT(u);
                                }else{
                                    res.getError(meta.getMsg());
                                }
                            }else{
                                res.getError("亲!网络异常!请稍后再试");
                            }
                        }
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        if(res!=null){
                            res.getError("亲!网络异常!请稍后再试");
                        }
                    }
                });
    }
}
