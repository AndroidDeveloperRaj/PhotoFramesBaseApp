package com.example.photoframesbaseapp.albums

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.photoframesbaseapp.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.shareImage),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    // Is this required ?
                }) {
                    Icon(Icons.Filled.ArrowBack, "backIcon", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.Blue,
                titleContentColor = Color.White,
            ),
        )
    }, content = {
        Column(
            modifier = Modifier
                .padding(it)
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Column(modifier = Modifier
                .clickable {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Description of Image")
                        putExtra(Intent.EXTRA_STREAM, R.drawable.good_night)
                        type = "image/*"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
                .padding(top = 5.dp, end = 20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End) {
                Image(
                    painter = painterResource(id = R.drawable.share),
                    modifier = Modifier.size(width = 50.dp, height = 40.dp),
                    contentDescription = stringResource(id = R.string.shareImage),
                )
                Text(
                    text = stringResource(id = R.string.share),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            Image(
                painter = painterResource(id = R.drawable.good_night),
                contentDescription = stringResource(id = R.string.shareImage),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 50.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(id = R.string.ad),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 5.dp, end = 5.dp)
                        .background(color = Color.Blue, shape = RectangleShape),
                    textAlign = TextAlign.Start
                )

                Text(
                    text = stringResource(id = R.string.moreAppsFromDeveloper),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(start = 2.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.more),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Green,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.background(
                        color = Color.LightGray, shape = RectangleShape
                    ),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            AndroidView(modifier = Modifier.fillMaxWidth(), factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                    loadAd(AdRequest.Builder().build())
                    adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            Log.d("exe", "onAdLoaded ")
                        }

                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            super.onAdFailedToLoad(p0)
                            Log.d("exe", "onAdFailedToLoad " + p0.responseInfo)
                        }
                    }
                }
            })
        }
    })
}