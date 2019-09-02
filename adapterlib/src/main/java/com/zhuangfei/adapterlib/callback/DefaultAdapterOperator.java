package com.zhuangfei.adapterlib.callback;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Liu ZhuangFei on 2019/9/3.
 */
public class DefaultAdapterOperator implements IAdapterOperator {
    Context context;
    public DefaultAdapterOperator(Context c){
        context=c;
    }
    @Override
    public boolean isVip() {
        return false;
    }

    @Override
    public void gotoVip() {
        Toast.makeText(context,"未安装怪兽课表!",Toast.LENGTH_SHORT).show();
    }
}
