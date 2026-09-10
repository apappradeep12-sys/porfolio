package com.example.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebPortfolioView(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  var webViewRef by remember { mutableStateOf<WebView?>(null) }
  var canGoBack by remember { mutableStateOf(false) }
  var canGoForward by remember { mutableStateOf(false) }
  var progress by remember { mutableFloatStateOf(0f) }
  var isLoading by remember { mutableStateOf(true) }

  Column(modifier = modifier.fillMaxSize().testTag("web_portfolio_view")) {
    // Web Toolbar controls
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 6.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
      )
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = { webViewRef?.goBack() },
          enabled = canGoBack,
          modifier = Modifier.testTag("btn_web_back")
        ) {
          Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = if (canGoBack) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
          )
        }

        IconButton(
          onClick = { webViewRef?.goForward() },
          enabled = canGoForward,
          modifier = Modifier.testTag("btn_web_forward")
        ) {
          Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Forward",
            tint = if (canGoForward) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
          )
        }

        IconButton(
          onClick = { webViewRef?.reload() },
          modifier = Modifier.testTag("btn_web_refresh")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Refresh",
            tint = MaterialTheme.colorScheme.primary
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
          text = "portfolio.html (Responsive HTML5/CSS3/JS)",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.weight(1f)
        )

        IconButton(
          onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("file:///android_asset/portfolio.html"))
            try {
              context.startActivity(intent)
            } catch (_: Exception) {
              // Internal asset fallback
            }
          },
          modifier = Modifier.testTag("btn_web_open_external")
        ) {
          Icon(
            imageVector = Icons.Default.OpenInBrowser,
            contentDescription = "Open Browser",
            tint = MaterialTheme.colorScheme.primary
          )
        }
      }
    }

    if (isLoading && progress < 1f) {
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier.fillMaxWidth()
      )
    }

    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
      AndroidView(
        factory = { ctx ->
          WebView(ctx).apply {
            layoutParams = ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT
            )

            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.builtInZoomControls = true
            settings.displayZoomControls = false

            webChromeClient = object : WebChromeClient() {
              override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progress = newProgress / 100f
                isLoading = newProgress < 100
              }
            }

            webViewClient = object : WebViewClient() {
              override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                isLoading = true
                canGoBack = view?.canGoBack() ?: false
                canGoForward = view?.canGoForward() ?: false
              }

              override fun onPageFinished(view: WebView?, url: String?) {
                isLoading = false
                canGoBack = view?.canGoBack() ?: false
                canGoForward = view?.canGoForward() ?: false
              }

              override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return false
                if (url.startsWith("tel:")) {
                  val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                  ctx.startActivity(dialIntent)
                  return true
                } else if (url.startsWith("mailto:")) {
                  val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
                  ctx.startActivity(emailIntent)
                  return true
                }
                return false
              }
            }

            loadUrl("file:///android_asset/portfolio.html")
            webViewRef = this
          }
        },
        update = { webView ->
          canGoBack = webView.canGoBack()
          canGoForward = webView.canGoForward()
        },
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}
