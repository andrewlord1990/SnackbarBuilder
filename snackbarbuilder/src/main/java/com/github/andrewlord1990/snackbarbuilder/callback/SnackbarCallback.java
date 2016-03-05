/*
 * Copyright (C) 2015 Andrew Lord
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.github.andrewlord1990.snackbarbuilder.callback;

import android.support.design.widget.Snackbar;
import android.util.Log;

/**
 * A callback for use with SnackbarBuilder that allows you to easily respond
 * to the events you are interested in by overriding the appropriate methods.
 * You know longer have to check the value of the dismiss event, as you do
 * with the standard Callback class.
 */
public class SnackbarCallback {

    private static final String TAG = SnackbarCallback.class.getSimpleName();

    /**
     * Indicates that the {@link Snackbar} was shown (made visible).
     *
     * @param snackbar The Snackbar.
     */
    public void onSnackbarShown(Snackbar snackbar) {
        Log.v(TAG, "onSnackbarShown");
    }

    /**
     * Indicates that the {@link Snackbar} was dismissed through any means.
     *
     * @param snackbar The Snackbar.
     */
    public void onSnackbarDismissed(Snackbar snackbar) {
        Log.v(TAG, "onSnackbarDismissed");
    }

    /**
     * Indicates that the {@link Snackbar} was dismissed due to the action
     * being pressed.
     *
     * @param snackbar The Snackbar.
     */
    public void onSnackbarActionPressed(Snackbar snackbar) {
        Log.v(TAG, "onSnackbarActionPressed");
    }

    /**
     * Indicates that the {@link Snackbar} was dismissed due to being
     * swiped away.
     *
     * @param snackbar The Snackbar.
     */
    public void onSnackbarSwiped(Snackbar snackbar) {
        Log.v(TAG, "onSnackbarSwiped");
    }

    /**
     * Indicates that the {@link Snackbar} was dismissed due to a timeout,
     * specified by the duration set.
     *
     * @param snackbar The Snackbar.
     */
    public void onSnackbarTimedOut(Snackbar snackbar) {
        Log.v(TAG, "onSnackbarTimedOut");
    }

    /**
     * Indicates that the {@link Snackbar} was dismissed manually via
     * a call to dismiss().
     *
     * @param snackbar The Snackbar.
     */
    public void onSnackbarManuallyDismissed(Snackbar snackbar) {
        Log.v(TAG, "onSnackbarManuallyDismissed");
    }

    /**
     * Indicates that the {@link Snackbar} was dismissed due to another
     * Snackbar being shown.
     *
     * @param snackbar The Snackbar.
     */
    public void onSnackbarDismissedAfterAnotherShown(Snackbar snackbar) {
        Log.v(TAG, "onSnackbarDismissedAfterAnotherShown");
    }

}
