package com.zhuangfei.adapterlib.activity.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.utils.PackageUtils;

public class TokenImportPopWindow extends PopupWindow {
    private static final String TAG = "TokenImportPopWindow";
    private final View view;
    private Activity context;
    private View.OnClickListener itemClick;
    TextView tv_guaishou_install;
    TextView tv_guaishou_install2;

    public TokenImportPopWindow(Activity context, View.OnClickListener itemClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popwindow_token_import, null);//alt+ctrl+f
        this.itemClick = itemClick;
        this.context = context;
        initView();
        initPopWindow();
    }


    private void initView() {
        tv_guaishou_install=view.findViewById(R.id.tv_guaishou_install);
        LinearLayout guaishouLayout=view.findViewById(R.id.ll_btn_guaishou);
        guaishouLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean install=PackageUtils.isInstallApk(context,"com.zhuangfei.hputimetable");
                if(install){
                    tv_guaishou_install.setText("已安装");
                }else{
                    tv_guaishou_install.setText("未安装");
                }
            }
        });

        tv_guaishou_install2=view.findViewById(R.id.tv_guaishou_install2);
        LinearLayout guaishouLayout2=view.findViewById(R.id.ll_btn_guaishou2);
        guaishouLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean install=PackageUtils.isInstallApk(context,"com.zhuangfei.apk");
                if(install){
                    tv_guaishou_install2.setText("已安装");
                }else{
                    tv_guaishou_install2.setText("未安装");
                }
            }
        });

    }

    private void initPopWindow() {
        this.setContentView(view);
        // 设置弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击()
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00FFFFFF);
        //设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha(context, 0.5f);//0.0-1.0
    }

    /**
     * 设置添加屏幕的背景透明度(值越大,透明度越高)
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}
