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

import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.widget.TextView;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.List;

public class SnackbarCustomAssert extends AbstractAssert<SnackbarCustomAssert, Snackbar> {

    protected SnackbarCustomAssert(Snackbar actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public static SnackbarCustomAssert assertThat(Snackbar actual) {
        return new SnackbarCustomAssert(actual, SnackbarCustomAssert.class);
    }

    public SnackbarCustomAssert hasMessagesAppended(List<Pair<String, Integer>> expected) {
        TextView messageView = (TextView) actual.getView().findViewById(R.id.snackbar_text);
        SpannableString message = SpannableString.valueOf(messageView.getText());
        int size = expected.size();
        for (int i = 0; i < size; i++) {
            Pair<String, Integer> pair = expected.get(i);
            int length = pair.first.length();
            Assertions.assertThat(message.subSequence(0, length).toString()).isEqualTo(pair.first);
            ForegroundColorSpan[] spans = message
                    .getSpans(0, length, ForegroundColorSpan.class);
            if (pair.second == 0) {
                Assertions.assertThat(spans).isEmpty();
            } else {
                Assertions.assertThat(spans).hasSize(1);
                Assertions.assertThat(spans[0].getForegroundColor()).isEqualTo(pair.second);
            }
            message = SpannableString.valueOf(message.subSequence(length, message.length()));
        }
        return this;
    }
}
