package com.google.drive.appdatapreferences;

import java.util.Map;

/**
 * An interface to let multiple sync strategies be defined.
 *
 * @author Alejandro Rivera (alejandro.rivera.lopez@gmail.com)
 * @since 2014-05-26 17:04
 */
public interface AppdataSyncStrategy {

    public enum Resolution {
        PUSH_LOCAL_TO_REMOTE,
        PUSH_REMOTE_TO_LOCAL,
        DO_NOTHING
    }

    public Resolution getSyncResolution(SyncContext context);


    public static final class SyncContext {
        private String lastSyncedJson;
        private Map<String, Object> lastSyncedValues;

        private String remoteJson;
        private Map<String, Object> remoteValues;

        private String localJson;
        private Map<String, ?> localValues;

        public SyncContext(String lastSyncedJson, String remoteJson, Map<String,
                Object> remoteValues, String localJson, Map<String, ?> localValues) {
            this.lastSyncedJson = lastSyncedJson;
            this.remoteJson = remoteJson;
            this.remoteValues = remoteValues;
            this.localJson = localJson;
            this.localValues = localValues;
        }

        public String getLastSyncedJson() {
            return lastSyncedJson;
        }

        public void setLastSyncedJson(String lastSyncedJson) {
            this.lastSyncedJson = lastSyncedJson;
        }

        public Map<String, Object> getLastSyncedValues() {
            return lastSyncedValues;
        }

        public void setLastSyncedValues(Map<String, Object> lastSyncedValues) {
            this.lastSyncedValues = lastSyncedValues;
        }

        public String getRemoteJson() {
            return remoteJson;
        }

        public void setRemoteJson(String remoteJson) {
            this.remoteJson = remoteJson;
        }

        public Map<String, Object> getRemoteValues() {
            return remoteValues;
        }

        public void setRemoteValues(Map<String, Object> remoteValues) {
            this.remoteValues = remoteValues;
        }

        public String getLocalJson() {
            return localJson;
        }

        public void setLocalJson(String localJson) {
            this.localJson = localJson;
        }

        public Map<String, ?> getLocalValues() {
            return localValues;
        }

        public void setLocalValues(Map<String, Object> localValues) {
            this.localValues = localValues;
        }
    }

}
