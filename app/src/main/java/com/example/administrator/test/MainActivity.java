package com.example.administrator.test;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.test.activity.RublishcleanActivity;
import com.example.administrator.test.adapter.HomeAdapter;
import com.example.administrator.test.antitheft.AntiTheftActivity;
import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.blacknumber.BlackNumberActivity;
import com.example.administrator.test.trafficmanager.TrafficManagerActivity;
import com.example.administrator.test.util.Md5Utils;
import com.example.administrator.test.util.SPUtils;
import com.example.administrator.test.virus.AntiVirusActivity;

import static com.example.administrator.test.MyApplication.PREF_PASSWORD;

public class MainActivity extends BaseActivity {


    @BindView(R.id.gv_fuction)
    GridView gvFuction;

    private int[] mImgIds;
    private String[] mGvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setMyTitle("PuppyPhone");
        hideTitleNavigationButton();
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        mGvItems = new String[]{"�ֻ�����", "��ȫ�ٿ�", "������", "��������", "������ɱ", "����ͳ��", "�û�����", "�ֻ�����", "�Ƴ���¼"};
        mImgIds = new int[]{R.mipmap.home_safe,
                R.mipmap.home_safe, R.mipmap.home_safe,
                R.mipmap.home_safe, R.mipmap.home_safe,
                R.mipmap.home_safe, R.mipmap.home_safe,
                R.mipmap.home_safe, R.mipmap.home_safe};
        gvFuction.setAdapter(new HomeAdapter(this,mGvItems,mImgIds));

        gvFuction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0://�ֻ�����
                            startActivity(AntiTheftActivity.class);
                            break;
                        case 1://��ȫ�ٿ�
                            break;
                        case 2://������
                            startActivity(BlackNumberActivity.class);
                            break;
                        case 3://��������
                             startActivity(RublishcleanActivity.class);
                            break;
                        case 4://������ɱ
                            startActivity(AntiVirusActivity.class);
                            break;
                        case 5://����ͳ��
                            startActivity(TrafficManagerActivity.class);
                            break;
                        case 6://�û�����
                            break;
                        case 7://�ֻ�����
                            break;
                        case 8://�˳���¼
                            break;
                            default:
                    }
            }
        });
    }

    /**
     * ��ʾ�ֻ���������
     */
    private void showSafeDialog() {
        String password = SPUtils.getInstance().getString(PREF_PASSWORD);

        //�ж��Ƿ��������
        if (!TextUtils.isEmpty(password)) {
            //��ʾ�������뵯��
            showInputPasswordDialog();
        } else {
            //��ʾ�������뵯��
            showSetPasswordDialog();
        }

    }

    /**
     * ��������ĵ���
     */
    private void showInputPasswordDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.dialog_input_password, null);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString().trim();
                //��ȡ���������
                String savedPassword = SPUtils.getInstance().getString(PREF_PASSWORD);

                //�ж������Ƿ�Ϊ��
                if (!TextUtils.isEmpty(password)) {
                    if (Md5Utils.encode(password).equals(savedPassword)) {
                        startActivity(AntiVirusActivity.class);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this,"�������",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,"���벻��Ϊ��",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }

    /**
     * ��������ĵ���
     */
    private void showSetPasswordDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.dialog_set_password, null);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
        final EditText etPasswordConfirm = (EditText) view.findViewById(R.id.et_password_confirm);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString().trim();
                String passwordConfirm = etPasswordConfirm.getText().toString().trim();

                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConfirm)) {
                    if (password.equals(passwordConfirm)) {
                        SPUtils.getInstance().put(PREF_PASSWORD, Md5Utils.encode(password));
                        startActivity(AntiVirusActivity.class);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this,"���벻һ��",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,"���벻��Ϊ��",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }

    private void startActivity(Class cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }
}
