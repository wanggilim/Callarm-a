package project.wgl.callarm;

import android.Manifest;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by WGL on 2018. 1. 10..
 */

public class AlarmSetupActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private static final String TAG = "AlarmSetupActivity";

    private AlarmSetupHolder holder;


    public AlarmSetupActivity() {
        holder = new AlarmSetupHolder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.SEND_SMS
            };

            Intent intent = new Intent(getApplicationContext(), PermissionsRequestActivity.class);
            intent.putExtra("str_arr_perms", perms);
            startActivityForResult(intent, 0);

        } else {
            start();
        }

    }

    private void start() {
        setContentView(R.layout.activity_alarm_setup);

        holder.toolbar = findViewById(R.id.tb_setup_alarm);
        holder.toolbar.inflateMenu(R.menu.set_alarm);
        holder.toolbar.setOnMenuItemClickListener(this);
        /**
         * TODO
         * strings
         */
        holder.toolbar.setTitle("알람 설정");
        holder.toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().hide(); // 기존에 있던 액션바 숨기기

        getFragmentManager().beginTransaction()
                .addToBackStack("AlarmSetupFragment")
                .replace(R.id.fragment_alarm_setup, new AlarmSetupFragment())
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: RESULT_OK");
            start();
        }

        if (resultCode == RESULT_CANCELED) {
            moveTaskToBack(true);
            finish();
            Log.d(TAG, "onActivityResult: RESULT_CANCELED");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack();
        Log.d(TAG, "onBackPressed: ");
        super.onBackPressed();
    }

    //////////////////////////////////////////////////

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        /**
         * TODO
         * strings
         */
        switch (item.getItemId()) {
            case R.id.item_save_alarm:

                // (1) 프래그먼트 객체와 그의 번들 객체를 선언
                Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_alarm_setup);
                Bundle bundle = fragment.getArguments();

                // ** 테스트 코드
                Toast.makeText(getBaseContext(),
                        "까꿍! " + fragment.getArguments().size(),
                        Toast.LENGTH_SHORT).show();

                // () 저장할 컬럼명 지정
                ArrayList<String> al = new ArrayList<>();
                Iterator<String> iter = bundle.keySet().iterator();
                while (iter.hasNext()) {
                    al.add(iter.next().toString());
                }

                String columnNames = "";
                for (int i = 0; i < al.size(); i++) {
                    columnNames += al.get(i).toString();

                    if (i != al.size()-1) {
                        // 마지막 컬럼이 아니라면
                        columnNames += ", ";
                    }
                }

                // (1) 알람 켜기/끄기 설정 -- 저장할 것이기 때문에 자동으로 켜지기
                final int isOn_int = 1;


                // (2) 반복 켜기/끄기 설정 + time_l 세팅 및 변환 준비
                Calendar ymd = Calendar.getInstance();
                Calendar hms = Calendar.getInstance();

                ymd.setTimeInMillis(bundle.getLong("date"));
                hms.setTimeInMillis(bundle.getLong("time"));

                int isRepeat_int = bundle.getBoolean("isRepeat")? 1 : 0;
                String days = null;    // 반복할 요일들 (기본값 : null)
                Calendar dt = Calendar.getInstance(); // long 타입 시간 저장하기 위한 Calendar 객체
                long time_l = 0l; // long 타입의 시간

                // (3)-1 요일 반복
                if (isRepeat_int == 1) {
                    days = bundle.getString("days");
                    Log.d(TAG, "onMenuItemClick: days ==> " + days);

                    dt.set(Calendar.HOUR_OF_DAY, hms.get(Calendar.HOUR_OF_DAY));
                    dt.set(Calendar.MINUTE, hms.get(Calendar.MINUTE));
                    dt.set(Calendar.SECOND, 0);
                    dt.set(Calendar.MILLISECOND, 0);

                // (3)-2 일회용 설정
                } else {
                    dt.set(ymd.get(Calendar.YEAR), ymd.get(Calendar.MONTH), ymd.get(Calendar.DAY_OF_MONTH),
                            hms.get(Calendar.HOUR_OF_DAY), hms.get(Calendar.MINUTE), 0);
                    dt.set(Calendar.MILLISECOND, 0);
                }

                // (3)-공통 time_l 최종 결정
                time_l = dt.getTimeInMillis();

                // (4) 진동 켜기/끄기 설정
                int isVibe_int = bundle.getBoolean("isVibe")? 1 : 0;

                // (5) 벨소리 uri
                String ringtoneUri = bundle.getString("ringtoneUri");

                // (6) 알람의 소리 세부설정 정보
                String volPattern = bundle.getString("volPattern");

                // (7) 연락처 정보
                String contactsUri = bundle.getString("contactsUri");

                // (8) 알람 활동시 동작 설정
                String split_ar = bundle.getString("split_ar");

                // (9) 데이터베이스 저장
                DatabaseOpenHelper helper = new DatabaseOpenHelper(getBaseContext());
                SQLiteDatabase db = helper.getWritableDatabase();

                helper.create(db,
                        columnNames,
                        isOn_int, isRepeat_int,
                        time_l, days,
                        isVibe_int, ringtoneUri,
                        volPattern, contactsUri,
                        split_ar);

                break;
            default:
                break;
        }
        return false;
    }
}

class AlarmSetupHolder {
    Toolbar toolbar;
}
