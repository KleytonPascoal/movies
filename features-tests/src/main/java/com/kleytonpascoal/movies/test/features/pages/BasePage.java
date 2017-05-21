package com.kleytonpascoal.movies.test.features.pages;

/**
 * Created by kleyton on 18/05/17.
 */

public class BasePage {

    public <T extends BasePage> T is(Class<T> type) {
        if (type.isInstance(this)) {
            return type.cast(this);
        } else {
            //HelperSteps.takeScreenshot(SCREENSHOT_TAG);
            throw new InvalidPageException("Invalid page type. Expected: " + type.getSimpleName() + ", but got: " + this.getClass().getSimpleName());
        }
    }

    public static class InvalidPageException extends RuntimeException {

        public InvalidPageException(final String message) {
            super(message);
        }
    }

}
