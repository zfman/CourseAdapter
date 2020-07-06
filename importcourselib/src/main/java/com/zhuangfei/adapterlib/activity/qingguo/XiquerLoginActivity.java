package com.zhuangfei.adapterlib.activity.qingguo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhuangfei.adapterlib.ParseManager;
import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.RecordEventManager;
import com.zhuangfei.adapterlib.StatManager;
import com.zhuangfei.adapterlib.apis.TimetableRequest;
import com.zhuangfei.adapterlib.apis.model.GreenFruitCourse;
import com.zhuangfei.adapterlib.apis.model.GreenFruitProfile;
import com.zhuangfei.adapterlib.apis.model.GreenFruitTerm;
import com.zhuangfei.adapterlib.core.ParseResult;
import com.zhuangfei.adapterlib.station.model.GreenFruitSchool;
import com.zhuangfei.adapterlib.utils.GsonUtils;
import com.zhuangfei.adapterlib.utils.TimetableUtils;
import com.zhuangfei.adapterlib.utils.ViewUtils;
import com.zhuangfei.toolkit.tools.ActivityTools;
import com.zhuangfei.toolkit.tools.BundleTools;
import com.zhuangfei.toolkit.tools.ShareTools;
import com.zhuangfei.toolkit.tools.ToastTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登录页面
 */
