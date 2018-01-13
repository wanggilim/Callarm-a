package project.wgl.callarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by WGL on 2018. 1. 10..
 */

public class AlarmSetupFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "AlarmSetupFragment";

    private Alarm alarm;

    private SharedPreferences as;
    private SwitchPreference sp_repeat;
    private Preference p_date;
    private Preference p_day;
    private TPDialogPreference p_time;
    private RingtonePreference rp_ringtone;
    private SwitchPreference sp_vibe;
    private Preference p_contact;

    private boolean isRepeat;

    private int hourOfDay = 0;
    private int minute = 0;
    private String phoneNumber = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.layout_alarm_setup);
        alarm = new Alarm();

        // 프리퍼런스 생성
        getPreferenceManager().setSharedPreferencesName("setNewAlarm");
        // 프리퍼런스 선언
        as = getActivity().getSharedPreferences("setNewAlarm", 0);
        as.edit().clear();
        sp_repeat = (SwitchPreference) findPreference("key_sp_repeat");
        p_date = findPreference("key_p_date");
        p_day = findPreference("key_p_day");
        p_time = (TPDialogPreference) findPreference("key_p_time");

        rp_ringtone = (RingtonePreference) findPreference("key_rp_ringtone");
        sp_vibe = (SwitchPreference) findPreference("key_sp_vibe");
        p_contact = findPreference("key_p_contact");

        // 선택 반영 리스너
        as.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (s.equals("key_sp_repeat")) {
            Log.d(TAG, "onSharedPreferenceChanged: key_sp_repeat");

            // 알람 반복
            isRepeat = sharedPreferences.getBoolean(s, false);

            if (isRepeat == true) {
                p_date.setEnabled(false);
                p_day.setEnabled(true);
            } else {
                p_date.setEnabled(true);
                p_day.setEnabled(false);
            }
        }

        if (s.equals("key_rp_ringtone")) {
            Log.d(TAG, "onSharedPreferenceChanged: key_rp_ringtone");
        }

        if (s.equals("sp_vibe")) {
            Log.d(TAG, "onSharedPreferenceChanged: sp_vibe");
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
    public void onDestroy() {
        as.unregisterOnSharedPreferenceChangeListener(this);
        as.edit().clear().apply();

        p_time.setPersistent(false);
        super.onDestroy();
    }
}
