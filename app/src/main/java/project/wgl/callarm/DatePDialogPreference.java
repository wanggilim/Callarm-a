package project.wgl.callarm;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by WGL on 2018. 1. 19..
 */

public class DatePDialogPreference extends DialogPreference implements DatePicker.OnDateChangedListener {
    private final static String TAG = "DatePDialogPreference";
    private Context context;
    private DatePDialogHolder holder;

    private Calendar calendar;
    private long time;

    public DatePDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        holder = new DatePDialogHolder();
    }

    //////////////////////////////////////////////////////////////////

    private void initCalendar() {
        Calendar calendar = Calendar.getInstance();
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    private void setCalendar(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        this.calendar = calendar;
    }

    private void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    private void setTime(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = getCalendar();
        calendar.set(year, monthOfYear, dayOfMonth);

        time = calendar.getTimeInMillis();
    }

    private void setTime(long time) {
        this.time = time;
    }

    public long getTime(){
        return time;
    }

    //////////////////////////////////////////////////////////////////

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_picker_date, null);
        holder.dp = (DatePicker) view.findViewById(R.id.dp);

        initCalendar();
        setTime(getCalendar().getTimeInMillis());
        holder.dp.setMinDate(getTime());

        if (isPersistent()) {
            // 등록 상태
            Log.d(TAG, "onPrepareDialogBuilder: 등록 상태");
            setCalendar(getPersistedLong(time));
        }

        setTime(getCalendar().getTimeInMillis());

        holder.dp.init(getCalendar().get(Calendar.YEAR),
                getCalendar().get(Calendar.MONTH),
                getCalendar().get(Calendar.DAY_OF_MONTH),
                this);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            Log.d(TAG, "onCreateView: under Nougat");
            holder.dp.setSpinnersShown(true);
            holder.dp.setCalendarViewShown(false);
        }
        builder.setView(view);

        super.onPrepareDialogBuilder(builder);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult == true) {
            // 확인 = 저장
            /**
             * TODO
             * -- 테스트 필요 --
             * 자정이 지나서 새로 날을 지정해야할 상황이 올 때
             * 예외처리가 잘 되는지 확인하기
             */
            if (holder.dp.getMinDate() > getTime()) {
                Toast.makeText(context, "새로 날짜를 선택해야합니다", Toast.LENGTH_SHORT).show();
            } else {
                setPersistent(true);
                persistLong(time);
                setCalendar(getTime());
                // 저장 확인
                String summary = getCalendar().get(Calendar.YEAR) + "년 "
                        + (getCalendar().get(Calendar.MONTH)+1) + "월 "
                        + getCalendar().get(Calendar.DAY_OF_MONTH) + "일 ";
                setSummary(summary);

                super.onDialogClosed(positiveResult);
            }

        } else {
            // 취소
            super.onDialogClosed(positiveResult);

        }

    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d(TAG, "onDateChanged: " + year + "/" + monthOfYear + "/" + dayOfMonth);
        setTime(year, monthOfYear, dayOfMonth);
    }
}

class DatePDialogHolder {
    DatePicker dp;
}
