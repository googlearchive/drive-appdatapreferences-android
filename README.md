# AppdataPreferences for Android SDK

`appdatapreferences-android` seamlessly syncs your Android application's preferences with a remote JSON file on [Google Drive's Application Data folder](https://developers.google.com/drive/appdata). Local changes are pushed to the remote file and the remote file changes are periodically polled. Periodic synchonization introduces a sync adapter, based on the existing account synchornization APIs of the Android SDK.

The sample below illustrates to how bind a Google account to a shared preferences instance. Once you're all set, sharedPreferences key/values will be synced to user's Google Drive, under your application's folder.

~~~~~ java
syncer = AppdataPreferencesSyncer.get(context);
syncer.bind(googleAccountCredential, sharedPreferences);
~~~~~

## What can I do with AppdataPreferences?

Some of the common use cases you can easily implement by using this library are:
* Backing up your application preferences on the cloud.
* Sharing application preferences and application state among user's different machines.
* Distributing content or altering application behavior by modifying user's preferences files.

### Configure

In order to get started, you need to follow the steps below for the initial configuration.
* Setup Google APIs access for your Android application.
* Add the background service that will initiate the synchornization and syncer provider to your AndroidManifest.xml.
* Configure sync adapter settings.

#### Google APIs Access
Go to [APIs console](https://code.google.com/apis/console) and create a project if you haven't already. On the "API Access" tab, create a new client ID for installed apps and select Android. Provide your package name and certificate finger print. You can extract SHA1 footprint by executing the following command:

    $ keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore -list -v

Our Android quickstart explains the flow more in detail on  [Google Developers](https://developers.google.com/drive/quickstart-android).

#### Add Sync to your AndroidManifest.xml

Add the background service and the syncer provider to your `AndroidManifest.xml` file.

~~~~~ xml
<application>
    ...
    <!-- appdatapreferences -->
    <service
        android:name="com.google.drive.appdatapreferences.AppdataSyncerService"
        android:exported="false" >
        <intent-filter>
            <action android:name="android.content.SyncAdapter" />
        </intent-filter>
        <meta-data
            android:name="android.content.SyncAdapter"
            android:resource="@xml/syncadapter" />
    </service>
    <provider
        android:name="com.google.drive.appdatapreferences.AppdataPreferencesProvider"
        android:authorities="com.google.drive.appdatapreferences"
        android:exported="false" >
        <grant-uri-permission android:pathPattern=".*" />
    </provider>
    <!-- end of appdata preferences -->
~~~~~

#### Configure Adapter Settings
        
Create an new XML resource (`@xml/syncadapter` ), and modify it to configure your sync adapter settings. A sample adapter is below:

~~~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<sync-adapter xmlns:android="http://schemas.android.com/apk/res/android"
    android:contentAuthority="com.google.drive.appdatapreferences" 
    android:accountType="com.google"
    android:userVisible="true"
    android:isAlwaysSyncable="true"
    android:supportsUploading="true" />
~~~~~

### Authorize and Authenticate
Authorization and authentication is handled by Google Play Service's auth modules. To learn the basics, please read the [official Android SDK documentaion for Google Play Services](http://developer.android.com/reference/com/google/android/gms/auth/GoogleAuthUtil.html).

Initialize a `GoogleAccountCredential` with drive.appdata scope and let user to pick a Google Account to use.

~~~~~ java
GoogleAccountCredential credential =
    GoogleAccountCredential.usingOAuth2(this, "https://www.googleapis.com/auth/drive.appdata");
credential.setSelectedAccountName(getGoogleAccountName());
~~~~~

### Synchronize

Once you configure you application to use the sync library, as explained in the Configuration section, you need to bind an account to a shared preferences instance:

~~~~~ java
AppdataPreferences preferences =
    AppdataPreferences.get(getApplicationContext());
syncer.bind(googleAccountCredential, sharedPreferences);
~~~~~

Use `syncer.sync` to force sync. It's useful to retrieve the remote preferences initially when application first launches.

~~~~~ java
syncer.sync();
~~~~~

Listen remote changes by setting syncer an `onChangeListener`.

~~~~~ java
syncer.setOnChangeListener(new OnChangeListener() {
  @Override
  public void onChange(SharedPreferences prefs) {
    // preferences are changed
  }
});
~~~~~

If background service recieves authorization errors, you can listen them by setting a `OnUserRecoverableAuthExceptionListener`.

~~~~~ java
syncer.setOnUserRecoverableAuthExceptionListener(new OnUserRecoverableAuthExceptionListener(){
  @Override
  public void onUserRecoverableAuthException(final UserRecoverableAuthIOException e) {
    // show user a notification to ask for permissions again.
  }
});
~~~~~

### Manage Synchronization

Start/stop synchnonization manually.

~~~~~ java
  syncer.getSyncManager().startSync();
  syncer.getSyncManager().stopSync();
  syncer.getSyncManager().startPeriodicSync(); // reschedules syncer
~~~~~

A working sample application is available on [appdatapreferences-android-quickstart](https://github.com/googledrive/appdatapreferences-android-quickstart).

## License

Copyright 2013 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

