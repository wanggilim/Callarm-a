package project.wgl.callarm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
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
        Log.d(TAG, "onActivityResult: ");
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
