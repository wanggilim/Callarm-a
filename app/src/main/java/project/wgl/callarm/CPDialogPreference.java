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

    private CPAdapter adapter;

    public CPDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        adapter = new CPAdapter(context);
        adapter.setup();

        //for (int i = 0; i < adapter.getCursor().getColumnCount(); i++) {
            //Log.d(TAG, "onPrepareDialogBuilder: >>" + adapter.getCursor().getColumnName(i));
        //}

        CharSequence[] nameEntries = new CharSequence[adapter.getCursor().getCount()];
        CharSequence[] numberEntryValues = new CharSequence[adapter.getCursor().getCount()];

        for (int i = 0; i < adapter.getCursor().getCount(); i++) {
            nameEntries[i] = adapter.getName(i);
            numberEntryValues[i] = adapter.getNumber(i);
        }
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
