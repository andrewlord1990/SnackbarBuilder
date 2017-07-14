/*
 * Copyright (C) 2016 Andrew Lord
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
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
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCallback;

import java.util.List;

/**
 * SnackbarWrapper is an extension to the Snackbar available within the Android Design Support library. By wrapping a
 * Snackbar within it, it provides many customisations that are not available without it. The setter methods also return
 * the SnackbarWrapper instance, allowing you to chain the calls together fluently. In order to create a SnackbarWrapper
 * there is the SnackbarBuilder.buildWrapper() method. Alternatively, if you already have a Snackbar instance, you can
 * just create your own SnackbarWrapper and pass the Snackbar into it.
 */
public final class SnackbarWrapper {

  Context context;

  private final Snackbar snackbar;
  private final TextView messageView;
  private final Button actionView;
  private final TextViewExtension actionViewExtension;

  /**
   * Create by wrapping a Snackbar.
   *
   * @param snackbar The Snackbar to wrap.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper(Snackbar snackbar) {
    this.snackbar = snackbar;
    messageView = (TextView) getView().findViewById(R.id.snackbar_text);
    actionView = (Button) getView().findViewById(R.id.snackbar_action);
    actionViewExtension = TextViewExtension.from(actionView);
    context = snackbar.getView().getContext();
  }

  /**
   * Get the Snackbar that has been wrapped.
   *
   * @return The Snackbar.
   */
  public Snackbar getSnackbar() {
    return snackbar;
  }

  /**
   * Get the Snackbar's view.
   *
   * @return This Snackbar's view.
   */
  @SuppressWarnings("WeakerAccess")
  public View getView() {
    return snackbar.getView();
  }

  /**
   * Get the action displayed in the Snackbar.
   *
   * @return Text displayed as an action.
   */
  @SuppressWarnings("WeakerAccess")
  public CharSequence getActionText() {
    return actionView.getText();
  }

  /**
   * Set the action to be displayed in the Snackbar.
   *
   * @param actionText String resource of the text to display as an action.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setActionText(@StringRes int actionText) {
    actionView.setText(actionText);
    return this;
  }

  /**
   * Set the action to be displayed in the Snackbar.
   *
   * @param actionText Text to display as an action.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setActionText(CharSequence actionText) {
    actionView.setText(actionText);
    return this;
  }

  /**
   * Set the callback to be invoked when the action is clicked.
   *
   * @param actionClickListener Callback to be invoked when the action is clicked.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setActionClickListener(OnClickListener actionClickListener) {
    actionView.setOnClickListener(actionClickListener);
    return this;
  }

  /**
   * Set the action to be displayed in the Snackbar and the callback to invoke when the action is clicked.
   *
   * @param actionText          String resource to display as an action.
   * @param actionClickListener Callback to be invoked when the action is clicked.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setAction(@StringRes int actionText, OnClickListener actionClickListener) {
    snackbar.setAction(actionText, actionClickListener);
    return this;
  }

  /**
   * Set the action to be displayed in the Snackbar and the callback to invoke when the action is clicked.
   *
   * @param actionText          Text to display as an action.
   * @param actionClickListener Callback to be invoked when the action is clicked.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setAction(CharSequence actionText, OnClickListener actionClickListener) {
    snackbar.setAction(actionText, actionClickListener);
    return this;
  }

  /**
   * Get the text color for the action on the Snackbar.
   *
   * @return The action text color.
   */
  @SuppressWarnings("WeakerAccess")
  public ColorStateList getActionTextColors() {
    return actionView.getTextColors();
  }

  /**
   * Get the text color for the action on the Snackbar.
   *
   * @return The action text color.
   */
  @ColorInt
  @SuppressWarnings("WeakerAccess")
  public int getActionCurrentTextColor() {
    return actionView.getCurrentTextColor();
  }

