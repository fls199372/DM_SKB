package com.example.dm_skb.ui.activity.searchcustomer;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dm_skb.R;
import com.example.dm_skb.bean.Register1;
import com.example.dm_skb.bean.RegisterJson1;
import com.example.dm_skb.dao.PictureDao;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.land.ImageUtils;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.widget.CustomProgress;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

/**
 * Created by yangxiaoping on 16/9/10.
 * 纠错名片拍照
 */

public  class CardPicturesActivity extends BaseActivity implements
        ResponseT<RegisterJson1<Register1>> {
    private TextView register_main;
    private File imagefile1 =null;// 拍的照片文件
    private File imagefile = null;// 拍的照片文件
    private String fileName;
    private File imagefilee;// 拍的照片文件
    private Bitmap photo = null;
    private Bitmap icon = null;
    private Vector<File> files = new Vector<File>();
    private String name;
    private ImageView positive;
    private ImageView opposite;
    private LinearLayout positive_layout;
    private LinearLayout opposite_layout;
    Dialog dialog;
    PictureDao pictureDao;
    String fcust_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_card_customer_main);
        register_main=(TextView)findViewById(R.id.register_main);
        register_main.setText("拍名片");
        File files = new File("sdcard/myImage");
        files.mkdirs();// 创建文件夹
        try {
            if(savedInstanceState != null) {
                //这里可以更新UI
                fileName = savedInstanceState.getString("fileUrl");//得到文件地址
            }
        } catch (Exception e) {

        }
        Bundle bundle=getIntent().getExtras();
        fcust_id=bundle.getString("fcust_id");
        positive=(ImageView) findViewById(R.id.positive);
        opposite=(ImageView) findViewById(R.id.opposite);
        positive.setVisibility(View.GONE);
        opposite.setVisibility(View.GONE);
        positive_layout=(LinearLayout) findViewById(R.id.positive_layout);
        opposite_layout=(LinearLayout)findViewById(R.id.opposite_layout);
        pictureDao=new PictureDao();
    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return) {
            finish();
        }else if(view.getId()==R.id.positive)
        {
            imagefile = new File(getPhotoFileName());// 拍的照片文件
            imagefile=new File(fileName);
            SharedPreferences preferences = getSharedPreferences("isfileName",
                    MODE_WORLD_READABLE);
            //实例化editor对象
            Editor editor=preferences.edit();
            //存入数据
            editor.putString("isFirstUse", ""+fileName);
            //提交修改
            editor.commit();
            openCamera();
        }else if(view.getId()==R.id.positive_layout)
        {
            imagefile = new File(getPhotoFileName());// 拍的照片文件
            imagefile=new File(fileName);
            SharedPreferences preferences = getSharedPreferences("isfileName",
                    MODE_WORLD_READABLE);
            //实例化editor对象
            Editor editor=preferences.edit();
            //存入数据
            editor.putString("isFirstUse", ""+fileName);
            //提交修改
            editor.commit();
            openCamera();
        }else if(view.getId()==R.id.opposite)
        {

            imagefile1= new File(getPhotoFileName());// 拍的照片文件
            imagefile1=new File(fileName);
            SharedPreferences preferences = getSharedPreferences("isfileName",
                    MODE_WORLD_READABLE);
            //实例化editor对象
            Editor editor=preferences.edit();
            //存入数据
            editor.putString("isFirstUse", ""+fileName);
            //提交修改
            editor.commit();
            openCamera1();
        }else if(view.getId()==R.id.opposite_layout)
        {

            imagefile1= new File(getPhotoFileName());// 拍的照片文件
            imagefile1=new File(fileName);
            SharedPreferences preferences = getSharedPreferences("isfileName",
                    MODE_WORLD_READABLE);
            //实例化editor对象
            Editor editor=preferences.edit();
            //存入数据
            editor.putString("isFirstUse", ""+fileName);
            //提交修改
            editor.commit();
            openCamera1();
        }else if(view.getId()==R.id.ok)
        {
            dialog= CustomProgress.show(CardPicturesActivity.this, "正在提交名片", false, null);
            Submit();
        }
    }
    public void Submit() {
        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main = new JSONObject();
        JSONArray filearray = new JSONArray();
        try {
            main.put("ComapnyId", "" + fcust_id);
            main.put("fUser_Id", "" + Common.fuser_id);//
            main.put("BillType", "1");//
            main.put("watermarkFlag", "0");//
            main.put("watermark", "");//
            main.put("compressFlag", "0");//

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (imagefile == null) {
            postmainHandler("请拍设名片正面照片");
            dialog.dismiss();
            return;
        }
        String[] fileKeys = new String[2];
        File[] files1 = new File[2];
        String[] fileKeys1 = new String[1];
        File[] files2 = new File[1];

        if (imagefile1 == null) {

            saveBitmapFile(ImageUtils.getImageFromPath(imagefile.getPath(), 100, 650), imagefile);
            fileKeys1[0] = imagefile.getName();
            files2[0] = imagefile;
            params.put("parameterdata", main.toString());//主表
            pictureDao.PictureUpload(params, this, map2Params(params), files2, fileKeys1, h);

        } else {

            saveBitmapFile(ImageUtils.getImageFromPath(imagefile.getPath(), 100, 650), imagefile);
            saveBitmapFile(ImageUtils.getImageFromPath(imagefile1.getPath(), 100, 650), imagefile1);
            files1[0] = imagefile;
            fileKeys[0] = imagefile.getName();
            fileKeys[1] = imagefile1.getName();
            files1[1] = imagefile1;
            params.put("parameterdata", main.toString());//主表
            pictureDao.PictureUpload(params, this, map2Params(params), files1, fileKeys, h);

        }
    }
    Object h;
    private void openCamera() {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            postmainHandler("SD卡不可用");
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagefile));
        startActivityForResult(intent, 1);
    }
    private void openCamera1() {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            postmainHandler("SD卡不可用");
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagefile1));
        startActivityForResult(intent, 2);
    }
    private String getPhotoFileName() {
        name = DateFormat.format("yyyyMMdd_hhmmss" + Common.fuser_id,
                Calendar.getInstance(Locale.CHINA)) + ".jpg";
        fileName = "sdcard/myImage/" + name;
        Common.fileName=fileName;
        return fileName;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == 1)
            {
                try{
                    // 读取ShareedPreferences中需要的数据
                    SharedPreferences preferences = getSharedPreferences("isfileName",
                            MODE_WORLD_READABLE);
                    Common.fileName=preferences.getString("isFirstUse",fileName);
                    photo = decodeBitmap(fileName);
                    files.add(imagefile);
                    positive_layout.setVisibility(View.GONE);
                    positive.setVisibility(View.VISIBLE);
                    positive.setImageBitmap(ImageUtils.getImageFromPath(imagefile.getPath(),100,750));
                    photo.recycle();
                    icon.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(requestCode == 2)
            {
                FileInputStream fis=null;
                try{
                    // 读取ShareedPreferences中需要的数据
                    SharedPreferences preferences = getSharedPreferences("isfileName",
                            MODE_WORLD_READABLE);
                    Common.fileName=preferences.getString("isFirstUse",fileName);
                    fis=new FileInputStream(Common.fileName);
                    files.add(imagefile1);
                    opposite.setVisibility(View.VISIBLE);
                    opposite_layout.setVisibility(View.GONE);
                    opposite.setImageBitmap(ImageUtils.getImageFromPath(imagefile1.getPath(),100,
                            750));
                    photo.recycle();
                    icon.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally
                {
                    try {
                        fis.close();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private Bitmap decodeBitmap(String path) {

        int angle=readPictureDegree(fileName);

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now
        Bitmap bitmap = BitmapFactory.decodeFile(path,newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 想要缩放的目标尺寸
        float hh = 480f;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = 320f;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(path, newOpts);
        // 压缩好比例大小后再进行质量压缩
//	        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        //三星手机拍照需要旋转的方法
        if(angle!=0)
        {
            Matrix matrix = new Matrix();
            matrix.reset();
            matrix.postRotate(angle);
            Bitmap nowBp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return nowBp;
        }else
        {
            return bitmap;
        }
    }
    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    private void addTxt() {

        int width = 320, hight = 480;
        icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888); // 建立一个空的BItMap
        photo.recycle();
        try {
            saveBitmapToFile(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveBitmapToFile(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;//
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(imagefile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace(); //
        }
    }
    public void saveBitmapFile(Bitmap bitmap,File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void getT(RegisterJson1<Register1> t){
        finish();
        dialog.dismiss();
        postmainHandler("名片提交成功!请等待审核");

    }
    public void getError(String msg) {
        // TODO Auto-generated method stub
        if(msg.equals("1"))
        {
            finish();
            postmainHandler("名片提交成功!请等待审核");
            dialog.dismiss();
        }else
        {
            dialog.dismiss();
            postmainHandler(""+msg);
        }

    }
}
