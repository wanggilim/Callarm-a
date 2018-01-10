package project.wgl.callarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by WGL on 2018. 1. 10..
 */

public class AlarmSetupActivity extends PreferenceActivity {
    private static final String TAG = "AlarmSetupActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setup);

        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_alarm_setup, new AlarmSetupFragment())
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
