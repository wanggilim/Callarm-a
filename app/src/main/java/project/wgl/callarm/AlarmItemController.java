package project.wgl.callarm;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


enum ButtonState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

public class AlarmItemController extends ItemTouchHelper.Callback {
    private final static String TAG = "AlarmItemCallback";

    private ButtonState buttonShowState = ButtonState.GONE;
    private boolean swipeBack = false;

    private final static float BUTTON_WIDTH = 200;  // 수정해야됨.
    private final static float CORNERS = 16;

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
        View itemView = viewHolder.itemView; // cardView

        int width = itemView.getWidth();
        int height = itemView.getHeight();
        int left = itemView.getLeft();
        int right = itemView.getRight();
        int top = itemView.getTop();
        int bottom = itemView.getBottom();

        RectF btn = new RectF();

        // Swipe 하고 있든, 떼버리든 항상 그림 그려져있어야함.
        // 버튼 그리기
        if (dX > 0) {
            // 왼쪽 그리기
            paint(c, btn, PAINT_LEFT, left, top, left+(width/4), bottom);
        } else if (dX < 0){
            // 오른쪽 그리기
            paint(c, btn, PAINT_RIGHT, right-(width/4), top, right, bottom);
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) { // 1
            //setOnTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            /**
             * @variable isCurrentlyActive
             * True if this view is currently being controlled by the user
             * or false it is simply animating back to its original state.
             */


            swipeBack = false;
            if (dX > 0 && dX > left+(width/4)) {
                buttonShowState = ButtonState.LEFT_VISIBLE;
                swipeBack = true;

                // 고정 가능 상태에서 놓으면 고정이 됨
                if (isCurrentlyActive == false) {
                    itemView.setScrollX(-(width/4)); // 야매 = itemView.scrollTo(-(width/4), 0);
                    itemView.setX(-(width/4));
                    /**
                     * TODO
                     * - 고정된 상태에서 버튼 보이기
                     * - 좌, 우 버튼 클릭할 때 다르게 적용하기
                     */
                    paint(c, btn, PAINT_LEFT, left, top, left+(width/4), bottom);
                    //itemView.setX(-(width/4)); // 안됨.
                    //recyclerView.smoothScrollBy(-(width/4), 0); // 안됨.
                    setItemsClickable(recyclerView, true);
                    //Log.d(TAG, "onChildDraw: dX = " + itemView.getX());
                }
            } else if (dX < 0 && Math.abs(dX) > width/4) { // dX > -(right-(width/4))
                paint(c, btn, PAINT_RIGHT, right-(width/4), top, right, bottom);
                buttonShowState = ButtonState.RIGHT_VISIBLE;
                swipeBack = true;

                // 고정 가능 상태에서 놓으면 고정이 됨
                if (isCurrentlyActive == false) {
                    itemView.setScrollX(width/4); // 야매
                    itemView.setX(width/4);
                    setItemsClickable(recyclerView, true);
                }
            } else {
                buttonShowState = ButtonState.GONE;
                /**
                 * TODO
                 * 고정된 상태에서 다시 땡기고 풀릴때 원복하기
                 */
//                if (isCurrentlyActive == false) {
//                    itemView.setScrollX(0);
//                    itemView.setX(0);
//                    setItemsClickable(recyclerView,false);
//                    swipeBack = false;
//                }

            }
            Log.d(TAG, "onChildDraw: buttonShowState = " + buttonShowState + ", " +
                    "isCurrentActive = "+ isCurrentlyActive + ", " +
                    "swipeBack = " + swipeBack);
        }

        itemView.requestLayout();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;   // 결과적으로, 떼면 원래대로 돌아온다.
        }

        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    private void setItemsClickable(final RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            recyclerView.getChildAt(i).setClickable(isClickable);
            recyclerView.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(recyclerView.getContext(), "눌렀습니다", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
        text.setTextSize(40f);
        text.setAntiAlias(true);
        text.setTextAlign(Paint.Align.CENTER);

        rectF.set(left, top, right, bottom);

        String str_drawText = "";
        if (direction == PAINT_LEFT) {
            paint.setColor(Color.BLUE);
            /**
             * TODO
             * strings
             */
            str_drawText = "EDIT";

        } else {
            paint.setColor(Color.RED);
            /**
             * TODO
             * strings
             */
            str_drawText = "DELETE";
        }
        c.drawRoundRect(rectF, CORNERS, CORNERS, paint);
        c.drawText(str_drawText,
                rectF.centerX(), rectF.centerY(),
                text);
    }

}

