
package com.sariidaman.giovan.helper;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.sariidaman.giovan.Splash;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.sariidaman.giovan.helper.GlobalVariabel.PICK_IMAGE;

public class Widget {

    public static void showMessageBox(final Context ctx, String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setPositiveButton(Html.fromHtml("<font color='#000000'>OK</font>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //((Activity) ctx).finish();
            }
        });
        builder.setMessage(Message);
        builder.show();
    }

    public static void showMessageBoxErrorServer(final Context ctx){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        builder.setTitle("Error");
        builder.setIcon(android.R.drawable.stat_notify_error);
        builder.setPositiveButton(Html.fromHtml("<font color='#000000'>ok</font>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //((Activity) ctx).finish();
            }
        });
        builder.setMessage("terjadi kesalahan dalam server");
        builder.show();
    }

    public static void showMessageBoxExit(final Context ctx, String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setPositiveButton(Html.fromHtml("<font color='#000000'>OK</font>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((Activity) ctx).finish();
            }
        });
        builder.setMessage(Message);
        builder.show();
    }
    public static void showMessageBoxLogout(final Context ctx, String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setPositiveButton(Html.fromHtml("<font color='#000000'>OK</font>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences settings = ctx.getSharedPreferences("myPref", Context.MODE_PRIVATE);
                settings.edit().clear().commit();
                Intent mStartActivity = new Intent(ctx, Splash.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(ctx, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                System.exit(0);
            }
        });
        builder.setMessage(Message);
        builder.show();
    }

    public static void showMessageBoxNoExit(final Context ctx, String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setPositiveButton(Html.fromHtml("<font color='#000000'>OK</font>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //((Activity) ctx).finish();
            }
        });
        builder.setMessage(Message);
        builder.show();
    }




    public static void setTimePicker(Context context, final EditText etTime){
        // TODO Auto-generated method stub
        final int[] jam = new int[1];
        final int[] menit = new int[1];
        final Calendar myCalendar = Calendar.getInstance();
        jam[0] =myCalendar.get(Calendar.HOUR_OF_DAY);
        menit[0] =myCalendar.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, AlertDialog.THEME_TRADITIONAL, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timePicker.clearFocus();
                jam[0] =timePicker.getCurrentHour();
                menit[0] =timePicker.getCurrentMinute();
                //etTime.setText(String.format("%02d:%02d", jam[0], menit[0]));

                etTime.setText(String.format("%02d:%02d", jam[0], menit[0]));
            }
        }, jam[0], menit[0], true);//Yes 24 hour time


        //mTimePicker.se;
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    public static void setDatePicker(Context context, final EditText etDate){
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                etDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK, dateListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
/*        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, -0); // subtract 2 years from now
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        cal.add(Calendar.DATE, 1); // add 4 years to min date to have 2 years after now
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());*/

        datePickerDialog.getDatePicker();
        datePickerDialog.show();
    }





}
