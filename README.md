# SnackbarBuilder

[![Build Status](https://travis-ci.org/andrewlord1990/SnackbarBuilder.svg?branch=master)](https://travis-ci.org/andrewlord1990/SnackbarBuilder)
[![Coverage Status](https://coveralls.io/repos/andrewlord1990/SnackbarBuilder/badge.svg?branch=master&service=github)](https://coveralls.io/github/andrewlord1990/SnackbarBuilder?branch=master)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-SnackbarBuilder-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/3357)
[![License](https://img.shields.io/badge/license-Apache%202.0-green.svg) ](https://github.com/andrewlord1990/SnackbarBuilder/blob/master/LICENSE)
[![API](https://img.shields.io/badge/API-9%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=9)
[![Download](https://api.bintray.com/packages/andrewlord1990/maven/snackbar-builder/images/download.svg) ](https://bintray.com/andrewlord1990/maven/snackbar-builder/_latestVersion)

The Android Design Support library introduced the `Snackbar`. SnackbarBuilder provides a builder pattern that not only makes Snackbars easier to create, but it also provides some extra customisations.

One of the main annoyances with the `Snackbar` is that it has a dark background, but takes the default text color from your theme, which is often dark as well. This makes the messages hard to read and requires you to retrieve the `TextView` yourself and change the text color. `SnackbarBuilder` defaults the text color to white and then allows you to choose your own colour if you wish.


## Download

Download via Gradle:
```groovy
compile 'com.github.andrewlord1990:snackbarbuilder:0.7.0'
```

The library is available on Bintray (JCenter).

```groovy
repositories {
  jcenter()
}
```

## Main Features

- A builder pattern to create Snackbars.
- Customise message and action text color with global theme attributes and on a per-Snackbar basis.
- Dismiss callbacks with separate methods for each type of dismiss event.
- Theme attribute for the default view ID in an activity to attach Snackbars to, so you don't need to provide the parent view each time you wish to show one.
- Set the default duration to use through a theme attribute.
- Append text, which allows you to have multiple text colours.
- Add an icon to the Snackbar if you wish.
- A ToastBuilder to create Toast messages, with a builder pattern and global theme attributes.

## Usage

### Create

It is really easy to build and customise snackbars.

```java
Snackbar snackbar = new SnackbarBuilder(this)
      .message("message")
      .actionText("Action")
      .snackbarCallback(new MySnackbarCallback())
      .build()
      .show();
```

Check out the sample project to see examples of how the library can be used.

### Callback

Simply implement the interface for the callback you are interested in and call the appropriate method on the `SnackbarBuilder`. It is as simple as that!

- Shown = `showCallback(SnackbarShowCallback callback)`
- Dismissed = `dismissCallback(SnackbarDismissCallback callback)`
- Dismissed (action pressed) = `actionDismissCallback(SnackbarActionDismissCallback callback)`
- Dismissed (swiped away) = `swipeDismissCallback(SnackbarSwipeDismissCallback callback)`
- Dismissed (timeout) = `timeoutDismissCallback(SnackbarTimeoutDismissCallback callback)`
- Dismissed (manual dismiss call) = `manualDismissCallback(SnackbarManualDismissCallback callback)`
- Dismissed (another Snackbar shown) = `consecutiveDismissCallback(SnackbarConsecutiveDismissCallback callback)`

```java
  new SnackbarBuilder(this)
      .message("Message")
      .actionText("Action")
      .showCallback(new MyShowCallback())
      .actionDismissCallback(new MyActionDismissCallback())
      .timeoutDismissCallback(new MyTimeoutDismissCallback())
      .build()
      .show();
```

Alternatively, if you would rather handle all the callbacks with a single class, then you can do that too. Simply extend the `SnackbarCallback` class and override only the methods you are interested in. Rather than needing to check the `dismissEvent` integer in the `Snackbar.Callback` class, there is a separate method to override for each dismiss type.


### ToastBuilder

Makes it very easy to create and customise `Toast` messages.

```java
new ToastBuilder(this)
    .message("message")
    .messageTextColor(Color.BLUE)
    .duration(Toast.LENGTH_LONG)
    .gravity(Gravity.TOP)
    .build()
    .show();
```

The builder allows you to change the text color of the displayed message. If you wish to change the background you can provide a custom view for the `Toast`. By providing the ID of a `TextView` within this view, you can set the message on it through the builder method `message(String)`.

### Theme

You can provide defaults to the `SnackbarBuilder` and the `ToastBuilder` through theme attributes.

```xml
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    <item name="colorAccent">@color/colorAccent</item>

    <item name="snackbarBuilderStyle">@style/SampleSnackbarStyle</item>
    <item name="toastBuilderStyle">@style/SampleToastStyle</item>
</style>

<style name="SampleSnackbarStyle" parent="SnackbarBuilder">
    <item name="snackbarBuilder_parentViewId">@id/coordinator</item>
    <item name="snackbarBuilder_duration">shortTime</item>
    <item name="snackbarBuilder_messageTextColor">@color/messageText</item>
    <item name="snackbarBuilder_actionTextColor">@color/colorAccent</item>
</style>

<style name="SampleToastStyle" parent="ToastBuilder">
    <item name="toastBuilder_messageTextColor">@color/snackbarbuilder_default_message</item>
    <item name="toastBuilder_duration">shortTime</item>
</style>
```

## Suggestions

If there is any features that have been missed that you are interested in then please open an Issue.

## License

    Copyright 2015 Andrew Lord

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
