package project.wgl.callarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by WGL on 2018. 1. 10..
 */

public class AlarmSetupFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "AlarmSetupFragment";

    private Alarm alarm;

    private SharedPreferences as;
    private SwitchPreference sp_repeat;
    private Preference p_date;
    private Preference p_day;
    private RingtonePreference rp_ringtone;
    private SwitchPreference sp_vibe;
    private Preference p_contact;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.layout_alarm_setup);
        alarm = new Alarm();

        // 프리퍼런스 생성
        getPreferenceManager().setSharedPreferencesName("setNewAlarm");
        // 프리퍼런스 선언
        as = getActivity().getSharedPreferences("setNewAlarm", 0);
        sp_repeat = (SwitchPreference) findPreference("key_sp_repeat");
        p_date = findPreference("key_p_date");
        p_day = findPreference("key_p_day");
        rp_ringtone = (RingtonePreference) findPreference("key_rp_ringtone");
        sp_vibe = (SwitchPreference) findPreference("key_sp_vibe");
        p_contact = findPreference("key_p_contact");

        // 선택 반영 리스너
        as.registerOnSharedPreferenceChangeListener(this);

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        switch (preference.getKey()) {
            case "key_sp_repeat":
                Log.d(TAG, "onPreferenceChange: " + findPreference("key_sp_repeat"));
                break;
            case "key_rp_ringtone":
                Log.d(TAG, "onPreferenceChange: " + findPreference("key_rp_ringtone"));
                break;
            case "sp_vibe":
                Log.d(TAG, "onPreferenceChange: " + findPreference("sp_vibe"));
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "key_p_date":
                Log.d(TAG, "onPreferenceClick: key_p_date");
                break;
            case "key_p_day":
                Log.d(TAG, "onPreferenceClick: key_p_day");
                break;
            case "key_p_time":
                Log.d(TAG, "onPreferenceClick: key_p_time");
                break;
            case "key_p_contact":
                Log.d(TAG, "onPreferenceClick: key_p_contact");
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
}
