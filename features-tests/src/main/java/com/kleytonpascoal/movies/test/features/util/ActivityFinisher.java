package com.kleytonpascoal.movies.test.features.util;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kleyton on 14/05/17.
 */

public class ActivityFinisher implements Runnable {

    public static void finishOpenActivities() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new ActivityFinisher());
    }

    private final ActivityLifecycleMonitor activityLifecycleMonitor;

    private ActivityFinisher() {
        this.activityLifecycleMonitor = ActivityLifecycleMonitorRegistry.getInstance();
    }

    @Override
    public void run() {
        final List<Activity> activities = new ArrayList<>();

        Stage[] stages = { Stage.CREATED, Stage.STOPPED, Stage.PAUSED, Stage.STARTED,
                Stage.RESUMED, Stage.RESTARTED, Stage.PRE_ON_CREATE };

        for (final Stage stage : stages) {
            activities.addAll(activityLifecycleMonitor.getActivitiesInStage(stage));
        }

        for (final Activity activity : activities) {
            InstrumentationRegistry.getInstrumentation().callActivityOnStop(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }


    }
}
