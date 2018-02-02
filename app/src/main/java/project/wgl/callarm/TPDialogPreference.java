package project.wgl.callarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by WGL on 2018. 1. 13..
 */

public class TPDialogPreference extends DialogPreference
        implements TimePicker.OnTimeChangedListener {

    private final static String TAG = "TimePickerDP";

    private Intent intent;
    private TPDialogHolder tpHolder;
    private Context context;

    private long timeInMillis;
    private int hourOfDay;
    private int minute;

    public TPDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        tpHolder = new TPDialogHolder();
    }


    @Override
    protected View onCreateDialogView() {
        Log.d(TAG, "onCreateDialogView: ");
        tpHolder.tp = new TimePicker(context);

        return tpHolder.tp;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        Log.d(TAG, "onPrepareDialogBuilder: ");
        tpHolder.tp.setOnTimeChangedListener(this);
        if (isPersistent()) {
            Calendar calendar = Calendar.getInstance();
            timeInMillis = getPersistedLong(calendar.getTimeInMillis());

            Log.d(TAG, "onPrepareDialogBuilder: isPersistent() " + timeInMillis);
            calendar.setTimeInMillis(timeInMillis);

            if (Build.VERSION.SDK_INT >= 23) {
                tpHolder.tp.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                tpHolder.tp.setMinute(calendar.get(Calendar.MINUTE));
            } else {
                tpHolder.tp.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                tpHolder.tp.setCurrentMinute(calendar.get(Calendar.MINUTE));
            }

        } else {
            Log.d(TAG, "onPrepareDialogBuilder: ! isPersistent()");
            Calendar calendar = Calendar.getInstance();
            hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            timeInMillis = calendar.getTimeInMillis();
        }

        super.onPrepareDialogBuilder(builder);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        //Log.d(TAG, "onTimeChanged: " + hourOfDay + ":" + minute);
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        Log.d(TAG, "onDialogClosed: " + positiveResult);

        if (positiveResult == true) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            timeInMillis = calendar.getTimeInMillis();

            setPersistent(true);
            persistLong(timeInMillis);

            String str_ampm = "";
            if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                str_ampm = "오전 ";
            } else {
                str_ampm = "오후 ";
            }

            // 시간 (12시간제 표기)
            String str_hour = "";
            if (hourOfDay < 10) {
                str_hour = "0" + String.valueOf(calendar.get(Calendar.HOUR));
            } else if (hourOfDay >= 10 && hourOfDay <= 12) {
                str_hour = String.valueOf(hourOfDay);
            } else if (hourOfDay > 12 && hourOfDay < 22) {
                str_hour = "0" + String.valueOf(hourOfDay - 12);
            } else {
                str_hour = String.valueOf(hourOfDay - 12);
            }



            // 분
            String str_minute = "";
            if (minute < 10) {
                str_minute = "0" + String.valueOf(minute);
            } else {
                str_minute = String.valueOf(minute);
            }

            // 12시간제 표기 결과
            setSummary(str_ampm + str_hour + ":" + str_minute);

            Log.d(TAG, "onDialogClosed: getTimeInMillis = " + timeInMillis);
        }

        super.onDialogClosed(positiveResult);
    }
}

class TPDialogHolder {
    TimePicker tp;
}
