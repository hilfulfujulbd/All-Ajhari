package org.hilfulfujul.allajhari.ads

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
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
        const val BANNER_AD_UNIT_ID = "ca-app-pub-5980068077636654/2912337209"
        var INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-5980068077636654/7985372931"
        var mAdView: AdView? = null

        // Banner Ad Loading
        fun loadBannerAd(
            context: Context, adContainer: LinearLayout, adUnitId: String = BANNER_AD_UNIT_ID
        ) {
            mAdView = AdView(context)
            mAdView?.setAdSize(AdSize.BANNER)
            mAdView?.adUnitId = adUnitId

            adContainer.removeAllViews()
            adContainer.addView(mAdView)

            val adRequest = AdRequest.Builder().build()
            mAdView?.loadAd(adRequest)

            mAdView?.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    adContainer.visibility = View.VISIBLE
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    adContainer.visibility = View.GONE
                }
            }
        }

        // Interstitial Ad Loading
        private var mInterstitialAd: InterstitialAd? = null

        fun loadInterstitialAd(context: Context, adUnitId: String = INTERSTITIAL_AD_UNIT_ID) {
            if (!isLoaded()) {
                InterstitialAd.load(context,
                    adUnitId,
                    AdRequest.Builder().build(),
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            mInterstitialAd = null
                            CountTimer.startTimer(SET_TIMEOUT_ADS_ON_AFTER,
                                listener = object : CountTimer.FinishedCount {
                                    override fun onCountFinish() {
                                        loadInterstitialAd(context)
                                    }
                                })
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            mInterstitialAd = interstitialAd
                        }
                    })
            }
        }

        fun isLoaded(): Boolean {
            return mInterstitialAd != null
        }

        fun showInterstitialAd(context: Activity) { // Requires activity context
            if (isLoaded()) {

                mInterstitialAd?.show(context)


                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        mInterstitialAd = null

                        CountTimer.startTimer(
                            SET_TIMEOUT_ADS_ON_AFTER,
                            listener = object : CountTimer.FinishedCount {
                                override fun onCountFinish() {
                                    loadInterstitialAd(context)
                                }
                            })
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        super.onAdFailedToShowFullScreenContent(adError)
                        mInterstitialAd = null
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