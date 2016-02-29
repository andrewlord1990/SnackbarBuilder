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
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
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
    @TargetApi(21)
    public void whenIconWithDrawableResource_thenIconSet() {
        //Given
        createBuilder();
        builderUnderTest.context = activity;
        Drawable testDrawable = mock(Drawable.class);
        @DrawableRes int drawableResId = 50;
        when(activity.getDrawable(drawableResId)).thenReturn(testDrawable);

        //When
        builderUnderTest.icon(drawableResId);

        //Then
        assertThat(builderUnderTest.icon).isEqualTo(testDrawable);
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

    private void createBuilder() {
        RuntimeEnvironment.application.setTheme(R.style.AppTheme);
        builderUnderTest = new SnackbarBuilder(parentView);
    }

    @ColorRes
    private int getColorResourceId(@ColorInt int color) {
        if (builderUnderTest != null) {
            builderUnderTest.context = activity;
        }
        @ColorRes int colorResId = 50;
        when(activity.getResources()).thenReturn(resources);
        when(resources.getColor(colorResId)).thenReturn(color);
        return colorResId;
    }

    @StringRes
    private int getStringResourceId(String string) {
        if (builderUnderTest != null) {
            builderUnderTest.context = activity;
        }
        @StringRes int stringResId = 50;
        when(activity.getString(stringResId)).thenReturn(string);
        return stringResId;
    }

}