  /**
   * Set the text color for the action on the Snackbar.
   *
   * @param colors Colors state list to apply to the action text.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setActionTextColor(ColorStateList colors) {
    snackbar.setActionTextColor(colors);
    return this;
  }

  /**
   * Set the text color for the action on the Snackbar.
   *
   * @param color Color to make the action text.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setActionTextColor(@ColorInt int color) {
    snackbar.setActionTextColor(color);
    return this;
  }

  /**
   * Set the text color for the action on the Snackbar.
   *
   * @param color Resource of the color to make the action text.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setActionTextColorRes(@ColorRes int color) {
    snackbar.setActionTextColor(ContextCompat.getColor(context, color));
    return this;
  }

  /**
   * Get the visibility of the action on the Snackbar.
   *
   * @return The action visibility.
   */
  @SuppressWarnings("WeakerAccess")
  public int getActionVisibility() {
    return actionView.getVisibility();
  }

  /**
   * Set the visibility of the action on the Snackbar.
   *
   * @param visibility The action visibility.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setActionVisibility(int visibility) {
    actionView.setVisibility(visibility);
    return this;
  }

  /**
   * Set whether the action text on the Snackbar is all uppercase or in sentence case.
   *
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setAllCapsActionText(boolean allCaps) {
    actionViewExtension.setAllCaps(allCaps);
    return this;
  }

  /**
   * Set the action text on the Snackbar to be in sentence case (starts with uppercase letter and remaining letters in
   * lowercase). By default on API 14 and above the action will be displayed all uppercase, this allows you to customise
   * that.
   *
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setLowercaseActionText() {
    actionViewExtension.setAllCaps(false);
    return this;
  }

  /**
   * Make the action text on the Snackbar all uppercase.
   *
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setUppercaseActionText() {
    actionViewExtension.setAllCaps(true);
    return this;
  }

  /**
   * Get the message shown in the Snackbar.
   *
   * @return The message shown.
   */
  @SuppressWarnings("WeakerAccess")
  public CharSequence getText() {
    return messageView.getText();
  }

  /**
   * Update the message in the Snackbar. This will overwrite the whole message that is currently shown.
   *
   * @param message String resource of the new message.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setText(@StringRes int message) {
    snackbar.setText(message);
    return this;
  }

  /**
   * Update the message in this Snackbar. This will overwrite the whole message that is currently shown.
   *
   * @param message The new message.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setText(@NonNull CharSequence message) {
    snackbar.setText(message);
    return this;
  }

  /**
   * Get the text color for the message on the Snackbar.
   *
   * @return The message text color.
   */
  @SuppressWarnings("WeakerAccess")
  public ColorStateList getTextColors() {
    return messageView.getTextColors();
  }

  /**
   * Get the text color for the message on the Snackbar.
   *
   * @return The message text color.
   */
  @ColorInt
  @SuppressWarnings("WeakerAccess")
  public int getCurrentTextColor() {
    return messageView.getCurrentTextColor();
  }

  /**
   * Set the text color for the message shown in the Snackbar.
   *
   * @param color The message text color.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setTextColor(@ColorInt int color) {
    messageView.setTextColor(color);
    return this;
  }

  /**
   * Set the text color for the message shown in the Snackbar.
   *
   * @param colors The message text color state list.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setTextColor(ColorStateList colors) {
    messageView.setTextColor(colors);
    return this;
  }

  /**
   * Set the text color for the message shown in the Snackbar.
   *
   * @param color The message text color resource.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setTextColorRes(@ColorRes int color) {
    messageView.setTextColor(ContextCompat.getColor(context, color));
    return this;
  }

  /**
   * Append text to the Snackbar message.
   *
   * @param message The text to append.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper appendMessage(@NonNull CharSequence message) {
    messageView.append(message);
    return this;
  }

  /**
   * Append text to the Snackbar message.
   *
   * @param message The string resource of the text to append.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper appendMessage(@StringRes int message) {
    return appendMessage(context.getString(message));
  }

  /**
   * Append text in the specified color to the Snackbar.
   *
   * @param message The text to append.
   * @param color   The color to apply to the text.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper appendMessage(@NonNull CharSequence message, @ColorInt int color) {
    Spannable spannable = new SpannableString(message);
    spannable.setSpan(new ForegroundColorSpan(color), 0, spannable.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    messageView.append(spannable);
    return this;
  }

  /**
   * Append text in the specified color to the Snackbar.
   *
   * @param message The string resource of the text to append.
   * @param color   The resource of the color to apply to the text.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper appendMessage(@StringRes int message,
                                       @ColorRes int color) {
    return appendMessage(context.getString(message),
        ContextCompat.getColor(context, color));
  }

  /**
   * Get the visibility of the message on the Snackbar.
   *
   * @return The message visibility.
   */
  @SuppressWarnings("WeakerAccess")
  public int getMessageVisibility() {
    return messageView.getVisibility();
  }

