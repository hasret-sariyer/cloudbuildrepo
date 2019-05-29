package smartface.com.nativetest.SnapAlignment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.SmoothScroller;
import android.view.View;


public class CustomizedSnapHelper extends PagerSnapHelper {

    public final static int SPANTOALIGNMENT_START = 0;
    public final static int SPANTOALIGNMENT_CENTER = 1;
    public final static int SPANTOALIGNMENT_END = 2;
    private boolean paginationEnabled = true;

    private final int START = 0;
    private final int END = 1;
    private final int BOTH = 2;

    @Nullable
    private OrientationHelper mVerticalHelper;
    @Nullable
    private OrientationHelper mHorizontalHelper;

    RecyclerView mRecyclerView;

    private int spanToAligment;

    public CustomizedSnapHelper(int spanToAlignment, RecyclerView recyclerView) {
        super();
        this.spanToAligment = spanToAlignment;
        this.mRecyclerView = recyclerView;
    }

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        int[] out = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceBasedOnAligment(layoutManager, targetView,
                    getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }

        if (layoutManager.canScrollVertically()) {
            out[1] = distanceBasedOnAligment(layoutManager, targetView,
                    getVerticalHelper(layoutManager));
        } else {
            out[1] = 0;
        }
        return out;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        return super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return findView(layoutManager, getVerticalHelper(layoutManager));
        } else if (layoutManager.canScrollHorizontally()) {
            return findView(layoutManager, getHorizontalHelper(layoutManager));
        }
        return null;
    }

    private int distanceToEnd(@NonNull RecyclerView.LayoutManager layoutManager,
                                 @NonNull View targetView, OrientationHelper helper) {


        final int childEnd = helper.getDecoratedEnd(targetView);
        final int container;
        if (layoutManager.getClipToPadding()) {
            container = helper.getEndAfterPadding();
        }else {
            container  = helper.getEnd();
        }

        if(reachedPointedChild(layoutManager, START) ||
                (!layoutManager.getClipToPadding() && isOverCrossActualRange(layoutManager,helper)))
            return  0;

        return   childEnd - container;
    }

    private int distanceToStart(@NonNull RecyclerView.LayoutManager layoutManager,
                                @NonNull View targetView, OrientationHelper helper) {

        final int childStart = helper.getDecoratedStart(targetView);
        final int containerStart;
        if (layoutManager.getClipToPadding()) {
            containerStart = helper.getStartAfterPadding();
        }else {
            containerStart = 0;
        }

        if(reachedPointedChild(layoutManager, END) ||
                (!layoutManager.getClipToPadding() && isOverCrossActualRange(layoutManager,helper)))
            return  0;

        return   childStart - containerStart;
    }

    private int distanceToCenter(@NonNull RecyclerView.LayoutManager layoutManager,
                                 @NonNull View targetView, OrientationHelper helper) {
        final int childCenter = helper.getDecoratedStart(targetView)
                + (helper.getDecoratedMeasurement(targetView) / 2);
        final int containerCenter;
        if (layoutManager.getClipToPadding()) {
            containerCenter = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        } else {
            containerCenter = helper.getEnd() / 2;
        }

        if(reachedPointedChild(layoutManager, BOTH) ||
                (!layoutManager.getClipToPadding() && isOverCrossActualRange(layoutManager,helper)))
            return  0;

        return childCenter - containerCenter;
    }

    private OrientationHelper getHorizontalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {

        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }
    private OrientationHelper getVerticalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {

        if (mVerticalHelper == null) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return mVerticalHelper;
    }

    private View findView(RecyclerView.LayoutManager layoutManager,
                                OrientationHelper helper) {

        int childCount = layoutManager.getChildCount();

        View closestChild = null;

        final int layoutSize = layoutBasedOnAligment(helper,layoutManager);

        int absClosest = Integer.MAX_VALUE;

        int totalMeasurement = 0;
        for (int i = 0; i < childCount; i++) {
            /*
            There is a known bug which leads sometimes unvisible children taken into account.
             */
            final View child = layoutManager.getChildAt(i);
            int resultChildPoint = childBasedOnAligment(helper, child);
            int absDistance = Math.abs(layoutSize - resultChildPoint);
            /** if child center is closer than previous closest, set it as closest  **/
                if (absDistance < absClosest) {
                    absClosest = absDistance;
                    closestChild = child;
                }
        }
        return closestChild;
    }
    private int childBasedOnAligment(OrientationHelper helper, View child){
          switch (spanToAligment){
            case SPANTOALIGNMENT_CENTER :
                return  helper.getDecoratedStart(child)
                        + (helper.getDecoratedMeasurement(child) / 2);
            case SPANTOALIGNMENT_END :
                return helper.getDecoratedEnd(child);
                default:
                    return helper.getDecoratedStart(child);
        }
    }

    private int layoutBasedOnAligment(OrientationHelper helper, RecyclerView.LayoutManager layoutManager){
        switch (spanToAligment){
            case SPANTOALIGNMENT_CENTER :
                final int center;
                if (layoutManager.getClipToPadding()) {
                    center = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
                } else {
                    center = helper.getEnd() / 2;
                }
                return center;
            case SPANTOALIGNMENT_END :
                final int end;
                if (layoutManager.getClipToPadding()) {
                    end = helper.getEndAfterPadding();
                } else {
                    end = helper.getEnd();
                }
                return end;

            default:
                final int defaultStart;
                if (layoutManager.getClipToPadding()) {
                    defaultStart = helper.getStartAfterPadding();
                } else {
                    defaultStart = 0;
                }
                return defaultStart;
        }
    }

    private int distanceBasedOnAligment(@NonNull RecyclerView.LayoutManager layoutManager,
                                        @NonNull View targetView, OrientationHelper helper){
        switch (spanToAligment){
            case SPANTOALIGNMENT_CENTER :
                return  distanceToCenter(layoutManager, targetView, helper);
            case SPANTOALIGNMENT_END :
                return distanceToEnd(layoutManager, targetView, helper);
            default:
                return distanceToStart(layoutManager, targetView, helper);
        }
    }

    @Nullable
    @Override
    protected SmoothScroller createScroller(RecyclerView.LayoutManager layoutManager) {
        if(paginationEnabled)
        return super.createScroller(layoutManager);
        else
            return  null;
    }

    public void disablePagination(boolean disable){
        if(disable == true)
            paginationEnabled = false;
        else
            paginationEnabled = true;
    }

    /*
    This method determines padding according to start / end point of layout.
    The intention: is if start / end point of layout is cross over the padding then do not make any snaping.
    Note: There is a known bug if RecyclerView padding is huge then leads the computed scroll offset 0 always.
     */
    private boolean isOverCrossActualRange(RecyclerView.LayoutManager layoutManager,OrientationHelper helper){

        final int  startPadding  = helper.getStartAfterPadding();
        final int occupiedSpace = helper.getTotalSpace();

        final int scrollRange;
        if(layoutManager.canScrollHorizontally()){
            scrollRange = (mRecyclerView.computeHorizontalScrollRange() - startPadding);

            return (mRecyclerView.computeHorizontalScrollOffset() < startPadding ||
                    scrollRange  < ( (mRecyclerView.computeHorizontalScrollOffset() + occupiedSpace) ) ? true : false );
        }else if(layoutManager.canScrollVertically()){
            scrollRange =  ( mRecyclerView.computeVerticalScrollRange() - startPadding);

            return (mRecyclerView.computeVerticalScrollOffset() < startPadding ||
                    scrollRange < (mRecyclerView.computeVerticalScrollOffset()  + occupiedSpace)? true : false );
        }
        return false;
    }

    /*
    Determines that whether FIRST / LAST view is completely visible.
    The intention : If it is visible then do not make any distance calculation and return 0.
     */
    private boolean reachedPointedChild(RecyclerView.LayoutManager layoutManager, int pointedPosition){
        switch (pointedPosition){
            case START:
                return isFirstViewPartiallyVisible(layoutManager);
            case END:
                return isLastViewPartiallyVisible(layoutManager);
            case BOTH:
                return  isFirstViewPartiallyVisible(layoutManager) || isLastViewPartiallyVisible(layoutManager);
                default:
                    return  false;
        }
    }

    private boolean isLastViewPartiallyVisible(RecyclerView.LayoutManager layoutManager){
        int lastItem = layoutManager.getItemCount() - 1;
        int childCount = layoutManager.getChildCount();

        View lastFoundChild = null;
        for (int i = 0; i < childCount; i++) {
            int position = layoutManager.getPosition(layoutManager.getChildAt(i));
            if(position == lastItem)
                lastFoundChild = layoutManager.getChildAt(i);
        }
        return  (lastFoundChild == null ? false :
                layoutManager.isViewPartiallyVisible(lastFoundChild,true,true));
    }

    private boolean isFirstViewPartiallyVisible(RecyclerView.LayoutManager layoutManager){
        final View firstView  =layoutManager.getChildAt(0);
        if(firstView == null || layoutManager.getPosition(firstView) != 0)
            return false;
        return ( firstView == null ?
                false :
                layoutManager.isViewPartiallyVisible(firstView,true,true));
    }
}
