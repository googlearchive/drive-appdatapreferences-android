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

package com.google.drive.appdatapreferences;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.drive.appdatapreferences.tasks.GetOrCreatePreferencesDriveTask;
import com.google.drive.appdatapreferences.tasks.UpdatePreferencesDriveTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Manages the synchronization between an appdata preferences file
 * and a local SharedPreferences instance.
 *
 * @author jbd@google.com (Burcu Dogan)
 */
public class AppdataPreferencesSyncer {

  private static AppdataPreferencesSyncer sInstance;

  private final Context mContext;
  private GoogleAccountCredential mCredential;
  private SharedPreferences mPreferences;
  private OnChangeListener mOnChangeListener;
  private OnUserRecoverableAuthExceptionExceptionListener mOnExceptionListener;
  private AppdataPreferencesSyncManager mSyncManager;
  private String mLastSyncedJson;

  /**
   * Gets the singleton {@code AppdataPreferencesSyncer} instance.
   * @param context Context of the application
   */
  public static AppdataPreferencesSyncer get(Context context) {
    if (sInstance == null) {
      sInstance = new AppdataPreferencesSyncer(context);
    }
    return sInstance;
  }

  /**
   * Private {@code AppdataPreferencesSyncer} constructor.
   * @param context Context of the application
   */
  private AppdataPreferencesSyncer(Context context) {
    mContext = context;
  }
  
  /**
   * Binds a {@code SharedPreferences} object to a Google account.
   * @param credential  Google account credentials.
   * @param preferences Preferences to be bound to the Google account.
   */
  public void bind(GoogleAccountCredential credential,
      SharedPreferences preferences) {
    setCredential(credential);
    setPreferences(preferences);
  }

  /**
   * Syncs the preferences file with an appdata preferences file.
   *
   * Synchronization steps:
   * 1. If there are local changes, sync the latest local version with remote
   *    and ignore merge conflicts. The last write wins.
   * 2. If there are no local changes, fetch the latest remote version. If
   *    it includes changes, notify that preferences have changed.
   */
  public synchronized void sync() {
    // TODO: don't silently ignore the sync operation
    // notify user that preferences and credential are not set.
    if (mPreferences == null || mCredential == null) {
      return;
    }
    // check if the values are changed since last update
    Map<String, ?> values = mPreferences.getAll();
    String localJson = GSON.toJson(values);
    try {
      if (values.size() > 0 && localJson != null && !localJson.equals(mLastSyncedJson)) {
        updateRemote(localJson);
      } else {
        updateLocal();
      }
    } catch (IOException e) {
      handleException(e);
    }
  }

  /**
   * Gets the context.
   */
  public Context getContext() {
    return mContext;
  }

  /**
   * Gets the sync manager.
   */
  public AppdataPreferencesSyncManager getSyncManager() {
    return mSyncManager;
  }

  /**
   * Gets the credential.
   */
  public GoogleAccountCredential getCredential() {
    return mCredential;
  }

  /**
   * Gets the preferences.
   */
  public SharedPreferences getPreferences() {
    return mPreferences;
  }

  /**
   * Constructs a Drive service in the current context and with the
   * credentials use to initiate AppdataPreferences instance.
   * @return Drive service instance.
   */
  public Drive getDriveService() {
    Drive service = new Drive.Builder(
        AndroidHttp.newCompatibleTransport(),
        new GsonFactory(),
        mCredential).build();
    return service;
  }

  /**
   * Sets the credential and starts a periodic sync for the
   * selected account.
   * @param credential User's credential
   */
  public void setCredential(GoogleAccountCredential credential) {
    mCredential = credential;
    mSyncManager =
        new AppdataPreferencesSyncManager(credential.getSelectedAccount());
    mSyncManager.startPeriodicSync();
  }

  /**
   * Sets the {@code SharedPreferences} instance.
   * @param preferences {@code SharedPreferences} instance to be synced.
   */
  public void setPreferences(SharedPreferences preferences) {
    mPreferences = preferences;
  }

  /**
   * Sets a {@code UserRecoverableAuthExceptionListener}.
   * @param listener
   */
  public void setOnUserRecoverableAuthExceptionListener(
      OnUserRecoverableAuthExceptionExceptionListener listener) {
    mOnExceptionListener = listener;
  }

  /**
   * Sets the OnChangeListener.
   * @param listener
   */
  public void setOnChangeListener(OnChangeListener listener) {
    mOnChangeListener = listener;
  }

  /**
   * Updates the remote preferences file with the given JSON content.
   * @param json    New contents of the remote preferences file in JSON.
   * @throws IOException
   */
  private void updateRemote(String json) throws IOException {
    Log.d(TAG, "Updating the remote preferences file");
    // update the remote
    new UpdatePreferencesDriveTask(getDriveService()).execute(json);
    mLastSyncedJson = json;
  }

  /**
   * Updates the local SharedPreferences instance with the remote
   * changes and calls OnChangeListener.
   * @throws IOException
   */
  private void updateLocal() throws IOException {
    Log.d(TAG, "Updating the local preferences file");
    // update the local preferences
    HashMap<String, Object> remoteObj = null;
    String json =
        new GetOrCreatePreferencesDriveTask(getDriveService()).execute();
    Type type = new TypeToken<HashMap<String, Object>>() {}.getType();
    remoteObj = GSON.fromJson(json, type);
    Utils.replaceValues(mPreferences, remoteObj);
    // Notify if there are changes
    if (json != mLastSyncedJson && mOnChangeListener != null) {
      mOnChangeListener.onChange(mPreferences);
      mLastSyncedJson = json;
    }
  }

  /**
   * Handles API exceptions and notifies OnExceptionListener
   * if given exception is a UserRecoverableAuthIOException.
   * @param exception Exception to handle
   */
  private void handleException(Exception exception) {
    if (mOnExceptionListener == null) {
      return;
    }
    if (exception instanceof UserRecoverableAuthIOException) {
      mOnExceptionListener.onUserRecoverableAuthException(
          (UserRecoverableAuthIOException)exception);
    } else {
      exception.printStackTrace();
    }
  }

  /**
   * Called when remote preferences file is changed and changes
   * are synced to the local SharedPreferences instance.
   */
  public interface OnChangeListener {
    public void onChange(SharedPreferences prefs);
  }

  /**
   * Called when Google Drive API requests responds with a 401 or 403 and
   * user's permission is required.
   */
  public interface OnUserRecoverableAuthExceptionExceptionListener {
    public void onUserRecoverableAuthException(
        UserRecoverableAuthIOException ex);
  }

  final public static Gson GSON = new GsonBuilder().create();
  final public static String TAG = "syncer";

}
