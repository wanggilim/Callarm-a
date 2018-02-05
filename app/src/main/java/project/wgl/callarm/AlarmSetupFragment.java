package project.wgl.callarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by WGL on 2018. 1. 10..
 */

public class AlarmSetupFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    private static final String TAG = "AlarmSetupFragment";

    private Context context;

    private Alarm alarm;

    private SharedPreferences as;

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
        //getPreferenceManager().setSharedPreferencesName("setNewAlarm");
        as = context.getSharedPreferences("setNewAlarm", 0);
        // 프리퍼런스 선언
        sp_repeat = (SwitchPreference) findPreference("key_sp_repeat");
        sp_repeat.setOnPreferenceChangeListener(this);
        /**
         * TODO
         * 설정 알람에 대한 반복 설정 가져오기
         */
        isRepeat = true; // 반복 여부 기본값, 여기와
        sp_repeat.setChecked(true); // 여기까지

        p_date = (DatePDialogPreference) findPreference("key_p_date");
        p_date.setPersistent(false);

        p_day = (DayPDialogPreference) findPreference("key_p_day");

        ex_daySet = getPreferenceManager().getSharedPreferences().getStringSet(p_day.getKey(), null);

        p_time = (TPDialogPreference) findPreference("key_p_time");
        p_time.setPersistent(false);

        //rp_ringtone = (RingtonePreference) findPreference("key_rp_ringtone");
        rp_ringtone = (RPDialogPreference) findPreference("key_rp_ringtone");
        rp_ringtone.setOnPreferenceChangeListener(this);
        Uri ringtoneUri = Uri.parse(as.getString(rp_ringtone.getKey(),""));
        RingtoneManager manager = new RingtoneManager(context);
        Log.d(TAG, "onRestoreRingtone: " + manager.getRingtone(context, ringtoneUri).getTitle(context));

        sp_vibe = (SwitchPreference) findPreference("key_sp_vibe");
        sp_vibe.setOnPreferenceChangeListener(this);
        /**
         * TODO
         * 설정 알람에 대한 반복 설정 가져오기
         */
        isVibe = true; // 진동 기본값, 여기와
        sp_vibe.setChecked(true); // 여기까지

        p_contact = (CPDialogPreference) findPreference("key_p_contact");
        p_contact.setOnPreferenceChangeListener(this);

        p_spCheck = (SPDialogPreference) findPreference("key_mp_check");
        p_spCheck.setOnPreferenceChangeListener(this);

        p_rv = (RVDialogPreference) findPreference("key_rv");
        p_rv.setOnPreferenceChangeListener(this);

        // 선택 반영 리스너
        //getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        as.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(TAG, "onSharedPreferenceChanged: ");
        
        if (s.equals("key_p_date")) {
            Log.d(TAG, "onSharedPreferenceChanged: key_p_date");
        }

        if (s.equals("key_p_day")) {
            Log.d(TAG, "onSharedPreferenceChanged: key_p_day");
        }

        if (s.equals("key_p_time")) {
            Log.d(TAG, "onSharedPreferenceChanged: key_p_time --> " + sharedPreferences.getLong("key_p_time", 0L));
        }

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        switch (preference.getKey().toString()) {
            case "key_sp_repeat":
                //isRepeat = getPreferenceManager().getSharedPreferences().getBoolean(preference.getKey().toString(), true);
                isRepeat = !sp_repeat.isChecked();

                if (isRepeat == true) {
                    Log.d(TAG, "onPreferenceChange: isRepeat " + isRepeat);
                    // 요일 선택
                    p_date.setEnabled(false);
                    p_day.setEnabled(true);
                } else {
                    Log.d(TAG, "onPreferenceChange: isRepeat " + isRepeat);
                    // 날짜 선택
                    p_day.setEnabled(false);
                    p_date.setEnabled(true);
                }
                break;

            case "key_rp_ringtone":
                preference.setPersistent(true);
                Log.d(TAG, "onPreferenceChange: " + newValue.toString());
                preference.setDefaultValue(newValue.toString());
                Uri uri = Uri.parse(newValue.toString());
                Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
                rp_ringtone.setSummary(ringtone.getTitle(context));

                break;

            case "key_p_contact":
                int index = p_contact.findIndexOfValue(newValue.toString());
                CharSequence name = p_contact.getEntries()[index];
                p_contact.setSummary(name.toString() + " (" + newValue.toString() + ")");
                p_spCheck.setEnabled(true); // 문자 전화 선택 활성화
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
                break;
        }

        return true;
    }


    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        //getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        as.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        //getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        as.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        p_time.setPersistent(false);
        p_date.setPersistent(false);
        p_day.setPersistent(false);
        rp_ringtone.setPersistent(false);
        rp_ringtone.getPreferenceManager().getSharedPreferences().edit().clear().apply();
        p_contact.setPersistent(false);
        p_spCheck.setPersistent(false);
        p_rv.setPersistent(false);

        //getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        as.unregisterOnSharedPreferenceChangeListener(this);

        super.onDestroy();
    }

}
