package project.wgl.callarm;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by WGL on 2018. 1. 10..
 */

public class AlarmSetupActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private static final String TAG = "AlarmSetupActivity";

    private AlarmSetupHolder holder;


    public AlarmSetupActivity() {
        holder = new AlarmSetupHolder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.SEND_SMS
            };

            Intent intent = new Intent(getApplicationContext(), PermissionsRequestActivity.class);
            intent.putExtra("str_arr_perms", perms);
            startActivityForResult(intent, 0);

        } else {
            start();
        }

    }

    private void start() {
        setContentView(R.layout.activity_alarm_setup);

        holder.toolbar = findViewById(R.id.tb_setup_alarm);
        holder.toolbar.inflateMenu(R.menu.set_alarm);
        holder.toolbar.setOnMenuItemClickListener(this);
        /**
         * TODO
         * strings
         */
        holder.toolbar.setTitle("알람 설정");
        holder.toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().hide(); // 기존에 있던 액션바 숨기기

        getFragmentManager().beginTransaction()
                .addToBackStack("AlarmSetupFragment")
                .replace(R.id.fragment_alarm_setup, new AlarmSetupFragment())
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: RESULT_OK");
            start();
        }

        if (resultCode == RESULT_CANCELED) {
            moveTaskToBack(true);
            finish();
            Log.d(TAG, "onActivityResult: RESULT_CANCELED");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack();
        Log.d(TAG, "onBackPressed: ");
        super.onBackPressed();
    }

    //////////////////////////////////////////////////

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        /**
         * TODO
         * strings
         */
        switch (item.getItemId()) {
            case R.id.item_save_alarm:
                Toast.makeText(getBaseContext(), "까꿍!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return false;
    }
}

class AlarmSetupHolder {
    Toolbar toolbar;
}
