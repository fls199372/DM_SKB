package com.example.dm_skb.dao;

import com.example.dm_skb.bean.CompanyDetail;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.MyNewsType;
import com.example.dm_skb.bean.ResponseJsonCustDetail;
import com.example.dm_skb.bean.ResponseList;
import com.example.dm_skb.util.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.example.dm_skb.bean.MyNewsList;
import java.util.Map;

/**
 * Created by yangxiaoping on 16/9/19.
 */

public class NewsDao {
    /**
     * 消息公告
     */
    private String url1="mynews/getMyNewsType/";
    public void getNewsMain(String kv,final ResponseT<ResponseList<MyNewsType>> res){
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+url1+kv,
                new OkHttpClientManager.ResultCallback<ResponseList<MyNewsType>>()
                {
                    @Override
                    public void onResponse(ResponseList<MyNewsType> u)
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
     * 消息公告
     */
    private String url2="mynews/getMyNewsList/";
    public void getMyNewsList(String kv,final ResponseT<ResponseList<MyNewsList>> res){
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+url2+kv,
                new OkHttpClientManager.ResultCallback<ResponseList<MyNewsList>>()
                {
                    @Override
                    public void onResponse(ResponseList<MyNewsList> u)
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
