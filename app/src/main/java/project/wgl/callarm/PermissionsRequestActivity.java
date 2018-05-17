package project.wgl.callarm;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * 권한 설정 액티비티
 * Created by WGL on 2018. 2. 8..
 */

public class PermissionsRequestActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "PermissionsHelper";

    private Context context;
    private String[] str_arr_perms;
    private String str_perm;

    private Button btn_allow_check_perm;
    private Button btn_deny_check_perm;

    public PermissionsRequestActivity() {
        // 빈 생성자
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        str_arr_perms = getIntent().getStringArrayExtra("str_arr_perms");
        if (!checkGrantPermissions(str_arr_perms)) {
            setContentView(R.layout.activity_helper_permissions);
            btn_allow_check_perm = findViewById(R.id.btn_allow_check_perm);
            btn_allow_check_perm.setOnClickListener(this);
            btn_deny_check_perm = findViewById(R.id.btn_deny_check_perm);
            btn_deny_check_perm.setOnClickListener(this);
        } else {
            setResult(RESULT_OK);
            finish();
        }

    }

    public boolean checkGrantPermissions(String[] str_arr_perms) { // 원래는 private 이었음.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < str_arr_perms.length; i++) {
                str_perm = str_arr_perms[i].toString();
                Log.d(TAG, "checkGrantPermissions: str_perm = " + str_perm);

                if (ContextCompat.checkSelfPermission(getApplicationContext(), str_perm) == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
        }

        return true;
    }

    //////////////////////////////

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString()) {
            case "check_allow":
                /**
                 * TODO
                 * Version Codes 숨기기
                 */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.d(TAG, "onCreate: permission_checker");
                    requestPermissions(str_arr_perms, 0);
                }
                break;

            case "check_deny":
                setResult(RESULT_CANCELED); // 본 코드는 한 가지만 되기 때문에 경우를 세분화해야한다.
                finish();
            default:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: " + requestCode);

        int result_ok_cnt = 0;

        for (int i = 0; i < grantResults.length; i++) {
            Log.d(TAG, "onRequestPermissionsResult: " + permissions[i] + ", " + grantResults[i]);

            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                setResult(RESULT_CANCELED); // 본 코드는 한 가지만 되기 때문에 경우를 세분화해야한다.
            } else {
                result_ok_cnt += 1;
                if (result_ok_cnt == grantResults.length) {
                    setResult(RESULT_OK); // 본 코드는 한 가지만 되기 때문에 경우를 세분화해야한다.
                }
            }
        }

        finish();
    }
}
