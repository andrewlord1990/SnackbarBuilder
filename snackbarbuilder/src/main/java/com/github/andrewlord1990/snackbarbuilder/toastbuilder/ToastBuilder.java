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

package com.github.andrewlord1990.snackbarbuilder.toastbuilder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar.Duration;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.andrewlord1990.snackbarbuilder.R;

/**
 * A builder pattern to easily create Toasts. On top of the options provided through the platform
 * API, you can also change the text colour to display the message.
 */
public class ToastBuilder {

    Context context;
    CharSequence message;
    View customView;
    int customViewMessageViewId = android.R.id.message;
    int messageTextColor;

    @Duration
    int duration = Toast.LENGTH_LONG;

    int gravity;
    int gravityOffsetX;
    int gravityOffsetY;

    /**
     * Create a builder to setup and then create a {@link Toast}.
     *
     * @param context The context to use for creating the Toast.
     */
    public ToastBuilder(Context context) {
        this.context = context;
        loadThemeAttributes();
    }

    /**
     * Set a custom view to apply to the {@link Toast} message.
     *
     * @param customView The custom view.
     * @return This instance.
     */
    public ToastBuilder customView(View customView) {
        this.customView = customView;
        return this;
    }

    /**
     * Set the ID of a TextView within the custom view to set the message on.
     *
     * @param customViewMessageViewId The ID of the message TextView within the custom view.
     * @return This instance.
     */
    public ToastBuilder customViewMessageViewId(int customViewMessageViewId) {
        this.customViewMessageViewId = customViewMessageViewId;
        return this;
    }

    /**
     * Set the message to display on the {@link Toast}.
     *
     * @param message The text to display.
     * @return This instance.
     */
    public ToastBuilder message(CharSequence message) {
        this.message = message;
        return this;
    }

    /**
     * Set the message to display on the {@link Toast}.
     *
     * @param messageResId The string resource of the text to display.
     * @return This instance.
     */
    public ToastBuilder message(@StringRes int messageResId) {
        this.message = context.getString(messageResId);
        return this;
    }

    /**
     * Set the color to display the message on the {@link Toast}.
     *
     * @param messageTextColor The color to display the message.
     * @return This instance.
     */
    public ToastBuilder messageTextColor(@ColorInt int messageTextColor) {
        this.messageTextColor = messageTextColor;
        return this;
    }

    /**
     * Set the color to display the message on the {@link Toast}.
     *
     * @param messageTextColorResId The resource of the color to display the message.
     * @return This instance.
     */
    public ToastBuilder messageTextColorRes(@ColorRes int messageTextColorResId) {
        this.messageTextColor = getColor(messageTextColorResId);
        return this;
    }

    /**
     * Set the duration to display the {@link Toast} for.
     *
     * @param duration Duration which can be either LENGTH_SHORT or LENGTH_LONG.
     * @return This instance.
     */
    public ToastBuilder duration(@Duration int duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Set the location the {@link Toast} appears on the screen. The gravity specifies which edge
     * of the screen it is attached to.
     *
     * @param gravity The location to display the Toast.
     * @return This instance.
     */
    public ToastBuilder gravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    /**
     * Set the horizontal offset in pixels to apply to the {@link Toast} location.
     *
     * @param gravityOffsetX The horizontal offset in pixels.
     * @return This instance.
     */
    public ToastBuilder gravityOffsetX(int gravityOffsetX) {
        this.gravityOffsetX = gravityOffsetX;
        return this;
    }

    /**
     * Set the vertical offset in pixels to apply to the {@link Toast} location.
     *
     * @param gravityOffsetY The vertical offset in pixels.
     * @return This instance.
     */
    public ToastBuilder gravityOffsetY(int gravityOffsetY) {
        this.gravityOffsetY = gravityOffsetY;
        return this;
    }

    /**
     * Build a {@link Toast} using the options specified in the builder.
     *
     * @return The constructed Toast.
     */
    @SuppressLint("ShowToast")
    public Toast build() {
        Toast toast = Toast.makeText(context, message, duration);

        TextView toastMessage = setupToastView(toast);
        setToastMessageTextColor(toastMessage);
        setToastGravity(toast);

        return toast;
    }

    private TextView setupToastView(Toast toast) {
        if (customView != null) {
            toast.setView(customView);

            if (message != null) {
                TextView messageView = (TextView) customView.findViewById(customViewMessageViewId);
                if (messageView != null) {
                    messageView.setText(message);
                    return messageView;
                }
            }
        }
        return getToastMessageView(toast.getView());
    }

    private void setToastMessageTextColor(TextView toastMessage) {
        if (messageTextColor != 0) {
            toastMessage.setTextColor(messageTextColor);
        }
    }

    private void setToastGravity(Toast toast) {
        if (gravity != 0) {
            toast.setGravity(gravity, gravityOffsetX, gravityOffsetY);
        }
    }

    private TextView getToastMessageView(View toastView) {
        return (TextView) toastView.findViewById(android.R.id.message);
    }

    private int getColor(@ColorRes int color) {
        return ContextCompat.getColor(context, color);
    }

    private void loadThemeAttributes() {
        TypedArray attrs = context.obtainStyledAttributes(
                null, R.styleable.ToastBuilderStyle, R.attr.toastBuilderStyle, 0);
        try {
            messageTextColor = attrs.getColor(
                    R.styleable.ToastBuilderStyle_toastBuilder_messageTextColor, 0);
            int durationAttr = attrs.getInteger(
                    R.styleable.ToastBuilderStyle_toastBuilder_duration, Integer.MIN_VALUE);
            if (durationAttr > Integer.MIN_VALUE) {
                duration = durationAttr;
            }
        } finally {
            attrs.recycle();
        }
    }

}
