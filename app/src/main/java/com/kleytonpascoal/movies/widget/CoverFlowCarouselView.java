package com.kleytonpascoal.movies.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.kleytonpascoal.movies.R;

/**
 * Created by kleyton on 09/05/17.
 */

public class CoverFlowCarouselView extends BaseRecyclerView {

    private final static String TAG = CoverFlowCarouselView.class.getSimpleName();

    private final Camera mCamera = new Camera();
    private final Matrix mMatrix = new Matrix();

    private final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);

    private int parentCenterHorizontal;
    private int parentCenterVertical;

    private int parentWidth;
    private int childViewWith;
    private boolean isDraggingNow;

    private int visibleViewsCount;
    private int centerVisibleChildPosition;

    public CoverFlowCarouselView(Context context) {
        super(context);
        setUp(context);
    }

    public CoverFlowCarouselView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp(context);
    }

    public CoverFlowCarouselView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUp(context);
    }

    private void setUp(Context context) {
        mPaint.setAntiAlias(true);
        setChildrenDrawingOrderEnabled(true);
        visibleViewsCount = context.getResources().getInteger(R.integer.cover_flow_carousel_visible_views);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setWidths(w, h);
    }

    private void setWidths(int width, int height) {
        parentWidth = width;
        childViewWith = parentWidth / visibleViewsCount;

        parentCenterHorizontal = width / 2;
        parentCenterVertical = height / 2;

    }

    @Override
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {

        /*child.getLayoutParams().width = childViewWith;*/

        final int childLeft = child.getLeft();
        final int childTop = child.getTop();

        final int childCenterHorizontal = child.getWidth() / 2;
        final int childCenterVertical = child.getHeight() / 2;

        final int childCenterHorizontalInParent = childCenterHorizontal + childLeft;
        //final int childCenterVerticalInParent = childCenterVertical + childTop;

        final int childDistanceHorizontal = parentCenterHorizontal - childCenterHorizontalInParent;
        //final int childDistanceVertical = parentCenterVertical - childCenterVerticalInParent;

        prepareTransformation(childDistanceHorizontal, parentCenterHorizontal);

        mMatrix.preTranslate(-childCenterHorizontal, -childCenterVertical);
        mMatrix.postTranslate(childCenterHorizontal, childCenterVertical);
        mMatrix.postTranslate(childLeft, childTop);

        child.setDrawingCacheEnabled(true);

        final Bitmap bitmap = getChildDrawingCache(child);
        canvas.drawBitmap(bitmap, mMatrix, mPaint);

        child.setDrawingCacheEnabled(false);
        return false;
    }

    private void prepareTransformation(int distance, int radius) {

        final int minDistance = Math.min(Math.abs(distance), radius);
        final float translateZ = (float) Math.sqrt((radius * radius) - (minDistance * minDistance));

        mCamera.save();
        float offset = radius - translateZ;
        mCamera.translate(0, 0, offset);

        float deg = offset / TILTED_FACTOR;
        mCamera.rotateY(distance > 0 ? deg : -deg);

        mCamera.getMatrix(mMatrix);
        mCamera.restore();
    }

    final int TILTED_FACTOR = 10;

    private Bitmap getChildDrawingCache(final View childView) {

        childView.buildDrawingCache();
        Bitmap bitmap = childView.getDrawingCache();
        if (bitmap == null) {
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache(true);
            bitmap = childView.getDrawingCache();
        }
        return bitmap;
    }

    private int childCenterPosition;

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {

        //find center row
        if (childCount % 2 == 0) { //even childCount number
            centerVisibleChildPosition = childCount / 2;

            // if childCount 8 (actualy 0 - 7), then 4 and 4-1 = 3 is in centre.
            int otherCenterChild = centerVisibleChildPosition - 1;

            final View centerChildView = getChildAt(centerVisibleChildPosition);
            final int left = centerChildView.getLeft();
            final int right = centerChildView.getRight();

            final int childWith = centerChildView.getWidth();
            centerVisibleChildPosition = left < (parentCenterHorizontal - childWith)
                    && right > (parentCenterHorizontal + childWith) ?
                    centerVisibleChildPosition : otherCenterChild;
        } else {
            //not even - done
            centerVisibleChildPosition = childCount / 2;
        }

        return super.getChildDrawingOrder(childCount, i);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        switch (state) {

            case SCROLL_STATE_IDLE:
                Log.d(TAG, "scroll state idle");
                if (!isDraggingNow)
                    return;

                isDraggingNow = false;

                LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();

                final int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                childCenterPosition = firstVisibleItemPosition + centerVisibleChildPosition;

                final View view = layoutManager.findViewByPosition(childCenterPosition);
                if (view == null)
                    return;

                final int viewCenter = view.getLeft() + childViewWith / 2;
                final int viewDistance = viewCenter - parentCenterHorizontal;

                final int nextChildPosition = childCenterPosition + 1;
                if (nextChildPosition >= getChildCount())
                    return;

                final View nextView = layoutManager.findViewByPosition(nextChildPosition);
                final int nextViewCenter = nextView.getLeft() + childViewWith / 2;
                final int nextViewDistance = nextViewCenter - parentCenterHorizontal;

                int[] location = new int[2];
                int dx;

                if (Math.abs(viewDistance) < Math.abs(nextViewDistance)) {
                    Log.d(TAG, "viewDistance: " + viewDistance);
                    view.getLocationInWindow(location);
                } else {
                    Log.d(TAG, "nextViewDistance: " + nextViewDistance);
                    nextView.getLocationInWindow(location);
                }

                dx = location[0] + childViewWith / 2 - parentCenterHorizontal;
                smoothScrollBy(dx, 0);
                break;

            case SCROLL_STATE_DRAGGING:
                Log.d(TAG, "scroll state drag");
                isDraggingNow = true;
                break;

            default:
                break;
        }
    }
}
