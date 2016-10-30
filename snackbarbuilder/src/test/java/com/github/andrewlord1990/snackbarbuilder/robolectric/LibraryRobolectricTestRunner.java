/*
 * Copyright (C) 2016 Andrew Lord
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.github.andrewlord1990.snackbarbuilder.robolectric;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.FileFsFile;
import org.robolectric.res.FsFile;

public class LibraryRobolectricTestRunner extends RobolectricGradleTestRunner {

  public LibraryRobolectricTestRunner(Class<?> klass) throws InitializationError {
    super(klass);
  }

  @Override
  protected AndroidManifest getAppManifest(Config config) {
    AndroidManifest appManifest = super.getAppManifest(config);
    FsFile androidManifestFile = appManifest.getAndroidManifestFile();

    if (androidManifestFile.exists()) {
      return appManifest;
    } else {
      androidManifestFile = FileFsFile.from(getModuleRootPath(config),
          appManifest.getAndroidManifestFile().getPath().replace("manifests/full", "manifests/aapt"));
      return new AndroidManifest(androidManifestFile, appManifest.getResDirectory(), appManifest.getAssetsDirectory());
    }
  }

  private String getModuleRootPath(Config config) {
    String moduleRoot = config.constants().getResource("").toString().replace("file:", "");
    return moduleRoot.substring(0, moduleRoot.indexOf("/build"));
  }

}
