package com.utility.expensetracker.feature.splash.data;

import com.utility.expensetracker.feature.splash.model.SplashConfig;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
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
public final class SplashRepositoryImpl_Factory implements Factory<SplashRepositoryImpl> {
  private final Provider<SplashConfig> defaultConfigProvider;

  private SplashRepositoryImpl_Factory(Provider<SplashConfig> defaultConfigProvider) {
    this.defaultConfigProvider = defaultConfigProvider;
  }

  @Override
  public SplashRepositoryImpl get() {
    return newInstance(defaultConfigProvider.get());
  }

  public static SplashRepositoryImpl_Factory create(Provider<SplashConfig> defaultConfigProvider) {
    return new SplashRepositoryImpl_Factory(defaultConfigProvider);
  }

  public static SplashRepositoryImpl newInstance(SplashConfig defaultConfig) {
    return new SplashRepositoryImpl(defaultConfig);
  }
}
