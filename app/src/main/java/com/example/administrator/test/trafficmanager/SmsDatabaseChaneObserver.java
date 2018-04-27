package com.example.administrator.test.trafficmanager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


public class SmsDatabaseChaneObserver extends ContentObserver {

	 // ֻ����ռ���
    public static final Uri MMSSMS_ALL_MESSAGE_URI = Uri.parse("content://sms/inbox");
    public static final int MESSAGE=0x010;
    public static final String SORT_FIELD_STRING = "_id asc";  // ����
    public static final String DB_FIELD_ID = "_id";
    public static final String DB_FIELD_ADDRESS = "address";
    public static final String DB_FIELD_PERSON = "person";
    public static final String DB_FIELD_BODY = "body";
    public static final String DB_FIELD_DATE = "date";
    public static final String DB_FIELD_TYPE = "type";
    public static final String DB_FIELD_THREAD_ID = "thread_id";
    public static final String[] ALL_DB_FIELD_NAME = {
            DB_FIELD_ID, DB_FIELD_ADDRESS, DB_FIELD_PERSON, DB_FIELD_BODY,
            DB_FIELD_DATE, DB_FIELD_TYPE, DB_FIELD_THREAD_ID };
    public static int mMessageCount = -1;
    
    private static final long DELTA_TIME = 60 * 1000;
    private ContentResolver mResolver;
    private Handler handler;
    
//    private BlackNumberDao blackNumberDao;
    public SmsDatabaseChaneObserver(ContentResolver resolver, Handler handler, Context context) {
        super(handler);
        mResolver = resolver;
        this.handler=handler;
//        blackNumberDao=new BlackNumberDao(context);
    }

    @Override
    public void onChange(boolean selfChange) {
        onReceiveSms();
    }

    private void onReceiveSms() {
        Cursor cursor = null;
        // ����쳣��׽
        try {
            cursor = mResolver.query(MMSSMS_ALL_MESSAGE_URI, ALL_DB_FIELD_NAME,
                    null, null, SORT_FIELD_STRING);
            final int count = cursor.getCount();
            if (count <= mMessageCount) {
                mMessageCount = count;
                return;
            }
            // �����ռ���Ķ�������Ŀ��֮ǰ�����Ϊ�Ǹս��յ��¶���---����������⣬������
            // ͬʱ��Ϊid����������¼Ϊ�ո��¼���Ķ��ŵ�id---���������������ģ����ֲ�һ���������ʱ�����ҲҪ��������
            mMessageCount = count;
           
            if (cursor != null) {
                cursor.moveToLast();
                final long smsdate = Long.parseLong(cursor.getString(cursor.getColumnIndex(DB_FIELD_DATE)));
                final long nowdate = System.currentTimeMillis();
                // �����ǰʱ��Ͷ���ʱ��������60��,��Ϊ����������Ч
                if (nowdate - smsdate > DELTA_TIME) {
                    return;
                }
                final String strAddress = cursor.getString(cursor.getColumnIndex(DB_FIELD_ADDRESS));    // ���ź���
                final String strbody = cursor.getString(cursor.getColumnIndex(DB_FIELD_BODY));          // �������ȡ������Ϣ
                final int smsid = cursor.getInt(cursor.getColumnIndex(DB_FIELD_ID));
                if (TextUtils.isEmpty(strAddress) || TextUtils.isEmpty(strbody)) {
                    return;
                }
//              String tag=  blackNumberDao.findBlockMode(strAddress);
                if(strAddress.equals("10086")){
                	Message message= Message.obtain();
                	message.obj=strbody;
                	message.what=MESSAGE;
                	handler.sendMessage(message);
                }
//                else if(tag.equals("1")){//�������еĶ�������
//                	mResolver.delete(MMSSMS_ALL_MESSAGE_URI, "_id="+smsid, null);
//                }
                // �õ����ź��������֮�������ش���
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {  // �п���cursor��û�д����ɹ�
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}