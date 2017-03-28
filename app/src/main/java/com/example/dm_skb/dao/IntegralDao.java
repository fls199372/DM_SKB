package com.example.dm_skb.dao;

import com.example.dm_skb.bean.Integral;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.IntegralJson;
import com.example.dm_skb.bean.IntegralList;
import com.example.dm_skb.bean.RechargeQusery;
import com.example.dm_skb.bean.ResponseList;
import com.example.dm_skb.util.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.example.dm_skb.bean.ProductList;
/**
 * Created by yangxiaoping on 16/9/22.
 */

public class IntegralDao {
    private String getUserScore="user/getUserScore/";
    public void getIntegral(String kv,final ResponseT<IntegralJson<Integral>> res){
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+getUserScore+kv,
                new OkHttpClientManager.ResultCallback<IntegralJson<Integral>>()
                {
                    @Override
                    public void onResponse(IntegralJson<Integral> u)
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
    //积分报表查询
    private String integralList="user/getIntegralList/";
    public void getIntegralList(String kv,final ResponseT<ResponseList<IntegralList>> res){
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+integralList+kv,
                new OkHttpClientManager.ResultCallback<ResponseList<IntegralList>>()
                {
                    @Override
                    public void onResponse(ResponseList<IntegralList> u)
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
    //积分报表查询
    private String rechargeQusery="user/getIntegralList/";
    public void getRechargeQusery(String kv,final ResponseT<ResponseList<RechargeQusery>> res){
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+rechargeQusery+kv,
                new OkHttpClientManager.ResultCallback<ResponseList<RechargeQusery>>()
                {
                    @Override
                    public void onResponse(ResponseList<RechargeQusery> u)
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



    //积分报表查询
    private String productList="product/getProductList/";
    public void getProductList(String kv,final ResponseT<ResponseList<ProductList>> res){
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+productList+kv,
                new OkHttpClientManager.ResultCallback<ResponseList<ProductList>>()
                {
                    @Override
                    public void onResponse(ResponseList<ProductList> u)
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
