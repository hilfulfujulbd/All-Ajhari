package org.hilfulfujul.allajhari.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.hilfulfujul.allajhari.databinding.FragmentWebViewBinding


class WebViewFragment : Fragment() {

    private lateinit var binding: FragmentWebViewBinding
    private val args: WebViewFragmentArgs by navArgs()

    private lateinit var webView: WebView
    private lateinit var progressbar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = args.url

        if (url.isEmpty()) {
            binding.chapterNonPublished.visibility = View.VISIBLE
        } else {
            binding.chapterNonPublished.visibility = View.GONE
        }

        val appCompatActivity = (requireActivity() as AppCompatActivity)
        appCompatActivity.supportActionBar?.title = args.title

        webView = binding.webView
        progressbar = binding.progressCircular

        val myWebViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressbar.visibility = View.VISIBLE // Show progress bar
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressbar.visibility = View.GONE // Hide progress bar
            }

            override fun onReceivedError(
                view: WebView?, request: WebResourceRequest?, error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                progressbar.visibility = View.GONE

                if (error?.errorCode == 404) {
                    view?.loadUrl("file:///android_asset/error/404.html")
                } else {
                    view?.loadUrl("file:///android_asset/error/error.html")
                }
            }
        }

        webView.settings.javaScriptEnabled = true
        webView.isVerticalScrollBarEnabled = false

        webView.webViewClient = myWebViewClient

        /** TEXT COPY FALSE */
        webView.setOnLongClickListener { true }
        webView.isLongClickable = false
        webView.isHapticFeedbackEnabled = false

        webView.setBackgroundColor(Color.TRANSPARENT)


        // webView.setBackgroundColor(resources.getColor(R.color.transparent))
        // webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)

        webView.loadUrl(url)


    }
}