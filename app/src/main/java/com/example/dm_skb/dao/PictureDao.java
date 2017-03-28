package com.example.dm_skb.dao;

import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register1;
import com.example.dm_skb.bean.RegisterJson1;
import com.example.dm_skb.util.OkHttpClientManager;
import com.squareup.okhttp.Request;

import java.io.File;
import java.util.Map;

/**
 * Created by yangxiaoping on 16/9/19.
 */

public class PictureDao {
    /**
     * 首页公司列表
     */
    private String url1="upload/UploadPic?";
    public void PictureUpload(Map<String, String> params, final
    ResponseT<RegisterJson1<Register1>> res,OkHttpClientManager.Param[] params1,File[] files,
                               String[] fileKeys,Object h){
        OkHttpClientManager.postAsyn1(OkHttpClientManager.BASE_URL+url1,fileKeys,
                files,params1,h,
                new OkHttpClientManager.ResultCallback<RegisterJson1<Register1>>()
                {
                    public void onResponse(RegisterJson1<Register1> u)
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
                            res.getError("1");
                        }
                    }
                });


    }

}
