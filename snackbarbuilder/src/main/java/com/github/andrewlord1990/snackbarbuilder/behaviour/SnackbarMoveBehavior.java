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

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class SnackbarMoveBehavior extends CoordinatorLayout.Behavior<View> {

    static boolean BEHAVIOR_ENABLED = VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
    private static final float FRACTION_OF_HEIGHT_AT_WHICH_TO_ANIMATE = 0.667f;

    private ObjectAnimator translationAnimator;
    private float verticalTranslation;

    public SnackbarMoveBehavior() {
    }

    public SnackbarMoveBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return BEHAVIOR_ENABLED && dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child,
                                          View dependency) {
        return BEHAVIOR_ENABLED && updateTranslationForSnackbar(parent, child);
    }

    @TargetApi(VERSION_CODES.HONEYCOMB)
    private boolean updateTranslationForSnackbar(CoordinatorLayout parent, View view) {
        if (view.getVisibility() != View.VISIBLE) {
            return false;
        }

        float targetVerticalTranslation = getVerticalTranslationForSnackbar(parent, view);
        if (isAtTargetVerticalTranslation(targetVerticalTranslation)) {
            return false;
        }

        translateView(view, targetVerticalTranslation);
        return true;
    }

    @TargetApi(VERSION_CODES.HONEYCOMB)
    private void translateView(View view, float targetVerticalTranslation) {
        float currentVerticalTranslation = view.getTranslationY();

        cancelAnimatorIfRunning();

        if (shouldAnimateTranslation(view, targetVerticalTranslation,
                currentVerticalTranslation)) {
            updateTranslationWithAnimation(view, targetVerticalTranslation,
                    currentVerticalTranslation);
        } else {
            view.setTranslationY(targetVerticalTranslation);
        }
        verticalTranslation = targetVerticalTranslation;
    }

    @TargetApi(VERSION_CODES.HONEYCOMB)
    private void cancelAnimatorIfRunning() {
        if (translationAnimator != null && translationAnimator.isRunning()) {
            translationAnimator.cancel();
        }
    }

    @TargetApi(VERSION_CODES.HONEYCOMB)
    private void updateTranslationWithAnimation(View view, float targetVerticalTranslation,
                                                float currentVerticalTranslation) {
        if (translationAnimator == null) {
            createAnimator(view, targetVerticalTranslation, currentVerticalTranslation);
        }
        translationAnimator.start();
    }

    @TargetApi(VERSION_CODES.HONEYCOMB)
    private void createAnimator(View view, float targetVerticalTranslation,
                                float currentVerticalTranslation) {
        translationAnimator = ObjectAnimator.ofFloat(view, "translationY",
                currentVerticalTranslation, targetVerticalTranslation);
        translationAnimator.setInterpolator(new FastOutSlowInInterpolator());
    }

    private boolean shouldAnimateTranslation(View view,
                                             float targetVerticalTranslation,
                                             float currentVerticalTranslation) {
        float thresholdAtWhichToAnimate = view.getHeight() * FRACTION_OF_HEIGHT_AT_WHICH_TO_ANIMATE;
        float translationDistance = currentVerticalTranslation - targetVerticalTranslation;
        return Math.abs(translationDistance) > thresholdAtWhichToAnimate;
    }

    private boolean isAtTargetVerticalTranslation(float targetVerticalTranslation) {
        return verticalTranslation == targetVerticalTranslation;
    }

    @TargetApi(VERSION_CODES.HONEYCOMB)
    private float getVerticalTranslationForSnackbar(CoordinatorLayout parent,
                                                    View view) {
        float minOffset = 0;
        List<View> dependencies = parent.getDependencies(view);
        for (View dependency : dependencies) {
            if (dependency instanceof Snackbar.SnackbarLayout
                    && parent.doViewsOverlap(view, dependency)) {
                minOffset = Math.min(minOffset,
                        dependency.getTranslationY() - dependency.getHeight());
            }
        }

        return minOffset;
    }

}
