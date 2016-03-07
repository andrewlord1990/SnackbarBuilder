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

package com.github.andrewlord1990.snackbarbuilder.callback;

import android.support.design.widget.Snackbar.Callback;

import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCombinedCallback.Builder;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class SnackbarCombinedCallbackBuilderAssert extends AbstractAssert<
        SnackbarCombinedCallbackBuilderAssert, Builder> {

    protected SnackbarCombinedCallbackBuilderAssert(Builder actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public static SnackbarCombinedCallbackBuilderAssert assertThat(Builder actual) {
        return new SnackbarCombinedCallbackBuilderAssert(actual,
                SnackbarCombinedCallbackBuilderAssert.class);
    }

    public SnackbarCombinedCallbackBuilderAssert hasCallback(Callback callback) {
        Assertions.assertThat(actual.callback).isEqualTo(callback);
        return this;
    }

    public SnackbarCombinedCallbackBuilderAssert hasSnackbarCallback(SnackbarCallback callback) {
        Assertions.assertThat(actual.snackbarCallback).isEqualTo(callback);
        return this;
    }

    public SnackbarCombinedCallbackBuilderAssert hasShowCallback(SnackbarShowCallback callback) {
        Assertions.assertThat(actual.showCallback).isEqualTo(callback);
        return this;
    }

    public SnackbarCombinedCallbackBuilderAssert hasDismissCallback(
            SnackbarDismissCallback callback) {
        Assertions.assertThat(actual.dismissCallback).isEqualTo(callback);
        return this;
    }

    public SnackbarCombinedCallbackBuilderAssert hasActionDismissCallback(
            SnackbarActionDismissCallback callback) {
        Assertions.assertThat(actual.actionDismissCallback).isEqualTo(callback);
        return this;
    }

    public SnackbarCombinedCallbackBuilderAssert hasSwipeDismissCallback(
            SnackbarSwipeDismissCallback callback) {
        Assertions.assertThat(actual.swipeDismissCallback).isEqualTo(callback);
        return this;
    }

    public SnackbarCombinedCallbackBuilderAssert hasTimeoutDismissCallback(
            SnackbarTimeoutDismissCallback callback) {
        Assertions.assertThat(actual.timeoutDismissCallback).isEqualTo(callback);
        return this;
    }

    public SnackbarCombinedCallbackBuilderAssert hasManualDismissCallback(
            SnackbarManualDismissCallback callback) {
        Assertions.assertThat(actual.manualDismissCallback).isEqualTo(callback);
        return this;
    }

    public SnackbarCombinedCallbackBuilderAssert hasConsecutiveDismissCallback(
            SnackbarConsecutiveDismissCallback callback) {
        Assertions.assertThat(actual.consecutiveDismissCallback).isEqualTo(callback);
        return this;
    }

}
