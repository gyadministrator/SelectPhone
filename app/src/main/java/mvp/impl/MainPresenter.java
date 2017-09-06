package mvp.impl;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.gy.selectphone.MainActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import model.Phone;
import mvp.MvpMainView;
import utils.HttpUtils;
import utils.NetWorkUtils;
import utils.PhoneUtils;
import utils.ToastUtils;

/**
 * Created by Administrator on 2017/8/16.
 */

public class MainPresenter extends BasePresenter {
    private MvpMainView mvpMainView;
    private String mUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
    private Phone mPhone;

    public MainPresenter(MvpMainView mainView) {
        mvpMainView = mainView;
    }

    public Phone getPhoneInfo() {
        return mPhone;
    }

    public void searchPhoneInfo(String phone, final Context context, View view) {
        //判断手机号码是否合法
        if (!PhoneUtils.isMobileNO(phone)) {
            mvpMainView.showToast("手机号码格式不对");
            return;
        }
        if (NetWorkUtils.checkNetworkState(context)) {
            view.setVisibility(View.GONE);
            mvpMainView.showLoading();
            //写上http请求的处理逻辑
            sendHttp(phone);
        } else {
            ToastUtils.show("请先设置网络,再查询", context.getApplicationContext());
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    NetWorkUtils.setNetwork(mContext);
                }
            });
        }
    }

    private void sendHttp(String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("tel", phone);
        HttpUtils httpUtils = new HttpUtils(new HttpUtils.HttpResponse() {
            @Override
            public void onSuccess(Object object) {
                String json = object.toString();
                int index = json.indexOf("{");
                json = json.substring(index, json.length());
                //JSONObject
                mPhone = parseModelWithOrgJson(json);
                //Gson
                mPhone = parseModelWithGson(json);
                //FastJson
                mPhone = parseModelWithFastJson(json);

                mvpMainView.hiddenLoading();
                mvpMainView.updateView();
            }

            @Override
            public void onFail(String error) {
                mvpMainView.showToast(error);
                mvpMainView.hiddenLoading();
            }
        });
        httpUtils.sendGetHttp(mUrl, map);
    }

    private Phone parseModelWithOrgJson(String json) {
        Phone phone = new Phone();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String value = jsonObject.getString("telString");
            phone.setTelString(value);

            value = jsonObject.getString("province");
            phone.setProvince(value);

            value = jsonObject.getString("catName");
            phone.setCatName(value);

            value = jsonObject.getString("carrier");
            phone.setCarrier(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return phone;
    }

    private Phone parseModelWithGson(String json) {
        Gson gson = new Gson();
        Phone phone = gson.fromJson(json, Phone.class);
        return phone;
    }

    private Phone parseModelWithFastJson(String json) {
        Phone phone = com.alibaba.fastjson.JSONObject.parseObject(json, Phone.class);
        return phone;
    }
}
