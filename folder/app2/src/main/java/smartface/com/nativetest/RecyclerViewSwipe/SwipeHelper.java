package smartface.com.nativetest.RecyclerViewSwipe;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;

import smartface.com.nativetest.RecyclerViewSwipe.ItemTouchHelperExtention.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import smartface.com.nativetest.R;

public class SwipeHelper extends  ItemTouchHelper.Callback {

    Context context;
    MyAdapter rAdapter;
    RecyclerView mRecyclerView;
    View myTestView ;

    RecyclerView.ViewHolder mPrevDrawnViewHolder = null;

    final static int drawItemWidth = 400;
    public static final int DEFAULT_SWIPE_ANIMATION_DURATION = 250;

    public SwipeHelper( Context parentContext , RecyclerView.Adapter adapter , RecyclerView recyclerView) {
        super();
        this.context = parentContext;
        this.rAdapter =(MyAdapter) adapter;
        this.mRecyclerView = recyclerView;

        myTestView = LayoutInflater.from(parentContext).inflate(R.layout.bitmaptest, null);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//        rAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        super.onChildDraw(c, recyclerView, viewHolder, dX , dY, actionState, isCurrentlyActive);

        Log.e("onChildDraw" , " Translation x "  +dX );

        drawARectColor(c, viewHolder, dX,dY);
        viewHolder.itemView.setTranslationX(dX);
        mPrevDrawnViewHolder = viewHolder;

    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return 1f;
    }

    /*
        Return width of intended to draw view
         */
    @Override
    public int drawnViewWidth() {
        return drawItemWidth;
    }

    public void drawARectColor(Canvas c, RecyclerView.ViewHolder viewHolder, float dX , float dY){
        View  myView = viewHolder.itemView;


        String dummyText = "Helly Bai!";

        Paint p = new Paint();
        RectF rectF = new RectF((myView.getRight() - drawItemWidth) ,myView.getTop(),myView.getRight(),myView.getBottom());

        myTestView.layout(0, 0, (int)rectF.width(), (int) rectF.height());
        int widthSpec = View.MeasureSpec.makeMeasureSpec((int) rectF.width(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(((int) rectF.height()), View.MeasureSpec.EXACTLY);
        myTestView.measure(widthSpec, heightSpec);
        c.translate(rectF.left, rectF.top);
        myTestView.draw(c);

    }

}
