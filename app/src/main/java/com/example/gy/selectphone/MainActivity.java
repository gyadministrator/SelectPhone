package com.example.gy.selectphone;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import model.Phone;
import mvp.MvpMainView;
import mvp.impl.MainPresenter;
import utils.NetWorkUtils;
import utils.ToastUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MvpMainView {
    private EditText input_phone;
    private Button btn_search;
    private TextView result_phone;
    private TextView result_province;
    private TextView result_type;
    private TextView result_carrier;
    private MainPresenter mainPresenter;
    private ProgressDialog progressDialog;
    private LinearLayout linearLayout;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (NetWorkUtils.checkNetworkState(MainActivity.this)) {
                    linearLayout.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout) findViewById(R.id.checkNetwork);
        input_phone = (EditText) findViewById(R.id.input_phone);
        btn_search = (Button) findViewById(R.id.btn_search);
        result_phone = (TextView) findViewById(R.id.result_phone);
        result_province = (TextView) findViewById(R.id.result_province);
        result_type = (TextView) findViewById(R.id.result_type);
        result_carrier = (TextView) findViewById(R.id.result_carrier);
        btn_search.setOnClickListener(this);
        input_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btn_search.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0) {
                    btn_search.setEnabled(false);
                    btn_search.setBackgroundColor(Color.rgb(212, 215, 217));
                } else {
                    btn_search.setEnabled(true);
                    btn_search.setBackgroundColor(Color.rgb(121, 103, 255));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //检查网络
        checkNetwork();
        mainPresenter = new MainPresenter(this);
        mainPresenter.attach(this);
    }

    @Override
    public void onClick(View view) {
        mainPresenter.searchPhoneInfo(input_phone.getText().toString(), this, linearLayout, handler);
    }

    //MvpMainView接口的方法
    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateView() {
        Phone phone = mainPresenter.getPhoneInfo();
        result_phone.setText(getString(R.string.phone) + phone.getTelString());
        result_carrier.setText(getString(R.string.carrier) + phone.getCarrier());
        result_type.setText(getString(R.string.type) + phone.getCatName());
        result_province.setText(getString(R.string.province) + phone.getProvince());

    }

    @Override
    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, "", "正在加载...", true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle("");
            progressDialog.setMessage("正在加载...");
        }
        progressDialog.show();
    }

    @Override
    public void hiddenLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void checkNetwork() {
        if (!NetWorkUtils.checkNetworkState(MainActivity.this)) {
            ToastUtils.show("么么哒，你的网络连接了吗？", this);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NetWorkUtils.setNetwork(MainActivity.this, linearLayout, handler);
                }
            });
        }
    }
}
