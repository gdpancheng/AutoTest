package com.xxxman.test.select.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xxxman.test.select.Constant;
import com.xxxman.test.select.object.DataRow;
import com.xxxman.test.select.object.HttpRequest;
import com.xxxman.test.select.object.HttpResult;

import java.util.HashMap;
import java.util.Map;


public class HttpUtil  {
    private static final String TAG = HttpUtil.class.getName();

    public static HttpResult post(String function){
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setFunction(function);
        return post(httpRequest);
    }
    public static HttpResult post(String function,String paraName,String paraValue){
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setFunction(function);
        Map<String,String> map = new HashMap<>();
        map.put(paraName,paraValue);
        httpRequest.setContent(map);
        return post(httpRequest);
    }
    public static HttpResult post(HttpRequest httpRequest){
        Connection my  = new Connection();
        String url = Constant.URL;
        HttpResult httpResult = new HttpResult();
        Gson gson = new GsonBuilder().serializeNulls().create();
        //更新到服务器
        String context = gson.toJson(httpRequest);
        Map<String, String> parms = new HashMap<>();
        parms.put("jsonContent", context);
        Log.d(TAG, "http请求" + context);
        String rs = my.getContextByHttp(url, parms);
        Log.d(TAG, "http请求结果" + rs);

        try{
            httpResult = (HttpResult) gson.fromJson(rs, new TypeToken<HttpResult<Map<String,String>>>() {}.getType());
            Log.d(TAG, "error" + httpResult.getErrorNo()+":"+httpResult.getErrorInfo());
            Log.d(TAG, "list" + "("+httpResult.getList().size()+"):"+httpResult.getList());
        }catch (Exception e){
            e.printStackTrace();
            httpResult.setErrorNo("-999");
            httpResult.setErrorInfo("网络或其它错误！");
        }
        return httpResult;
    }
}