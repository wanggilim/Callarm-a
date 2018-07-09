package project.wgl.callarm;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


enum ButtonState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

public class AlarmItemController extends ItemTouchHelper.Callback implements RecyclerView.OnItemTouchListener {
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
        setOnTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        addOnItemTouchListener(recyclerView);

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
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) { // 1
            setOnTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            // 버튼 그리기
            if (dX > 0) {
                // 왼쪽 그리기
                paint(c, btn, PAINT_LEFT, left, top, left+(width/4), bottom);
            } else if (dX < 0){
                // 오른쪽 그리기
                paint(c, btn, PAINT_RIGHT, right-(width/4), top, right, bottom);
            }
        }

//        if (isCurrentlyActive == false) {
//                // 고정 가능 상태에서 놓으면 고정이 됨
//                //CardView cardView = (CardView) viewHolder.itemView;
//                //cardView.offsetLeftAndRight((int) BUTTON_WIDTH);
//                //itemView.requestLayout();
//
//                /**
//                 * @variable isCurrentlyActive
//                 * True if this view is currently being controlled by the user
//                 * or false it is simply animating back to its original state.
//                 */
//        }


        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }


    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        //Log.d(TAG, "convertToAbsoluteDirection: " + flags + ", " + layoutDirection);
        if (swipeBack) {
            swipeBack = false;
            return 0;   // 결과적으로, 떼면 원래대로 돌아온다.
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    // 기타 이벤트(1) //////////////////////////////////////
    private void setOnTouchListener(final Canvas c,
                                    final RecyclerView recyclerView,
                                    final RecyclerView.ViewHolder viewHolder,
                                    final float dX, final float dY,
                                    final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            /**
             * ACTION_DOWN = 0
             * ACTION_UP = 1
             * ACTION_MOVE = 2
             * ACTION_CANCEL = 3
             */

            // 눌렀을 때 원래대로 돌아올 수 있다고 바뀐다.
            View itemView = viewHolder.itemView; // cardView

            int width = itemView.getWidth();
            int height = itemView.getHeight();
            int left = itemView.getLeft();
            int right = itemView.getRight();
            int top = itemView.getTop();
            int bottom = itemView.getBottom();
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = (event.getAction() == MotionEvent.ACTION_CANCEL) ||
                        (event.getAction() == MotionEvent.ACTION_UP);

                if (swipeBack) {
                    if (dX > 0 && dX > left+(width/4)) {
                        buttonShowState = ButtonState.LEFT_VISIBLE;
                        /**
                         *  RIGHT_VISIBLE 만 뜨도록 구분하기
                         */
                    } else if (dX < 0 && dX > -(right-(left+(width/4)))) { // dX > -(right-(width/4))
                        buttonShowState = ButtonState.RIGHT_VISIBLE;
                    } else {
                        buttonShowState = ButtonState.GONE;
                    }
                    Log.d(TAG, "onTouch: " + buttonShowState
                            + " " + dX + " " + (-(right-(width/4))));

                    if (buttonShowState != ButtonState.GONE) {
                        setOnTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

    private void setOnTouchDownListener(final Canvas c,
                                      final RecyclerView recyclerView,
                                      final RecyclerView.ViewHolder viewHolder,
                                      final float dX, final float dY,
                                      final int actionState, final boolean isCurrentlyActive) {

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            /**
             * ACTION_DOWN = 0
             * ACTION_UP = 1
             * ACTION_MOVE = 2
             * ACTION_CANCEL = 3
             */

            // 눌렀을 때 원래대로 돌아올 수 있다고 바뀐다.
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "onTouch: setOnTouchListener");
                    setOnTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        });
    }


    private void setOnTouchUpListener(final Canvas c,
                                    final RecyclerView recyclerView,
                                    final RecyclerView.ViewHolder viewHolder,
                                    final float dX, final float dY,
                                    final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            /**
             * ACTION_DOWN = 0
             * ACTION_UP = 1
             * ACTION_MOVE = 2
             * ACTION_CANCEL = 3
             */

            // 눌렀을 때 원래대로 돌아올 수 있다고 바뀐다.
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /**
                 * 추정 TODO
                 * 일정 수치 땡기면 야매로 View 위젯 만들어보기
                 */
                    Log.d(TAG, "onTouch: setOnTouchUpListener");
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    AlarmItemController.super.onChildDraw(c, recyclerView, viewHolder, 0f, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;
                    buttonShowState = ButtonState.GONE;
                }
                return false;
            }
        });

    }


    private void addOnItemTouchListener(final RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                /**
                 * ACTION_DOWN = 0
                 * ACTION_UP = 1
                 * ACTION_MOVE = 2
                 * ACTION_CANCEL = 3
                 */
                Log.d(TAG, "onTouchEvent: " + e.getAction());
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                Log.d(TAG, "onRequestDisallowInterceptTouchEvent: ");
            }
        });
    }


    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }


    // 기타 이벤트(2) //////////////////////////////////////
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


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: ");
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(TAG, "onTouchEvent: ");

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Log.d(TAG, "onRequestDisallowInterceptTouchEvent: ");
    }
}

