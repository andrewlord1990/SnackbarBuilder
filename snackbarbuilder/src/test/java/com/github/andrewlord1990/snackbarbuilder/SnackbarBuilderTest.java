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
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
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

    private SnackbarBuilder builderUnderTest;

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
        layout.setId(R.id.test_id);
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
        createBuilder();

        //When
        builderUnderTest.message("message");

        //Then
        assertThat(builderUnderTest.message).isEqualTo("message");
    }

    @Test
    public void givenSpan_whenMessage_thenMessageSet() {
        //Given
        createBuilder();
        Spannable spannable = new SpannableString("testMessage");
        spannable.setSpan(new ForegroundColorSpan(Color.CYAN), 0, spannable.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //When
        builderUnderTest.message(spannable);

        //Then
        assertThat(builderUnderTest.message.toString()).isEqualTo("testMessage");
        SpannableString actual = SpannableString.valueOf(builderUnderTest.message);
        ForegroundColorSpan[] spans = actual.getSpans(0, spannable.length(), ForegroundColorSpan.class);
        assertThat(spans).hasSize(1);
        assertThat(spans[0].getForegroundColor()).isEqualTo(Color.CYAN);
    }

    @Test
    public void whenMessageWithStringResource_thenMessageSet() {
        //Given
        createBuilder();
        @StringRes int stringResId = getStringResourceId("Test");

        //When
        builderUnderTest.message(stringResId);

        //Then
        assertThat(builderUnderTest.message).isEqualTo("Test");
    }

    @Test
    public void whenMessageTextColorRes_thenMessageTextColorSet() {
        //Given
        createBuilder();
        @ColorRes int colorResId = getColorResourceId(0xFF444444);

        //When
        builderUnderTest.messageTextColorRes(colorResId);

        //Then
        assertThat(builderUnderTest.messageTextColor).isEqualTo(0xFF444444);
    }

    @Test
    public void whenMessageTextColor_thenMessageTextColorSet() {
        //Given
        createBuilder();

        //When
        builderUnderTest.messageTextColor(0xFF333333);

        //Then
        assertThat(builderUnderTest.messageTextColor).isEqualTo(0xFF333333);
    }

    @Test
    public void whenAppendMessageWithString_thenMessageAdded() {
        //Given
        createBuilder();
        String message = "aMessage";

        //When
        builderUnderTest.appendMessage(message);

        //Then
        assertThat(builderUnderTest.appendMessages.toString()).isEqualTo(message);
    }

    @Test
    public void givenMessageAlreadyAppended_whenAppendMessageWithString_thenMessageAdded() {
        //Given
        createBuilder();
        String message = "aMessage";
        String starting = "startingMessage";
        builderUnderTest.appendMessage(starting);

        //When
        builderUnderTest.appendMessage(message);

        //Then
        assertThat(builderUnderTest.appendMessages.toString()).isEqualTo(starting + message);
    }

    @Test
    public void whenAppendMessageWithStringResource_thenMessageAdded() {
        //Given
        createBuilder();
        @StringRes int message = getStringResourceId("testMessage");

        //When
        builderUnderTest.appendMessage(message);

        //Then
        assertThat(builderUnderTest.appendMessages.toString()).isEqualTo("testMessage");
    }

    @Test
    public void givenMessageAlreadyAppended_whenAppendMessageWithStringResource_thenMessageAdded() {
        //Given
        createBuilder();
        @StringRes int message = getStringResourceId("testMessage");
        String starting = "startingMessage";
        builderUnderTest.appendMessage(starting);

        //When
        builderUnderTest.appendMessage(message);

        //Then
        assertThat(builderUnderTest.appendMessages.toString()).isEqualTo("startingMessagetestMessage");
    }

    @Test
    public void whenAppendMessageWithColor_thenMessageWithForegroundColorAdded() {
        //Given
        createBuilder();

        //When
        builderUnderTest.appendMessageWithColor("message", Color.RED);

        //Then
        assertThatMessagesWithColorsAppended("message", Color.RED);
    }

    @Test
    public void givenMessageAlreadyAppended_whenAppendMessageWithColor_thenMessagesWithForegroundColorsAdded() {
        //Given
        createBuilder();
        builderUnderTest.appendMessageWithColor("first", Color.BLACK);

        //When
        builderUnderTest.appendMessageWithColor("second", Color.MAGENTA);

        //Then
        assertThatMessagesWithColorsAppended("firstsecond", Color.BLACK, Color.MAGENTA);
    }

    @Test
    public void whenAppendMessageWithColorRes_thenMessageWithForegroundColorAdded() {
        //Given
        createBuilder();
        @ColorRes int colorResId = getColorResourceId(Color.BLUE);

        //When
        builderUnderTest.appendMessageWithColorRes("blahblah", colorResId);

        //Then
        assertThatMessagesWithColorsAppended("blahblah", Color.BLUE);
    }

    @Test
    public void whenAppendMessageResWithColor_thenMessageWithForegroundColorAdded() {
        //Given
        createBuilder();
        @StringRes int messageResId = getStringResourceId("testingtimewithaprettylongstring");

        //When
        builderUnderTest.appendMessageResWithColor(messageResId, Color.RED);

        //Then
        assertThatMessagesWithColorsAppended("testingtimewithaprettylongstring", Color.RED);
    }

    @Test
    public void whenAppendMessageResWithColorRes_thenMessageWithForegroundColorAdded() {
        //Given
        createBuilder();
        @StringRes int messageResId = getStringResourceId("thisisanotherstring");
        @ColorRes int colorResId = getColorResourceId(Color.CYAN);

        //When
        builderUnderTest.appendMessageResWithColorRes(messageResId, colorResId);

        //Then
        assertThatMessagesWithColorsAppended("thisisanotherstring", Color.CYAN);
    }

    private void assertThatMessagesWithColorsAppended(String expected, int... colors) {
        int length = expected.length();
        assertThat(builderUnderTest.appendMessages.subSequence(0, length)).isEqualTo(expected);
        ForegroundColorSpan[] spans = builderUnderTest.appendMessages
                .getSpans(0, length, ForegroundColorSpan.class);
        assertThat(spans.length).isEqualTo(colors.length);
        for (int i = 0; i < colors.length; i++) {
            assertThat(spans[i].getForegroundColor()).isEqualTo(colors[i]);
        }
    }

    @Test
    public void whenDuration_thenDurationSet() {
        //Given
        createBuilder();

        //When
        builderUnderTest.duration(Snackbar.LENGTH_INDEFINITE);

        //Then
        assertThat(builderUnderTest.duration).isEqualTo(Snackbar.LENGTH_INDEFINITE);
    }

    @Test
    public void whenActionTextColorRes_thenActionTextColorSet() {
        //Given
        createBuilder();
        @ColorRes int colorResId = getColorResourceId(0xFF444444);

        //When
        builderUnderTest.actionTextColorRes(colorResId);

        //Then
        assertThat(builderUnderTest.actionTextColor).isEqualTo(0xFF444444);
    }

    @Test
    public void whenActionTextColor_thenActionTextColorSet() {
        //Given
        createBuilder();

        //When
        builderUnderTest.actionTextColor(0xFF333333);

        //Then
        assertThat(builderUnderTest.actionTextColor).isEqualTo(0xFF333333);
    }

    @Test
    public void whenActionTextWithString_thenActionTextSet() {
        //Given
        createBuilder();

        //When
        builderUnderTest.actionText("text");

        //Then
        assertThat(builderUnderTest.actionText).isEqualTo("text");
    }

    @Test
    public void whenActionTextWithStringResource_thenActionTextSet() {
        //Given
        createBuilder();
        @StringRes int stringResId = getStringResourceId("Test");

        //When
        builderUnderTest.actionText(stringResId);

        //Then
        assertThat(builderUnderTest.actionText).isEqualTo("Test");
    }

    @Test
    public void whenBackgroundColorRes_thenBackgroundColorSet() {
        //Given
        createBuilder();
        @ColorRes int colorResId = getColorResourceId(0xFF444444);

        //When
        builderUnderTest.backgroundColorRes(colorResId);

        //Then
        assertThat(builderUnderTest.backgroundColor).isEqualTo(0xFF444444);
    }

    @Test
    public void whenBackgroundColor_thenBackgroundColorSet() {
        //Given
        createBuilder();

        //When
        builderUnderTest.backgroundColor(0xFF333333);

        //Then
        assertThat(builderUnderTest.backgroundColor).isEqualTo(0xFF333333);
    }

    @Test
    public void whenActionClickListener_thenActionClickListenerSet() {
        //Given
        createBuilder();
        OnClickListener click = new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click
            }
        };

        //When
        builderUnderTest.actionClickListener(click);

        //Then
        assertThat(builderUnderTest.actionClickListener).isEqualTo(click);
    }

    @Test
    public void whenCallback_thenCallbackSet() {
        //Given
        createBuilder();
        Callback callback = new Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
            }
        };

        //When
        builderUnderTest.callback(callback);

        //Then
        assertThat(builderUnderTest.callback).isEqualTo(callback);
    }

    @Test
    public void whenSnackbarCallback_thenSnackbarCallbackSet() {
        //Given
        createBuilder();
        SnackbarCallback callback = new SnackbarCallback();

        //When
        builderUnderTest.snackbarCallback(callback);

        //Then
        assertThat(builderUnderTest.snackbarCallback).isEqualTo(callback);
    }

    @Test
    public void whenLowercaseAction_thenActionAllCapsFalse() {
        //Given
        createBuilder();

        //When
        builderUnderTest.lowercaseAction();

        //Then
        assertThat(builderUnderTest.actionAllCaps).isFalse();
    }

    @Test
    public void whenIconWithDrawableResource_thenIconSet() {
        //Given
        createBuilder();
        @DrawableRes int drawableResId = 50;
        Drawable testDrawable = getDrawable(drawableResId);

        //When
        builderUnderTest.icon(drawableResId);

        //Then
        assertThat(builderUnderTest.icon).isEqualTo(testDrawable);
    }

    @Test
    public void whenIconWithDrawable_thenIconSet() {
        //Given
        createBuilder();
        @DrawableRes int drawableResId = 50;
        Drawable testDrawable = getDrawable(drawableResId);

        //When
        builderUnderTest.icon(testDrawable);

        //Then
        assertThat(builderUnderTest.icon).isEqualTo(testDrawable);
    }

    @Test
    public void whenIconMarginStartPixels_thenIconMarinStartSet() {
        //Given
        createBuilder();
        int iconMarginStart = 100;

        //When
        builderUnderTest.iconMarginStartPixels(iconMarginStart);

        //Then
        assertThat(builderUnderTest.iconMarginStartPixels).isEqualTo(iconMarginStart);
    }

    @Test
    public void whenIconMarginStart_thenIconMarinStartSet() {
        //Given
        createBuilder();
        int iconMarginStart = 100;
        @DimenRes int dimenResId = getDimensionPixelSize(iconMarginStart);

        //When
        builderUnderTest.iconMarginStart(dimenResId);

        //Then
        assertThat(builderUnderTest.iconMarginStartPixels).isEqualTo(iconMarginStart);
    }

    @Test
    public void whenIconMarginEndPixels_thenIconMarinEndSet() {
        //Given
        createBuilder();
        int iconMarginEnd = 100;

        //When
        builderUnderTest.iconMarginEndPixels(iconMarginEnd);

        //Then
        assertThat(builderUnderTest.iconMarginEndPixels).isEqualTo(iconMarginEnd);
    }

    @Test
    public void whenIconMarginEnd_thenIconMarinStartSet() {
        //Given
        createBuilder();
        int iconMarginEnd = 100;
        @DimenRes int dimenResId = getDimensionPixelSize(iconMarginEnd);

        //When
        builderUnderTest.iconMarginEnd(dimenResId);

        //Then
        assertThat(builderUnderTest.iconMarginEndPixels).isEqualTo(iconMarginEnd);
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

        TextView textView = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        assertThat(textView.getCurrentTextColor()).isEqualTo(messageTextColor);
        assertThat(textView.getText().toString()).isEqualTo(message);

        ColorDrawable backgroundColor = (ColorDrawable) snackbar.getView().getBackground();
        assertThat(backgroundColor.getColor()).isEqualTo(0xFF777777);

        Button button = (Button) snackbar.getView().findViewById(R.id.snackbar_action);
        assertThat(button.getCurrentTextColor()).isEqualTo(actionTextColor);
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
                .appendMessageWithColor("second_in_blue", Color.BLUE)
                .appendMessageWithColor("third_in_dark_grey", Color.DKGRAY)
                .build();

        //Then
        List<Pair<String, Integer>> expected = new ArrayList<>();
        expected.add(new Pair<>("start", 0));
        expected.add(new Pair<>("first_added", 0));
        expected.add(new Pair<>("second_in_blue", Color.BLUE));
        expected.add(new Pair<>("third_in_dark_grey", Color.DKGRAY));
        assertThatMessagesWithColorsAppended(snackbar, expected);
    }

    private void assertThatMessagesWithColorsAppended(Snackbar snackbar,
                                                      List<Pair<String, Integer>> expected) {
        TextView messageView = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        SpannableString message = SpannableString.valueOf(messageView.getText());
        int size = expected.size();
        for (int i = 0; i < size; i++) {
            Pair<String, Integer> pair = expected.get(i);
            int length = pair.first.length();
            assertThat(message.subSequence(0, length).toString()).isEqualTo(pair.first);
            ForegroundColorSpan[] spans = message
                    .getSpans(0, length, ForegroundColorSpan.class);
            if (pair.second == 0) {
                assertThat(spans).isEmpty();
            } else {
                assertThat(spans).hasSize(1);
                assertThat(spans[0].getForegroundColor()).isEqualTo(pair.second);
            }
            message = SpannableString.valueOf(message.subSequence(length, message.length()));
        }
    }

    private void createBuilder() {
        RuntimeEnvironment.application.setTheme(R.style.AppTheme);
        builderUnderTest = new SnackbarBuilder(parentView);
    }

    @ColorRes
    private int getColorResourceId(@ColorInt int color) {
        setMockContext();
        @ColorRes int colorResId = 50;
        when(resources.getColor(colorResId)).thenReturn(color);
        return colorResId;
    }

    @StringRes
    private int getStringResourceId(String string) {
        setMockContext();
        @StringRes int stringResId = 50;
        when(activity.getString(stringResId)).thenReturn(string);
        return stringResId;
    }

    @TargetApi(21)
    private Drawable getDrawable(@DrawableRes int drawableResId) {
        setMockContext();
        when(activity.getDrawable(drawableResId)).thenReturn(drawable);
        return drawable;
    }

    @DimenRes
    private int getDimensionPixelSize(int pixels) {
        setMockContext();
        @DimenRes int dimenResId = 50;
        when(resources.getDimensionPixelSize(dimenResId)).thenReturn(pixels);
        return dimenResId;
    }

    private void setMockContext() {
        if (builderUnderTest != null) {
            builderUnderTest.context = activity;
        }
        when(activity.getResources()).thenReturn(resources);
    }

}