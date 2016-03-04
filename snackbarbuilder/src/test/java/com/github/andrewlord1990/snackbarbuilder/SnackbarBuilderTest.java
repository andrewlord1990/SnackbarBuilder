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

package com.github.andrewlord1990.snackbarbuilder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;
import android.support.design.widget.Snackbar.SnackbarLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCallback;

import org.assertj.android.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
public class SnackbarBuilderTest {

    @Mock
    CoordinatorLayout parentView;

    @Mock
    Activity activity;

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

        when(parentView.getContext()).thenReturn(RuntimeEnvironment.application);
    }

    @Test
    public void givenView_whenCreated_thenParentViewSetCorrectly() {
        //When
        SnackbarBuilder builder = new SnackbarBuilder(parentView);

        //Then
        assertThat(builder.parentView).isEqualTo(parentView);
        assertThat(builder.context).isEqualTo(parentView.getContext());
    }

    @Test
    public void givenActivity_whenCreated_thenParentViewFoundUsingParentViewId() {
        //Given
        Activity activity = Robolectric.setupActivity(Activity.class);
        activity.setTheme(R.style.CustomAttrTheme);
        LinearLayout layout = new LinearLayout(activity);
        layout.setId(R.id.snackbar_icon);
        activity.setContentView(layout);

        //When
        SnackbarBuilder builder = new SnackbarBuilder(activity);

        //Then
        assertThat(builder.parentView).isEqualTo(layout);
        assertThat(builder.context).isEqualTo(activity);
        assertThat(builder.actionTextColor).isEqualTo(0xFF454545);
        assertThat(builder.messageTextColor).isEqualTo(0xFF987654);
    }

    @Test
    public void whenCreated_thenActionTextColorFromCustomThemeAttribute() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.CustomAttrTheme);

        //When
        SnackbarBuilder builder = new SnackbarBuilder(parentView);

        //Then
        assertThat(builder.actionTextColor).isEqualTo(0xFF454545);
    }

    @Test
    public void givenNoCustomThemeAttribute_whenCreated_thenActionTextColorFromColorAccentThemeAttribute() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.FallbackAttrTheme);

        //When
        SnackbarBuilder builder = new SnackbarBuilder(parentView);

        //Then
        assertThat(builder.actionTextColor).isEqualTo(0xFF232323);
    }

    @Test
    public void whenCreated_thenMessageTextColorFromCustomThemeAttribute() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.CustomAttrTheme);

        //When
        SnackbarBuilder builder = new SnackbarBuilder(parentView);

        //Then
        assertThat(builder.messageTextColor).isEqualTo(0xFF987654);
    }

    @Test
    public void givenNoCustomThemeAttribute_whenCreated_thenMessageTextColorFromColorAccentThemeAttribute() {
        //When
        SnackbarBuilder builder = new SnackbarBuilder(parentView);

        //Then
        assertThat(builder.messageTextColor).isEqualTo(Color.WHITE);
    }

    @Test
    public void whenCreated_thenDurationFromCustomThemeAttribute() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.CustomAttrTheme);

        //When
        SnackbarBuilder builder = new SnackbarBuilder(parentView);

        //Then
        assertThat(builder.duration).isEqualTo(Snackbar.LENGTH_INDEFINITE);
    }

    @Test
    public void givenNoCustomThemeAttribute_whenCreated_thenDurationLong() {
        //When
        SnackbarBuilder builder = new SnackbarBuilder(parentView);

        //Then
        assertThat(builder.duration).isEqualTo(Snackbar.LENGTH_LONG);
    }

    @Test
    public void whenCreated_thenBackgroundColorFromCustomThemeAttribute() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.CustomAttrTheme);

        //When
        SnackbarBuilder builder = new SnackbarBuilder(parentView);

        //Then
        assertThat(builder.backgroundColor).isEqualTo(0xFF999999);
    }

    @Test
    public void whenMessageWithString_thenMessageSet() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.message("message");

        //Then
        assertThat(builder.message).isEqualTo("message");
    }

    @Test
    public void givenSpan_whenMessage_thenMessageSet() {
        //Given
        SnackbarBuilder builder = createBuilder();
        Spannable spannable = new SpannableString("testMessage");
        spannable.setSpan(new ForegroundColorSpan(Color.CYAN), 0, spannable.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //When
        builder.message(spannable);

        //Then
        assertThat(builder.message.toString()).isEqualTo("testMessage");
        SpannableString actual = SpannableString.valueOf(builder.message);
        ForegroundColorSpan[] spans = actual.getSpans(0, spannable.length(), ForegroundColorSpan.class);
        assertThat(spans).hasSize(1);
        assertThat(spans[0].getForegroundColor()).isEqualTo(Color.CYAN);
    }

    @Test
    public void whenMessageWithStringResource_thenMessageSet() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.message(R.string.snackbar_action_undo);

        //Then
        assertThat(builder.message).isEqualTo("Undo");
    }

    @Test
    public void whenMessageTextColorRes_thenMessageTextColorSet() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.messageTextColorRes(R.color.default_message);

        //Then
        assertThat(builder.messageTextColor).isEqualTo(Color.WHITE);
    }

    @Test
    public void whenMessageTextColor_thenMessageTextColorSet() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.messageTextColor(0xFF333333);

        //Then
        assertThat(builder.messageTextColor).isEqualTo(0xFF333333);
    }

    @Test
    public void whenAppendMessageWithString_thenMessageAdded() {
        //Given
        SnackbarBuilder builder = createBuilder();
        String message = "aMessage";

        //When
        builder.appendMessage(message);

        //Then
        assertThat(builder.appendMessages.toString()).isEqualTo(message);
    }

    @Test
    public void givenMessageAlreadyAppended_whenAppendMessageWithString_thenMessageAdded() {
        //Given
        SnackbarBuilder builder = createBuilder();
        String message = "aMessage";
        String starting = "startingMessage";
        builder.appendMessage(starting);

        //When
        builder.appendMessage(message);

        //Then
        assertThat(builder.appendMessages.toString()).isEqualTo(starting + message);
    }

    @Test
    public void whenAppendMessageWithStringResource_thenMessageAdded() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.appendMessage(R.string.snackbar_action_undo);

        //Then
        assertThat(builder.appendMessages.toString()).isEqualTo("Undo");
    }

    @Test
    public void givenMessageAlreadyAppended_whenAppendMessageWithStringResource_thenMessageAdded() {
        //Given
        SnackbarBuilder builder = createBuilder();
        String starting = "startingMessage";
        builder.appendMessage(starting);

        //When
        builder.appendMessage(R.string.snackbar_action_undo);

        //Then
        assertThat(builder.appendMessages.toString()).isEqualTo("startingMessageUndo");
    }

    @Test
    public void whenAppendMessage_thenMessageWithForegroundColorAdded() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.appendMessage("message", Color.RED);

        //Then
        assertThatMessagesWithColorsAppended(builder, "message", Color.RED);
    }

    @Test
    public void givenMessageAlreadyAppended_whenAppendMessageWithColor_thenMessagesWithForegroundColorsAdded() {
        //Given
        SnackbarBuilder builder = createBuilder();
        builder.appendMessage("first", Color.BLACK);

        //When
        builder.appendMessage("second", Color.MAGENTA);

        //Then
        assertThatMessagesWithColorsAppended(builder, "firstsecond", Color.BLACK, Color.MAGENTA);
    }

    @Test
    public void whenAppendMessageResWithColorRes_thenMessageWithForegroundColorAdded() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.appendMessage(R.string.snackbar_action_undo, R.color.default_message);

        //Then
        assertThatMessagesWithColorsAppended(builder, "Undo", Color.WHITE);
    }

    private void assertThatMessagesWithColorsAppended(SnackbarBuilder builder, String expected, int... colors) {
        int length = expected.length();
        assertThat(builder.appendMessages.subSequence(0, length)).isEqualTo(expected);
        ForegroundColorSpan[] spans = builder.appendMessages
                .getSpans(0, length, ForegroundColorSpan.class);
        assertThat(spans.length).isEqualTo(colors.length);
        for (int i = 0; i < colors.length; i++) {
            assertThat(spans[i].getForegroundColor()).isEqualTo(colors[i]);
        }
    }

    @Test
    public void whenDuration_thenDurationSet() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.duration(Snackbar.LENGTH_INDEFINITE);

        //Then
        assertThat(builder.duration).isEqualTo(Snackbar.LENGTH_INDEFINITE);
    }

    @Test
    public void whenActionTextColorRes_thenActionTextColorSet() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.actionTextColorRes(R.color.default_message);

        //Then
        assertThat(builder.actionTextColor).isEqualTo(Color.WHITE);
    }

    @Test
    public void whenActionTextColor_thenActionTextColorSet() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.actionTextColor(0xFF333333);

        //Then
        assertThat(builder.actionTextColor).isEqualTo(0xFF333333);
    }

    @Test
    public void whenActionTextWithString_thenActionTextSet() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.actionText("text");

        //Then
        assertThat(builder.actionText).isEqualTo("text");
    }

    @Test
    public void whenActionTextWithStringResource_thenActionTextSet() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.actionText(R.string.snackbar_action_undo);

        //Then
        assertThat(builder.actionText).isEqualTo("Undo");
    }

    @Test
    public void whenBackgroundColorRes_thenBackgroundColorSet() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.backgroundColorRes(R.color.default_message);

        //Then
        assertThat(builder.backgroundColor).isEqualTo(Color.WHITE);
    }

    @Test
    public void whenBackgroundColor_thenBackgroundColorSet() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.backgroundColor(0xFF333333);

        //Then
        assertThat(builder.backgroundColor).isEqualTo(0xFF333333);
    }

    @Test
    public void whenActionClickListener_thenActionClickListenerSet() {
        //Given
        SnackbarBuilder builder = createBuilder();
        OnClickListener click = new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click
            }
        };

        //When
        builder.actionClickListener(click);

        //Then
        assertThat(builder.actionClickListener).isEqualTo(click);
    }

    @Test
    public void whenCallback_thenCallbackSet() {
        //Given
        SnackbarBuilder builder = createBuilder();
        Callback callback = new Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
            }
        };

        //When
        builder.callback(callback);

        //Then
        assertThat(builder.callback).isEqualTo(callback);
    }

    @Test
    public void whenSnackbarCallback_thenSnackbarCallbackSet() {
        //Given
        SnackbarBuilder builder = createBuilder();
        SnackbarCallback callback = new SnackbarCallback();

        //When
        builder.snackbarCallback(callback);

        //Then
        assertThat(builder.snackbarCallback).isEqualTo(callback);
    }

    @Test
    public void whenLowercaseAction_thenActionAllCapsFalse() {
        //Given
        SnackbarBuilder builder = createBuilder();

        //When
        builder.lowercaseAction();

        //Then
        assertThat(builder.actionAllCaps).isFalse();
    }

    @Test
    public void whenIconWithDrawableResource_thenIconSet() {
        //Given
        SnackbarBuilder builder = createBuilder();
        @DrawableRes int drawableResId = 50;
        getResourceCreator(builder).createMockDrawableResId(drawableResId, drawable);

        //When
        builder.icon(drawableResId);

        //Then
        assertThat(builder.icon).isEqualTo(drawable);
    }

    @Test
    public void whenIconWithDrawable_thenIconSet() {
        //Given
        SnackbarBuilder builder = createBuilder();
        @DrawableRes int drawableResId = 50;
        getResourceCreator(builder).createMockDrawableResId(drawableResId, drawable);

        //When
        builder.icon(drawable);

        //Then
        assertThat(builder.icon).isEqualTo(drawable);
    }

    @Test
    public void whenIconMarginStartPixels_thenIconMarinStartSet() {
        //Given
        SnackbarBuilder builder = createBuilder();
        int iconMarginStart = 100;

        //When
        builder.iconMarginStartPixels(iconMarginStart);

        //Then
        assertThat(builder.iconMarginStartPixels).isEqualTo(iconMarginStart);
    }

    @Test
    public void whenIconMarginStart_thenIconMarinStartSet() {
        //Given
        SnackbarBuilder builder = createBuilder();
        int iconMarginStart = 100;
        @DimenRes int dimenResId = getResourceCreator(builder)
                .getDimensionPixelSize(iconMarginStart);

        //When
        builder.iconMarginStart(dimenResId);

        //Then
        assertThat(builder.iconMarginStartPixels).isEqualTo(iconMarginStart);
    }

    @Test
    public void whenIconMarginEndPixels_thenIconMarinEndSet() {
        //Given
        SnackbarBuilder builder = createBuilder();
        int iconMarginEnd = 100;

        //When
        builder.iconMarginEndPixels(iconMarginEnd);

        //Then
        assertThat(builder.iconMarginEndPixels).isEqualTo(iconMarginEnd);
    }

    @Test
    public void whenIconMarginEnd_thenIconMarinStartSet() {
        //Given
        SnackbarBuilder builder = createBuilder();
        int iconMarginEnd = 100;
        @DimenRes int dimenResId = getResourceCreator(builder)
                .getDimensionPixelSize(iconMarginEnd);

        //When
        builder.iconMarginEnd(dimenResId);

        //Then
        assertThat(builder.iconMarginEndPixels).isEqualTo(iconMarginEnd);
    }

    @Test
    public void WhenBuildWrapper_thenSnackbarWrapperSetup() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.AppTheme);
        CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);

        //When
        SnackbarWrapper wrapper = new SnackbarBuilder(parent)
                .message("message")
                .actionText("action")
                .duration(Snackbar.LENGTH_SHORT)
                .buildWrapper();

        //Then
        assertThat(wrapper.getText()).isEqualTo("message");
        assertThat(wrapper.getActionText()).isEqualTo("action");
        assertThat(wrapper.getDuration()).isEqualTo(Snackbar.LENGTH_SHORT);
    }

    @Test
    @TargetApi(11)
    public void whenBuild_thenSnackbarSetup() {
        //Given
        int messageTextColor = 0xFF111111;
        int actionTextColor = 0xFF999999;
        String message = "message";
        String action = "action";
        RuntimeEnvironment.application.setTheme(R.style.AppTheme);
        CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);

        //When
        Snackbar snackbar = new SnackbarBuilder(parent)
                .messageTextColor(messageTextColor)
                .actionTextColor(actionTextColor)
                .message(message)
                .actionText(action)
                .duration(Snackbar.LENGTH_INDEFINITE)
                .backgroundColor(0xFF777777)
                .lowercaseAction()
                .build();

        //Then
        assertThat(snackbar.getDuration()).isEqualTo(Snackbar.LENGTH_INDEFINITE);

        Assertions.assertThat((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
                .hasCurrentTextColor(messageTextColor)
                .hasText(message);

        Assertions.assertThat((ColorDrawable) snackbar.getView().getBackground())
                .hasColor(0xFF777777);

        Button button = (Button) snackbar.getView().findViewById(R.id.snackbar_action);
        Assertions.assertThat(button).hasCurrentTextColor(actionTextColor);
        button.performClick();
        assertThat(button.getTransformationMethod()).isNull();
    }

    @Test
    public void givenCallback_whenBuild_thenCallbackSet() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.AppTheme);
        CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);

        //When
        Snackbar snackbar = new SnackbarBuilder(parent)
                .message("message")
                .actionText("action")
                .duration(Snackbar.LENGTH_SHORT)
                .callback(callback)
                .build();
        snackbar.show();

        //Then
        snackbar.dismiss();
        verify(callback).onDismissed(snackbar, Callback.DISMISS_EVENT_MANUAL);
    }

    @Test
    public void givenSnackbarCallback_whenBuild_thenSnackbarCallbackSet() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.AppTheme);
        CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);

        //When
        Snackbar snackbar = new SnackbarBuilder(parent)
                .message("message")
                .actionText("action")
                .duration(Snackbar.LENGTH_SHORT)
                .snackbarCallback(snackbarCallback)
                .build();
        snackbar.show();

        //Then
        snackbar.dismiss();
        verify(snackbarCallback).onSnackbarManuallyDismissed(snackbar);
    }

    @Test
    public void givenNotLowercaseAction_whenBuild_thenActionAllCaps() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.AppTheme);
        CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);

        //When
        Snackbar snackbar = new SnackbarBuilder(parent)
                .message("message")
                .actionText("action")
                .duration(Snackbar.LENGTH_SHORT)
                .build();
        snackbar.show();

        //Then
        Button button = (Button) snackbar.getView().findViewById(R.id.snackbar_action);
        assertThat(button.getTransformationMethod()).isNotNull();
    }

    @Test
    public void givenIcon_whenBuild_thenIconAddedToSnackbar() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.AppTheme);
        CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);

        //When
        Snackbar snackbar = new SnackbarBuilder(parent)
                .message("messsage")
                .icon(drawable)
                .iconMarginStartPixels(10)
                .iconMarginEndPixels(20)
                .build();

        //Then
        SnackbarLayout layout = (SnackbarLayout) snackbar.getView();
        View firstChildView = layout.getChildAt(0);
        assertThat(firstChildView).isExactlyInstanceOf(ImageView.class);
        ImageView iconView = (ImageView) firstChildView;
        assertThat(iconView.getDrawable()).isEqualTo(drawable);
        SnackbarLayout.LayoutParams layoutParams = (LayoutParams) iconView.getLayoutParams();
        assertThat(layoutParams.leftMargin).isEqualTo(10);
        assertThat(layoutParams.rightMargin).isEqualTo(20);
    }

    @Test
    public void givenAppendedMessages_whenBuild_thenMessagesAppendedToMainMessage() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.AppTheme);
        CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);

        //When
        Snackbar snackbar = new SnackbarBuilder(parent)
                .message("start")
                .appendMessage("first_added")
                .appendMessage("second_in_blue", Color.BLUE)
                .appendMessage("third_in_dark_grey", Color.DKGRAY)
                .build();

        //Then
        List<Pair<String, Integer>> expected = new ArrayList<>();
        expected.add(new Pair<>("start", 0));
        expected.add(new Pair<>("first_added", 0));
        expected.add(new Pair<>("second_in_blue", Color.BLUE));
        expected.add(new Pair<>("third_in_dark_grey", Color.DKGRAY));
        SnackbarCustomAssert.assertThat(snackbar).hasMessagesAppended(expected);
    }

    private SnackbarBuilder createBuilder() {
        RuntimeEnvironment.application.setTheme(R.style.AppTheme);
        return new SnackbarBuilder(parentView);
    }

    private MockResourceCreator getResourceCreator(SnackbarBuilder builder) {
        return MockResourceCreator.fromBuilder(builder)
                .withContext(activity)
                .withResources(resources);
    }

}