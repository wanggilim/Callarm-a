package project.wgl.callarm;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * 알람 설정 프래그먼트를 감싸는 액티비티
 * Created by WGL on 2018. 1. 10..
 */

public class AlarmSetupActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private static final String TAG = "AlarmSetupActivity";

    private AlarmSetupHolder holder;
    private Bundle args;
    private Fragment fragment;

    public AlarmSetupActivity() {
        holder = new AlarmSetupHolder();
        args = new Bundle();
    }

    public AlarmSetupActivity(Bundle args) {
        holder = new AlarmSetupHolder();
        this.args = args;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // Fragment newInstance
        fragment = AlarmSetupFragment.newInstance(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.fl_fragment_container, fragment)
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

    /**
     * 데이터베이스 저장
     */
    private void save(Bundle bundle) {

        // (2) 저장할 컬럼명 지정
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

        // (3) 알람 켜기/끄기 설정 -- 저장할 것이기 때문에 자동으로 켜지기
        final int isOn_int = 1;


        // (4) 반복 켜기/끄기 설정 + time_l 세팅 및 변환 준비
        Calendar ymd = Calendar.getInstance();
        Calendar hms = Calendar.getInstance();

        ymd.setTimeInMillis(bundle.getLong("date"));
        hms.setTimeInMillis(bundle.getLong("time"));

        int isRepeat_int = bundle.getBoolean("isRepeat")? 1 : 0;
        String days = null;    // 반복할 요일들 (기본값 : null)
        Calendar dt = Calendar.getInstance(); // long 타입 시간 저장하기 위한 Calendar 객체
        long time_l = 0l; // long 타입의 시간

        // (5)-1 요일 반복
        if (isRepeat_int == 1) {
            days = bundle.getString("days");
            Log.d(TAG, "onMenuItemClick: days ==> " + days);

            dt.set(Calendar.HOUR_OF_DAY, hms.get(Calendar.HOUR_OF_DAY));
            dt.set(Calendar.MINUTE, hms.get(Calendar.MINUTE));
            dt.set(Calendar.SECOND, 0);
            dt.set(Calendar.MILLISECOND, 0);

            // (6)-2 일회용 설정
        } else {
            dt.set(ymd.get(Calendar.YEAR), ymd.get(Calendar.MONTH), ymd.get(Calendar.DAY_OF_MONTH),
                    hms.get(Calendar.HOUR_OF_DAY), hms.get(Calendar.MINUTE), 0);
            dt.set(Calendar.MILLISECOND, 0);
        }

        // (6)-공통 time_l 최종 결정
        time_l = dt.getTimeInMillis();

        // (7) 진동 켜기/끄기 설정
        int isVibe_int = bundle.getBoolean("isVibe")? 1 : 0;

        // (8) 벨소리 uri
        String ringtoneUri = bundle.getString("ringtoneUri");

        // (9) 알람의 소리 세부설정 정보
        String volPattern = bundle.getString("volPattern");

        // (10) 연락처 정보
        String contactsUri = bundle.getString("contactsUri");

        // (11) 알람 활동시 동작 설정
        String split_ar = bundle.getString("split_ar");

        // (12) 데이터베이스 저장
        DatabaseOpenHelper helper = new DatabaseOpenHelper(getBaseContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        helper.create(db,
                columnNames,
                isOn_int, isRepeat_int,
                time_l, days,
                isVibe_int, ringtoneUri,
                volPattern, contactsUri,
                split_ar);


        // ** 테스트 코드
        Toast.makeText(getBaseContext(),
                "까꿍! " + bundle.size(),
                Toast.LENGTH_SHORT).show();

        //finishActivity(code);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            /**
             * TODO
             * strings
             */
            case R.id.item_save_alarm:
                /**
                 *  기본 설정 부분 : 진동, 반복, 벨소리 선택, 알람 볼륨과 증감 조절
                 *  설정이 꼭 필요한 부분
                 *  1. 반복 여부 + 시간/요일 설정 + 시간 설정
                 *  2. 1번과 연락처 설정 + 텍스트 설정
                 */

                // (1) 프래그먼트 객체와 그의 번들 객체를 선언
                final Bundle bundle = fragment.getArguments();

                // (1-2) 분기점 걸릴 시 나올 AlertDialog 와, 저장시 확인되는 분기점
                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmSetupActivity.this, R.style.Theme_AppCompat_Dialog);


                /**
                 * TODO
                 * strings
                 */
                if (!bundle.getBoolean("isRepeat") && !bundle.getBoolean("ck_date")) {
                    builder.setMessage("날짜를 선택하세요");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: AlertDialog 종료");
                        }
                    });
                    builder.show();
                    break;

                } else if (bundle.getBoolean("isRepeat") && !bundle.getBoolean("ck_days")) {
                    builder.setMessage("요일을 선택하세요");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: AlertDialog 종료");
                        }
                    });
                    builder.show();
                    break;

                } else if (!bundle.getBoolean("ck_time")) {
                    builder.setMessage("시간을 선택하세요");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: AlertDialog 종료");
                        }
                    });
                    builder.show();
                    break;

                } else if (!bundle.getBoolean("ck_ringtoneUri")) {
                    builder.setMessage("정말 벨소리를 선택하지 않으시겠습니까?");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: AlertDialog 예 종료");
                            save(bundle);
                            finish();
                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: AlertDialog 아니오 종료");
                        }
                    });
                    builder.show();
                    break;

                } else if (bundle.containsKey("ck_contactsUri")) {
                    if (!bundle.getBoolean("ck_contactsUri")) {
                        builder.setMessage("정말 연락처를 선택하지 않으시겠습니까?");
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: AlertDialog 예 종료");
                                save(bundle);
                                finish();
                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: AlertDialog 아니오 종료");
                            }
                        });
                        builder.show();
                        break;
                    }
                    if (!bundle.getBoolean("ck_split_ar")) {
                        builder.setMessage("전화나 문자메시지를 설정하지 않았습니다.");
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: AlertDialog 종료");
                            }
                        });
                        builder.show();
                        break;
                    }
                }

                save(bundle);
                finish();
                break;

            default:
                break;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: AlarmSetupActivity onDestroy");
        super.onDestroy();
    }
}

class AlarmSetupHolder {
    Toolbar toolbar;
}
