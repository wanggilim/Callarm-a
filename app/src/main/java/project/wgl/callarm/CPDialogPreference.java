package project.wgl.callarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

/**
 * ContactPickerDialogPreference
 * Created by WGL on 2018. 1. 30..
 */

public class CPDialogPreference extends ListPreference {
    private final static String TAG = "CPDialogPreference";

    private Context context;

    private CharSequence[] nameEntries;
    private CharSequence[] numberEntryValues;
    private int contacts_cnt;

    public CPDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        start(new CPAdapter(this.context));
    }

    private void start(CPAdapter ad) {
        /**
         * TODO
         * strings (+-0)
         */
        onSetInitialValue(false, "+-0"); // 기본적으로 '없음(none)' 지정

        ad.setup();

        contacts_cnt = ad.getCursor().getCount();
        if (contacts_cnt > 1) {
            nameEntries = new CharSequence[contacts_cnt];
            numberEntryValues = new CharSequence[contacts_cnt];

            for (int i = 0; i < contacts_cnt; i++) {
                nameEntries[i] = ad.getName(i);
                numberEntryValues[i] = ad.getNumber(i);
            }
            setEnabled(true);

        } else {
            setEnabled(false);
        }
    }

    public int getContacts_cnt() {
        return contacts_cnt;
    }


    @Override
    protected void onPrepareDialogBuilder(final AlertDialog.Builder builder) {
        setEntries(nameEntries);
        setEntryValues(numberEntryValues);
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        Log.d(TAG, "onDialogClosed: " + positiveResult);

        if (positiveResult) {
            setPersistent(true);
            notifyChanged();
        }
        super.onDialogClosed(positiveResult);
    }

}
