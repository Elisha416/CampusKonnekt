package com.example.campuskonnekt.ui.screens.main.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campuskonnekt.data.model.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen() {
    var showCreatePost by remember { mutableStateOf(false) }

    // Sample data - will be replaced with Firebase data
    val posts = remember {
        listOf(
            Post(id = "1", authorName = "John Doe", authorMajor = "Computer Science", timestamp = System.currentTimeMillis() - 7200000, content = "Just finished my final project! Anyone else done with theirs?", likes = List(24){ ""}, commentCount = 5),
            Post(id = "2", authorName = "Jane Smith", authorMajor = "Business", timestamp = System.currentTimeMillis() - 18000000, content = "Looking for study partners for Economics 101. Hit me up!", likes = List(15){ ""}, commentCount = 8),
            Post(id = "3", authorName = "Mike Johnson", authorMajor = "Engineering", timestamp = System.currentTimeMillis() - 86400000, content = "The new library study rooms are amazing! Highly recommend booking one.", likes = List(45){ ""}, commentCount = 12)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Campus Feed") },
                actions = {
                    IconButton(onClick = { /* TODO: Notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreatePost = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Post")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(posts) { post ->
                PostCard(post)
            }
        }
    }

    if (showCreatePost) {
        CreatePostDialog(onDismiss = { showCreatePost = false })
    }
}

@Composable
fun PostCard(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Author info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = post.authorName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${post.authorMajor} • ${post.timestamp}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Post content
            Text(text = post.content)

            Spacer(modifier = Modifier.height(12.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    IconButton(onClick = { /* TODO: Like */ }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Like")
                    }
                    Text(
                        text = "${post.likes.size}",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Row {
                    IconButton(onClick = { /* TODO: Comment */ }) {
                        Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Comment")
                    }
                    Text(
                        text = "${post.commentCount}",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                IconButton(onClick = { /* TODO: Share */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostDialog(onDismiss: () -> Unit) {
    var postContent by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Post") },
        text = {
            OutlinedTextField(
                value = postContent,
                onValueChange = { postContent = it },
                placeholder = { Text("What's on your mind?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 5
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    // TODO: Save post to Firebase
                    onDismiss()
                }
            ) {
                Text("Post")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}