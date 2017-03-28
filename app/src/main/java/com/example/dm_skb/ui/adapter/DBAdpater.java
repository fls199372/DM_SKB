package com.example.dm_skb.ui.adapter;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.dm_skb.ui.adapter.MetaData.User;
public abstract class DBAdpater {

	public DBHelper dbHelper;
	Context context;

	public DBAdpater(Context context) {
		this.context = context;
		dbHelper = new DBHelper(context, MetaData.DB_NAME, null, 6);
	}
	class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, MetaData.DB_NAME, null,5);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			String c_User = "create table " + User.TABLE_NAME + "(" + ""
					+ User.USER_ID + " text," + "" + User.USER_PWD + " text,"+User.USER_NAME+" " +
					"text,"+User.id+" text)";
			db.execSQL(c_User);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				//db.execSQL("drop table if exists " + Username.TABLE_NAME);
				db.execSQL("drop table if exists " + User.TABLE_NAME);
				onCreate(db);
		}
	}
}
