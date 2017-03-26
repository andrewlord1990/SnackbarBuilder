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

import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.SnackbarLayout;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

class SnackbarIconBuilder {

  Snackbar snackbar;
  private ImageView iconView;
  private Drawable icon;
  private int iconMarginStartPixels;
  private int iconMarginEndPixels;

  SnackbarIconBuilder(Snackbar snackbar) {
    this.snackbar = snackbar;
    iconView = (ImageView) snackbar.getView().findViewById(R.id.snackbarbuilder_icon);
  }

  static SnackbarIconBuilder builder(Snackbar snackbar) {
    return new SnackbarIconBuilder(snackbar);
  }

  SnackbarIconBuilder icon(Drawable icon) {
    this.icon = icon;
    return this;
  }

  SnackbarIconBuilder iconMarginStartPixels(int iconMarginStartPixels) {
    this.iconMarginStartPixels = iconMarginStartPixels;
    return this;
  }

  SnackbarIconBuilder iconMarginEndPixels(int iconMarginEndPixels) {
    this.iconMarginEndPixels = iconMarginEndPixels;
    return this;
  }

  ImageView bindToSnackbar() {
    FrameLayout.LayoutParams params = getIconViewLayoutParams();
    if (iconView == null) {
      iconView = new ImageView(snackbar.getView().getContext());
      iconView.setId(R.id.snackbarbuilder_icon);

      SnackbarLayout view = (SnackbarLayout) snackbar.getView();
      view.addView(iconView, 0);
    }
    iconView.setLayoutParams(params);
    iconView.setImageDrawable(icon);
    return iconView;
  }

  private FrameLayout.LayoutParams getIconViewLayoutParams() {
    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
        FrameLayout.LayoutParams.WRAP_CONTENT);
    params.gravity = Gravity.CENTER_VERTICAL;
    MarginLayoutParamsCompat.setMarginStart(params, iconMarginStartPixels);
    MarginLayoutParamsCompat.setMarginEnd(params, iconMarginEndPixels);
    return params;
  }

}
