package com.kleytonpascoal.movies.test.util;

import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers.Visibility;
import android.support.test.espresso.util.HumanReadables;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.AnyOf.anyOf;

/**
 * Created by kleyton on 13/05/17.
 */

public class ViewActionsUtil {

    public static ViewAction scrollNestedScrollView(boolean hasBehaviour) {
        return ViewActions.actionWithAssertions(new NestedScrollToAction(hasBehaviour));
    }

    public static ViewAction swipeRightOnTop() {
        return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.TOP_LEFT,
                GeneralLocation.TOP_RIGHT, Press.FINGER);
    }

    public static ViewAction swipeLeftOnTop() {
        return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.TOP_RIGHT,
                GeneralLocation.TOP_LEFT, Press.FINGER);
    }

    private static class NestedScrollToAction implements ViewAction {

        private final String TAG = NestedScrollToAction.class.getSimpleName();
        private final boolean mHasBehaviour;

        public NestedScrollToAction(boolean hasBehaviour) {
            mHasBehaviour = hasBehaviour;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Matcher<View> getConstraints() {
            return allOf(withEffectiveVisibility(Visibility.VISIBLE), isDescendantOfA(anyOf(
                    isAssignableFrom(NestedScrollView.class))));
        }

        @Override
        public void perform(UiController uiController, View view) {
            if (isDisplayingAtLeast(90).matches(view)) {
                Log.i(TAG, "View is already displayed. Returning.");
                return;
            }

            View parentScrollView = findScrollView(view);

            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parentScrollView.getLayoutParams();
            if (!mHasBehaviour)
                params.setBehavior(null);
            parentScrollView.requestLayout();

            uiController.loopMainThreadUntilIdle();

            Rect rect = new Rect();
            view.getDrawingRect(rect);
            if (!view.requestRectangleOnScreen(rect, true /* immediate */)) {
                Log.w(TAG, "Scrolling to view was requested, but none of the parents scrolled.");
            }

            uiController.loopMainThreadUntilIdle();

            if (!isDisplayingAtLeast(90).matches(view)) {
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new RuntimeException(
                                "Scrolling to view was attempted, but the view is not displayed"))
                        .build();
            }
        }

        private View findScrollView(View view) {
            View parent = (View) view.getParent();
            if (parent != null) {
                if (parent instanceof NestedScrollView) {
                    return parent;
                }
                return findScrollView(parent);
            }
            throw new PerformException.Builder()
                    .withActionDescription(this.getDescription())
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(new RuntimeException(
                            "Scrolling aborted due to not being NestedScrollView child"))
                    .build();
        }

        @Override
        public String getDescription() {
            return "scroll to";
        }
    }
}