  /**
   * Set the visibility of the message on the Snackbar.
   *
   * @param visibility The message visibility.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setMessageVisibility(int visibility) {
    messageView.setVisibility(visibility);
    return this;
  }

  /**
   * Get the total duration to show the Snackbar for.
   *
   * @return The total duration to show for.
   */
  @SuppressWarnings("WeakerAccess")
  public int getDuration() {
    return snackbar.getDuration();
  }

  /**
   * Set the total time to show the Snackbar for.
   *
   * @param duration The length of time for which to show the Snackbar, can either be one of the predefined lengths:
   *                 LENGTH_SHORT, LENGTH_LONG or a custom duration in milliseconds.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setDuration(int duration) {
    snackbar.setDuration(duration);
    return this;
  }

  /**
   * Set the background color of the Snackbar.
   *
   * @param color The background color.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setBackgroundColor(@ColorInt int color) {
    getView().setBackgroundColor(color);
    return this;
  }

  /**
   * Set the background color of the Snackbar.
   *
   * @param color The background color resource.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setBackgroundColorRes(@ColorRes int color) {
    getView().setBackgroundColor(ContextCompat.getColor(context, color));
    return this;
  }

  /**
   * Add a callback to the Snackbar for various events.
   *
   * @param callback The callback to be added.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper addCallback(Callback callback) {
    snackbar.addCallback(callback);
    return this;
  }

  /**
   * Adds multiple callbacks to the Snackbar for various events.
   *
   * @param callbacks The callbacks to be added.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper addCallbacks(List<Callback> callbacks) {
    int callbacksSize = callbacks.size();
    for (int i = 0; i < callbacksSize; i++) {
      addCallback(callbacks.get(i));
    }
    return this;
  }

  /**
   * Add a SnackbarCallback to the Snackbar for various events.
   *
   * @param callback The callback to be added.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper addSnackbarCallback(SnackbarCallback callback) {
    return addCallback(callback);
  }

  /**
   * Set the icon at the start of the Snackbar.  If there is no icon it will be added, or if there is then it will be
   * replaced.
   *
   * @param icon The icon drawable resource to display.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setIcon(@DrawableRes int icon) {
    return setIcon(ContextCompat.getDrawable(context, icon));
  }

  /**
   * Set the icon at the start of the Snackbar. If there is no icon it will be added, or if there is then it will be
   * replaced.
   *
   * @param icon The icon to display.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setIcon(Drawable icon) {
    messageView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
    return this;
  }

  /**
   * Set the margin to be displayed between the icon and the text.
   *
   * @param iconMarginPixels The margin before the icon.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setIconMargin(int iconMarginPixels) {
    messageView.setCompoundDrawablePadding(iconMarginPixels);
    return this;
  }

  /**
   * Set the margin to be displayed between the icon and the text.
   *
   * @param iconMargin The margin before the icon.
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper setIconMarginRes(@DimenRes int iconMargin) {
    return setIconMargin(context.getResources().getDimensionPixelSize(iconMargin));
  }

  /**
   * Show the Snackbar.
   *
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper show() {
    snackbar.show();
    return this;
  }

  /**
   * Dismiss the Snackbar.
   *
   * @return This instance.
   */
  @NonNull
  @SuppressWarnings("WeakerAccess")
  public SnackbarWrapper dismiss() {
    snackbar.dismiss();
    return this;
  }

  /**
   * Get whether the Snackbar is showing.
   *
   * @return Whether this Snackbar is currently being shown.
   */
  @SuppressWarnings("WeakerAccess")
  public boolean isShown() {
    return snackbar.isShown();
  }

  /**
   * Get whether the Snackbar is showing, or is queued to be shown next.
   *
   * @return Whether the Snackbar is currently being shown, or is queued to be shown next.
   */
  @SuppressWarnings("WeakerAccess")
  public boolean isShownOrQueued() {
    return snackbar.isShownOrQueued();
  }

}
