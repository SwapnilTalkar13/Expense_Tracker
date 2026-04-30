package com.utility.expensetracker.feature.splash.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("com.utility.expensetracker.feature.splash.di.SplashMinDisplayTime")
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
    "cast"
})
public final class SplashModule_Companion_ProvideSplashMinDisplayTimeFactory implements Factory<Long> {
  @Override
  public Long get() {
    return provideSplashMinDisplayTime();
  }

  public static SplashModule_Companion_ProvideSplashMinDisplayTimeFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static long provideSplashMinDisplayTime() {
    return SplashModule.Companion.provideSplashMinDisplayTime();
  }

  private static final class InstanceHolder {
    private static final SplashModule_Companion_ProvideSplashMinDisplayTimeFactory INSTANCE = new SplashModule_Companion_ProvideSplashMinDisplayTimeFactory();
  }
}
