/*
 *
 *  * Copyright (C) 2015 Andrew Lord
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.github.andrewlord1990.snackbarbuilder.toastbuilder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

@Implements(Toast.class)
public class CustomShadowToast {

    @Implementation
    @SuppressLint("ShowToast")
    public static Toast makeText(Context context, CharSequence text, int duration) {

        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        TextView messageView = new TextView(context);
        layout.setLayoutParams(
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        messageView.setId(android.R.id.message);

        layout.addView(messageView);

        Toast toast = new Toast(context);
        toast.setView(layout);
        toast.setDuration(duration);
        toast.setText(text);
        return toast;
    }

}
