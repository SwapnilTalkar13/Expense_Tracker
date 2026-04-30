package com.utility.expensetracker.feature.splash.di;

import com.utility.expensetracker.feature.splash.model.SplashConfig;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class SplashModule_Companion_ProvideSplashConfigFactory implements Factory<SplashConfig> {
  @Override
  public SplashConfig get() {
    return provideSplashConfig();
  }

  public static SplashModule_Companion_ProvideSplashConfigFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SplashConfig provideSplashConfig() {
    return Preconditions.checkNotNullFromProvides(SplashModule.Companion.provideSplashConfig());
  }

  private static final class InstanceHolder {
    static final SplashModule_Companion_ProvideSplashConfigFactory INSTANCE = new SplashModule_Companion_ProvideSplashConfigFactory();
  }
}
