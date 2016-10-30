/*
 * Copyright (C) 2016 Andrew Lord
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 *
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.github.andrewlord1990.snackbarbuilder;

import android.annotation.TargetApi;
import android.os.Build;
import android.widget.TextView;

import com.github.andrewlord1990.snackbarbuilder.robolectric.LibraryRobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

@RunWith(LibraryRobolectricTestRunner.class)
public class TextViewExtensionTest {

  @Mock
  TextView textView;

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  public void givenAfterSdk14_whenSetAllCaps_thenSetAllCapsCalled() {
    TextViewExtension.from(textView).setAllCaps(false);

    verify(textView).setAllCaps(false);
  }

}