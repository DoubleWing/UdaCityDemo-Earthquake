package com.example.qzz;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.text.DecimalFormat;
import android.graphics.drawable.GradientDrawable;

import com.example.android.didyoufeelit.R;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    //全局变量of
    private static final String LOCATION_SEPARATOR = " of ";

//创建一个适配器
EarthquakeAdapter(Context context, List<Earthquake> earthquakes){

        super(context,0,earthquakes);
    }

//辅助方法1-3

    //1-震级值返回格式化后的仅显示一位小数的震级字符串
    private  String formatMagnitude(double magnitude){
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);

    }

//2-Date对象返回格式化的日期字符串
    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-LL-dd");

        return  dateFormat.format(dateObject);
    }
//3-Date对象返回格式化的时间字符串
    private String formatTime(Date dateObject){

    SimpleDateFormat  timeFormat = new SimpleDateFormat("h:mm a");
    return timeFormat.format(dateObject);

    }

//圆圈颜色设置
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }







    @Override
    //控制列表项的现实方式
    public View getView(int postion, View convertView, ViewGroup parent){
        View listItemView = convertView;
//        是否可以使用回收视图
        if(listItemView == null){
            //使用earthquake_list_item的
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list_item,parent,false);
        }

        //从地震列表里获取正确的地震对象引用
        Earthquake currentEarthquake = getItem(postion);




        //地震级别绑定视图
        TextView magnitudeView = listItemView.findViewById(R.id.magnitude);
        String formattedMagnitude = formatMagnitude(currentEarthquake.getMagnitude());
        magnitudeView.setText(formattedMagnitude);

        //为地震级别设置背景圆圈颜色
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);



        //获取原始位置字符串
        String orginalLocation = currentEarthquake.getLocation();



        //主要位置和偏移位置
        String primaryLocation;
        String locationOffset;
        //逻辑部分
        if(orginalLocation.contains(LOCATION_SEPARATOR)){
            String[] parts = orginalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] +LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        }else{
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = orginalLocation;
        }
        //地震主要位置绑定视图
        TextView primaryLocationView = listItemView.findViewById(R.id.primary_location);
        primaryLocationView.setText(primaryLocation);
        //地震偏移位置绑定视图
        TextView locationOffsetView = listItemView.findViewById(R.id.location_offset);
        locationOffsetView.setText(locationOffset);




        //地震时间转换成date对象
        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());

        TextView dateView = listItemView.findViewById(R.id.date);
        //设置日期格式
        String formattedDate = formatDate(dateObject);
        dateView.setText(formattedDate);

        TextView timeView = listItemView.findViewById(R.id.time);
        //设置时间格式
        String formattedTime = formatTime(dateObject);
        timeView.setText(formattedTime);


        //返回视图给调用方
        //listItemView为调用方法
        return listItemView;
    }

}
