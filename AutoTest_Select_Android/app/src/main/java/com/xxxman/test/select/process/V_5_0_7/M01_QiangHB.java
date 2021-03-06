package com.xxxman.test.select.process.V_5_0_7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.UiWatcher;
import android.util.Log;

import com.xxxman.test.select.Constant;
import com.xxxman.test.select.object.DataRow;
import com.xxxman.test.select.object.HttpResult;
import com.xxxman.test.select.object.Task;
import com.xxxman.test.select.sql.TaskSQL;
import com.xxxman.test.select.util.HttpUtil;
import com.xxxman.test.select.util.SQLUtil;
import com.xxxman.test.select.util.ToastUitl;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class M01_QiangHB {
    private static final String TAG = M01_QiangHB.class.getName();
    UiDevice mUIDevice = null;
    Context mContext = null;

    //抢红包
    private void qiangHongBao(Task task)   {
        try{
            S07_Change_AirPlane.start();
            S00_App_Reboot.start();
            S01_Login.start(task);
            TaskSQL.updateTaskCount(task.getId(),"task_count",task.getTask_count()+1);
            for(int j=0;j<100;j++){
                try{
                    HttpResult httpResult = HttpUtil.post("F200101");
                    if("".equals(httpResult.getErrorNo()) && httpResult.getList().size()>0) {
                        List<DataRow> list_dataRow =  httpResult.getList();
                        int size =3;
                        if(list_dataRow.size()<size){
                            size = list_dataRow.size();
                        }
                        for(int i=0;i<size;i++){
                            try {
                                String uid = list_dataRow.get(i).getString("uid");
                                mUIDevice.runWatchers();
                                S02_Go_Zhibo.start(uid);
                                Thread.sleep(3000);
                                S03_Share.start();
                                S04_Qiang.start(task);
                                S05_Close_Zhibo.start();
                                if(task.getFail_count()>=3){
                                    break;
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                S00_App_Reboot.start();
                            }
                        }
                    }else{
                        ToastUitl.sendBroadcast(mContext,"没差到红包，休息10秒中");
                        Thread.sleep(10000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    S00_App_Reboot.start();
                }
                if(task.getFail_count()>=3){
                    break;
                }
                if(task.getSuccess_count()>=Constant.HONGBAO_COUNT_ONE && task.getTask_count()==0){
                    break;
                }
                if(task.getSuccess_count()>=Constant.HONGBAO_COUNT && task.getTask_count()>1){
                    break;
                }
            }
            S01_Quit.start(task,true);
        }catch (Exception e){
            e.printStackTrace();
            S00_App_Reboot.start();
            try {
                Thread.sleep(1000);
                S01_Quit.start(task,true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    @Test
    public void test() {
        try {
            //启动APP
            S00_App_Reboot.start();
            Thread.sleep(3000);
            mUIDevice.pressBack();
            //获取任务
            List<Task> list = S00_Get_Task.start();
            if(list.size()>0){
                for (int j = 0; j < 6; j++) {
                    for(Task task :list){
                        Log.d(TAG,"开始执行第（"+task.getNumber()+"）任务："
                                +"phone="+task.getPhone()
                                +"，task_count="+task.getTask_count()
                                +"，success_count="+task.getSuccess_count()
                                +"，fail_count="+task.getFail_count());
                        //没抢满红包
                        if(task.getSuccess_count()<Constant.HONGBAO_COUNT ){
                            //执行小于6次
                            if( task.getTask_count()<j+1 ){
                                //失败小于6次
                                if( task.getFail_count()< 6 ){
                                    //失败小于3次，或者失败小于6次但成功小于3次
                                    if(task.getFail_count()< 3 || task.getSuccess_count()<3){
                                        //提醒
                                        String info = "开始第（"+task.getNumber()+"）用户："
                                                +"，第"+task.getTask_count()+"次执行,"
                                                +"，已抢到"+task.getSuccess_count()+"次,"
                                                +"，未抢到"+task.getFail_count()+"次；";
                                        //提示
                                        ToastUitl.sendBroadcast(mContext,info);
                                        qiangHongBao(task);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void before() throws RemoteException {
        mUIDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());  //获得device对象
        mUIDevice.registerWatcher("close_ad", new UiWatcher() {
            @Override
            public boolean checkForCondition() {
                // just press back
                Log.d(TAG,":进入Watcher-close_ad");
                try {
                    return  S00_AD_Close.start();
                    //Thread.sleep(1000);
                } catch (Exception e) {
                    Log.d(TAG,":关闭广告失败");
                    e.printStackTrace();
                }
                return false;
            }
        });
        mContext = InstrumentationRegistry.getContext();
        if(!mUIDevice.isScreenOn()){  //唤醒屏幕
            mUIDevice.wakeUp();
        }
    }
}
