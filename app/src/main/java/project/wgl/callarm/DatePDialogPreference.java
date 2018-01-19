package project.wgl.callarm;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by WGL on 2018. 1. 19..
 */

public class DatePDialogPreference extends DialogPreference implements DatePicker.OnDateChangedListener, View.OnClickListener {
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
        holder.btn_dp_today = (Button) view.findViewById(R.id.btn_dp_today);
        holder.btn_dp_today.setOnClickListener(this);
        holder.btn_dp_tomorrow = (Button) view.findViewById(R.id.btn_dp_tomorrow);
        holder.btn_dp_tomorrow.setOnClickListener(this);
        holder.btn_dp_next_week = (Button) view.findViewById(R.id.btn_dp_next_week);
        holder.btn_dp_next_week.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dp_today:
                initCalendar();
                setTime(getCalendar().getTimeInMillis());
                holder.dp.setMinDate(getTime());
                holder.dp.updateDate(getCalendar().get(Calendar.YEAR),
                        getCalendar().get(Calendar.MONTH),
                        getCalendar().get(Calendar.DAY_OF_MONTH));
                break;

            case R.id.btn_dp_tomorrow:
                initCalendar();
                Calendar calendar = getCalendar();
                holder.dp.setMinDate(calendar.getTimeInMillis());
                calendar.add(Calendar.MILLISECOND, 1000*86400);
                holder.dp.updateDate(getCalendar().get(Calendar.YEAR),
                        getCalendar().get(Calendar.MONTH),
                        getCalendar().get(Calendar.DAY_OF_MONTH));
                break;

            case R.id.btn_dp_next_week:
                initCalendar();
                Calendar calendar2 = getCalendar();
                holder.dp.setMinDate(calendar2.getTimeInMillis());
                calendar2.add(Calendar.MILLISECOND, 1000*7*86400);
                holder.dp.updateDate(getCalendar().get(Calendar.YEAR),
                        getCalendar().get(Calendar.MONTH),
                        getCalendar().get(Calendar.DAY_OF_MONTH));
                break;

            default:
                break;
        }

    }
}

class DatePDialogHolder {
    DatePicker dp;
    Button btn_dp_today;
    Button btn_dp_tomorrow;
    Button btn_dp_next_week;
}
