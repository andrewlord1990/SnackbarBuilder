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

package com.github.andrewlord1990.snackbarbuilder.callback;

import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowLog.LogItem;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
public class SnackbarCallbackTest {

    private SnackbarCallback callbackUnderTest;

    @Mock
    Snackbar snackbar;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        callbackUnderTest = new SnackbarCallback();
    }

    @Test
    public void whenOnSnackbarShown_thenSnackbarShownMessageLogged() {
        //When
        callbackUnderTest.onSnackbarShown(snackbar);

        //Then
        List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).msg).isEqualTo("onSnackbarShown");
    }

    @Test
    public void whenOnSnackbarDismissed_thenSnackbarDismissedMessageLogged() {
        //When
        callbackUnderTest.onSnackbarDismissed(snackbar);

        //Then
        List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).msg).isEqualTo("onSnackbarDismissed");
    }

    @Test
    public void whenOnSnackbarDismissedWithEventType_thenSnackbarDismissedMessageLogged() {
        //When
        callbackUnderTest.onSnackbarDismissed(snackbar, Callback.DISMISS_EVENT_TIMEOUT);

        //Then
        List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).msg).isEqualTo("onSnackbarDismissed method = 2");
    }

    @Test
    public void whenOnSnackbarActionPressed_thenSnackbarActionPressedMessageLogged() {
        //When
        callbackUnderTest.onSnackbarActionPressed(snackbar);

        //Then
        List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).msg).isEqualTo("onSnackbarActionPressed");
    }

    @Test
    public void whenOnSnackbarSwiped_thenSnackbarSwipedMessageLogged() {
        //When
        callbackUnderTest.onSnackbarSwiped(snackbar);

        //Then
        List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).msg).isEqualTo("onSnackbarSwiped");
    }

    @Test
    public void whenOnSnackbarTimedOut_thenSnackbarTimedOutMessageLogged() {
        //When
        callbackUnderTest.onSnackbarTimedOut(snackbar);

        //Then
        List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).msg).isEqualTo("onSnackbarTimedOut");
    }

    @Test
    public void whenOnSnackbarManuallyDismissed_thenSnackbarManuallyDismissedMessageLogged() {
        //When
        callbackUnderTest.onSnackbarManuallyDismissed(snackbar);

        //Then
        List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).msg).isEqualTo("onSnackbarManuallyDismissed");
    }

    @Test
    public void whenOnSnackbarDismissedAfterAnotherShown_thenSnackbarDismissedAfterAnotherShownMessageLogged() {
        //When
        callbackUnderTest.onSnackbarDismissedAfterAnotherShown(snackbar);

        //Then
        List<LogItem> logs = ShadowLog.getLogsForTag(SnackbarCallback.class.getSimpleName());
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).msg).isEqualTo("onSnackbarDismissedAfterAnotherShown");
    }

}