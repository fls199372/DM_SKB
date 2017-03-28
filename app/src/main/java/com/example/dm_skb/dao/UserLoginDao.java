package com.example.dm_skb.dao;

import com.example.dm_skb.bean.Login;
import com.example.dm_skb.bean.LoginJson;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.util.OkHttpClientManager;
import com.squareup.okhttp.Request;

import java.util.Map;

/**
 * Created by yangxiaoping on 16/9/19.
 */

public class UserLoginDao {
    private String verificationocde="user/UserLogin/";
    public void getFInfo(String kv,final ResponseT<LoginJson<Login>> res){
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+verificationocde+kv,
                new OkHttpClientManager.ResultCallback<LoginJson<Login>>()
                {
                    @Override
                    public void onResponse(LoginJson<Login> u)
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
    /**
     * 关于我及设置更新用户信息记录
     */
    private String url1="user/updateUserInfo?";
    public void getupdateUserInfo(Map<String, String> params, final ResponseT<RegisterJson<Register>> res){
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+url1,params,
                new OkHttpClientManager.ResultCallback<RegisterJson<Register>>()
                {
                    @Override
                    public void onResponse(RegisterJson<Register> u)
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
