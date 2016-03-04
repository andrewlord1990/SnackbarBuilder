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

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;
import android.support.design.widget.Snackbar.SnackbarLayout;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCallback;

import org.assertj.android.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
public class SnackbarWrapperTest {

    private Snackbar snackbar;
    private SnackbarWrapper wrapper;
    private MockResourceCreator resourceCreator;

    @Mock
    Context context;

    @Mock
    Resources resources;

    @Mock
    Callback callback;

    @Mock
    SnackbarCallback snackbarCallback;

    @Mock
    Drawable drawable;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        RuntimeEnvironment.application.setTheme(R.style.AppTheme);
        CoordinatorLayout layout = new CoordinatorLayout(RuntimeEnvironment.application);
        snackbar = Snackbar.make(layout, "SomeText", Snackbar.LENGTH_LONG);
        wrapper = new SnackbarWrapper(snackbar);
        resourceCreator = MockResourceCreator
                .fromWrapper(wrapper)
                .withContext(context)
                .withResources(resources);
    }

    @Test
    public void whenGetSnackbar_thenWrappedSnackbarReturned() {
        //When
        Snackbar actual = wrapper.getSnackbar();

        //Then
        assertThat(actual).isEqualTo(snackbar);
    }

    @Test
    public void whenSetActionTextWithResource_thenActionTextUpdated() {
        //When
        wrapper.setActionText(R.string.snackbar_action_undo);

        //Then
        Assertions.assertThat(getActionView()).hasText("Undo");
    }

    @Test
    public void whenSetActionText_thenActionTextUpdated() {
        //Given
        String actionText = "someAction";

        //When
        wrapper.setActionText(actionText);

        //Then
        assertThat(wrapper.getActionText()).isEqualTo(actionText);
    }

    @Test
    public void whenSetActionClickListener_thenActionClickListenerSet() {
        //Given
        TestClickListener clickListener = new TestClickListener();

        //When
        wrapper.setActionClickListener(clickListener);
        getActionView().performClick();

        //Then
        assertThat(clickListener.isClicked()).isTrue();
    }

    @Test
    public void givenNullClickListener_whenSetActionWithResource_thenActionHidden() {
        //When
        wrapper.setAction(R.string.snackbar_action_undo, null);

        //Then
        Assertions.assertThat(getActionView()).isNotVisible();
    }

    @Test
    public void givenClickListener_whenSetActionWithResource_thenActionSetAndShown() {
        //Given
        TestClickListener clickListener = new TestClickListener();

        //When
        wrapper.setAction(R.string.snackbar_action_undo, clickListener);

        //Then
        Assertions.assertThat(getActionView())
                .isVisible()
                .hasText("Undo");
        getActionView().performClick();
        assertThat(clickListener.isClicked()).isTrue();
    }

    @Test
    public void givenNullClickListener_whenSetAction_thenActionHidden() {
        //When
        wrapper.setAction("Some text", null);

        //Then
        Assertions.assertThat(getActionView()).isNotVisible();
    }

    @Test
    public void givenClickListener_whenSetAction_thenActionSetAndShown() {
        //Given
        TestClickListener clickListener = new TestClickListener();
        String actionText = "some action";

        //When
        wrapper.setAction(actionText, clickListener);

        //Then
        Assertions.assertThat(getActionView())
                .isVisible()
                .hasText(actionText);
        getActionView().performClick();
        assertThat(clickListener.isClicked()).isTrue();
    }

    @Test
    public void whenSetActionTextColorWithStateList_thenActionTextColorSet() {
        //Given
        ColorStateList stateList = createColorStateList();

        //When
        wrapper.setActionTextColor(stateList);

        //Then
        assertThat(wrapper.getActionTextColors())
                .isEqualTo(stateList)
                .isEqualTo(getActionView().getTextColors());
    }

    @Test
    public void whenSetActionTextColorWithColor_thenActionTextColorSet() {
        //When
        wrapper.setActionTextColor(Color.GREEN);

        //Then
        assertThat(wrapper.getActionCurrentTextColor()).isEqualTo(Color.GREEN);
    }

    @Test
    public void whenSetActionTextColorResId_thenActionTextColorSet() {
        //When
        wrapper.setActionTextColorResId(R.color.default_message);

        //Then
        assertThat(wrapper.getActionCurrentTextColor())
                .isEqualTo(Color.WHITE)
                .isEqualTo(getActionView().getCurrentTextColor());
    }

    @Test
    public void whenSetActionVisibility_thenActionVisibilityChanged() {
        //Given
        getActionView().setVisibility(View.GONE);

        //When
        wrapper.setActionVisibility(View.VISIBLE);

        //Then
        assertThat(wrapper.getActionVisibility())
                .isEqualTo(View.VISIBLE)
                .isEqualTo(getActionView().getVisibility());
    }

    @Test
    public void whenSetLowercaseActionText_thenActionNotAllCaps() {
        //Given
        Compatibility.getInstance().setAllCaps(getActionView(), true);

        //When
        wrapper.setLowercaseActionText();

        //Then
        assertThat(getActionView().getTransformationMethod()).isNull();
    }

    @Test
    public void whenSetUppercaseActionText_thenActionAllCaps() {
        //Given
        Compatibility.getInstance().setAllCaps(getActionView(), false);

        //When
        wrapper.setUppercaseActionText();

        //Then
        assertThat(getActionView().getTransformationMethod()).isNotNull();
    }

    @Test
    public void whenSetTextWithString_thenMessageSet() {
        //Given
        String message = "some message to set";

        //When
        wrapper.setText(message);

        //Then
        assertThat(wrapper.getText())
                .isEqualTo(message)
                .isEqualTo(getMessageView().getText().toString());
    }

    @Test
    public void whenSetTextWithResource_thenMessageSet() {
        //When
        wrapper.setText(R.string.snackbar_action_undo);

        //Then
        assertThat(wrapper.getText())
                .isEqualTo("Undo")
                .isEqualTo(getMessageView().getText().toString());
    }

    @Test
    public void whenSetTextColorWithColor_thenMessageColorSet() {
        //When
        wrapper.setTextColor(Color.RED);

        //Then
        assertThat(wrapper.getCurrentTextColor())
                .isEqualTo(Color.RED)
                .isEqualTo(getMessageView().getCurrentTextColor());
    }

    @Test
    public void whenSetTextColorWithStateList_thenMessageColorSet() {
        //Given
        ColorStateList stateList = createColorStateList();

        //When
        wrapper.setTextColor(stateList);

        //Then
        assertThat(wrapper.getTextColors())
                .isEqualTo(stateList)
                .isEqualTo(getMessageView().getTextColors());
    }

    @Test
    public void whenSetTextColorResId_thenMessageColorSet() {
        //When
        wrapper.setTextColorResId(R.color.default_message);

        //Then
        assertThat(wrapper.getCurrentTextColor())
                .isEqualTo(Color.WHITE)
                .isEqualTo(getMessageView().getCurrentTextColor());
    }

    @Test
    public void whenAppendMessageWithString_thenTextAppendedToMessage() {
        //Given
        wrapper.setText("startingText");

        //When
        wrapper.appendMessage("MORE");

        //Then
        assertThat(wrapper.getText()).isEqualTo("startingTextMORE");
    }

    @Test
    public void whenAppendMessageWithResource_thenTextAppendedToMessage() {
        //Given
        wrapper.setText("startingText");

        //When
        wrapper.appendMessage(R.string.snackbar_action_undo);

        //Then
        assertThat(wrapper.getText()).isEqualTo("startingTextUndo");
    }

    @Test
    public void whenAppendMessagesWithColors_thenTextWithColorsAppendedToMessage() {
        //When
        wrapper.setText("start")
                .appendMessage(R.string.snackbar_action_undo, R.color.default_message)
                .appendMessage("second_in_blue", Color.BLUE)
                .appendMessage("third_in_dark_grey", Color.DKGRAY);

        //Then
        List<Pair<String, Integer>> expected = new ArrayList<>();
        expected.add(new Pair<>("start", 0));
        expected.add(new Pair<>("Undo", Color.WHITE));
        expected.add(new Pair<>("second_in_blue", Color.BLUE));
        expected.add(new Pair<>("third_in_dark_grey", Color.DKGRAY));
        SnackbarCustomAssert.assertThat(snackbar).hasMessagesAppended(expected);
    }

    @Test
    public void whenSetMessageVisibility_thenMessageVisibilityChanged() {
        //Given
        getMessageView().setVisibility(View.GONE);

        //When
        wrapper.setMessageVisibility(View.VISIBLE);

        //Then
        assertThat(wrapper.getMessageVisibility())
                .isEqualTo(View.VISIBLE)
                .isEqualTo(getMessageView().getVisibility());
    }

    @Test
    public void whenSetDuration_thenDurationSet() {
        //Given
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);

        //When
        wrapper.setDuration(Snackbar.LENGTH_LONG);

        //Then
        assertThat(wrapper.getDuration())
                .isEqualTo(Snackbar.LENGTH_LONG)
                .isEqualTo(snackbar.getDuration());
    }

    @Test
    public void whenGetView_thenSnackbarView() {
        assertThat(wrapper.getView()).isEqualTo(snackbar.getView());
    }

    @Test
    public void whenSetBackgroundColor_thenBackgroundColorSet() {
        //When
        wrapper.setBackgroundColor(Color.MAGENTA);

        //Then
        Assertions.assertThat((ColorDrawable) wrapper.getView().getBackground())
                .hasColor(Color.MAGENTA);
    }

    @Test
    public void whenSetBackgroundColorResId_thenBackgroundColorSet() {
        //When
        wrapper.setBackgroundColorResId(R.color.default_message);

        //Then
        Assertions.assertThat((ColorDrawable) wrapper.getView().getBackground())
                .hasColor(Color.WHITE);
    }

    @Test
    public void whenSetCallback_thenCallbackSet() {
        //When
        wrapper.setCallback(callback);
        wrapper.show();

        //Then
        wrapper.dismiss();
        verify(callback).onDismissed(snackbar, Callback.DISMISS_EVENT_MANUAL);
    }

    @Test
    public void whenSetSnackbarCallback_thenSnackbarCallbackSet() {
        //When
        wrapper.setSnackbarCallback(snackbarCallback);
        wrapper.show();

        //Then
        wrapper.dismiss();
        verify(snackbarCallback).onSnackbarManuallyDismissed(snackbar);
    }

    @Test
    public void whenSetIconWithResource_thenIconSet() {
        //Given
        @DrawableRes int drawableResId = 50;
        resourceCreator.createMockDrawableResId(drawableResId, drawable);

        //When
        wrapper.setIcon(drawableResId);

        //Then
        assertThat(getIconView().getDrawable()).isEqualTo(drawable);
    }

    @Test
    public void whenSetIconWithDrawable_thenIconSet() {
        //When
        wrapper.setIcon(drawable);

        //Then
        assertThat(getIconView().getDrawable()).isEqualTo(drawable);
    }

    @Test
    public void whenSetIconMarginsWithResource_thenIconMarginsSet() {
        //Given
        Resources resources = RuntimeEnvironment.application.getResources();
        int start = resources.getDimensionPixelSize(R.dimen.icon_margin_end_default);
        int end = resources.getDimensionPixelSize(R.dimen.icon_margin_start_default);

        //When
        wrapper.setIconMarginStart(R.dimen.icon_margin_end_default)
                .setIconMarginEnd(R.dimen.icon_margin_start_default);

        //Then
        assertThatIconMarginsEqualTo(start, end);
    }

    @Test
    public void whenSetIconMarginsWithPixels_thenIconMarginsSet() {
        //Given
        int start = 100;
        int end = 20;

        //When
        wrapper.setIconMarginStartPixels(start)
                .setIconMarginEndPixels(end);

        //Then
        assertThatIconMarginsEqualTo(start, end);
    }

    @Test
    public void whenShow_thenSnackbarShown() {
        //When
        wrapper.show();

        //Then
        assertThat(wrapper.isShown())
                .isTrue()
                .isEqualTo(snackbar.isShown());
        assertThat(wrapper.isShownOrQueued())
                .isTrue()
                .isEqualTo(snackbar.isShownOrQueued());
    }

    @Test
    public void whenDismiss_thenSnackbarDismissed() {
        //Given
        wrapper.setSnackbarCallback(snackbarCallback);
        wrapper.show();

        //When
        wrapper.dismiss();

        //Then
        assertThat(wrapper.isShown()).isFalse();
        verify(snackbarCallback).onSnackbarManuallyDismissed(snackbar);
    }

    private TextView getMessageView() {
        return (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
    }

    private Button getActionView() {
        return (Button) snackbar.getView().findViewById(R.id.snackbar_action);
    }

    private ImageView getIconView() {
        return (ImageView) snackbar.getView().findViewById(R.id.snackbar_icon);
    }

    private ColorStateList createColorStateList() {
        int[] state0 = new int[]{0, 1};
        int[] state1 = new int[]{2, 3};
        int[][] states = new int[][]{state0, state1};
        int[] colors = new int[]{Color.RED, Color.CYAN};
        return new ColorStateList(states, colors);
    }

    private void assertThatIconMarginsEqualTo(int leftMargin, int rightMargin) {
        SnackbarLayout.LayoutParams layoutParams = (LayoutParams) getIconView().getLayoutParams();
        assertThat(layoutParams.leftMargin).isEqualTo(leftMargin);
        assertThat(layoutParams.rightMargin).isEqualTo(rightMargin);
    }

}