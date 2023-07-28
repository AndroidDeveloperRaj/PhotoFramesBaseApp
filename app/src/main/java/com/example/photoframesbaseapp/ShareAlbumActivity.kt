package com.example.photoframesbaseapp

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
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
import com.example.photoframesbaseapp.share.ShareAlbumViewModel
import com.example.photoframesbaseapp.ui.theme.PhotoFramesBaseAppTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class ShareAlbumActivity : ComponentActivity() {

    private val viewModel: ShareAlbumViewModel by viewModels()
    var mInterstitialAd: InterstitialAd? = null

    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadInterstitial(this)
        setContent {
            PhotoFramesBaseAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScaffoldWithTopBar()
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScaffoldWithTopBar() {
        val context = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.shareImage),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {finish()}) {
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
                    Column(
                        modifier = Modifier
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
                        horizontalAlignment = Alignment.End
                    ) {
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

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp, end = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start)
                    {
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
                            modifier = Modifier.background(color = Color.LightGray, shape = RectangleShape),
                            textAlign = TextAlign.End
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = { context ->
                            AdView(context).apply {
                                setAdSize(AdSize.BANNER)
                                adUnitId = "ca-app-pub-3940256099942544/6300978111"
                                loadAd(AdRequest.Builder().build())
                                adListener = object : AdListener() {
                                    override fun onAdLoaded() {
                                        Log.d("exe","onAdLoaded ")
                                    }

                                    override fun onAdFailedToLoad(p0: LoadAdError) {
                                        super.onAdFailedToLoad(p0)
                                        Log.d("exe","onAdFailedToLoad "+p0.responseInfo)
                                    }
                                }
                            }
                        }
                    )
                }
            })
    }

    fun loadInterstitial(context: Context) {
        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712",
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    Log.d("exe","adError "+adError.responseInfo)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    showInterstitial(context)
                    Log.d("exe","onAdLoaded "+interstitialAd.responseInfo)
                }
            }
        )
    }

    fun showInterstitial(context: Context) {
        val activity = context.findActivity()
        if (mInterstitialAd != null&&activity!=null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdFailedToShowFullScreenContent(e: AdError) {
                    mInterstitialAd = null
                }

                override fun onAdDismissedFullScreenContent() {
                    mInterstitialAd = null
                }
            }
            mInterstitialAd?.show(activity)
        }
    }

    fun removeInterstitial() {
        mInterstitialAd?.fullScreenContentCallback = null
        mInterstitialAd = null
    }

    fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }

    override fun onDestroy() {
        super.onDestroy()
        removeInterstitial()
    }
}
