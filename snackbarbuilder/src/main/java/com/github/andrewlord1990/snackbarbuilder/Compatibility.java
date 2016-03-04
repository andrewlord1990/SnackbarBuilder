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

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.widget.TextView;

final class Compatibility {

    private static Compatibility instance;

    private Compatibility() {
    }

    static Compatibility getInstance() {
        if (instance == null) {
            instance = new Compatibility();
        }
        return instance;
    }

    private boolean isApiAtLeast14() {
        return VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    void setAllCaps(TextView textView, boolean allCaps) {
        if (isApiAtLeast14()) {
            textView.setAllCaps(allCaps);
        }
    }

}
