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

private static String dbURL="data/data/com.example.administrator.test/antivirus.db";//�������ݿ�
	/**
	 * ���ĳ��md5�Ƿ��ǲ���
	 * 
	 * @param md5
	 * @return null ����ɨ�谲ȫ
	 */
	public static String checkVirus(String md5, Context context) {
		String desc = null;


		createDBFile(context);
		// �򿪲������ݿ�
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
	 * �ж����ݿ��ļ��Ƿ����
	 * 
	 * @return
	 */
	public  static void createDBFile(Context context) {

		File file = new File(
				dbURL);
		if (!file.exists()){
			//�������ȴ����ļ���
			if (!file.getParentFile().exists()){
				if(!file.getParentFile().mkdirs()) {
					Log.e("createDBFile: ", "create failed��");
				}else {
					Log.e("createDBFile: ", "create succeess��");
				}
			}else {
				Log.e("createDBFile: ", "had��");
			}
			try {
				//�õ���Դ
				AssetManager am= context.getAssets();
				//�õ����ݿ��������
				InputStream is=am.open("antivirus.db");
				Log.e("test", is+"");
				//�������д��SDcard����
				FileOutputStream fos=new FileOutputStream(file);
				Log.e("test", "fos="+fos);
				//����byte����  ����1KBдһ��
				byte[] buffer=new byte[1024];
				int count = 0;
				while((count = is.read(buffer))>0){
					fos.write(buffer,0,count);
				}
				//���رվͿ�����
				fos.flush();
				fos.close();
				is.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * ��ȡ���ݿ�汾��
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
	 * �������ݿ�汾�ŵĲ���
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
	 * ���²������ݿ��API
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
	 * ɾ������api
	 * @param md5
	 */
	public static void delete(String md5){
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbURL, null, SQLiteDatabase.OPEN_READWRITE);
		String[] values={md5};
		db.delete("datable", "md5=?", values);
		db.close();
	}
	
	
	//public static void main(String[] args) {
		//add("����ľ����", "3a0c9fdcf83c8c039dd8838a614a2cf0");
	//}
}
