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
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;

import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarActionDismissCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarConsecutiveDismissCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarDismissCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarManualDismissCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarShowCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarSwipeDismissCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarTimeoutDismissCallback;
import com.github.andrewlord1990.snackbarbuilder.parent.SnackbarParentFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * A builder pattern to easily create and customise Android Design Support library Snackbars. On top of the
 * customisations you can make through the Snackbar API, there are some additional ones.
 * These include:
 * - Altering text and background colours
 * - Easier-to-use callbacks
 * - Appending messages
 * - Adding an icon
 * - Providing defaults for all SnackbarBuilders through theme attributes
 * The builder can either output a constructed Snackbar or a SnackbarWrapper. The SnackbarWrapper allows you to further
 * customise the Snackbar after creation and gives you more control than the Snackbar API. Various default values can
 * be loaded from a style specified within your theme. These values can be overridden by the builder methods.
 */
public final class SnackbarBuilder {

  Context context;
  View parentView;
  SpannableStringBuilder appendMessages;
  CharSequence message;
  int duration = Snackbar.LENGTH_LONG;
  CharSequence actionText;
  OnClickListener actionClickListener;
  List<Snackbar.Callback> callbacks = new ArrayList<>();
  boolean actionAllCaps = true;
  int backgroundColor;
  int actionTextColor;
  int messageTextColor;
  int parentViewId;
  Drawable icon;
  int iconMargin;

  /**
   * Create a builder to create a Snackbar. The Snackbar will be attached to the specified parent view.
   *
   * @param view Parent view to attach the Snackbar to.
   */
  public SnackbarBuilder(View view) {
    setup(view.getContext());
    parentView = view;
  }

  /**
   * Create a builder to create a Snackbar. The parent view to attach the Snackbar to is specified through the
   * attribute snackbarBuilder_parentViewId. This attribute is within a style provided through the theme attribute
   * snackbarBuilderStyle. The parent view will be found using this ID and the Snackbar will be attached to it.
   *
   * @param activity Activity to show the Snackbar in, it should contain a view with the ID specified in the style
   *                 attribute snackbarBuilder_parentViewId.
   */
  public SnackbarBuilder(Activity activity) {
    setup(activity);
    parentView = activity.findViewById(parentViewId);
  }

  /**
   * Create a builder to create a Snackbar. The parent view to attach the Snackbar to is found using the provided
   * SnackbarParentFinder. This gives you the flexibility of not using a single ID for the parent view, or to have
   * fallback view IDs in the case that the usual one isn't found in a particular activity.
   *
   * @param activity     Activity to show the Snackbar in.
   * @param parentFinder Used to find the parent view to attach the Snackbar to.
   */
  public SnackbarBuilder(Activity activity, SnackbarParentFinder parentFinder) {
    setup(activity);
    parentView = parentFinder.findSnackbarParent(activity);
  }

  private void setup(Context context) {
    this.context = context;
    loadThemeAttributes();
  }

  /**
   * Set the text to display on the Snackbar.
   *
   * @param message Text to display.
   * @return This instance.
   */
  public SnackbarBuilder message(CharSequence message) {
    this.message = message;
    return this;
  }

  /**
   * Set the text to display on the Snackbar.
   *
   * @param messageResId String resource of the text to display.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder message(@StringRes int messageResId) {
    this.message = context.getString(messageResId);
    return this;
  }

  /**
   * Set the colour to display the message on the Snackbar.
   *
   * @param messageTextColor Colour to display the message.
   * @return This instance.
   */
  public SnackbarBuilder messageTextColor(@ColorInt int messageTextColor) {
    this.messageTextColor = messageTextColor;
    return this;
  }

  /**
   * Set the colour to display the message on the Snackbar.
   *
   * @param messageTextColor Resource of the colour to display the message.
   * @return This instance.
   */
  public SnackbarBuilder messageTextColorRes(@ColorRes int messageTextColor) {
    this.messageTextColor = getColor(messageTextColor);
    return this;
  }

