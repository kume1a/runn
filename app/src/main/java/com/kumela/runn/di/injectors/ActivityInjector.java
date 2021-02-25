package com.kumela.runn.di.injectors;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.kumela.runn.core.base.BaseActivity;
import com.kumela.runn.core.base.BaseApplication;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.android.AndroidInjector;

public class ActivityInjector {

    private final Map<Class<? extends Activity>, Provider<AndroidInjector.Factory<? extends Activity>>> activityInjectors;
    private final Map<String, AndroidInjector<? extends Activity>> cache = new HashMap<>();

    @Inject
    ActivityInjector(Map<Class<? extends Activity>, Provider<AndroidInjector.Factory<? extends Activity>>> activityInjectors) {
        this.activityInjectors = activityInjectors;
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    void inject(@NonNull Activity activity) {
        if (!(activity instanceof BaseActivity)) {
            throw new IllegalArgumentException("activity must extend " + BaseActivity.class.getSimpleName());
        }

        String instanceId = ((BaseActivity) activity).getInstanceId();
        if (cache.containsKey(instanceId)) {
            ((AndroidInjector<Activity>) cache.get(instanceId)).inject(activity);
            return;
        }

        AndroidInjector.Factory<Activity> injectorFactory =
                (AndroidInjector.Factory<Activity>) activityInjectors.get(activity.getClass()).get();
        AndroidInjector<Activity> injector = injectorFactory.create(activity);

        cache.put(instanceId, injector);
        injector.inject(activity);
    }

    void clear(@NonNull Activity activity) {
        if (!(activity instanceof BaseActivity)) {
            throw new IllegalArgumentException("activity must extend " + BaseActivity.class.getSimpleName());
        }

        cache.remove(((BaseActivity) activity).getInstanceId());
    }

    @NonNull
    static ActivityInjector get(@NotNull Context context) {
        return ((BaseApplication) context.getApplicationContext()).getActivityInjector();
    }

}
