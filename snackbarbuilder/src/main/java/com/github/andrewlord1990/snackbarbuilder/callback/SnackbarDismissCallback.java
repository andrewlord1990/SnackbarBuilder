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
 * A callback to be notified when the Snackbar is dimissed through any means.
 */
public interface SnackbarDismissCallback {

  /**
   * Indicates that the Snackbar was dismissed through any means.
   *
   * @param snackbar     The Snackbar.
   * @param dismissEvent The event through which it was dismissed.
   */
  void onSnackbarDismissed(Snackbar snackbar, int dismissEvent);

}
