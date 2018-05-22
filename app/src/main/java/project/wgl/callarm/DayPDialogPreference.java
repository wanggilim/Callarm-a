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

        Log.d(TAG, "onDialogClosed: " + positiveResult);
        if (positiveResult) {
            //setPersistent(true);
            notifyChanged();
        }

        super.onDialogClosed(positiveResult);
    }
}
