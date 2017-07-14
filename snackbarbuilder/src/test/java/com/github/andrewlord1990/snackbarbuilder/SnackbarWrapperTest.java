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
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCallback;
import com.github.andrewlord1990.snackbarbuilder.robolectric.LibraryRobolectricTestRunner;

import org.assertj.android.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(LibraryRobolectricTestRunner.class)
public class SnackbarWrapperTest {

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
  private Snackbar snackbar;
  private SnackbarWrapper wrapper;
  private MockResourceCreator resourceCreator;

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);

    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
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
    Snackbar actual = wrapper.getSnackbar();

    assertThat(actual).isEqualTo(snackbar);
  }

  @Test
  public void whenSetActionTextWithResource_thenActionTextUpdated() {
    wrapper.setActionText(R.string.snackbarbuilder_action_undo);

    Assertions.assertThat(getActionView()).hasText("Undo");
  }

  @Test
  public void whenSetActionText_thenActionTextUpdated() {
    String actionText = "someAction";

    wrapper.setActionText(actionText);

    assertThat(wrapper.getActionText()).isEqualTo(actionText);
  }

  @Test
  public void whenSetActionClickListener_thenActionClickListenerSet() {
    TestClickListener clickListener = new TestClickListener();

    wrapper.setActionClickListener(clickListener);
    getActionView().performClick();

    assertThat(clickListener.isClicked()).isTrue();
  }

  @Test
  public void givenNullClickListener_whenSetActionWithResource_thenActionHidden() {
    wrapper.setAction(R.string.snackbarbuilder_action_undo, null);

    Assertions.assertThat(getActionView()).isNotVisible();
  }

  @Test
  public void givenClickListener_whenSetActionWithResource_thenActionSetAndShown() {
    TestClickListener clickListener = new TestClickListener();

    wrapper.setAction(R.string.snackbarbuilder_action_undo, clickListener);

    Assertions.assertThat(getActionView())
        .isVisible()
        .hasText("Undo");
    getActionView().performClick();
    assertThat(clickListener.isClicked()).isTrue();
  }

  @Test
  public void givenNullClickListener_whenSetAction_thenActionHidden() {
    wrapper.setAction("Some text", null);

    Assertions.assertThat(getActionView()).isNotVisible();
  }

  @Test
  public void givenClickListener_whenSetAction_thenActionSetAndShown() {
    TestClickListener clickListener = new TestClickListener();
    String actionText = "some action";

    wrapper.setAction(actionText, clickListener);

    Assertions.assertThat(getActionView())
        .isVisible()
        .hasText(actionText);
    getActionView().performClick();
    assertThat(clickListener.isClicked()).isTrue();
  }

  @Test
  public void whenSetActionTextColorWithStateList_thenActionTextColorSet() {
    ColorStateList stateList = createColorStateList();

    wrapper.setActionTextColor(stateList);

    assertThat(wrapper.getActionTextColors())
        .isEqualTo(stateList)
        .isEqualTo(getActionView().getTextColors());
  }

  @Test
  public void whenSetActionTextColorWithColor_thenActionTextColorSet() {
    wrapper.setActionTextColor(Color.GREEN);

    assertThat(wrapper.getActionCurrentTextColor()).isEqualTo(Color.GREEN);
  }

  @Test
  public void whenSetActionTextColorResId_thenActionTextColorSet() {
    wrapper.setActionTextColorRes(R.color.snackbarbuilder_default_message);

    assertThat(wrapper.getActionCurrentTextColor())
        .isEqualTo(Color.WHITE)
        .isEqualTo(getActionView().getCurrentTextColor());
  }

  @Test
  public void whenSetActionVisibility_thenActionVisibilityChanged() {
    getActionView().setVisibility(View.GONE);

    wrapper.setActionVisibility(View.VISIBLE);

    assertThat(wrapper.getActionVisibility())
        .isEqualTo(View.VISIBLE)
        .isEqualTo(getActionView().getVisibility());
  }

  @Test
  public void givenTrue_whenSetAllCapsActionText_thenActionAllCaps() {
    TextViewExtension.from(getActionView()).setAllCaps(false);

    wrapper.setAllCapsActionText(true);

    assertThat(getActionView().getTransformationMethod()).isNotNull();
  }

  @Test
  public void givenFalse_whenSetAllCapsActionText_thenActionNotAllCaps() {
    TextViewExtension.from(getActionView()).setAllCaps(true);

    wrapper.setAllCapsActionText(false);
    
    assertThat(getActionView().getTransformationMethod()).isNull();
  }

  @Test
  public void whenSetLowercaseActionText_thenActionNotAllCaps() {
    TextViewExtension.from(getActionView()).setAllCaps(true);

    wrapper.setLowercaseActionText();

    assertThat(getActionView().getTransformationMethod()).isNull();
  }

  @Test
  public void whenSetUppercaseActionText_thenActionAllCaps() {
    TextViewExtension.from(getActionView()).setAllCaps(false);

    wrapper.setUppercaseActionText();

    assertThat(getActionView().getTransformationMethod()).isNotNull();
  }

  @Test
  public void whenSetTextWithString_thenMessageSet() {
    String message = "some message to set";

    wrapper.setText(message);

    assertThat(wrapper.getText())
        .isEqualTo(message)
        .isEqualTo(getMessageView().getText().toString());
  }

  @Test
  public void whenSetTextWithResource_thenMessageSet() {
    wrapper.setText(R.string.snackbarbuilder_action_undo);

    assertThat(wrapper.getText())
        .isEqualTo("Undo")
        .isEqualTo(getMessageView().getText().toString());
  }

  @Test
  public void whenSetTextColorWithColor_thenMessageColorSet() {
    wrapper.setTextColor(Color.RED);

    assertThat(wrapper.getCurrentTextColor())
        .isEqualTo(Color.RED)
        .isEqualTo(getMessageView().getCurrentTextColor());
  }

  @Test
  public void whenSetTextColorWithStateList_thenMessageColorSet() {
    ColorStateList stateList = createColorStateList();

    wrapper.setTextColor(stateList);

    assertThat(wrapper.getTextColors())
        .isEqualTo(stateList)
        .isEqualTo(getMessageView().getTextColors());
  }

  @Test
  public void whenSetTextColorResId_thenMessageColorSet() {
    wrapper.setTextColorRes(R.color.snackbarbuilder_default_message);

    assertThat(wrapper.getCurrentTextColor())
        .isEqualTo(Color.WHITE)
        .isEqualTo(getMessageView().getCurrentTextColor());
  }

  @Test
  public void whenAppendMessageWithString_thenTextAppendedToMessage() {
    wrapper.setText("startingText");

    wrapper.appendMessage("MORE");

    assertThat(wrapper.getText().toString()).isEqualTo("startingTextMORE");
  }

  @Test
  public void whenAppendMessageWithResource_thenTextAppendedToMessage() {
    wrapper.setText("startingText");

    wrapper.appendMessage(R.string.snackbarbuilder_action_undo);

    assertThat(wrapper.getText().toString()).isEqualTo("startingTextUndo");
  }

  @Test
  public void whenAppendMessagesWithColors_thenTextWithColorsAppendedToMessage() {
    wrapper.setText("start")
        .appendMessage(R.string.snackbarbuilder_action_undo, R.color.snackbarbuilder_default_message)
        .appendMessage("second_in_blue", Color.BLUE)
        .appendMessage("third_in_dark_grey", Color.DKGRAY);

    List<Pair<String, Integer>> expected = new ArrayList<>();
    expected.add(new Pair<>("start", 0));
    expected.add(new Pair<>("Undo", Color.WHITE));
    expected.add(new Pair<>("second_in_blue", Color.BLUE));
    expected.add(new Pair<>("third_in_dark_grey", Color.DKGRAY));
    SnackbarCustomAssert.assertThat(snackbar).hasMessagesAppended(expected);
  }

  @Test
  public void whenSetMessageVisibility_thenMessageVisibilityChanged() {
    getMessageView().setVisibility(View.GONE);

    wrapper.setMessageVisibility(View.VISIBLE);

    assertThat(wrapper.getMessageVisibility())
        .isEqualTo(View.VISIBLE)
        .isEqualTo(getMessageView().getVisibility());
  }

  @Test
  public void whenSetDuration_thenDurationSet() {
    snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);

    wrapper.setDuration(Snackbar.LENGTH_LONG);

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
    wrapper.setBackgroundColor(Color.MAGENTA);

    Assertions.assertThat((ColorDrawable) wrapper.getView().getBackground())
        .hasColor(Color.MAGENTA);
  }

  @Test
  public void whenSetBackgroundColorResId_thenBackgroundColorSet() {
    wrapper.setBackgroundColorRes(R.color.snackbarbuilder_default_message);

    Assertions.assertThat((ColorDrawable) wrapper.getView().getBackground())
        .hasColor(Color.WHITE);
  }

  @Test
  public void whenAddCallback_thenCallbackAdded() {
    wrapper.addCallback(callback);
    wrapper.show();

    wrapper.dismiss();
    verify(callback).onDismissed(snackbar, Callback.DISMISS_EVENT_MANUAL);
  }

  @Test
  public void whenAddSnackbarCallback_thenSnackbarCallbackAdded() {
    wrapper.addSnackbarCallback(snackbarCallback);
    wrapper.show();

    wrapper.dismiss();
    verify(snackbarCallback).onSnackbarManuallyDismissed(snackbar);
  }

  @Test
  public void whenSetIconWithResource_thenIconSet() {
    @DrawableRes int drawableResId = 50;
    resourceCreator.createMockDrawableResId(drawableResId, drawable);

    wrapper.setIcon(drawableResId);

    assertThat(getMessageView().getCompoundDrawables()[0]).isEqualTo(drawable);
  }

  @Test
  public void whenSetIconWithDrawable_thenIconSet() {
    wrapper.setIcon(drawable);

    assertThat(getMessageView().getCompoundDrawables()[0]).isEqualTo(drawable);
  }

  @Test
  public void whenSetIconMarginWithResource_thenIconMarginSet() {
    Resources resources = RuntimeEnvironment.application.getResources();
    final int expected = resources.getDimensionPixelSize(R.dimen.snackbarbuilder_icon_margin_default);

    wrapper.setIconMargin(R.dimen.snackbarbuilder_icon_margin_default);

    assertThat(getMessageView().getCompoundDrawablePadding()).isEqualTo(expected);
  }

  @Test
  public void whenSetIconMarginWithPixels_thenIconMarginSet() {
    final int expected = 100;

    wrapper.setIconMarginPixels(expected);

    assertThat(getMessageView().getCompoundDrawablePadding()).isEqualTo(expected);
  }

  @Test
  public void whenShow_thenSnackbarShown() {
    wrapper.show();

    assertThat(wrapper.isShown())
        .isTrue()
        .isEqualTo(snackbar.isShown());
    assertThat(wrapper.isShownOrQueued())
        .isTrue()
        .isEqualTo(snackbar.isShownOrQueued());
  }

  @Test
  public void whenDismiss_thenSnackbarDismissed() {
    wrapper.addSnackbarCallback(snackbarCallback);
    wrapper.show();

    wrapper.dismiss();

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
    return (ImageView) snackbar.getView().findViewById(R.id.snackbarbuilder_icon);
  }

  private ColorStateList createColorStateList() {
    int[] state0 = new int[] {0, 1};
    int[] state1 = new int[] {2, 3};
    int[][] states = new int[][] {state0, state1};
    int[] colors = new int[] {Color.RED, Color.CYAN};
    return new ColorStateList(states, colors);
  }

  private void assertThatIconMarginsEqualTo(int leftMargin, int rightMargin) {
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getIconView().getLayoutParams();
    assertThat(layoutParams.leftMargin).isEqualTo(leftMargin);
    assertThat(layoutParams.rightMargin).isEqualTo(rightMargin);
  }

}