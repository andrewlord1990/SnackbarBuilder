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
public class SnackbarCallbackWrapperTest {

  @Mock
  Callback callback;
  @Mock
  Snackbar snackbar;
  private SnackbarCallbackWrapper callbackUnderTest;

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);

    callbackUnderTest = new SnackbarCallbackWrapper(callback);
  }

  @Test
  public void whenCreated_thenSetupCorrectly() throws Exception {
    assertThat(callbackUnderTest.callback).isEqualTo(callback);
  }

  @Test
  public void whenOnDismissed_thenCallbackInformedOfEvent() {
    int dismissEvent = Callback.DISMISS_EVENT_MANUAL;

    callbackUnderTest.onDismissed(snackbar, dismissEvent);

    verify(callback).onDismissed(snackbar, dismissEvent);
  }

  @Test
  public void whenOnShown_thenCallbackInformedOfEvent() {
    callbackUnderTest.onShown(snackbar);

    verify(callback).onShown(snackbar);
  }
}