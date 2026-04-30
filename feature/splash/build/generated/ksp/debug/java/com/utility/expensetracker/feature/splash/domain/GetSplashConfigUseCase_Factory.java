package com.utility.expensetracker.feature.splash.domain;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "cast"
})
public final class GetSplashConfigUseCase_Factory implements Factory<GetSplashConfigUseCase> {
  private final Provider<SplashRepository> repositoryProvider;

  public GetSplashConfigUseCase_Factory(Provider<SplashRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetSplashConfigUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetSplashConfigUseCase_Factory create(
      Provider<SplashRepository> repositoryProvider) {
    return new GetSplashConfigUseCase_Factory(repositoryProvider);
  }

  public static GetSplashConfigUseCase newInstance(SplashRepository repository) {
    return new GetSplashConfigUseCase(repository);
  }
}
