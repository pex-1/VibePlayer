package com.example.vibeplayer.feature.nowplaying

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.vibeplayer.core.playback.PlaybackState
import com.example.vibeplayer.core.presentation.designsystem.components.DefaultSongIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.accent
import com.example.vibeplayer.core.presentation.designsystem.theme.bodyMediumRegular
import com.example.vibeplayer.core.util.applyMarquee
import com.example.vibeplayer.feature.nowplaying.components.NowPlayingBottomBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NowPlayingScreenRoot(
    viewModel: NowPlayingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NowPlayingScreen(state) { action ->
        viewModel.onAction(action)
    }
}

@Composable
fun NowPlayingScreen(
    state: PlaybackState = PlaybackState(),
    onAction: (NowPlayingActions) -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .padding(horizontal = 46.dp)
                    .heightIn(max = 320.dp)
                    .widthIn(max = 320.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp)),
                contentDescription = null,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(state.artUri)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                error = {
                    DefaultSongIcon()
                },
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(60.dp),
                        color = MaterialTheme.colorScheme.accent
                    )
                }
            )

            Text(
                modifier = Modifier
                    .applyMarquee()
                    .padding(top = 28.dp, bottom = 4.dp, start = 24.dp, end = 24.dp)
                    .widthIn(max = 400.dp),
                text = state.title,
                maxLines = 1,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                modifier = Modifier
                    .applyMarquee()
                    .padding(horizontal = 24.dp)
                    .widthIn(max = 400.dp),
                text = state.artist,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMediumRegular
            )

        }
        NowPlayingBottomBar(state = state, onAction = onAction)
    }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingScreenPreview() {
    VibePlayerTheme {
        NowPlayingScreen(PlaybackState(title = "Title", artist = "Artist"))
    }
}