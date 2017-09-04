package utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/8/25.
 */

public class ToastUtils {
    private static Toast mToast;
    public static void show(String msg,Context context){
        if (mToast==null){
            mToast=Toast.makeText(context,msg,Toast.LENGTH_LONG);
        }
        mToast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        mToast.show();
    }
}
