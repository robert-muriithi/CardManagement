package com.robert.cards.cards.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.robert.domain.models.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TasksTopBar(
    pendingCount: Int,
    userProfile: UserProfile,
    onLogout: () -> Unit
) {
    TopAppBar(
        title = {
            Column(modifier = Modifier.testTag("tasksTopBarTitle")) {
                Text("Hi, ${userProfile.firstName}", style = MaterialTheme.typography.titleMedium)
            }
        },
        actions = {
            CircularImage(
                imageUrl = userProfile.avatarUrl.orEmpty(),
                size = 40.dp
            )
        },
        modifier = Modifier.testTag("tasksTopBar")
    )
}

@Composable
fun CircularImage(imageUrl: String, size: Dp = 100.dp) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Profile",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
    )
}

