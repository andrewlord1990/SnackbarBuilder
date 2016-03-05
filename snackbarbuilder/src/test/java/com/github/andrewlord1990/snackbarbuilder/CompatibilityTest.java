/*
 * Copyright (C) 2016 Andrew Lord
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.github.andrewlord1990.snackbarbuilder;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
public class CompatibilityTest {

    @Mock
    TextView textView;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @TargetApi(VERSION_CODES.ICE_CREAM_SANDWICH)
    public void givenAfterSdk14_whenSetAllCaps_thenSetAllCapsCalled() {
        //When
        Compatibility.getInstance().setAllCaps(textView, false);

        //Then
        verify(textView).setAllCaps(anyBoolean());
    }

}