package com.google.drive.appdatapreferences;

import android.os.Bundle;
import android.util.Log;

public class ADPLog {

    public static final String LOG_VERBOSE = "log_verbose";
    public static final String LOG_DEBUG = "log_debug";
    public static final String LOG_INFO = "log_info";
    public static final String LOG_WARN = "log_warn";
    public static final String LOG_ERROR = "log_error";

    private static ADPLog INSTANCE = new ADPLog();
    private Bundle configs = new Bundle();

    private ADPLog() {};

    static ADPLog getInstance()
    {
        return INSTANCE;
    }

    public void setBundle(Bundle b)
    {
        this.configs = b;
    }


    public static void v(java.lang.String tag, java.lang.String msg) {
        if(INSTANCE.configs.getBoolean(LOG_VERBOSE,false))
        {
            Log.v(tag,msg);
        }
    }

    public static void v(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        if(INSTANCE.configs.getBoolean(LOG_VERBOSE,false))
        {
            Log.v(tag,msg);
        }
    }

    public static void d(java.lang.String tag, java.lang.String msg) {
        if(INSTANCE.configs.getBoolean(LOG_DEBUG,false))
        {
            Log.d(tag,msg);
        }
    }

    public static void d(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr)  {
        if(INSTANCE.configs.getBoolean(LOG_DEBUG,false))
        {
            Log.d(tag,msg);
        }
    }

    public static void i(java.lang.String tag, java.lang.String msg) {
        if(INSTANCE.configs.getBoolean(LOG_INFO,false))
        {
            Log.i(tag,msg);
        }
    }

    public static void i(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        if(INSTANCE.configs.getBoolean(LOG_INFO,false))
        {
            Log.i(tag,msg);
        }
    }

    public static void w(java.lang.String tag, java.lang.String msg) {
        if(INSTANCE.configs.getBoolean(LOG_WARN,false))
        {
            Log.w(tag,msg);
        }
    }

    public static void w(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr)  {
        if(INSTANCE.configs.getBoolean(LOG_WARN,false))
        {
            Log.w(tag,msg);
        }
    }

    public static void w(java.lang.String tag, java.lang.Throwable tr) {
        if(INSTANCE.configs.getBoolean(LOG_WARN,false))
        {
            Log.w(tag,tr);
        }
    }

    public static void e(java.lang.String tag, java.lang.String msg) {
        if(INSTANCE.configs.getBoolean(LOG_ERROR,true))
        {
            Log.e(tag,msg);
        }
    }

    public static void e(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        if(INSTANCE.configs.getBoolean(LOG_ERROR,true))
        {
            Log.e(tag,msg);
        }
    }


}
