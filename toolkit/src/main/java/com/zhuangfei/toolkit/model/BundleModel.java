package com.zhuangfei.toolkit.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liu ZhuangFei on 2018/2/7.
 */

public class BundleModel implements Serializable {

    private Class<?> fromClass;

    private int toItem=0;

    private int fromItem=0;

    private Map<String,Object> dataMap=new HashMap<>();

    public BundleModel setToItem(int toItem) {
        this.toItem = toItem;
        return this;
    }

    public BundleModel setFromClass(Class<?> fromClass) {
        this.fromClass = fromClass;
        return this;
    }

    public BundleModel setFromItem(int fromItem) {
        this.fromItem = fromItem;
        return this;
    }

    public int getToItem() {
        return toItem;
    }

    public Class<?> getFromClass() {
        return fromClass;
    }

    public int getFromItem() {
        return fromItem;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public BundleModel setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
        return this;
    }

    public Object get(String key){
        if(dataMap.containsKey(key)){
            return dataMap.get(key);
        }
        return null;
    }

    public Object get(String key,Object obj){
        if(dataMap.containsKey(key)){
            return dataMap.get(key);
        }
        return obj;
    }

    public BundleModel put(String key,String value){
        dataMap.put(key,value);
        return this;
    }

    public BundleModel put(String key,Object value){
        dataMap.put(key,value);
        return this;
    }

    public BundleModel() {
    }

    public BundleModel(Class<?> fromClass) {
        this.fromClass = fromClass;
    }

    public BundleModel(Class<?> fromClass, int toItem) {
        this.fromClass = fromClass;
        this.toItem = toItem;
    }

    public BundleModel(Class<?> fromClass, int toItem, int fromItem) {
        this.fromClass = fromClass;
        this.toItem = toItem;
        this.fromItem = fromItem;
    }

    public BundleModel(Class<?> fromClass, int toItem, int fromItem, Map<String, Object> dataMap) {
        this.fromClass = fromClass;
        this.toItem = toItem;
        this.fromItem = fromItem;
        this.dataMap = dataMap;
    }
}
