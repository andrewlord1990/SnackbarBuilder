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

package com.github.andrewlord1990.snackbarbuilder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;
import android.support.design.widget.Snackbar.Duration;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCombinedCallback;

/**
 * SnackbarWrapper is an extension to the {@link Snackbar} available within the Android Design
 * Support library.
 * By wrapping a {@link Snackbar} within it, it provides many customisations that are not
 * available without it. The setter methods also return the {@link SnackbarWrapper} instance,
 * allowing you to chain the calls together fluently.
 * In order to create a {@link SnackbarWrapper} there is the SnackbarBuilder.buildWrapper()
 * method. Alternatively, if you already have a {@link Snackbar} instance, you can just create
 * your own {@link SnackbarWrapper} and pass the {@link Snackbar} into it.
 */
public final class SnackbarWrapper {

    Context context;

    private Snackbar snackbar;
    private TextView messageView;
    private Button actionView;

    private Callback callback;
    private SnackbarIconBuilder iconBuilder;

    /**
     * Create by wrapping a {@link Snackbar}.
     *
     * @param snackbar The {@link Snackbar} to wrap.
     */
    public SnackbarWrapper(Snackbar snackbar) {
        this.snackbar = snackbar;
        messageView = (TextView) getView().findViewById(R.id.snackbar_text);
        actionView = (Button) getView().findViewById(R.id.snackbar_action);
        iconBuilder = SnackbarIconBuilder.builder(snackbar);
        context = snackbar.getView().getContext();
    }

    /**
     * Get the {@link Snackbar} that has been wrapped.
     *
     * @return The {@link Snackbar}.
     */
    public Snackbar getSnackbar() {
        return snackbar;
    }

    /**
     * Set the action to be displayed in the {@link Snackbar}.
     *
     * @param actionTextResId String resource of the text to display as an action.
     * @return This instance.
     */
    public SnackbarWrapper setActionText(@StringRes int actionTextResId) {
        actionView.setText(actionTextResId);
        return this;
    }

    /**
     * Set the action to be displayed in the {@link Snackbar}.
     *
     * @param actionText Text to display as an action.
     * @return This instance.
     */
    public SnackbarWrapper setActionText(CharSequence actionText) {
        actionView.setText(actionText);
        return this;
    }

    /**
     * Get the action displayed in the {@link Snackbar}.
     *
     * @return Text displayed as an action.
     */
    public CharSequence getActionText() {
        return actionView.getText();
    }

    /**
     * Set the callback to be invoked when the action is clicked.
     *
     * @param actionClickListener Callback to be invoked when the action is clicked.
     * @return This instance.
     */
    public SnackbarWrapper setActionClickListener(OnClickListener actionClickListener) {
        actionView.setOnClickListener(actionClickListener);
        return this;
    }

    /**
     * Set the action to be displayed in the {@link Snackbar} and the callback to invoke when the
     * action is clicked.
     *
     * @param actionTextResId     String resource to display as an action.
     * @param actionClickListener Callback to be invoked when the action is clicked.
     * @return This instance.
     */
    @NonNull
    public SnackbarWrapper setAction(@StringRes int actionTextResId, OnClickListener actionClickListener) {
        snackbar.setAction(actionTextResId, actionClickListener);
        return this;
    }

    /**
     * Set the action to be displayed in the {@link Snackbar} and the callback to invoke when the
     * action is clicked.
     *
     * @param actionText          Text to display as an action.
     * @param actionClickListener Callback to be invoked when the action is clicked.
     * @return This instance.
     */
    @NonNull
    public SnackbarWrapper setAction(CharSequence actionText, OnClickListener actionClickListener) {
        snackbar.setAction(actionText, actionClickListener);
        return this;
    }

    /**
     * Set the text color for the action on the {@link Snackbar}.
     *
     * @param colors Colors state list to apply to the action text.
     * @return This instance.
     */
    @NonNull
    public SnackbarWrapper setActionTextColor(ColorStateList colors) {
        snackbar.setActionTextColor(colors);
        return this;
    }

