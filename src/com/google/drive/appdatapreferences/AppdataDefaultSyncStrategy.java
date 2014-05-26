package com.google.drive.appdatapreferences;

import android.util.Log;

/**
 * If there are local changes, sync the latest local version with remote and ignore merge conflicts.
 * The last write wins.
 *
 * If there are no local changes, fetch the latest remote version.
 *
 * @author Alejandro Rivera (alejandro.rivera.lopez@gmail.com)
 * @since 2014-05-26 17:14
 */
public class AppdataDefaultSyncStrategy implements AppdataSyncStrategy {

    public static final String TAG = AppdataPreferencesSyncer.TAG + "_default_strategy";

    @Override
    public Resolution getSyncResolution(SyncContext context) {
        if (context.getLocalJson() != null && !context.getLocalJson().equals(context.getLastSyncedJson())) {
            Log.d(TAG, "There are local changes. Ignore remote values and push local values");
            return Resolution.PUSH_LOCAL_TO_REMOTE;
        } else {
            Log.d(TAG, "There are no local changes. Fetch remote values.");
            return Resolution.PUSH_REMOTE_TO_LOCAL;
        }
    }
}
