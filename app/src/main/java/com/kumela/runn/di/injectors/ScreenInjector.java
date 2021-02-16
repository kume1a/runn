package com.kumela.runn.di.injectors;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.bluelinelabs.conductor.Controller;
import com.kumela.runn.core.base.BaseActivity;
import com.kumela.runn.core.base.BaseController;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.android.AndroidInjector;

public class ScreenInjector {

    private final Map<Class<? extends Controller>, Provider<AndroidInjector.Factory<? extends Controller>>> screenInjectors;
    private final Map<String, AndroidInjector<? extends Controller>> cache = new HashMap<>();

    @Inject
    public ScreenInjector(Map<Class<? extends Controller>, Provider<AndroidInjector.Factory<? extends Controller>>> screenInjectors) {
        this.screenInjectors = screenInjectors;
    }

    @SuppressWarnings("unchecked")
    public void inject(@NonNull Controller controller) {
        checkControllerType(controller);

        String instanceId = controller.getInstanceId();
        if (cache.containsKey(instanceId)) {
            ((AndroidInjector<Controller>) cache.get(instanceId)).inject(controller);
            return;
        }

        AndroidInjector.Factory<Controller> injectorFactory =
                ((AndroidInjector.Factory<Controller>) screenInjectors.get(controller.getClass()).get());
        AndroidInjector<Controller> injector = injectorFactory.create(controller);
        cache.put(instanceId, injector);
        injector.inject(controller);
    }

    public void clear(@NonNull Controller controller) {
        checkControllerType(controller);
        cache.remove(controller.getInstanceId());
    }

    private void checkControllerType(@NonNull Controller controller) {
        if (!(controller instanceof BaseController)) {
            throw new IllegalArgumentException(controller.getClass().getSimpleName() + " must extend " + BaseController.class.getSimpleName());
        }
    }

    @NonNull
    public static ScreenInjector get(@NonNull Activity activity) {
        if (!(activity instanceof BaseActivity)) {
            throw new IllegalArgumentException("controller must be hosted from " + BaseActivity.class.getSimpleName());
        }
        return (((BaseActivity) activity)).getScreenInjector();
    }

}
