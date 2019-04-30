package com.zhuangfei.adapterlib.once.local;

/**
 * Created by Liu ZhuangFei on 2019/4/30.
 */
public class OnceUser {
    private String number;
    private String password;

    private String number2;
    private String password2;

    public String getNumber2() {
        return number2;
    }

    public void setNumber2(String number2) {
        this.number2 = number2;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean same(OnceUser user){
        if(user!=null){
            if(user.getNumber().equals(getNumber())){
                return true;
            }
        }
        return false;
    }
}
