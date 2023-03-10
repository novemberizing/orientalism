package net.novemberizing.orientalism;

import android.app.Application;
import android.util.Log;

public class OrientalismApplication extends Application {
    public static final String Tag = "OrientalismApplication";
    @Override
    public void onCreate(){
        super.onCreate();

        Log.e(OrientalismApplication.Tag, "onCreate()");

        OrientalismApplicationDB.gen(this);
    }

    @Override
    public void onTerminate(){
        super.onTerminate();

        OrientalismApplicationDB.rem();

        Log.e(OrientalismApplication.Tag, "onTerminate()");
    }
}
