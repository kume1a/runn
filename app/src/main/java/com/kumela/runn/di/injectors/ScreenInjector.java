package com.kumela.runn.di.injectors;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.bluelinelabs.conductor.Controller;
import com.kumela.dialogcontroller.DialogController;
import com.kumela.runn.core.base.BaseActivity;
import com.kumela.runn.core.base.BaseController;
import com.kumela.runn.di.annotations.ActivityScope;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.android.AndroidInjector;

@ActivityScope
public class ScreenInjector {

    private final Map<Class<? extends Controller>, Provider<AndroidInjector.Factory<? extends Controller>>> screenInjectors;
    private final Map<String, AndroidInjector<Controller>> cache = new HashMap<>();

    @Inject
    public ScreenInjector(Map<Class<? extends Controller>, Provider<AndroidInjector.Factory<? extends Controller>>> screenInjectors) {
        this.screenInjectors = screenInjectors;
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    void inject(Controller controller) {
        if (!(controller instanceof BaseController)) {
            throw new IllegalArgumentException("controller must extend " + BaseController.class.getSimpleName());
        }

        String instanceId = controller.getInstanceId();
        if (cache.containsKey(instanceId)) {
            cache.get(instanceId).inject(controller);
            return;
        }

        AndroidInjector.Factory<Controller> injectorFactory =
                (AndroidInjector.Factory<Controller>) screenInjectors.get(controller.getClass()).get();
        AndroidInjector<Controller> injector = injectorFactory.create(controller);

        cache.put(instanceId, injector);
        injector.inject(controller);
    }

    void clear(Controller controller) {
        if (!(controller instanceof BaseController) && !(controller instanceof DialogController)) {
            throw new IllegalArgumentException("controller must extend " + BaseController.class.getSimpleName());
        }

        cache.remove(controller.getInstanceId());
    }

    @NonNull
    static ScreenInjector get(Activity activity) {
        if (!(activity instanceof BaseActivity)) {
            throw new IllegalArgumentException("controller must be hosted by " + BaseActivity.class.getSimpleName());
        }

        return ((BaseActivity) activity).getScreenInjector();
    }

}
