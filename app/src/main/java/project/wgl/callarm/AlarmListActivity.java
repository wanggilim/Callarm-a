package project.wgl.callarm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * 알람 설정 확인 리스트
 * Created by WGL on 2018. 1. 10..
 */
public class AlarmListActivity extends AppCompatActivity {
    private static final String TAG = "AlarmListActivity";

    private Context context;
    private RecyclerView recyclerView;
    private AlarmListAdapter adapter;
    private boolean backFromSwipe;


    public AlarmListActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: AlarmListActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);

        context = getBaseContext();

        DatabaseOpenHelper helper = new DatabaseOpenHelper(getBaseContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = helper.read(db);

        ArrayList<Alarm> alarms = new ArrayList<>();
        //CPAdapter adapter = new CPAdapter(context);
        /**
         * TODO
         * 1. Alarm 객체 가공해야함
         * 2. ViewHolder 안에 등록된 Alarm 개체들의 getter 값을 첨부한다.
         *
         * (추후)
         * - 가공한 Alarm 서비스로 등록하기
         */
        AlarmItemCreator creator = new AlarmItemCreator(context);

        while (result.moveToNext()) {
            // (1) 새 Alarm 객체 선언
            Alarm alarm = new Alarm();

            // (2) Alarm 객체안으로 넣을 변수들을 선언하고 값을 정의함.
            boolean isOn = result.getInt(0) == 0 ? false : true;
            boolean isRepeat = result.getInt(1) == 0 ? false : true;
            long time_l = result.getLong(2);
            String days = result.getString(3);
            boolean isVibe = result.getInt(4) == 0 ? false : true;
            String ringtoneUri = result.getString(5);
            String volPattern = result.getString(6);
            String contactsUri = result.getString(7);
            String split_ar = result.getString(8);

            // (3) Alarm 객체의 setter 로 값들을 삽입한다.
            alarm.setOn(isOn);
            alarm.setRepeat(isRepeat);
            alarm.setContactsUri(contactsUri);
            alarm.setTime_l(time_l);
            alarm.setDays(days);
            alarm.setVibe(isVibe);
            alarm.setRingtoneUri(ringtoneUri);
            alarm.setVolPattern(volPattern);
            alarm.setContactsUri(contactsUri);
            alarm.setSplit_ar(split_ar);

            // (4) Alarm 객체 기반 ArrayList 안으로, 선언하고 정의한 Alarm 객체를 삽입한다.
            alarms.add(alarm);
        }

        adapter = new AlarmListAdapter(context, alarms);

        recyclerView = findViewById(R.id.rv_alarm_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch: " + event.getAction());
                backFromSwipe
                        = (event.getAction() == MotionEvent.ACTION_UP) ||
                        (event.getAction() == MotionEvent.ACTION_CANCEL);
                return false;
            }
        });


        // 스와이프 컨트롤러 (왼->오 에디트, 오->왼 삭제)
        ItemTouchHelper.SimpleCallback callback
                = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Log.d(TAG, "onMove: ");
                return false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                //Log.d(TAG, "onChildDraw: " + dX + ", " + dY + ", " + actionState + ", " + isCurrentlyActive);
                RectF leftBtn = new RectF();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                Log.d(TAG, "onSelectedChanged: ");
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        // 삭제 아이콘 등장

                        // 삭제 버튼 누를시
                        adapter.getItems().remove(position);
                        adapter.notifyItemRemoved(position);
                        break;
                    case ItemTouchHelper.RIGHT:
                        // 편집 아이콘 등장
                        break;
                    case ItemTouchHelper.UP:
                        break;
                    case ItemTouchHelper.DOWN:
                        break;

                    default:
                        break;
                }
            }

            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                if (backFromSwipe) {
                    backFromSwipe = false;
                    return 0;
                }
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        
        /**
         * TODO
         * 달력에서 등록 이벤트를 클릭시 RecyclerView 가 뜨고
         * 그렇지 않다면 RecyclerView 숨기는 기능을 구현해야한다.
         */
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
