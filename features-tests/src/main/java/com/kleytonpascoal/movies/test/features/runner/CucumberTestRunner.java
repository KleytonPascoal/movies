package com.kleytonpascoal.movies.test.features.runner;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;

import cucumber.api.android.CucumberInstrumentationCore;

/**
 * Created by kleyton on 18/05/17.
 */

public class CucumberTestRunner  extends AndroidJUnitRunner {

    private CucumberInstrumentationCore instrumentationCore = new CucumberInstrumentationCore(this);

    @Override
    public void onCreate(Bundle arguments) {
        instrumentationCore.create(arguments);
        super.onCreate(arguments);
    }

    @Override
    public void onStart() {
        waitForIdleSync();
        instrumentationCore.start();
        super.onStart();
    }
}
