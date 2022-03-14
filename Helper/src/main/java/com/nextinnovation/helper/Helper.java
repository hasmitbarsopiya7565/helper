package com.nextinnovation.helper;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Helper {

    TextToSpeech textToSpeechSystem = null;

    //show full screen
    public  void showFullScreen(Context context)
    {
        //context.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ((Activity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //hide full screen
    public void hideFullScreen(Context context)
    {
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    public void customToast(Context c, String messsage, String type) {

        View layout = LayoutInflater.from(c).inflate(R.layout.custom_toast_layout,null);
        TextView toastMsg = layout.findViewById(R.id.custom_toast_message);
        ImageView toastIv = layout.findViewById(R.id.custom_toast_image);
        LinearLayout custom_toast_layout = layout.findViewById(R.id.custom_toast_layout);


        switch (type.toLowerCase())
        {
            case "success":
                custom_toast_layout.getBackground().setColorFilter(ContextCompat.getColor(c,R.color.google_green), PorterDuff.Mode.SRC_ATOP);
                break;

            case "update":
                custom_toast_layout.getBackground().setColorFilter(ContextCompat.getColor(c,R.color.google_blue),PorterDuff.Mode.SRC_ATOP);
                break;

            case "nodata":
                toastIv.setImageResource(R.drawable.ic_sad_cloud);
                custom_toast_layout.getBackground().setColorFilter(ContextCompat.getColor(c,R.color.google_yellow),PorterDuff.Mode.SRC_ATOP);
                break;

            case "delete":
                toastIv.setImageResource(R.drawable.ic_delete);
                custom_toast_layout.getBackground().setColorFilter(ContextCompat.getColor(c,R.color.google_red),PorterDuff.Mode.SRC_ATOP);
                break;
        }

        Toast toast = new Toast(c);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toastMsg.setText(messsage);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                toast.cancel();
            }
        }, 1300);

        Animation bounce = AnimationUtils.loadAnimation(c,R.anim.bounce);
        toastIv.startAnimation(bounce);
        toast.setView(layout);
        toast.show();
    }

    //show toast
    public void showToast(Context context,String message)
    {
        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
    }

    public Context setLocall(Context context, String langCode)
    {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        }
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        return context;
    }

    public void setLocal(Context context, String langCode)
    {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        }
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }


    //get date
    public String getDate(String date){

        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date newDate = null;//You will get date object relative to server/client timezone wherever it is parsed
        try {
            newDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); //If you need time just put specific format for time like 'HH:mm:ss'
        String dateStr = formatter.format(newDate);


        return dateStr;
    }


    //convert date yyyy-MM-dd
    public String YMD(String fdate)
    {
        String datetime=null;
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat d= new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        }catch (ParseException e)
        {

        }
        return  datetime;
    }

    //convert date dd-MM-yyyy
    public String DMY(String fdate)
    {
        String datetime=null;
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat d= new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        }catch (ParseException e)
        {

        }
        return  datetime;
    }

    //set time ago
    public String setTimeAgo(String date)
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        CharSequence ago = null;
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long time = sdf.parse(date).getTime();
            Log.d("TIME",String.valueOf(time));
            long now = System.currentTimeMillis();
            ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (ago.equals("0 minutes ago")){

            return "just now";
        }else {
            return ago+"";
        }
    }

   /* //send push notification
    public void sendNotification(String to,String title,String message,String activity,String id,String date)
    {
        ApiService apiService = Client.getClient().create(ApiService.class);

        Data data = new Data(title,message,activity,id,date);
        NotificationSender sender = new NotificationSender(data,to);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                Log.d("response",response.toString());
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }*/

   /* public void sendNotification(String to,String title,String message,String activity,String id)
    {

        ApiService apiService = Client.getClient().create(ApiService.class);

        Data data = new Data(title,message,activity,id);
        NotificationSender sender = new NotificationSender(data,to);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                Log.d("response",response.toString());
                if(response.code() == 200)
                {

                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });

    }*/


  /*  public File SelectFileAndCheckExtenstion(Context context, int requestCode, int resultCode, Intent data, TextView attach_file, ImageView file_iv, int pickImageRequest, TextView errorSize)
    {

        File filepath = null;
        if (requestCode == pickImageRequest) {
            if (resultCode == RESULT_OK) {

                Uri path = data.getData();
                String file = UriUtils.getPathFromUri(context, path);
                filepath = new File(file);
                String ext = file.substring(file.lastIndexOf("."));
                attach_file.setText(filepath.getName());
                int size = Integer.parseInt(String.valueOf(filepath.length()/10240));
                if(size>2048)
                {
                    errorSize.setVisibility(View.VISIBLE);
                }
                else
                {
                    errorSize.setVisibility(View.GONE);
                }
                Log.e("File", "Path => " + filepath.getAbsolutePath());

                file_iv.setVisibility(View.VISIBLE);
                if(ext.equalsIgnoreCase(".pdf"))
                {
                    setFileImageTypeFromUrl(context,path.toString(),R.drawable.ic_pdf,file_iv);
                    *//*Picasso.with(context)
                            .load(path.toString())
                            .placeholder(R.drawable.ic_pdf)
                            .fit().centerCrop()
                            .into(file_iv);*//*
                }
                else if(ext.equalsIgnoreCase(".apk"))
                {
                    setFileImageTypeFromUrl(context,path.toString(),R.drawable.ic_apk,file_iv);
                   *//* Picasso.with(context)
                            .load(path.toString())
                            .placeholder(R.drawable.ic_apk)
                            .fit().centerCrop()
                            .into(file_iv);*//*
                }
                else if(ext.equalsIgnoreCase(".zip"))
                {
                    setFileImageTypeFromUrl(context,path.toString(),R.drawable.ic_zip,file_iv);
                   *//* Picasso.with(context)
                            .load(path.toString())
                            .placeholder(R.drawable.ic_zip)
                            .fit().centerCrop()
                            .into(file_iv);*//*
                }
                else if(ext.equalsIgnoreCase(".xlsx") || ext.equalsIgnoreCase(".xls"))
                {
                    setFileImageTypeFromUrl(context,path.toString(),R.drawable.ic_excel,file_iv);
                    *//*Picasso.with(context)
                            .load(path.toString())
                            .placeholder(R.drawable.ic_excel)
                            .fit().centerCrop()
                            .into(file_iv);*//*
                }
                else if(ext.equalsIgnoreCase(".pptx") || ext.equalsIgnoreCase(".ppt"))
                {
                    setFileImageTypeFromUrl(context,path.toString(),R.drawable.ic_ppt,file_iv);
                 *//*   Picasso.with(context)
                            .load(path.toString())
                            .placeholder(R.drawable.ic_pdf)
                            .fit().centerCrop()
                            .into(file_iv);*//*
                }
                else if(ext.equalsIgnoreCase(".txt"))
                {
                    setFileImageTypeFromUrl(context,path.toString(),R.drawable.ic_txt,file_iv);
                    *//*Picasso.with(context)
                            .load(path.toString())
                            .placeholder(R.drawable.ic_txt)
                            .fit().centerCrop()
                            .into( file_iv);
                    *//*
                }
                else if(ext.equalsIgnoreCase(".docx"))
                {
                    setFileImageTypeFromUrl(context,path.toString(),R.drawable.ic_doc,file_iv);
                   *//* Picasso.with(context)
                            .load(path.toString())
                            .placeholder(R.drawable.ic_doc)
                            .fit().centerCrop()
                            .into( file_iv);*//*
                }
                else if(ext.equalsIgnoreCase(".rar"))
                {
                    setFileImageTypeFromUrl(context,path.toString(),R.drawable.ic_rar,file_iv);
                   *//* Picasso.with(context)
                            .load(path.toString())
                            .placeholder(R.drawable.ic_rar)
                            .fit().centerCrop()
                            .into( file_iv);*//*
                }
                else
                {
                    setFileImageTypeFromUrl(context,path.toString(),R.drawable.ic_image,file_iv);
                    *//*Picasso.with(context)
                            .load(path.toString())
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .fit().centerCrop()
                            .into(file_iv);*//*
                }

            }
        }
        return filepath;
    }

    public void addFileList(Context mContext, ArrayList<SelectFileModel> selectedFileList, Intent data, int requestCode, int resultCode, int pickImageRequest) {
        File filepath = null;
        if (requestCode == pickImageRequest) {
            if (data != null) {
                if (data.getClipData() != null) { // checking multiple selection or not
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();

                        String file = UriUtils.getPathFromUri(mContext, uri);
                        filepath = new File(file);
                        String ext = file.substring(file.lastIndexOf("."));
                        //attach_file.setText(filepath.getName());
                        int size = Integer.parseInt(String.valueOf(filepath.length() / 10240));

                        if(i<5) {
                            selectedFileList.add(new SelectFileModel(filepath, filepath.getName(), ext, size));
                        }
                        else {
                            customToast(mContext,mContext.getString(R.string.youCanUploadMaximumFiveAttachment),"nodata");
                        }
                    }
                }
                else if(resultCode == RESULT_OK) {

                    Uri path = data.getData();
                    String file = UriUtils.getPathFromUri(mContext, path);
                    filepath = new File(file);
                    String ext = file.substring(file.lastIndexOf("."));
                    //attach_file.setText(filepath.getName());
                    int size = Integer.parseInt(String.valueOf(filepath.length() / 10240));

                    selectedFileList.add(new SelectFileModel(filepath, filepath.getName(), ext, size));

                    //filepath =  helper.SelectFileAndCheckExtenstion(getContext(), requestCode, resultCode, data, addSelectFileET, fileIv, PICK_IMAGE_REQUEST);
                }
            }
        }
    }


    public String convertListToString(ArrayList<SelectFileModel> selectedFileList){
        String strSeparator = "__,__";
        String str = "";
        for (int i = 0;i<selectedFileList.size(); i++) {
            str = str+selectedFileList.get(i).getFilepath();
            // Do not append comma at the end of last element
            if(i<selectedFileList.size()-1){
                str = str+strSeparator;
            }
        }
        return str;
    }*/

    public String convertArrayListToString(ArrayList<String> stringList){
        String strSeparator = ",";
        String str = "";
        for (int i = 0;i<stringList.size(); i++) {
            str = str+stringList.get(i);
            // Do not append comma at the end of last element
            if(i<stringList.size()-1){
                str = str+strSeparator;
            }
        }
        return str;
    }

    public String[] convertStringToArray(String str){
        String strSeparator = "__,__";
        String[] arr = str.split(strSeparator);
        return arr;
    }


   /* public ArrayList<SelectFileModel> convertStringToList(String str){
        String strSeparator = "__,__";
        String[] arr = str.split(strSeparator);
        ArrayList<SelectFileModel> selectedFileList = new ArrayList<>();

        for (String s : arr) {
            File filepath = new File(s);
            if(s.contains("."))
            {
                String ext = s.substring(s.lastIndexOf("."));

                //attach_file.setText(filepath.getName());
                int size = Integer.parseInt(String.valueOf(filepath.length() / 10240));

                selectedFileList.add(new SelectFileModel(filepath, filepath.getName(), ext, size));
            }
        }

        return selectedFileList;
    }*/

    public void setFileImageFromDrawable(Context context,int drawable,ImageView imageView)
    {
        Picasso.with(context)
                .load(drawable)
                .fit().centerCrop()
                .into(imageView);
    }

    public void setFileImageTypeFromUrl(Context context,String Url,int PlaceHolderImage,ImageView imageView)
    {
        Picasso.with(context)
                .load(Url)
                .placeholder(PlaceHolderImage)
                .fit().centerCrop()
                .into(imageView);
    }

    public void setFileImageTypeFromFile(Context context,File file,int PlaceHolderImage,ImageView imageView)
    {
        Picasso.with(context)
                .load(Uri.fromFile(file))
                .placeholder(PlaceHolderImage)
                .fit().centerCrop()
                .into(imageView);
    }

    public void setFileImageTypeFromUri(Context context,Uri uri,int PlaceHolderImage,ImageView imageView)
    {
        Picasso.with(context)
                .load(uri)
                .placeholder(PlaceHolderImage)
                .fit().centerCrop()
                .into(imageView);
    }



    public String checkExtension(String s) {
        String ext = s.substring(s.lastIndexOf("."));
        return ext;
    }


    public String stringCapitlize(String string) {
        String[] splits = string.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splits.length; i++) {
            String eachWord = splits[i];
            if (i > 0 && eachWord.length() > 0) {
                sb.append(" ");
            }
            String cap = eachWord.substring(0, 1).toUpperCase()
                    + eachWord.substring(1);
            sb.append(cap);
        }
        return sb.toString();
    }
    public String capitizeString(String name){
        String captilizedString="";
        if(!name.trim().equals("")){
            captilizedString = name.substring(0,1).toUpperCase() + name.substring(1);
        }
        return captilizedString;
    }

    public void dataNotAvailableDailog(Context context, int drawable, String firstMessage, String secondMessage)
    {
        AlertDialog.Builder notFoundDailogBuilder = new AlertDialog.Builder(context,R.style.CustomAlertDialog);
        AlertDialog notFoundDailog = notFoundDailogBuilder.create();
        View notFoundDailogView = LayoutInflater.from(context).inflate(R.layout.not_found_dailog_view,null);
        notFoundDailog.setView(notFoundDailogView);
        notFoundDailog.setCancelable(false);

        //bind controls
        ImageView notFoundIv = notFoundDailogView.findViewById(R.id.notFoundIv);
        TextView notFoundFirstMessageTv = notFoundDailogView.findViewById(R.id.notFoundFirstMessageTv);
        TextView notFoundSecondMessageTv = notFoundDailogView.findViewById(R.id.notFoundSecondMessageTv);
        AppCompatButton okBtn = notFoundDailogView.findViewById(R.id.okBtn);

        notFoundIv.setImageResource(drawable);

        //animation
        Animation pulse = AnimationUtils.loadAnimation(context,R.anim.pulse);
        notFoundIv.startAnimation(pulse);

        //set message
        notFoundFirstMessageTv.setText(firstMessage);
        notFoundSecondMessageTv.setText(secondMessage);

        //cancel dailog
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notFoundDailog.dismiss();
            }
        });

        //dismiss dailog
        notFoundDailog.show();
    }

    public void setTime(Context context, TextView textView)
    {
        final int[] hours = new int[1];
        final int[] min = new int[1];

        Calendar myCalendar = Calendar.getInstance();

        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,R.style.DialogTheme2,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String status = "AM";
                        hours[0] = hourOfDay;
                        min[0] = minute;
                        if(hourOfDay > 11)
                        {
                            // If the hour is greater than or equal to 12
                            // Then the current AM PM status is PM
                            status = "PM";
                        }

                        // Initialize a new variable to hold 12 hour format hour value
                        int hour_of_12_hour_format;

                        if(hourOfDay > 11){

                            // If the hour is greater than or equal to 12
                            // Then we subtract 12 from the hour to make it 12 hour format time
                            hour_of_12_hour_format = hourOfDay - 12;
                        }
                        else {
                            hour_of_12_hour_format = hourOfDay;
                        }


                        if(minute<9)
                        {
                            textView.setText(hour_of_12_hour_format + ":" +"0"+minute+" "+status);
                            //  Smin = Integer.valueOf(String.valueOf(0) + String.valueOf(minute));
                            //  Toast.makeText(getContext(), "0"+min, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            textView.setText(hour_of_12_hour_format + ":" +minute+" "+status);
                        }
                        /* selectTime.setText(hour_of_12_hour_format + ":" +minute);*/

                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    public void setTime(Context context,int h,int m,String AM_PM,TextView textView)
    {
        final int[] hours = new int[1];
        final int[] min = new int[1];

        Calendar myCalendar = Calendar.getInstance();

        if(AM_PM.toUpperCase().equals("PM"))
            myCalendar.set(Calendar.HOUR_OF_DAY,h+12);
        else
            myCalendar.set(Calendar.HOUR_OF_DAY,h);

        myCalendar.set(Calendar.MINUTE,m);



        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(context,R.style.DialogTheme2,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        String status = "AM";
                        hours[0] = hourOfDay;
                        min[0] = minute;
                        if(hourOfDay > 11)
                        {
                            // If the hour is greater than or equal to 12
                            // Then the current AM PM status is PM
                            status = "PM";
                        }

                        // Initialize a new variable to hold 12 hour format hour value
                        int hour_of_12_hour_format;

                        if(hourOfDay > 11){

                            // If the hour is greater than or equal to 12
                            // Then we subtract 12 from the hour to make it 12 hour format time
                            hour_of_12_hour_format = hourOfDay - 12;
                        }
                        else {
                            hour_of_12_hour_format = hourOfDay;
                        }


                        if(minute<9)
                        {
                            textView.setText(hour_of_12_hour_format + ":" +"0"+minute+" "+status);
                            //  Smin = Integer.valueOf(String.valueOf(0) + String.valueOf(minute));
                            //  Toast.makeText(getContext(), "0"+min, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            textView.setText(hour_of_12_hour_format + ":" +minute+" "+status);
                        }
                        /* selectTime.setText(hour_of_12_hour_format + ":" +minute);*/

                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    public String[] convertStringArrayListToStringArray(ArrayList<String> arrayList)
    {
        String[] mStringArray = new String[arrayList.size()];
        mStringArray = arrayList.toArray(mStringArray);

        return mStringArray;
    }


    public StringBuilder convertArrayToString(String[] stringArray) {
        StringBuilder string = new StringBuilder();
        for(int i=0;i<stringArray.length;i++)
        {
            if(i != stringArray.length-1)
                string.append(stringArray[i]).append(",");
            else
                string.append(stringArray[i]);
        }
        return string;
    }

    public ArrayList<String> convertStringToArrayList(String str){
        String strSeparator = ",";
        String[] arr = str.split(strSeparator);
        ArrayList<String> selectedFileList = new ArrayList<>();

        for (String s : arr) {
            selectedFileList.add(s);
        }

        return selectedFileList;
    }



    /*  public boolean cautionDialog(Context c)
      {
          final boolean[] status = {false};
          //delete confirmation dialog start
          androidx.appcompat.app.AlertDialog.Builder cautionDailogBuilder = new androidx.appcompat.app.AlertDialog.Builder(c,R.style.CustomAlertDialog);
          AlertDialog cautionDailog = cautionDailogBuilder.create();

          //set layout
          View delete_view = LayoutInflater.from(c).inflate(R.layout.cuation_dailog_layout,null);
          cautionDailog.setView(delete_view);
          cautionDailog.setCancelable(false);

          //bind button
          Button yesBtn = delete_view.findViewById(R.id.yesBtn),noBtn = delete_view.findViewById(R.id.noBtn);
          TextView cautionSecondMessageTv = delete_view.findViewById(R.id.cautionSecondMessageTv);
          TextView cuationFirstMessageTv = delete_view.findViewById(R.id.cuationFirstMessageTv);
          ImageView cautionIv = delete_view.findViewById(R.id.cautionIv);
          Animation pulse = AnimationUtils.loadAnimation(c,R.anim.pulse);
          cautionIv.startAnimation(pulse);

          yesBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                  status[0] = true;
                  //delete confrimation dialog over
                  cautionDailog.dismiss();
              }
          });
          noBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  status[0] = false;
                  cautionDailog.dismiss();
              }
          });

          //show delete confrimation dialog
          cautionDailog.show();

          return status[0];

      }*/
    public String getPreviousOrNextDate(String inputDate,int day){

        SimpleDateFormat  format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = format.parse(inputDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            c.add(Calendar.DATE, day);
            inputDate = format.format(c.getTime());

            System.out.println(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            inputDate ="";
        }
        return inputDate;
    }

    public long getTimeFromDate(String fromDate)
    {
        String[] fromDateSplit = fromDate.split("-");

        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(fromDateSplit[2]),Integer.parseInt(fromDateSplit[1]),Integer.parseInt(fromDateSplit[0]));
        return c.getTimeInMillis();

    }

    public boolean checkTimeIsGreaterOrNot(String startTime,String endTime)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        Date inTime = null;
        Date outTime = null;
        try {
            inTime = sdf.parse(startTime);
            outTime = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(inTime.compareTo(outTime) > 0){
            return true;
        }
        else
        {
            return false;
        }
    }

    public long getDifferceBetweenTimeInMinutes(String timeFrom,String timeTo) {
        long min = 0;
        long difference ;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa"); // for 12-hour system, hh should be used instead of HH
            // There is no minute different between the two, only 8 hours difference. We are not considering Date, So minute will always remain 0
            Date date1 = simpleDateFormat.parse(timeFrom);
            Date date2 = simpleDateFormat.parse(timeTo);

            difference = (date2.getTime() - date1.getTime()) / 1000;
            long hours = difference % (24 * 3600) / 3600; // Calculating Hours
            long minute = difference % 3600 / 60; // Calculating minutes if there is any minutes difference
            min = minute + (hours * 60); // This will be our final minutes. Multiplying by 60 as 1 hour contains 60 mins
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return min;
    }

    public String getTimeFromString(String time) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");

            Date date1 = simpleDateFormat.parse(time);
            date1.getTime();

            Log.d("STRING TO DATE TIME",date1.toLocaleString());

            return date1.toLocaleString();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getTimeFromGMT(String time)
    {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date newDate = null;//You will get date object relative to server/client timezone wherever it is parsed
        try {
            newDate = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") DateFormat hoursFormate = new SimpleDateFormat("HH");
        @SuppressLint("SimpleDateFormat") DateFormat minuteFormate = new SimpleDateFormat("mm");
        String hours = hoursFormate.format(newDate);
        String minute = minuteFormate.format(newDate);

        String finalTime =  ((Integer.parseInt(hours) > 12) ? Integer.parseInt(hours) % 12 : Integer.parseInt(hours)) + ":" + (Integer.parseInt(minute) < 10 ? (minute) : minute) + " " + ((Integer.parseInt(hours) >= 12) ? "PM" : "AM");
        return finalTime;

    }

    public String getTimeFromLocalString(String time)
    {
        String dateStr = "Jul 16, 2013 12:08:59 AM";
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a", Locale.ENGLISH);
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm a");

        Date date = null;
        Date date2 = null;
        try {
            date = df.parse(time);

            df.setTimeZone(TimeZone.getDefault());
            String formattedDate = df2.format(date);
            date2 = df2.parse(formattedDate);
            return df2.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String formateMilliSeccond(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        return finalTimerString;
    }

    public void dataNotAvailableDailogColor(Context context, int drawable, String firstMessage, String secondMessage,int color) {
        AlertDialog.Builder notFoundDailogBuilder = new AlertDialog.Builder(context,R.style.CustomAlertDialog);
        AlertDialog notFoundDailog = notFoundDailogBuilder.create();
        View notFoundDailogView = LayoutInflater.from(context).inflate(R.layout.not_found_dailog_view,null);
        notFoundDailog.setView(notFoundDailogView);
        notFoundDailog.setCancelable(false);

        //bind controls
        ImageView notFoundIv = notFoundDailogView.findViewById(R.id.notFoundIv);
        notFoundIv.setColorFilter(ContextCompat.getColor(context,color),PorterDuff.Mode.SRC_ATOP);
        TextView notFoundFirstMessageTv = notFoundDailogView.findViewById(R.id.notFoundFirstMessageTv);
        TextView notFoundSecondMessageTv = notFoundDailogView.findViewById(R.id.notFoundSecondMessageTv);
        AppCompatButton okBtn = notFoundDailogView.findViewById(R.id.okBtn);

        notFoundIv.setImageResource(drawable);

        //animation
        Animation pulse = AnimationUtils.loadAnimation(context,R.anim.pulse);
        notFoundIv.startAnimation(pulse);

        //set message
        notFoundFirstMessageTv.setText(firstMessage);
        notFoundSecondMessageTv.setText(secondMessage);

        //cancel dailog
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notFoundDailog.dismiss();
            }
        });

        //dismiss dailog
        notFoundDailog.show();
    }

    public void deleteFileFromStroage(Context context, String string){
        Uri uri = Uri.parse(string);
        File file = new File(uri.getPath());
        file.delete();
        if(file.exists()){
            try {
                file.getCanonicalFile().delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(file.exists()){
                context.deleteFile(file.getPath());
            }
        }
    }

    public String getTodayDate() {
        Calendar myCalendar = Calendar.getInstance();
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf.format(myCalendar.getTime());
    }

  /*  //text to speech
    public void textToSpeech(Context context,String message) {
        SessionUtil sessionUtil = new SessionUtil(context);
        textToSpeechSystem = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeechSystem.setLanguage(new Locale(sessionUtil.getSelectedLanguage()));
                    textToSpeechSystem.speak(message, TextToSpeech.QUEUE_ADD, null, null);
                }
            }
        });
    }*/

    public double getDistanceInMiles(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public float getDistanceInMeters(double fromLatitiude , double fromLongitude, double toLatitude, double toLongitude) {

        Location loc1 = new Location("");
        loc1.setLatitude(fromLatitiude);// current latitude
        loc1.setLongitude(fromLongitude);//current  Longitude

        Location loc2 = new Location("");
        loc2.setLatitude(toLatitude);
        loc2.setLongitude(toLongitude);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Float.parseFloat(decimalFormat.format(loc1.distanceTo(loc2)));

    }

    public String getDistanceInKM(double fromLatitiude , double fromLongitude, double toLatitude, double toLongitude) {

        Location loc1 = new Location("");
        loc1.setLatitude(fromLatitiude);// current latitude
        loc1.setLongitude(fromLongitude);//current  Longitude

        Location loc2 = new Location("");
        loc2.setLatitude(toLatitude);
        loc2.setLongitude(toLongitude);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(loc1.distanceTo(loc2)/1000)+" KM" ;

    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for(int idx=0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    /**
     * Get utf8 byte array.
     * @param str which to be converted
     * @return  array of NULL if error was found
     */
    public static byte[] getUTF8Bytes(String str) {
        try { return str.getBytes("UTF-8"); } catch (Exception ex) { return null; }
    }

    /**
     * Load UTF8withBOM or any ansi text file.
     * @param filename which to be converted to string
     * @return String value of File
     * @throws java.io.IOException if error occurs
     */
    public static String loadFileAsString(String filename) throws java.io.IOException {
        final int BUFLEN=1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8=false;
            int read,count=0;
            while((read=is.read(bytes)) != -1) {
                if (count==0 && bytes[0]==(byte)0xEF && bytes[1]==(byte)0xBB && bytes[2]==(byte)0xBF ) {
                    isUTF8=true;
                    baos.write(bytes, 3, read-3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count+=read;
            }
            return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
        } finally {
            try{ is.close(); } catch(Exception ignored){}
        }
    }

    /**
     * Returns MAC address of the given interface name.
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return  mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) buf.append(String.format("%02X:",aMac));
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }

    public String getAmPmTimeFrom24HoursTime(String string){
        String[] TIME = string.split(":");
        int HOUR =Integer.parseInt(TIME[0]);
        int MIN = Integer.parseInt(TIME[1]);
        int SEC = Integer.parseInt(TIME[2]);
        String AM_PM ;
        if(HOUR>=12){
            if(HOUR>12){
                HOUR = HOUR - 12;
            }
            AM_PM = "PM";
        }else{
            if(HOUR==0){
                HOUR = 12;
            }
            AM_PM = "AM";
        }

        return ""+HOUR+":"+MIN+":"+SEC+" "+AM_PM;
        //return string;
    }

    public void clearDirectoryData(Context context,String path){

        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        if(directory.listFiles() != null){
            File[] files = directory.listFiles();

            Log.d("Files", "Size: "+ files.length);
            for (File file : files) {
                Log.d("Files", "FileName:" + file.getName());
                Log.d("Files PATH", "File Path:" + file.getPath());
                file.delete();
                deleteFile(context,file);
            }
        }
    }

    public void deleteFile(Context context,File file){

        file.delete();
        if(file.exists()){
            try {
                file.getCanonicalFile().delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(file.exists()){
                context.deleteFile(file.getPath());
            }
        }
    }

    public File createDirecotryIfNotExist(File path, String directory){
        File fileDirectory = new File(path + directory);
        try{
            fileDirectory = new File(path + directory);
            if (!fileDirectory.mkdirs()) {
                fileDirectory.mkdirs();
            }
            return fileDirectory;
        }catch (Throwable t){
            return fileDirectory;
        }

    }

    public boolean CheckStorageAndRecordPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
                return false;
            }else{
                // this method is used to check permission
                int result = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
                int result1 = ContextCompat.checkSelfPermission(context, RECORD_AUDIO);
                return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
            }
        }else{
            // this method is used to check permission
            int result = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(context, RECORD_AUDIO);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }

    }

    public void RequestStorageAndRecordPermissions(Context context) {
        // this method is used to request the
        // permission for audio recording and storage.
        ActivityCompat.requestPermissions((Activity) context, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, 1);
    }

    public void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();


        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    private void deleteFile(String inputPath, String inputFile) {
        try {
            // delete the original file
            new File(inputPath + inputFile).delete();
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }
}
