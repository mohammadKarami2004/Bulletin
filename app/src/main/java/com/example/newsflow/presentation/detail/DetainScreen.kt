package com.example.newsflow.presentation.detail

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.newsflow.service.ArticleDownloadService
import com.example.newsflow.ui.theme.NewsFlowTheme

@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onBookmarkClick: () -> Unit = {}
) {
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onBookmarkClick) {
                Icon(
                    imageVector = if (uiState.isBookmarked)
                        Icons.Outlined.Bookmark
                    else
                        Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark"
                )
            }
        }
    ) { paddingValues ->

        if (uiState.isLoading) {
            CircularProgressIndicator()
            return@Scaffold
        }

        uiState.article?.let { article ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = article.urlToImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )

                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.height(8.dp))

                    article.author?.let {
                        Text(
                            text = "By $it",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Text(
                        text = article.publishedAt,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(Modifier.height(16.dp))

                    article.description?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    article.content?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Button(onClick = {
                    val intent = Intent(context, ArticleDownloadService::class.java).apply {
                        putExtra("url", uiState.article?.url)
                    }
                    context.startService(intent)
                }) {
                    Text("دانلود مقاله")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDetailScreen() {
    NewsFlowTheme {
        DetailScreen(uiState = DetailUiState())
    }
}