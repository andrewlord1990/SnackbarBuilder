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

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;
import android.support.design.widget.Snackbar.SnackbarLayout;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCombinedCallback;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
public class SnackbarCustomiserTest {

    private Snackbar snackbar;
    private SnackbarCustomiser customiser;

    @Mock
    Drawable drawable;

    @Mock
    Callback callback;

    @Mock
    SnackbarCallback snackbarCallback;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
        CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);
        snackbar = new SnackbarBuilder(parent).build();
        customiser = new SnackbarCustomiser(snackbar);
    }

    @Test
    public void whenCustomiseMessage_thenMessageTextColorSetAndMessagesAppended() {
        SpannableStringBuilder appendMessages = new SpannableStringBuilder();
        appendMessages.append("one");
        appendMessages.append("two");
        appendMessages.append("three");

        customiser.customiseMessage(Color.GREEN, appendMessages);

        assertThat(customiser.getMessageView()).hasCurrentTextColor(Color.GREEN);
        assertThat(customiser.getMessageView()).hasText("onetwothree");
    }

    @Test
    public void whenSetBackgroundColor_thenBackgroundColorSet() {
        customiser.setBackgroundColor(Color.GRAY);

        assertThat((ColorDrawable) snackbar.getView().getBackground())
                .hasColor(Color.GRAY);
    }

    @Test
    public void whenSetAction_thenActionClickListenerAndTextSet() {
        String text = "some action text";
        TestClickListener clickListener = new TestClickListener();

        customiser.setAction(text, clickListener);
        customiser.getActionView().performClick();

        assertThat(customiser.getActionView()).hasText(text);
        Assertions.assertThat(clickListener.isClicked()).isTrue();
    }

    @Test
    public void givenNoClickListener_whenSetAction_thenTextSet() {
        String text = "some text";

        customiser.setAction(text, null);
        customiser.getActionView().performClick();

        assertThat(customiser.getActionView()).hasText(text);
    }

    @Test
    public void whenSetActionTextColor_thenActionTextColorSet() {
        customiser.setActionTextColor(Color.YELLOW);

        assertThat(customiser.getActionView()).hasCurrentTextColor(Color.YELLOW);
    }

    @Test
    public void givenAllCapsFalse_whenSetActionAllCaps_thenLowercaseAction() {
        customiser.setActionAllCaps(false);

        Assertions.assertThat(customiser.getActionView().getTransformationMethod()).isNull();
    }

    @Test
    public void givenAllCapsTrue_whenSetActionAllCaps_thenUppercaseAction() {
        customiser.setActionAllCaps(true);

        Assertions.assertThat(customiser.getActionView().getTransformationMethod()).isNotNull();
    }

    @Test
    public void whenSetCallbacks_thenCallbacksSetup() {
        snackbar.show();
        SnackbarCombinedCallback combinedCallback = new SnackbarCombinedCallback(
                snackbarCallback, callback);

        customiser.setCallbacks(combinedCallback);
        snackbar.dismiss();

        verify(snackbarCallback).onSnackbarManuallyDismissed(snackbar);
        verify(callback).onDismissed(snackbar, Callback.DISMISS_EVENT_MANUAL);
    }

    @Test
    public void whenSetIcon_thenIconAndIconMarginsSet() {
        customiser.setIcon(drawable, 10, 20);

        SnackbarLayout layout = (SnackbarLayout) snackbar.getView();
        View firstChildView = layout.getChildAt(0);
        assertThat(firstChildView).isExactlyInstanceOf(ImageView.class);
        ImageView iconView = (ImageView) firstChildView;
        assertThat(iconView).hasDrawable(drawable);
        assertThat((LayoutParams) iconView.getLayoutParams())
                .hasLeftMargin(10)
                .hasRightMargin(20);
    }

}