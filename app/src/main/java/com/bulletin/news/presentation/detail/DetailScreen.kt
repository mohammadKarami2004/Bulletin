package com.bulletin.news.presentation.detail

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bulletin.news.ui.theme.Spacing
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.core.net.toUri

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailScreen(
    uiState: DetailUiState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBackClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onShareClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val article = uiState.article

    // انیمیشن bounce ساده: با هر تغییر isBookmarked، آیکون یه لحظه بزرگ می‌شه
    // و با یه spring برمی‌گرده سایز عادی. اولین بار که صفحه باز می‌شه بونس نمی‌زنه
    // (isInitial)، فقط وقتی کاربر واقعاً کلیک می‌کنه.
    val bookmarkScale = remember { Animatable(1f) }
    var isInitialBookmarkState by remember { mutableStateOf(true) }

    LaunchedEffect(uiState.isBookmarked) {
        if (isInitialBookmarkState) {
            isInitialBookmarkState = false
            return@LaunchedEffect
        }
        bookmarkScale.animateTo(1.35f, animationSpec = tween(120))
        bookmarkScale.animateTo(
            1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onBookmarkClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = if (uiState.isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = if (uiState.isBookmarked) "Remove bookmark" else "Save article",
                    modifier = Modifier.scale(bookmarkScale.value)
                )
            }
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Box {
                with(sharedTransitionScope) {
                    AsyncImage(
                        model = article.urlToImage,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .sharedElement(
                                rememberSharedContentState(key = "article-image-${article.url}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .background(
                            Brush.verticalGradient(
                                0f to Color.Black.copy(alpha = 0.15f),
                                1f to MaterialTheme.colorScheme.background
                            )
                        )
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(Spacing.md),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CircleIconButton(Icons.Filled.ArrowBack, "Back", onBackClick)
                    CircleIconButton(Icons.Outlined.Share, "Share") {
                        onShareClick(article.url)
                    }
                }

                Text(
                    text = article.source,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(Spacing.md)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = Spacing.sm, vertical = 4.dp)
                )
            }

            Column(Modifier.padding(Spacing.md)) {
                with(sharedTransitionScope) {
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.sharedBounds(
                            rememberSharedContentState(key = "article-title-${article.url}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                }

                Spacer(Modifier.height(Spacing.sm))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    article.author?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        formatDate(article.publishedAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                HorizontalDivider(Modifier.padding(vertical = Spacing.md))

                article.description?.let {
                    Text(it, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(Spacing.sm))
                }

                article.content?.let {
                    Text(
                        it.substringBefore("[+"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(Spacing.md))

// NewsAPI (پلن رایگان) content رو تا ۲۰۰ کاراکتر truncate می‌کنه،
// پس تنها راه خوندن متن کامل، باز کردن خود لینک خبره.
                OutlinedButton(
                    onClick = {
                        CustomTabsIntent.Builder()
                            .build()
                            .launchUrl(context, article.url.toUri())
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Outlined.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(Spacing.xs))
                    Text("Read full article")
                }
                Spacer(Modifier.height(Spacing.xxl))
            }
        }
    }
}

@Composable
private fun CircleIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.35f))
    ) {
        Icon(icon, contentDescription = contentDescription, tint = Color.White)
    }
}

fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        dateString
    }
}