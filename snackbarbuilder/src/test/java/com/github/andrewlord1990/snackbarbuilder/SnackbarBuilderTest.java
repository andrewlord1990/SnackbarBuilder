/*
 * Copyright (C) 2016 Andrew Lord
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
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
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;
import android.support.design.widget.Snackbar.Callback.DismissEvent;
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

import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarActionDismissCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCombinedCallbackBuilderAssert;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarConsecutiveDismissCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarDismissCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarManualDismissCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarShowCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarSwipeDismissCallback;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarTimeoutDismissCallback;
import com.github.andrewlord1990.snackbarbuilder.parent.SnackbarParentFinder;
import com.github.andrewlord1990.snackbarbuilder.robolectric.LibraryRobolectricTestRunner;

import org.assertj.android.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(LibraryRobolectricTestRunner.class)
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
    SnackbarBuilder builder = new SnackbarBuilder(parentView);

    assertThat(builder.parentView).isEqualTo(parentView);
    assertThat(builder.context).isEqualTo(parentView.getContext());
  }

  @Test
  public void givenActivity_whenCreated_thenParentViewFoundUsingParentViewId() {
    Activity activity = Robolectric.setupActivity(Activity.class);
    activity.setTheme(R.style.TestSnackbarBuilder_CustomTheme);
    LinearLayout layout = new LinearLayout(activity);
    layout.setId(R.id.snackbarbuilder_icon);
    activity.setContentView(layout);

    SnackbarBuilder builder = new SnackbarBuilder(activity);

    assertThat(builder.parentView).isEqualTo(layout);
    assertThat(builder.context).isEqualTo(activity);
    assertThat(builder.actionTextColor).isEqualTo(0xFF454545);
    assertThat(builder.messageTextColor).isEqualTo(0xFF987654);
  }

  @Test
  public void givenActivityAndParentFinder_whenCreated_thenParentViewFoundUsingTheParentFinder() {
    Activity activity = Robolectric.setupActivity(Activity.class);
    @IdRes final int fallbackId = 100;
    SnackbarParentFinder parentFinder = new SnackbarParentFinder() {
      @Override
      public View findSnackbarParent(Activity activity) {
        View defaultParent = activity.findViewById(R.id.snackbarbuilder_icon);
        if (defaultParent != null) {
          return defaultParent;
        }
        return activity.findViewById(fallbackId);
      }
    };
    LinearLayout layout = new LinearLayout(activity);
    layout.setId(fallbackId);
    activity.setContentView(layout);

    SnackbarBuilder builder = new SnackbarBuilder(activity, parentFinder);

    assertThat(builder.parentView).isEqualTo(layout);
    assertThat(builder.context).isEqualTo(activity);
  }

  @Test
  public void whenCreated_thenActionTextColorFromCustomThemeAttribute() {
    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_CustomTheme);

    SnackbarBuilder builder = new SnackbarBuilder(parentView);

    assertThat(builder.actionTextColor).isEqualTo(0xFF454545);
  }

  @Test
  public void givenNoCustomThemeAttribute_whenCreated_thenActionTextColorFromColorAccentThemeAttribute() {
    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_FallbackTheme);

    SnackbarBuilder builder = new SnackbarBuilder(parentView);

    assertThat(builder.actionTextColor).isEqualTo(0xFF232323);
  }

  @Test
  public void whenCreated_thenMessageTextColorFromCustomThemeAttribute() {
    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_CustomTheme);

    SnackbarBuilder builder = new SnackbarBuilder(parentView);

    assertThat(builder.messageTextColor).isEqualTo(0xFF987654);
  }

  @Test
  public void givenNoCustomThemeAttribute_whenCreated_thenMessageTextColorFromColorAccentThemeAttribute() {
    SnackbarBuilder builder = new SnackbarBuilder(parentView);

    assertThat(builder.messageTextColor).isEqualTo(Color.WHITE);
  }

  @Test
  public void whenCreated_thenDurationFromCustomThemeAttribute() {
    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_CustomTheme);

    SnackbarBuilder builder = new SnackbarBuilder(parentView);

    assertThat(builder.duration).isEqualTo(Snackbar.LENGTH_INDEFINITE);
  }

  @Test
  public void givenNoCustomThemeAttribute_whenCreated_thenDurationLong() {
    SnackbarBuilder builder = new SnackbarBuilder(parentView);

    assertThat(builder.duration).isEqualTo(Snackbar.LENGTH_LONG);
  }

  @Test
  public void whenCreated_thenBackgroundColorFromCustomThemeAttribute() {
    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_CustomTheme);

    SnackbarBuilder builder = new SnackbarBuilder(parentView);

    assertThat(builder.backgroundColor).isEqualTo(0xFF999999);
  }

  @Test
  public void whenMessageWithString_thenMessageSet() {
    SnackbarBuilder builder = createBuilder();

    builder.message("message");

    assertThat(builder.message).isEqualTo("message");
  }

  @Test
  public void givenSpan_whenMessage_thenMessageSet() {
    SnackbarBuilder builder = createBuilder();
    Spannable spannable = new SpannableString("testMessage");
    spannable.setSpan(new ForegroundColorSpan(Color.CYAN), 0, spannable.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    builder.message(spannable);

    assertThat(builder.message.toString()).isEqualTo("testMessage");
    SpannableString actual = SpannableString.valueOf(builder.message);
    ForegroundColorSpan[] spans = actual.getSpans(0, spannable.length(), ForegroundColorSpan.class);
    assertThat(spans).hasSize(1);
    assertThat(spans[0].getForegroundColor()).isEqualTo(Color.CYAN);
  }

  @Test
  public void whenMessageWithStringResource_thenMessageSet() {
    SnackbarBuilder builder = createBuilder();

    builder.message(R.string.snackbarbuilder_action_undo);

    assertThat(builder.message).isEqualTo("Undo");
  }

  @Test
  public void whenMessageTextColorRes_thenMessageTextColorSet() {
    SnackbarBuilder builder = createBuilder();

    builder.messageTextColorRes(R.color.snackbarbuilder_default_message);

    assertThat(builder.messageTextColor).isEqualTo(Color.WHITE);
  }

  @Test
  public void whenMessageTextColor_thenMessageTextColorSet() {
    SnackbarBuilder builder = createBuilder();

    builder.messageTextColor(0xFF333333);

    assertThat(builder.messageTextColor).isEqualTo(0xFF333333);
  }

  @Test
  public void whenAppendMessageWithString_thenMessageAdded() {
    SnackbarBuilder builder = createBuilder();
    String message = "aMessage";

    builder.appendMessage(message);

    assertThat(builder.appendMessages.toString()).isEqualTo(message);
  }

  @Test
  public void givenMessageAlreadyAppended_whenAppendMessageWithString_thenMessageAdded() {
    SnackbarBuilder builder = createBuilder();
    String message = "aMessage";
    String starting = "startingMessage";
    builder.appendMessage(starting);

    builder.appendMessage(message);

    assertThat(builder.appendMessages.toString()).isEqualTo(starting + message);
  }

  @Test
  public void whenAppendMessageWithStringResource_thenMessageAdded() {
    SnackbarBuilder builder = createBuilder();

    builder.appendMessage(R.string.snackbarbuilder_action_undo);

    assertThat(builder.appendMessages.toString()).isEqualTo("Undo");
  }

  @Test
  public void givenMessageAlreadyAppended_whenAppendMessageWithStringResource_thenMessageAdded() {
    SnackbarBuilder builder = createBuilder();
    String starting = "startingMessage";
    builder.appendMessage(starting);

    builder.appendMessage(R.string.snackbarbuilder_action_undo);

    assertThat(builder.appendMessages.toString()).isEqualTo("startingMessageUndo");
  }

  @Test
  public void whenAppendMessage_thenMessageWithForegroundColorAdded() {
    SnackbarBuilder builder = createBuilder();

    builder.appendMessage("message", Color.RED);

    assertThatMessagesWithColorsAppended(builder, "message", Color.RED);
  }

  @Test
  public void givenMessageAlreadyAppended_whenAppendMessageWithColor_thenMessagesWithForegroundColorsAdded() {
    SnackbarBuilder builder = createBuilder();
    builder.appendMessage("first", Color.BLACK);

    builder.appendMessage("second", Color.MAGENTA);

    assertThatMessagesWithColorsAppended(builder, "firstsecond", Color.BLACK, Color.MAGENTA);
  }

  @Test
  public void whenAppendMessageResWithColorRes_thenMessageWithForegroundColorAdded() {
    SnackbarBuilder builder = createBuilder();

    builder.appendMessage(R.string.snackbarbuilder_action_undo, R.color.snackbarbuilder_default_message);

    assertThatMessagesWithColorsAppended(builder, "Undo", Color.WHITE);
  }

  private void assertThatMessagesWithColorsAppended(SnackbarBuilder builder, String expected, int... colors) {
    int length = expected.length();
    assertThat(builder.appendMessages.subSequence(0, length).toString()).isEqualTo(expected);
    ForegroundColorSpan[] spans = builder.appendMessages
        .getSpans(0, length, ForegroundColorSpan.class);
    assertThat(spans.length).isEqualTo(colors.length);
    for (int i = 0; i < colors.length; i++) {
      assertThat(spans[i].getForegroundColor()).isEqualTo(colors[i]);
    }
  }

  @Test
  public void whenDuration_thenDurationSet() {
    SnackbarBuilder builder = createBuilder();

    builder.duration(Snackbar.LENGTH_INDEFINITE);

    assertThat(builder.duration).isEqualTo(Snackbar.LENGTH_INDEFINITE);
  }

  @Test
  public void whenActionTextColorRes_thenActionTextColorSet() {
    SnackbarBuilder builder = createBuilder();

    builder.actionTextColorRes(R.color.snackbarbuilder_default_message);

    assertThat(builder.actionTextColor).isEqualTo(Color.WHITE);
  }

  @Test
  public void whenActionTextColor_thenActionTextColorSet() {
    SnackbarBuilder builder = createBuilder();

    builder.actionTextColor(0xFF333333);

    assertThat(builder.actionTextColor).isEqualTo(0xFF333333);
  }

  @Test
  public void whenActionTextWithString_thenActionTextSet() {
    SnackbarBuilder builder = createBuilder();

    builder.actionText("text");

    assertThat(builder.actionText).isEqualTo("text");
  }

  @Test
  public void whenActionTextWithStringResource_thenActionTextSet() {
    SnackbarBuilder builder = createBuilder();

    builder.actionText(R.string.snackbarbuilder_action_undo);

    assertThat(builder.actionText).isEqualTo("Undo");
  }

  @Test
  public void whenBackgroundColorRes_thenBackgroundColorSet() {
    SnackbarBuilder builder = createBuilder();

    builder.backgroundColorRes(R.color.snackbarbuilder_default_message);

    assertThat(builder.backgroundColor).isEqualTo(Color.WHITE);
  }

  @Test
  public void whenBackgroundColor_thenBackgroundColorSet() {
    SnackbarBuilder builder = createBuilder();

    builder.backgroundColor(0xFF333333);

    assertThat(builder.backgroundColor).isEqualTo(0xFF333333);
  }

  @Test
  public void whenActionClickListener_thenActionClickListenerSet() {
    SnackbarBuilder builder = createBuilder();
    OnClickListener click = new OnClickListener() {
      @Override
      public void onClick(View view) {
        //Click
      }
    };

    builder.actionClickListener(click);

    assertThat(builder.actionClickListener).isEqualTo(click);
  }

  @Test
  public void whenCallback_thenCallbackSet() {
    SnackbarBuilder builder = createBuilder();
    Callback callback = new Callback() {
      @Override
      public void onDismissed(Snackbar snackbar, int event) {
        super.onDismissed(snackbar, event);
      }
    };

    builder.callback(callback);

    SnackbarCombinedCallbackBuilderAssert.assertThat(builder.callbackBuilder)
        .hasCallback(callback);
  }

  @Test
  public void whenSnackbarCallback_thenSnackbarCallbackSet() {
    SnackbarBuilder builder = createBuilder();
    SnackbarCallback callback = new SnackbarCallback() {
    };

    builder.snackbarCallback(callback);

    SnackbarCombinedCallbackBuilderAssert.assertThat(builder.callbackBuilder)
        .hasSnackbarCallback(callback);
  }

  @Test
  public void whenShowCallback_thenCallbackSet() {
    SnackbarBuilder builder = createBuilder();
    SnackbarShowCallback callback = new SnackbarShowCallback() {
      @Override
      public void onSnackbarShown(Snackbar snackbar) {
      }
    };

    builder.showCallback(callback);

    SnackbarCombinedCallbackBuilderAssert.assertThat(builder.callbackBuilder)
        .hasShowCallback(callback);
  }

  @Test
  public void whenDismissCallback_thenCallbackSet() {
    SnackbarBuilder builder = createBuilder();
    SnackbarDismissCallback callback = new SnackbarDismissCallback() {
      @Override
      public void onSnackbarDismissed(Snackbar snackbar, @DismissEvent int dismissEvent) {
      }
    };

    builder.dismissCallback(callback);

    SnackbarCombinedCallbackBuilderAssert.assertThat(builder.callbackBuilder)
        .hasDismissCallback(callback);
  }

  @Test
  public void whenActionDismissCallback_thenCallbackSet() {
    SnackbarBuilder builder = createBuilder();
    SnackbarActionDismissCallback callback = new SnackbarActionDismissCallback() {
      @Override
      public void onSnackbarActionPressed(Snackbar snackbar) {
      }
    };

    builder.actionDismissCallback(callback);

    SnackbarCombinedCallbackBuilderAssert.assertThat(builder.callbackBuilder)
        .hasActionDismissCallback(callback);
  }

  @Test
  public void whenSwipeDismissCallback_thenCallbackSet() {
    SnackbarBuilder builder = createBuilder();
    SnackbarSwipeDismissCallback callback = new SnackbarSwipeDismissCallback() {
      @Override
      public void onSnackbarSwiped(Snackbar snackbar) {
      }
    };

    builder.swipeDismissCallback(callback);

    SnackbarCombinedCallbackBuilderAssert.assertThat(builder.callbackBuilder)
        .hasSwipeDismissCallback(callback);
  }

  @Test
  public void whenTimeoutDismissCallback_thenCallbackSet() {
    SnackbarBuilder builder = createBuilder();
    SnackbarTimeoutDismissCallback callback = new SnackbarTimeoutDismissCallback() {
      @Override
      public void onSnackbarTimedOut(Snackbar snackbar) {
      }
    };

    builder.timeoutDismissCallback(callback);

    SnackbarCombinedCallbackBuilderAssert.assertThat(builder.callbackBuilder)
        .hasTimeoutDismissCallback(callback);
  }

  @Test
  public void whenManualDismissCallback_thenCallbackSet() {
    SnackbarBuilder builder = createBuilder();
    SnackbarManualDismissCallback callback = new SnackbarManualDismissCallback() {
      @Override
      public void onSnackbarManuallyDismissed(Snackbar snackbar) {
      }
    };

    builder.manualDismissCallback(callback);

    SnackbarCombinedCallbackBuilderAssert.assertThat(builder.callbackBuilder)
        .hasManualDismissCallback(callback);
  }

  @Test
  public void whenConsecutiveDismissCallback_thenCallbackSet() {
    SnackbarBuilder builder = createBuilder();
    SnackbarConsecutiveDismissCallback callback = new SnackbarConsecutiveDismissCallback() {
      @Override
      public void onSnackbarDismissedAfterAnotherShown(Snackbar snackbar) {
      }
    };

    builder.consecutiveDismissCallback(callback);

    SnackbarCombinedCallbackBuilderAssert.assertThat(builder.callbackBuilder)
        .hasConsecutiveDismissCallback(callback);
  }

  @Test
  public void whenLowercaseAction_thenActionAllCapsFalse() {
    SnackbarBuilder builder = createBuilder();

    builder.lowercaseAction();

    assertThat(builder.actionAllCaps).isFalse();
  }

  @Test
  public void whenIconWithDrawableResource_thenIconSet() {
    SnackbarBuilder builder = createBuilder();
    @DrawableRes int drawableResId = 50;
    getResourceCreator(builder).createMockDrawableResId(drawableResId, drawable);

    builder.icon(drawableResId);

    assertThat(builder.icon).isEqualTo(drawable);
  }

  @Test
  public void whenIconWithDrawable_thenIconSet() {
    SnackbarBuilder builder = createBuilder();
    @DrawableRes int drawableResId = 50;
    getResourceCreator(builder).createMockDrawableResId(drawableResId, drawable);

    builder.icon(drawable);

    assertThat(builder.icon).isEqualTo(drawable);
  }

  @Test
  public void whenIconMarginStart_thenIconMarinStartSet() {
    SnackbarBuilder builder = createBuilder();
    int iconMarginStart = 100;

    builder.iconMarginStart(iconMarginStart);

    assertThat(builder.iconMarginStart).isEqualTo(iconMarginStart);
  }

  @Test
  public void whenIconMarginStartRes_thenIconMarinStartSet() {
    SnackbarBuilder builder = createBuilder();
    int iconMarginStart = 100;
    @DimenRes int dimenResId = getResourceCreator(builder)
        .getDimensionPixelSize(iconMarginStart);

    builder.iconMarginStartRes(dimenResId);

    assertThat(builder.iconMarginStart).isEqualTo(iconMarginStart);
  }

  @Test
  public void whenIconMarginEnd_thenIconMarinEndSet() {
    SnackbarBuilder builder = createBuilder();
    int iconMarginEnd = 100;

    builder.iconMarginEnd(iconMarginEnd);

    assertThat(builder.iconMarginEnd).isEqualTo(iconMarginEnd);
  }

  @Test
  public void whenIconMarginEndRes_thenIconMarinStartSet() {
    SnackbarBuilder builder = createBuilder();
    int iconMarginEnd = 100;
    @DimenRes int dimenResId = getResourceCreator(builder)
        .getDimensionPixelSize(iconMarginEnd);

    builder.iconMarginEndRes(dimenResId);

    assertThat(builder.iconMarginEnd).isEqualTo(iconMarginEnd);
  }

  @Test
  public void whenBuildWrapper_thenSnackbarWrapperSetup() {
    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
    CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);

    SnackbarWrapper wrapper = new SnackbarBuilder(parent)
        .message("message")
        .actionText("action")
        .duration(Snackbar.LENGTH_SHORT)
        .buildWrapper();

    assertThat(wrapper.getText()).isEqualTo("message");
    assertThat(wrapper.getActionText()).isEqualTo("action");
    assertThat(wrapper.getDuration()).isEqualTo(Snackbar.LENGTH_SHORT);
  }

  @Test
  @TargetApi(11)
  public void whenBuild_thenSnackbarSetup() {
    int messageTextColor = 0xFF111111;
    int actionTextColor = 0xFF999999;
    String message = "message";
    String action = "action";
    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
    CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);

    Snackbar snackbar = new SnackbarBuilder(parent)
        .messageTextColor(messageTextColor)
        .actionTextColor(actionTextColor)
        .message(message)
        .actionText(action)
        .duration(Snackbar.LENGTH_INDEFINITE)
        .backgroundColor(0xFF777777)
        .lowercaseAction()
        .build();

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
    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
    CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);

    Snackbar snackbar = new SnackbarBuilder(parent)
        .message("message")
        .actionText("action")
        .duration(Snackbar.LENGTH_SHORT)
        .callback(callback)
        .build();
    snackbar.show();

    snackbar.dismiss();
    verify(callback).onDismissed(snackbar, Callback.DISMISS_EVENT_MANUAL);
  }

  @Test
  public void givenSnackbarCallback_whenBuild_thenSnackbarCallbackSet() {
    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
    CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);

    Snackbar snackbar = new SnackbarBuilder(parent)
        .message("message")
        .actionText("action")
        .duration(Snackbar.LENGTH_SHORT)
        .snackbarCallback(snackbarCallback)
        .build();
    snackbar.show();

    snackbar.dismiss();
    verify(snackbarCallback).onSnackbarManuallyDismissed(snackbar);
  }

  @Test
  public void givenNotLowercaseAction_whenBuild_thenActionAllCaps() {
    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
    CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);

    Snackbar snackbar = new SnackbarBuilder(parent)
        .message("message")
        .actionText("action")
        .duration(Snackbar.LENGTH_SHORT)
        .build();
    snackbar.show();

    Button button = (Button) snackbar.getView().findViewById(R.id.snackbar_action);
    assertThat(button.getTransformationMethod()).isNotNull();
  }

  @Test
  public void givenIcon_whenBuild_thenIconAddedToSnackbar() {
    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
    CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);

    Snackbar snackbar = new SnackbarBuilder(parent)
        .message("messsage")
        .icon(drawable)
        .iconMarginStart(10)
        .iconMarginEnd(20)
        .build();

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
    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
    CoordinatorLayout parent = new CoordinatorLayout(RuntimeEnvironment.application);

    final Snackbar snackbar = new SnackbarBuilder(parent)
        .message("start")
        .appendMessage("first_added")
        .appendMessage("second_in_blue", Color.BLUE)
        .appendMessage("third_in_dark_grey", Color.DKGRAY)
        .build();

    List<Pair<String, Integer>> expected = new ArrayList<>();
    expected.add(new Pair<>("start", 0));
    expected.add(new Pair<>("first_added", 0));
    expected.add(new Pair<>("second_in_blue", Color.BLUE));
    expected.add(new Pair<>("third_in_dark_grey", Color.DKGRAY));
    SnackbarCustomAssert.assertThat(snackbar).hasMessagesAppended(expected);
  }

  private SnackbarBuilder createBuilder() {
    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
    return new SnackbarBuilder(parentView);
  }

  private MockResourceCreator getResourceCreator(SnackbarBuilder builder) {
    return MockResourceCreator.fromBuilder(builder)
        .withContext(activity)
        .withResources(resources);
  }

}