    /**
     * Set the text color for the action on the {@link Snackbar}.
     *
     * @param color Color to make the action text.
     * @return This instance.
     */
    @NonNull
    public SnackbarWrapper setActionTextColor(@ColorInt int color) {
        snackbar.setActionTextColor(color);
        return this;
    }

    /**
     * Set the text color for the action on the {@link Snackbar}.
     *
     * @param colorResId Resource of the color to make the action text.
     * @return This instance.
     */
    @NonNull
    public SnackbarWrapper setActionTextColorRes(@ColorRes int colorResId) {
        snackbar.setActionTextColor(ContextCompat.getColor(context, colorResId));
        return this;
    }

    /**
     * Get the text color for the action on the {@link Snackbar}.
     *
     * @return The action text color.
     */
    public ColorStateList getActionTextColors() {
        return actionView.getTextColors();
    }

    /**
     * Get the text color for the action on the {@link Snackbar}.
     *
     * @return The action text color.
     */
    @ColorInt
    public int getActionCurrentTextColor() {
        return actionView.getCurrentTextColor();
    }

    /**
     * Set the visibility of the action on the {@link Snackbar}.
     *
     * @param visibility The action visibility.
     * @return This instance.
     */
    public SnackbarWrapper setActionVisibility(int visibility) {
        actionView.setVisibility(visibility);
        return this;
    }

    /**
     * Get the visibility of the action on the {@link Snackbar}.
     *
     * @return The action visibility.
     */
    public int getActionVisibility() {
        return actionView.getVisibility();
    }

    /**
     * Set the action text on the {@link Snackbar} to start with an uppercase letter and for the
     * remaining letters to be lowercase. By default on API 14 and above the action will be
     * displayed all uppercase, this allows you to customise that.
     *
     * @return This instance.
     */
    @NonNull
    public SnackbarWrapper setLowercaseActionText() {
        Compatibility.getInstance().setAllCaps(actionView, false);
        return this;
    }

    /**
     * Make the action text on the {@link Snackbar} all uppercase.
     *
     * @return This instance.
     */
    @NonNull
    public SnackbarWrapper setUppercaseActionText() {
        Compatibility.getInstance().setAllCaps(actionView, true);
        return this;
    }

    /**
     * Update the message in this {@link Snackbar}. This will overwrite the whole message that is
     * currently shown.
     *
     * @param message The new message.
     * @return This instance.
     */
    @NonNull
    public SnackbarWrapper setText(@NonNull CharSequence message) {
        snackbar.setText(message);
        return this;
    }

    /**
     * Update the message in the {@link Snackbar}. This will overwrite the whole message that is
     * currently shown.
     *
     * @param messageResId String resource of the new message.
     * @return This instance.
     */
    @NonNull
    public SnackbarWrapper setText(@StringRes int messageResId) {
        snackbar.setText(messageResId);
        return this;
    }

    /**
     * Get the message shown in the {@link Snackbar}.
     *
     * @return The message shown.
     */
    public CharSequence getText() {
        return messageView.getText();
    }

    /**
     * Set the text color for the message shown in the {@link Snackbar}.
     *
     * @param color The message text color.
     * @return This instance.
     */
    public SnackbarWrapper setTextColor(@ColorInt int color) {
        messageView.setTextColor(color);
        return this;
    }

    /**
     * Set the text color for the message shown in the {@link Snackbar}.
     *
     * @param colors The message text color state list.
     * @return This instance.
     */
    public SnackbarWrapper setTextColor(ColorStateList colors) {
        messageView.setTextColor(colors);
        return this;
    }

    /**
     * Set the text color for the message shown in the {@link Snackbar}.
     *
     * @param colorResId The message text color resource.
     * @return This instance.
     */
    public SnackbarWrapper setTextColorRes(@ColorRes int colorResId) {
        messageView.setTextColor(ContextCompat.getColor(context, colorResId));
        return this;
    }

