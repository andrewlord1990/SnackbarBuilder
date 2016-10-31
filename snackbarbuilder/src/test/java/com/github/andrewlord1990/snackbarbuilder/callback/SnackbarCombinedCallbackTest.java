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

package com.github.andrewlord1990.snackbarbuilder.callback;

import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;

import com.github.andrewlord1990.snackbarbuilder.robolectric.LibraryRobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(LibraryRobolectricTestRunner.class)
public class SnackbarCombinedCallbackTest {

  @Mock
  SnackbarCallback snackbarCallback;
  @Mock
  Callback callback;
  @Mock
  Snackbar snackbar;
  @Mock
  SnackbarShowCallback showCallback;
  @Mock
  SnackbarDismissCallback dismissCallback;
  @Mock
  SnackbarActionDismissCallback actionDismissCallback;
  @Mock
  SnackbarSwipeDismissCallback swipeDismissCallback;
  @Mock
  SnackbarTimeoutDismissCallback timeoutDismissCallback;
  @Mock
  SnackbarManualDismissCallback manualDismissCallback;
  @Mock
  SnackbarConsecutiveDismissCallback consecutiveDismissCallback;
  private SnackbarCombinedCallback callbackUnderTest;

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);

    callbackUnderTest = new SnackbarCombinedCallback(snackbarCallback, callback);
  }

  @Test
  public void whenCreated_thenSetupCorrectly() {
    assertThat(callbackUnderTest.snackbarCallback).isEqualTo(snackbarCallback);
    assertThat(callbackUnderTest.callback).isEqualTo(callback);
  }

  @Test
  public void givenNoSnackbarCallback_whenCreated_thenSetupCorrectly() {
    int dismissEvent = Callback.DISMISS_EVENT_ACTION;

    SnackbarCombinedCallback testCallback = new SnackbarCombinedCallback(callback);
    testCallback.onShown(snackbar);
    testCallback.onDismissed(snackbar, dismissEvent);

    assertThat(testCallback.snackbarCallback).isNull();
    assertThat(testCallback.callback).isEqualTo(callback);
    verify(callback).onShown(snackbar);
    verify(callback).onDismissed(snackbar, dismissEvent);
  }

  @Test
  public void givenNoCallback_whenCreated_thenSetupCorrectly() {
    int dismissEvent = Callback.DISMISS_EVENT_ACTION;

    SnackbarCombinedCallback testCallback = new SnackbarCombinedCallback(snackbarCallback);
    testCallback.onShown(snackbar);
    testCallback.onDismissed(snackbar, dismissEvent);

    assertThat(testCallback.callback).isNull();
    assertThat(testCallback.snackbarCallback).isEqualTo(snackbarCallback);
    verify(snackbarCallback).onSnackbarShown(snackbar);
    verify(snackbarCallback).onSnackbarActionPressed(snackbar);
  }

  @Test
  public void whenOnDismissed_thenCallbackInformedOfEvent() {
    int dismissEvent = Callback.DISMISS_EVENT_MANUAL;

    callbackUnderTest.onDismissed(snackbar, dismissEvent);

    verify(callback).onDismissed(snackbar, dismissEvent);
  }

  @Test
  public void whenOnDismissed_thenSnackbarCallbackInformedOfEvent() {
    callbackUnderTest.onDismissed(snackbar, Callback.DISMISS_EVENT_MANUAL);

    verify(snackbarCallback).onSnackbarDismissed(snackbar);
  }

  @Test
  public void givenSwipeEvent_whenOnDismissed_thenSnackbarCallbackSwipedEvent() {
    int dismissEvent = Callback.DISMISS_EVENT_SWIPE;

    callbackUnderTest.onDismissed(snackbar, dismissEvent);

    verify(snackbarCallback).onSnackbarSwiped(snackbar);
  }

  @Test
  public void givenActionEvent_whenOnDismissed_thenSnackbarCallbackActionEvent() {
    int dismissEvent = Callback.DISMISS_EVENT_ACTION;

    callbackUnderTest.onDismissed(snackbar, dismissEvent);

    verify(snackbarCallback).onSnackbarActionPressed(snackbar);
  }

  @Test
  public void givenTimeoutEvent_whenOnDismissed_thenSnackbarCallbackTimeoutEvent() {
    int dismissEvent = Callback.DISMISS_EVENT_TIMEOUT;

    callbackUnderTest.onDismissed(snackbar, dismissEvent);

    verify(snackbarCallback).onSnackbarTimedOut(snackbar);
  }

  @Test
  public void givenManualEvent_whenOnDismissed_thenSnackbarCallbackManualEvent() {
    int dismissEvent = Callback.DISMISS_EVENT_MANUAL;

    callbackUnderTest.onDismissed(snackbar, dismissEvent);

    verify(snackbarCallback).onSnackbarManuallyDismissed(snackbar);
  }

  @Test
  public void givenConsecutiveEvent_whenOnDismissed_thenSnackbarCallbackConsecutiveEvent() {
    int dismissEvent = Callback.DISMISS_EVENT_CONSECUTIVE;

    callbackUnderTest.onDismissed(snackbar, dismissEvent);

    verify(snackbarCallback).onSnackbarDismissedAfterAnotherShown(snackbar);
  }

  @Test
  public void whenOnShown_thenCallbackInformedOfEvent() {
    callbackUnderTest.onShown(snackbar);

    verify(callback).onShown(snackbar);
    verify(snackbarCallback).onSnackbarShown(snackbar);
  }

  @Test
  public void whenCreatedUsingBuilder_thenAllCallbacksInformedOfEvents() {
    SnackbarCombinedCallback actual = SnackbarCombinedCallback.builder()
        .callback(callback)
        .snackbarCallback(snackbarCallback)
        .showCallback(showCallback)
        .dismissCallback(dismissCallback)
        .actionDismissCallback(actionDismissCallback)
        .swipeDismissCallback(swipeDismissCallback)
        .timeoutDismissCallback(timeoutDismissCallback)
        .manualDismissCallback(manualDismissCallback)
        .consecutiveDismissCallback(consecutiveDismissCallback)
        .build();

    actual.onShown(snackbar);
    actual.onDismissed(snackbar, Callback.DISMISS_EVENT_ACTION);
    actual.onDismissed(snackbar, Callback.DISMISS_EVENT_SWIPE);
    actual.onDismissed(snackbar, Callback.DISMISS_EVENT_TIMEOUT);
    actual.onDismissed(snackbar, Callback.DISMISS_EVENT_MANUAL);
    actual.onDismissed(snackbar, Callback.DISMISS_EVENT_CONSECUTIVE);

    verify(callback).onShown(snackbar);
    verify(snackbarCallback).onSnackbarShown(snackbar);
    verify(showCallback).onSnackbarShown(snackbar);
    verify(dismissCallback).onSnackbarDismissed(snackbar, Callback.DISMISS_EVENT_ACTION);
    verify(dismissCallback).onSnackbarDismissed(snackbar, Callback.DISMISS_EVENT_SWIPE);
    verify(dismissCallback).onSnackbarDismissed(snackbar, Callback.DISMISS_EVENT_TIMEOUT);
    verify(dismissCallback).onSnackbarDismissed(snackbar, Callback.DISMISS_EVENT_MANUAL);
    verify(dismissCallback).onSnackbarDismissed(snackbar, Callback.DISMISS_EVENT_CONSECUTIVE);
    verify(actionDismissCallback).onSnackbarActionPressed(snackbar);
    verify(swipeDismissCallback).onSnackbarSwiped(snackbar);
    verify(timeoutDismissCallback).onSnackbarTimedOut(snackbar);
    verify(manualDismissCallback).onSnackbarManuallyDismissed(snackbar);
    verify(consecutiveDismissCallback).onSnackbarDismissedAfterAnotherShown(snackbar);
  }

}