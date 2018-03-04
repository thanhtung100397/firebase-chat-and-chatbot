package com.ttt.chat_module;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.os.Bundle;

/**
 * Created by Admin on 7/7/2017.
 */

public class AppStatusApplication extends Application implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {
    public static final int ON_CREATE = 0;
    public static final int ON_START = 1;
    public static final int ON_PAUSE = 2;
    public static final int ON_RESUME = 3;
    public static final int ON_STOP = 4;
    public static final int ON_DESTROY = 5;
    public static int stateOfLifecycle;
    public static boolean wasInBackground = false;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        stateOfLifecycle = ON_CREATE;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        stateOfLifecycle = ON_START;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        stateOfLifecycle = ON_RESUME;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        stateOfLifecycle = ON_PAUSE;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        stateOfLifecycle = ON_STOP;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        wasInBackground = false;
        stateOfLifecycle = ON_DESTROY;
    }

    @Override
    public void onTrimMemory(int level) {
//        if (stateOfLifecycle == ON_STOP) {
//            wasInBackground = true;
//            String userKey = ActiveUser.getAccount().getKey();
//            if(userKey != null) {
//                FirebaseDatabaseHelper.updateUserOnlineState(userKey, false, new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Toast.makeText(AppStatusApplication.this, "Offline", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        }
        super.onTrimMemory(level);
    }
}
