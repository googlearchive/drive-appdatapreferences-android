package com.google.drive.appdatapreferences;

import android.util.Log;

/**
 * Strategy where the last-modified wins, regardless of remote or local.
 * Note: there's no conflict detection/resolution in this model.
 *
 * @author Alejandro Rivera (alejandro.rivera.lopez@gmail.com)
 * @since 2014-05-26 17:14
 */
public class AppdataTimestampSyncStrategy implements AppdataSyncStrategy {

    public static final String TAG = AppdataPreferencesSyncer.TAG + "_timestamp_strategy";
    public static final String LAST_UPDATE_KEY = "_last_update";

    @Override
    public Resolution getSyncResolution(SyncContext context) {

        Long remoteLastUpdated = getRemoteTimestamp(context);
        Long localLastUpdated = getLocalTimestamp(context);

        Log.d(TAG, "Timestamps: remoteLastUpdated=" + remoteLastUpdated + ", localLastUpdated=" + localLastUpdated);
        if (remoteLastUpdated == null && localLastUpdated == null) {
            Log.w(TAG, "No timestamp info. Reverting to default sync resolution");
            return new AppdataDefaultSyncStrategy().getSyncResolution(context);
        }
        else if (remoteLastUpdated == null && localLastUpdated != null){
            return Resolution.PUSH_LOCAL_TO_REMOTE;
        }
        else if (remoteLastUpdated != null && localLastUpdated == null){
            return Resolution.PUSH_REMOTE_TO_LOCAL;
        }
        else if (remoteLastUpdated > localLastUpdated){
            return Resolution.PUSH_REMOTE_TO_LOCAL;
        }
        else if (remoteLastUpdated < localLastUpdated){
            return Resolution.PUSH_LOCAL_TO_REMOTE;
        }
        else if (remoteLastUpdated.equals(localLastUpdated)){
            if (context.getLocalJson().equals(context.getRemoteJson())){
                Log.i(TAG, "Everything is up to date.");
                return Resolution.DO_NOTHING;
            }
            else {
                Log.w(TAG, "Timestamps match, but JSONs are different. Corruption or hack?");
                return Resolution.DO_NOTHING;
            }
        }
        else {
            Log.wtf(TAG, "What kind of state is this?!");
            return Resolution.DO_NOTHING;
        }
    }

    protected Long getLocalTimestamp(SyncContext context) {
        return (Long) context.getLocalValues().get(LAST_UPDATE_KEY);
    }

    protected Long getRemoteTimestamp(SyncContext context) {
        return (Long) context.getRemoteValues().get(LAST_UPDATE_KEY);
    }
}
