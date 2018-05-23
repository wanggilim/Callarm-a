package project.wgl.callarm;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.SwitchPreference;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 알람 설정 프래그먼트
 * Created by WGL on 2018. 1. 10..
 *
 * TODO
 * Bundle 에 포함되어있는 Arguments 들을 그대로 불러오기
 * 초기 설정인 경우, 기본값에 해당되는 별도의 static 변수를 사용하여 불러오기
 */

public class AlarmSetupFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    private static final String TAG = "AlarmSetupFragment";

    private Context context;

    private SwitchPreference sp_repeat;
    private DatePDialogPreference p_date;
    private DayPDialogPreference p_day;
    private TPDialogPreference p_time;
    private RPDialogPreference rp_ringtone;
    private SwitchPreference sp_vibe;
    private boolean isVibe;
    private CPDialogPreference p_contact;
    private SPDialogPreference p_spCheck;
    private RVDialogPreference p_rv;
    private PreferenceGroup key_pc_5;

    private boolean isRepeat; // 알람 반복 유무

    public static AlarmSetupFragment newInstance(Bundle args) {
        Log.d(TAG, "AlarmSetupFragment newInstance");

        AlarmSetupFragment fragment = new AlarmSetupFragment();

        if (args == null || args.isEmpty()) {
            Log.d(TAG, "newInstance: args is null or empty");
            args = new Bundle();
        } else {
            Log.d(TAG, "newInstance: args is not null nor empty");
        }
        fragment.setArguments(args);

        return fragment;
    }

    public AlarmSetupFragment() {
        Log.d(TAG, "AlarmSetupFragment: empty constructor");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.layout_alarm_setup); // 이 부분을 띄워야한다.
        context = getActivity().getBaseContext();

        // 프리퍼런스 선언
        // (1)
        sp_repeat = (SwitchPreference) findPreference("key_sp_repeat");
        sp_repeat.setOnPreferenceChangeListener(this);
        /**
         * TODO
         * 설정 알람에 대한 반복 설정 가져오기
         */
        sp_repeat.setChecked(true); // 반복 여부 기본값, 여기와
        getArguments().putBoolean("isRepeat", sp_repeat.isChecked()); // 여기까지

        // (2)
        p_date = (DatePDialogPreference) findPreference("key_p_date");
        p_date.setPersistent(false);
        p_date.setOnPreferenceChangeListener(this);
        p_day = (DayPDialogPreference) findPreference("key_p_day");
        p_day.setPersistent(false);
        p_day.setOnPreferenceChangeListener(this);

        // (3)
        p_time = (TPDialogPreference) findPreference("key_p_time");
        p_time.setPersistent(false);
        p_time.setOnPreferenceChangeListener(this);

        // (4)
        rp_ringtone = (RPDialogPreference) findPreference("key_rp_ringtone");
        rp_ringtone.setOnPreferenceChangeListener(this);
        /**
         * TODO
         * 기본 값 숨기기
         */
        getArguments().putString("ringtoneUri", "content://settings/system/ringtone");
        getArguments().putBoolean("ck_ringtoneUri", true);
        p_rv = (RVDialogPreference) findPreference("key_p_rv");
        p_rv.setOnPreferenceChangeListener(this);
        /**
         * TODO
         * 기본 값 숨기기
         */
        getArguments().putString("volPattern", "80:rb_af_sp");
        getArguments().putBoolean("ck_volPattern", true);
        sp_vibe = (SwitchPreference) findPreference("key_sp_vibe");
        sp_vibe.setOnPreferenceChangeListener(this);
        /**
         * TODO
         * 설정 알람에 대한 반복 설정 가져오기
         */
        isVibe = true; // 진동 기본값, 여기부터
        sp_vibe.setChecked(true);
        getArguments().putBoolean("isVibe", true); // 여기까지

        // (5)
        key_pc_5 = (PreferenceGroup) findPreference("key_pc_5");
        p_contact = (CPDialogPreference) findPreference("key_p_contact");
        p_contact.setOnPreferenceChangeListener(this);
        p_spCheck = (SPDialogPreference) findPreference("key_mp_check");
        p_spCheck.setOnPreferenceChangeListener(this);
        if (p_contact.getContacts_cnt() > 1) {
            getArguments().putBoolean("ck_contactsUri", false);
            p_contact.setEnabled(true);
        } else {
            getPreferenceScreen().removePreference(key_pc_5);

        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        switch (preference.getKey().toString()) {
            case "key_sp_repeat":
                isRepeat = !sp_repeat.isChecked();

                if (isRepeat == true) {
                    // 요일 선택
                    p_date.setEnabled(false);
                    p_day.setEnabled(true);

                } else {
                    // 날짜 선택
                    p_day.setEnabled(false);
                    p_date.setEnabled(true);
                }
                getArguments().putBoolean("isRepeat", isRepeat);
                break;

            case "key_p_date":
                Log.d(TAG, "onPreferenceChange: key_p_date " + newValue.toString());
                getArguments().putLong("date", Long.parseLong(newValue.toString()));
                getArguments().putBoolean("ck_date", true);
                break;

            case "key_p_day":
                Log.d(TAG, "onPreferenceChange: key_p_day " + newValue.toString());
                HashSet<String> values = (HashSet) newValue;

                if (!values.isEmpty()) {
                    Object[] intArr = values.toArray();
                    Arrays.sort(intArr);
                    Log.d(TAG, "onPreferenceChange: intArr.length = " + intArr.length);
                    int dayIndex = 0;
                    String days = "";
                    String summary = "";
                    String[] ddd = getResources().getStringArray(R.array.ddd);

                    for (int i = 0; i < intArr.length; i++) {
                        dayIndex = Integer.parseInt(String.valueOf(intArr[i]));
                        days += dayIndex;
                        summary += ddd[dayIndex] + " ";
                        Log.d(TAG, "onPreferenceChange: day = " + dayIndex + ", days = " + days + ", " + "summary = " + summary);
                    }
                    getArguments().putString("days", days);
                    getArguments().putBoolean("ck_days", true);

                    /**
                     * TODO
                     * strings
                     */
                    String prefDay = "매주 ";
                    preference.setSummary(
                            prefDay + summary
                    );
                } else {
                    /**
                     * TODO
                     * strings
                     */
                    preference.setSummary("요일을 설정합니다");
                    getArguments().putString("days", "");
                    getArguments().putBoolean("ck_days", false);
                }

                break;

            case "key_p_time":
                Log.d(TAG, "onPreferenceChange: key_p_time " + newValue.toString());
                getArguments().putLong("time", Long.parseLong(newValue.toString()));
                getArguments().putBoolean(("ck_time"), true);
                break;

            case "key_rp_ringtone":
                preference.setPersistent(true);
                Log.d(TAG, "onPreferenceChange: key_rp_ringtone " + newValue.toString());
                String result = newValue.toString();
                preference.setDefaultValue(result);
                Uri uri = Uri.parse(result);
                Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
                rp_ringtone.setSummary(ringtone.getTitle(context));
                getArguments().putString("ringtoneUri", result);
                /**
                 * TODO
                 * if 안으로 들어가는 조건이 더 있는지 확인해보기
                 */
                if (result.equals("") || result.equals(null)) {
                    getArguments().putBoolean("ck_ringtoneUri", false);
                } else {
                    getArguments().putBoolean("ck_ringtoneUri", true);
                }

                break;

            case "key_p_rv":
                Log.d(TAG, "onPreferenceChange: key_p_rv " + newValue.toString());
                getArguments().putString("volPattern", newValue.toString());
                getArguments().putBoolean("ck_volPattern", true);
                break;

            case "key_p_contact":
                /**
                 * TODO
                 * strings (+-0)
                 */
                if (!newValue.toString().equals("+-0")) {
                    int index = p_contact.findIndexOfValue(newValue.toString());
                    CharSequence name = p_contact.getEntries()[index];
                    p_contact.setSummary(name.toString() + " (" + newValue.toString() + ")");
                    p_spCheck.setEnabled(true); // 문자 전화 선택 활성화
                    getArguments().putString("contactsUri", newValue.toString());
                    getArguments().putBoolean("ck_contactsUri", true);
                    getArguments().putBoolean("ck_split_ar", false);
                } else {
                    p_contact.setSummary(null);
                    p_spCheck.setEnabled(false);
                    getArguments().putString("contactsUri", null);
                    getArguments().putBoolean("ck_contactsUri", false);
                    getArguments().putBoolean("ck_split_ar", false);
                }
                break;

            case "key_sp_vibe":
                Log.d(TAG, "onPreferenceChange: sp_vibe");
                final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                isVibe = !isVibe;
                if (!isVibe) {
                    vibrator.cancel();
                } else {
                    vibrator.vibrate(500);
                }
                getArguments().putBoolean("isVibe", isVibe);
                break;

            case "key_mp_check":
                Log.d(TAG, "onPreferenceChange: mp_check");
                if (!newValue.toString().equals("")) {
                    getArguments().putString("split_ar", newValue.toString());
                    getArguments().putBoolean("ck_split_ar", true);
                } else {
                    getArguments().putString("split_ar", newValue.toString());
                    getArguments().putBoolean("ck_split_ar", false);
                }
                break;
        }

        return true;
    }


    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        p_date.setPersistent(false);
        p_day.setPersistent(false);
        p_day.getPreferenceManager().getSharedPreferences().edit().clear().apply();

        p_time.setPersistent(false);

        rp_ringtone.setPersistent(false);
        rp_ringtone.getPreferenceManager().getSharedPreferences().edit().clear().apply();
        p_rv.setPersistent(false);

        p_contact.setPersistent(false);
        p_spCheck.setPersistent(false);

        key_pc_5.setPersistent(false);

        getPreferenceManager().getSharedPreferences().edit().clear().apply();
        getArguments().clear();

        super.onDestroy();
    }
}
