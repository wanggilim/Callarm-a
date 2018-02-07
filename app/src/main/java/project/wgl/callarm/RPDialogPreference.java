package project.wgl.callarm;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.RingtonePreference;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 벨소리 선택 Preference (RingtonePickerPreference)
 * Created by WGL on 2018. 1. 29..
 */

public class RPDialogPreference extends RingtonePreference {
    private final static String TAG = "RPDialogPreference";
    private Context context;

    private Uri ringtoneUri;

    public RPDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "RPDialogPreference: constructor");
        this.context = context;

        /**
         * TODO
         * strings
         * persistString 값과 defaultReturnValue 값 숨기기
         */
        persistString("content://settings/system/ringtone");
        ringtoneUri = Uri.parse(getPersistedString("content://settings/system/ringtone"));
        RingtoneManager manager = new RingtoneManager(context);
        setSummary(manager.getRingtone(context, ringtoneUri).getTitle(context));
    }

    @Override
    protected Uri onRestoreRingtone() {
        Log.d(TAG, "onRestoreRingtone: ");
        /**
         * TODO
         * strings
         * defaultReturnValue 값 숨기기
         */
        ringtoneUri = Uri.parse(getPersistedString("content://settings/system/ringtone"));

        return ringtoneUri;
    }
}
