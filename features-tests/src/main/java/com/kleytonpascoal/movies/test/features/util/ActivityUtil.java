package com.kleytonpascoal.movies.test.features.util;

import android.app.Activity;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.util.Log;

import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by kleyton on 19/05/17.
 */

public class ActivityUtil {

    public static Activity getActivityInstance(){
        final Activity[] currentActivity = new Activity[1];
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                for (Activity act: resumedActivities){
                    Log.d("Current activity: ", act.getClass().getName());
                    currentActivity[0] = act;
                    break;
                }
            }
        });

        return currentActivity[0];
    }
}
