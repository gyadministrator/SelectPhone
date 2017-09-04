package mvp.impl;

import android.content.Context;

/**
 * Created by Administrator on 2017/8/16.
 */

public class BasePresenter {
    protected Context mContext;
    public void attach(Context context) {
        mContext=context;
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onDestroy() {
        mContext=null;
    }
}
