package com.example.campuskonnekt.ui.screens.main.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campuskonnekt.data.model.StudyGroup
import com.example.campuskonnekt.ui.viewmodel.StudyGroupsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyGroupsScreen(viewModel: StudyGroupsViewModel = viewModel()) {
    var showCreateGroup by remember { mutableStateOf(false) }
    val groupsState by viewModel.groupsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Groups") },
                actions = {
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateGroup = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Group")
            }
        }
    ) { padding ->
        if (groupsState.isLoading && groupsState.groups.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(groupsState.groups) { group ->
                    StudyGroupCard(group = group, onJoinClick = { viewModel.joinGroup(group.id) })
                }
            }
        }
    }

    if (showCreateGroup) {
        CreateStudyGroupDialog(
            onDismiss = { showCreateGroup = false },
            onCreate = { courseName, courseCode, meetingTime, location, description ->
                viewModel.createStudyGroup(courseName, courseCode, meetingTime, location, description)
                showCreateGroup = false
            }
        )
    }
}

@Composable
fun StudyGroupCard(group: StudyGroup, onJoinClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = group.courseName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = group.courseCode,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.People,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${group.memberIds.size}", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = group.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = group.meetingTime, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = group.location, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onJoinClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Join Group")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStudyGroupDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, String, String, String) -> Unit
) {
    var courseName by remember { mutableStateOf("") }
    var courseCode by remember { mutableStateOf("") }
    var meetingTime by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Study Group") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = courseName,
                    onValueChange = { courseName = it },
                    label = { Text("Course Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = courseCode,
                    onValueChange = { courseCode = it },
                    label = { Text("Course Code (e.g., CS 101)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = meetingTime,
                    onValueChange = { meetingTime = it },
                    label = { Text("Meeting Time (e.g., Mon 6 PM)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (courseName.isNotBlank() && courseCode.isNotBlank() && meetingTime.isNotBlank() && location.isNotBlank() && description.isNotBlank()) {
                        onCreate(courseName, courseCode, meetingTime, location, description)
                    }
                },
                enabled = courseName.isNotBlank() && courseCode.isNotBlank() && meetingTime.isNotBlank() && location.isNotBlank() && description.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}