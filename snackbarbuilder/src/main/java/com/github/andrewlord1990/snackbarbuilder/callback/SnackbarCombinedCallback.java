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
 * Used to combine an enhanced SnackbarCallback and a standard Callback. This allows both to be
 * notified of any Snackbar events. This is used internally by SnackbarBuilder, but is also
 * available for use outside.
 */
public class SnackbarCombinedCallback extends SnackbarCallbackWrapper {

    protected SnackbarCallback snackbarCallback;

    private SnackbarShowCallback showCallback;
    private SnackbarDismissCallback dismissCallback;
    private SnackbarActionDismissCallback actionDismissCallback;
    private SnackbarSwipeDismissCallback swipeDismissCallback;
    private SnackbarTimeoutDismissCallback timeoutDismissCallback;
    private SnackbarManualDismissCallback manualDismissCallback;
    private SnackbarConsecutiveDismissCallback consecutiveDismissCallback;

    /**
     * Create from a standard Callback.
     *
     * @param callback The callback to contain within.
     */
    public SnackbarCombinedCallback(Callback callback) {
        this(null, callback);
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
     * Create by combining an enhanced SnackbarCallback and a standard Callback.
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
     * Notifies that the {@link Snackbar} has been dismissed through some event, for example
     * swiping or the action being pressed.
     *
     * @param snackbar The Snackbar which has been dismissed.
     * @param event    The event which caused the dismissal.
     */
    @Override
    public void onDismissed(Snackbar snackbar, @DismissEvent int event) {
        super.onDismissed(snackbar, event);

        notifySnackbarCallback(snackbar, event);
        notifySeparateCallbacks(snackbar, event);
    }

    private void notifySnackbarCallback(Snackbar snackbar, @DismissEvent int event) {
        if (snackbarCallback != null) {
            switch (event) {
                case Callback.DISMISS_EVENT_ACTION:
                    snackbarCallback.onSnackbarActionPressed(snackbar);
                    break;
                case Callback.DISMISS_EVENT_SWIPE:
                    snackbarCallback.onSnackbarSwiped(snackbar);
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

    private void notifySeparateCallbacks(Snackbar snackbar, @DismissEvent int event) {
        switch (event) {
            case Callback.DISMISS_EVENT_ACTION:
                if (actionDismissCallback != null) {
                    actionDismissCallback.onSnackbarActionPressed(snackbar);
                }
                break;
            case Callback.DISMISS_EVENT_SWIPE:
                if (swipeDismissCallback != null) {
                    swipeDismissCallback.onSnackbarSwiped(snackbar);
                }
                break;
            case Callback.DISMISS_EVENT_TIMEOUT:
                if (timeoutDismissCallback != null) {
                    timeoutDismissCallback.onSnackbarTimedOut(snackbar);
                }
                break;
            case Callback.DISMISS_EVENT_MANUAL:
                if (manualDismissCallback != null) {
                    manualDismissCallback.onSnackbarManuallyDismissed(snackbar);
                }
                break;
            case Callback.DISMISS_EVENT_CONSECUTIVE:
                if (consecutiveDismissCallback != null) {
                    consecutiveDismissCallback.onSnackbarDismissedAfterAnotherShown(snackbar);
                }
                break;
        }
        if (dismissCallback != null) {
            dismissCallback.onSnackbarDismissed(snackbar, event);
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

        if (showCallback != null) {
            showCallback.onSnackbarShown(snackbar);
        }
    }

    /**
     * Create a builder.
     *
     * @return A builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder pattern for combined callbacks, this allows you to specify the ones you want
     * without required many different constructors.
     */
    public static class Builder {

        Callback callback;
        SnackbarCallback snackbarCallback;
        SnackbarShowCallback showCallback;
        SnackbarDismissCallback dismissCallback;
        SnackbarActionDismissCallback actionDismissCallback;
        SnackbarSwipeDismissCallback swipeDismissCallback;
        SnackbarTimeoutDismissCallback timeoutDismissCallback;
        SnackbarManualDismissCallback manualDismissCallback;
        SnackbarConsecutiveDismissCallback consecutiveDismissCallback;

        /**
         * Set the standard callback for being informed of the {@link Snackbar} being shown or
         * dismissed.
         *
         * @param callback The callback.
         * @return This instance.
         */
        public Builder callback(Callback callback) {
            this.callback = callback;
            return this;
        }

        /**
         * Set the enhanced callback for being informed of the {@link Snackbar} being shown or
         * dismissed, through individual callback methods.
         *
         * @param snackbarCallback The callback.
         * @return This instance.
         */
        public Builder snackbarCallback(SnackbarCallback snackbarCallback) {
            this.snackbarCallback = snackbarCallback;
            return this;
        }

        /**
         * Set the callback to be informed of the {@link Snackbar} being shown.
         *
         * @param callback The callback.
         * @return This instance.
         */
        public Builder showCallback(SnackbarShowCallback callback) {
            showCallback = callback;
            return this;
        }

        /**
         * Set the callback to be informed of the {@link Snackbar} being dismissed through some means.
         *
         * @param callback The callback.
         * @return This instance.
         */
        public Builder dismissCallback(SnackbarDismissCallback callback) {
            dismissCallback = callback;
            return this;
        }

        /**
         * Set the callback to be informed of the {@link Snackbar} being dismissed due to the action
         * being pressed.
         *
         * @param callback The callback.
         * @return This instance.
         */
        public Builder actionDismissCallback(SnackbarActionDismissCallback callback) {
            actionDismissCallback = callback;
            return this;
        }

        /**
         * Set the callback to be informed of the {@link Snackbar} being dismissed due to being
         * swiped away.
         *
         * @param callback The callback.
         * @return This instance.
         */
        public Builder swipeDismissCallback(SnackbarSwipeDismissCallback callback) {
            swipeDismissCallback = callback;
            return this;
        }

        /**
         * Set the callback to be informed of the {@link Snackbar} being dismissed due to a timeout.
         *
         * @param callback The callback.
         * @return This instance.
         */
        public Builder timeoutDismissCallback(SnackbarTimeoutDismissCallback callback) {
            timeoutDismissCallback = callback;
            return this;
        }

        /**
         * Set the callback to be informed of the {@link Snackbar} being dismissed manually, due to a
         * call to dismiss().
         *
         * @param callback The callback.
         * @return This instance.
         */
        public Builder manualDismissCallback(SnackbarManualDismissCallback callback) {
            manualDismissCallback = callback;
            return this;
        }

        /**
         * Set the callback to be informed of the {@link Snackbar} being dismissed due to another
         * Snackbar being shown.
         *
         * @param callback The callback.
         * @return This instance.
         */
        public Builder consecutiveDismissCallback(
                SnackbarConsecutiveDismissCallback callback) {
            consecutiveDismissCallback = callback;
            return this;
        }

        /**
         * Create a combined callback from all the provided callbacks.
         *
         * @return A combined callback.
         */
        public SnackbarCombinedCallback build() {
            SnackbarCombinedCallback combinedCallback = new SnackbarCombinedCallback(
                    snackbarCallback, callback);
            combinedCallback.showCallback = showCallback;
            combinedCallback.dismissCallback = dismissCallback;
            combinedCallback.actionDismissCallback = actionDismissCallback;
            combinedCallback.swipeDismissCallback = swipeDismissCallback;
            combinedCallback.timeoutDismissCallback = timeoutDismissCallback;
            combinedCallback.manualDismissCallback = manualDismissCallback;
            combinedCallback.consecutiveDismissCallback = consecutiveDismissCallback;
            return combinedCallback;
        }

    }
}
