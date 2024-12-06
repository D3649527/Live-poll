package uk.ac.tees.mad.livepoll.presentation.ui

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import uk.ac.tees.mad.livepoll.presentation.viewmodel.PollViewModel

@Composable
fun ProfileScreen(viewModel: PollViewModel, navController: NavHostController) {
    val context = LocalContext.current

     val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { imageBitmap ->
            if (imageBitmap != null) {
                // Handle the captured image (e.g., upload to server or save locally)
                // Example: viewModel.uploadProfileImage(imageBitmap)
            }
        }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }



    viewModel.fetchUserData()
    val user by viewModel.user

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Profile", style = MaterialTheme.typography.h6)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardDoubleArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Profile image or placeholder
            if (user?.profileImage?.isNotEmpty() == true) {
                AsyncImage(
                    model = user!!.profileImage,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(75.dp))
                        .clickable {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Profile Placeholder",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(75.dp))
                        .clickable {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = user?.name ?: "Name not available", style = MaterialTheme.typography.h6)
            Text(text = user?.email ?: "Email not available", style = MaterialTheme.typography.body1)
        }
    }
}
