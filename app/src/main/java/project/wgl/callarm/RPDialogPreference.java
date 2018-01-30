package project.wgl.callarm;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.RingtonePreference;
import android.util.AttributeSet;
import android.util.Log;

/**
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

        ringtoneUri = Uri.parse(getPersistedString("content://settings/system/ringtone"));
        RingtoneManager manager = new RingtoneManager(context);
        setSummary(manager.getRingtone(context, ringtoneUri).getTitle(context));
    }

    @Override
    protected Uri onRestoreRingtone() {
        Log.d(TAG, "onRestoreRingtone: ");
        ringtoneUri = Uri.parse(getPersistedString("content://settings/system/ringtone"));

        return ringtoneUri;
    }
}
