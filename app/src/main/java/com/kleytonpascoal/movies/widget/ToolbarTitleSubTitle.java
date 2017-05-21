package com.kleytonpascoal.movies.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kleytonpascoal.movies.R;

/**
 * Created by kleyton on 11/05/17.
 */

public class ToolbarTitleSubTitle extends LinearLayout {

    private TextView mTitle;
    private TextView mSubTitle;

    public ToolbarTitleSubTitle(Context context) {
        super(context);
        init(context);
    }

    public ToolbarTitleSubTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ToolbarTitleSubTitle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ToolbarTitleSubTitle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.toolbar_title_subtitle, this);
        mTitle = (TextView) findViewById(R.id.toolbar_title_subtitle_view_title);
        mSubTitle = (TextView) findViewById(R.id.toolbar_title_subtitle_view_subtitle);
    }

    public void setTitleSubtitle(String title, String subtitle) {
        if (!TextUtils.isEmpty(title))
            mTitle.setText(title);
        else
            mTitle.setVisibility(GONE);

        if (!TextUtils.isEmpty(subtitle))
            mSubTitle.setText(subtitle);
        else
            mSubTitle.setVisibility(GONE);
    }

    public static class ViewBehavior extends CoordinatorLayout.Behavior<ToolbarTitleSubTitle> {

        private Context mContext;

        private int mStartMarginLeft;
        private int mEndMargintLeft;
        private int mMarginRight;
        private int mStartMarginBottom;
        private boolean isHide;

        public ViewBehavior(Context context, AttributeSet attrs) {
            mContext = context;
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, ToolbarTitleSubTitle child, View dependency) {
            return dependency instanceof AppBarLayout;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, ToolbarTitleSubTitle child, View dependency) {
            initLayoutMargins();

            int maxScroll = ((AppBarLayout) dependency).getTotalScrollRange();
            float percentage = Math.abs(dependency.getY()) / (float) maxScroll;

            float childPosition = dependency.getHeight()
                    + dependency.getY()
                    - child.getHeight()
                    - (getToolbarHeight() - child.getHeight()) * percentage / 2;

            childPosition = childPosition - mStartMarginBottom * (1f - percentage);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.leftMargin = (int) (percentage * mEndMargintLeft) + mStartMarginLeft;
            lp.rightMargin = mMarginRight;
            child.setLayoutParams(lp);

            child.setY(childPosition);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                if (isHide && percentage < 1) {
                    child.setVisibility(View.VISIBLE);
                    isHide = false;
                } else if (!isHide && percentage == 1) {
                    child.setVisibility(View.GONE);
                    isHide = true;
                }
            }
            return true;
        }

        private void initLayoutMargins() {

            if (mStartMarginLeft == 0)
                mStartMarginLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.toolbar_title_subtitle_view_start_margin_left);

            if (mEndMargintLeft == 0)
                mEndMargintLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.toolbar_title_subtitle_end_margin_left);

            if (mStartMarginBottom == 0)
                mStartMarginBottom = mContext.getResources().getDimensionPixelOffset(R.dimen.toolbar_title_subtitle_start_margin_bottom);

            if (mMarginRight == 0)
                mMarginRight = mContext.getResources().getDimensionPixelOffset(R.dimen.toolbar_title_subtitle_end_margin_right);

        }


        public int getToolbarHeight() {
            int result = 0;
            TypedValue outValue = new TypedValue();
            if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, outValue, true)) {
                result = TypedValue.complexToDimensionPixelSize(outValue.data, mContext.getResources().getDisplayMetrics());
            }
            return result;
        }

    }
}
