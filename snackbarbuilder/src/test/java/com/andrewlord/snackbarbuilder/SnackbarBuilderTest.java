package com.andrewlord.snackbarbuilder;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
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

        //When
        builderUnderTest.message(R.string.test);

        //Then
        assertThat(builderUnderTest.message).isEqualTo("Test");
    }

    @Test
    public void whenMessageTextColorRes_thenMessageTextColorSet() {
        //Given
        createBuilder();

        //When
        builderUnderTest.messageTextColorRes(R.color.test);

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

        //When
        builderUnderTest.actionTextColorRes(R.color.test);

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

        //When
        builderUnderTest.actionText(R.string.test);

        //Then
        assertThat(builderUnderTest.actionText).isEqualTo("Test");
    }

    @Test
    public void whenBackgroundColorRes_thenBackgroundColorSet() {
        //Given
        createBuilder();

        //When
        builderUnderTest.backgroundColorRes(R.color.test);

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
                .build();

        //Then
        assertThat(snackbar.getDuration()).isEqualTo(Snackbar.LENGTH_INDEFINITE);

        TextView textView = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        assertThat(textView.getCurrentTextColor()).isEqualTo(messageTextColor);

        Button button = (Button) snackbar.getView().findViewById(R.id.snackbar_action);
        assertThat(button.getCurrentTextColor()).isEqualTo(actionTextColor);
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
        verify(callback, times(1)).onDismissed(snackbar, Callback.DISMISS_EVENT_MANUAL);
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
        verify(snackbarCallback, times(1)).onSnackbarManuallyDismissed(snackbar);
    }

    private void createBuilder() {
        RuntimeEnvironment.application.setTheme(R.style.AppTheme);
        builderUnderTest = new SnackbarBuilder(parentView);
    }

}