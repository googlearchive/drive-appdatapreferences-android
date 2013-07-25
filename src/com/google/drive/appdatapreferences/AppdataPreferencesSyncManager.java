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

import android.accounts.Account;
import android.content.ContentResolver;
import android.os.Bundle;

/**
 * Helps to manage the syncer. Provides utilities to start, stop
 * and schedule synchronization.
 *
 * @author jbd@google.com (Burcu Dogan)
 */
public class AppdataPreferencesSyncManager {

  private Account mAccount;
  private final Bundle mParams;

  /**
   * Constructs a sync manager for the given account.
   * @param account The Google account that preferences will be synced to
   */
  public AppdataPreferencesSyncManager(Account account) {
    this(account, new Bundle());
  }

  /**
   * Constructs a sync manager for the given with the params.
   * @param account The Google account that preferences will be synced to
   * @param params  Parameters
   */
  public AppdataPreferencesSyncManager(Account account, Bundle params) {
    mAccount = account;
    mParams = params;
    ADPLog.getInstance().setBundle(params);
  }

  /**
   * Starts the synchronization.
   */
  public void startSync() {
    ContentResolver.setIsSyncable(mAccount, AUTHORITY, 1);
  }

  /**
   * Stops the synchronization.
   */
  public void stopSync() {
    ContentResolver.setIsSyncable(mAccount, AUTHORITY, 0);
  }

  /**
   * Schedules the sync tasks to occur periodically.
   */
  public void startPeriodicSync() {
    startSync();
    int freqInSecs = mParams.getInt(KEY_FREQ, 3);
    ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
    ContentResolver.addPeriodicSync(mAccount, AUTHORITY, mParams, freqInSecs);
  }

  /**
   * Explicitly request a sync operation.
   */
  public void requestSync() {
    ContentResolver.requestSync(mAccount, AUTHORITY, mParams);
  }

  /**
   * Sets the account that sync manager is managing. Changes doesn't reflect
   * until a new start/stop action.
   * @param account
   */
  public void setAccount(Account account) {
    mAccount = account;
  }

  public final static String AUTHORITY = "com.google.drive.appdatapreferences";
  public final static String KEY_FREQ = "freqInSecs";

}
