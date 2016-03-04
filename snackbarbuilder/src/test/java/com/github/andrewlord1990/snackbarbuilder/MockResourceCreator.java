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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;

import static org.mockito.Mockito.when;

class MockResourceCreator {

    private SnackbarBuilder builder;
    private SnackbarWrapper wrapper;
    private Context context;
    private Resources resources;

    static MockResourceCreator fromBuilder(SnackbarBuilder builder) {
        MockResourceCreator creator = new MockResourceCreator();
        creator.builder = builder;
        return creator;
    }

    static MockResourceCreator fromWrapper(SnackbarWrapper wrapper) {
        MockResourceCreator creator = new MockResourceCreator();
        creator.wrapper = wrapper;
        return creator;
    }

    MockResourceCreator withContext(Context context) {
        this.context = context;
        return this;
    }

    MockResourceCreator withResources(Resources resources) {
        this.resources = resources;
        return this;
    }

    @TargetApi(21)
    void createMockDrawableResId(@DrawableRes int drawableResId, Drawable drawable) {
        setMockContext();
        when(context.getDrawable(drawableResId)).thenReturn(drawable);
    }

    @DimenRes
    int getDimensionPixelSize(int pixels) {
        setMockContext();
        @DimenRes int dimenResId = 50;
        when(resources.getDimensionPixelSize(dimenResId)).thenReturn(pixels);
        return dimenResId;
    }

    private void setMockContext() {
        if (builder != null) {
            builder.context = context;
        }
        if (wrapper != null) {
            wrapper.context = context;
        }
        when(context.getResources()).thenReturn(resources);
    }

}
