package com.xxxman.autotest.shell;

/**
 * Created by tuzi on 2017/8/17.
 */

public class Constant {

    //总红包任务数
    public static final int HONGBAO_COUNT = 6;
    //第一次红包任务数
    public static final int HONGBAO_COUNT_ONE = 3;

    public static final String REG_URL = "http://reg.zbqhb.com:3000/action/lfs/action/FunctionAction";

//    public static final String TAG = "ite";
//    public static final String TAG = "hd";
//    public static final String TAG = "hsm";
    public static final String TAG = "ab";

    public static final String URL(){
            return "http://"+TAG+".zbqhb.com:3000/action/lfs/action/FunctionAction";
    }
    public static final boolean IS_4X(){
        boolean flag = true;
        if("Redmi 4A".equals(android.os.Build.MODEL)){
            flag =  false;
        }
        return flag;
    }
}