    /**
     * Get the text color for the message on the {@link Snackbar}.
     *
     * @return The message text color.
     */
    public ColorStateList getTextColors() {
        return messageView.getTextColors();
    }

    /**
     * Get the text color for the message on the {@link Snackbar}.
     *
     * @return The message text color.
     */
    @ColorInt
    public int getCurrentTextColor() {
        return messageView.getCurrentTextColor();
    }

    /**
     * Append text to the {@link Snackbar} message.
     *
     * @param message The text to append.
     * @return This instance.
     */
    public SnackbarWrapper appendMessage(CharSequence message) {
        messageView.append(message);
        return this;
    }

    /**
     * Append text to the {@link Snackbar} message.
     *
     * @param messageResId The string resource of the text to append.
     * @return This instance.
     */
    public SnackbarWrapper appendMessage(@StringRes int messageResId) {
        return appendMessage(context.getString(messageResId));
    }

    /**
     * Append text in the specified color to the {@link Snackbar}.
     *
     * @param message The text to append.
     * @param color   The color to apply to the text.
     * @return This instance.
     */
    public SnackbarWrapper appendMessage(CharSequence message, @ColorInt int color) {
        Spannable spannable = new SpannableString(message);
        spannable.setSpan(new ForegroundColorSpan(color), 0, spannable.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        messageView.append(spannable);
        return this;
    }

    /**
     * Append text in the specified color to the {@link Snackbar}.
     *
     * @param messageResId The string resource of the text to append.
     * @param colorResId   The resource of the color to apply to the text.
     * @return This instance.
     */
    public SnackbarWrapper appendMessage(@StringRes int messageResId,
                                         @ColorRes int colorResId) {
        return appendMessage(context.getString(messageResId),
                ContextCompat.getColor(context, colorResId));
    }

    /**
     * Set the visibility of the message on the {@link Snackbar}.
     *
     * @param visibility The message visibility.
     * @return This instance.
     */
    public SnackbarWrapper setMessageVisibility(int visibility) {
        messageView.setVisibility(visibility);
        return this;
    }

    /**
     * Get the visibility of the message on the {@link Snackbar}.
     *
     * @return The message visibility.
     */
    public int getMessageVisibility() {
        return messageView.getVisibility();
    }

    /**
     * Set the total time to show the {@link Snackbar} for.
     *
     * @param duration The length of time for which to show the {@link Snackbar}, can either be one
     *                 of the predefined lengths: LENGTH_SHORT, LENGTH_LONG or a custom duration
     *                 in milliseconds.
     * @return This instance.
     */
    @NonNull
    public SnackbarWrapper setDuration(@Duration int duration) {
        snackbar.setDuration(duration);
        return this;
    }

    /**
     * Get the total duration to show the {@link Snackbar} for.
     *
     * @return The total duration to show for.
     */
    @Duration
    public int getDuration() {
        return snackbar.getDuration();
    }

    /**
     * Get the {@link Snackbar}'s view.
     *
     * @return This {@link Snackbar}'s view.
     */
    @NonNull
    public View getView() {
        return snackbar.getView();
    }

    /**
     * Set the background color of the {@link Snackbar}.
     *
     * @param color The background color.
     * @return This instance.
     */
    public SnackbarWrapper setBackgroundColor(@ColorInt int color) {
        getView().setBackgroundColor(color);
        return this;
    }

    /**
     * Set the background color of the {@link Snackbar}.
     *
     * @param colorResId The background color resource.
     * @return This instance.
     */
    public SnackbarWrapper setBackgroundColorRes(@ColorRes int colorResId) {
        getView().setBackgroundColor(ContextCompat.getColor(context, colorResId));
        return this;
    }

    /**
     * Set the callback to be called when the {@link Snackbar} changes visibility. This will
     * overwrite any callback that has already been set on the {@link Snackbar}.
     *
     * @param callback The callback to be called.
     * @return This instance.
     */
    @NonNull
    public SnackbarWrapper setCallback(Callback callback) {
        snackbar.setCallback(callback);
        this.callback = callback;
        return this;
    }

    /**
     * Set the callback to be called when the {@link Snackbar} changes visibility. If
     * {@link this.setCallback(Callback)} has already been called, then that callback will be
     * combined with the SnackbarCallback specified here.
     *
     * @param callback The callback to be called.
     * @return This instance.
     */
    @NonNull
    public SnackbarWrapper setSnackbarCallback(SnackbarCallback callback) {
        return setCallback(new SnackbarCombinedCallback(callback, this.callback));
    }

    /**
     * Set the icon at the start of the {@link Snackbar}.  If there is no icon it will be added,
     * or if there is then it will be replaced.
     *
     * @param iconResId The icon drawable resource to display.
     * @return This instance.
     */
    public SnackbarWrapper setIcon(@DrawableRes int iconResId) {
        return setIcon(ContextCompat.getDrawable(context, iconResId));
    }

    /**
     * Set the icon at the start of the {@link Snackbar}. If there is no icon it will be added,
     * or if there is then it will be replaced.
     *
     * @param icon The icon to display.
     * @return This instance.
     */
    public SnackbarWrapper setIcon(Drawable icon) {
        iconBuilder
                .icon(icon)
                .bindToSnackbar();
        return this;
    }

    /**
     * Set the margin to be displayed before the icon. On platform versions that support
     * bi-directional layouts, this will be the start margin, on platforms before this it will just
     * be the left margin.
     *
     * @param iconMarginStart The margin before the icon.
     * @return This instance.
     */
    public SnackbarWrapper setIconMarginStart(@DimenRes int iconMarginStart) {
        return setIconMarginStartPixels(context.getResources().getDimensionPixelSize(iconMarginStart));
    }

    /**
     * Set the margin to be displayed before the icon in pixels. On platform versions that support
     * bi-directional layouts, this will be the start margin, on platforms before this it will just
     * be the left margin.
     *
     * @param iconMarginStartPixels The margin before the icon.
     * @return This instance.
     */
    public SnackbarWrapper setIconMarginStartPixels(int iconMarginStartPixels) {
        iconBuilder
                .iconMarginStartPixels(iconMarginStartPixels)
                .bindToSnackbar();
        return this;
    }

    /**
     * Set the margin to be displayed after the icon. On platform versions that support
     * bi-directional layouts, this will be the end margin, on platforms before this it will just
     * be the right margin.
     *
     * @param iconMarginEnd The margin after the icon.
     * @return This instance.
     */
    public SnackbarWrapper setIconMarginEnd(@DimenRes int iconMarginEnd) {
        return setIconMarginEndPixels(context.getResources().getDimensionPixelSize(iconMarginEnd));
    }

    /**
     * Set the margin to be displayed after the icon in pixels. On platform versions that support
     * bi-directional layouts, this will be the end margin, on platforms before this it will just
     * be the right margin.
     *
     * @param iconMarginEndPixels The margin after the icon.
     * @return This instance.
     */
    public SnackbarWrapper setIconMarginEndPixels(int iconMarginEndPixels) {
        iconBuilder
                .iconMarginEndPixels(iconMarginEndPixels)
                .bindToSnackbar();
        return this;
    }

    /**
     * Show the {@link Snackbar}.
     *
     * @return This instance.
     */
    public SnackbarWrapper show() {
        snackbar.show();
        return this;
    }

    /**
     * Dismiss the {@link Snackbar}.
     *
     * @return This instance.
     */
    public SnackbarWrapper dismiss() {
        snackbar.dismiss();
        return this;
    }

    /**
     * Get whether the {@link Snackbar} is showing.
     *
     * @return Whether this {@link Snackbar} is currently being shown.
     */
    public boolean isShown() {
        return snackbar.isShown();
    }

    /**
     * Get whether the {@link Snackbar} is showing, or is queued to be shown next.
     *
     * @return Whether the {@link Snackbar} is currently being shown, or is queued to be shown next.
     */
    public boolean isShownOrQueued() {
        return snackbar.isShownOrQueued();
    }

}
