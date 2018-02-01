package project.wgl.callarm;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 문자 전화 다이얼로그 프리퍼런스
 * Created by WGL on 2018. 2. 1..
 */

public class SPDialogPreference extends DialogPreference implements CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener {
    private static final String TAG = "SPDialogPreference";

    private Context context;
    private View view;
    private LayoutInflater inflater;

    CheckBox cb_phone;
    EditText et_sms;
    CheckBox cb_sms;
    CheckBox cb_sms_auto;
    TextView tv_sms_limit;

    AppCompatSpinner spin_time;
    TextWatcher watcher;

    private String s_origin = ""; // 자동 버튼 누르기전, 작성하던 텍스트

    public SPDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "SPDialogPreference: ");
        this.context = context;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        Log.d(TAG, "onPrepareDialogBuilder: ");

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.view_selector_sp, null);
            cb_phone = view.findViewById(R.id.cb_phone);
            cb_phone.setTag("cb_phone");

            tv_sms_limit = view.findViewById(R.id.tv_sms_limit);
            tv_sms_limit.setTag("tv_sms_limit");
            /**
             * TODO
             * strings
             */
            tv_sms_limit.setText("0 / 70 바이트");

            cb_sms = view.findViewById(R.id.cb_sms);
            cb_sms.setTag("cb_sms");
            cb_sms.setOnCheckedChangeListener(this);

            cb_sms_auto = view.findViewById(R.id.cb_sms_auto);
            cb_sms_auto.setTag("cb_sms_auto");
            cb_sms_auto.setOnCheckedChangeListener(this);

            spin_time = view.findViewById(R.id.spin_time);
            spin_time.setTag("spin_time");
            spin_time.setAdapter(new SPSPAdapter(context, R.layout.view_spinner_time, R.id.tv_into_spinner));
            spin_time.setOnItemSelectedListener(this);

            et_sms = view.findViewById(R.id.et_sms);
            /**
             * TODO
             * strings
             */
            et_sms.addTextChangedListener(new TextWatcher() {
                String str_byte = " / 70 바이트";

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    tv_sms_limit.setText("0" + str_byte);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tv_sms_limit.setText(s.length() + str_byte);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    tv_sms_limit.setText(s.length() + str_byte);
                }
            });
            et_sms.setTag("et_sms");
        }

        builder.setView(view);

        super.onPrepareDialogBuilder(builder);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        Log.d(TAG, "onDialogClosed: " + positiveResult);

        // 종료시 ViewGroup 생성, view 객체를 종료하고 IllegalStateException 방지한다
        final ViewGroup parent = (ViewGroup) view.getParent();
        parent.removeView(view);

        if (positiveResult) {
            setPersistent(true);
        } else {
            /**
             * TODO
             * (수정사항)
             * 저장 후 취소 눌러도, 그대로 반영된다.
             */
        }

        super.onDialogClosed(positiveResult);
    }

    /////////////////////////////////////////////////////////////////

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getTag().toString()) {
            case "cb_sms":
                /**
                 * TODO
                 * strings
                 */
                et_sms.setHint("이 곳을 눌러 입력하기");
                if (isChecked) {
                    cb_sms_auto.setEnabled(true);
                    if (cb_sms_auto.isChecked()) {
                        et_sms.setEnabled(!isChecked);
                    } else {
                        et_sms.setEnabled(isChecked);
                    }
                } else {
                    cb_sms_auto.setEnabled(false);
                    et_sms.setEnabled(isChecked);
                }
                break;

            case "cb_sms_auto":
                if (isChecked) {
                    if (et_sms.getText().length() >= 0) {
                        s_origin = et_sms.getText().toString();
                        Log.d(TAG, "onCheckedChanged: s_origin = " + s_origin);
                        /**
                         * TODO
                         * strings
                         */
                        et_sms.setText("저를 깨워주세요!"); // 추후에 strings 로 옮겨져야함.
                    } else {

                    }
                } else {
                    if (s_origin.getBytes().length >= 0) {
                        et_sms.setText(s_origin.toString());
                    } else {

                    }
                }
                et_sms.setEnabled(!isChecked);
                break;

            default:
                break;
        }
    }

    /////////////////////////////////////////////////////////////////

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: " + parent.getItemAtPosition(position).toString() + ", " + parent.getItemIdAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected: ");
    }
}
