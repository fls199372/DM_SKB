package com.example.dm_skb.dao;

import com.example.dm_skb.bean.CompanyDetail;
import com.example.dm_skb.bean.Companycollection;

import com.example.dm_skb.bean.Companylist;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.ResponseList;
import com.example.dm_skb.bean.ResponseJsonCustDetail;
import com.example.dm_skb.util.OkHttpClientManager;
import com.squareup.okhttp.Request;

import java.util.Map;

/**
 * Created by yangxiaoping on 16/9/19.
 */

public class CusetDao {
    /**
     * 首页公司列表
     */
    private String url1="company/getCompanylist";
    public void getCompanylist(Map<String, String> params, final ResponseT<ResponseList<Companylist>> res){
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+url1,params,
                new OkHttpClientManager.ResultCallback<ResponseList<Companylist>>()
                {
                    @Override
                    public void onResponse(ResponseList<Companylist> u)
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
     * 公司收藏
     */
    private String url2="mycompany/getmyCompanylist/";
    public void getCompanycollection(String kv, final ResponseT<ResponseList<Companycollection>> res){
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+url2+kv,
                new OkHttpClientManager.ResultCallback<ResponseList<Companycollection>>()
                {
                    @Override
                    public void onResponse(ResponseList<Companycollection> u)
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
    private String getcompanyDetail="company/getCompanyDetail/";
    public void getCompanyDetail(String kv,final ResponseT<ResponseJsonCustDetail<CompanyDetail>> res){
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+getcompanyDetail+kv,
                new OkHttpClientManager.ResultCallback<ResponseJsonCustDetail<CompanyDetail>>()
                {
                    @Override
                    public void onResponse(ResponseJsonCustDetail<CompanyDetail> u)
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
