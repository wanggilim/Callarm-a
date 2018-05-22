package project.wgl.callarm;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListHolder> {
    private static final String TAG = "AlarmListAdapter";

    private Context context;
    private ArrayList<Alarm> alarms;

    public AlarmListAdapter() {
        Log.d(TAG, "AlarmListAdapter: empty constructor");
    }

    public AlarmListAdapter(Context context, ArrayList<Alarm> alarms) {
        Log.d(TAG, "AlarmListAdapter: constructor with param");
        this.context = context;
        this.alarms = alarms;
    }

    @Override
    public AlarmListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        return new AlarmListHolder(context, parent);
    }

    @Override
    public void onBindViewHolder(AlarmListHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Alarm alarm = getItem(position);

        boolean isOn = alarm.isOn();
        if (isOn == true) {
            holder.iv_cell.setBackgroundColor(Color.GREEN); // 알람 켬
        } else {
            holder.iv_cell.setBackgroundColor(Color.GRAY); // 알람 끔
        }
        holder.sw_cell_alarm.setChecked(isOn); // 알람 켬 끔에 따라 스위치 위젯 설정

        //holder.tv_cell_target.setText(alarm.getContact().toString()); // 알람 전화번호 출력 (null 해결하기)
        //holder.tv_cell_details.setText(); // 문자메시지, 전화 설정 유무
        holder.tv_cell_ringtone.setText(alarm.getRingtoneUri()); // 벨소리 제목
        //holder.tv_cell_time.setText(); // 설정 시간
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "getItemId: ");
        return position;
    }

    public Alarm getItem(int position) {
        Log.d(TAG, "getItem: ");
        return alarms.get(position);
    }
}


class AlarmListHolder extends RecyclerView.ViewHolder implements Switch.OnClickListener {
    private final static String TAG = "AlarmListHolder";

    ImageView iv_cell;
    TextView tv_cell_target;
    TextView tv_cell_details;
    TextView tv_cell_ringtone;
    Switch sw_cell_alarm;
    TextView tv_cell_time;

    public AlarmListHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.cell_list_alarm, parent, false));
        Log.d(TAG, "AlarmListHolder: ");

        iv_cell = itemView.findViewById(R.id.iv_cell);
        tv_cell_target = itemView.findViewById(R.id.tv_cell_target);
        tv_cell_details = itemView.findViewById(R.id.tv_cell_details);
        tv_cell_ringtone = itemView.findViewById(R.id.tv_cell_ringtone);
        sw_cell_alarm = itemView.findViewById(R.id.sw_cell_alarm);
        sw_cell_alarm.setOnClickListener(this);
        tv_cell_time = itemView.findViewById(R.id.tv_cell_time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sw_cell_alarm:
                Log.d(TAG, "onClick: ");
                if (sw_cell_alarm.isChecked()) {
                    iv_cell.setBackgroundColor(Color.GREEN);
                } else {
                    iv_cell.setBackgroundColor(Color.GRAY);
                }
                break;

            default:
                break;
        }

    }
}
