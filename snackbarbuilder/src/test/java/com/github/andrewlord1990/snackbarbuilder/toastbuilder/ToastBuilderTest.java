/*
 *
 *  * Copyright (C) 2016 Andrew Lord
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

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.andrewlord1990.snackbarbuilder.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = {CustomShadowToast.class})
public class ToastBuilderTest {

    private ToastBuilder builderUnderTest;

    @Mock
    Context context;

    @Mock
    Resources resources;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenCreated_thenMessageTextColorFromCustomThemeAttribute() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_CustomTheme);

        //When
        ToastBuilder builder = new ToastBuilder(RuntimeEnvironment.application);

        //Then
        assertThat(builder.messageTextColor).isEqualTo(0xFF123456);
    }

    @Test
    public void whenCreated_thenDurationFromCustomThemeAttribute() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_CustomTheme);

        //When
        ToastBuilder builder = new ToastBuilder(RuntimeEnvironment.application);

        //Then
        assertThat(builder.duration).isEqualTo(Toast.LENGTH_SHORT);
    }

    @Test
    public void whenCustomView_thenCustomViewSet() {
        //Given
        createBuilder();
        View customView = new View(RuntimeEnvironment.application);

        //When
        builderUnderTest.customView(customView);

        //Then
        assertThat(builderUnderTest.customView).isEqualTo(customView);
    }

    @Test
    public void whenCustomViewMessageViewId_thenCustomViewMessageViewIdSet() {
        //Given
        createBuilder();

        //When
        builderUnderTest.customViewMessageViewId(R.id.snackbarbuilder_icon);

        //Then
        assertThat(builderUnderTest.customViewMessageViewId).isEqualTo(R.id.snackbarbuilder_icon);
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
        @ColorRes int colorResId = getColorResourceId(0xFF223344);

        //When
        builderUnderTest.messageTextColorRes(colorResId);

        //Then
        assertThat(builderUnderTest.messageTextColor).isEqualTo(0xFF223344);
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
        builderUnderTest.duration(Toast.LENGTH_LONG);

        //Then
        assertThat(builderUnderTest.duration).isEqualTo(Toast.LENGTH_LONG);
    }

    @Test
    public void whenGravity_thenGravitySet() {
        //Given
        createBuilder();
        int expected = Gravity.TOP | Gravity.END;

        //When
        builderUnderTest.gravity(expected);

        //Then
        assertThat(builderUnderTest.gravity).isEqualTo(expected);
    }

    @Test
    public void whenGravityOffsetX_thenGravityOffsetXSet() {
        //Given
        createBuilder();
        int expected = 600;

        //When
        builderUnderTest.gravityOffsetX(expected);

        //Then
        assertThat(builderUnderTest.gravityOffsetX).isEqualTo(expected);
    }

    @Test
    public void whenGravityOffsetY_thenGravityOffsetYSet() {
        //Given
        createBuilder();
        int expected = -30;

        //When
        builderUnderTest.gravityOffsetY(expected);

        //Then
        assertThat(builderUnderTest.gravityOffsetY).isEqualTo(expected);
    }

    @Test
    public void whenBuild_thenToastSetup() {
        //Given
        int messageTextColor = 0xFF111111;
        String message = "message";
        RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);

        //When
        Toast toast = new ToastBuilder(RuntimeEnvironment.application)
                .messageTextColor(messageTextColor)
                .message(message)
                .duration(Toast.LENGTH_SHORT)
                .gravity(Gravity.TOP)
                .build();

        //Then
        assertThat(toast.getDuration()).isEqualTo(Toast.LENGTH_SHORT);

        TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
        assertThat(textView.getCurrentTextColor()).isEqualTo(messageTextColor);
        assertThat(textView.getText().toString()).isEqualTo(message);

        assertThat(toast.getGravity()).isEqualTo(Gravity.TOP);
    }

    @Test
    public void givenCustomView_whenBuild_thenCustomViewSetup() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
        View customView = new View(RuntimeEnvironment.application);

        //When
        Toast toast = new ToastBuilder(RuntimeEnvironment.application)
                .customView(customView)
                .duration(Toast.LENGTH_LONG)
                .build();

        //Then
        assertThat(toast.getDuration()).isEqualTo(Toast.LENGTH_LONG);
        assertThat(toast.getView()).isEqualTo(customView);
    }

    @Test
    public void givenCustomViewAndMessageViewId_whenBuild_thenCustomViewSetup() {
        //Given
        RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
        LinearLayout customView = new LinearLayout(RuntimeEnvironment.application);
        customView.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        TextView messageView = new TextView(RuntimeEnvironment.application);
        messageView.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        messageView.setId(R.id.snackbarbuilder_icon);
        customView.addView(messageView);
        int messageTextColor = 0xFF111111;
        String message = "message";

        //When
        Toast toast = new ToastBuilder(RuntimeEnvironment.application)
                .customView(customView)
                .customViewMessageViewId(R.id.snackbarbuilder_icon)
                .messageTextColor(messageTextColor)
                .message(message)
                .duration(Toast.LENGTH_LONG)
                .build();

        //Then
        assertThat(toast.getDuration()).isEqualTo(Toast.LENGTH_LONG);
        assertThat(toast.getView()).isEqualTo(customView);

        TextView textView = (TextView) toast.getView().findViewById(R.id.snackbarbuilder_icon);
        assertThat(textView.getCurrentTextColor()).isEqualTo(messageTextColor);
        assertThat(textView.getText().toString()).isEqualTo(message);
    }

    private void createBuilder() {
        RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
        builderUnderTest = new ToastBuilder(RuntimeEnvironment.application);
    }

    @ColorRes
    private int getColorResourceId(@ColorInt int color) {
        if (builderUnderTest != null) {
            builderUnderTest.context = context;
        }
        @ColorRes int colorResId = 50;
        when(context.getResources()).thenReturn(resources);
        when(resources.getColor(colorResId)).thenReturn(color);
        return colorResId;
    }

    @StringRes
    private int getStringResourceId(String string) {
        if (builderUnderTest != null) {
            builderUnderTest.context = context;
        }
        @StringRes int stringResId = 50;
        when(context.getString(stringResId)).thenReturn(string);
        return stringResId;
    }

}