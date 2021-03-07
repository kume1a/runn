package com.kumela.runn.di.common;

import com.kumela.runn.core.lifecycle.ScreenLifecycleTask;
import com.kumela.runn.di.annotations.ScreenScope;

import java.util.Set;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.Multibinds;

@Module
public abstract class ScreenModule {

    @Multibinds
    abstract Set<ScreenLifecycleTask> screenLifecycleTaskSet();
}
