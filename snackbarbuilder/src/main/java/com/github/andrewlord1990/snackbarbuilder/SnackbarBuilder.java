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

package com.github.andrewlord1990.snackbarbuilder;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Duration;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;

import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCallback;

/**
 * A builder pattern to easily create and customise Android Design Support
 * library Snackbars.
 * On top of the customisations you can make through the Snackbar API,
 * there are any additional ones. These include:
 * - Altering text and background colors
 * - Easier-to-use callbacks
 * - Appending messages
 * - Adding an icon
 * - Providing defaults for all SnackbarBuilders through theme attributes
 * The builder can either output a constructed Snackbar or a SnackbarWrapper.
 * The SnackbarWrapper allows you to further customise the Snackbar after
 * creation and gives you more control than the Snackbar API.
 */
public final class SnackbarBuilder {

    Context context;
    View parentView;
    SpannableStringBuilder appendMessages;
    CharSequence message;

    @Duration
    int duration = Snackbar.LENGTH_LONG;

    CharSequence actionText;
    OnClickListener actionClickListener;
    Snackbar.Callback callback;
    SnackbarCallback snackbarCallback;
    boolean actionAllCaps = true;
    int backgroundColor;
    int actionTextColor;
    int messageTextColor;
    int parentViewId;
    Drawable icon;
    int iconMarginStartPixels;
    int iconMarginEndPixels;

    public SnackbarBuilder(View view) {
        parentView = view;
        context = parentView.getContext();
        loadThemeAttributes();
    }

    public SnackbarBuilder(Activity activity) {
        context = activity;
        loadThemeAttributes();
        parentView = activity.findViewById(parentViewId);
    }

    public SnackbarBuilder message(CharSequence message) {
        this.message = message;
        return this;
    }

    public SnackbarBuilder message(@StringRes int messageResId) {
        this.message = context.getString(messageResId);
        return this;
    }

    public SnackbarBuilder messageTextColorRes(@ColorRes int messageTextColor) {
        this.messageTextColor = getColor(messageTextColor);
        return this;
    }

    public SnackbarBuilder messageTextColor(int messageTextColor) {
        this.messageTextColor = messageTextColor;
        return this;
    }

    public SnackbarBuilder appendMessage(CharSequence message) {
        initialiseAppendMessages();
        appendMessages.append(message);
        return this;
    }

    public SnackbarBuilder appendMessage(@StringRes int messageResId) {
        return appendMessage(context.getString(messageResId));
    }

