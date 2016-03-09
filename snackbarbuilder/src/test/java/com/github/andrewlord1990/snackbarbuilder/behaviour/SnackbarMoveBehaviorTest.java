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

package com.github.andrewlord1990.snackbarbuilder.behaviour;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar.SnackbarLayout;
import android.view.View;

import com.github.andrewlord1990.snackbarbuilder.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
public class SnackbarMoveBehaviorTest {

    private SnackbarMoveBehavior behavior;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        SnackbarMoveBehavior.BEHAVIOR_ENABLED = true;
        RuntimeEnvironment.application.setTheme(R.style.TestSnackbarBuilder_AppTheme);
        behavior = new SnackbarMoveBehavior();
    }

    @Test
    public void givenBehaviorDisabled_whenLayoutDependsOn_thenFalse() {
        //Given
        SnackbarMoveBehavior.BEHAVIOR_ENABLED = false;
        CoordinatorLayout coordinatorLayout = new CoordinatorLayout(RuntimeEnvironment.application);
        View child = new View(RuntimeEnvironment.application);
        View dependency = new View(RuntimeEnvironment.application);

        //When
        boolean actual = behavior.layoutDependsOn(coordinatorLayout, child, dependency);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    public void givenBehaviorEnabledAndSnackbarLayout_whenLayoutDependsOn_thenTrue() {
        //Given
        CoordinatorLayout coordinatorLayout = new CoordinatorLayout(RuntimeEnvironment.application);
        View child = new View(RuntimeEnvironment.application);
        SnackbarLayout dependency = new SnackbarLayout(RuntimeEnvironment.application);

        //When
        boolean actual = behavior.layoutDependsOn(coordinatorLayout, child, dependency);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    public void givenBehaviourDisabled_whenOnDependentViewChanged_thenFalse() {
        //Given
        SnackbarMoveBehavior.BEHAVIOR_ENABLED = false;
        CoordinatorLayout coordinatorLayout = new CoordinatorLayout(RuntimeEnvironment.application);
        View child = new View(RuntimeEnvironment.application);
        View dependency = new View(RuntimeEnvironment.application);

        //When
        boolean actual = behavior.onDependentViewChanged(coordinatorLayout, child, dependency);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    public void givenChildViewNotVisible_whenOnDependentViewChanged_thenFalse() {
        //Given
        CoordinatorLayout coordinatorLayout = new CoordinatorLayout(RuntimeEnvironment.application);
        View child = new View(RuntimeEnvironment.application);
        child.setVisibility(View.GONE);
        View dependency = new View(RuntimeEnvironment.application);

        //When
        boolean actual = behavior.onDependentViewChanged(coordinatorLayout, child, dependency);

        //Then
        assertThat(actual).isFalse();
    }

}