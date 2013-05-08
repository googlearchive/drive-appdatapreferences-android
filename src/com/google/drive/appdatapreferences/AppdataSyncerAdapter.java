package com.google.drive.appdatapreferences;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

/**
 * Sync adapter that invokes syncing of the preferences file
 * between remote Drive appdata directory and the client.
 *
 * @author jbd@google.com (Burcu Dogan)
 */
public class AppdataSyncerAdapter extends AbstractThreadedSyncAdapter {

  private final Context mContext;

  /**
   * Constructs a {@code AppdataSyncerAdapter} instance.
   * @param context
   * @param autoInitialize
   */
  public AppdataSyncerAdapter(Context context, boolean autoInitialize) {
    super(context, autoInitialize);
    mContext = context;
  }

  /**
   * Invokes the the syncing operations to fetch the remote
   * preference file and update the local copy.
   */
  @Override
  public void onPerformSync(
      Account account, Bundle bundle, String authority,
      ContentProviderClient provider, SyncResult syncResult) {
    Log.d(TAG, "Syncing the preferences....");
    // TODO: experiment exponential backoff for erroneous cases.
    // TODO: update syncResult.stats accordingly
    AppdataPreferencesSyncer.get(mContext).sync();
  }

  private final static String TAG = "syncer";

}
