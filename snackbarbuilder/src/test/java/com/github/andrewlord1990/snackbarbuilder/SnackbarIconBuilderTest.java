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

import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.SnackbarLayout;
import android.widget.ImageView;

import com.github.andrewlord1990.snackbarbuilder.robolectric.LibraryRobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.android.api.Assertions.assertThat;

@RunWith(LibraryRobolectricTestRunner.class)
public class SnackbarIconBuilderTest {

  @Mock
  Drawable icon1;
  @Mock
  Drawable icon2;
  private CoordinatorLayout snackbarParentView;

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);

    RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
    snackbarParentView = new CoordinatorLayout(RuntimeEnvironment.application);
  }

  @Test
  public void givenIconAlready_whenBindToSnackbar_thenIconUpdated() {
    Snackbar snackbar = new SnackbarBuilder(snackbarParentView)
        .icon(icon1)
        .iconMarginStart(50)
        .iconMarginEnd(100)
        .build();
    SnackbarIconBuilder iconBuilder = new SnackbarIconBuilder(snackbar);
    int expectedMarginStart = 10;
    int expectedMarginEnd = 20;

    iconBuilder
        .icon(icon2)
        .iconMarginStartPixels(expectedMarginStart)
        .iconMarginEndPixels(expectedMarginEnd)
        .bindToSnackbar();

    ImageView iconView = getIconView(iconBuilder.snackbar);
    assertThat(iconView).hasDrawable(icon2);
    assertThat((SnackbarLayout.LayoutParams) iconView.getLayoutParams())
        .hasLeftMargin(expectedMarginStart)
        .hasRightMargin(expectedMarginEnd);
  }

  @Test
  public void givenNoIconAlready_whenBindToSnackbar_thenIconAdded() {
    int expectedMarginStart = 10;
    int expectedMarginEnd = 20;
    Snackbar snackbar = new SnackbarBuilder(snackbarParentView).build();
    SnackbarIconBuilder iconBuilder = new SnackbarIconBuilder(snackbar);

    iconBuilder
        .icon(icon2)
        .iconMarginStartPixels(expectedMarginStart)
        .iconMarginEndPixels(expectedMarginEnd)
        .bindToSnackbar();

    ImageView iconView = getIconView(iconBuilder.snackbar);
    assertThat(iconView).hasDrawable(icon2);
    assertThat((SnackbarLayout.LayoutParams) iconView.getLayoutParams())
        .hasLeftMargin(expectedMarginStart)
        .hasRightMargin(expectedMarginEnd);
  }

  private ImageView getIconView(Snackbar snackbar) {
    return (ImageView) snackbar.getView().findViewById(R.id.snackbarbuilder_icon);
  }

}