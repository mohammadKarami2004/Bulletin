package com.example.newsflow.presentation.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.newsflow.domain.model.Article
import com.example.newsflow.ui.theme.NewsFlowTheme

@Composable
fun BookmarkScreen(
    uiState: BookmarkUiState,
    onDelete: (Article) -> Unit
){
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()

                .padding(10.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(25.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Top Articles", fontWeight = FontWeight.Bold)
            Row(
                Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
            ) {
                Icon(Icons.Outlined.Notifications, contentDescription = "Notification")
                Spacer(Modifier.width(10.dp))
                Icon(Icons.Outlined.Search, contentDescription = "Search")
            }

        }

        //form
        LazyColumn(Modifier.fillMaxSize()) {
            items(uiState.news) { article ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(article.urlToImage, contentDescription = "Image")
                    Text(article.title)
                    IconButton(onClick = { onDelete(article) }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Delete")
                    }
                }
            }
        }

    }

}



@Preview(showBackground = true)
@Composable
fun BookmarkScreenPreview(){
    NewsFlowTheme {
        BookmarkScreen(uiState = BookmarkUiState(), onDelete = {})
    }
}