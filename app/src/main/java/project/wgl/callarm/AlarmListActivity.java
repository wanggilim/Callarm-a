package project.wgl.callarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * 알람 설정 확인 리스트
 * Created by WGL on 2018. 1. 10..
 */
public class AlarmListActivity extends AppCompatActivity {
    private static final String TAG = "AlarmListActivity";

    private AlarmListHolder holder;

    public AlarmListActivity() {
        holder = new AlarmListHolder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: AlarmListActivity");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm_list);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

class AlarmListHolder {

}
