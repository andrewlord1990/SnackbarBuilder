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

package com.github.andrewlord1990.snackbarbuilder.callback;

import android.support.design.widget.Snackbar;

/**
 * A callback for use with SnackbarBuilder that allows you to easily respond to the events you are interested in by
 * overriding the appropriate methods. You know longer have to check the value of the dismiss event, as you do with the
 * standard Callback class. Override whichever callback methods you are interested in.
 */
public class SnackbarCallback extends Snackbar.Callback {

  protected SnackbarCallback() {
    // To prevent class being instantiated directly.
  }

  @Override
  public final void onShown(Snackbar snackbar) {
    super.onShown(snackbar);

    onSnackbarShown(snackbar);
  }

  /**
   * Indicates that the Snackbar was shown (made visible).
   *
   * @param snackbar The Snackbar.
   */
  public void onSnackbarShown(Snackbar snackbar) {
    // Override if needed
  }

  /**
   * Notifies that the Snackbar has been dismissed through some event, for example swiping or the action being pressed.
   *
   * @param snackbar     The Snackbar which has been dismissed.
   * @param dismissEvent The event which caused the dismissal.
   */
  @Override
  public final void onDismissed(Snackbar snackbar, int dismissEvent) {
    super.onDismissed(snackbar, dismissEvent);

    notifySnackbarCallback(snackbar, dismissEvent);
  }

  private void notifySnackbarCallback(Snackbar snackbar, int dismissEvent) {
    switch (dismissEvent) {
      case Snackbar.Callback.DISMISS_EVENT_ACTION:
        onSnackbarActionPressed(snackbar);
        break;
      case Snackbar.Callback.DISMISS_EVENT_SWIPE:
        onSnackbarSwiped(snackbar);
        break;
      case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
        onSnackbarTimedOut(snackbar);
        break;
      case Snackbar.Callback.DISMISS_EVENT_MANUAL:
        onSnackbarManuallyDismissed(snackbar);
        break;
      case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:
        onSnackbarDismissedAfterAnotherShown(snackbar);
        break;
      default:
        break;
    }
    onSnackbarDismissed(snackbar);
    onSnackbarDismissed(snackbar, dismissEvent);
  }

  /**
   * Indicates that the Snackbar was dismissed through any means.
   *
   * @param snackbar The Snackbar.
   */
  public void onSnackbarDismissed(Snackbar snackbar) {
    // Override if needed
  }

  /**
   * Indicates that the Snackbar was dismissed through any means.
   *
   * @param snackbar     The Snackbar.
   * @param dismissEvent The event through which it was dismissed.
   */
  public void onSnackbarDismissed(Snackbar snackbar, int dismissEvent) {
    // Override if needed
  }

  /**
   * Indicates that the Snackbar was dismissed due to the action being pressed.
   *
   * @param snackbar The Snackbar.
   */
  public void onSnackbarActionPressed(Snackbar snackbar) {
    // Override if needed
  }

  /**
   * Indicates that the Snackbar was dismissed due to being swiped away.
   *
   * @param snackbar The Snackbar.
   */
  public void onSnackbarSwiped(Snackbar snackbar) {
    // Override if needed
  }

  /**
   * Indicates that the Snackbar was dismissed due to a timeout, specified by the
   * duration set.
   *
   * @param snackbar The Snackbar.
   */
  public void onSnackbarTimedOut(Snackbar snackbar) {
    // Override if needed
  }

  /**
   * Indicates that the Snackbar was dismissed manually via a call to dismiss().
   *
   * @param snackbar The Snackbar.
   */
  public void onSnackbarManuallyDismissed(Snackbar snackbar) {
    // Override if needed
  }

  /**
   * Indicates that the Snackbar was dismissed due to another Snackbar being shown.
   *
   * @param snackbar The Snackbar.
   */
  public void onSnackbarDismissedAfterAnotherShown(Snackbar snackbar) {
    // Override if needed
  }

}
