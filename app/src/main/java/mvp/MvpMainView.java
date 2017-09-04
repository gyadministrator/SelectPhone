package mvp;


/**
 * Created by Administrator on 2017/8/16.
 */

public interface MvpMainView extends MvpLoadingView {
   void showToast(String msg);
    void updateView();
}
