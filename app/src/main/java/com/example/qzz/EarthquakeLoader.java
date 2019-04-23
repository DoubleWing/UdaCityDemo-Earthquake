package com.example.qzz;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    /** 日志消息标签 */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();
    /** 查询 URL */
    private String mUrl;

    public  EarthquakeLoader(Context context,String url){
        super(context);
        mUrl =url;
    }


    @Override
    protected void onStartLoading(){
        Log.i(LOG_TAG,"onStartLoading().....");
        forceLoad();
    }


    /**
     * 这位于后台线程上。
     */
    @Override
    public List<Earthquake> loadInBackground(){
        Log.i(LOG_TAG,"loadInBackground().....");
        if (mUrl==null){
            return  null;
        }
        // 执行网络请求、解析响应和提取地震列表。
        List<Earthquake> earthquakes =QueryUtils.EarthquakeData(mUrl);
        return  earthquakes;

    }
}
