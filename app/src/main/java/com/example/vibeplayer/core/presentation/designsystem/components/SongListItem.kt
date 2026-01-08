package com.example.vibeplayer.core.presentation.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.vibeplayer.core.domain.model.Song
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.accent
import com.example.vibeplayer.core.presentation.designsystem.theme.bodyMediumRegular
import com.example.vibeplayer.core.util.applyMarquee
import com.example.vibeplayer.core.util.toMinutesSeconds
import com.example.vibeplayer.feature.songlist.PreviewDataSource

@Composable
fun SongListItem(
    song: Song,
    onSongClickedAction: (Long) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clickable(onClick = {
                onSongClickedAction(song.songId)
            })
            .padding(vertical = 12.dp)
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SubcomposeAsyncImage(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentDescription = null,
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.albumArtUri)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            error = {
                DefaultSongIcon()
            },
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier.padding(10.dp),
                    color = MaterialTheme.colorScheme.accent
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 12.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.applyMarquee(),
                text = song.title,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                modifier = Modifier.applyMarquee(),
                text = song.artist,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMediumRegular
            )
        }

        Text(
            text = song.durationMs.toMinutesSeconds(),
            style = MaterialTheme.typography.bodyMediumRegular,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SongListItemPreview() {
    VibePlayerTheme {
        SongListItem(PreviewDataSource.previewSongList[0])
    }
}