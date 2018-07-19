package project.wgl.callarm;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;


public class AlarmItemController extends ItemTouchHelper.Callback {
    private final static String TAG = "AlarmItemCallback";

    private AlarmListAdapter adapter;
    private Alarm alarm;

    private final static float BUTTON_WIDTH = 200;  // 수정해야됨.
    private final static float CORNERS = 16;

    public AlarmItemController(AlarmListAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        View itemView = viewHolder.itemView;
        final int position = viewHolder.getAdapterPosition();

        if (direction == ItemTouchHelper.LEFT) {
            // RTL
            // (1) 백업하고
            alarm = adapter.getItem(position);
            adapter.removeItem(position);

            // (2) 지우고
            /**
             * TODO
             * strings
             */
            Snackbar snackbar = Snackbar.make(itemView, "삭제되었습니다.", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.undoItem(alarm, position);
                }
            });
            snackbar.show();
        } else {
            // LTR


        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        /**
         * @param actionState
         * ACTION_STATE_IDLE = 0
         * ACTION_STATE_SWIPE = 1
         * ACTION_STATE_DRAG = 2
         */
//                Log.d(TAG, "onChildDraw: "
//                        + viewHolder.getAdapterPosition()
//                        + ", " + dX + ", " + dY
//                        + ", " + actionState + ", " + isCurrentlyActive
//                );
        //CardView itemView = findViewById(R.id.cv_list_alarm); // cardView
        //CardView itemView2 = (CardView) viewHolder.itemView; // cardView
        final View itemView = viewHolder.itemView; // cardView


        int width = itemView.getWidth();
        int height = itemView.getHeight();
        int left = itemView.getLeft();
        int right = itemView.getRight();
        int top = itemView.getTop();
        int bottom = itemView.getBottom();

        RectF btn = new RectF();

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX > 0) {
                //paint(c, btn, PAINT_LEFT, left, top, left+(width/4), bottom);
                paint(c, btn, PAINT_LEFT, left, top, dX, bottom);
            } else if (dX < 0) {
                //paint(c, btn, PAINT_RIGHT, right-(width/4), top, right, bottom);
                paint(c, btn, PAINT_RIGHT, dX+right, top, right, bottom);
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
//        if (swipeBack) {
//            swipeBack = false;
//            return 0;   // 결과적으로, 떼면 원래대로 돌아온다.
//        }

        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    // 기타 이벤트(1) //////////////////////////////////////
    private final static int PAINT_LEFT = 0;
    private final static int PAINT_RIGHT = 1;
    private void paint(Canvas c,
                       RectF rectF,
                       int direction,
                       float left, float top, float right, float bottom) {
        /**
         * 버튼 그리기
         * @param c
         * @param direction
         * @param left
         * @param top
         * @param right
         * @param bottom
         */

        Paint paint = new Paint();
        Paint text = new Paint();

        paint.setAntiAlias(true);
        text.setColor(Color.WHITE);
        text.setTextSize(45f);
        text.setAntiAlias(true);

        rectF.set(left, top, right, bottom);

        String str_drawText = "";
        text.setTextAlign(Paint.Align.CENTER);
        if (direction == PAINT_LEFT) {
            paint.setColor(Color.rgb(63, 81, 181));
            /**
             * TODO
             * strings
             */
            str_drawText = "SWIPE TO EDIT";
        } else if (direction == PAINT_RIGHT) {
            paint.setColor(Color.RED);
            /**
             * TODO
             * strings
             */
            str_drawText = "SWIPE TO DELETE";
        }
        c.drawRoundRect(rectF, CORNERS, CORNERS, paint);
        c.drawText(str_drawText,
                rectF.centerX(), rectF.centerY(), text);
    }

}

