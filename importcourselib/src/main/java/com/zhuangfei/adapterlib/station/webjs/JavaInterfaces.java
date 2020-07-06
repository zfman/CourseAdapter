package com.zhuangfei.adapterlib.station.webjs;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JavaInterfaces {
    public static List<BaseJavaInterface> interfaces;

    public static BaseJavaInterface getInterfaces(String action){
        if(interfaces==null){
            interfaces=new ArrayList<>();
        }
        BaseJavaInterface baseJavaInterface=new DefaultJavaInterface();
        switch (action){
            case "toast":
                baseJavaInterface=new ToastJavaInterface();
                break;
            case "setTitle":
                baseJavaInterface=new SetTitleJavaInterface();
                break;
            case "putItem":
                baseJavaInterface=new PutItemJavaInterface();
                break;
            case "getItem":
                baseJavaInterface=new GetItemJavaInterface();
                break;
        }
        return baseJavaInterface;
    }
}
