package com.example.qzz;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.USAGE_STATS_SERVICE;
import static com.example.qzz.MainActivity.LOG_TAG;


final class QueryUtils {

    private QueryUtils() {
    }

    /**创建URL
     *
     * @param stringUrl
     * @return
     */
    private static URL Createurl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);

        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"问题在创建URL",e);
        }
        return url;

    }

    /**
     * 向给定 URL 进行 HTTP 请求，并返回字符串作为响应。
     */
    private  static String makeHttpRequest(URL url) throws IOException{

        String jsonResponse = "";

// 如果 URL 为空，则提早返回
        if (url==null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream =null;
        try{
            urlConnection  =(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // 如果请求成功（响应代码 200），
            // 则读取输入流并解析响应。
            if(urlConnection.getResponseCode() ==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = ReadFromStream(inputStream);
            }else {
                Log.e(LOG_TAG,"Error response code:"+urlConnection.getResponseCode());
            }


        }catch (IOException e){
            Log.e(LOG_TAG,"问题在http请求",e);
        }finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * 将 {@link InputStream} 转换为包含
     * 来自服务器的整个 JSON 响应的字符串。
     */
    private static String ReadFromStream(InputStream inputStream) throws IOException{

        StringBuilder output = new StringBuilder();
        if (inputStream!=null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();


            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }



    /**
     * 返回通过解析给定 JSON 响应构建的 {@link Earthquake} 对象
     * 列表
     */
     private static List<Earthquake> extractFeatureFromJson(String earthquakeJSON) {

// 如果 JSON 字符串为空或 null，将提早返回。
         if (TextUtils.isEmpty(earthquakeJSON)){
             return  null;
         }

         // 创建一个可以添加地震的空 ArrayList
        List<Earthquake> earthquakes = new ArrayList<>();


        try {

            //json解析步骤
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            for (int i=0;i<earthquakeArray.length();i++){
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                Double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");

                // 创建新的 {@link Earthquake} 对象
                Earthquake earthquake = new Earthquake(magnitude,location,time,url);
                // 将该新 {@link Earthquake} 添加到地震列表。
                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

// 返回地震列表
        return earthquakes;
    }



    /**
     * 查询 USGS 数据集并返回 {@link Earthquake} 对象的列表。
     */
    public   static  List<Earthquake> EarthquakeData(String requestUrl){
        //测试强制后台睡眠半秒钟
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i(LOG_TAG,"EarthquakeData().....");
        URL url = Createurl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);

        }catch (IOException e){
            Log.e(LOG_TAG,"Problem making the HTTP request",e);

        }
        // 从 JSON 响应提取相关域并创建 {@link Earthquake} 的列表
        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);

        // 返回 {@link Earthquake} 的列表
        return earthquakes;




    }





}