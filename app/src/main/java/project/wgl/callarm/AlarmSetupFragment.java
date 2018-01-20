package project.wgl.callarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by WGL on 2018. 1. 10..
 */

public class AlarmSetupFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private static final String TAG = "AlarmSetupFragment";

    private Context context;

    private Alarm alarm;

    private SwitchPreference sp_repeat;
    private DatePDialogPreference p_date;
    private DayPDialogPreference p_day;
    private TPDialogPreference p_time;
    private RingtonePreference rp_ringtone;
    private SwitchPreference sp_vibe;
    private boolean isVibe;
    private Preference p_contact;

    private boolean isRepeat; // 알람 반복 유무

    private String phoneNumber = "";

    private Set<String> ex_daySet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.layout_alarm_setup);
        context = getActivity().getBaseContext();

        alarm = new Alarm();

        // 프리퍼런스 생성
        getPreferenceManager().setSharedPreferencesName("setNewAlarm");
        // 프리퍼런스 선언
        sp_repeat = (SwitchPreference) findPreference("key_sp_repeat");
        sp_repeat.setOnPreferenceClickListener(this);
        p_date = (DatePDialogPreference) findPreference("key_p_date");
        p_date.setPersistent(false);
        p_day = (DayPDialogPreference) findPreference("key_p_day");
        ex_daySet = getPreferenceManager().getSharedPreferences().getStringSet(p_day.getKey(), null);
        p_time = (TPDialogPreference) findPreference("key_p_time");
        p_time.setPersistent(false);

        rp_ringtone = (RingtonePreference) findPreference("key_rp_ringtone");
        sp_vibe = (SwitchPreference) findPreference("key_sp_vibe");
        sp_vibe.setOnPreferenceClickListener(this);
        isVibe = true; // 진동 기본값
        p_contact = findPreference("key_p_contact");

        // 선택 반영 리스너
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (s.equals("key_rp_ringtone")) {
            Log.d(TAG, "onSharedPreferenceChanged: key_rp_ringtone");
        }

        if (s.equals("key_p_date")) {
            Log.d(TAG, "onSharedPreferenceChanged: key_p_date");
        }

        if (s.equals("key_p_day")) {
            Log.d(TAG, "onSharedPreferenceChanged: key_p_day");
        }

        if (s.equals("key_p_time")) {
            Log.d(TAG, "onSharedPreferenceChanged: key_p_time --> " + sharedPreferences.getLong("key_p_time", 0L));
        }

        if (s.equals("key_p_contact")) {
            Log.d(TAG, "onSharedPreferenceChanged: key_p_contact");
        }

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.d(TAG, "onPreferenceClick: ");
        switch (preference.getKey().toString()) {
            case "key_sp_repeat":
                Log.d(TAG, "onPreferenceClick: ");
                // 알람 반복
                isRepeat = getPreferenceManager().getSharedPreferences().getBoolean(preference.getKey().toString(), true);

                if (isRepeat == true) {
                    Log.d(TAG, "onPreferenceClick: isRepeat " + isRepeat);
                    // 요일 선택
                    p_date.setEnabled(false);
                    p_day.setEnabled(true);
                } else {
                    Log.d(TAG, "onPreferenceClick: isRepeat " + isRepeat);
                    // 날짜 선택
                    p_day.setEnabled(false);
                    p_date.setEnabled(true);
                }
                break;

            case "key_sp_vibe":
                Log.d(TAG, "onPreferenceClick: sp_vibe");
                final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                isVibe = !isVibe;
                if (!isVibe) {
                    vibrator.cancel();
                } else {
                    vibrator.vibrate(500);
                }
                break;

            default:
                break;
        }


        return false;
    }


    @Override
    public void onResume() {
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        p_time.setPersistent(false);
        p_date.setPersistent(false);
        p_day.setPersistent(false);
        super.onDestroy();
    }
}
