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

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by WGL on 2018. 1. 10..
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.layout_alarm_setup);
        context = getActivity().getBaseContext();

        setArguments(new Bundle());

        // 프리퍼런스 선언
        // (1)
        sp_repeat = (SwitchPreference) findPreference("key_sp_repeat");
        sp_repeat.setOnPreferenceChangeListener(this);
        /**
         * TODO
         * 설정 알람에 대한 반복 설정 가져오기
         */
        isRepeat = true; // 반복 여부 기본값, 여기와
        sp_repeat.setChecked(true); // 여기까지

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
        p_rv = (RVDialogPreference) findPreference("key_p_rv");
        p_rv.setOnPreferenceChangeListener(this);
        sp_vibe = (SwitchPreference) findPreference("key_sp_vibe");
        sp_vibe.setOnPreferenceChangeListener(this);
        /**
         * TODO
         * 설정 알람에 대한 반복 설정 가져오기
         */
        isVibe = true; // 진동 기본값, 여기와
        sp_vibe.setChecked(true); // 여기까지

        // (5)
        key_pc_5 = (PreferenceGroup) findPreference("key_pc_5");
        p_contact = (CPDialogPreference) findPreference("key_p_contact");
        p_contact.setOnPreferenceChangeListener(this);
        p_spCheck = (SPDialogPreference) findPreference("key_mp_check");
        p_spCheck.setOnPreferenceChangeListener(this);
        if (p_contact.getContacts_cnt() > 1) {
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
                Log.d(TAG, "onPreferenceChange: isRepeat " + isRepeat);
                getArguments().putBoolean("isRepeat", isRepeat);
                break;

            case "key_p_date":
                Log.d(TAG, "onPreferenceChange: key_p_date " + newValue.toString());
                getArguments().putLong("date", Long.parseLong(newValue.toString()));
                break;

            case "key_p_day":
                Log.d(TAG, "onPreferenceChange: key_p_day " + newValue.toString());
                HashSet<String> values = (HashSet) newValue;
                String days = "";
                String summary = "";
                Iterator<String> iter = values.iterator();
                String[] ddd = getResources().getStringArray(R.array.ddd);
                while (iter.hasNext()) {
                    String iterNext = iter.next().toString();
                    days += iterNext;
                    summary += ddd[Integer.parseInt(iterNext)] + " ";
                    Log.d(TAG, "onPreferenceChange: days = " + iterNext + ", summary = " + summary);
                }
                getArguments().putString("days", days);

                /**
                 * TODO
                 * strings
                 */
                String prefDay = "매주 ";
                preference.setSummary(
                        prefDay + summary
                );
                break;

            case "key_p_time":
                Log.d(TAG, "onPreferenceChange: key_p_time " + newValue.toString());
                getArguments().putLong("time", Long.parseLong(newValue.toString()));
                break;

            case "key_rp_ringtone":
                preference.setPersistent(true);
                Log.d(TAG, "onPreferenceChange: key_rp_ringtone " + newValue.toString());
                preference.setDefaultValue(newValue.toString());
                Uri uri = Uri.parse(newValue.toString());
                Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
                rp_ringtone.setSummary(ringtone.getTitle(context));
                getArguments().putString("ringtoneUri", newValue.toString());
                break;

            case "key_p_rv":
                Log.d(TAG, "onPreferenceChange: key_p_rv " + newValue.toString());
                getArguments().putString("volPattern", newValue.toString());
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
                } else {
                    p_contact.setSummary(null);
                    p_spCheck.setEnabled(false);
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
                getArguments().putString("split_ar", newValue.toString());
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

        super.onDestroy();
    }

}
