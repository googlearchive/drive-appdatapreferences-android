package com.google.drive.appdatapreferences.tasks;

import java.io.IOException;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

/**
 * Updates a remote preferences file on appdata folder.
 *
 * @author jbd@google.com (Burcu Dogan)
 */
public class UpdatePreferencesDriveTask extends DriveTask {

  /**
   * Constructs a new task.
   * @param driveService A drive service.
   */
  public UpdatePreferencesDriveTask(Drive driveService) {
    super(driveService);
  }

  /**
   * Executes the request.
   * @param content The new file content.
   * @throws IOException
   */
  public void execute(String content) throws IOException {
    // updates the existing preferences file with
    // the preferences
    File preferences = getPreferencesFile();
    updatePreferencesFile(preferences, content);
  }

}
