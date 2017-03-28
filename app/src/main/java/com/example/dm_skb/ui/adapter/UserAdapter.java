package com.example.dm_skb.ui.adapter;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.dm_skb.ui.adapter.MetaData.User;
import com.example.dm_skb.bean.UserEntity;
import java.util.ArrayList;
import java.util.List;
public class UserAdapter extends DBAdpater {
	public UserAdapter(Context context) {
		super(context);
	}
	public void addUser(String userid,String pwd,String fuser_name,String id){
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		String sql="insert into "+User.TABLE_NAME+"("+User.USER_ID+","+User.USER_PWD+","+User
				.USER_NAME+","+User.id+") values " +
				"('"+userid+"','"+pwd+"','"+fuser_name+"','"+id+"')";
		db.execSQL(sql);
		db.close();
	}
	public void deleteByUserid(String userGid){
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		db.delete(User.TABLE_NAME,User.USER_ID+"='"+userGid+"'",null);
	}
	public List<UserEntity> listAll(){
		List<UserEntity> userlist=new ArrayList<UserEntity>();
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor cursor=db.query(User.TABLE_NAME, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			String fuser_id=cursor.getString(cursor.getColumnIndex(User.USER_ID));
			String fuser_pwd=cursor.getString(cursor.getColumnIndex(User.USER_PWD));
			String fuser_name=cursor.getString(cursor.getColumnIndex(User.USER_NAME));
			String id=cursor.getString(cursor.getColumnIndex(User.id));

			UserEntity entity=new UserEntity(fuser_id,fuser_pwd,fuser_name,id);
			userlist.add(entity);
		}
		return userlist;
	}

}
