package project.wgl.callarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by WGL on 2018. 1. 15..
 */

public class DayPDialogPreference extends MultiSelectListPreference {
    private static final String TAG = "DayPDialogPreference";
    private Context context;

    public DayPDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        return super.onCreateView(parent);
    }

    @Override
    protected View onCreateDialogView() {
        return super.onCreateDialogView();
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        setEntries(context.getResources().getStringArray(R.array.ddd));
        setEntryValues(context.getResources().getStringArray(R.array.ddd_result));
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        Log.d(TAG, "onDialogClosed: " + positiveResult);
        if (positiveResult) {
            Set<String> getDaySet = getSharedPreferences().getStringSet(this.getKey(), null);
            //Set<String> getDaySet = sharedPreferences.getStringSet(key, new HashSet<String>());//도 됨!

            if (getDaySet == null || getDaySet.size() == 0) {
                /**
                 * TODO
                 * (충분한 테스트) + strings
                 */
                setSummary("요일을 설정합니다");
                Log.d(TAG, "onDialogClosed: setSummary Init");
            } else {
                String day = "";

                // int형 ArrayList로 형변환
                ArrayList<Integer> i_days = new ArrayList<Integer>();
                for (String s1 : getDaySet) {
                    i_days.add(Integer.valueOf(s1));
                }

                // int형 배열로 변환 + 정렬
                int[] sort = new int[i_days.size()];
                for (int i = 0; i < sort.length; i++) {
                    sort[i] = i_days.get(i).intValue();
                    Log.d(TAG, "onDialogClosed: sort[" + i + "] " + sort[i]);
                }

                Arrays.sort(sort);

                for (int i = 0; i < sort.length; i++) {
                    day += getEntries()[sort[i]] + " ";
                    Log.d(TAG, "onDialogClosed: day 목록 => " + day);
                }

                String prefDay = "매주 ";
                String postDay = "마다";
                setSummary(
                        prefDay + day + postDay
                );

            }

            Log.d(TAG, "onDialogClosed: 완료");
        }
    }
}
