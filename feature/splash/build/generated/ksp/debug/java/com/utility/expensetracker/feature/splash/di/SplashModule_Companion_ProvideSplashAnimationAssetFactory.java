package com.utility.expensetracker.feature.splash.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("com.utility.expensetracker.feature.splash.di.SplashAnimationAsset")
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
public final class SplashModule_Companion_ProvideSplashAnimationAssetFactory implements Factory<String> {
  @Override
  public String get() {
    return provideSplashAnimationAsset();
  }

  public static SplashModule_Companion_ProvideSplashAnimationAssetFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static String provideSplashAnimationAsset() {
    return Preconditions.checkNotNullFromProvides(SplashModule.Companion.provideSplashAnimationAsset());
  }

  private static final class InstanceHolder {
    private static final SplashModule_Companion_ProvideSplashAnimationAssetFactory INSTANCE = new SplashModule_Companion_ProvideSplashAnimationAssetFactory();
  }
}
