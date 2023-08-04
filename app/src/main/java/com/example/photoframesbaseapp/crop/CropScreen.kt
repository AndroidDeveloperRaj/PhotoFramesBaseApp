package com.example.photoframesbaseapp.crop

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.photoframesbaseapp.BuildConfig
import com.example.photoframesbaseapp.R
import com.example.photoframesbaseapp.share.findActivity
import com.example.photoframesbaseapp.ui.theme.Pink80
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun CropScreen(navHostController: NavHostController) {
    cropContent()
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun cropContent() {

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri?>(Uri.EMPTY)
    }
    val imageCropIntent =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val extras: Bundle? = it.data?.extras
                val selectedBitmap = extras?.getParcelable<Bitmap>("data")
                if (selectedBitmap != null) {
                    val updatedImageUri = getImageUri(context, selectedBitmap)
                    capturedImageUri = updatedImageUri
                } else {
                    val errorMessage = "oops - your device doesn't support the crop action!"
                    val toast =
                        Toast.makeText(context.findActivity(), errorMessage, Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
        }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                capturedImageUri = uri
                val intent = getIntent(context.findActivity()!!, capturedImageUri)
                intent?.let {
                    val pendIntent = PendingIntent.getActivity(context.findActivity()!!, 0, it, 0)
                    imageCropIntent.launch(
                        IntentSenderRequest.Builder(pendIntent)
                            .build()
                    )
                }
            }
        }
    val storageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            if (it!=null) {
                capturedImageUri = it
                val intent = getIntent(context.findActivity()!!, capturedImageUri)
                intent?.let {
                    val pendIntent = PendingIntent.getActivity(context.findActivity()!!, 0, it, 0)
                    imageCropIntent.launch(
                        IntentSenderRequest.Builder(pendIntent)
                            .build()
                    )
                }
            }
        }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
           // cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Button(onClick = {
            val permissionCheckResult =
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(uri)
            } else {
                // Request a permission
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }) {
            Text(text = "Capture Image From Camera")
        }

        Button(onClick = {
            val permissionCheckResult =
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                storageLauncher.launch("image/*")
            } else {
                // Request a permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }) {
            Text(text = "Capture Image From Device")
        }
        if (capturedImageUri?.path?.isNotEmpty() == true) {
            Row(
                modifier = Modifier
                    .clickable {
                        val intent = getIntent(context.findActivity()!!, capturedImageUri)
                        intent?.let {
                            val pendIntent =
                                PendingIntent.getActivity(context.findActivity(), 0, it, 0)
                            imageCropIntent.launch(
                                IntentSenderRequest
                                    .Builder(pendIntent)
                                    .build()
                            )
                        }
                    }
                    .padding(top = 5.dp)
                    .background(color = Pink80, shape = RoundedCornerShape(5.dp))
            ) {
                Text(text = "EDIT IMAGE", textAlign = TextAlign.Center, color = Color.White, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 5.dp))
                Image(
                    painterResource(R.drawable.edit),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .align(alignment = Alignment.CenterVertically),
                )
            }
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 20.dp, bottom = 20.dp),
                painter = rememberImagePainter(capturedImageUri),
                contentDescription = ""
            )
        }
    }
}

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}

private fun getIntent(activity: Activity, uri: Uri?): Intent? {
    try {
        val cropIntent = Intent("com.android.camera.action.CROP")
        cropIntent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        cropIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        cropIntent.setDataAndType(uri, "image/*")
        cropIntent.putExtra("crop", true)
        cropIntent.putExtra("aspectX", 1)
        cropIntent.putExtra("aspectY", 1)
        cropIntent.putExtra("outputX", 128)
        cropIntent.putExtra("outputY", 128)
        cropIntent.putExtra("return-data", true)
        return cropIntent
    }
    catch (anfe: ActivityNotFoundException) {
        // display an error message
        val errorMessage = "oops - your device doesn't support the crop action!"
        val toast = Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT)
        toast.show()
        return null
    }
}

private fun getImageUri(context: Context, inImage: Bitmap?): Uri? {
    val bytes = ByteArrayOutputStream()
    inImage?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path =
        MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
    return Uri.parse(path)
}