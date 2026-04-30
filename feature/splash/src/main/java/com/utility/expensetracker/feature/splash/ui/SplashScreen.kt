package com.utility.expensetracker.feature.splash.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.utility.expensetracker.feature.splash.R
import com.utility.expensetracker.feature.splash.presentation.SplashError
import com.utility.expensetracker.feature.splash.presentation.SplashUiState
import com.utility.expensetracker.feature.splash.presentation.SplashViewModel
import com.utility.expensetracker.feature.splash.ui.theme.SplashPreviewTheme

/**
 * Splash screen composable with Lottie animation
 *
 * @param onSplashComplete Callback invoked when splash is complete
 * @param animationAsset Path to the Lottie animation asset
 * @param modifier Modifier for styling
 * @param viewModel ViewModel for managing splash state
 */
@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit,
    animationAsset: String = "splash_animation.json",
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val animationProgress by viewModel.animationProgress.collectAsState()

    // Handle navigation when splash is complete
    LaunchedEffect(uiState) {
        if (uiState is SplashUiState.Complete) {
            onSplashComplete()
        }
    }

    SplashContent(
        uiState = uiState,
        animationProgress = animationProgress,
        animationAsset = animationAsset,
        modifier = modifier,
    )
}

/**
 * Stateless splash screen content
 *
 * @param uiState Current UI state of the splash screen
 * @param animationProgress Progress of the animation (0f to 1f)
 * @param animationAsset Path to the Lottie animation asset
 * @param modifier Modifier for styling
 */
@Composable
internal fun SplashContent(
    uiState: SplashUiState,
    animationProgress: Float,
    animationAsset: String = "splash_animation.json",
    modifier: Modifier = Modifier,
) {
    val loadingDescription = stringResource(R.string.splash_loading_description)
    val animationDescription = stringResource(R.string.splash_animation_description)
    val errorMessage = stringResource(R.string.splash_error_loading)

    // Load the Lottie animation
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(animationAsset),
    )

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.systemBars)
                .semantics {
                    contentDescription = loadingDescription
                },
        contentAlignment = Alignment.Center,
    ) {
        when (uiState) {
            is SplashUiState.Loading -> {
                LottieAnimation(
                    composition = composition,
                    progress = { animationProgress },
                    modifier =
                        Modifier
                            .size(200.dp)
                            .semantics {
                                contentDescription = animationDescription
                            },
                )
            }
            is SplashUiState.Complete -> {
                LottieAnimation(
                    composition = composition,
                    progress = { 1f },
                    modifier =
                        Modifier
                            .size(200.dp)
                            .semantics {
                                contentDescription = animationDescription
                            },
                )
            }
            is SplashUiState.Error -> {
                val specificErrorMessage = stringResource(uiState.error.messageRes)
                val errorContentDescription = stringResource(R.string.splash_error_content_description)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier =
                        Modifier
                            .padding(horizontal = 32.dp)
                            .semantics {
                                contentDescription = errorContentDescription
                            },
                ) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = specificErrorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

/**
 * Preview parameter provider for different splash states
 */
private class SplashUiStateProvider : PreviewParameterProvider<SplashUiState> {
    override val values =
        sequenceOf(
            SplashUiState.Loading,
            SplashUiState.Complete,
            SplashUiState.Error(SplashError.NetworkConnection),
        )
}

// Light mode previews
@Preview(
    name = "Splash States - Light",
    showBackground = true,
    backgroundColor = 0xFFFFFBFE,
)
@Composable
private fun SplashContentLightPreview(
    @PreviewParameter(SplashUiStateProvider::class) uiState: SplashUiState,
) {
    SplashPreviewTheme(darkTheme = false) {
        SplashContent(
            uiState = uiState,
            animationProgress =
                when (uiState) {
                    is SplashUiState.Loading -> 0.5f
                    is SplashUiState.Complete -> 1f
                    is SplashUiState.Error -> 0f
                },
        )
    }
}

// Dark mode previews
@Preview(
    name = "Splash States - Dark",
    showBackground = true,
    backgroundColor = 0xFF1C1B1F,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun SplashContentDarkPreview(
    @PreviewParameter(SplashUiStateProvider::class) uiState: SplashUiState,
) {
    SplashPreviewTheme(darkTheme = true) {
        SplashContent(
            uiState = uiState,
            animationProgress =
                when (uiState) {
                    is SplashUiState.Loading -> 0.7f
                    is SplashUiState.Complete -> 1f
                    is SplashUiState.Error -> 0f
                },
        )
    }
}

// Additional specific state previews for better testing
@Preview(
    name = "Loading State - Light",
    showBackground = true,
    group = "Splash States",
)
@Composable
private fun SplashLoadingLightPreview() {
    SplashPreviewTheme(darkTheme = false) {
        SplashContent(
            uiState = SplashUiState.Loading,
            animationProgress = 0.3f,
        )
    }
}

@Preview(
    name = "Loading State - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    group = "Splash States",
)
@Composable
private fun SplashLoadingDarkPreview() {
    SplashPreviewTheme(darkTheme = true) {
        SplashContent(
            uiState = SplashUiState.Loading,
            animationProgress = 0.8f,
        )
    }
}

@Preview(
    name = "Error State - Light",
    showBackground = true,
    group = "Splash States",
)
@Composable
private fun SplashErrorLightPreview() {
    SplashPreviewTheme(darkTheme = false) {
        SplashContent(
            uiState = SplashUiState.Error(SplashError.AnimationLoad),
            animationProgress = 0f,
        )
    }
}

@Preview(
    name = "Error State - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    group = "Splash States",
)
@Composable
private fun SplashErrorDarkPreview() {
    SplashPreviewTheme(darkTheme = true) {
        SplashContent(
            uiState = SplashUiState.Error(SplashError.NetworkTimeout),
            animationProgress = 0f,
        )
    }
}
