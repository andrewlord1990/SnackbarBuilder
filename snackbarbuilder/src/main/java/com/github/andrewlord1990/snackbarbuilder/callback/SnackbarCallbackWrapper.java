/*
 * Copyright (C) 2016 Andrew Lord
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
import android.support.design.widget.Snackbar.Callback;

/**
 * A wrapper for the standard Callback, which you can inherit from.
 * This allows you to combine multiple callbacks to the Snackbar.
 */
public class SnackbarCallbackWrapper extends Callback {

    protected Callback callback;

    /**
     * Create the callback wrapper from a standard Callback.
     *
     * @param callback The callback to wrap.
     */
    public SnackbarCallbackWrapper(Callback callback) {
        this.callback = callback;
    }

    /**
     * Notifies that the {@link Snackbar} has been dismissed through some means.
     *
     * @param snackbar The Snackbar which has been dismissed.
     * @param event    The event which caused the dismissal.
     */
    @Override
    public void onDismissed(Snackbar snackbar, @DismissEvent int event) {
        if (callback != null) {
            callback.onDismissed(snackbar, event);
        }
    }

    /**
     * Notifies that the {@link Snackbar} has been shown (made visible).
     *
     * @param snackbar The Snackbar which has been shown.
     */
    @Override
    public void onShown(Snackbar snackbar) {
        if (callback != null) {
            callback.onShown(snackbar);
        }
    }
}
