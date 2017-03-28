package com.example.dm_skb.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.example.dm_skb.util.OkHttpClientManager.Param;
import com.example.dm_skb.util.OkHttpClientManager;
import com.umeng.analytics.MobclickAgent;
import com.example.dm_skb.bean.UserEntity;
import com.example.dm_skb.ui.adapter.UserAdapter;
import com.example.dm_skb.util.Common;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.message.PushAgent;
import com.zhy.autolayout.AutoLayoutActivity;
import com.umeng.analytics.MobclickAgent.EScenarioType;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by yangxiaoping on 16/8/31.
 */
/**
 * 带打开和关闭动画的(activity基类)
 */
public abstract class BaseActivity extends AutoLayoutActivity{//FragmentActivity
    public Handler mainHandler;
    public boolean processFlag = true; //默认可以点击
    public Context context;
    UserAdapter userAdapter;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setCatchUncaughtExceptions(true);
        BaseApplication.GetInstance().addActivity(this);// 添加自己到activity管理集合
        mainHandler = new Handler();
        context = this;
        PushAgent.getInstance(context).onAppStart();
        userAdapter=new UserAdapter(this);
        UserID();
        DisplayMetrics dm = new DisplayMetrics();

        dm = this.getApplicationContext().getResources().getDisplayMetrics();

        Common.screenWidth = dm.widthPixels;
        Common.screenHeight = dm.heightPixels;
        MobclickAgent.setScenarioType(context, EScenarioType.E_UM_NORMAL);

    }
    public void UserID()
    {
        try {
            List<UserEntity> userlist = userAdapter.listAll();
            if(userlist.size()==0)
            {
                return;
            }
            for (int i = userlist.size() - 1; i < userlist
                    .size(); i++) {
                UserEntity entity = userlist.get(i);
                Common.fuser_id = entity.getFuser_id();
                Common.fuser_name = entity.getFuser_name();
                Common.fuser_pwd = entity.getPassword();
                Common.id=entity.getId();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(context);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(context);
    }
    @Override
    protected void onDestroy() {
        BaseApplication.GetInstance().removeActivity(this);// 移除自己在activity管理集合
        super.onDestroy();
    }
    /**
     * 判断返回值是不是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
    public void postmainHandler(final String message){
        mainHandler.post(new Runnable() {

            @SuppressWarnings("unused")
            @Override
            public void run() {
                Toast toast=new Toast(context);
                if (toast != null)
                {
                    toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                    toast.show();
                } else
                {
                    toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        CharSequence inputStr = phoneNumber;
        //正则表达式

        String phone="^1[34578]\\d{9}$" ;


        Pattern pattern = Pattern.compile(phone);
        Matcher matcher = pattern.matcher(inputStr);


        if(matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    public static String isPassword(String mobiles){
        if(mobiles==null)
        {
            return "密码不能为空";
        }else if(mobiles.length()==0)
        {
            return "密码不能为空";
        }else if(mobiles.length()<6)
        {
            return "密码不能小于6位";
        }else  if(mobiles.length()>20)
        {
            return "密码不能大于20位";
        }else
        {
            return "1";
        }

    }
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
    /**
     * 判断邮箱是否合法
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
    public Param[] map2Params(Map<String, String> params)
    {
        if (params == null) return new Param[0];
        int size = params.size();
        OkHttpClientManager.Param[] res = new OkHttpClientManager.Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries)
        {
            res[i++] = new OkHttpClientManager.Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

}


