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

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCombinedCallback;

class SnackbarCustomiser {

    private Snackbar snackbar;

    SnackbarCustomiser(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    SnackbarCustomiser customiseMessage(@ColorInt int messageTextColor, CharSequence appendMessages) {
        if (messageTextColor != 0) {
            TextView messageView = getMessageView();
            messageView.setTextColor(messageTextColor);
            if (appendMessages != null) {
                messageView.append(appendMessages);
            }
        }
        return this;
    }

    TextView getMessageView() {
        return (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
    }

    SnackbarCustomiser setBackgroundColor(@ColorInt int backgroundColor) {
        if (backgroundColor != 0) {
            snackbar.getView().setBackgroundColor(backgroundColor);
        }
        return this;
    }

    SnackbarCustomiser setAction(CharSequence actionText, OnClickListener actionClickListener) {
        OnClickListener clickListener = actionClickListener;
        if (clickListener == null) {
            clickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            };
        }
        if (actionText != null) {
            snackbar.setAction(actionText, clickListener);
        }
        return this;
    }

    SnackbarCustomiser setActionTextColor(@ColorInt int actionTextColor) {
        if (actionTextColor != 0) {
            snackbar.setActionTextColor(actionTextColor);
        }
        return this;
    }

    SnackbarCustomiser setActionAllCaps(boolean actionAllCaps) {
        Compatibility.getInstance().setAllCaps(getActionView(), actionAllCaps);
        return this;
    }

    SnackbarCustomiser setCallbacks(SnackbarCombinedCallback combinedCallback) {
        snackbar.setCallback(combinedCallback);
        return this;
    }

    SnackbarCustomiser setIcon(Drawable icon, int iconMarginStartPixels, int iconMarginEndPixels) {
        if (icon != null) {
            SnackbarIconBuilder.builder(snackbar)
                    .icon(icon)
                    .iconMarginStartPixels(iconMarginStartPixels)
                    .iconMarginEndPixels(iconMarginEndPixels)
                    .bindToSnackbar();
        }
        return this;
    }

    Button getActionView() {
        return (Button) snackbar.getView().findViewById(R.id.snackbar_action);
    }
}
