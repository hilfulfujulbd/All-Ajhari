package org.hilfulfujul.allajhari.ads

import android.app.Activity
import android.content.Context
import android.widget.LinearLayout
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import org.hilfulfujul.allajhari.setting.CountTimer
import org.hilfulfujul.allajhari.setting.CountTimer.SET_TIMEOUT_ADS_ON_AFTER

class AdMobHelper {
    companion object {
        const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
        var TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        var mAdView: AdView? = null

        // Banner Ad Loading
        fun loadBannerAd(
            context: Context, adContainer: LinearLayout, adUnitId: String = TEST_BANNER_AD_UNIT_ID
        ) {
            mAdView = AdView(context)
            mAdView?.setAdSize(AdSize.BANNER)
            mAdView?.adUnitId = adUnitId

            adContainer.removeAllViews()
            adContainer.addView(mAdView)

            val adRequest = AdRequest.Builder().build()
            mAdView?.loadAd(adRequest)
        }

        // Interstitial Ad Loading
        private lateinit var interstitialAd: InterstitialAd

        fun loadInterstitialAd(context: Context, adUnitId: String = TEST_INTERSTITIAL_AD_UNIT_ID) {

            InterstitialAd.load(context,
                adUnitId,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(loadedInterstitialAd: InterstitialAd) {
                        interstitialAd = loadedInterstitialAd
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Handle ad loading failure
                    }
                })
        }

        fun isLoaded(): Boolean {
            return ::interstitialAd.isInitialized
        }

        fun showInterstitialAd(context: Activity) { // Requires activity context
            if (isLoaded()) {
                interstitialAd.show(context)
                interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        CountTimer.startTimer(SET_TIMEOUT_ADS_ON_AFTER, listener = object : CountTimer.FinishedCount {
                            override fun onCountFinish() {
                                loadInterstitialAd(context)
                            }
                        })
                    }

                    override fun onAdFailedToShowFullScreenContent(error: AdError) {
                        // Called when the ad fails to show
                    }
                }
            }
        }

        fun onPause() {
            mAdView?.pause()
        }

        fun onResume() {
            mAdView?.resume()
        }

        fun onDestroy() {
            mAdView?.destroy()
        }
    }
}