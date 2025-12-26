package com.example.vibeplayer.feature.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.vibeplayer.core.domain.model.Song
import com.example.vibeplayer.core.presentation.designsystem.theme.ShadowColor
import com.example.vibeplayer.core.presentation.designsystem.theme.SurfaceOutline
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerIcons.ArrowUp
import com.example.vibeplayer.feature.main.MainActions
import kotlinx.coroutines.launch

@Composable
fun SongList(
    songs: List<Song>,
    onAction: (MainActions) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val showScrollToTopButton by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex >= 10
        }
    }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp),
            state = lazyListState
        ) {
            itemsIndexed(
                items = songs,
                key = { _, song -> song.id }
            ) { index, song ->
                MainListItem(song){
                    onAction(MainActions.PlaySong(it))
                }
                if (index < songs.lastIndex) {
                    HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                }
            }
        }
        AnimatedVisibility(
            visible = showScrollToTopButton,
            enter = scaleIn(transformOrigin = TransformOrigin(1f, 1f)),
            exit = scaleOut(transformOrigin = TransformOrigin(1f, 1f))
        ) {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        lazyListState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 12.dp, bottom = 12.dp)
                    .wrapContentSize(Alignment.BottomEnd)
                    .dropShadow(
                        shape = RoundedCornerShape(100.dp),
                        shadow = Shadow(
                            radius = 8.dp,
                            color = ShadowColor,
                            spread = 2.dp,
                            offset = DpOffset(y = 2.dp, x = 0.dp),
                        )
                    ),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = ArrowUp,
                    contentDescription = "Scroll to top"
                )
            }
        }
    }
}
