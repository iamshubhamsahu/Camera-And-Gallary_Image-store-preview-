package com.example.on_rental_app.android

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.on_rental_app.Greeting
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    GreetingView("Android")
                }
            }
        }
    }
}

private fun mToast(context: Context) {
    Toast.makeText(context, "Image", Toast.LENGTH_SHORT).show()
}

@NonRestartableComposable
@OptIn(ExperimentalPermissionsApi::class, ExperimentalCoilApi::class)
@SuppressLint("PrivateResource", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GreetingView(text: String) {
    var description by remember { mutableStateOf("") }
    val mcontext = LocalContext.current
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
            bitmap = it
        }
    var selectImage by remember { mutableStateOf(listOf<Uri>()) }
    val galleryLaunch =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            selectImage = it
        }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(106.dp),
                backgroundColor = Color.White,
                title = { Text(text = "Add Post") },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = "Icon"
                        )
                    }
                },
                contentColor = Color.Black,
                elevation = 50.dp,
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)

        ) {
            Row(verticalAlignment = Alignment.Top) {

                bitmap?.let {
                    Image(
                        bitmap = bitmap?.asImageBitmap()!!,
                        contentDescription = "",
                        modifier = Modifier.size(75.dp).padding(9.dp)
                    )
                }
                LazyRow{


                    items(selectImage) { uri ->
                        Image(
                            painter = rememberImagePainter(uri),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .padding(9.dp)
                                .size(75.dp)
                        )
                    }
                }
                Image(
                    modifier = Modifier
                        .size(75.dp)
                        .padding(9.dp)
                        .clickable {
                            if (cameraPermissionState.status.isGranted) {
                                launcher.launch()
                            } else {
                                cameraPermissionState.launchPermissionRequest()
                            }
                            mToast(mcontext)
                        },
                    painter = painterResource(id = R.drawable.ic_image_upload),
                    contentDescription = "Upload Image Icon",
                )
                Image(
                    modifier = Modifier
                        .size(75.dp)
                        .padding(9.dp)
                        .clickable {

                            galleryLaunch.launch("image/*")
                            mToast(mcontext)
                        },
                    painter = painterResource(id = R.drawable.ic_image_gallary),
                    contentDescription = "Upload Image Icon",
                )
            }
            Text(text = "Description*")
            TextField(
                value = description,
                onValueChange = { description = it },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
            )
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
