package org.hilfulfujul.allajhari.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import org.hilfulfujul.allajhari.repository.AdsRepository

class AdsViewModel : ViewModel() {
    private val adsRepository: AdsRepository = AdsRepository()

    val bannerAds = adsRepository.getBannerAds().asLiveData()

    val interstitial = adsRepository.getInterstitialAds().asLiveData()
}