  /**
   * Add some text to append to the end of the message shown on the Snackbar.
   *
   * @param message Text to append to the Snackbar message.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder appendMessage(CharSequence message) {
    initialiseAppendMessages();
    appendMessages.append(message);
    return this;
  }

  /**
   * Add some text to append to the message shown on the Snackbar.
   *
   * @param messageResId String resource of the text to append to the Snackbar message.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder appendMessage(@StringRes int messageResId) {
    return appendMessage(context.getString(messageResId));
  }

  /**
   * Add some text to append to the message shown on the Snackbar and a colour to make it.
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
   * Add some text to append to the message shown on the Snackbar and a colour to make it.
   *
   * @param messageResId String resource of the text to append to the Snackbar message.
   * @param colorResId   Resource of the colour to make the appended text.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder appendMessage(@StringRes int messageResId,
                                       @ColorRes int colorResId) {
    return appendMessage(context.getString(messageResId),
        getColor(colorResId));
  }

  /**
   * Set the duration to show the Snackbar for.
   *
   * @param duration The duration.
   * @return This instance.
   */
  public SnackbarBuilder duration(int duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Set the text to display as an action on the Snackbar.
   *
   * @param actionText The text to set as an action.
   * @return This instance.
   */
  public SnackbarBuilder actionText(CharSequence actionText) {
    this.actionText = actionText;
    return this;
  }

  /**
   * Set the text to display as an action on the Snackbar.
   *
   * @param actionTextResId The string resource of the text to set as an action.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder actionText(@StringRes int actionTextResId) {
    this.actionText = context.getString(actionTextResId);
    return this;
  }

  /**
   * Set the colour to display the action on the Snackbar.
   *
   * @param actionTextColor Colour to display the action.
   * @return This instance.
   */
  public SnackbarBuilder actionTextColor(@ColorInt int actionTextColor) {
    this.actionTextColor = actionTextColor;
    return this;
  }

  /**
   * Set the colour to display the action on the Snackbar.
   *
   * @param actionTextColorResId Resource of the colour to display the action.
   * @return This instance.
   */
  public SnackbarBuilder actionTextColorRes(@ColorRes int actionTextColorResId) {
    this.actionTextColor = getColor(actionTextColorResId);
    return this;
  }

  /**
   * Set the click listener for the action on the Snackbar.
   *
   * @param actionClickListener Click listener for the action.
   * @return This instance.
   */
  public SnackbarBuilder actionClickListener(OnClickListener actionClickListener) {
    this.actionClickListener = actionClickListener;
    return this;
  }

  /**
   * Set the colour to make the background of the Snackbar.
   *
   * @param backgroundColor The background colour.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder backgroundColor(@ColorInt int backgroundColor) {
    this.backgroundColor = backgroundColor;
    return this;
  }

  /**
   * Set the colour to make the background of the Snackbar.
   *
   * @param backgroundColorResId The resource of the background colour.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder backgroundColorRes(@ColorRes int backgroundColorResId) {
    this.backgroundColor = getColor(backgroundColorResId);
    return this;
  }

  /**
   * Set the standard callback for being informed of the Snackbar being shown or dismissed.
   *
   * @param callback The callback.
   * @return This instance.
   */
  public SnackbarBuilder callback(Snackbar.Callback callback) {
    callbacks.add(callback);
    return this;
  }

  /**
   * Set the enhanced callback for being informed of the Snackbar being shown or dismissed, through individual callback
   * methods.
   *
   * @param snackbarCallback The callback.
   * @return This instance.
   */
  public SnackbarBuilder snackbarCallback(SnackbarCallback snackbarCallback) {
    callbacks.add(snackbarCallback);
    return this;
  }

  /**
   * Set the callback to be informed of the Snackbar being shown.
   *
   * @param callback The callback.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder showCallback(final SnackbarShowCallback callback) {
    callbacks.add(new SnackbarCallback() {
      public void onSnackbarShown(Snackbar snackbar) {
        callback.onSnackbarShown(snackbar);
      }
    });
    return this;
  }

  /**
   * Set the callback to be informed of the Snackbar being dismissed through some means.
   *
   * @param callback The callback.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder dismissCallback(final SnackbarDismissCallback callback) {
    callbacks.add(new SnackbarCallback() {
      public void onSnackbarDismissed(Snackbar snackbar, int dismissEvent) {
        callback.onSnackbarDismissed(snackbar, dismissEvent);
      }
    });
    return this;
  }

  /**
   * Set the callback to be informed of the Snackbar being dismissed due to the action being pressed.
   *
   * @param callback The callback.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder actionDismissCallback(final SnackbarActionDismissCallback callback) {
    callbacks.add(new SnackbarCallback() {
      public void onSnackbarActionPressed(Snackbar snackbar) {
        callback.onSnackbarActionPressed(snackbar);
      }
    });
    return this;
  }

  /**
   * Set the callback to be informed of the Snackbar being dismissed due to being swiped away.
   *
   * @param callback The callback.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder swipeDismissCallback(final SnackbarSwipeDismissCallback callback) {
    callbacks.add(new SnackbarCallback() {
      public void onSnackbarSwiped(Snackbar snackbar) {
        callback.onSnackbarSwiped(snackbar);
      }
    });
    return this;
  }

  /**
   * Set the callback to be informed of the Snackbar being dismissed due to a timeout.
   *
   * @param callback The callback.
   * @return This instance.
   */
  public SnackbarBuilder timeoutDismissCallback(final SnackbarTimeoutDismissCallback callback) {
    callbacks.add(new SnackbarCallback() {
      public void onSnackbarTimedOut(Snackbar snackbar) {
        callback.onSnackbarTimedOut(snackbar);
      }
    });
    return this;
  }

  /**
   * Set the callback to be informed of the Snackbar being dismissed manually, due to a call to dismiss().
   *
   * @param callback The callback.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder manualDismissCallback(final SnackbarManualDismissCallback callback) {
    callbacks.add(new SnackbarCallback() {
      public void onSnackbarManuallyDismissed(Snackbar snackbar) {
        callback.onSnackbarManuallyDismissed(snackbar);
      }
    });
    return this;
  }

  /**
   * Set the callback to be informed of the Snackbar being dismissed due to another Snackbar being shown.
   *
   * @param callback The callback.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder consecutiveDismissCallback(final SnackbarConsecutiveDismissCallback callback) {
    callbacks.add(new SnackbarCallback() {
      public void onSnackbarDismissedAfterAnotherShown(Snackbar snackbar) {
        callback.onSnackbarDismissedAfterAnotherShown(snackbar);
      }
    });
    return this;
  }

  /**
   * Make the action be displayed with the first letter uppercase and the rest lowercase. This is useful due to the
   * action being displayed all uppercase by default on API 14 and above.
   *
   * @return This instance.
   */
  public SnackbarBuilder lowercaseAction() {
    actionAllCaps = false;
    return this;
  }

  /**
   * Set an icon to display on the Snackbar next to the message. It is set using compound drawable on the TextView,
   * so it isn't currently compatible with AppCompat vector drawables unfortunately.
   *
   * @param icon The drawable of the icon.
   * @return This instance.
   */
  public SnackbarBuilder icon(Drawable icon) {
    this.icon = icon;
    return this;
  }

  /**
   * Set an icon to display on the Snackbar next to the message. It is set using compound drawable on the TextView,
   * so it isn't currently compatible with AppCompat vector drawables unfortunately.
   *
   * @param iconResId The drawable resource of the icon.
   * @return This instance.
   */
  public SnackbarBuilder icon(@DrawableRes int iconResId) {
    icon = getDrawable(iconResId);
    return this;
  }

  /**
   * Set the margin to be displayed between the icon and the text.
   *
   * @param iconMargin The margin before the icon.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder iconMargin(int iconMargin) {
    this.iconMargin = iconMargin;
    return this;
  }

  /**
   * Set the margin to be displayed between the icon and the text.
   *
   * @param iconMarginResId The dimension resource of the margin before the icon.
   * @return This instance.
   */
  @SuppressWarnings("WeakerAccess")
  public SnackbarBuilder iconMarginRes(@DimenRes int iconMarginResId) {
    return iconMargin(
        context.getResources().getDimensionPixelSize(iconMarginResId));
  }

  /**
   * Build a Snackbar using the options specified in the builder. Wrap this Snackbar into a SnackbarWrapper, which
   * allows further customisation.
   *
   * @return A SnackbarWrapper, a class which wraps a Snackbar for further customisation.
   */
  public SnackbarWrapper buildWrapper() {
    Snackbar snackbar = Snackbar.make(parentView, message, duration);
    SnackbarWrapper wrapper = new SnackbarWrapper(snackbar)
        .setAction(actionText, sanitisedActionClickListener())
        .setActionTextColor(actionTextColor)
        .setAllCapsActionText(actionAllCaps)
        .setTextColor(messageTextColor)
        .addCallbacks(callbacks)
        .setIconMargin(iconMargin);
    if (appendMessages != null) {
      wrapper.appendMessage(appendMessages);
    }
    if (backgroundColor != 0) {
      wrapper.setBackgroundColor(backgroundColor);
    }
    if (icon != null) {
      wrapper.setIcon(icon);
    }
    return wrapper;
  }

  private OnClickListener sanitisedActionClickListener() {
    if (actionClickListener == null) {
      return new OnClickListener() {
        @Override
        public void onClick(View view) {
        }
      };
    }
    return actionClickListener;
  }

  /**
   * Build a Snackbar using the options specified in the builder.
   *
   * @return A Snackbar.
   */
  public Snackbar build() {
    return buildWrapper().getSnackbar();
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
      loadIconMargin(attrs);
      loadActionAllCaps(attrs);

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

  private void loadIconMargin(TypedArray attrs) {
    iconMargin = attrs.getDimensionPixelSize(
        R.styleable.SnackbarBuilderStyle_snackbarBuilder_iconMargin, context.getResources()
            .getDimensionPixelSize(R.dimen.snackbarbuilder_icon_margin_default));
  }

  private void loadActionAllCaps(TypedArray attrs) {
    actionAllCaps = attrs.getBoolean(R.styleable.SnackbarBuilderStyle_snackbarBuilder_actionAllCaps, true);
  }

  private void loadFallbackAttributes(TypedArray attrs) {
    if (messageTextColor == 0) {
      messageTextColor = getColor(R.color.snackbarbuilder_default_message);
    }
    if (actionTextColor == 0) {
      actionTextColor = attrs.getColor(R.styleable.SnackbarBuilderStyle_colorAccent,
          getColor(R.color.snackbarbuilder_default_message));
    }
  }

  private int getColor(@ColorRes int color) {
    return ContextCompat.getColor(context, color);
  }

  private Drawable getDrawable(@DrawableRes int drawableResId) {
    return ContextCompat.getDrawable(context, drawableResId);
  }
}
