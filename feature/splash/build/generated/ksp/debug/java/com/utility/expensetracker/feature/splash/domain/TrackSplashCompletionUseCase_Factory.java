package com.utility.expensetracker.feature.splash.domain;

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
public final class TrackSplashCompletionUseCase_Factory implements Factory<TrackSplashCompletionUseCase> {
  private final Provider<SplashRepository> repositoryProvider;

  private TrackSplashCompletionUseCase_Factory(Provider<SplashRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public TrackSplashCompletionUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static TrackSplashCompletionUseCase_Factory create(
      Provider<SplashRepository> repositoryProvider) {
    return new TrackSplashCompletionUseCase_Factory(repositoryProvider);
  }

  public static TrackSplashCompletionUseCase newInstance(SplashRepository repository) {
    return new TrackSplashCompletionUseCase(repository);
  }
}
