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
 * - Altering text and background colours
 * - Easier-to-use callbacks
 * - Appending messages
 * - Adding an icon
 * - Providing defaults for all SnackbarBuilders through theme attributes
 * The builder can either output a constructed Snackbar or a SnackbarWrapper.
 * The SnackbarWrapper allows you to further customise the Snackbar after
 * creation and gives you more control than the Snackbar API.
 * Various default values can be loaded from a style specified within your
 * theme. These values can be overridden by the builder methods.
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

    /**
     * Create a builder to create a Snackbar. The Snackbar will be attached
     * to the specified parent view.
     *
     * @param view Parent view to attach the Snackbar to.
     */
    public SnackbarBuilder(View view) {
        parentView = view;
        context = parentView.getContext();
        loadThemeAttributes();
    }

    /**
     * Create a builder to create a Snackbar. The parent view to attach the
     * Snackbar to can be specified through the attribute
     * snackbarBuilder_parentViewId. This attribute is within a style than
     * can be provided through the theme attribute snackbarBuilderStyle.
     * The parent view will be found using this ID and the Snackbar will be
     * attached to it.
     *
     * @param activity Activity to show your Snackbar in, it should contain
     *                 a view with the ID specified in the style attribute
     *                 snackbarBuilder_parentViewId.
     */
    public SnackbarBuilder(Activity activity) {
        context = activity;
        loadThemeAttributes();
        parentView = activity.findViewById(parentViewId);
    }

    /**
     * Set the text to display on the {@link Snackbar}.
     *
     * @param message Text to display.
     * @return This instance.
     */
    public SnackbarBuilder message(CharSequence message) {
        this.message = message;
        return this;
    }

    /**
     * Set the text to display on the {@link Snackbar}.
     *
     * @param messageResId String resource of the text to display.
     * @return This instance.
     */
    public SnackbarBuilder message(@StringRes int messageResId) {
        this.message = context.getString(messageResId);
        return this;
    }

    /**
     * Set the colour to display the message on the {@link Snackbar}.
     *
     * @param messageTextColor Colour to display the message.
     * @return This instance.
     */
    public SnackbarBuilder messageTextColor(@ColorInt int messageTextColor) {
        this.messageTextColor = messageTextColor;
        return this;
    }

    /**
     * Set the colour to display the message on the {@link Snackbar}.
     *
     * @param messageTextColor Resource of the colour to display the message.
     * @return This instance.
     */
    public SnackbarBuilder messageTextColorRes(@ColorRes int messageTextColor) {
        this.messageTextColor = getColor(messageTextColor);
        return this;
    }

    /**
     * Add some text to append to the end of the message shown on the
     * {@link Snackbar}.
     *
     * @param message Text to append to the Snackbar message.
     * @return This instance.
     */
    public SnackbarBuilder appendMessage(CharSequence message) {
        initialiseAppendMessages();
        appendMessages.append(message);
        return this;
    }

    /**
     * Add some text to append to the message shown on the {@link Snackbar}.
     *
     * @param messageResId String resource of the text to append to
     *                     the Snackbar message.
     * @return This instance.
     */
    public SnackbarBuilder appendMessage(@StringRes int messageResId) {
        return appendMessage(context.getString(messageResId));
    }

    /**
     * Add some text to append to the message shown on the {@link Snackbar}
     * and a colour to make it.
     *
     * @param message Text to append to the Snackbar message.
     * @param color   Colour to make the appended text.
     * @return This instance.
     */
    public SnackbarBuilder appendMessage(CharSequence message, @ColorInt int color) {
        initialiseAppendMessages();
        Spannable spannable = new SpannableString(message);
        spannable.setSpan(new ForegroundColorSpan(color), 0, spannable.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        appendMessages.append(spannable);
        return this;
    }

    /**
     * Add some text to append to the message shown on the {@link Snackbar}
     * and a colour to make it.
     *
     * @param messageResId String resource of the text to append to the
     *                     Snackbar message.
     * @param colorResId   Resource of the colour to make the appended text.
     * @return This instance.
     */
    public SnackbarBuilder appendMessage(@StringRes int messageResId,
                                         @ColorRes int colorResId) {
        return appendMessage(context.getString(messageResId),
                getColor(colorResId));
    }

    /**
     * Set the duration to show the {@link Snackbar} for.
     *
     * @param duration The duration.
     * @return This instance.
     */
    public SnackbarBuilder duration(@Duration int duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Set the text to display as an action on the {@link Snackbar}.
     *
     * @param actionText The text to set as an action.
     * @return This instance.
     */
    public SnackbarBuilder actionText(CharSequence actionText) {
        this.actionText = actionText;
        return this;
    }

    /**
     * Set the text to display as an action on the {@link Snackbar}.
     *
     * @param actionTextResId The string resource of the text to set
     *                        as an action.
     * @return This instance.
     */
    public SnackbarBuilder actionText(@StringRes int actionTextResId) {
        this.actionText = context.getString(actionTextResId);
        return this;
    }

    /**
     * Set the colour to display the action on the {@link Snackbar}.
     *
     * @param actionTextColor Colour to display the action.
     * @return This instance.
     */
    public SnackbarBuilder actionTextColor(@ColorInt int actionTextColor) {
        this.actionTextColor = actionTextColor;
        return this;
    }

    /**
     * Set the u to display the action on the {@link Snackbar}.
     *
     * @param actionTextColorResId Resource of the colour to display the action.
     * @return This instance.
     */
    public SnackbarBuilder actionTextColorRes(@ColorRes int actionTextColorResId) {
        this.actionTextColor = getColor(actionTextColorResId);
        return this;
    }

    /**
     * Set the click listener for the action on the {@link Snackbar}.
     *
     * @param actionClickListener Click listener for the action.
     * @return This instance.
     */
    public SnackbarBuilder actionClickListener(OnClickListener actionClickListener) {
        this.actionClickListener = actionClickListener;
        return this;
    }

    /**
     * Set the colour to make the background of the {@link Snackbar}.
     *
     * @param backgroundColor The background colour.
     * @return This instance.
     */
    public SnackbarBuilder backgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Set the colour to make the background of the {@link Snackbar}.
     *
     * @param backgroundColorResId The resource of the background colour.
     * @return This instance.
     */
    public SnackbarBuilder backgroundColorRes(@ColorRes int backgroundColorResId) {
        this.backgroundColor = getColor(backgroundColorResId);
        return this;
    }

    /**
     * Set the standard callback for being informed of the {@link Snackbar}
     * being shown or dismissed.
     *
     * @param callback The callback.
     * @return This instance.
     */
    public SnackbarBuilder callback(Snackbar.Callback callback) {
        this.callback = callback;
        return this;
    }

    /**
     * Set the enhanced callback for being informed of the {@link Snackbar}
     * being shown or dismissed, through individual callback methods.
     *
     * @param snackbarCallback The callback.
     * @return This instance.
     */
    public SnackbarBuilder snackbarCallback(SnackbarCallback snackbarCallback) {
        this.snackbarCallback = snackbarCallback;
        return this;
    }

    /**
     * Make the action be displayed with the first letter uppercase and the
     * rest lowercase. It will be displayed all uppercase by default on API 14
     * and above.
     *
     * @return This instance.
     */
    public SnackbarBuilder lowercaseAction() {
        actionAllCaps = false;
        return this;
    }

    /**
     * Set an icon to display on the {@link Snackbar} next to the message.
     *
     * @param icon The drawable of the icon.
     * @return This instance.
     */
    public SnackbarBuilder icon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Set an icon to display on the {@link Snackbar} next to the message.
     *
     * @param iconResId The drawable resource of the icon.
     * @return This instance.
     */
    public SnackbarBuilder icon(@DrawableRes int iconResId) {
        icon = getDrawable(iconResId);
        return this;
    }

    /**
     * Set the margin to be displayed before the icon. On platform versions
     * that support bi-directional layouts, this will be the start margin,
     * on platforms before this it will just be the left margin.
     *
     * @param iconMarginStartPixels The margin before the icon.
     * @return This instance.
     */
    public SnackbarBuilder iconMarginStartPixels(int iconMarginStartPixels) {
        this.iconMarginStartPixels = iconMarginStartPixels;
        return this;
    }

    /**
     * Set the margin to be displayed before the icon. On platform versions
     * that support bi-directional layouts, this will be the start margin,
     * on platforms before this it will just be the left margin.
     *
     * @param iconMarginStart The dimension resource of the margin before
     *                        the icon.
     * @return This instance.
     */
    public SnackbarBuilder iconMarginStart(@DimenRes int iconMarginStart) {
        return iconMarginStartPixels(
                context.getResources().getDimensionPixelSize(iconMarginStart));
    }

    /**
     * Set the margin to be displayed after the icon in pixels. On platform
     * versions that support bi-directional layouts, this will be the end
     * margin, on platforms before this it will just be the right margin.
     *
     * @param iconMarginEndPixels The margin after the icon in pixels.
     * @return This instance.
     */
    public SnackbarBuilder iconMarginEndPixels(int iconMarginEndPixels) {
        this.iconMarginEndPixels = iconMarginEndPixels;
        return this;
    }

    /**
     * Set the margin to be displayed after the icon in pixels. On platform
     * versions that support bi-directional layouts, this will be the end
     * margin, on platforms before this it will just be the right margin.
     *
     * @param iconMarginEnd The margin after the icon in pixels.
     * @return This instance.
     */
    public SnackbarBuilder iconMarginEnd(@DimenRes int iconMarginEnd) {
        return iconMarginEndPixels(
                context.getResources().getDimensionPixelSize(iconMarginEnd));
    }

    /**
     * Build a Snackbar using the options specified in the builder. Wrap this
     * Snackbar into a SnackbarWrapper, which allows further customisation.
     *
     * @return A SnackbarWrapper, a class which wraps a Snackbar for further
     * customisation.
     */
    public SnackbarWrapper buildWrapper() {
        return new SnackbarWrapper(build());
    }

    /**
     * Build a Snackbar using the options specified in the builder.
     *
     * @return A Snackbar.
     */
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


