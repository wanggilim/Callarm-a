package project.wgl.callarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.InputFilter;
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

    private String s_origin = ""; // 자동 버튼 누르기전, 작성하던 텍스트
    private String s_save = "";
    private boolean isPhone = false;
    private boolean isSms = false;
    private boolean isSmsAuto = false;
    private int i_spin_select_no = 0;
    private long l_time = 60000; // 스피너 선택 long 형 시간 기본값

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

        String s_gp = getPersistedString("");
        if (!s_gp.equals("")) {
            /**
             * TODO
             * strings (: 구분선)
             */
            String[] split_ar = s_gp.split(":");

            /**
             * split_ar list
             * 0) i_spin_select_no  (spinner implement)
             * 1) l_time            (spinner implement)
             * 2) isPhone           (checkbox implement)
             * 3) s_save
             * 4) isSms             (checkbox implement)
             * 5) isSmsAuto         (checkbox implement)
             */
            //test
            for (int i = 0; i < split_ar.length; i++) {
                Log.d(TAG, "onPrepareDialogBuilder: split_ar[" + i + "] = " + split_ar[i]);
            }

            spin_time.setSelection(Integer.valueOf(split_ar[0]));
            cb_phone.setChecked(Boolean.valueOf(split_ar[2]));
            s_origin = split_ar[3];
            et_sms.setText(s_origin);
            cb_sms.setChecked(Boolean.valueOf(split_ar[4]));
            cb_sms_auto.setChecked(Boolean.valueOf(split_ar[5]));

        } else {
            spin_time.setSelection(0);
            /**
             * TODO
             * strings
             */
            et_sms.setHint("문자 메시지 체크상자를 눌러주세요");
            et_sms.setText(s_origin);
        }

        super.onPrepareDialogBuilder(builder);
    }

    /**
     * DialogPreference 에 내장되어있는 Dialog 버튼 이벤트 처리
     * (전화 또는 문자메시지 미체크시 나오는 문구 띄우기 설계)
     * @param state
     */
    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        final AlertDialog ad = (AlertDialog) getDialog();
        ad.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cb_sms.isChecked() && !cb_phone.isChecked()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    /**
                     * TODO
                     * strings
                     */
                    builder.setMessage("전화나 문자메시지를 선택해주세요.");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: 닫기");
                        }
                    });
                    builder.show();
                } else if (cb_sms.isChecked() && et_sms.getText().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    /**
                     * TODO
                     * strings
                     */
                    builder.setMessage("문자메시지를 입력해주세요");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: 닫기");
                        }
                    });
                    builder.show();
                } else {
                    ad.dismiss();
                }
            }
        });
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {

        // 종료시 ViewGroup 생성, view 객체를 종료하고 IllegalStateException 방지한다
        final ViewGroup parent = (ViewGroup) view.getParent();
        parent.removeView(view);

        if (positiveResult) {
            /**
             * 0) i_spin_select_no  (spinner implement)
             * 1) l_time            (spinner implement)
             * 2) isPhone           (checkbox implement)
             * 3) s_save
             * 4) isSms             (checkbox implement)
             * 5) isSmsAuto         (checkbox implement)
             */
            /**
             * TODO
             * strings + (: 구분선-보통 입력할 수 없는 문자로 변경)
             */
            isPhone = cb_phone.isChecked();
            isSms = cb_sms.isChecked();
            isSmsAuto = cb_sms_auto.isChecked();
            s_save = et_sms.getText().toString();

            setPersistent(true);
            persistString(i_spin_select_no
                    + ":" + l_time
                    + ":" + isPhone
                    + ":" + s_save
                    + ":" + isSms
                    + ":" + isSmsAuto);

            callChangeListener(getPersistedString(""));

            super.onDialogClosed(positiveResult);
        }

    }

    /////////////////////////////////////////////////////////////////

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getTag().toString()) {
            case "cb_sms":
                /**
                 * TODO
                 * strings setHint 메서드 안에 있는 매개변수
                 */
                if (isChecked) {
                    cb_sms_auto.setEnabled(true);
                    if (cb_sms_auto.isChecked()) {
                        et_sms.setEnabled(!isChecked);
                    } else {
                        et_sms.setHint("이 곳을 눌러 입력하기");
                        et_sms.setEnabled(isChecked);
                    }
                } else {
                    et_sms.setHint("문자 메시지 체크상자를 눌러주세요");
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
        i_spin_select_no = position;
        l_time = parent.getItemIdAtPosition(position); // 스피너 선택 long 형 시간값 저장
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected: ");
    }
}
