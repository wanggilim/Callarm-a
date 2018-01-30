package project.wgl.callarm;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * ContactsPickerAdapter
 * Created by WGL on 2018. 1. 30..
 */

public class CPAdapter extends BaseAdapter implements ListAdapter {
    private final static String TAG = "CPAdapter";

    private Context context;
    private int layout;
    private LayoutInflater inflater;
    private View view;

    private CPAdapterHolder holder;

    private ArrayList<CData> al;

    private Cursor c_cursor;


    public CPAdapter() {
        // 빈 생성자
    }

    public CPAdapter(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        holder = new CPAdapterHolder();
        layout = R.layout.view_picker_contacts;
        al = new ArrayList<>();

        setCursor();
    }

    private void setCursor() {
        // 커서 생성
        String order = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        c_cursor
                = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER},
                null, null, order);
    }

    public Cursor getCursor() {
        return c_cursor;
    }

    public void setup() {
        for (int i = 0; i < getCursor().getCount(); i++) {
            getCursor().moveToPosition(i);
            String phoneName = getCursor().getString(1);
            String phoneNum = getCursor().getString(2);
            // Log.d(TAG, phoneName + ", " + phoneNum);
            al.add(new CData(phoneName, phoneNum));
        }
    }

    public String getName(int position) {
        return al.get(position).name.toString();
    }

    public String getNumber(int position) {
        return al.get(position).phone_num.toString();
    }

    /////////////////////////////////

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(layout, null);
            holder.lv = (ListView) view.findViewById(R.id.lv_contacts);
            holder.lv.setAdapter(this);
            view.setTag(holder);

        }
//        else {
//            ViewParent parent = (ViewParent) view.getParent();
//            if (parent != container) {
//                ((ViewGroup) parent).removeView(view);
//            }
//            holder = (DayPickerHolder) view.getTag();
//        }

        return view;
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
        return position;
    }


    /////////////////////////////////

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

} // end of CPAdapter



class CPAdapterHolder {
    ListView lv;
} // end of CPAdapterHolder


class CData {
    /**
     * ContactsData (empty class)
     */

    String name = "";
    String phone_num = "";
    public CData() {
    }

    public CData(String name, String phone_num) {
        this.name = name;
        this.phone_num = phone_num;
    }
} // end of CData


