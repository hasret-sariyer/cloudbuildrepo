package smartface.com.nativetest.RecyclerViewSwipe.ItemTouchHelperExtention;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.recyclerview.R;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchUIUtil;
import smartface.com.nativetest.RecyclerViewSwipe.ItemTouchHelperExtention.ItemTouchUIUtilImpl;
import android.util.Log;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;


public class ItemTouchHelper extends RecyclerView.ItemDecoration
        implements RecyclerView.OnChildAttachStateChangeListener {

    /**
     * Up direction, used for swipe & drag control.
     */
    public static final int UP = 1;

    /**
     * Down direction, used for swipe & drag control.
     */
    public static final int DOWN = 1 << 1;

    /**
     * Left direction, used for swipe & drag control.
     */
    public static final int LEFT = 1 << 2;

    /**
     * Right direction, used for swipe & drag control.
     */
    public static final int RIGHT = 1 << 3;

    // If you change these relative direction values, update Callback#convertToAbsoluteDirection,
    // Callback#convertToRelativeDirection.
    /**
     * Horizontal start direction. Resolved to LEFT or RIGHT depending on RecyclerView's layout
     * direction. Used for swipe & drag control.
     */
    public static final int START = LEFT << 2;

    /**
     * Horizontal end direction. Resolved to LEFT or RIGHT depending on RecyclerView's layout
     * direction. Used for swipe & drag control.
     */
    public static final int END = RIGHT << 2;

    /**
     * ItemTouchHelper is in idle state. At this state, either there is no related motion event by
     * the user or latest motion events have not yet triggered a swipe or drag.
     */
    public static final int ACTION_STATE_IDLE = 0;

    /**
     * A View is currently being swiped.
     */
    public static final int ACTION_STATE_SWIPE = 1;

    /**
     * A View is currently being dragged.
     */
    public static final int ACTION_STATE_DRAG = 2;

    /**
     * Animation type for views which are swiped successfully.
     */
    public static final int ANIMATION_TYPE_SWIPE_SUCCESS = 1 << 1;

    /**
     * Animation type for views which are not completely swiped thus will animate back to their
     * original position.
     */
    public static final int ANIMATION_TYPE_SWIPE_CANCEL = 1 << 2;

    /**
     * Animation type for views that were dragged and now will animate to their final position.
     */
    public static final int ANIMATION_TYPE_DRAG = 1 << 3;

    private static final int ANIMATION_TYPE_REVERT = 1 << 4;

    static final String TAG = "ItemTouchHelper";

    static final boolean DEBUG = false;

    static final int ACTIVE_POINTER_ID_NONE = -1;

    static final int DIRECTION_FLAG_COUNT = 8;

    private static final int ACTION_MODE_IDLE_MASK = (1 << DIRECTION_FLAG_COUNT) - 1;

    static final int ACTION_MODE_SWIPE_MASK = ACTION_MODE_IDLE_MASK << DIRECTION_FLAG_COUNT;

    static final int ACTION_MODE_DRAG_MASK = ACTION_MODE_SWIPE_MASK << DIRECTION_FLAG_COUNT;

    /**
     * The unit we are using to track velocity
     */
    private static final int PIXELS_PER_SECOND = 1000;

    /**
     * Views, whose state should be cleared after they are detached from RecyclerView.
     * This is necessary after swipe dismissing an item. We wait until animator finishes its job
     * to clean these views.
     */
    final List<View> mPendingCleanup = new ArrayList<View>();

    /**
     * Re-use array to calculate dx dy for a ViewHolder
     */
    private final float[] mTmpPosition = new float[2];

    /**
     * Currently selected view holder
     */
    ViewHolder mSelected = null;

    /**
     * The reference coordinates for the action start. For drag & drop, this is the time long
     * press is completed vs for swipe, this is the initial touch point.
     */
    float mInitialTouchX;

    float mInitialTouchY;

    /*
     * dX values are calculated accorgind to StartingPoint;
     */
    float startingPoint;

    /**
     * Set when ItemTouchHelper is assigned to a RecyclerView.
     */
    float mSwipeEscapeVelocity;

    /**
     * Set when ItemTouchHelper is assigned to a RecyclerView.
     */
    float mMaxSwipeVelocity;

    /**
     * The diff between the last event and initial touch.
     */
    float mDx;

    float mDy;

    /**
     * The coordinates of the selected view at the time it is selected. We record these values
     * when action starts so that we can consistently position it even if LayoutManager moves the
     * View.
     */
    float mSelectedStartX;

    float mSelectedStartY;

    /**
     * The pointer we are tracking.
     */
    int mActivePointerId = ACTIVE_POINTER_ID_NONE;

    /**
     * Developer callback which controls the behavior of ItemTouchHelper.
     */
   ItemTouchHelper.Callback mCallback;

    /**
     * Current mode.
     */
    int mActionState = ACTION_STATE_IDLE;

    /**
     * The direction flags obtained from unmasking
     * {@link android.support.v7.widget.helper.ItemTouchHelper.Callback#getAbsoluteMovementFlags(RecyclerView, ViewHolder)} for the current
     * action state.
     */
    int mSelectedFlags;

    /**
     * When a View is dragged or swiped and needs to go back to where it was, we create a Recover
     * Animation and animate it to its location using this custom Animator, instead of using
     * framework Animators.
     * Using framework animators has the side effect of clashing with ItemAnimator, creating
     * jumpy UIs.
     */
    List<RecoverAnimation> mRecoverAnimations = new ArrayList<RecoverAnimation>();

    private int mSlop;

    RecyclerView mRecyclerView;

    /**
     * Used for detecting fling swipe
     */
    VelocityTracker mVelocityTracker;

    //re-used list for selecting a swap target
    private List<ViewHolder> mSwapTargets;

    //re used for for sorting swap targets
    private List<Integer> mDistances;

    /**
     * If drag & drop is supported, we use child drawing order to bring them to front.
     */
    private RecyclerView.ChildDrawingOrderCallback mChildDrawingOrderCallback = null;

    /**
     * This keeps a reference to the child dragged by the user. Even after user stops dragging,
     * until view reaches its final position (end of recover animation), we keep a reference so
     * that it can be drawn above other children.
     */
    View mOverdrawChild = null;

    /**
     * We cache the position of the overdraw child to avoid recalculating it each time child
     * position callback is called. This value is invalidated whenever a child is attached or
     * detached.
     */
    int mOverdrawChildPosition = -1;

    private static boolean widthTakenIntoAccount = false;

    private static ViewHolder prevSelectedView = null;

    private final OnItemTouchListener mOnItemTouchListener = new OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
//            mGestureDetector.onTouchEvent(event);
            if (DEBUG) {
                Log.d(TAG, "intercept: x:" + event.getX() + ",y:" + event.getY() + ", " + event);
            }
            final int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {
                mActivePointerId = event.getPointerId(0);
                mInitialTouchX = event.getX();
                mInitialTouchY = event.getY();
                Log.d("initialtouch" , " initial touch " + mInitialTouchX);
                obtainVelocityTracker();
                if (mSelected == null) {
                    final RecoverAnimation animation = findAnimation(event);
                    if (animation != null) {
                        mInitialTouchX -= animation.mX;
                        mInitialTouchY -= animation.mY;
                        Log.d("initialtouch" , " initial touch after - mx  " + mInitialTouchX);
                        endRecoverAnimation(animation.mViewHolder, true);
                        if (mPendingCleanup.remove(animation.mViewHolder.itemView)) {
                            mCallback.clearView(mRecyclerView, animation.mViewHolder);
                        }
                        select(animation.mViewHolder, animation.mActionState);
                        updateDxDy(event, mSelectedFlags, 0);
                    }
                }
                revertPositionAnimation(prevSelectedView);
            } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                mActivePointerId = ACTIVE_POINTER_ID_NONE;
                select(null, ACTION_STATE_IDLE);
            } else if (mActivePointerId != ACTIVE_POINTER_ID_NONE) {
                // in a non scroll orientation, if distance change is above threshold, we
                // can select the item
                final int index = event.findPointerIndex(mActivePointerId);
                if (DEBUG) {
                    Log.d(TAG, "pointer index " + index);
                }
                if (index >= 0) {
                    checkSelectForSwipe(action, event, index);
                }
            }
            if (mVelocityTracker != null) {
                mVelocityTracker.addMovement(event);
            }
            return mSelected != null;
        }

        @Override
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {
            if (DEBUG) {
                Log.d(TAG,
                        "on touch: x:" + mInitialTouchX + ",y:" + mInitialTouchY + ", :" + event);
            }
            if (mVelocityTracker != null) {
                mVelocityTracker.addMovement(event);
            }
            if (mActivePointerId == ACTIVE_POINTER_ID_NONE) {
                return;
            }
            final int action = event.getActionMasked();
            final int activePointerIndex = event.findPointerIndex(mActivePointerId);
            if (activePointerIndex >= 0) {
                checkSelectForSwipe(action, event, activePointerIndex);
            }
            ViewHolder viewHolder = mSelected;
            if (viewHolder == null) {
                return;
            }
            switch (action) {
                case MotionEvent.ACTION_MOVE: {
                    // Find the index of the active pointer and fetch its position
                    if (activePointerIndex >= 0) {
                        updateDxDy(event, mSelectedFlags, activePointerIndex);
                        mRecyclerView.invalidate();
                    }
                    break;
                }
                case MotionEvent.ACTION_CANCEL:
                    if (mVelocityTracker != null) {
                        mVelocityTracker.clear();
                    }
                    // fall through
                case MotionEvent.ACTION_UP:
                    select(null, ACTION_STATE_IDLE);
                    mActivePointerId = ACTIVE_POINTER_ID_NONE;
                    break;
                case MotionEvent.ACTION_POINTER_UP: {
                    final int pointerIndex = event.getActionIndex();
                    final int pointerId = event.getPointerId(pointerIndex);
                    if (pointerId == mActivePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        mActivePointerId = event.getPointerId(newPointerIndex);
                        updateDxDy(event, mSelectedFlags, pointerIndex);
                    }
                    break;
                }
            }
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            if (!disallowIntercept) {
                return;
            }
            select(null, ACTION_STATE_IDLE);
        }
    };

    /**
     * Temporary rect instance that is used when we need to lookup Item decorations.
     */
    private Rect mTmpRect;

    /**
     * When user started to drag scroll. Reset when we don't scroll
     */
    private long mDragScrollStartTimeInMs;

    /**
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p>
     * You can attach ItemTouchHelper to a RecyclerView via
     * {@link #attachToRecyclerView(RecyclerView)}. Upon attaching, it will add an item decoration,
     * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
     *
     * @param callback The Callback which controls the behavior of this touch helper.
     */
    public ItemTouchHelper(ItemTouchHelper.Callback callback) {
        mCallback = callback;
    }

    private static boolean hitTest(View child, float x, float y, float left, float top) {
        return x >= left
                && x <= left + child.getWidth()
                && y >= top
                && y <= top + child.getHeight();
    }

    /**
     * Attaches the ItemTouchHelper to the provided RecyclerView. If TouchHelper is already
     * attached to a RecyclerView, it will first detach from the previous one. You can call this
     * method with {@code null} to detach it from the current RecyclerView.
     *
     * @param recyclerView The RecyclerView instance to which you want to add this helper or
     *                     {@code null} if you want to remove ItemTouchHelper from the current
     *                     RecyclerView.
     */
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        if (mRecyclerView == recyclerView) {
            return; // nothing to do
        }
        if (mRecyclerView != null) {
            destroyCallbacks();
        }
        mRecyclerView = recyclerView;
        if (recyclerView != null) {
            final Resources resources = recyclerView.getResources();
            mSwipeEscapeVelocity = resources
                    .getDimension(R.dimen.item_touch_helper_swipe_escape_velocity);
            mMaxSwipeVelocity = resources
                    .getDimension(R.dimen.item_touch_helper_swipe_escape_max_velocity);
            setupCallbacks();
        }
    }

    private void setupCallbacks() {
        ViewConfiguration vc = ViewConfiguration.get(mRecyclerView.getContext());
        mSlop = vc.getScaledTouchSlop();
        mRecyclerView.addItemDecoration(this);
        mRecyclerView.addOnItemTouchListener(mOnItemTouchListener);
        mRecyclerView.addOnChildAttachStateChangeListener(this);
    }

    private void destroyCallbacks() {
        mRecyclerView.removeItemDecoration(this);
        mRecyclerView.removeOnItemTouchListener(mOnItemTouchListener);
        mRecyclerView.removeOnChildAttachStateChangeListener(this);
        // clean all attached
        final int recoverAnimSize = mRecoverAnimations.size();
        for (int i = recoverAnimSize - 1; i >= 0; i--) {
            final RecoverAnimation recoverAnimation = mRecoverAnimations.get(0);
            mCallback.clearView(mRecyclerView, recoverAnimation.mViewHolder);
        }
        mRecoverAnimations.clear();
        mOverdrawChild = null;
        mOverdrawChildPosition = -1;
        releaseVelocityTracker();
    }


    private void getSelectedDxDy(float[] outPosition) {
        if ((mSelectedFlags & (LEFT | RIGHT)) != 0) {
            Log.d("getSelectedDxDy ", "mSelectedStartX " + mSelectedStartX + "mDx  "+ mDx + " getLeft() " +mSelected.itemView.getLeft());
            outPosition[0] =( mSelectedStartX + mDx - mSelected.itemView.getLeft() ) ;
        } else {
            outPosition[0] = mSelected.itemView.getTranslationX();
        }
        if ((mSelectedFlags & (UP | DOWN)) != 0) {
            outPosition[1] = mSelectedStartY + mDy - mSelected.itemView.getTop();
        } else {
            outPosition[1] = mSelected.itemView.getTranslationY();
        }
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        float dx = 0, dy = 0;
        if (mSelected != null) {
            getSelectedDxDy(mTmpPosition);
            dx = mTmpPosition[0];
            dy = mTmpPosition[1];
        }
        mCallback.onDrawOver(c, parent, mSelected,
                mRecoverAnimations, mActionState, dx, dy);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        // we don't know if RV changed something so we should invalidate this index.
        mOverdrawChildPosition = -1;
        float dx = 0, dy = 0;
        if (mSelected != null) {
            getSelectedDxDy(mTmpPosition);
            dx = mTmpPosition[0];
            dy = mTmpPosition[1];
        }
        mCallback.onDraw(c, parent, mSelected,
                mRecoverAnimations, mActionState, dx, dy);
    }

    /**
     * Starts dragging or swiping the given View. Call with null if you want to clear it.
     *
     * @param selected    The ViewHolder to drag or swipe. Can be null if you want to cancel the
     *                    current action
     * @param actionState The type of action
     */
    void select(ViewHolder selected, int actionState) {
        if (selected == mSelected && actionState == mActionState) {
            return;
        }
        mDragScrollStartTimeInMs = Long.MIN_VALUE;
        final int prevActionState = mActionState;
        // prevent duplicate animations
        endRecoverAnimation(selected, true);
        mActionState = actionState;
        if (actionState == ACTION_STATE_DRAG) {
            // we remove after animation is complete. this means we only elevate the last drag
            // child but that should perform good enough as it is very hard to start dragging a
            // new child before the previous one settles.
            mOverdrawChild = selected.itemView;
            addChildDrawingOrderCallback();
        }
        int actionStateMask = (1 << (DIRECTION_FLAG_COUNT + DIRECTION_FLAG_COUNT * actionState))
                - 1;
        boolean preventLayout = false;

        if (mSelected != null) {
            final ViewHolder prevSelected = mSelected;
            if (prevSelected.itemView.getParent() != null) {
                final int swipeDir = prevActionState == ACTION_STATE_DRAG ? 0
                        : swipeIfNecessary(prevSelected);
                releaseVelocityTracker();
                // find where we should animate to
                final float targetTranslateX, targetTranslateY;
                int animationType;
                switch (swipeDir) {
                    case LEFT:
                    case RIGHT:
                    case START:
                    case END:
                        targetTranslateY = 0;
                        targetTranslateX = Math.signum(mDx) * mRecyclerView.getWidth();
                        break;
                    case UP:
                    case DOWN:
                        targetTranslateX = 0;
                        targetTranslateY = Math.signum(mDy) * mRecyclerView.getHeight();
                        break;
                    default:
                        targetTranslateX = 0;
                        targetTranslateY = 0;
                }

                if (prevActionState == ACTION_STATE_DRAG) {
                    animationType = ANIMATION_TYPE_DRAG;
                } else if (swipeDir > 0) {
                    animationType = ANIMATION_TYPE_SWIPE_SUCCESS;
                } else {
                    animationType = ANIMATION_TYPE_SWIPE_CANCEL;
                }
                getSelectedDxDy(mTmpPosition);
                final float currentTranslateX = mTmpPosition[0];
                final float currentTranslateY = mTmpPosition[1];

                final RecoverAnimation rv = new RecoverAnimation(prevSelected, animationType,
                        prevActionState, currentTranslateX, currentTranslateY,
                        targetTranslateX, targetTranslateY, mCallback) {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (this.mOverridden) {
                            return;
                        }
                        if (swipeDir <= 0) {
                            // this is a drag or failed swipe. recover immediately
                            mCallback.clearView(mRecyclerView, prevSelected);
                            // full cleanup will happen on onDrawOver
                        } else {
                            // wait until remove animation is complete.
                            mPendingCleanup.add(prevSelected.itemView);
                            mIsPendingCleanup = true;
                            if (swipeDir > 0) {
                                // Animation might be ended by other animators during a layout.
                                // We defer callback to avoid editing adapter during a layout.
                                postDispatchSwipe(this, swipeDir);
                            }
                        }
                        // removed from the list after it is drawn for the last time
                        if (mOverdrawChild == prevSelected.itemView) {
                            removeChildDrawingOrderCallbackIfNecessary(prevSelected.itemView);
                        }
                    }
                };
                final long duration = mCallback.getAnimationDuration(mRecyclerView, animationType,
                        targetTranslateX - currentTranslateX, targetTranslateY - currentTranslateY);
                rv.setDuration(duration);
                mRecoverAnimations.add(rv);
                rv.start();
                preventLayout = true;
            } else {
                removeChildDrawingOrderCallbackIfNecessary(prevSelected.itemView);
                mCallback.clearView(mRecyclerView, prevSelected);
            }
            prevSelectedView = prevSelected;
            mSelected = null;
        }
        if (selected != null) {
            mSelectedFlags =
                    (mCallback.getAbsoluteMovementFlags(mRecyclerView, selected) & actionStateMask)
                            >> (mActionState * DIRECTION_FLAG_COUNT);
            mSelectedStartX = selected.itemView.getLeft();
            mSelectedStartY = selected.itemView.getTop();
            mSelected = selected;
        }
        final ViewParent rvParent = mRecyclerView.getParent();
        if (rvParent != null) {
            rvParent.requestDisallowInterceptTouchEvent(mSelected != null);
        }
        if (!preventLayout) {
            mRecyclerView.getLayoutManager().requestSimpleAnimationsInNextLayout();
        }
        Log.d("onSelectedChanged", " onSelectedChanged mActionState" + mActionState + "mSelected != null " + (mSelected != null));
        mCallback.onSelectedChanged(mSelected, mActionState);
        mRecyclerView.invalidate();
    }

    void revertPositionAnimation(ViewHolder prevSelected){
        if(!widthTakenIntoAccount || prevSelected == null)
            return;

        widthTakenIntoAccount = false;
        float currentTranslateX = prevSelected.itemView.getTranslationX();
        float targetX = 0;
        float targetY = 0;
        float animateDx = 0;

        final RecoverAnimation rv = new RecoverAnimation(prevSelected, ANIMATION_TYPE_REVERT,
                -1, currentTranslateX, 0,
                targetX, targetY, mCallback);
        final long duration = mCallback.getAnimationDuration(mRecyclerView, ANIMATION_TYPE_REVERT,
                animateDx - currentTranslateX, targetX - targetY);
        rv.getmValueAnimator().addUpdateListener((valueAnimator -> {
            rv.setFraction(valueAnimator.getAnimatedFraction());
            rv.update();
            Log.d("revertAnimation", " rv.mXv" + rv.mX);
            prevSelected.itemView.setTranslationX(rv.mX);
        }));
        rv.setDuration(duration);
        rv.start();

        mRecyclerView.getLayoutManager().requestSimpleAnimationsInNextLayout();
        mRecyclerView.invalidate();
    }

    void postDispatchSwipe(final ItemTouchHelper.RecoverAnimation anim, final int swipeDir) {
        // wait until animations are complete.
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (mRecyclerView != null && mRecyclerView.isAttachedToWindow()
                        && !anim.mOverridden
                        && anim.mViewHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    final RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
                    // if animator is running or we have other active recover animations, we try
                    // not to call onSwiped because DefaultItemAnimator is not good at merging
                    // animations. Instead, we wait and batch.
                    if ((animator == null || !animator.isRunning(null))
                            && !hasRunningRecoverAnim()) {
                        mCallback.onSwiped(anim.mViewHolder, swipeDir);
                    } else {
                        mRecyclerView.post(this);
                    }
                }
            }
        });
    }

    boolean hasRunningRecoverAnim() {
        final int size = mRecoverAnimations.size();
        for (int i = 0; i < size; i++) {
            if (!mRecoverAnimations.get(i).mEnded) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        removeChildDrawingOrderCallbackIfNecessary(view);
        final ViewHolder holder = mRecyclerView.getChildViewHolder(view);
        if (holder == null) {
            return;
        }
        if (mSelected != null && holder == mSelected) {
            select(null, ACTION_STATE_IDLE);
        } else {
            endRecoverAnimation(holder, false); // this may push it into pending cleanup list.
            if (mPendingCleanup.remove(holder.itemView)) {
                mCallback.clearView(mRecyclerView, holder);
            }
        }
    }

    /**
     * Returns the animation type or 0 if cannot be found.
     */
    int endRecoverAnimation(ViewHolder viewHolder, boolean override) {
        final int recoverAnimSize = mRecoverAnimations.size();
        for (int i = recoverAnimSize - 1; i >= 0; i--) {
            final ItemTouchHelper.RecoverAnimation anim = mRecoverAnimations.get(i);
            if (anim.mViewHolder == viewHolder) {
                anim.mOverridden |= override;
                if (!anim.mEnded) {
                    anim.cancel();
                }
                mRecoverAnimations.remove(i);
                return anim.mAnimationType;
            }
        }
        return 0;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.setEmpty();
    }

    void obtainVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
        }
        mVelocityTracker = VelocityTracker.obtain();
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private ViewHolder findSwipedView(MotionEvent motionEvent) {
        final RecyclerView.LayoutManager lm = mRecyclerView.getLayoutManager();
        if (mActivePointerId == ACTIVE_POINTER_ID_NONE) {
            return null;
        }
        final int pointerIndex = motionEvent.findPointerIndex(mActivePointerId);
        final float dx = motionEvent.getX(pointerIndex) - mInitialTouchX;
        final float dy = motionEvent.getY(pointerIndex) - mInitialTouchY;
        final float absDx = Math.abs(dx);
        final float absDy = Math.abs(dy);

        if (absDx < mSlop && absDy < mSlop) {
            return null;
        }
        if (absDx > absDy && lm.canScrollHorizontally()) {
            return null;
        } else if (absDy > absDx && lm.canScrollVertically()) {
            return null;
        }
        View child = findChildView(motionEvent);
        if (child == null) {
            return null;
        }
        return mRecyclerView.getChildViewHolder(child);
    }

    /**
     * Checks whether we should select a View for swiping.
     */
    boolean checkSelectForSwipe(int action, MotionEvent motionEvent, int pointerIndex) {
        if (mSelected != null || action != MotionEvent.ACTION_MOVE
                || mActionState == ACTION_STATE_DRAG || !mCallback.isItemViewSwipeEnabled()) {
            return false;
        }
        if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) {
            return false;
        }
        final ViewHolder vh = findSwipedView(motionEvent);
        if (vh == null) {
            return false;
        }
        final int movementFlags = mCallback.getAbsoluteMovementFlags(mRecyclerView, vh);

        final int swipeFlags = (movementFlags & ACTION_MODE_SWIPE_MASK)
                >> (DIRECTION_FLAG_COUNT * ACTION_STATE_SWIPE);

        if (swipeFlags == 0) {
            return false;
        }

        // mDx and mDy are only set in allowed directions. We use custom x/y here instead of
        // updateDxDy to avoid swiping if user moves more in the other direction
        final float x = motionEvent.getX(pointerIndex);
        final float y = motionEvent.getY(pointerIndex);

        // Calculate the distance moved
        final float dx = x - mInitialTouchX;
        final float dy = y - mInitialTouchY;
        // swipe target is chose w/o applying flags so it does not really check if swiping in that
        // direction is allowed. This why here, we use mDx mDy to check slope value again.
        final float absDx = Math.abs(dx);
        final float absDy = Math.abs(dy);

        if (absDx < mSlop && absDy < mSlop) {
            return false;
        }
        if (absDx > absDy) {
            if (dx < 0 && (swipeFlags & LEFT) == 0) {
                return false;
            }
            if (dx > 0 && (swipeFlags & RIGHT) == 0) {
                return false;
            }
        } else {
            if (dy < 0 && (swipeFlags & UP) == 0) {
                return false;
            }
            if (dy > 0 && (swipeFlags & DOWN) == 0) {
                return false;
            }
        }
        mDx = mDy = 0f;
        mActivePointerId = motionEvent.getPointerId(0);
        select(vh, ACTION_STATE_SWIPE);
        return true;
    }

    View findChildView(MotionEvent event) {
        // first check elevated views, if none, then call RV
        final float x = event.getX();
        final float y = event.getY();
        if (mSelected != null) {
            final View selectedView = mSelected.itemView;
            if (hitTest(selectedView, x, y, mSelectedStartX + mDx, mSelectedStartY + mDy)) {
                return selectedView;
            }
        }
        for (int i = mRecoverAnimations.size() - 1; i >= 0; i--) {
            final ItemTouchHelper.RecoverAnimation anim = mRecoverAnimations.get(i);
            final View view = anim.mViewHolder.itemView;
            if (hitTest(view, x, y, anim.mX, anim.mY)) {
                return view;
            }
        }
        return mRecyclerView.findChildViewUnder(x, y);
    }

    /**
     * Starts swiping the provided ViewHolder. By default, ItemTouchHelper starts swiping a View
     * when user swipes their finger (or mouse pointer) over the View. You can disable this
     * behavior
     * by overriding {@link android.support.v7.widget.helper.ItemTouchHelper.Callback}
     * <p>
     * For this method to work:
     * <ul>
     * <li>The provided ViewHolder must be a child of the RecyclerView to which this
     * ItemTouchHelper is attached.</li>
     * <li>{@link android.support.v7.widget.helper.ItemTouchHelper.Callback} must have swiping enabled.</li>
     * <li>There must be a previous touch event that was reported to the ItemTouchHelper
     * through RecyclerView's ItemTouchListener mechanism. As long as no other ItemTouchListener
     * grabs previous events, this should work as expected.</li>
     * </ul>
     *
     * For example, if you would like to let your user to be able to swipe an Item by touching one
     * of its descendants, you may implement it as follows:
     * <pre>
     *     viewHolder.dragButton.setOnTouchListener(new View.OnTouchListener() {
     *         public boolean onTouch(View v, MotionEvent event) {
     *             if (MotionEvent.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
     *                 mItemTouchHelper.startSwipe(viewHolder);
     *             }
     *             return false;
     *         }
     *     });
     * </pre>
     *
     * @param viewHolder The ViewHolder to start swiping. It must be a direct child of
     *                   RecyclerView.
     */
    public void startSwipe(ViewHolder viewHolder) {
        if (!mCallback.hasSwipeFlag(mRecyclerView, viewHolder)) {
            Log.e(TAG, "Start swipe has been called but swiping is not enabled");
            return;
        }
        if (viewHolder.itemView.getParent() != mRecyclerView) {
            Log.e(TAG, "Start swipe has been called with a view holder which is not a child of "
                    + "the RecyclerView controlled by this ItemTouchHelper.");
            return;
        }
        obtainVelocityTracker();
        mDx = mDy = 0f;
        select(viewHolder, ACTION_STATE_SWIPE);
    }

    RecoverAnimation findAnimation(MotionEvent event) {
        if (mRecoverAnimations.isEmpty()) {
            return null;
        }
        View target = findChildView(event);
        for (int i = mRecoverAnimations.size() - 1; i >= 0; i--) {
            final RecoverAnimation anim = mRecoverAnimations.get(i);
            if (anim.mViewHolder.itemView == target) {
                return anim;
            }
        }
        return null;
    }

    void updateDxDy(MotionEvent ev, int directionFlags, int pointerIndex) {
        final float x = ev.getX(pointerIndex);
        final float y = ev.getY(pointerIndex);

        Log.d("updateDxDy", "x "  + mSelected.itemView.getTranslationX());
        // Calculate the distance moved
        mDx = x - mInitialTouchX ;
        mDy = y - mInitialTouchY;
        if ((directionFlags & LEFT) == 0) {
            mDx = Math.max(0, mDx);
        }
        if ((directionFlags & RIGHT) == 0) {
            mDx = Math.min(0, mDx);
        }
        if ((directionFlags & UP) == 0) {
            mDy = Math.max(0, mDy);
        }
        if ((directionFlags & DOWN) == 0) {
            mDy = Math.min(0, mDy);
        }
    }

    private int swipeIfNecessary(ViewHolder viewHolder) {
        if (mActionState == ACTION_STATE_DRAG) {
            return 0;
        }
        final int originalMovementFlags = mCallback.getMovementFlags(mRecyclerView, viewHolder);
        final int absoluteMovementFlags = mCallback.convertToAbsoluteDirection(
                originalMovementFlags,
                ViewCompat.getLayoutDirection(mRecyclerView));
        final int flags = (absoluteMovementFlags
                & ACTION_MODE_SWIPE_MASK) >> (ACTION_STATE_SWIPE * DIRECTION_FLAG_COUNT);
        if (flags == 0) {
            return 0;
        }
        final int originalFlags = (originalMovementFlags
                & ACTION_MODE_SWIPE_MASK) >> (ACTION_STATE_SWIPE * DIRECTION_FLAG_COUNT);
        int swipeDir;
        if (Math.abs(mDx) > Math.abs(mDy)) {
            if ((swipeDir = checkHorizontalSwipe(viewHolder, flags)) > 0) {
                // if swipe dir is not in original flags, it should be the relative direction
                if ((originalFlags & swipeDir) == 0) {
                    // convert to relative
                    return android.support.v7.widget.helper.ItemTouchHelper.Callback.convertToRelativeDirection(swipeDir,
                            ViewCompat.getLayoutDirection(mRecyclerView));
                }
                return swipeDir;
            }
            if ((swipeDir = checkVerticalSwipe(viewHolder, flags)) > 0) {
                return swipeDir;
            }
        } else {
            if ((swipeDir = checkVerticalSwipe(viewHolder, flags)) > 0) {
                return swipeDir;
            }
            if ((swipeDir = checkHorizontalSwipe(viewHolder, flags)) > 0) {
                // if swipe dir is not in original flags, it should be the relative direction
                if ((originalFlags & swipeDir) == 0) {
                    // convert to relative
                    return ItemTouchHelper.Callback.convertToRelativeDirection(swipeDir,
                            ViewCompat.getLayoutDirection(mRecyclerView));
                }
                return swipeDir;
            }
        }
        return 0;
    }

    private int checkHorizontalSwipe(ViewHolder viewHolder, int flags) {
        if ((flags & (LEFT | RIGHT)) != 0) {
            final int dirFlag = mDx > 0 ? RIGHT : LEFT;
            if (mVelocityTracker != null && mActivePointerId > -1) {
                mVelocityTracker.computeCurrentVelocity(PIXELS_PER_SECOND,
                        mCallback.getSwipeVelocityThreshold(mMaxSwipeVelocity));
                final float xVelocity = mVelocityTracker.getXVelocity(mActivePointerId);
                final float yVelocity = mVelocityTracker.getYVelocity(mActivePointerId);
                final int velDirFlag = xVelocity > 0f ? RIGHT : LEFT;
                final float absXVelocity = Math.abs(xVelocity);
                if ((velDirFlag & flags) != 0 && dirFlag == velDirFlag
                        && absXVelocity >= mCallback.getSwipeEscapeVelocity(mSwipeEscapeVelocity)
                        && absXVelocity > Math.abs(yVelocity)) {
                    return velDirFlag;
                }
            }

            final float threshold = mRecyclerView.getWidth() * mCallback
                    .getSwipeThreshold(viewHolder);

            if ((flags & dirFlag) != 0 && Math.abs(mDx) > threshold) {
                return dirFlag;
            }
        }
        return 0;
    }

    private int checkVerticalSwipe(ViewHolder viewHolder, int flags) {
        if ((flags & (UP | DOWN)) != 0) {
            final int dirFlag = mDy > 0 ? DOWN : UP;
            if (mVelocityTracker != null && mActivePointerId > -1) {
                mVelocityTracker.computeCurrentVelocity(PIXELS_PER_SECOND,
                        mCallback.getSwipeVelocityThreshold(mMaxSwipeVelocity));
                final float xVelocity = mVelocityTracker.getXVelocity(mActivePointerId);
                final float yVelocity = mVelocityTracker.getYVelocity(mActivePointerId);
                final int velDirFlag = yVelocity > 0f ? DOWN : UP;
                final float absYVelocity = Math.abs(yVelocity);
                if ((velDirFlag & flags) != 0 && velDirFlag == dirFlag
                        && absYVelocity >= mCallback.getSwipeEscapeVelocity(mSwipeEscapeVelocity)
                        && absYVelocity > Math.abs(xVelocity)) {
                    return velDirFlag;
                }
            }

            final float threshold = mRecyclerView.getHeight() * mCallback
                    .getSwipeThreshold(viewHolder);
            if ((flags & dirFlag) != 0 && Math.abs(mDy) > threshold) {
                return dirFlag;
            }
        }
        return 0;
    }

    private void addChildDrawingOrderCallback() {
        if (Build.VERSION.SDK_INT >= 21) {
            return; // we use elevation on Lollipop
        }
        if (mChildDrawingOrderCallback == null) {
            mChildDrawingOrderCallback = new RecyclerView.ChildDrawingOrderCallback() {
                @Override
                public int onGetChildDrawingOrder(int childCount, int i) {
                    if (mOverdrawChild == null) {
                        return i;
                    }
                    int childPosition = mOverdrawChildPosition;
                    if (childPosition == -1) {
                        childPosition = mRecyclerView.indexOfChild(mOverdrawChild);
                        mOverdrawChildPosition = childPosition;
                    }
                    if (i == childCount - 1) {
                        return childPosition;
                    }
                    return i < childPosition ? i : i + 1;
                }
            };
        }
        mRecyclerView.setChildDrawingOrderCallback(mChildDrawingOrderCallback);
    }

    void removeChildDrawingOrderCallbackIfNecessary(View view) {
        if (view == mOverdrawChild) {
            mOverdrawChild = null;
            // only remove if we've added
            if (mChildDrawingOrderCallback != null) {
                mRecyclerView.setChildDrawingOrderCallback(null);
            }
        }
    }

    @SuppressWarnings("UnusedParameters")
    public abstract static class Callback {

        public static final int DEFAULT_DRAG_ANIMATION_DURATION = 200;

        public static final int DEFAULT_SWIPE_ANIMATION_DURATION = 250;

        private static float startingPoint = 0;
        private static boolean isStartingPointIndicated = false;

        static final int RELATIVE_DIR_FLAGS = START | END
                | ((START | END) << DIRECTION_FLAG_COUNT)
                | ((START | END) << (2 * DIRECTION_FLAG_COUNT));

        private static final ItemTouchUIUtil sUICallback;

        private static final int ABS_HORIZONTAL_DIR_FLAGS = LEFT | RIGHT
                | ((LEFT | RIGHT) << DIRECTION_FLAG_COUNT)
                | ((LEFT | RIGHT) << (2 * DIRECTION_FLAG_COUNT));

        private static final Interpolator sDragScrollInterpolator = new Interpolator() {
            @Override
            public float getInterpolation(float t) {
                return t * t * t * t * t;
            }
        };

        private static final Interpolator sDragViewScrollCapInterpolator = new Interpolator() {
            @Override
            public float getInterpolation(float t) {
                t -= 1.0f;
                return t * t * t * t * t + 1.0f;
            }
        };

        /**
         * Drag scroll speed keeps accelerating until this many milliseconds before being capped.
         */
        private static final long DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS = 2000;

        private int mCachedMaxScrollSpeed = -1;

        static {
            if (Build.VERSION.SDK_INT >= 21) {
                sUICallback = new ItemTouchUIUtilImpl.Api21Impl();
            } else {
                sUICallback = new ItemTouchUIUtilImpl.BaseImpl();
            }
        }

        public static ItemTouchUIUtil getDefaultUIUtil() {
            return sUICallback;
        }


        public static int convertToRelativeDirection(int flags, int layoutDirection) {
            int masked = flags & ABS_HORIZONTAL_DIR_FLAGS;
            if (masked == 0) {
                return flags; // does not have any abs flags, good.
            }
            flags &= ~masked; //remove left / right.
            if (layoutDirection == ViewCompat.LAYOUT_DIRECTION_LTR) {
                // no change. just OR with 2 bits shifted mask and return
                flags |= masked << 2; // START is 2 bits after LEFT, END is 2 bits after RIGHT.
                return flags;
            } else {
                // add RIGHT flag as START
                flags |= ((masked << 1) & ~ABS_HORIZONTAL_DIR_FLAGS);
                // first clean RIGHT bit then add LEFT flag as END
                flags |= ((masked << 1) & ABS_HORIZONTAL_DIR_FLAGS) << 2;
            }
            return flags;
        }

        /**
         * Convenience method to create movement flags.
         **/
        public static int makeMovementFlags(int dragFlags, int swipeFlags) {
            return makeFlag(ACTION_STATE_IDLE, swipeFlags | dragFlags)
                    | makeFlag(ACTION_STATE_SWIPE, swipeFlags)
                    | makeFlag(ACTION_STATE_DRAG, dragFlags);
        }

        /**
         * Shifts the given direction flags to the offset of the given action state.
         *
         */
        public static int makeFlag(int actionState, int directions) {
            return directions << (actionState * DIRECTION_FLAG_COUNT);
        }

        /**
         * Should return a composite flag which defines the enabled move directions in each state
         *
         */
        public abstract int getMovementFlags(RecyclerView recyclerView,
                                             ViewHolder viewHolder);

        /**
         * Converts a given set of flags to absolution direction which means {@link #START} and
         * {@link #END} are replaced with {@link #LEFT} and {@link #RIGHT} depending on the layout
         * direction.
         */
        public int convertToAbsoluteDirection(int flags, int layoutDirection) {
            int masked = flags & RELATIVE_DIR_FLAGS;
            if (masked == 0) {
                return flags; // does not have any relative flags, good.
            }
            flags &= ~masked; //remove start / end
            if (layoutDirection == ViewCompat.LAYOUT_DIRECTION_LTR) {
                // no change. just OR with 2 bits shifted mask and return
                flags |= masked >> 2; // START is 2 bits after LEFT, END is 2 bits after RIGHT.
                return flags;
            } else {
                // add START flag as RIGHT
                flags |= ((masked >> 1) & ~RELATIVE_DIR_FLAGS);
                // first clean start bit then add END flag as LEFT
                flags |= ((masked >> 1) & RELATIVE_DIR_FLAGS) >> 2;
            }
            return flags;
        }

        final int getAbsoluteMovementFlags(RecyclerView recyclerView,
                                           ViewHolder viewHolder) {
            final int flags = getMovementFlags(recyclerView, viewHolder);
            return convertToAbsoluteDirection(flags, ViewCompat.getLayoutDirection(recyclerView));
        }


        boolean hasSwipeFlag(RecyclerView recyclerView,
                             ViewHolder viewHolder) {
            final int flags = getAbsoluteMovementFlags(recyclerView, viewHolder);
            return (flags & ACTION_MODE_SWIPE_MASK) != 0;
        }

        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        public int getBoundingBoxMargin() {
            return 0;
        }

        public float getSwipeThreshold(ViewHolder viewHolder) {
            return .5f;
        }

        public float getMoveThreshold(ViewHolder viewHolder) {
            return .5f;
        }


        public float getSwipeEscapeVelocity(float defaultValue) {
            return defaultValue;
        }

        public float getSwipeVelocityThreshold(float defaultValue) {
            return defaultValue;
        }

        public ViewHolder chooseDropTarget(ViewHolder selected,
                                           List<ViewHolder> dropTargets, int curX, int curY) {
            int right = curX + selected.itemView.getWidth();
            int bottom = curY + selected.itemView.getHeight();
            ViewHolder winner = null;
            int winnerScore = -1;
            final int dx = curX - selected.itemView.getLeft();
            final int dy = curY - selected.itemView.getTop();
            final int targetsSize = dropTargets.size();
            for (int i = 0; i < targetsSize; i++) {
                final ViewHolder target = dropTargets.get(i);
                if (dx > 0) {
                    int diff = target.itemView.getRight() - right;
                    if (diff < 0 && target.itemView.getRight() > selected.itemView.getRight()) {
                        final int score = Math.abs(diff);
                        if (score > winnerScore) {
                            winnerScore = score;
                            winner = target;
                        }
                    }
                }
                if (dx < 0) {
                    int diff = target.itemView.getLeft() - curX;
                    if (diff > 0 && target.itemView.getLeft() < selected.itemView.getLeft()) {
                        final int score = Math.abs(diff);
                        if (score > winnerScore) {
                            winnerScore = score;
                            winner = target;
                        }
                    }
                }
                if (dy < 0) {
                    int diff = target.itemView.getTop() - curY;
                    if (diff > 0 && target.itemView.getTop() < selected.itemView.getTop()) {
                        final int score = Math.abs(diff);
                        if (score > winnerScore) {
                            winnerScore = score;
                            winner = target;
                        }
                    }
                }

                if (dy > 0) {
                    int diff = target.itemView.getBottom() - bottom;
                    if (diff < 0 && target.itemView.getBottom() > selected.itemView.getBottom()) {
                        final int score = Math.abs(diff);
                        if (score > winnerScore) {
                            winnerScore = score;
                            winner = target;
                        }
                    }
                }
            }
            return winner;
        }

        public abstract void onSwiped(ViewHolder viewHolder, int direction);

        public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
            if (viewHolder != null) {
                sUICallback.onSelected(viewHolder.itemView);
            }
        }

        private int getMaxDragScroll(RecyclerView recyclerView) {
            if (mCachedMaxScrollSpeed == -1) {
                mCachedMaxScrollSpeed = recyclerView.getResources().getDimensionPixelSize(
                        R.dimen.item_touch_helper_max_drag_scroll_per_frame);
            }
            return mCachedMaxScrollSpeed;
        }


        void onDraw(Canvas c, RecyclerView parent, ViewHolder selected,
                    List<ItemTouchHelper.RecoverAnimation> recoverAnimationList,
                    int actionState, float dX, float dY) {
            final int recoverAnimSize = recoverAnimationList.size();
            Log.d("onDrawDebug", " recoverAnimSize " + recoverAnimationList.size());
            for (int i = 0; i < recoverAnimSize; i++) {
                final ItemTouchHelper.RecoverAnimation anim = recoverAnimationList.get(i);
                anim.update();
                final int count = c.save();
                Log.d("onDrawDebug", " anim.mX " + anim.mX + " anim.mY " +  anim.mY);
                widthTakenIntoAccount = Math.abs(anim.mX) == drawnViewWidth() ? true : false;
                onChildDraw(c, parent, anim.mViewHolder, anim.mX, anim.mY, anim.mActionState,
                        false);
                c.restoreToCount(count);
            }
            if (selected != null) {
                final int count = c.save();
                float startingPoint = calculateStartingPoint(dX);
                Log.d("onDrawDebug", " startingPoint  "  + startingPoint);
                onChildDraw(c, parent, selected, startingPoint, dY, actionState, true);
                c.restoreToCount(count);
            }
        }

        float calculateStartingPoint(float dX){
            if(widthTakenIntoAccount)
               return dX -= drawnViewWidth();
            else
                return dX;
        }


        void onDrawOver(Canvas c, RecyclerView parent, ViewHolder selected,
                        List<ItemTouchHelper.RecoverAnimation> recoverAnimationList,
                        int actionState, float dX, float dY) {
            final int recoverAnimSize = recoverAnimationList.size();
            for (int i = 0; i < recoverAnimSize; i++) {
                final ItemTouchHelper.RecoverAnimation anim = recoverAnimationList.get(i);
                final int count = c.save();
                onChildDrawOver(c, parent, anim.mViewHolder, anim.mX, anim.mY, anim.mActionState,
                        false);
                c.restoreToCount(count);
            }
            if (selected != null) {
                final int count = c.save();
                onChildDrawOver(c, parent, selected, dX, dY, actionState, true);
                c.restoreToCount(count);
            }
            boolean hasRunningAnimation = false;
            for (int i = recoverAnimSize - 1; i >= 0; i--) {
                final ItemTouchHelper.RecoverAnimation anim = recoverAnimationList.get(i);
                if (anim.mEnded && !anim.mIsPendingCleanup) {
                    recoverAnimationList.remove(i);
                } else if (!anim.mEnded) {
                    hasRunningAnimation = true;
                }
            }
            if (hasRunningAnimation) {
                parent.invalidate();
            }
        }

        public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
            sUICallback.clearView(viewHolder.itemView);
        }


        public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
            sUICallback.onDraw(c, recyclerView, viewHolder.itemView, dX, dY, actionState,
                    isCurrentlyActive);
        }

        public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                    ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
            sUICallback.onDrawOver(c, recyclerView, viewHolder.itemView, dX, dY, actionState,
                    isCurrentlyActive);
        }

        /**
         * Called by the ItemTouchHelper when user action finished on a ViewHolder and now the View
         * will be animated to its final position.
         * <p>
         * Default implementation uses ItemAnimator's duration values. If
         * <code>animationType</code> is {@link #ANIMATION_TYPE_DRAG}, it returns
         * {@link RecyclerView.ItemAnimator#getMoveDuration()}, otherwise, it returns
         * {@link RecyclerView.ItemAnimator#getRemoveDuration()}. If RecyclerView does not have
         * any {@link RecyclerView.ItemAnimator} attached, this method returns
         * {@code DEFAULT_DRAG_ANIMATION_DURATION} or {@code DEFAULT_SWIPE_ANIMATION_DURATION}
         * depending on the animation type.
         *
         * @param recyclerView  The RecyclerView to which the ItemTouchHelper is attached to.
         * @param animationType The type of animation. Is one of {@link #ANIMATION_TYPE_DRAG},
         *                      {@link #ANIMATION_TYPE_SWIPE_CANCEL} or
         *                      {@link #ANIMATION_TYPE_SWIPE_SUCCESS}.
         * @param animateDx     The horizontal distance that the animation will offset
         * @param animateDy     The vertical distance that the animation will offset
         * @return The duration for the animation
         */
        public long getAnimationDuration(RecyclerView recyclerView, int animationType,
                                         float animateDx, float animateDy) {
            final RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
            if (itemAnimator == null) {
                return animationType == ANIMATION_TYPE_DRAG ? DEFAULT_DRAG_ANIMATION_DURATION
                        : DEFAULT_SWIPE_ANIMATION_DURATION;
            } else {
                return animationType == ANIMATION_TYPE_DRAG ? itemAnimator.getMoveDuration()
                        : itemAnimator.getRemoveDuration();
            }
        }

        /**
         * Called by the ItemTouchHelper when user is dragging a view out of bounds.
         * <p>
         * You can override this method to decide how much RecyclerView should scroll in response
         * to this action. Default implementation calculates a value based on the amount of View
         * out of bounds and the time it spent there. The longer user keeps the View out of bounds,
         * the faster the list will scroll. Similarly, the larger portion of the View is out of
         * bounds, the faster the RecyclerView will scroll.
         *
         * @param recyclerView        The RecyclerView instance to which ItemTouchHelper is
         *                            attached to.
         * @param viewSize            The total size of the View in scroll direction, excluding
         *                            item decorations.
         * @param viewSizeOutOfBounds The total size of the View that is out of bounds. This value
         *                            is negative if the View is dragged towards left or top edge.
         * @param totalSize           The total size of RecyclerView in the scroll direction.
         * @param msSinceStartScroll  The time passed since View is kept out of bounds.
         * @return The amount that RecyclerView should scroll. Keep in mind that this value will
         * be passed to {@link RecyclerView#scrollBy(int, int)} method.
         */
        public int interpolateOutOfBoundsScroll(RecyclerView recyclerView,
                                                int viewSize, int viewSizeOutOfBounds,
                                                int totalSize, long msSinceStartScroll) {
            final int maxScroll = getMaxDragScroll(recyclerView);
            final int absOutOfBounds = Math.abs(viewSizeOutOfBounds);
            final int direction = (int) Math.signum(viewSizeOutOfBounds);
            // might be negative if other direction
            float outOfBoundsRatio = Math.min(1f, 1f * absOutOfBounds / viewSize);
            final int cappedScroll = (int) (direction * maxScroll
                    * sDragViewScrollCapInterpolator.getInterpolation(outOfBoundsRatio));
            final float timeRatio;
            if (msSinceStartScroll > DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS) {
                timeRatio = 1f;
            } else {
                timeRatio = (float) msSinceStartScroll / DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS;
            }
            final int value = (int) (cappedScroll * sDragScrollInterpolator
                    .getInterpolation(timeRatio));
            if (value == 0) {
                return viewSizeOutOfBounds > 0 ? 1 : -1;
            }
            return value;
        }


        public abstract int drawnViewWidth();
    }

    public static class RecoverAnimation implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

        final float mStartDx;

        final float mStartDy;

        final float mTargetX;

        final float mTargetY;

        final ViewHolder mViewHolder;

        final int mActionState;

        private final ValueAnimator mValueAnimator;

        final int mAnimationType;

        public boolean mIsPendingCleanup;

        float mX;

        float mY;

        // if user starts touching a recovering view, we put it into interaction mode again,
        // instantly.
        boolean mOverridden = false;

        boolean mEnded = false;


        private float mFraction;

        ItemTouchHelper.Callback mCallback;

        RecoverAnimation(ViewHolder viewHolder, int animationType,
                         int actionState, float startDx, float startDy, float targetX, float targetY, ItemTouchHelper.Callback callback) {
            mCallback = callback;
            mActionState = actionState;
            mAnimationType = animationType;
            mViewHolder = viewHolder;
            mStartDx = widthTakenIntoAccount ? startDx + mViewHolder.itemView.getTranslationX() : startDx;
            mStartDy = startDy;
            mTargetX = targetX;
            mTargetY = targetY;
            Log.d("RecoverAnimation", "startDx "+ startDx + " widthTakenIntoAccount " + widthTakenIntoAccount);
            mValueAnimator = ValueAnimator.ofFloat(0f, 1f);
            mValueAnimator.addUpdateListener(this);
            mValueAnimator.setTarget(viewHolder.itemView);
            mValueAnimator.addListener(this);
            setFraction(0f);
        }

        public void setDuration(long duration) {
            mValueAnimator.setDuration(duration);
        }

        public void start() {
            mViewHolder.setIsRecyclable(false);
            mValueAnimator.start();
        }

        public void cancel() {
            mValueAnimator.cancel();
        }

        public void setFraction(float fraction) {
            mFraction = fraction;
        }

        public ValueAnimator getmValueAnimator(){
            return mValueAnimator;
        }

        /**
         * We run updates on onDraw method but use the fraction from animator callback.
         * This way, we can sync translate x/y values w/ the animators to avoid one-off frames.
         */
        public void update() {
            if (mStartDx == mTargetX) {
                mX = mViewHolder.itemView.getTranslationX();
            } else {
                mX = mStartDx + mFraction * (( mTargetX - takeWidthIntoAccount()) - mStartDx);
            }
            if (mStartDy == mTargetY) {
                mY = mViewHolder.itemView.getTranslationY();
            } else {
                mY = mStartDy + mFraction * (mTargetY - mStartDy);
            }
        }

        private float takeWidthIntoAccount(){
            if(Math.abs(mStartDx) > mCallback.drawnViewWidth()) {
                return mCallback.drawnViewWidth();
            }else {
                return 0;
            }
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            setFraction(valueAnimator.getAnimatedFraction());
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!mEnded) {
                mViewHolder.setIsRecyclable(true);
            }
            mEnded = true;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            setFraction(1f); //make sure we recover the view's state.
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
