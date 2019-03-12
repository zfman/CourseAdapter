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

public class CustomPopWindow extends PopupWindow {
    private static final String TAG = "CustomPopWindow";
    private final View view;
    private Activity context;
    private View.OnClickListener itemClick;
    boolean haveLocal = false;

    public CustomPopWindow(Activity context, boolean haveLocal, View.OnClickListener itemClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popwindow_station, null);//alt+ctrl+f
        this.itemClick = itemClick;
        this.context = context;
        this.haveLocal = haveLocal;
        initView();
        initPopWindow();
    }


    private void initView() {
        TextView addHomeText = view.findViewById(R.id.pop_add_home);
        if (haveLocal) {
            addHomeText.setText("从首页移除");
        } else {
            addHomeText.setText("添加到首页");
        }
        addHomeText.setOnClickListener(itemClick);
        view.findViewById(R.id.pop_about).setOnClickListener(itemClick);
        view.findViewById(R.id.pop_to_home).setOnClickListener(itemClick);
        view.findViewById(R.id.pop_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
