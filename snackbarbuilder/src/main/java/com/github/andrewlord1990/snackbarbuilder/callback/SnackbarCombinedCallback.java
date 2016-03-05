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

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;

/**
 * Used to combine an enhanced SnackbarCallback and a standard Callback.
 * This allows both to be notified of any Snackbar events. This is used
 * internally by SnackbarBuilder, but is also available for use outside.
 */
public class SnackbarCombinedCallback extends SnackbarCallbackWrapper {

    protected SnackbarCallback snackbarCallback;

    /**
     * Create from a standard Callback.
     *
     * @param callback The callback to contain within.
     */
    public SnackbarCombinedCallback(Callback callback) {
        super(callback);
    }

    /**
     * Create from an enhanced SnackbarCallback.
     *
     * @param snackbarCallback The callback to contain within.
     */
    public SnackbarCombinedCallback(SnackbarCallback snackbarCallback) {
        this(snackbarCallback, null);
    }

    /**
     * Create by combining an enhanced SnackbarCallback and a
     * standard Callback.
     *
     * @param snackbarCallback The enhanced SnackbarCallback (can be null).
     * @param callback         The standard Callback (can be null).
     */
    public SnackbarCombinedCallback(@Nullable SnackbarCallback snackbarCallback,
                                    @Nullable Callback callback) {
        super(callback);

        this.snackbarCallback = snackbarCallback;
    }

    /**
     * Notifies that the {@link Snackbar} has been dismissed through some
     * event, for example swiping or the action being pressed.
     *
     * @param snackbar The Snackbar which has been dismissed.
     * @param event    The event which caused the dismissal.
     */
    @Override
    public void onDismissed(Snackbar snackbar, @DismissEvent int event) {
        super.onDismissed(snackbar, event);

        if (snackbarCallback != null) {
            switch (event) {
                case Callback.DISMISS_EVENT_SWIPE:
                    snackbarCallback.onSnackbarSwiped(snackbar);
                    break;
                case Callback.DISMISS_EVENT_ACTION:
                    snackbarCallback.onSnackbarActionPressed(snackbar);
                    break;
                case Callback.DISMISS_EVENT_TIMEOUT:
                    snackbarCallback.onSnackbarTimedOut(snackbar);
                    break;
                case Callback.DISMISS_EVENT_MANUAL:
                    snackbarCallback.onSnackbarManuallyDismissed(snackbar);
                    break;
                case Callback.DISMISS_EVENT_CONSECUTIVE:
                    snackbarCallback.onSnackbarDismissedAfterAnotherShown(snackbar);
                    break;
            }
            snackbarCallback.onSnackbarDismissed(snackbar);
        }
    }

    /**
     * Notifies that the {@link Snackbar} has been shown (made visible).
     *
     * @param snackbar The Snackbar which has been shown.
     */
    @Override
    public void onShown(Snackbar snackbar) {
        super.onShown(snackbar);

        if (snackbarCallback != null) {
            snackbarCallback.onSnackbarShown(snackbar);
        }
    }
}
