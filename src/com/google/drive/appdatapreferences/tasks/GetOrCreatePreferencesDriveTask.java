package com.google.drive.appdatapreferences.tasks;

import java.io.IOException;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

/**
 * Gets or creates a preferences file on user's appdata
 * folder.
 *
 * @author jbd@google.com (Burcu Dogan)
 *
 */
public class GetOrCreatePreferencesDriveTask extends DriveTask {

  /**
   * Constructs a new get or create task.
   * @param driveService A drive service.
   */
  public GetOrCreatePreferencesDriveTask(Drive driveService) {
    super(driveService);
  }

  /**
   * Executes the request..
   * @return Remote file's content.
   * @throws IOException
   */
  public String execute() throws IOException {
    File file = getPreferencesFile();
    if (file.getDownloadUrl() != null) {
      // retrieve the content
      return downloadFile(file);
    }
    return null;
  }

}
