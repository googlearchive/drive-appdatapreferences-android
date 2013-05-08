package com.google.drive.appdatapreferences;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Initializes an {@code AppdataSyncerAdapter} instance and manages its
 * life cycle.
 *
 * @author jbd@google.com (Burcu Dogan)
 */
public class AppdataSyncerService extends Service {

  private static AppdataSyncerAdapter sAppdataSyncerAdapter;

  /**
   * Initializes a new sync adapter on creation.
   */
  @Override
  public void onCreate() {
    synchronized (lock) {
      if (sAppdataSyncerAdapter == null) {
        sAppdataSyncerAdapter =
            new AppdataSyncerAdapter(getApplicationContext(), true);
      }
    }
  }

  @Override
  public IBinder onBind(Intent arg) {
    return sAppdataSyncerAdapter.getSyncAdapterBinder();
  }

  final static private Object lock = new Object();

}
