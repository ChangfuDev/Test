package com.example.administrator.test.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

//select desc from datable where md5='a2bd62c89207956348986bf1357dea01'

public class AntiVirusDao {
	public AntiVirusDao() {
		
	}

private static String dbURL="data/data/com.example.administrator.test/antivirus.db";//病毒数据库
	/**
	 * 检查某个md5是否是病毒
	 * 
	 * @param md5
	 * @return null 代表扫描安全
	 */
	public static String checkVirus(String md5, Context context) {
		String desc = null;


		createDBFile(context);
		// 打开病毒数据库
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				dbURL, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select desc from datable where md5=?",
				new String[] { md5 });
		if (cursor.moveToNext()) {
			desc = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return desc;
	}

	/**
	 * 判断数据库文件是否存在
	 * 
	 * @return
	 */
	public  static void createDBFile(Context context) {

		File file = new File(
				dbURL);
		if (!file.exists()){
			//不存在先创建文件夹
			if (!file.getParentFile().exists()){
				if(!file.getParentFile().mkdirs()) {
					Log.e("createDBFile: ", "create failed！");
				}else {
					Log.e("createDBFile: ", "create succeess！");
				}
			}else {
				Log.e("createDBFile: ", "had！");
			}
			try {
				//得到资源
				AssetManager am= context.getAssets();
				//得到数据库的输入流
				InputStream is=am.open("antivirus.db");
				Log.e("test", is+"");
				//用输出流写到SDcard上面
				FileOutputStream fos=new FileOutputStream(file);
				Log.e("test", "fos="+fos);
				//创建byte数组  用于1KB写一次
				byte[] buffer=new byte[1024];
				int count = 0;
				while((count = is.read(buffer))>0){
					fos.write(buffer,0,count);
				}
				//最后关闭就可以了
				fos.flush();
				fos.close();
				is.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取数据库版本号
	 * 
	 * @return
	 */
	public static String getDBVersionNum() {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				dbURL, null,
				SQLiteDatabase.OPEN_READONLY);
		String versionnumber = "0";
		Cursor cursor = db.rawQuery("select  subcnt from version", null);
		if (cursor.moveToNext()) {
			versionnumber = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return versionnumber;
	}
	/**
	 * 更新数据库版本号的操作
	 * @param newversion
	 */
	public static void updateDBVersion(int newversion){
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				"", null,
				SQLiteDatabase.OPEN_READWRITE);
		String versionnumber = "0";
		ContentValues values = new ContentValues();
		values.put("subcnt", newversion);
		db.update("version", values, null, null);
		db.close();
	}
	/**
	 * 更新病毒数据库的API
	 * @param desc
	 * @param md5
	 */
	public static void add(String desc, String md5){
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbURL, null, SQLiteDatabase.OPEN_READWRITE);
		ContentValues values = new ContentValues();
		values.put("md5", md5);
		values.put("desc", desc);
		values.put("type", 6);
		values.put("name", "Android.Hack.i22hkt.a");
		db.insert("datable", null, values);
		db.close();
	}
	
	
	/**
	 * 删除病毒api
	 * @param md5
	 */
	public static void delete(String md5){
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbURL, null, SQLiteDatabase.OPEN_READWRITE);
		String[] values={md5};
		db.delete("datable", "md5=?", values);
		db.close();
	}
	
	
	//public static void main(String[] args) {
		//add("超级木马病毒", "3a0c9fdcf83c8c039dd8838a614a2cf0");
	//}
}