    public SnackbarBuilder appendMessage(CharSequence message, @ColorInt int color) {
        initialiseAppendMessages();
        Spannable spannable = new SpannableString(message);
        spannable.setSpan(new ForegroundColorSpan(color), 0, spannable.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        appendMessages.append(spannable);
        return this;
    }

    public SnackbarBuilder appendMessage(@StringRes int messageResId,
                                         @ColorRes int colorResId) {
        return appendMessage(context.getString(messageResId),
                getColor(colorResId));
    }

    public SnackbarBuilder duration(@Duration int duration) {
        this.duration = duration;
        return this;
    }

    public SnackbarBuilder actionTextColorRes(@ColorRes int actionTextColor) {
        this.actionTextColor = getColor(actionTextColor);
        return this;
    }

    public SnackbarBuilder actionTextColor(int actionTextColor) {
        this.actionTextColor = actionTextColor;
        return this;
    }

    public SnackbarBuilder actionText(@StringRes int actionText) {
        this.actionText = context.getString(actionText);
        return this;
    }

    public SnackbarBuilder actionText(CharSequence actionText) {
        this.actionText = actionText;
        return this;
    }

    public SnackbarBuilder actionClickListener(OnClickListener actionClickListener) {
        this.actionClickListener = actionClickListener;
        return this;
    }

    public SnackbarBuilder backgroundColorRes(@ColorRes int backgroundColor) {
        this.backgroundColor = getColor(backgroundColor);
        return this;
    }

    public SnackbarBuilder backgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public SnackbarBuilder callback(Snackbar.Callback callback) {
        this.callback = callback;
        return this;
    }

    public SnackbarBuilder snackbarCallback(SnackbarCallback snackbarCallback) {
        this.snackbarCallback = snackbarCallback;
        return this;
    }

    public SnackbarBuilder lowercaseAction() {
        actionAllCaps = false;
        return this;
    }

    public SnackbarBuilder icon(@DrawableRes int iconResId) {
        icon = getDrawable(iconResId);
        return this;
    }

    public SnackbarBuilder icon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public SnackbarBuilder iconMarginStartPixels(int iconMarginStartPixels) {
        this.iconMarginStartPixels = iconMarginStartPixels;
        return this;
    }

    public SnackbarBuilder iconMarginStart(@DimenRes int iconMarginStart) {
        return iconMarginStartPixels(
                context.getResources().getDimensionPixelSize(iconMarginStart));
    }

    public SnackbarBuilder iconMarginEndPixels(int iconMarginEndPixels) {
        this.iconMarginEndPixels = iconMarginEndPixels;
        return this;
    }

    public SnackbarBuilder iconMarginEnd(@DimenRes int iconMarginEnd) {
        return iconMarginEndPixels(
                context.getResources().getDimensionPixelSize(iconMarginEnd));
    }

    public SnackbarWrapper buildWrapper() {
        return new SnackbarWrapper(build());
    }

    public Snackbar build() {
        Snackbar snackbar = Snackbar.make(parentView, message, duration);

        new SnackbarCustomiser(snackbar)
                .customiseMessage(messageTextColor, appendMessages)
                .setBackgroundColor(backgroundColor)
                .setAction(actionText, actionClickListener)
                .setActionTextColor(actionTextColor)
                .setActionAllCaps(actionAllCaps)
                .setCallbacks(snackbarCallback, callback)
                .setIcon(icon, iconMarginStartPixels, iconMarginEndPixels);

        return snackbar;
    }

    private void initialiseAppendMessages() {
        if (appendMessages == null) {
            appendMessages = new SpannableStringBuilder();
        }
    }

    private void loadThemeAttributes() {
        TypedArray attrs = context.obtainStyledAttributes(
                null, R.styleable.SnackbarBuilderStyle, R.attr.snackbarBuilderStyle, 0);
        try {
            loadMessageTextColor(attrs);
            loadActionTextColor(attrs);
            loadParentViewId(attrs);
            loadDuration(attrs);
            loadBackgroundColor(attrs);

            loadFallbackAttributes(attrs);
        } finally {
            attrs.recycle();
        }
    }

    private void loadBackgroundColor(TypedArray attrs) {
        backgroundColor = attrs.getColor(
                R.styleable.SnackbarBuilderStyle_snackbarBuilder_backgroundColor, 0);
    }

    private void loadDuration(TypedArray attrs) {
        int durationAttr = attrs.getInteger(
                R.styleable.SnackbarBuilderStyle_snackbarBuilder_duration, Integer.MIN_VALUE);
        if (durationAttr > Integer.MIN_VALUE) {
            duration = durationAttr;
        }
    }

    private void loadParentViewId(TypedArray attrs) {
        parentViewId = attrs.getResourceId(
                R.styleable.SnackbarBuilderStyle_snackbarBuilder_parentViewId, 0);
    }

    private void loadActionTextColor(TypedArray attrs) {
        actionTextColor = attrs.getColor(
                R.styleable.SnackbarBuilderStyle_snackbarBuilder_actionTextColor, 0);
    }

    private void loadMessageTextColor(TypedArray attrs) {
        messageTextColor = attrs.getColor(
                R.styleable.SnackbarBuilderStyle_snackbarBuilder_messageTextColor, 0);
    }

    private void loadFallbackAttributes(TypedArray attrs) {
        if (messageTextColor == 0) {
            messageTextColor = getColor(R.color.snackbarbuilder_default_message);
        }
        if (actionTextColor == 0) {
            actionTextColor = attrs.getColor(R.styleable.SnackbarBuilderStyle_colorAccent, 0);
        }
        if (iconMarginStartPixels == 0) {
            iconMarginStartPixels = context.getResources()
                    .getDimensionPixelSize(R.dimen.snackbarbuilder_icon_margin_start_default);
        }
        if (iconMarginEndPixels == 0) {
            iconMarginEndPixels = context.getResources()
                    .getDimensionPixelSize(R.dimen.snackbarbuilder_icon_margin_end_default);
        }
    }

    private int getColor(@ColorRes int color) {
        return ContextCompat.getColor(context, color);
    }

    private Drawable getDrawable(@DrawableRes int drawableResId) {
        return ContextCompat.getDrawable(context, drawableResId);
    }

}


