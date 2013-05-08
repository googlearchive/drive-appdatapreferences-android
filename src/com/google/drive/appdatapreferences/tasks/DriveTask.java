/**
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.drive.appdatapreferences.tasks;

import java.io.IOException;
import java.util.Arrays;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.google.drive.appdatapreferences.Utils;

/**
 * Provides fundamental base abstractions for
 * @author jbd@google.com (Burcu Dogan)
 *
 */
public abstract class DriveTask {

  private Drive mDriveService = null;

  /**
   * Constructs an object.
   * @param driveService
   */
  public DriveTask(Drive driveService) {
    mDriveService = driveService;
  }

  /**
   * Getter for the Drive service.
   */
  public Drive getDriveService() {
    return mDriveService;
  }

  /**
   * Inserts preferences file into the appdata folder.
   * @param content The application context.
   * @return Inserted file.
   * @throws IOException
   */
  public File insertPreferencesFile(String content) throws IOException {
    File metadata = new File();
    metadata.setTitle(FILE_NAME);
    metadata.setParents(Arrays.asList(new ParentReference().setId("appdata")));
    ByteArrayContent c =
        ByteArrayContent.fromString(FILE_MIME_TYPE, content);
    return mDriveService.files().insert(metadata, c).execute();
  }

  /**
   * Updates the preferences file with content.
   * @param file File metadata.
   * @param content File content in JSON.
   * @return Updated file.
   * @throws IOException
   */
  public File updatePreferencesFile(File file, String content)
      throws IOException {
    ByteArrayContent c =
        ByteArrayContent.fromString(FILE_MIME_TYPE, content);
    return mDriveService.files().update(file.getId(), file, c).execute();
  }

  /**
   * Retrieves the preferences file from the appdata folder.
   * @return Retrieved preferences file or {@code null}.
   * @throws IOException
   */
  public File getPreferencesFile() throws IOException {
    // TODO: fix the contains query once title querying bug is being resolved.
    String query =
        "title contains '" + FILE_NAME + "' and 'appdata' in parents";
    FileList list = mDriveService.files().list().setQ(query).execute();
    if (list != null && list.getItems().size() > 0) {
      return list.getItems().get(0);
    } else {
      // create a new preferences file
      return insertPreferencesFile("{}");
    }
  }

  /**
   * Downloads the file contents.
   * @param file File to download.
   * @return The file content.l
   * @throws IOException
   */
  public String downloadFile(File file) throws IOException {
    HttpResponse res = mDriveService.getRequestFactory()
        .buildGetRequest(new GenericUrl(file.getDownloadUrl())).execute();
    return Utils.fromInputStreamtoString(res.getContent());
  }

  final public static String FILE_NAME = "preferences.json";
  final public static String FILE_MIME_TYPE = "application/json";

}
