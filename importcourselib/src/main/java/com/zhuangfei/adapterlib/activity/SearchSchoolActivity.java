package com.zhuangfei.adapterlib.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuangfei.adapterlib.AdapterLibManager;
import com.zhuangfei.adapterlib.RecordEventManager;
import com.zhuangfei.adapterlib.StatManager;
import com.zhuangfei.adapterlib.activity.qingguo.XiquerLoginActivity;
import com.zhuangfei.adapterlib.apis.model.ParseJsModel;
import com.zhuangfei.adapterlib.apis.model.TemplateJsV2;
import com.zhuangfei.adapterlib.callback.DefaultAdapterOperator;
import com.zhuangfei.adapterlib.callback.IAdapterOperator;
import com.zhuangfei.adapterlib.callback.OnVersionFindCallback;
import com.zhuangfei.adapterlib.ParseManager;
import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.apis.model.SearchResultModel;
import com.zhuangfei.adapterlib.activity.adapter.SearchSchoolAdapter;
import com.zhuangfei.adapterlib.core.AssetTools;
import com.zhuangfei.adapterlib.station.IStationOperator;
import com.zhuangfei.adapterlib.station.IStationSearchOperator;
import com.zhuangfei.adapterlib.station.StationManager;
import com.zhuangfei.adapterlib.station.StationSdk;
import com.zhuangfei.adapterlib.station.model.GreenFruitSchool;
import com.zhuangfei.adapterlib.station.model.TinyConfig;
import com.zhuangfei.adapterlib.utils.GsonUtils;
import com.zhuangfei.adapterlib.utils.Md5Security;
import com.zhuangfei.adapterlib.utils.PackageUtils;
import com.zhuangfei.adapterlib.utils.SoftInputUtils;
import com.zhuangfei.adapterlib.utils.ViewUtils;
import com.zhuangfei.adapterlib.apis.TimetableRequest;
import com.zhuangfei.adapterlib.apis.model.AdapterResultV2;
import com.zhuangfei.adapterlib.apis.model.ListResult;
import com.zhuangfei.adapterlib.apis.model.ObjResult;
import com.zhuangfei.adapterlib.apis.model.School;
import com.zhuangfei.adapterlib.apis.model.StationModel;
import com.zhuangfei.adapterlib.apis.model.TemplateModel;
import com.zhuangfei.toolkit.model.BundleModel;
import com.zhuangfei.toolkit.tools.ActivityTools;
import com.zhuangfei.toolkit.tools.ShareTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchSchoolActivity extends AppCompatActivity implements OnCommonFunctionClickListener {

    private static final String TAG = "SearchSchoolActivity";
    protected Activity context;

    ListView searchListView;
    List<SearchResultModel> models;
    List<SearchResultModel> allDatas;
    SearchSchoolAdapter searchAdapter;
    List<TemplateModel> templateModels;
    String baseJs;

    EditText searchEditText;
    LinearLayout loadLayout;
    TextView versionDisplayTextView;

    boolean firstStatus=true;
    public static final int RESULT_CODE=10;
    public static final String EXTRA_SEARCH_KEY="key";
    public static final String EXTRA_STATION_OPERATOR="operator";

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    IStationOperator operator;
    List<GreenFruitSchool> allSchool;
    IAdapterOperator adapterOperator;

    TemplateJsV2 localTemplateJsV2;
    ParseJsModel parseJsModel;
    int nowTemplateVersion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_school);
        ViewUtils.setStatusTextGrayColor(this);
        initView();
        inits();
        loadSchools();
        StatManager.sendKVEvent(this,"pf_search_enter",null);

    }

    protected StationSdk getStationSdk(){
        return new StationSdk();
    }

    private void loadSchools() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String schoolStr= AssetTools.readAssetFile(getContext(),"schools.txt");
                TypeToken<List<GreenFruitSchool>> typeToken=new TypeToken<List<GreenFruitSchool>>(){};
                List<GreenFruitSchool> school=new Gson().fromJson(schoolStr,typeToken.getType());
                Message message=new Message();
                message.obj=school;
                message.what=0x123;
                handler.sendMessage(message);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.obj!=null){
                allSchool= (List<GreenFruitSchool>) msg.obj;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(ParseManager.isSuccess()){
            setResult(RESULT_CODE);
            finish();
        }
    }

    private void initView() {
        sp=getSharedPreferences("station_space_all", Context.MODE_PRIVATE);
        editor=sp.edit();
        searchListView=findViewById(R.id.id_search_listview);
        searchEditText=findViewById(R.id.id_search_edittext);
        loadLayout=findViewById(R.id.id_loadlayout);
        versionDisplayTextView=findViewById(R.id.id_version_display);
    }

    public void setLoadLayout(boolean isShow) {
        if (isShow) {
            loadLayout.setVisibility(View.VISIBLE);
        } else {
            loadLayout.setVisibility(View.GONE);
        }
    }

    private void inits() {
        context = this;
        ParseManager.clearCache();
        String localJson=sp.getString("TemplateJsV2",null);
        if(!TextUtils.isEmpty(localJson)){
            localTemplateJsV2=GsonUtils.getGson().fromJson(localJson,TemplateJsV2.class);
            if(localTemplateJsV2!=null){
                templateModels=localTemplateJsV2.getTemplate();
                baseJs=localTemplateJsV2.getBase();
            }
        }

        models = new ArrayList<>();
        allDatas=new ArrayList<>();
        searchAdapter = new SearchSchoolAdapter(this, allDatas,models,this);
        searchListView.setAdapter(searchAdapter);
        searchEditText.addTextChangedListener(textWatcher);

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onItemClicked(i);
            }
        });
        findViewById(R.id.id_search_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        findViewById(R.id.id_copyright).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse("https://github.com/zfman/CourseAdapter"));
                    context.startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(context,"跳转失败 https://github.com/zfman/CourseAdapter",Toast.LENGTH_SHORT).show();
                }
            }
        });
        String searchKey=getIntent().getStringExtra(EXTRA_SEARCH_KEY);
        if(!TextUtils.isEmpty(searchKey)){
            search("recommend://"+searchKey);
        }else{
            searchKey=ShareTools.getString(this,"lastCLicked",null);
            if(!TextUtils.isEmpty(searchKey)){
                search("recommend://"+searchKey);
            }else{
                search("recommend://");
            }
        }

        adapterOperator= getAdapterOperator();

        operator= (IStationOperator) getIntent().getSerializableExtra(EXTRA_STATION_OPERATOR);
        versionDisplayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse("https://github.com/zfman/CourseAdapter/wiki/%E7%89%88%E6%9C%AC%E5%8F%98%E6%9B%B4"));
                    context.startActivity(intent);
                }catch (Exception e){}
            }
        });

        String updateId="9f37c8171f4100a7ac585dcb702c7f64";
        AdapterLibManager.checkUpdate(context,updateId, new OnVersionFindCallback() {
            @Override
            public void onNewVersionFind(int newNumber, String newVersionName, String newVersionDesc) {
                versionDisplayTextView.setVisibility(View.VISIBLE);
                versionDisplayTextView.setText("发现新版本lib-v"+newVersionName+" "+newVersionDesc);
            }
        });
    }

    public void onItemClicked(int i) {
        final SearchResultModel model=models.get(i);
        if(model==null) return;
        //学校教务导入
        if(model.getType()==SearchResultModel.TYPE_SCHOOL){
            if(!checkTemplateJs()){
                requestTemplateJs(new OnDoActionListener() {
                    @Override
                    public void doActionAfter() {
                        handleItemClickedForSchool(model);
                    }
                });
            }else{
                handleItemClickedForSchool(model);
            }
        }
        else if(model.getType()==SearchResultModel.TYPE_XIQUER){
            onXuqerItemClicked(model);
        }
    }

    private void handleCommonParse(final List<TemplateModel> templateModels){
        if(templateModels!=null){
            String[] items=new String[templateModels.size()];
            for(int r=0;r<items.length;r++){
                items[r]=templateModels.get(r).getTemplateName();
            }
            SoftInputUtils.hideInput(getContext());
            AlertDialog.Builder builder=new AlertDialog.Builder(this)
                    .setTitle("选择通用模板")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TemplateModel templateModel = (TemplateModel) templateModels.get(i);
                            if (templateModel!=null){
                                handleCustomTemplate(templateModel);
                            }
                        }
                    });
            builder.create().show();
        }
    }

    private void handleCustomTemplate(TemplateModel templateModel){
        if(baseJs==null){
            Toast.makeText(getContext(),"基础函数库发生异常，请联系qq:1193600556",Toast.LENGTH_SHORT).show();
        }
        else {
            if(templateModel.getStat() != null){
                StatManager.sendKVEvent(getContext(),templateModel.getStat(),null);
            }
            toAdapterSameTypeActivity(templateModel.getTemplateName(),baseJs+templateModel.getTemplateJs());
        }
    }

    private void handleItemClickedForSchool(final SearchResultModel model){
        if(parseJsModel==null||parseJsModel.getParsejs()==null){
            School school = (School) model.getObject();
            if(school!=null){
                requestParseJs(school.getAid(), new OnDoActionListener() {
                    @Override
                    public void doActionAfter() {
                        realHandleItemClickedForSchool(model);
                    }
                });
            }
        }else{
            realHandleItemClickedForSchool(model);
        }
    }

    private void realHandleItemClickedForSchool(SearchResultModel model){
        School school = (School) model.getObject();
        if(school!=null&&parseJsModel!=null){
            ShareTools.putString(this,"lastCLicked",school.getSchoolName());

            Map<String,String> params=new HashMap<>();
            params.put("school",school.getSchoolName());
            StatManager.sendKVEvent(getContext(),"pf_jwdr",null);
            if(parseJsModel.getParsejs().startsWith("template/")){
                TemplateModel searchModel=searchInTemplate(templateModels,parseJsModel.getParsejs());
                if(baseJs==null){
                    Toast.makeText(this,"基础函数库发生异常，请联系qq:1193600556",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(searchModel!=null){
                    toAdapterSchoolActivity(school.getSchoolName(),
                            school.getUrl(),
                            school.getType(),
                            searchModel.getTemplateJs()+baseJs);
                }else {
                    Toast.makeText(this,"通用解析模板发生异常，请联系qq:1193600556",Toast.LENGTH_SHORT).show();
                }
            }else{
                toAdapterSchoolActivity(school.getSchoolName(),school.getUrl(),school.getType(),parseJsModel.getParsejs());
            }
        }
    }

    private void handleItemClickedForStation(SearchResultModel model){
        StationModel stationModel=(StationModel) model.getObject();
        getStationConfig((StationModel) model.getObject());
    }

    protected IAdapterOperator getAdapterOperator(){
        return new DefaultAdapterOperator();
    }

    public void onXuqerItemClicked(SearchResultModel model){

        ActivityTools.toActivityWithout(this, XiquerLoginActivity.class,
                new BundleModel()
                        .put("selectSchool",model.getObject()));
    }

    public void getStationConfig(final StationModel stationModel){
        String stationName=StationManager.getStationName(stationModel.getUrl());
        Log.d(TAG, "getStationConfig: "+stationName);
        if(TextUtils.isEmpty(stationName)) return;
        String config=sp.getString("config_"+stationModel.getStationId(),null);
        if(!TextUtils.isEmpty(config)){
            handleConfig(GsonUtils.getGson().fromJson(config,TinyConfig.class),stationModel);
            return;
        }
        setLoadLayout(true);
        TimetableRequest.getStationConfig(getContext(), stationName, new Callback<TinyConfig>() {
            @Override
            public void onResponse(Call<TinyConfig> call, Response<TinyConfig> response) {
                setLoadLayout(false);
                if(response!=null){
                    TinyConfig config=response.body();
                    handleConfig(config,stationModel);
                    if(config!=null){
                        editor.putString("config_"+stationModel.getStationId(), GsonUtils.getGson().toJson(config));
                        editor.commit();
                    }
                }else{
                    Toast.makeText(getContext(),"Error:response is null",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TinyConfig> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleConfig(TinyConfig config,StationModel stationModel) {
        if(config!=null){
            if(config.getSupport()> StationSdk.SDK_VERSION){
                Toast.makeText(getContext(),"版本太低，不支持本服务站，请升级新版本!",Toast.LENGTH_SHORT).show();
            }else{
                StationManager.openStationWithout(getContext(),config,stationModel,operator,getStationSdk());
            }
        }else{
            Toast.makeText(getContext(),"Error:config is null",Toast.LENGTH_SHORT).show();
        }
    }

    public void toAdapterSameTypeActivity(String type,String js){
        Intent intent=new Intent(this,AdapterSameTypeActivity.class);
        intent.putExtra(AdapterSameTypeActivity.EXTRA_TYPE,type);
        intent.putExtra(AdapterSameTypeActivity.EXTRA_JS,js);
        startActivity(intent);
    }

    public void toAdapterSchoolActivity(String school,String url,String type,String js){
        Intent intent=new Intent(this,AdapterSchoolActivity.class);
        intent.putExtra(AdapterSchoolActivity.EXTRA_URL,url);
        intent.putExtra(AdapterSchoolActivity.EXTRA_SCHOOL,school);
        intent.putExtra(AdapterSchoolActivity.EXTRA_TYPE,type);
        intent.putExtra(AdapterSchoolActivity.EXTRA_PARSEJS,js);
        intent.putExtra("operator",adapterOperator);
        startActivity(intent);
    }

    public TemplateModel searchInTemplate(List<TemplateModel> models,String tag){
        if(models==null||tag==null) return null;
        for(TemplateModel model:models){
            if(model!=null){
                if(tag.equals(model.getTemplateTag())){
                    return model;
                }
            }
        }
        return null;
    }

    public Activity getContext() {
        return context;
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String key = charSequence.toString();
            firstStatus=false;
            if (TextUtils.isEmpty(key)) {
                models.clear();
                allDatas.clear();
                searchAdapter.notifyDataSetChanged();
                search("recommend://");
            } else {
                search(charSequence.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void search() {
        String key = searchEditText.getText().toString();
        search(key);
    }

    public void search(final String key) {
        if(TextUtils.isEmpty(key)) {
            return;
        }

        Map<String,String> statMap=new HashMap<>();
        statMap.put("key",key);
        StatManager.sendKVEvent(getContext(),"pf_ssxx",statMap);

        models.clear();
        allDatas.clear();

        String packageMd5= PackageUtils.getPackageMd5(this);
        String appkey=AdapterLibManager.getAppKey();
        String time=""+System.currentTimeMillis();
        StringBuffer sb=new StringBuffer();
        sb.append("time="+time);
        String sign= Md5Security.encrypBy(sb.toString()+context.getResources().getString(R.string.md5_sign_key));
        if(TextUtils.isEmpty(packageMd5)||TextUtils.isEmpty(appkey)){
            Toast.makeText(context,"未初始化",Toast.LENGTH_SHORT).show();
            return;
        }

//        searchStation(key);
        if (!TextUtils.isEmpty(key)) {
            setLoadLayout(true);
            Map<String,String> params=new HashMap<>();
            params.put("key",key);
            StatManager.sendKVEvent(this,"pf_search_key",params);
            TimetableRequest.getAdapterSchoolsV2(this, key,packageMd5,appkey, time,sign,new Callback<ObjResult<AdapterResultV2>>() {
                @Override
                public void onResponse(Call<ObjResult<AdapterResultV2>> call, Response<ObjResult<AdapterResultV2>> response) {
                    ObjResult<AdapterResultV2> result = response.body();
                    if (result != null) {
                        if (result.getCode() == 200) {
                            showResult(result.getData(),key);
                        } else if(result.getCode()>=330&&result.getCode()<=400){
                            showDialog(result.getMsg());
                        }
                        else {
                            Toast.makeText(SearchSchoolActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SearchSchoolActivity.this, "school response is null!", Toast.LENGTH_SHORT).show();
                    }
                    setLoadLayout(false);
                }

                @Override
                public void onFailure(Call<ObjResult<AdapterResultV2>> call, Throwable t) {
                    setLoadLayout(false);
                }
            });
        }
    }

    public void showDialog(String msg){
        AlertDialog.Builder builder=new AlertDialog.Builder(this)
                .setTitle("校验失败")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        if(dialogInterface!=null){
                            dialogInterface.dismiss();
                        }
                    }
                });
        builder.create().show();
    }

    public void searchStation(final String key) {
        if (!TextUtils.isEmpty(key)) {
            setLoadLayout(true);
            TimetableRequest.getStations(this, key, new Callback<ListResult<StationModel>>() {
                @Override
                public void onResponse(Call<ListResult<StationModel>> call, Response<ListResult<StationModel>> response) {
                    setLoadLayout(false);
                    ListResult<StationModel> result = response.body();
                    if (result != null) {
                        if (result.getCode() == 200) {
                            showStationResult(result.getData(),key);
                        } else {
                            Toast.makeText(SearchSchoolActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SearchSchoolActivity.this, "school response is null!", Toast.LENGTH_SHORT).show();
                    }
                    setLoadLayout(false);
                }

                @Override
                public void onFailure(Call<ListResult<StationModel>> call, Throwable t) {
                    setLoadLayout(false);
                }
            });
        }
    }

    private void showStationResult(List<StationModel> result,String key) {
        if(key!=null&&key.indexOf("recommend://")!=-1){
        }else{
            if(!firstStatus&&searchEditText.getText()!=null&&key!=null&&!searchEditText.getText().toString().equals(key)){
                return;
            }
        }

        if (result == null) return;
        List<SearchResultModel> addList=new ArrayList<>();
        for (int i=0;i<Math.min(result.size(),SearchSchoolAdapter.TYPE_STATION_MAX_SIZE);i++) {
            StationModel model=result.get(i);
            SearchResultModel searchResultModel = new SearchResultModel();
            searchResultModel.setType(SearchResultModel.TYPE_STATION);
            if(result.size()>4){
                searchResultModel.setType(SearchResultModel.TYPE_STATION_MORE);
            }
            searchResultModel.setObject(model);
            addModelToList(searchResultModel);
        }

        for (int i=0;i<result.size();i++) {
            StationModel model=result.get(i);
            SearchResultModel searchResultModel = new SearchResultModel();
            searchResultModel.setType(SearchResultModel.TYPE_STATION);
            searchResultModel.setObject(model);
            addList.add(searchResultModel);
        }

        sortResult();
        addAllDataToList(addList);
        searchAdapter.notifyDataSetChanged();
    }

    /**
     *
     * @param result
     * @param key 用于校验输入框是否发生了变化，如果变化，则忽略
     */
    private void showResult(AdapterResultV2 result,String key) {
        if(key!=null&&key.indexOf("recommend://")!=-1){
        }else{
            if(!firstStatus&&searchEditText.getText()!=null&&key!=null){
                if(!searchEditText.getText().toString().equals(key)) return;
            }
        }
        if(result==null) return;

        Map<String,String> params=new HashMap<>();
        params.put("oldv",""+nowTemplateVersion);
        params.put("nowv",""+result.getTemplateVersion());
        StatManager.sendKVEvent(getContext(),"pf_tyjx_qq",params);

        nowTemplateVersion=result.getTemplateVersion();
        List<School> list=result.getSchoolList();
        if (list == null) {
            return;
        }

        SearchResultModel searchResultModel = new SearchResultModel();
        searchResultModel.setType(SearchResultModel.TYPE_COMMON);
        addModelToList(searchResultModel);

        if(allSchool!=null){
            for (GreenFruitSchool schoolBean : allSchool) {
                if (schoolBean.getXxmc() != null && schoolBean.getXxmc().indexOf(key) != -1) {
                    SearchResultModel searchResultModel2 = new SearchResultModel();
                    searchResultModel2.setType(SearchResultModel.TYPE_XIQUER);
                    searchResultModel2.setObject(schoolBean);
                    addModelToList(searchResultModel2);
                }
            }
        }

        for (School schoolBean : list) {
            SearchResultModel searchResultModel3 = new SearchResultModel();
            searchResultModel3.setType(SearchResultModel.TYPE_SCHOOL);
            searchResultModel3.setObject(schoolBean);
            addModelToList(searchResultModel3);
        }

        /*
        SearchResultModel searchResultModel = new SearchResultModel();
        searchResultModel.setType(SearchResultModel.TYPE_COMMON);
        TemplateModel addAdapterModel=new TemplateModel();
        addAdapterModel.setTemplateName("申请学校适配");
        addAdapterModel.setTemplateTag("custom/upload");
        searchResultModel.setObject(addAdapterModel);

        if(firstStatus||addAdapterModel.getTemplateName().indexOf(key)!=-1||result.getSchoolList().size()==0){
            addModelToList(searchResultModel);
        }

        SearchResultModel feedResultModel = new SearchResultModel();
        feedResultModel.setType(SearchResultModel.TYPE_COMMON);
        TemplateModel feedModel=new TemplateModel();
        feedModel.setTemplateName("申请学校适配");
        feedModel.setTemplateTag("custom/feedback");
        feedResultModel.setObject(feedModel);

        SearchResultModel searchResultModel2 = new SearchResultModel();
        searchResultModel2.setType(SearchResultModel.TYPE_COMMON);
        TemplateModel hezuoModel=new TemplateModel();
        hezuoModel.setTemplateName("商务合作");
        hezuoModel.setTemplateTag("custom/hezuo");
        searchResultModel2.setObject(hezuoModel);

        SearchResultModel searchResultModel3 = new SearchResultModel();
        searchResultModel3.setType(SearchResultModel.TYPE_COMMON);
        TemplateModel noResultTipModel=new TemplateModel();
        noResultTipModel.setTemplateName("未搜到你的学校? 查看解决方案");
        noResultTipModel.setTemplateTag("custom/guide");
        searchResultModel3.setObject(noResultTipModel);

        if(firstStatus||addAdapterModel.getTemplateName().indexOf(key)!=-1||result.getSchoolList().size()==0){
            addModelToList(feedResultModel);
        }
        if(firstStatus||hezuoModel.getTemplateName().indexOf(key)!=-1||result.getSchoolList().size()==0){
            addModelToList(searchResultModel2);
        }
        if(firstStatus||noResultTipModel.getTemplateName().indexOf(key)!=-1||result.getSchoolList().size()==0){
            addModelToList(searchResultModel3);
        }
        */

        sortResult();
        searchAdapter.notifyDataSetChanged();
    }

    public void sortResult() {
        if (models != null) {
            Collections.sort(models);
        }
    }

    public synchronized void addModelToList(SearchResultModel searchResultModel) {
        if (models != null) {
            models.add(searchResultModel);
        }
    }

    public synchronized void addAllDataToList(List<SearchResultModel> searchResultModels) {
        if (allDatas != null) {
            for(SearchResultModel model:searchResultModels){
                allDatas.add(model);
            }
        }
    }

    @Override
    public void onBackPressed() {
        StatManager.sendKVEvent(getContext(),"pf_jwdr_fh",null);
        finish();
    }

    @Override
    public void onCommonFunctionClicked(String key) {
        if(TextUtils.isEmpty(key)){
            return;
        }
        if(key.equals("common_import")){
            StatManager.sendKVEvent(getContext(),"pf_tyjx",null);
            if(!checkTemplateJs()){
                requestTemplateJs(new OnDoActionListener() {
                    @Override
                    public void doActionAfter() {
                        handleCommonParse(templateModels);
                    }
                });
            }else{
                handleCommonParse(templateModels);
            }
        }
        if(key.equals("camera_import")){

        }
        if(key.equals("scan_import")){
            StatManager.sendKVEvent(getContext(),"pf_smdr",null);
            Toast.makeText(getContext(),"暂未开放",Toast.LENGTH_SHORT).show();
        }
        if(key.equals("feedback")){
            StatManager.sendKVEvent(getContext(),"pf_wtfk",null);
            Intent intent= new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("https://support.qq.com/product/162820");
            intent.setData(content_url);
            startActivity(intent);
        }
        if(key.equals("upload")){
            StatManager.sendKVEvent(getContext(),"pf_sqsp",null);
            Intent intent=new Intent(getContext(),AdapterTipActivity.class);
            startActivity(intent);
        }
    }

    public boolean checkTemplateJs(){
        if(localTemplateJsV2==null) return false;
        if(localTemplateJsV2.getTemplateVersion()<nowTemplateVersion){
            return false;
        }
        if(localTemplateJsV2.getTemplate()==null||localTemplateJsV2.getTemplate().size()==0){
            return false;
        }
        return true;
    }

    public void requestTemplateJs(final OnDoActionListener doActionListener){
        TimetableRequest.getTemplateJs(this, new Callback<ObjResult<TemplateJsV2>>() {
            @Override
            public void onResponse(Call<ObjResult<TemplateJsV2>> call, Response<ObjResult<TemplateJsV2>> response) {
                ObjResult<TemplateJsV2> objResult=response.body();
                if(objResult!=null){
                    if (objResult.getCode() == 200) {
                        TemplateJsV2 templateJsV2=objResult.getData();
                        if(templateJsV2!=null){
                            baseJs=templateJsV2.getBase();
                            templateModels=templateJsV2.getTemplate();
                            String json=GsonUtils.getGson().toJson(templateJsV2);
                            editor.putString("TemplateJsV2",json);
                            editor.commit();
                            if(doActionListener!=null){
                                doActionListener.doActionAfter();
                            }
                        }
                    } else {
                        Toast.makeText(SearchSchoolActivity.this, objResult.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SearchSchoolActivity.this, "templatejs response is null!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ObjResult<TemplateJsV2>> call, Throwable t) {
                Toast.makeText(getContext(),"Fail:"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestParseJs(int aid,final OnDoActionListener doActionListener){
        TimetableRequest.getAdapterParseJs(this,aid, new Callback<ObjResult<ParseJsModel>>() {
            @Override
            public void onResponse(Call<ObjResult<ParseJsModel>> call, Response<ObjResult<ParseJsModel>> response) {
                ObjResult<ParseJsModel> objResult=response.body();
                if(objResult!=null){
                    if (objResult.getCode() == 200) {
                        parseJsModel=objResult.getData();
                        if(parseJsModel!=null){
                            if(doActionListener!=null){
                                doActionListener.doActionAfter();
                            }
                        }
                    } else {
                        Toast.makeText(SearchSchoolActivity.this, objResult.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SearchSchoolActivity.this, "templatejs response is null!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ObjResult<ParseJsModel>> call, Throwable t) {
                Toast.makeText(getContext(),"Fail:"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