public class XiquerLoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginButton;

    EditText userName;
    EditText userPassword;

    TextView userSchool;

    LinearLayout loadingLayout;
    LinearLayout termLayout;

    GreenFruitSchool selectSchool=null;

    ListView listView;
    List<String> list;
    GreenFruitTerm termList;
    ArrayAdapter<String> adapter;

    GreenFruitProfile greenFruitProfile;

    TextView titleText;
    TextView loadingTextView;

    int number=0;
    int number2=0;
    List<GreenFruitCourse> courseList;
    Map<String,String> courseMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.setTransparent(this);
        setContentView(R.layout.activity_xiquer_login);
        initView();
        initEvent();
    }

    private void initEvent() {
        loginButton.setOnClickListener(this);
    }

    private void initView() {

        userSchool=findViewById(R.id.user_select_school);
        termLayout=findViewById(R.id.id_termlayout);
        listView=findViewById(R.id.id_term_listview);
        titleText=findViewById(R.id.id_term_title);
        loadingTextView=findViewById(R.id.id_loading_text);


        loginButton = (Button) findViewById(R.id.login);
        userName = (EditText) findViewById(R.id.user_name);
        userPassword = (EditText) findViewById(R.id.user_password);
        loadingLayout=findViewById(R.id.id_loadlayout);

        list=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                termLayout.setVisibility(View.GONE);
                GreenFruitTerm.XnxqBean term=termList.getXnxq().get(i);
                getCourses(term);
            }
        });

        findViewById(R.id.user_select_school_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectSchoolClicked();
            }
        });

        Object obj= BundleTools.getObject(this,"selectSchool",null);
        if(obj!=null&&obj instanceof GreenFruitSchool){
            selectSchool= (GreenFruitSchool) obj;
            userSchool.setText(selectSchool.getXxmc());
            Map<String,String> params=new HashMap<>();
            params.put("school",selectSchool.getXxmc());
            StatManager.sendKVEvent(this,"pf_xiquer",params);
        }
    }

    @Override
    public void onClick(View arg0) {
        if(arg0.getId()==R.id.login){
            login();
        }
    }

    public Context getContext(){
        return this;
    }

    /**
     * 登录请求服务器
     */
    private void login() {
        try{
            View view = getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }catch (Exception e){}

        final String name = userName.getText().toString();
        final String pw = userPassword.getText().toString();
        if (selectSchool==null) {
            ToastTools.show(this, "请选择学校");
            return;
        }
        if (name.isEmpty() || pw.isEmpty()) {
            ToastTools.show(this, "不可以为空");
            return;
        }

        number=0;
        number2=0;
        courseMap=new HashMap<>();
        courseList=new ArrayList<>();
        loadingTextView.setText("加载中...");
        loadingLayout.setVisibility(View.VISIBLE);
        ShareTools.putString(XiquerLoginActivity.this, "username", name);
        ShareTools.putString(XiquerLoginActivity.this, "password", pw);
        login(name,pw);
    }

    public void login(String name,String pw){
        TimetableRequest.loginGreenFruit(getContext(), selectSchool.getXxdm(),
                name, pw, new Callback<GreenFruitProfile>() {
                    @Override
                    public void onResponse(Call<GreenFruitProfile> call, Response<GreenFruitProfile> response) {
                        loadingLayout.setVisibility(View.GONE);
                        GreenFruitProfile profile=response.body();
                        if(profile!=null&&profile.getFlag()!=null){
                            if(profile.getFlag().equals("0")){
                                greenFruitProfile=profile;
                                titleText.setText(profile.getXm()+"-请选择学期");
                                if(greenFruitProfile!=null){
                                    getTerm(profile);
                                }
                            }else{
                                ToastTools.show(getContext(),"Error:["+profile.getFlag()+"] "+profile.getMsg());
                            }
                        }else {
                            ToastTools.show(getContext(),"Errot:profile is null");
                        }
                    }

                    @Override
                    public void onFailure(Call<GreenFruitProfile> call, Throwable t) {
                        loadingLayout.setVisibility(View.GONE);
                        ToastTools.show(getContext(),"Errot:"+t.getMessage());
                    }
                });
    }

    public void getCourses(GreenFruitTerm.XnxqBean term){
        if(greenFruitProfile==null) return;
        loadingLayout.setVisibility(View.VISIBLE);
        for(int i=1;i<=25;i++){
            iteratorGetCourse(term,i);
        }
        iteratorGetCourse(term,-1);
    }

    public synchronized void addNumber(){
        number++;
    }

    public synchronized void addNumber2(){
        number2++;
    }

    public void iteratorGetCourse(GreenFruitTerm.XnxqBean term, final int week){
        String weekStr="";
        if(week!=-1){
            weekStr=""+week;
        }
        TimetableRequest.getGreenFruitCourse(getContext(), greenFruitProfile.getUserid(), greenFruitProfile.getUsertype(),
                term.getDm(),weekStr, greenFruitProfile.getToken(), new Callback<GreenFruitCourse>() {
                    @Override
                    public void onResponse(Call<GreenFruitCourse> call, Response<GreenFruitCourse> response) {
                        addNumber();
                        GreenFruitCourse fruitCourse=response.body();
                        if(fruitCourse!=null){
                            if(fruitCourse.getWeek1()==null&&fruitCourse.getWeek2()==null&&
                                    fruitCourse.getWeek3()==null&&fruitCourse.getWeek4()==null
                                    &&fruitCourse.getWeek5()==null){
                                addNumber2();
                                loadingTextView.setText("第"+week+"周数据获取完成,处理中..");
                            }else{
                                if(week==-1){
                                    int curWeek=Integer.parseInt(fruitCourse.getZc());
//                                    ShareTools.putString(getContext(), ShareConstants.STRING_START_TIME, TimetableTools.getStartSchoolTime(curWeek));
                                }
                                courseList.add(fruitCourse);
                                loadingTextView.setText("第"+week+"周数据获取完成,处理中..");
                            }
                        }else {
                            ToastTools.show(getContext(),"Errot:profile is null");
                            loadingTextView.setText("第"+week+"周数据获取完成,处理中..");
                        }
                        if(number>=26){

                            loadingLayout.setVisibility(View.GONE);
                            saveCourses();
                            userName.setText("");
                            userPassword.setText("");
                            userSchool.setText("请选择学校");
                            selectSchool=null;
                            if(number2>=26){
                                ToastTools.show(getContext(),"该学期没有课程,请选择其他学期");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GreenFruitCourse> call, Throwable t) {
                        loadingLayout.setVisibility(View.GONE);
                        addNumber();
                        ToastTools.show(getContext(),"Errot:"+t.getMessage());
                    }
                });
    }

    private void saveCourses(){
        if(courseList==null) courseList=new ArrayList<>();

        for(GreenFruitCourse course:courseList) {
            saveCourses2(course);
        }
    }

    private void saveCourses2(GreenFruitCourse fruitCourse) {
        if(fruitCourse==null) return;
        List<GreenFruitCourse.WeekBean> weekList1=fruitCourse.getWeek1();
        List<GreenFruitCourse.WeekBean> weekList2=fruitCourse.getWeek2();
        List<GreenFruitCourse.WeekBean> weekList3=fruitCourse.getWeek3();
        List<GreenFruitCourse.WeekBean> weekList4=fruitCourse.getWeek4();
        List<GreenFruitCourse.WeekBean> weekList5=fruitCourse.getWeek5();
        List<GreenFruitCourse.WeekBean> weekList6=fruitCourse.getWeek6();
        List<GreenFruitCourse.WeekBean> weekList7=fruitCourse.getWeek7();

        List<ParseResult> models=new ArrayList<>();
        saveCourses(models,weekList1,1);
        saveCourses(models,weekList2,2);
        saveCourses(models,weekList3,3);
        saveCourses(models,weekList4,4);
        saveCourses(models,weekList5,5);
        saveCourses(models,weekList6,6);
        saveCourses(models,weekList7,7);

        if(models.size()>0){
            StatManager.sendKVEvent(getContext(),"pf_xiquer_success",null);
            ParseManager.setSuccess(true);
            ParseManager.setTimestamp(System.currentTimeMillis());
            ParseManager.setData(models);
        }
        finish();
    }

    private void saveCourses(List<ParseResult> models, List<GreenFruitCourse.WeekBean> weekList1,int day) {
        if(weekList1==null) return;
        try{
            for(GreenFruitCourse.WeekBean weekBean:weekList1){
                String json= GsonUtils.getGson().toJson(weekBean);
                if(json==null||!courseMap.containsKey(json)){
                    ParseResult model=new ParseResult();
                    model.setName(weekBean.getKcmc());
                    model.setRoom(weekBean.getSkdd());
                    model.setTeacher(weekBean.getRkjs());
                    String[] startAndEnd=weekBean.getJcxx().split("-");
                    int start=Integer.parseInt(startAndEnd[0]);
                    int end=start;
                    if(startAndEnd.length>1){
                        end=Integer.parseInt(startAndEnd[1]);
                    }
                    int step=end-start+1;
                    model.setDay(day);
                    model.setStart(start);
                    model.setStep(step);
                    model.setWeekList(TimetableUtils.getWeekList(weekBean.getSkzs(),weekBean.getDsz()));
                    models.add(model);
                    courseMap.put(json,"1");
                }
            }
        }catch (Exception e){
            ToastTools.show(XiquerLoginActivity.this,"Error[saveCourses::"+day+"]"+e.getMessage());
        }

    }

    public void getTerm(final GreenFruitProfile profile){
        if(profile==null) return;
        loadingLayout.setVisibility(View.VISIBLE);
        TimetableRequest.getGreenFruitTerm(getContext(), profile.getUserid(),
                profile.getUsertype(), profile.getToken(),
                new Callback<GreenFruitTerm>() {
                    @Override
                    public void onResponse(Call<GreenFruitTerm> call, Response<GreenFruitTerm> response) {
                        loadingLayout.setVisibility(View.GONE);
                        GreenFruitTerm fruitTerm=response.body();
                        if(fruitTerm!=null){
                            if(fruitTerm.getErrcode()!=null&&!fruitTerm.getErrcode().equals("0")){
                                ToastTools.show(getContext(),"Error:["+fruitTerm.getErrcode()+"] "+fruitTerm.getMessage());
                            }else{
                                termList=fruitTerm;
                                createTermView(fruitTerm);
                            }
                        }else {
                            ToastTools.show(getContext(),"Errot:profile is null");
                        }
                    }

                    @Override
                    public void onFailure(Call<GreenFruitTerm> call, Throwable t) {
                        loadingLayout.setVisibility(View.GONE);
                        ToastTools.show(getContext(),"Errot:"+t.getMessage());
                    }
                });
    }

    private void createTermView(GreenFruitTerm fruitTerm) {
        if(list==null) return;
        termLayout.setVisibility(View.VISIBLE);
        list.clear();
        if(fruitTerm.getXnxq()==null){
            ToastTools.show(getContext(),"学期为空，可能不支持你们学校");
        }else{
            for(GreenFruitTerm.XnxqBean bean:fruitTerm.getXnxq()){
                if(bean.getDqxq()!=null&&bean.getDqxq().equals("1")){
                    list.add(bean.getMc()+" [当前学期]");
                }else list.add(bean.getMc());
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void onSelectSchoolClicked(){
        ActivityTools.toActivityWithout(this,ChooseSchoolActivity.class);
    }
}
