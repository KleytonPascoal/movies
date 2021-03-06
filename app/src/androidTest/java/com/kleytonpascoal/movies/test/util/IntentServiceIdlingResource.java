package com.kleytonpascoal.movies.test.util;

import android.app.ActivityManager;
import android.content.Context;
import android.support.test.espresso.IdlingResource;
import android.util.Log;

import com.kleytonpascoal.movies.service.MoviePersistenceService;

/**
 * Created by kleyton on 14/05/17.
 */

public class IntentServiceIdlingResource implements IdlingResource {

    private final String TAG = IntentServiceIdlingResource.class.getName();

    private final Context context;
    private ResourceCallback resourceCallback;

    public IntentServiceIdlingResource(Context context) {
        this.context = context;
    }

    @Override
    public String getName() {
        return IntentServiceIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = !isIntentServiceRunning();
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(IdlingResource.ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    private boolean isIntentServiceRunning() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo info : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.d(TAG, info.service.getClassName());
            if (MoviePersistenceService.class.getName().equals(info.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
