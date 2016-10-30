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
import android.util.Log;

import com.github.andrewlord1990.snackbarbuilder.robolectric.LibraryRobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowLog.LogItem;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(LibraryRobolectricTestRunner.class)
public class SnackbarCallbackTest {

  @Mock
  Snackbar snackbar;
  private SnackbarCallback callbackUnderTest;

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);

    callbackUnderTest = new SnackbarCallback() {
      @Override
      public void onSnackbarShown(Snackbar snackbar) {
        super.onSnackbarShown(snackbar);
        Log.v(SnackbarCallback.class.getSimpleName(), "onSnackbarShown");
      }

      @Override
      public void onSnackbarDismissed(Snackbar snackbar) {
        super.onSnackbarDismissed(snackbar);
        Log.v(SnackbarCallback.class.getSimpleName(), "onSnackbarDismissed");
      }

      @Override
      public void onSnackbarDismissed(Snackbar snackbar, @Callback.DismissEvent int dismissEvent) {
        super.onSnackbarDismissed(snackbar, dismissEvent);
        Log.v(SnackbarCallback.class.getSimpleName(), "onSnackbarDismissed method = " + dismissEvent);
      }

      @Override
      public void onSnackbarActionPressed(Snackbar snackbar) {
        super.onSnackbarActionPressed(snackbar);
        Log.v(SnackbarCallback.class.getSimpleName(), "onSnackbarActionPressed");
      }

      @Override
      public void onSnackbarSwiped(Snackbar snackbar) {
        super.onSnackbarSwiped(snackbar);
        Log.v(SnackbarCallback.class.getSimpleName(), "onSnackbarSwiped");
      }

      @Override
      public void onSnackbarTimedOut(Snackbar snackbar) {
        super.onSnackbarTimedOut(snackbar);
        Log.v(SnackbarCallback.class.getSimpleName(), "onSnackbarTimedOut");
      }

      @Override
      public void onSnackbarManuallyDismissed(Snackbar snackbar) {
        super.onSnackbarManuallyDismissed(snackbar);
        Log.v(SnackbarCallback.class.getSimpleName(), "onSnackbarManuallyDismissed");
      }

      @Override
      public void onSnackbarDismissedAfterAnotherShown(Snackbar snackbar) {
        super.onSnackbarDismissedAfterAnotherShown(snackbar);
        Log.v(SnackbarCallback.class.getSimpleName(), "onSnackbarDismissedAfterAnotherShown");
      }
    };
  }

  @Test
  public void whenOnSnackbarShown_thenSnackbarShownMessageLogged() {
    callbackUnderTest.onSnackbarShown(snackbar);

    List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
    assertThat(logs).hasSize(1);
    assertThat(logs.get(0).msg).isEqualTo("onSnackbarShown");
  }

  @Test
  public void whenOnSnackbarDismissed_thenSnackbarDismissedMessageLogged() {
    callbackUnderTest.onSnackbarDismissed(snackbar);

    List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
    assertThat(logs).hasSize(1);
    assertThat(logs.get(0).msg).isEqualTo("onSnackbarDismissed");
  }

  @Test
  public void whenOnSnackbarDismissedWithEventType_thenSnackbarDismissedMessageLogged() {
    callbackUnderTest.onSnackbarDismissed(snackbar, Callback.DISMISS_EVENT_TIMEOUT);

    List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
    assertThat(logs).hasSize(1);
    assertThat(logs.get(0).msg).isEqualTo("onSnackbarDismissed method = 2");
  }

  @Test
  public void whenOnSnackbarActionPressed_thenSnackbarActionPressedMessageLogged() {
    callbackUnderTest.onSnackbarActionPressed(snackbar);

    List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
    assertThat(logs).hasSize(1);
    assertThat(logs.get(0).msg).isEqualTo("onSnackbarActionPressed");
  }

  @Test
  public void whenOnSnackbarSwiped_thenSnackbarSwipedMessageLogged() {
    callbackUnderTest.onSnackbarSwiped(snackbar);

    List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
    assertThat(logs).hasSize(1);
    assertThat(logs.get(0).msg).isEqualTo("onSnackbarSwiped");
  }

  @Test
  public void whenOnSnackbarTimedOut_thenSnackbarTimedOutMessageLogged() {
    callbackUnderTest.onSnackbarTimedOut(snackbar);

    List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
    assertThat(logs).hasSize(1);
    assertThat(logs.get(0).msg).isEqualTo("onSnackbarTimedOut");
  }

  @Test
  public void whenOnSnackbarManuallyDismissed_thenSnackbarManuallyDismissedMessageLogged() {
    callbackUnderTest.onSnackbarManuallyDismissed(snackbar);

    List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
    assertThat(logs).hasSize(1);
    assertThat(logs.get(0).msg).isEqualTo("onSnackbarManuallyDismissed");
  }

  @Test
  public void whenOnSnackbarDismissedAfterAnotherShown_thenSnackbarDismissedAfterAnotherShownMessageLogged() {
    callbackUnderTest.onSnackbarDismissedAfterAnotherShown(snackbar);

    List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
    assertThat(logs).hasSize(1);
    assertThat(logs.get(0).msg).isEqualTo("onSnackbarDismissedAfterAnotherShown");
  }

}