package com.google.drive.appdatapreferences;

import android.os.Bundle;
import android.util.Log;

public class ADPLog {

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
        if(INSTANCE.configs.getBoolean("log_verbose",false))
        {
            Log.v(tag,msg);
        }
    }

    public static void v(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        if(INSTANCE.configs.getBoolean("log_verbose",false))
        {
            Log.v(tag,msg);
        }
    }

    public static void d(java.lang.String tag, java.lang.String msg) {
        if(INSTANCE.configs.getBoolean("log_debug",false))
        {
            Log.d(tag,msg);
        }
    }

    public static void d(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr)  {
        if(INSTANCE.configs.getBoolean("log_debug",false))
        {
            Log.d(tag,msg);
        }
    }

    public static void i(java.lang.String tag, java.lang.String msg) {
        if(INSTANCE.configs.getBoolean("log_info",false))
        {
            Log.i(tag,msg);
        }
    }

    public static void i(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        if(INSTANCE.configs.getBoolean("log_info",false))
        {
            Log.i(tag,msg);
        }
    }

    public static void w(java.lang.String tag, java.lang.String msg) {
        if(INSTANCE.configs.getBoolean("log_warn",false))
        {
            Log.w(tag,msg);
        }
    }

    public static void w(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr)  {
        if(INSTANCE.configs.getBoolean("log_warn",false))
        {
            Log.w(tag,msg);
        }
    }

    public static void w(java.lang.String tag, java.lang.Throwable tr) {
        if(INSTANCE.configs.getBoolean("log_warn",false))
        {
            Log.w(tag,tr);
        }
    }

    public static void e(java.lang.String tag, java.lang.String msg) {
        if(INSTANCE.configs.getBoolean("log_error",true))
        {
            Log.e(tag,msg);
        }
    }

    public static void e(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        if(INSTANCE.configs.getBoolean("log_error",true))
        {
            Log.e(tag,msg);
        }
    }


}
