package project.wgl.callarm;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * SPDialogPreference 스피너 연동 어댑터
 * Created by WGL on 2018. 2. 1..
 */

public class SPSPAdapter extends ArrayAdapter implements ListAdapter {
    private static final String TAG = "SPSPAdapter";
    private Context context;

    private ArrayList<String> al;


    public SPSPAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.context = context;

        init();
    }

    private void init() {

        al = new ArrayList<>();

        /**
         * TODO
         * strings : string-array
         */
        al.add("1분 후");
        al.add("3분 후");
        al.add("5분 후");
        al.add("10분 후");
        al.add("15분 후");
        al.add("20분 후");
        al.add("30분 후");
        al.add("45분 후");
        al.add("60분 후");
    }

    @Override
    public int getCount() {
        return al.size();
    }

    @Override
    public Object getItem(int position) {
        return al.get(position);
    }

    @Override
    public long getItemId(int position) {
        long m_ms = 60000;
        long limit_time = 0L;

        switch (position) {
            case 0:
                limit_time = m_ms * 1;
                break;
            case 1:
                limit_time = m_ms * 3;
                break;
            case 2:
                limit_time = m_ms * 5;
                break;
            case 3:
                limit_time = m_ms * 10;
                break;
            case 4:
                limit_time = m_ms * 15;
                break;
            case 5:
                limit_time = m_ms * 20;
                break;
            case 6:
                limit_time = m_ms * 30;
                break;
            case 7:
                limit_time = m_ms * 45;
                break;
            case 8:
                limit_time = m_ms * 60;
                break;

            default:
                break;
        }

        return limit_time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);

        //View view = inflater.inflate(layout, null);
        //view.setTag(holder);
        //return view;

    }

    ////////////////////////////////////////////

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

}