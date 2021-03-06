package project.wgl.callarm;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by WGL on 2018. 2. 4..
 */

public class RVDialogPreference extends DialogPreference
        implements RadioGroup.OnCheckedChangeListener, RadioButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
    private final static String TAG = "RVDialogPreference";

    private Context context;

    private View view;
    private RVHolder holder;

    private String perStr = "";
    private int volValue = 0;
    private String volPattern = "";
    private String s_p_rv = "";

    public RVDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        init();
    }

    private void init() {
        view = View.inflate(context, R.layout.view_volume_rp, null);

        holder = new RVHolder();

        holder.rg_sp = view.findViewById(R.id.rg_sp);
        holder.rb_af_sp = view.findViewById(R.id.rb_af_sp);
        holder.rb_ai_sp = view.findViewById(R.id.rb_ai_sp);
        holder.rb_aid_sp = view.findViewById(R.id.rb_aid_sp);

        holder.tv_ass = view.findViewById(R.id.tv_ass);
        holder.sb_ass = view.findViewById(R.id.sb_ass);

        view.setTag(holder);


        perStr = getPersistedString(80 + ":" + holder.rb_af_sp.getTag().toString());
        /**
         * TODO
         * (수정)
         * 초기 볼륨크기 및 패턴 지정하여 null 관련 예외 발생하지 않도록 하기
         *
         * strings
         */
        volValue = Integer.parseInt(perStr.substring(0, perStr.indexOf(":"))); // : 부분
        s_p_rv = perStr.substring(perStr.indexOf(":")+1, perStr.length()); // : 부분

        if (s_p_rv.equals(holder.rb_af_sp.getTag().toString())) {
            volPattern = holder.rb_af_sp.getText().toString();
        } else if (s_p_rv.equals(holder.rb_ai_sp.getTag().toString())) {
            volPattern = holder.rb_ai_sp.getText().toString();
        } else {
            volPattern = holder.rb_aid_sp.getText().toString();
        }
        
        persistString(volValue + ":" + s_p_rv); // : 부분
        String pre_volv = "볼륨 : ";
        setSummary(pre_volv + volValue + ", " + volPattern); // 여기까지
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        holder.rg_sp.setOnCheckedChangeListener(this);
        holder.rb_af_sp.setOnCheckedChangeListener(this);
        holder.rb_ai_sp.setOnCheckedChangeListener(this);
        holder.rb_aid_sp.setOnCheckedChangeListener(this);

        holder.sb_ass.setOnSeekBarChangeListener(this);
        /**
         * TODO
         * (수정)
         * 알람 편집시 Progress 가 등록되어있는 체크 상태로 복원되어야함.
         *
         * strings (: 부분)
         */
        perStr = getPersistedString(80 + ":" + holder.rb_af_sp.getTag().toString());
        volValue = Integer.parseInt(perStr.substring(0, perStr.indexOf(":")));
        s_p_rv = perStr.substring(perStr.indexOf(":")+1, perStr.length()); // 여기까지

        holder.sb_ass.setProgress(volValue);

        if (s_p_rv.equals(holder.rb_af_sp.getTag().toString())) {
            holder.rb_af_sp.setChecked(true);
        } else if (s_p_rv.equals(holder.rb_ai_sp.getTag().toString())) {
            holder.rb_ai_sp.setChecked(true);
        } else {
            holder.rb_aid_sp.setChecked(true);
        }

        builder.setView(view);
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (holder.rb_af_sp.isChecked()) {
            volPattern = holder.rb_af_sp.getText().toString();
        } else if (holder.rb_ai_sp.isChecked()) {
            volPattern = holder.rb_ai_sp.getText().toString();
        } else {
            volPattern = holder.rb_aid_sp.getText().toString();
        }


        if (positiveResult) {
            setPersistent(true);
            /**
             * TODO
             * strings
             */
            persistString(volValue + ":" + s_p_rv); // : 부분
            String pre_volv = "볼륨 : ";
            setSummary(pre_volv + volValue + ", " + volPattern); // 여기까지

            //Log.d(TAG, "onDialogClosed: getPersistedString = " + getPersistedString(""));
            callChangeListener(getPersistedString(""));
        }

        // 종료시 ViewGroup 생성, view 객체를 종료하고 IllegalStateException 방지한다
        final ViewGroup parent = (ViewGroup) view.getParent();
        parent.removeView(view);

        super.onDialogClosed(positiveResult);
    }

    //////////////////////////////////////////////////////

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            s_p_rv = buttonView.getTag().toString();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.d(TAG, "onCheckedChanged: " + group.toString() + ", " + checkedId);
    }

    //////////////////////////////////////////////////////

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //Log.d(TAG, "onProgressChanged: ");
        volValue = progress;
        /**
         * TODO
         * strings
         */
        String post_vol = "/100";
        holder.tv_ass.setText((progress + post_vol).toString());

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStartTrackingTouch: " + seekBar.getProgress());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStopTrackingTouch: " + seekBar.getProgress());
    }
}

class RVHolder {
    RadioGroup rg_sp;
    RadioButton rb_af_sp;
    RadioButton rb_ai_sp;
    RadioButton rb_aid_sp;

    TextView tv_ass;
    SeekBar sb_ass;
}
